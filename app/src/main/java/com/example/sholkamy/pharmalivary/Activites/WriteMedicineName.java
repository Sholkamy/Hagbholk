package com.example.sholkamy.pharmalivary.Activites;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sholkamy.pharmalivary.Adapter.DisplayOrdersAdapter;
import com.example.sholkamy.pharmalivary.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

public class WriteMedicineName extends AppCompatActivity {


    /**
     * Views ...
     */
    private ImageButton mUpButton;
    private ImageButton mDownButton;
    private EditText mDrugEditText;
    private TextView mQuantityText;
    private Button mToNextButton;
    private Button mAddAnotherOrder;
    private ListView mDisplayOrdersListView;


    /**
     * Variables ...
     */
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private DisplayOrdersAdapter mDisplayOrdersAdapter;
    private ArrayList<String> medicine = new ArrayList<String>();
    private ArrayList<String> quantity = new ArrayList<String>();
    private int realQuantity = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_medicine_name);


        if(isServicesOK()){
            init();
        }

        mDisplayOrdersAdapter = new DisplayOrdersAdapter(this, medicine ,quantity);

        /**
         * Initialize references to views ...
         */
        mUpButton = (ImageButton) findViewById(R.id.upButton);
        mDownButton = (ImageButton) findViewById(R.id.downButton);
        mDrugEditText = (EditText) findViewById(R.id.messageEditText);
        mQuantityText = (TextView) findViewById(R.id.quantityText);
        mAddAnotherOrder = (Button) findViewById(R.id.addAnotherOrder);
        mDisplayOrdersListView = (ListView) findViewById(R.id.DisplayMedicineListView);


        /**
         * Enforce editText not to start with space.
         */
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.length()>0 &&Character.isWhitespace(source.charAt(0)) && mDrugEditText.getText().toString().length()==0) {
                    return "";
                }
                return source;
            }
        };
        mDrugEditText.setFilters(new InputFilter[] { filter });


/**
 * This method is called when the plus button is clicked.
 */
        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (realQuantity < 30) {
                    realQuantity++;
                    mQuantityText.setText("" + realQuantity);
                } else
                    return;
            }
        });


        /**
         * This method is called when the minus button is clicked.
         */
        mDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (realQuantity != 1) {
                    realQuantity--;
                    mQuantityText.setText("" + realQuantity);
                } else
                    return;
            }
        });


        /**
         * This method is called when the (Add Another Order) button is clicked.
         */
        mAddAnotherOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDrugEditText.getText().toString().length() != 0 ) {
                    medicine.add(mDrugEditText.getText().toString());
                    quantity.add(Integer.toString(realQuantity));
                    configureAddAnotherOrder();
                    mDisplayOrdersListView.setAdapter(mDisplayOrdersAdapter);
                }
                else
                    ViewMessage("Enter medicine name ...");
            }
        });
    }

    
    private void init(){
        mToNextButton = (Button) findViewById(R.id.ToNextButton);
        mToNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                medicine.add(mDrugEditText.getText().toString());
                quantity.add(Integer.toString(realQuantity));
                configureAddAnotherOrder();

                Intent intent = new Intent(WriteMedicineName.this, MapActivity.class);
                intent.putExtra("Medicine_ArrayList", medicine);
                intent.putExtra("Quantity_ArrayList", quantity);
                startActivity(intent);
            }
        });
    }


    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(WriteMedicineName.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(WriteMedicineName.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    /**
     * This function configure add another order.
     */
    public void configureAddAnotherOrder() {
        mDrugEditText.setText("");
        realQuantity = 1;
        mQuantityText.setText("" + realQuantity);
    }


    private void ViewMessage(String message) {
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT).show();
    }

}
