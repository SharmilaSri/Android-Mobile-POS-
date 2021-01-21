package com.retailx.dreamdx.retailx.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.Adapters.MyAdapter;
import com.retailx.dreamdx.retailx.Adapters.ProductListAdapter;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.util.ArrayList;

public class ProductListFragment extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    ArrayList<Product> productList;
    ProductListAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    android.support.v7.widget.SearchView searchView;


    // newInstance constructor for creating fragment with arguments
    public static ProductListFragment newInstance(int page, String title) {
        ProductListFragment fragmentFirst = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString("Title", title);
        fragmentFirst.setArguments(args);

        return fragmentFirst;
    }


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("Title");
    }

    ListView listview;
    ArrayList<Product> productListSearch=new ArrayList<>();
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        listview=view.findViewById(R.id.product_edit_list);
        searchView= (android.support.v7.widget.SearchView) view.findViewById(R.id.search_list);

        recyclerView = (RecyclerView)view. findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(getActivity(),productList,title);
        recyclerView.setAdapter(mAdapter);

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int textLength=newText.length();
                String tempTitle="";
                productListSearch.clear();

                try {

                    if (productList != null && productList.size() >= 1) {
                        for (int i = 0; i < productList.size(); i++) {
                            tempTitle = productList.get(i).getProduct_title();
                            if (textLength > 0 && textLength <= tempTitle.length() && !tempTitle.equalsIgnoreCase("ADD NEW")) {
                                if (newText.equalsIgnoreCase(tempTitle.substring(0, textLength)) || tempTitle.contains(newText.toUpperCase())) {
                                    productListSearch.add(productList.get(i));
                                }
                            } else  {
                                mAdapter = new MyAdapter(getActivity(), productList, title);
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }


                        }

                        if (productListSearch.size() > 0) {

                            productListSearch.add(new Product("ADD NEW",
                                    0.00, 0.00, "LAST_IMAGE", "", 0, 0, "0", 0.0, ""));
                            mAdapter = new MyAdapter(getActivity(), productListSearch, title);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        populateData("ALL");
    }

    private void populateList(){

        //productList=new ArrayList<>();
        productList= Product.getProductList();
        mAdapter = new MyAdapter(getActivity(),productList,title);
        recyclerView.setAdapter(mAdapter);
    }


    public void populateData(String categoryTitle) {
        DBHelper db=new DBHelper(getContext());
        Cursor productListCur = null;
        productList = new ArrayList<>();
        String userId = SharedPreference.getInstance().getValue(getContext(), Constants.USER_ID);


        try {
            if (categoryTitle.equalsIgnoreCase(ConstantsUsed.ALL)) {
                productListCur = db.getAllProductList(userId);
            } else {
                Cursor catId = db.getCatId(categoryTitle);
                String catIdTxt = "0";
                while (catId.moveToNext()) {
                    catIdTxt = catId.getString(catId.getColumnIndex((ConstantsUsed.COLUMN_CAT_ID)));
                }
                productListCur = db.getProductList(catIdTxt,userId);//id correct
            }

            if ((productListCur != null) && (productListCur.getCount() > 0)) {
                while (productListCur.moveToNext()) {

                    String product_id = productListCur.getString(productListCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_ID)));
                    String imagePath = productListCur.getString(productListCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_IMAGE_PATH)));
                    String product_title = productListCur.getString(productListCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_TITLE)));
                    double unit_selling_price = productListCur.getDouble(productListCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE)));
                    double unit_buying_price = productListCur.getDouble(productListCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_BUYING_PRICE)));
                    double stockHand=productListCur.getDouble(productListCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_STOCK_IN_HAND)));
                    double stockMin=productListCur.getDouble(productListCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_STOCK_MINIMUM)));
                    String uniType=productListCur.getString(productListCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE)));
                    String barcode=productListCur.getString(productListCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE)));

                    productList.add(new Product(product_title,
                            unit_selling_price,unit_buying_price
                            , imagePath, product_id,stockHand,stockMin,uniType,0.0,barcode));
                }
            }

            productList.add(new Product(getContext().getResources().getString(R.string.add),
                    0.00,0.00, "LAST_IMAGE", "",0,0,"0",0.0,"0"));

            Product.setProductList(productList);

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        } finally {
            if (productListCur != null)
                productListCur.close();
            populateList();
        }


    }

}
