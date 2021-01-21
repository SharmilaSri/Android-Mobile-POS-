package com.retailx.dreamdx.retailx.Fragments;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.CreateProductStockActivity;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.UtilityFunctions;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.util.ArrayList;

public class FragmentCreateProduct extends Fragment {

    private static final int RC_BARCODE_CAPTURE = 9001;
    Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    DBHelper db;
    Spinner categorySpiner;
    EditText edtTitle, editProductCode, edtSellingPrice;
    //EditText  edtSupplerCode, edtBuyingPrice, edtDesc, edtBrandCode;
    private static String txtTitle,  txtSerialBarCode;
    //private static String  txtDesc, txtBrandCode, txtSupplerCode, txtCatTitle;
    private static double txtSellingPrice = 0.00;
    //private static double txtBuyingPrice = 0.00;
    ImageView imgViewProductImage;
    private static ArrayList<String> catList;
    int flag=1;
    String currentPhotoPath="";
    String title="";
    String sellingPrice="0.0";
    String barccode="";
    String imagePath="";
    String productId="";
    Button btnScanBarCode,addCategory,saveProduct;



    public FragmentCreateProduct(){

    }

    public static FragmentCreateProduct newInstance(String catTitle,int flag) {
        Bundle args = new Bundle();
        args.putString(ConstantsUsed.TITLE, catTitle);
        args.putInt(ConstantsUsed.FLAG_EDIT_VIEW,flag);
        FragmentCreateProduct fragment = new FragmentCreateProduct();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_product, container, false);


        db = new DBHelper(getActivity());
        categorySpiner=(Spinner) view.findViewById(R.id.id_spinner_category);
        edtTitle = (EditText) view.findViewById(R.id.id_productname);
        editProductCode = (EditText)view. findViewById(R.id.id_product_barcode);
        edtSellingPrice = (EditText)view. findViewById(R.id.id_selling_price);
        categorySpiner = (Spinner)view. findViewById(R.id.id_spinner_category);
        imgViewProductImage = (ImageView)view. findViewById(R.id.id_imageCamera);
        imgViewProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateProductStockActivity) getActivity()).addImageCamera();
            }
        });

        btnScanBarCode = (Button)view. findViewById(R.id.id_barcode_drawable_right);
        btnScanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateProductStockActivity) getActivity()).scanBarCode();
            }
        });

        addCategory = (Button)view. findViewById(R.id.add_category);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateProductStockActivity) getActivity()).moveToCreateCategory();
            }
        });

        saveProduct = (Button)view. findViewById(R.id.save_product);
        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFileds();
            }
        });


        displayValuesInViews();

        setUpCategorySpinner();

        currentPhotoPath="NO_IMAGE";
        return  view;
    }


    private void  validateFileds(){
        txtTitle = "";
        txtSellingPrice = 0.0;
        txtTitle = edtTitle.getText().toString();
        //txtDesc = edtTitle.getText().toString();
        //txtBrandCode = edtBrandCode.getText().toString();
        txtSerialBarCode = editProductCode.getText().toString();
        //txtSupplerCode = edtSupplerCode.getText().toString();
        String catTitle = categorySpiner.getSelectedItem().toString();
        //txtSellingPrice=Double.parseDouble();

        if (!txtTitle.isEmpty() && !edtSellingPrice.getText().toString().isEmpty()) {

           ((CreateProductStockActivity) getActivity()).insertOrUpdateProductDetails(txtTitle,catTitle,txtTitle,edtSellingPrice.getText().toString());

            // startActivity(new Intent(this,MainActivity.class));
        } else {
            if(txtTitle.isEmpty()) {
                edtTitle.setHint("EMPTY FIELD");
                edtTitle.setHintTextColor(Color.RED);
            }
            else {
                edtSellingPrice.setHint("EMPTY FIELD");
                edtSellingPrice.setHintTextColor(Color.RED);
            }
        }
    }


    public void addBarcode(String value){
        editProductCode.setText(value);
    }

    public void setPic(String path){
        try {
            UtilityFunctions.setPicImageRounded(path,imgViewProductImage,getActivity());
        }catch (Exception e){

        }
    }

    public  void setUpCategorySpinner() {
        Cursor cur = null;
        try {
            catList = new ArrayList<>();
            catList.add("SELECT A CATEGORY");
            String userId = SharedPreference.getInstance().getValue(getContext(), Constants.USER_ID);

            cur = db.getAllCategoryList(userId);

            while (cur.moveToNext()) {
                String product_title = "";
                product_title = cur.getString(cur.getColumnIndex((ConstantsUsed.COLUMN_CAT_TITLE)));
                catList.add(product_title);
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), "setUpCategorySpinner" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        } finally {
            cur.close();
            if(catList.size()==0){
                catList.add("NO CATEGORY AVAILABLE");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_spinner_item,
                    catList);

            categorySpiner.setAdapter(adapter);

        }

    }


    private void displayValuesInViews(){

        try {
            Bundle extras = getActivity().getIntent().getExtras();
            String edit = extras.getString(ConstantsUsed.EDIT_PRODUCT_KEY,"false");

            productId = extras.getString(ConstantsUsed.PRODUCT_ID_KEY,"");

            if (!productId.isEmpty()) {
                flag=0;
                Cursor infoCur = null;

                try {
                    infoCur = db.getProductDetailsWdProductId(productId);
                    while (infoCur.moveToNext()) {
                        title = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_TITLE)));
                        sellingPrice=String.valueOf(infoCur.getDouble(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE))));
                        barccode=infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE)));
                        imagePath=infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_IMAGE_PATH)));

                    }
                }catch (Exception e){
                    // Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }finally{
                    if(infoCur!=null)
                        infoCur.close();

                    if(!title.isEmpty())
                        edtTitle.setText(title);

                    if(!sellingPrice.isEmpty())
                        edtSellingPrice.setText(sellingPrice);

                    if(!barccode.isEmpty())
                        editProductCode.setText(barccode);

                    if(!imagePath.isEmpty() && !imagePath.equalsIgnoreCase("NO_IMAGE") && imagePath!=null) {
                        UtilityFunctions.setPicImageRounded(imagePath, imgViewProductImage,getActivity());
                        currentPhotoPath=imagePath;
                    }

                }
            }
        } catch (Exception e) {
            //Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }


    }
