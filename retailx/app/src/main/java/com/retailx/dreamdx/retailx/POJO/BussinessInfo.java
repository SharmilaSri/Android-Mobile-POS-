package com.retailx.dreamdx.retailx.POJO;

public class BussinessInfo {


    String bussinessName="";
    String email="";
    String bussinesPhoneNo="";
    String logoPath="";
    String bussinessAddress="";

    static BussinessInfo bussinessInfoObj=null;

    private BussinessInfo(){

    }

    public static BussinessInfo getInstance(){

        if(bussinessInfoObj==null){
            bussinessInfoObj=new BussinessInfo();
        }
        return bussinessInfoObj;
    }


    public String getBussinessName() {
        return bussinessName;
    }

    public void setBussinessName(String bussinessName) {
        this.bussinessName = bussinessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBussinesPhoneNo() {
        return bussinesPhoneNo;
    }

    public void setBussinesPhoneNo(String bussinesPhoneNo) {
        this.bussinesPhoneNo = bussinesPhoneNo;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getBussinessAddress() {
        return bussinessAddress;
    }

    public void setBussinessAddress(String bussinessAddress) {
        this.bussinessAddress = bussinessAddress;
    }
}
