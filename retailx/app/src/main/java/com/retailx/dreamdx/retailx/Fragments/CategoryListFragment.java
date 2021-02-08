package com.retailx.dreamdx.retailx.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.POJO.ListType;
import com.retailx.dreamdx.retailx.POJO.Category;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.util.ArrayList;

public class CategoryListFragment extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    ListView listview;

    // newInstance constructor for creating fragment with arguments
    public static CategoryListFragment newInstance(int page, String title) {
        CategoryListFragment fragmentFirst = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        listview=view.findViewById(R.id.category_edit_list);
        setUpCategorySpinner();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpCategorySpinner();
    }

    private static ArrayList<String> catList;
    DBHelper db;
    private void setUpCategorySpinner() {//dupliacted in create main ativity
        Category.clear(getContext());
        Cursor catCur = null;
        db=new DBHelper(getContext());
        String userId = SharedPreference.getInstance().getValue(getContext(), Constants.USER_ID);

        try {
            catCur = db.getAllCategoryList(userId);

                while (catCur.moveToNext()) {
                    String catTitle = catCur.getString(catCur.getColumnIndex((ConstantsUsed.COLUMN_CAT_TITLE)));
                    String catId = catCur.getString(catCur.getColumnIndex((ConstantsUsed.COLUMN_CAT_ID)));
                    String catDesc = catCur.getString(catCur.getColumnIndex((ConstantsUsed.COLUMN_CAT_DESCRIPTION)));
                    Category.addCategoryToList(new Category(catTitle, catId, catDesc));
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "setUpCategorySpinner" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        } finally {
            if(catCur!=null)
                catCur.close();

            Category.addCategoryToList(new Category("ADD NEW", "", ""));

            if(Category.getCategoryList().size()==0){
                //catList.add("NO CATEGORY AVAILABLE");
            }else{

                ListType.getInstance().setType("CATEGORY_LIST");
                ListAdapter adapter=new com.retailx.dreamdx.retailx.Adapters.ListAdapter(getActivity(),Category.getCategoryList(),"");
                listview.setAdapter(adapter);
            }

        }

    }
}
