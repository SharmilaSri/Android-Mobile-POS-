package com.retailx.dreamdx.retailx.POJO;

import android.content.Context;

import java.util.ArrayList;

public class Category {

    public static ArrayList<String> getCategoryTitleList() {
        return categoryTitleList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    String title="";
    String catId="0";
    String desc="";

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    String selectedCategory="ALL";

    public static void setCategoryTitleList(ArrayList<String> categoryTitleList) {
        Category.categoryTitleList = categoryTitleList;
    }

    private static ArrayList<String> categoryTitleList=new ArrayList<>();
    private static ArrayList<Category> categoryList=null;


    public  Category(String tit,String id,String des){
        title=tit;
        catId=id;
        desc=des;
    }

    private Category(){

    }


    public static void addCategoryToList(Category cat){
        if(categoryList==null )
            categoryList=new ArrayList<>();
        categoryList.add(cat);

    }

    public static void clear(Context ctx){

        if(categoryList!=null && !categoryList.isEmpty()) {
            categoryList.clear();
        }
    }


    public static ArrayList<Category>  getCategoryList(){
        return categoryList;
    }


    private static Category obj;
    public static Category getCategoryInstance(){
      if(obj==null){
          obj=new Category();
      }

      return obj;
    }
}
