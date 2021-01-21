package com.retailx.dreamdx.retailx;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.Fragments.FragmentMain;
import com.retailx.dreamdx.retailx.POJO.PrivilageData;
import com.retailx.dreamdx.retailx.Syncronisation.SyncFunctions;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ImagePickDialog;
import com.retailx.dreamdx.retailx.POJO.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class UtilityFunctions {

    public static Bitmap setPicImageRounded(String currentPhotoPath, ImageView imgViewProductImage, Context ctx) {
        Bitmap bitmap=null;
        Bitmap imageRounded=null;

        try{
            // Get the dimensions of the View
            int targetW=90;//
            int targetH=60 ;//

            targetW = imgViewProductImage.getWidth();
            targetH = imgViewProductImage.getHeight();


            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor;


            try{
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            }catch (Exception e){
                scaleFactor = Math.min(photoW / 90, photoH / 60);

            }
           /* if(targetH !=0 && targetW !=0) {
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            }else{
                scaleFactor = Math.min(photoW / 90, photoH / 60);

            }*/
            // Determine how much to scale down the image

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            imageRounded = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            Canvas canvas = new Canvas(imageRounded);
            Paint mpaint = new Paint();
            mpaint.setAntiAlias(true);
            mpaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawRoundRect((new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight())), 7, 7, mpaint);// Round Image Corner 100 100 100 100

        }catch (Exception e){
            //Toast.makeText(ctx,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally{
            //uploadBitmap(bitmap,ctx);
        }


        return  imageRounded;
    }


    //done screen
    public static Bitmap getBitMapFromPath(String currentPhotoPath,Context ctx) throws Exception{
        Bitmap bitmap=null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        return  bitmap;
    }


    public static void uploadBitmap(final Bitmap bitmap,Context ctx,String clientId,String productId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", clientId);
        params.put("product_id", productId);
        //ApiCalls netWork = new VolleyCalls();
        //netWork.connectTONetworkPostMultipart(ctx, ConstantsUsed.URL_POST_IMAGE, ConstantsUsed.TYPE_IMAGE, params, bitmap);
       SyncFunctions.connectTONetworkPostMultipart(ctx, ConstantsUsed.URL_POST_IMAGE, ConstantsUsed.TYPE_IMAGE, params, bitmap);
    }


    public static   void setImageFromGallery(Intent imageReturnedIntent, final Context ctx, ImageView logoImage){
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
            //logoImage.setImageBitmap(setPicImageRounded(ImagePickDialog.currentPhotoPath,logoImage,ctx));
            File f = new File(ImagePickDialog.currentPhotoPath);
            Picasso.get().load(f).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                    .error(R.drawable.no_image).resize(150, 150).centerCrop()
                    .into(logoImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ctx,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static SharedPreference sharedPreference = SharedPreference.getInstance();

    public static void apiCallBackSuccess(String response, Context ctx){

        if (response.equalsIgnoreCase("success")) {

            sharedPreference.save(ctx, Constants.CLIENT_ID,User.getInstance().getClient_id());
            sharedPreference.save(ctx, Constants.EMAIL,User.getInstance().getEmail_id() );
            sharedPreference.save(ctx, Constants.PHONE_NUMBER,User.getInstance().getMobile_no());

            Intent login = new Intent(ctx, MainActivity.class);
            ctx.startActivity(login);
        }
    }

    public static void apiCallBackOtp(String otp, Context ctx){
        sharedPreference.save(ctx, Constants.OTP,otp);
        sharedPreference.save(ctx, Constants.CLIENT_ID,User.getInstance().getClient_id());
        sharedPreference.save(ctx, Constants.EMAIL,User.getInstance().getEmail_id() );
        sharedPreference.save(ctx, Constants.PHONE_NUMBER,User.getInstance().getMobile_no());

        Intent login = new Intent(ctx, ActivationCodeActivity.class);
        ctx.startActivity(login);
    }

    public static void apiCallBackOtpSuccess( Context ctx, JSONObject reader){
        try {

            /*save privilage data*/
            /*this code is duplicated in SignupActivity -apiCallBackSuccess*/
            sharedPreference.save(ctx, ConstantsUsed.IS_ADMIN, PrivilageData.getInstance().getIs_admin());
            sharedPreference.saveInt(ctx, ConstantsUsed.CREATE_DISCOUNT, PrivilageData.getInstance().getCreate_discount());
            sharedPreference.saveInt(ctx, ConstantsUsed.CREATE_EDIT_PRODUCT,PrivilageData.getInstance().getCreate_edit_product() );
            sharedPreference.saveInt(ctx, ConstantsUsed.MANAGE_STOCK,PrivilageData.getInstance().getManage_stock());
            sharedPreference.saveInt(ctx, ConstantsUsed.VIEW_ALL_TAX,PrivilageData.getInstance().getView_all_tx());
            /*this code is duplicated in SignupActivity -apiCallBackSuccess*/

            String basic_features_value = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(0).getString("basic_features");
            String billprint_value = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(1).getString("billprint");
            String viewreport_value = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(2).getString("viewreport");
           // String viewhistory_value = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(3).getString("viewhistory");
            String customerprofile = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(4).getString("customerprofile");
            String multiusers = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(5).getString("multiusers");
            String billshare = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(6).getString("billshare");
            //String managestock = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(7).getString("managestock");
            String creditcard = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(8).getString("creditcard");
            String qrcode = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(9).getString("qrcode");
            String cheque = reader.getJSONObject("subscription_data").getJSONArray("subscription_data").getJSONObject(10).getString("cheque");
            String subscription_exp_date_value = reader.getJSONObject("subscription_data").getString("subscription_exp_date");

            sharedPreference.save(ctx,"basic_features", basic_features_value);
            sharedPreference.save(ctx, "billprint", billprint_value);
            sharedPreference.save(ctx,"viewreport",viewreport_value );
           // sharedPreference.save(ctx, "viewhistory",viewhistory_value);
            sharedPreference.save(ctx, "customerprofile",customerprofile);
            sharedPreference.save(ctx, "multiusers",multiusers);
            sharedPreference.save(ctx, "billshare",billshare);
            //sharedPreference.save(ctx, "managestock",managestock);
            sharedPreference.save(ctx, "creditcard",creditcard);
            sharedPreference.save(ctx, "qrcode",qrcode);
            sharedPreference.save(ctx, "cheque",cheque);

            sharedPreference.save(ctx, "subscription_exp_date",subscription_exp_date_value);


            // free version values
            basic_features_value = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(0).getString("basic_features");
            billprint_value = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(1).getString("billprint");
            viewreport_value = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(2).getString("viewreport");
            //viewhistory_value = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(3).getString("viewhistory");
            customerprofile = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(4).getString("customerprofile");
            multiusers = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(5).getString("multiusers");
            billshare = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(6).getString("billshare");
            //managestock = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(7).getString("managestock");
            creditcard = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(8).getString("creditcard");
            qrcode = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(9).getString("qrcode");
            cheque = reader.getJSONObject("subscription_data").getJSONArray("basic_features_array").getJSONObject(10).getString("cheque");

            DBHelper db=new DBHelper(ctx);
            db.insertSubDetails(User.getInstance().getUser_id(),"basic_features",basic_features_value);
            db.insertSubDetails(User.getInstance().getUser_id(),"billprint",billprint_value);
            db.insertSubDetails(User.getInstance().getUser_id(),"viewreport",viewreport_value);
           // db.insertSubDetails(User.getInstance().getUser_id(),"viewhistory",viewhistory_value);//not implemented
            db.insertSubDetails(User.getInstance().getUser_id(),"customerprofile",customerprofile);
            db.insertSubDetails(User.getInstance().getUser_id(),"multiusers",multiusers);
            db.insertSubDetails(User.getInstance().getUser_id(),"billshare",billshare);//not implemented
            //db.insertSubDetails(User.getInstance().getUser_id(),"managestock",managestock);//not implemented
            db.insertSubDetails(User.getInstance().getUser_id(),"creditcard",creditcard);
            db.insertSubDetails(User.getInstance().getUser_id(),"qrcode",qrcode);//not implemented
            db.insertSubDetails(User.getInstance().getUser_id(),"cheque",cheque);


        }catch(Exception e){
            Toast.makeText(ctx,e.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }

        /*this code is duplicated in SignupActivity -apiCallBackSuccess*/

        Intent login = new Intent(ctx, MainActivity.class);
        ctx.startActivity(login);
    }



    public static void logout(Context ctx){
            sharedPreference.save(ctx, Constants.CLIENT_ID,"-1");
            sharedPreference.save(ctx, Constants.USER_ID,"-1");
            sharedPreference.save(ctx, Constants.EMAIL,"-1" );
            sharedPreference.save(ctx, Constants.PHONE_NUMBER,"-1");
            Intent intent = new Intent(ctx, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);
    }


    public static FragmentMain attachBarcodeReaderFragment(String catTitle, Context ctx, FragmentManager supportFragmentManager, int flag) {
        // final FragmentManager supportFragmentManager = ctx.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        FragmentMain fragment = FragmentMain.newInstance(catTitle,flag);
        fragmentTransaction.replace(R.id.main_fm_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    public static  boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)ctx. getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
