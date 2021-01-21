package com.retailx.dreamdx.retailx.POJO;

public class ListType {

    static ListType listType=null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type="";

    public static ListType getInstance(){
        if(listType==null) {
            listType=new ListType();
            return listType;
        }else
            return listType;
    }

}
