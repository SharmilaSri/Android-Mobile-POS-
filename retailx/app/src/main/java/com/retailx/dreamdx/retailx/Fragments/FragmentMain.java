package com.retailx.dreamdx.retailx.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import android.widget.Toast;

import com.retailx.dreamdx.retailx.Adapters.AddNonInventoryItemDialog;
import com.retailx.dreamdx.retailx.Adapters.ProductGridAdapter;
import com.retailx.dreamdx.retailx.BarcodeScanActivity;
import com.retailx.dreamdx.retailx.MainActivity;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.interfaces.NonInventoryCreatedListener;
import com.retailx.dreamdx.retailx.interfaces.ProductItemActionListener;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.util.ArrayList;

public class FragmentMain extends Fragment {

    private Context ctx;
    GridView gridview;
    int flag=0;//default for maintActivity
    android.support.v7.widget.SearchView searchView;
    ImageView scanImageView,nonInventoryProduct;
   // private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    public FragmentMain(){

    }

    public static FragmentMain newInstance(String catTitle,int flag) {
        Bundle args = new Bundle();
        args.putString(ConstantsUsed.TITLE, catTitle);
        args.putInt(ConstantsUsed.FLAG_EDIT_VIEW,flag);
        FragmentMain fragment = new FragmentMain();
        fragment.setArguments(args);
        return fragment;
    }

    int mNum;

    String title="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            this.title = arguments.getString(ConstantsUsed.TITLE, "ALL");
            this.flag=arguments.getInt(ConstantsUsed.FLAG_EDIT_VIEW,0);
        }
    }


    ProductGridAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridview = (GridView) view.findViewById(R.id.id_product_grid);

        searchView= (android.support.v7.widget.SearchView) view.findViewById(R.id.searchView);
        scanImageView = (ImageView) view.findViewById(R.id.id_scan_product);

        scanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanProductInFragment();
            }
        });

        nonInventoryProduct = (ImageView) view.findViewById(R.id.id_non_inventory_product);
        nonInventoryProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNonInventory();
            }
        });

        populateData(title);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               int textLength=newText.length();
               String tempTitle="";
                String tempPrice="";
                productListSearch.clear();

               if(productList!=null && productList.size()>=1){
                   for(int i=0;i<productList.size();i++){
                       tempTitle=productList.get(i).getProduct_title();
                       tempPrice=String.valueOf(productList.get(i).getUnit_selling_price());
                       if(textLength>0 && textLength<=tempTitle.length() && !tempTitle.equalsIgnoreCase(getContext().getResources().getString(R.string.add))){
                           if(newText.equalsIgnoreCase(tempTitle.substring(0,textLength)) || tempTitle.contains(newText.toUpperCase()) || newText.equalsIgnoreCase(tempPrice.substring(0,textLength))){
                               productListSearch.add(productList.get(i));
                           }
                       }else{
                           populateGrid("",0);
                       }


                   }

                   if(productListSearch.size()>0){
                       productListSearch.add(new Product(getContext().getResources().getString(R.string.add),
                               0.00,0.00, "LAST_IMAGE", "",0,0,"0",0.0,"0"));
                       adapter = new ProductGridAdapter(getActivity(),productListSearch ,flag);
                       gridview.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                   }
               }


                return false;
            }
        });
        if(flag==1){
            scanImageView.setVisibility(View.GONE);
            nonInventoryProduct.setVisibility(View.GONE);
        }


        return view;

    }

    private void createNonInventory(){
        AddNonInventoryItemDialog dialog = new AddNonInventoryItemDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setListener(new NonInventoryCreatedListener() {
            @Override
            public void itemAddedToCart(Product p) {
                AppFeatures.vibrate(getActivity());
                Product.setSelectedList(p,getContext());
                ((MainActivity) getActivity()).setTotalText_(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount()),
                        String.valueOf(Product.getSelectedItemCount()) + " " + "ITEMS");
            }

            @Override
            public void itemCreated(Product p) {

                populateData("ALL");

            }
        });
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.94f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.7f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);
    }

    public void scanProductInFragment() {
        MainActivity.shouldShow="false";
        SharedPreference.getInstance().save(getContext(),"shouldShow","false");
        startActivity(new Intent(getActivity(), BarcodeScanActivity.class));
    }

    ArrayList<Product> productList;
    ArrayList<Product> productListSearch=new ArrayList<>();
    public void populateGrid(String tag,int flag){

        if(Product.getProductList() !=null ) {

            productList=new ArrayList<>();
            productList=Product.getProductList();
            adapter = new ProductGridAdapter(getActivity(),productList ,flag);
            gridview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setListener(new ProductItemActionListener() {
                @Override
                public void onItemTap(ImageView imageView) {

                }
            });



            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {


                }

            });

        }
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
            populateGrid("test",flag);
        }


    }



    @Override
    public void onResume() {
        super.onResume();

        //populateData(title);
    }




}
