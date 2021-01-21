package com.retailx.dreamdx.retailx;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.retailx.dreamdx.retailx.POJO.Category;
import com.retailx.dreamdx.retailx.POJO.CreateOrEditFlag;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import com.retailx.dreamdx.retailx.apicalls.VolleyMultipartRequest.DataPart;

import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.ImagePickDialog;
import com.retailx.dreamdx.retailx.utils.Validator;
import com.squareup.picasso.Picasso;

public class CreateProductActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 10;
    static  final int ADD_STOCK=1000;
    static final String KEY_STOCK="key_stock";
    static final String KEY_STOCK_MIN="key_stock_min";
    DBHelper db;
    TextView categoryBtn;
    TextView unitTxt;
    TextInputEditText edtTitle, edtSellingPrice;
    EditText editProductCode, edtBuyingPrice;
    //EditText  edtSupplerCode, edtBuyingPrice, edtDesc, edtBrandCode;
    private static String txtTitle,  txtSerialBarCode;
    //private static String  txtDesc, txtBrandCode, txtSupplerCode, txtCatTitle;
    private static double txtSellingPrice = 0.00;
    private static double txtBuyingPrice = 0.00;
    static ImageView imgViewProductImage;
    TextView btnAddStock;
    private static ArrayList<String> catList;
    int flag=0;
    String title="";
    String sellingPrice="0.0";
    String buyingPrice="0.0";
    String barccode="";
    String imagePath="";
    String productId="";
    double stockInHand=0.0;
    double stockMin=0.0;
    String categoryId ="";
    String category="";
    String selectedCatgory="ALL";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_create_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.create_new_product));

        db = new DBHelper(this);
        categoryBtn= findViewById(R.id.id_btn_category);
        unitTxt= findViewById(R.id.id_btn_unit);
        setUPViews();

        currentPhotoPath="NO_IMAGE";

        flag=CreateOrEditFlag.getInstance().getFlag();
        selectedCatgory= Category.getCategoryInstance().getSelectedCategory();
        if(!selectedCatgory.equalsIgnoreCase("ALL"))//if we select the category tab and click on create product button
        {
            catTitle=selectedCatgory;
            categoryBtn.setText(selectedCatgory);
        }

        displayValuesInViews();



        setUpCategorySpinner();



    }

    ArrayList<String> unitList;
    String unitType="1";//1 for unit or 0 for fraction
    public void unitClicked(View v){
        unitList=new ArrayList<>();//reset
        unitList.add(getResources().getString(R.string.unit_piece));
        unitList.add(getResources().getString(R.string.kilo_pound_inch));
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CreateProductActivity.this);
        builderSingle.setIcon(android.R.drawable.ic_menu_add);
        builderSingle.setTitle(getResources().getString(R.string.unit_title));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateProductActivity.this,
                android.R.layout.select_dialog_singlechoice,unitList);


        builderSingle.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(unitList.get(which).equalsIgnoreCase(getResources().getString(R.string.unit_piece))){
                    unitType="1";
                }else if(unitList.get(which).equalsIgnoreCase(getResources().getString(R.string.kilo_pound_inch))){
                    unitType="0";
                }
                unitTxt.setText(unitList.get(which));
            }
        });
        builderSingle.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOptionalOpen=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOptionalOpen=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return true;
    }


    private void navigationBack(){//should change
        if(flag==0) {
            startActivity(new Intent(this,MainActivity.class));
        }else if(flag==1){
            startActivity(new Intent(this,EditProduct.class));
        }
    }


    boolean isOptionalOpen=false;
    public void showOptionalDetails(View v){
        Button moreDetailsBtn=findViewById(R.id.id_btn_more_details);
        LinearLayout detailsLayout=findViewById(R.id.more_details_layout);
        if(isOptionalOpen){
            detailsLayout.setVisibility(View.GONE);
            moreDetailsBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
            isOptionalOpen=false;

        }else{
            detailsLayout.setVisibility(View.VISIBLE);
            moreDetailsBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up_black_24dp, 0);
            isOptionalOpen=true;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationBack();
                break;

            case R.id.action_delete:
                if(!productId.isEmpty()) {
                    new AlertDialog.Builder(this)
                            .setTitle(getResources().getString(R.string.confirm_delete))
                            .setMessage(getResources().getString(R.string.confirm_delete_msg))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    db.deleteProductWdId(productId);
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(R.drawable.ic_delete_black_24dp)
                            .show();

                }
                break;
        }
        return true;
    }

    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 112;
    public void openDialog(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_STORAGE);
            return;
        }

    }



    private void displayValuesInViews(){

        try {
            Bundle extras = getIntent().getExtras();

            productId = extras.getString(ConstantsUsed.PRODUCT_ID_KEY,"");

            if (!productId.isEmpty()) {
                flag=1;
                Cursor infoCur = null;
                Cursor catTileCur = null;

                try {
                    infoCur = db.getProductDetailsWdProductId(productId);
                    while (infoCur.moveToNext()) {
                        title = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_TITLE)));
                        sellingPrice=String.valueOf(infoCur.getDouble(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE))));
                        buyingPrice=String.valueOf(infoCur.getDouble(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_BUYING_PRICE))));
                        barccode=infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE)));
                        imagePath=infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_IMAGE_PATH)));
                        stockInHand=infoCur.getInt(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_STOCK_IN_HAND)));
                        categoryId =infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_CAT_ID)));
                        unitType =infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE)));

                        catTileCur = db.getCatTitle(categoryId);//id correct
                        while (catTileCur.moveToNext()) {
                            catTitle=catTileCur.getString(catTileCur.getColumnIndex((ConstantsUsed.COLUMN_CAT_TITLE)));
                        }

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

                    if(!buyingPrice.isEmpty())
                        edtBuyingPrice.setText(buyingPrice);

                    if(!barccode.isEmpty())
                        editProductCode.setText(barccode);

                    if(!catTitle.isEmpty())
                        categoryBtn.setText(catTitle);

                    if(!imagePath.isEmpty() && !imagePath.equalsIgnoreCase("NO_IMAGE") && imagePath!=null) {

                        File f = new File(imagePath);
                        Picasso.get().load(f).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                                .error(R.drawable.no_image).resize(110, 110).centerCrop()
                                .into(imgViewProductImage);
                        //imgViewProductImage.setImageBitmap(UtilityFunctions.setPicImageRounded(imagePath, imgViewProductImage,this));
                        currentPhotoPath=imagePath;
                    }

                    if(!categoryId.isEmpty() && !categoryId.equalsIgnoreCase("0")){
                       Cursor cur= db.getCatTitle(categoryId);

                        if(cur!=null) {
                            while (cur.moveToNext()) {
                                category = cur.getString(cur.getColumnIndex((ConstantsUsed.COLUMN_CAT_TITLE)));
                                categoryBtn.setText(category);
                            }
                        }
                    }

                    if(!unitType.isEmpty()) {
                        if(unitType.equalsIgnoreCase("1"))
                            unitTxt.setText("UNIT/PIECES");
                        else
                            unitTxt.setText("KILO,POUND,INCHES ETC");

                    }

                    if(stockInHand>0){
                        addStock(stockInHand);
                    }

                }
            }
        } catch (Exception e) {
            //Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }



    private void setUpCategorySpinner() {
        Cursor cur = null;
        try {
            catList = new ArrayList<>();
            //catList.add("SELECT A CATEGORY");
            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

            cur = db.getAllCategoryList(userId);

            while (cur.moveToNext()) {
                String product_title = "";
                product_title = cur.getString(cur.getColumnIndex((ConstantsUsed.COLUMN_CAT_TITLE)));
                catList.add(product_title);
            }

        } catch (Exception e) {
            Toast.makeText(this, "setUpCategorySpinner" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        } finally {
            cur.close();
            if(catList.size()==0){
               catList.add("NO CATEGORY AVAILABLE");
            }

        }

    }

    public void scanBarCode(View v) {

        Intent intent = new Intent(this, BarcodeReaderActivity.class);
        intent.putExtra(BarcodeReaderActivity.KEY_AUTO_FOCUS, true);
        intent.putExtra(BarcodeReaderActivity.KEY_USE_FLASH, false);

        startActivityForResult(intent, RC_BARCODE_CAPTURE);


    }

    public void moveToStockActivity(View v) {
        moveToStockActivity();
    }

    private void moveToStockActivity(){
        Intent stock=new Intent(CreateProductActivity.this,StockActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ConstantsUsed.EDIT_PRODUCT_KEY,ConstantsUsed.EDIT_PRODUCT_VALUE);
        extras.putString(ConstantsUsed.PRODUCT_ID_KEY,productId);
        stock.putExtras(extras);
        startActivityForResult(stock,ADD_STOCK);
    }

    private void addStock(double stock){
        btnAddStock.setText("CURRENT STOCK IS : "+String.valueOf(stock));
    }

    private void setUPViews() {
        edtTitle = findViewById(R.id.id_productname);
        //edtDesc = (EditText) findViewById(R.id.id_productdesc);
        //edtBrandCode = (EditText) findViewById(R.id.id_product_brandcode);
        editProductCode = (EditText) findViewById(R.id.id_product_barcode);
        //edtSupplerCode = (EditText) findViewById(R.id.id_supplier_code);
        edtBuyingPrice = (EditText) findViewById(R.id.id_buying_price);
        edtSellingPrice = findViewById(R.id.id_selling_price);

        //categorySpiner = (Spinner) findViewById(R.id.id_spinner_category);

        imgViewProductImage = (ImageView) findViewById(R.id.id_imageCamera);

         btnAddStock = findViewById(R.id.id_btn_add_stock);

    }

    String catTitle = "SELECT A CATEGORY";

    private void  insertOrUpdate(int flag){
        txtTitle = "";
        txtSellingPrice = 0.0;
        txtTitle = edtTitle.getText().toString();
        //txtDesc = edtTitle.getText().toString();
        //txtBrandCode = edtBrandCode.getText().toString();
        txtSerialBarCode = editProductCode.getText().toString();
        //txtSupplerCode = edtSupplerCode.getText().toString();
       // String catTitle = categorySpiner.getSelectedItem().toString();
        txtSellingPrice=0.0;

        if (!txtTitle.isEmpty() && !edtSellingPrice.getText().toString().isEmpty() /*&&
        !edtBuyingPrice.getText().toString().isEmpty()*/) {

            txtSellingPrice = Double.parseDouble(edtSellingPrice.getText().toString());

            if(!edtBuyingPrice.getText().toString().isEmpty())
                txtBuyingPrice = Double.parseDouble(edtBuyingPrice.getText().toString());
            db = new DBHelper(this);
            Cursor catId=null;


            try{
                if(!catTitle.equalsIgnoreCase("SELECT A CATEGORY") && !catTitle.equalsIgnoreCase("NO CATEGORY AVAILABLE"))
                    catId=db.getCatId(catTitle);
                String userId = SharedPreference.getInstance().getValue(this,Constants.USER_ID);
                String catIdTxt="0";
                if(catId!=null) {
                    while (catId.moveToNext()) {
                        catIdTxt = catId.getString(catId.getColumnIndex((ConstantsUsed.COLUMN_CAT_ID)));

                    }
                }

                //if(flag==0){
                if(productId.isEmpty()) {
                    productId = ConstantsUsed.PRODUCT + AppFeatures.getTimeStamp();
                    String date=AppFeatures.getTimeStamp();

                    db.insertProductDetails(userId, productId, txtTitle.toUpperCase(), "0",
                            catIdTxt, unitType, txtSerialBarCode, "0", "0", txtSellingPrice,
                            txtBuyingPrice, "0", currentPhotoPath, 1, 1,1
                    );


                    if (stockInHand > 0) {
                        db.updateProductStockInHand(productId, stockInHand);
                    }

                    if (stockMin > 0)
                        db.updateProductStockMinimum(productId, stockMin);

                }else{

                    db.updateProductDetails(userId, productId, txtTitle.toUpperCase(), "0",
                            catIdTxt, unitType, txtSerialBarCode, "0", "0", txtSellingPrice,
                            txtBuyingPrice, "0", currentPhotoPath, 1, 1,1
                    );

                }





            }catch(Exception e){
                Validator.showToast(this,e.getMessage().toString());
            }finally {
                    navigationBack();
            }
            // startActivity(new Intent(this,MainActivity.class));
        } else {
            if(txtTitle.isEmpty()) {
                edtTitle.setError(getResources().getString(R.string.empty_field));
                edtTitle.setHintTextColor(Color.RED);
            }
            else {
                edtSellingPrice.setError(getResources().getString(R.string.empty_field));
                edtSellingPrice.setHintTextColor(Color.RED);
            }
        }
    }

    public void insertOrUpdateProductDetails(View v) {

       insertOrUpdate(flag);
    }




    int createcategory=888;
    public void moveToCreateCategory() {
        Intent i = new Intent(this, CreateCategoryActivity.class);
        startActivityForResult(i,createcategory);
    }


    int MY_PERMISSIONS_REQUEST_CAMERA =200;
    public void addImageCamera(View v) {

        openDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    proceedAfterCameraPermission();
                } else {
                    Toast.makeText(this,"Cannot open Camera",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case 112:{
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ImagePickDialog dialog=new ImagePickDialog(this);
                        dialog.show();
                    } else {
                        Toast.makeText(this,"Cannot open Gallery",Toast.LENGTH_SHORT).show();
                    }
                    return;
            }




            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private void proceedAfterCameraPermission(){

        proceedTakingImage();

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

   static  String currentPhotoPath;

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


        if (requestCode == RC_BARCODE_CAPTURE && data != null && resultCode == BarcodeReaderActivity.RESULT_OK) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            editProductCode.setText(barcode.rawValue);
        }else if(requestCode==createcategory){
            setUpCategorySpinner();
        }else if(requestCode==ADD_STOCK){
            stockInHand=data.getDoubleExtra(KEY_STOCK,0.0);
            stockMin=data.getDoubleExtra(KEY_STOCK_MIN,0.0);
            if(stockInHand>0)
                addStock(stockInHand);

        }else if(requestCode==1 && resultCode==RESULT_OK){
            if(ImagePickDialog.flagIsCam==1) {
                try {
                    ((TextView)findViewById(R.id.image_gone)).setVisibility(View.GONE);
                    currentPhotoPath=ImagePickDialog.currentPhotoPath;
                    imgViewProductImage.setImageBitmap(UtilityFunctions.setPicImageRounded(ImagePickDialog.currentPhotoPath,imgViewProductImage,this));
                } catch (Exception e) {
                    Validator.showToast(this, e.getMessage().toString());
                }
            }else{
                ((TextView)findViewById(R.id.image_gone)).setVisibility(View.GONE);
                setImageFromGallery(data,this,imgViewProductImage);
            }

        }
    }

    public   void setImageFromGallery(Intent imageReturnedIntent, Context ctx, ImageView logoImage){
        try {

            Uri selectedImage = imageReturnedIntent.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = ctx.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImagePickDialog.image = new File(picturePath);
            ImagePickDialog.currentPhotoPath=ImagePickDialog.image.getAbsolutePath();
            currentPhotoPath=ImagePickDialog.image.getAbsolutePath();
            //UtilityFunctions.setPic(ImagePickDialog.currentPhotoPath,logoImage,this);
            imgViewProductImage.setImageBitmap(UtilityFunctions.setPicImageRounded(ImagePickDialog.currentPhotoPath,logoImage,this));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();
            navigationBack();
    }

    public void showCatList(View v){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CreateProductActivity.this);
        builderSingle.setIcon(android.R.drawable.ic_menu_add);
        builderSingle.setTitle("SELECT CATEGORY : ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateProductActivity.this,
                android.R.layout.select_dialog_singlechoice,catList);


        builderSingle.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setPositiveButton("NEW CATEGORY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveToCreateCategory();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               catTitle=catList.get(which);
               categoryBtn.setText(catTitle);
            }
        });
        builderSingle.show();
    }



}
