package com.example.sholkamy.pharmalivary.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.ActionBar;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;




import com.example.sholkamy.pharmalivary.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private Toolbar mToolbar ;
    private MaterialSearchView mMaterialSearchView;
    private ListView mMedicineListView;

    String[] lstSource = {
            "بودسونيد" ,
            "ايسونيد" ,
            "حقن ديكستران 40 10%" ,
            "اقراص فيروغراديوميت" ,
            "فلودركس" ,
            "سيكلوهيرب" ,
            "ديفلات" ,
            "فلاتيديل" ,
            "يوريسباس" ,
            "كزاترال" ,
            "توبراستيل" ,
            "أوفالمولوزا كلورامفينيكول" ,
            "اكني-مايسين" ,
            "اليركسيم" ,
            "بودسونيد"
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View RootView = inflater.inflate(R.layout.fragment_news, container, false);

        mToolbar = (Toolbar) RootView.findViewById(R.id.toolbar);
        mMaterialSearchView = (MaterialSearchView) RootView.findViewById(R.id.search_view);
        mMedicineListView = (ListView) RootView.findViewById(R.id.MedicineListView);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,lstSource);
        mMedicineListView.setAdapter(adapter);
        mMaterialSearchView.setVoiceSearch(true);



        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search");
        mToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));


        mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                mMedicineListView = (ListView) RootView.findViewById(R.id.MedicineListView);
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,lstSource);
                mMedicineListView.setAdapter(adapter);

            }
        });

        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    List<String> lstFound = new ArrayList<String>();
                    for (String item : lstSource) {
                        if (item.contains(newText))
                            lstFound.add(item);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,lstFound);
                    mMedicineListView.setAdapter(adapter);
                }
                else {
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,lstSource);
                    mMedicineListView.setAdapter(adapter);
                }
                return true;
            }
        });


        return RootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mMaterialSearchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
