package com.retailx.dreamdx.retailx.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.retailx.dreamdx.retailx.BuildConfig;
import com.retailx.dreamdx.retailx.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagePickDialog extends Dialog implements
        View.OnClickListener{

    public static Activity c;
    public Dialog d;
    public ImageView imgGallery, imgCamera;
    public static String currentPhotoPath="NO_IMAGE";
    public static Uri photoURI;
    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;
    static final int REQUEST_IMAGE_LOGO = 1;

    public ImagePickDialog(Activity a) {
        super(a);
        this.c = a;
    }

    public static int flagIsCam=1;
    static int MY_PERMISSIONS_REQUEST_STORAGE=100;
    public static void proceedTakingImage(int isCam){
        Intent takePictureIntent;
        if (AppFeatures.checkCameraHardware(c)) {

            if(isCam==1) {
                takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            }else {
                takePictureIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            if (takePictureIntent.resolveActivity(c.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                   // Toast.makeText(c, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {

                    photoURI= FileProvider.getUriForFile(c,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                    if(isCam==1) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        c.startActivityForResult(takePictureIntent, REQUEST_IMAGE_LOGO);
                    }else {


                        takePictureIntent.setType("image/*");
                        c.startActivityForResult(takePictureIntent, REQUEST_IMAGE_LOGO);
                    }

                }
            }
        }
    }

    int MY_PERMISSIONS_REQUEST_CAMERA =200;
    private void askPermissionCamera(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(c, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(c,
                        Manifest.permission.CAMERA)) {

                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(c,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);

                }


            } else {
                proceedTakingImage(1);
                flagIsCam=1;
            }
        }else{
            proceedTakingImage(1);
            flagIsCam=1;
        }

    }


    private void checkExternalStorageAvailable(){

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    public static File image;
    public static File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_image_pick);
        imgGallery = (ImageView) findViewById(R.id.img_gallery);
        imgCamera = (ImageView) findViewById(R.id.img_camera);
        imgGallery.setOnClickListener(this);
        imgCamera.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_gallery:
                flagIsCam=0;
                proceedTakingImage(0);
                /*Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                c.startActivityForResult(pickPhoto , 1);*/
                break;
            case R.id.img_camera:
                flagIsCam=1;
                askPermissionCamera();
               // proceedTakingImage(1);
               /* Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                c.startActivityForResult(takePicture, 0);*/
                break;
            default:
                break;
        }
        dismiss();
    }
}
