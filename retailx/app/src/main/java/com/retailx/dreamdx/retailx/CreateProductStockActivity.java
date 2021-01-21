package com.retailx.dreamdx.retailx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.retailx.dreamdx.retailx.Fragments.FragmentCreateProduct;
import com.retailx.dreamdx.retailx.Fragments.FragmentCreateStock;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.POJO.User;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateProductStockActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TabLayout tabLayout;
    ArrayList<String> titleList;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final int CREATCATERGORY = 888;
    int flag=1;
    public String productId="";
    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_create_product_stock);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_stock));
        //setSupportActionBar(toolbar);

        titleList = new ArrayList<>();
        titleList.add("PRODUCT");
        titleList.add("STOCK");

        attachBarcodeReaderFragment(titleList.get(0),0);

        setUpTabs();

        db=new DBHelper(this);

        try {
            Bundle extras = getIntent().getExtras();
            String edit = extras.getString(ConstantsUsed.EDIT_PRODUCT_KEY, "false");

            productId = extras.getString(ConstantsUsed.PRODUCT_ID_KEY, "");

            if (!productId.isEmpty()) {
                flag = 0;
            } else {
                flag = 1;
            }
        }catch (Exception e){

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(flag==1) {
                    startActivity(new Intent(this,MainActivity.class));
                }else if(flag==0){
                    startActivity(new Intent(this,EditProductActivity.class));
                }
                break;
        }
        return true;
    }

    FragmentCreateProduct fragment;
    FragmentCreateStock fragmentStock;
    private void attachBarcodeReaderFragment(String type,int flag) {



        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        if(type.equalsIgnoreCase(titleList.get(0))) {
            fragment = FragmentCreateProduct.newInstance(type, 0);
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }else if(type.equalsIgnoreCase(titleList.get(1))){
            fragmentStock = FragmentCreateStock.newInstance(type, 0);
            fragmentTransaction.replace(R.id.frame_container, fragmentStock);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void scanBarCode() {

        Intent intent = new Intent(this, BarcodeReaderActivity.class);
        intent.putExtra(BarcodeReaderActivity.KEY_AUTO_FOCUS, true);
        intent.putExtra(BarcodeReaderActivity.KEY_USE_FLASH, false);

        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    public void insertOrUpdateProductDetails(String txtSerialBarCode,String catTitle,String name,String price) {

        insertOrUpdate(txtSerialBarCode,catTitle,name,price,flag);
    }


    private void  insertOrUpdate(String txtSerialBarCode,String catTitle,String txtTitle,String price,int flag){


            double txtSellingPrice = Double.parseDouble(price);
            db = new DBHelper(this);
            Cursor catId=null;


            try{
                if(!catTitle.equalsIgnoreCase("SELECT A CATEGORY"))
                    catId=db.getCatId(catTitle);
                String userId = User.getInstance().getUser_id();
                String catIdTxt="0";
                if(catId!=null) {
                    while (catId.moveToNext()) {
                        catIdTxt = catId.getString(catId.getColumnIndex((ConstantsUsed.COLUMN_CAT_ID)));

                    }
                }

                if(flag==1) {
                    db.insertProductDetails(userId, userId + ConstantsUsed.MOBILE + AppFeatures.getTimeStamp(), txtTitle.toUpperCase(), "0",
                            catIdTxt, "0", txtSerialBarCode, "0", "0", txtSellingPrice,
                            0.00, "0", currentPhotoPath, 1, 1,1
                    );
                }else if(flag==0){
                    db.updateProductDetails(userId, productId, txtTitle.toUpperCase(), "0",
                            catIdTxt, "0", txtSerialBarCode, "0", "0", txtSellingPrice,
                            0.00, "0", currentPhotoPath, 1, 1,1
                    );
                }




            }catch(Exception e){
                Validator.showToast(this,e.getMessage().toString());
            }finally {
                //startActivity(new Intent(this, MainActivity.class));

            }
            // startActivity(new Intent(this,MainActivity.class));
        }


    public void moveToCreateCategory() {
        Intent i = new Intent(this, CreateCategoryActivity.class);
        startActivityForResult(i,CREATCATERGORY);
    }



    int MY_PERMISSIONS_REQUEST_CAMERA =200;
    public void addImageCamera() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(CreateProductStockActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {

                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);

                }


            } else {
                proceedAfterCameraPermission();
            }
        }else{
            proceedAfterCameraPermission();
        }

    }


    private void proceedAfterCameraPermission(){

        proceedTakingImage();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {
                fragment.setPic(currentPhotoPath);
            }catch (Exception e) {
                Toast.makeText(this, "ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == RC_BARCODE_CAPTURE && data != null && resultCode == BarcodeReaderActivity.RESULT_OK) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            fragment.addBarcode(barcode.rawValue);
        }else if(requestCode==CREATCATERGORY){
            fragment.setUpCategorySpinner();
        }
    }

    private void proceedTakingImage(){

        if (AppFeatures.checkCameraHardware(this)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    /*Uri photoURI = FileProvider.getUriForFile(this,
                            ".",
                            photoFile);*/

                    Uri photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    private void setUpTabs() {
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    selectTab(tab);
                }
                private void selectTab(TabLayout.Tab tab) {
                    attachBarcodeReaderFragment(titleList.get(tab.getPosition()),0);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }


                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

            });

            tabLayout.getTabAt(0).select();

    }
}
