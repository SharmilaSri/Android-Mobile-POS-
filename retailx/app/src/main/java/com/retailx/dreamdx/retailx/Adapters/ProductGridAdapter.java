package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.retailx.dreamdx.retailx.CreateProductActivity;
import com.retailx.dreamdx.retailx.MainActivity;

import com.retailx.dreamdx.retailx.POJO.CreateOrEditFlag;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.POJO.ProductDetailsListGrid;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.UtilityFunctions;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.interfaces.CountChangedListener;
import com.retailx.dreamdx.retailx.interfaces.ProductItemActionListener;
import com.retailx.dreamdx.retailx.interfaces.UnitOfMeasurementChangedListener;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.Validator;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ProductGridAdapter
        extends ArrayAdapter {


    private final Activity context;
    private  ArrayList<Product> product;
    LinearLayout layout;
    static setImageAsync asynTask;
    //TextView badgeNotificationStock;
    int flag=0;//default 0-for main activity
    double stockInHand=0.0;
    double stockMin=0.0;
    String productId="";


    public ProductGridAdapter(Activity context, ArrayList<Product> product,int flag) {
        super(context, R.layout.grid_item_product, product);

        this.context=context;
        this.product=product;
        this.flag=flag;

    }
    double total=0.00;

    ProductItemActionListener listener;
    public void setListener(ProductItemActionListener listener){
        this.listener=listener;
    }
    TextView productNameTxt=null;
    TextView productPriceTxt=null;
    TextView badgeNotificationStock=null;
    ImageView imageview=null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater=context.getLayoutInflater();
        View view=null;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (view == null) {
            view = new View(getContext());
            view = inflater.inflate(R.layout.grid_item_product, null);
            productNameTxt = (TextView) view.findViewById(R.id.id_product_name);
            productPriceTxt = (TextView) view.findViewById(R.id.id_prodcut_price);
            badgeNotificationStock = (TextView) view.findViewById(R.id.badge_notification_stock);
            imageview = (ImageView) view.findViewById(R.id.image_grid);
            layout = (LinearLayout) view.findViewById(R.id.layout_linear);

        }else{
                view = (View) convertView;

        }

           productNameTxt.setText(product.get(position).getProduct_title());


        if (product.get(position).getUnit_selling_price() > 0.0)
                productPriceTxt.setText(String.valueOf(product.get(position).getUnit_selling_price()));
            else
                productPriceTxt.setText(getContext().getResources().getString(R.string.new_));

        final String imagePath = product.get(position).getImagePath();

            if (!imagePath.equalsIgnoreCase("LAST_IMAGE")) {
                if (!imagePath.equalsIgnoreCase("NO_IMAGE")) {


                    if(imagePath.startsWith("http")){
                        Picasso.get().load(imagePath).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                                .error(R.drawable.no_image).resize(110, 110).centerCrop()
                                .into(imageview);
                    }else {
                        File f = new File(imagePath);
                        Picasso.get().load(f).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                                .error(R.drawable.no_image).resize(110, 110).centerCrop()
                                .into(imageview);
                    }

                }

            } else if (imagePath.equalsIgnoreCase("LAST_IMAGE") && position==product.size()-1) {
                Picasso.get().load(R.drawable.icong_plus_bg).into(imageview);
                //imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.icong_plus_bg));

            }else if(imagePath.equalsIgnoreCase("NO_IMAGE")){
               // imageviewCopy.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_image));

            }

            stockInHand=product.get(position).getStockInHand();
            stockMin=product.get(position).getStockMinimum();
            productId=product.get(position).getProduct_id();

            executeStockManagement(stockInHand,stockMin,productId,badgeNotificationStock);

        if(flag==0) {//Mainactivity

            RelativeLayout gridLayoutItem = (RelativeLayout) view.findViewById(R.id.id_grid_layout);
            gridLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == product.size() - 1) {

                        SharedPreference sharedPreference = SharedPreference.getInstance();
                        if(sharedPreference.getInt(context,ConstantsUsed.CREATE_EDIT_PRODUCT)!=0) {
                            ((MainActivity) context).shouldShow = "false";
                            Intent createCategory = new Intent(context, CreateProductActivity.class);
                            CreateOrEditFlag.getInstance().setFlag(0);
                            context.startActivity(createCategory);
                        }else{
                            Validator.showToast(context,"Sorry! You are not privilaged to create products");
                        }
                    } else if(product.get(position).getUnit_of_measure().equalsIgnoreCase("0")){
                        //listener.onItemTap(imageviewCopy);

                        AddUnitOfmeasurement dialog = new AddUnitOfmeasurement(context, "");
                        dialog.setCancelable(false);
                        dialog.setListener(new UnitOfMeasurementChangedListener() {
                            @Override
                            public void unitOfmeasurementAdded(double amount) {

                                product.get(position).setUnit_of_measure_count(amount);

                                productAddedTotheCart(product.get(position).getStockInHand(),product.get(position).getStockMinimum()
                                        ,product.get(position).getProduct_id(),position, 1,badgeNotificationStock,amount);

                            }
                        });
                        dialog.show();

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int displayWidth = displayMetrics.widthPixels;
                        int displayHeight = displayMetrics.heightPixels;

                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.copyFrom(dialog.getWindow().getAttributes());
                        int dialogWindowWidth = (int) (displayWidth * 0.94f);
                        // Set alert dialog height equal to screen height 80%
                        int dialogWindowHeight = (int) (displayHeight * 0.8f);
                        layoutParams.width = dialogWindowWidth;
                        layoutParams.height = dialogWindowHeight;
                        dialog.getWindow().setAttributes(layoutParams);


                    }else{
                        productAddedTotheCart(product.get(position).getStockInHand(),product.get(position).getStockMinimum()
                                ,product.get(position).getProduct_id(),position, 1,badgeNotificationStock,0.0);
                    }
                }
            });


            gridLayoutItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NumberSelectorDialog dialog = new NumberSelectorDialog(context, product.get(position), 0);
                    dialog.setCancelable(false);
                    dialog.setListener(new CountChangedListener() {
                        @Override
                        public void notifyCountDecreased(double count) {
                            productDeletedFromtheCart(product.get(position).getStockInHand(), product.get(position).getStockMinimum()
                                    , product.get(position).getProduct_id(), position, count, badgeNotificationStock);

                        }

                        @Override
                        public void notifyCountIncreased(double count) {
                            //Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();
                            productAddedTotheCart(product.get(position).getStockInHand(), product.get(position).getStockMinimum()
                                    , product.get(position).getProduct_id(),

                                    position, count, badgeNotificationStock, 0.0);

                        }

                        @Override
                        public void notifyCountChanged(double count) {
                            //Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();

                        }


                        public void notifyEditBtnClicked() {
                            switchToEdit(position, flag);
                        }

                        @Override
                        public void priceChanged(double price) {
                            product.get(position).setUnit_selling_price(price);
                            productPriceTxt.setText(String.valueOf(product.get(position).getUnit_selling_price()));

                        }

                        @Override
                        public void stockChanged(double stock) {
                            product.get(position).setStockInHand(stock);
                            DBHelper db = new DBHelper(context);
                            db.updateProductStockInHand(product.get(position).getProduct_id(), stock);
                        }
                    });// Get screen width and height in pixels

                    dialog.show();

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int displayWidth = displayMetrics.widthPixels;
                    int displayHeight = displayMetrics.heightPixels;

                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    int dialogWindowWidth = (int) (displayWidth * 0.94f);
                    // Set alert dialog height equal to screen height 70%
                    int dialogWindowHeight = (int) (displayHeight * 0.7f);
                    layoutParams.width = dialogWindowWidth;
                    layoutParams.height = dialogWindowHeight;
                    dialog.getWindow().setAttributes(layoutParams);


                    return false;
                }
            });

        }
        return view;

    };

    private void switchToEdit(int position,int flag){
        MainActivity.shouldShow="false";
        CreateOrEditFlag.getInstance().setFlag(flag);
        Intent editProduct=new Intent(context, CreateProductActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ConstantsUsed.EDIT_PRODUCT_KEY,ConstantsUsed.EDIT_PRODUCT_VALUE);
        extras.putString(ConstantsUsed.PRODUCT_ID_KEY,product.get(position).getProduct_id());
        editProduct.putExtras(extras);
        context.startActivity(editProduct);
    }

    private void updateBadgeText(double stockInHand,double stockMin,String productId,TextView badgeNotificationStock){
        double count=Product.getSelectedItemCountWdProductId(productId);
        double displayValue=stockInHand - count;
        if(displayValue > 0 && !productId.isEmpty()) {
            //badgeNotificationStock.setText(String.valueOf(displayValue));

            if(   displayValue<=stockMin) {

                badgeNotificationStock.setBackgroundResource(R.drawable.badge_item_count_red);

            }else if(displayValue<=(stockMin+3) &&
                    displayValue>(stockMin)){
                badgeNotificationStock.setBackgroundResource(R.drawable.badge_item_count_yellow);

            }else if(displayValue>stockMin+3){
                badgeNotificationStock.setBackgroundResource(R.drawable.badge_item_count_green);

            }

        }else if(displayValue<=0 && !productId.isEmpty()) {
           // badgeNotificationStock.setText(String.valueOf(0));//display value as 0 if stock finished
            badgeNotificationStock.setBackgroundResource(R.drawable.badge_item_count_red);

        }

    }

    private void executeStockManagement(double stockInHand,double stockMin,String productId,TextView badgeNotificationStock){


            updateBadgeText(stockInHand,stockMin,productId,badgeNotificationStock);
    }



   public void productAddedTotheCart(double stockHand,double stockMin,String productId,int position,double count,TextView badgeNoti,double amount){
       for(int i=0;i<count;i++) {
           AppFeatures.vibrate(context);

           if(product.get(position).getUnit_of_measure().equalsIgnoreCase("1")) {
               Product.setSelectedList(new Product(product.get(position).getProduct_title(), product.get(position).getUnit_selling_price(), product.get(position).getUnit_buying_price(), product.get(position).getImagePath(),
                       product.get(position).getProduct_id(), product.get(position).getStockInHand(), product.get(position).getStockMinimum(), product.get(position).getUnit_of_measure(),product.get(position).getUnit_of_measure_count(),product.get(position).getSerial_code()),getContext());
           }else{
               //Validator.showToast(getContext(),String.valueOf(product.get(position).getUnit_of_measure_count()));
               Product.setSelectedList(new Product(product.get(position).getProduct_title(), product.get(position).getUnit_selling_price(), product.get(position).getUnit_buying_price(), product.get(position).getImagePath(),
                       product.get(position).getProduct_id(), product.get(position).getStockInHand(), product.get(position).getStockMinimum(), product.get(position).getUnit_of_measure(),product.get(position).getUnit_of_measure_count(),product.get(position).getSerial_code()),getContext());

           }
           ((MainActivity) context).setTotalText_(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount()),
                   String.valueOf(Product.getSelectedItemCount()) + " " + "ITEMS");
           executeStockManagement(stockHand,stockMin,productId,badgeNoti);

       }

   }

   private void productDeletedFromtheCart(double stockHand,double stockMin,String productId,int position,double count,TextView badge){
       for(int i=0;i<count;i++) {
           AppFeatures.vibrate(context);
           ((MainActivity) context).removeList(product.get(position).getProduct_id());

       }
       ((MainActivity) context).setTotalText_(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount()),
               String.valueOf(Product.getSelectedItemCount()) + " " + "ITEMS");
       executeStockManagement(stockHand,stockMin,productId,badge);

   }


    public static  class setImageAsync extends AsyncTask<ProductDetailsListGrid, Integer, Bitmap> {

        long re=0;
        String imagePath="NO_IMAGE";
        ImageView imagevw;
        LinearLayout layout;
        Context ctx;

        private final WeakReference<ImageView> imageViewReference;

        public setImageAsync(Context context, ImageView img){
            imageViewReference = new WeakReference<ImageView>(img);
            ctx = context;
        }
        protected Bitmap doInBackground(ProductDetailsListGrid... itm) {


            if (!isCancelled()) {
                ProductDetailsListGrid item=(ProductDetailsListGrid) itm[0];
                imagePath=item.getImagePath();
                imagevw=item.getImageView();
            }
            return UtilityFunctions.setPicImageRounded(imagePath,imagevw,ctx);


        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Bitmap bitmap) {

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }

        }
    }


}
