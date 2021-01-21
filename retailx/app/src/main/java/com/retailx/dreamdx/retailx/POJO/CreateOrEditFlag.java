package com.retailx.dreamdx.retailx.POJO;

public class CreateOrEditFlag {
    static  CreateOrEditFlag obj=null;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    int flag=0;

    private CreateOrEditFlag(){

    }

    public static CreateOrEditFlag getInstance(){
        if(obj==null)
            obj=new CreateOrEditFlag();

        return obj;
    }
}
