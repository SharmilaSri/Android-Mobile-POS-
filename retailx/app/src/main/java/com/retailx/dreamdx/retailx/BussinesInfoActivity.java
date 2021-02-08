package com.retailx.dreamdx.retailx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.ImagePickDialog;
import com.retailx.dreamdx.retailx.utils.Validator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class BussinesInfoActivity extends AppCompatActivity {
    static com.retailx.dreamdx.retailx.utils.CircularImageView logoImage;
    TextInputEditText nameEdt,numberEdt,addressEdt;
    DBHelper db;
    String name="";
    String number="";
    String address="";
    String imagePath="";
    boolean isUpdate=false;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_bussines_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.busines_information));

        db=new DBHelper(this);

        setUpView();

        showDetails();
    }

    private void showDetails(){
        Cursor infoCur = null;

        try {
            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

            infoCur = db.getBussinessDetails(userId);
                while (infoCur.moveToNext()) {
                    id= infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_ID)));
                    name = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_NAME)));
                    number = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_NUMBER)));
                    address = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_ADDRESS)));
                    imagePath = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_LOGO)));

                    nameEdt.setText(name);
                    numberEdt.setText(number);
                    addressEdt.setText(address);

                    if (!imagePath.equalsIgnoreCase("NO_IMAGE") && imagePath != null) {
                        //Toast.makeText(this,imagePath,Toast.LENGTH_SHORT).show();

                        ImagePickDialog.currentPhotoPath = imagePath;

                        if(imagePath.startsWith("http")){
                            Picasso.get().load(imagePath).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                                    .error(R.drawable.no_image).resize(110, 110).centerCrop()
                                    .into(logoImage);
                        }else {
                            File f = new File(imagePath);
                            Picasso.get().load(f).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                                    .error(R.drawable.no_image).resize(150, 150).centerCrop()
                                    .into(logoImage, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Toast.makeText(BussinesInfoActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }

                    }
                }

            }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally{
            if(infoCur!=null)
            infoCur.close();
        }
    }



    private void setUpView(){
        logoImage=findViewById(R.id.img_logo);
        nameEdt=findViewById(R.id.edt_bus_name);
        addressEdt=findViewById(R.id.edt_bus_address);
        numberEdt=findViewById(R.id.edt_bus_contact);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 112: {
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


        }
    }
    public void saveBussinessInfo(View v){

        name=nameEdt.getText().toString();
        number=numberEdt.getText().toString();
        address=addressEdt.getText().toString();

        try{
            if(!name.isEmpty() && !number.isEmpty() && !address.isEmpty()){

                if(!id.isEmpty()){
                    db.updateBussinessInfo(id,name,number,address,ImagePickDialog.currentPhotoPath,1);
                }else{
                    String id= AppFeatures.getTimeStamp()  ;
                    String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

                    db.insertBussinessInfo(userId,id,name,number,address,ImagePickDialog.currentPhotoPath,1);

                }
                startActivity(new Intent(this,MainActivity.class));
            } else if (name.isEmpty()) {

                nameEdt.setError(getResources().getString(R.string.empty_field));


                nameEdt.setHintTextColor(Color.RED);
            }else if(number.isEmpty()){
                numberEdt.setError(getResources().getString(R.string.empty_field));
                numberEdt.setHintTextColor(Color.RED);
            }else if(address.isEmpty()){
                addressEdt.setError(getResources().getString(R.string.empty_field));
                addressEdt.setHintTextColor(Color.RED);
            }
        }catch (Exception e){

        }


    }

    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 112;
    public void openDialog(View v){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_STORAGE);
            return;
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){

                    if(ImagePickDialog.flagIsCam==1) {
                        try {
                            //logoImage.setImageBitmap(UtilityFunctions.setPicImageRounded(ImagePickDialog.currentPhotoPath,logoImage,this));
                            File f = new File(ImagePickDialog.currentPhotoPath);
                            Picasso.get().load(f).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                                    .error(R.drawable.no_image).resize(150, 150).centerCrop()
                                    .into(logoImage, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Toast.makeText(BussinesInfoActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } catch (Exception e) {
                            Validator.showToast(this, e.getMessage().toString());
                        }
                    }else{
                        UtilityFunctions.setImageFromGallery(imageReturnedIntent,BussinesInfoActivity.this,logoImage);

                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }

}
