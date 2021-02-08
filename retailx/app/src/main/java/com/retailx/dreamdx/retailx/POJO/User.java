package com.retailx.dreamdx.retailx.POJO;

import com.google.gson.annotations.SerializedName;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

public  class User {

   static  User user;

    @SerializedName(ConstantsUsed.FIRST_NAME)
    String first_name="";

    @SerializedName(ConstantsUsed.LAST_NAME)
    String last_name="";

    @SerializedName(ConstantsUsed.EMAIL_ID)
    String email_id="";

    /*@SerializedName(ConstantsUsed.CONTACT_ID)
    String contact_id="";*/

    @SerializedName(ConstantsUsed.MOBILE_NO)
    String mobile_no="";

    @SerializedName(ConstantsUsed.CLIENT_ID)
    String client_id="";

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @SerializedName(ConstantsUsed.USER_ID)
    String user_id="";

    @SerializedName(ConstantsUsed.LOCATION_ADDRESS)
    String location_address="";

    String otp_code ="";

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }


  /*  @SerializedName(ConstantsUsed.DISC_AMOUNT)
    String disc_amt="";*/

    public static void setInstance(User usr){
        user=usr;
    }

    public static User getInstance(){
        if(user!=null)
            return  user;
        else {
            user=new User();
            return user;
        }
    }


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

  /*  public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }*/

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

}
