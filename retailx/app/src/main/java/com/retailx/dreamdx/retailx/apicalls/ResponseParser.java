package com.retailx.dreamdx.retailx.apicalls;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.google.gson.Gson;
import com.retailx.dreamdx.retailx.LoginActivity;
import com.retailx.dreamdx.retailx.MainActivity;
import com.retailx.dreamdx.retailx.POJO.PrivilageData;
import com.retailx.dreamdx.retailx.SignUpActivity;
import com.retailx.dreamdx.retailx.SubscriptionActivity;
import com.retailx.dreamdx.retailx.Syncronisation.SyncFunctions;
import com.retailx.dreamdx.retailx.UserAuthorisationActivity;
import com.retailx.dreamdx.retailx.UtilityFunctions;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.POJO.User;
import com.retailx.dreamdx.retailx.utils.Validator;

import org.json.JSONArray;
import org.json.JSONObject;

public class ResponseParser {
    private static User user;
    private static PrivilageData data;

    public static void PARSE_JSON(Context ctx, String response, int type, Gson gson) {


        try {
            JSONObject reader;
            DBHelper db=new DBHelper(ctx);
            String userId = SharedPreference.getInstance().getValue(ctx, Constants.USER_ID);

            switch (type) {
                case ConstantsUsed.TYPE_LOGIN://Login
                    reader = new JSONObject(response);
                    response = reader.getString(ConstantsUsed.SUCCESS);
                    if (response.equalsIgnoreCase("success")) {
                        if(reader.getString(ConstantsUsed.OTP_STATUS).equalsIgnoreCase("0")){//not compleated
                            User.getInstance().setOtp_code(reader.getString(ConstantsUsed.OTP_CODE));
                            user = gson.fromJson(reader.getJSONObject(ConstantsUsed.USER_DATA).toString(), User.class);
                            User.getInstance().setInstance(user);

                            SignUpActivity.apiCallBackOtp(reader.getString(ConstantsUsed.OTP_CODE),ctx);
                        }else{//Login : Register success,otp completed,admin

                            user = gson.fromJson(reader.getJSONObject(ConstantsUsed.USER_DATA).toString(), User.class);
                            User.getInstance().setInstance(user);

                            data = gson.fromJson(reader.getJSONObject(ConstantsUsed.PRIVILAGED_DATA).toString(), PrivilageData.class);
                            PrivilageData.getInstance().setData(data);

                            SignUpActivity.apiCallBackSuccess(response,ctx,reader);
                        }

                    }  else if(response.equalsIgnoreCase("false") ){
                        if(reader.getString(ConstantsUsed.OTP_STATUS).equalsIgnoreCase("0")) {//not completed but client exist

                            User.getInstance().setOtp_code(reader.getString(ConstantsUsed.OTP_CODE));
                            User.getInstance().setEmail_id(reader.getJSONObject(ConstantsUsed.USER_DATA).getString("email_id"));
                            user = gson.fromJson(reader.getJSONObject(ConstantsUsed.USER_DATA).toString(), User.class);
                            User.getInstance().setInstance(user);
                            SignUpActivity.apiCallBackOtp(reader.getString(ConstantsUsed.OTP_CODE),ctx);

                        }else{
                            Validator.showToast(ctx, "SIGN UP/LOGIN FAILED:"+reader.getString("massage"));
                        }

                    }


                    break;
                case ConstantsUsed.TYPE_SIGNUP:

                    reader = new JSONObject(response);
                    response = reader.getString(ConstantsUsed.SUCCESS);
                    if (response.equalsIgnoreCase("success")) {
                        user = gson.fromJson(reader.getJSONObject(ConstantsUsed.USER_DATA).toString(), User.class);
                        User.getInstance().setInstance(user);
                        UtilityFunctions.apiCallBackSuccess(response,ctx);

                    } else {
                        //Toast.makeText(ctx, reader.getString("massage"), Toast.LENGTH_LONG).show();
                    }
                    break;

                case ConstantsUsed.TYPE_CREATE_CATEGORY:
                    JSONArray catArray = new JSONArray(response);
                    response = catArray.getJSONObject(0).getString("success");

                    if (response.equalsIgnoreCase("success")) {
                        SyncFunctions.apiCalBackCategory(response,ctx);
                      //  Toast.makeText(ctx, "CATEGORY UPLOAD :"+"success", Toast.LENGTH_LONG).show();


                    } else {
                       // Toast.makeText(ctx, "CATEGORY UPLOAD :"+catArray.getJSONObject(0).getString("massage"), Toast.LENGTH_LONG).show();
                    }
                    break;

                case ConstantsUsed.TYPE_CREATE_PRODUCT:


                    JSONArray readerArray = new JSONArray(response);
                    response = readerArray.getJSONObject(0).getString("success");

                    if (response.equalsIgnoreCase("success")) {

                        //Toast.makeText(ctx, "PRODUCT UPLOADED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                        SyncFunctions.apiCalBackProduct(response,ctx);

                        //((CreateProductActivity)ctx).apiCalBackProduct(response,ctx);

                    } else {
                        //Toast.makeText(ctx, "PRODUCT UPLOAD"+readerArray.getJSONObject(0).getString("massage"), Toast.LENGTH_LONG).show();
                    }
                    break;

                case ConstantsUsed.TYPE_GET_BUSSINESS_INFO:


                    reader = new JSONObject(response);

                    String companyName=reader.getString("company_name");
                    String companyNo=reader.getString("contact_id");
                    String companyAddress=reader.getString("location_address");
                    String companyImage=reader.getString("profile_image");
                    String id= AppFeatures.getTimeStamp()  ;

                    Cursor info=null;
                    try{

                        info = db.getBussinessDetails(userId);
                        if (info.getCount() > 0) {
                            db.updateBussinessInfo(userId,companyName,companyNo,companyAddress,companyImage,0);
                        }else{
                            db.insertBussinessInfo(userId,id,companyName,companyNo,companyAddress,companyImage,0);

                        }
                    }catch(Exception e){

                    }finally{
                        if(info!=null)
                            info.close();
                    }




                    break;

                case ConstantsUsed.TYPE_IMAGE:
                    JSONArray readerImage = new JSONArray(response);
                    response = readerImage.getJSONObject(0).getString("success");

                    if (response.equalsIgnoreCase("success")) {
                        SyncFunctions.apiCalBackProductImages(readerImage.getJSONObject(0).getString("product_id"),ctx);
                    } else {
                       //Toast.makeText(ctx, reader.getString("massage"), Toast.LENGTH_LONG).show();
                    }
                    break;

                case ConstantsUsed.TYPE_CREATE_INVOICE:
                    JSONArray readerArrayInvoice = new JSONArray(response);
                    response = readerArrayInvoice.getJSONObject(0).getString("success");

                    if (response.equalsIgnoreCase("success")) {
                        //Toast.makeText(ctx, "TRANSACTION UPLOADED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                        SyncFunctions.apiCalBackTransaction(response,ctx);


                    } else {
                        //Toast.makeText(ctx,"TRANSACTION UPLOAD FAILED" /*readerArrayInvoice.getJSONObject(0).getString("massage")*/, Toast.LENGTH_LONG).show();
                    }
                    break;

                case ConstantsUsed.TYPE_GET_CATEGORY:
                    reader = new JSONObject(response);
                    response = reader.getString(ConstantsUsed.SUCCESS);

                    if (response.equalsIgnoreCase("success")) {


                        JSONArray catArr=reader.getJSONArray("category_data");
                        if(catArr.length()>0) {
                            for (int i = 0; i < catArr.length(); i++) {
                                String catId = catArr.getJSONObject(i).getString("cat_id");
                                String tilte = catArr.getJSONObject(i).getString("cat_title");
                                String desc = catArr.getJSONObject(i).getString("description");

                                Cursor infom=null;
                                try{

                                    infom = db.getCatTitle(catId);
                                    if (infom.getCount() > 0) {
                                        db.updateCategoryDetails(tilte, catId, 0);
                                    }else{
                                        db.insertCategoryDetails(userId, catId, tilte, desc, AppFeatures.getTimeStamp("date only"),
                                                1, 0);
                                    }
                                }catch(Exception e){

                                }finally{
                                    if(infom!=null)
                                        infom.close();
                                }


                                ((MainActivity)ctx).setUpTabs(ctx);
                                //ctx.startActivity(new Intent(ctx, MainActivity.class));

                            }
                        }
                        //Category cat = gson.fromJson(reader.getJSONObject(ConstantsUsed.USER_DATA).toString(), Category.class);
                        //User.setInstance(user);

                        SyncFunctions.apiCalBackCategoryGet(response,ctx);

                    } else {
                       // Toast.makeText(ctx, "CATEGORY DOWNLOAD :"+reader.getString("massage"), Toast.LENGTH_LONG).show();
                    }
                    break;


                case ConstantsUsed.TYPE_GET_PRODUCTS:
                    reader = new JSONObject(response);
                    response = reader.getString(ConstantsUsed.SUCCESS);

                    if (response.equalsIgnoreCase("success")) {

                        JSONArray prodArr=reader.getJSONArray("product_data");
                        if(prodArr.length()>0){
                        for(int i=0;i<prodArr.length();i++) {
                            double unit_selling = prodArr.getJSONObject(i).getDouble("unit_selling_price");
                            double unit_buying = prodArr.getJSONObject(i).getDouble("unit_buying_price");
                            String product_id = prodArr.getJSONObject(i).getString("product_id");
                            String tilte = prodArr.getJSONObject(i).getString("product_title");
                            String serial_code = prodArr.getJSONObject(i).getString("serial_code");
                            String imagePath = prodArr.getJSONObject(i).getString("image");
                            String desc = prodArr.getJSONObject(i).getString("description");
                            String catId = prodArr.getJSONObject(i).getString("category_id");
                            String uomeasurement = prodArr.getJSONObject(i).getString("unit_of_measure");
                            Cursor infor=null;
                            try {
                                infor = db.getProductDetailsWdProductId(product_id);
                                if (infor.getCount() > 0) {
                                    db.updateProductDetails(userId, product_id, tilte, desc, catId, uomeasurement, serial_code, "0", "0", unit_selling, unit_buying, AppFeatures.getTimeStamp("date"), imagePath
                                            , 1, 0, 0);
                                } else {
                                    db.insertProductDetails(userId, product_id, tilte, desc, catId, uomeasurement, serial_code, "0", "0", unit_selling, unit_buying, AppFeatures.getTimeStamp("date"), imagePath
                                            , 1, 0, 0);
                                }


                            }catch (Exception e){

                            }finally {
                                if(infor!=null)
                                    infor.close();
                            }

                            ((MainActivity)ctx).attachBarcodeReaderFragment("ALL");

                        }


                        }

                    }



                    break;
                case ConstantsUsed.TYPE_OTP_SUBMIT://otp submit : Register success,otp completed,admin

                    reader = new JSONObject(response);
                    response = reader.getString(ConstantsUsed.SUCCESS);
                    if (response.equalsIgnoreCase("success")) {


                        data = gson.fromJson(reader.getJSONObject(ConstantsUsed.PRIVILAGED_DATA).toString(), PrivilageData.class);
                        PrivilageData.getInstance().setData(data);
                        UtilityFunctions.apiCallBackOtpSuccess(ctx,reader);



                    } else {
                        //Toast.makeText(ctx, reader.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    break;

                case ConstantsUsed.TYPE_OTP_RE_SUBMIT:
                    reader = new JSONObject(response);
                    String otp = reader.getString("otp_code");

                    User.getInstance().setOtp_code(otp);

                    break;

                case ConstantsUsed.TYPE_UPLOAD_CUSTOMER:
                    JSONArray array = new JSONArray(response);
                    response = array.getJSONObject(0).getString("success");
                    if(response.equalsIgnoreCase("success")){
                        SyncFunctions.apiCalBackCustomer();
                        //Toast.makeText(ctx, "Customer upload success", Toast.LENGTH_LONG).show();

                    }else{
                        //Toast.makeText(ctx, "Customer upload failed", Toast.LENGTH_LONG).show();

                    }
                    break;


                case ConstantsUsed.TYPE_BUSSINESS_INFO:

                    JSONObject bussinessObj = new JSONObject(response);
                    response = bussinessObj.getString("massage");
                    break;


                case ConstantsUsed.TYPE_SUBSCRIPTION:

                    reader = new JSONObject(response);
                    SubscriptionActivity.onSuccessSubscription(reader,ctx);

                    break;

                case ConstantsUsed.TYPE_SUBMIT:
                    ctx.startActivity(new Intent(ctx, LoginActivity.class));
                    break;

                case ConstantsUsed.TYPE_RESET_PW:
                    reader = new JSONObject(response);
                    response = reader.getString(ConstantsUsed.SUCCESS);
                    if (response.equalsIgnoreCase("success")) {
                        Validator.showToast(ctx,reader.getString("message"));

                    } else {
                        //Toast.makeText(ctx, reader.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    break;

                case ConstantsUsed.TYPE_PRIVILAGE:

                    reader = new JSONObject(response);
                    response = reader.getString(ConstantsUsed.SUCCESS);
                    if (response.equalsIgnoreCase("success")) {
                        //Toast.makeText(ctx, reader.getString("massage"), Toast.LENGTH_LONG).show();
                        UserAuthorisationActivity.apiCallBackPrivilageSuccess(ctx);

                    } else {
                        //Toast.makeText(ctx, reader.getString("massage"), Toast.LENGTH_LONG).show();
                    }

                    break;


                case ConstantsUsed.TYPE_ERROR:

                    reader = new JSONObject(response);
                    response = reader.getString(ConstantsUsed.SUCCESS);
                    if (response.equalsIgnoreCase("success")) {
                        SyncFunctions.apiCalBackError();

                    } else {
                        //Toast.makeText(ctx, reader.getString("massage"), Toast.LENGTH_LONG).show();
                    }

                    break;

                case ConstantsUsed.TYPE_POST_SUB:
                    reader = new JSONObject(response);
                    UtilityFunctions.apiCallBackOtpSuccess(ctx,reader);

                    break;

                default:
                    break;
            }


        } catch (Exception e) {
            //Toast.makeText(ctx, "PARSE_JSON" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

    }
}
