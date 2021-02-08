package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.CreateProductActivity;
import com.retailx.dreamdx.retailx.MainActivity;
import com.retailx.dreamdx.retailx.POJO.CreateOrEditFlag;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.POJO.ProductDetailsListGrid;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.StockActivity;
import com.retailx.dreamdx.retailx.UtilityFunctions;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
//import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private String[] mDataset;
    static setImageAsync asynTask;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        TextView productNameTxt ;
        TextView productPriceTxt;
        ImageView imageview ;
        LinearLayout layout ;
        public MyViewHolder(View convertView) {
            super(convertView);
            productNameTxt = (TextView) convertView.findViewById(R.id.id_product_name);
            productPriceTxt = (TextView) convertView.findViewById(R.id.id_prodcut_price);
            imageview = (ImageView) convertView.findViewById(R.id.image_grid);
            layout = (LinearLayout) convertView.findViewById(R.id.layout_linear);
        }
    }

    private ArrayList<Product> product;
    LinearLayout layout;
    private String title="products";
    private Context context;


    public MyAdapter(Activity context, ArrayList<Product> product, String title) {

        this.context=context;
        this.product=product;
        this.title=title;

    }



    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product_edit, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        viewHolder.productNameTxt.setText(product.get(position).getProduct_title());
        final String imagePath = product.get(position).getImagePath();

        if (product.get(position).getUnit_selling_price() > 0.0 && title.equalsIgnoreCase("products"))
            viewHolder.productPriceTxt.setText("Price : "+String.valueOf(product.get(position).getUnit_selling_price()));
        else if(title.equalsIgnoreCase("Stock") && !product.get(position).getProduct_title().equalsIgnoreCase("ADD NEW"))
            viewHolder.productPriceTxt.setText("Stock : "+String.valueOf(product.get(position).getStockInHand()));

        if (!imagePath.equalsIgnoreCase("LAST_IMAGE")) {
            if (!imagePath.equalsIgnoreCase("NO_IMAGE")) {
                asynTask = new setImageAsync(context, viewHolder.imageview);
                asynTask.execute(new ProductDetailsListGrid(product.get(position).getImagePath(), viewHolder.imageview, context));

                /*File f = new File(product.get(position).getImagePath());

                Picasso.with(context)
                        .load(f)
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(viewHolder.imageview);*/

                //Picasso.with(context).load(getImageUri(context,thumbImage)).into(viewHolder.imageview);
                /*File f = new File(product.get(position).getImagePath());
                Uri uri = Uri.fromFile(new File(product.get(position).getImagePath()));
                Picasso.with(context).load("file://"+product.get(position).getImagePath()).into(viewHolder.imageview);*/
               /* Picasso.with(context).load(uri)
                        .resize(40, 40).centerCrop().into(viewHolder.imageview);*/
                //asynTask = new setImageAsync(context, viewHolder.imageview);
                //asynTask.execute(new ProductDetailsListGrid(product.get(position).getImagePath(), viewHolder.imageview, context));

                //imageviewCopy.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_image));

            }

        } else if (imagePath.equalsIgnoreCase("LAST_IMAGE") && position==product.size()-1) {

            viewHolder.imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_circle_outline_black_24dp));

        }else if(imagePath.equalsIgnoreCase("NO_IMAGE")){
            viewHolder.imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.no_image));

        }



        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(title.equalsIgnoreCase("products")){
                    switchToEdit(position,1);
                }else{

                    if(product.get(position).getProduct_id().isEmpty())
                        switchToEdit(position,1);
                    else
                        moveToStockActivity(position);

                }

            }
        });

    }

    private void switchToEdit(int position,int flag){//duplicated if prouctgridadapter

        SharedPreference sharedPreference = SharedPreference.getInstance();
        //if(sharedPreference.getInt(context,ConstantsUsed.CREATE_EDIT_PRODUCT)!=0) {
            MainActivity.shouldShow = "false";
            CreateOrEditFlag.getInstance().setFlag(flag);
            Intent editProduct = new Intent(context, CreateProductActivity.class);
            Bundle extras = new Bundle();
            extras.putString(ConstantsUsed.EDIT_PRODUCT_KEY, ConstantsUsed.EDIT_PRODUCT_VALUE);
            extras.putString(ConstantsUsed.PRODUCT_ID_KEY, product.get(position).getProduct_id());
            editProduct.putExtras(extras);
            context.startActivity(editProduct);
        //}else{
            //Validator.showToast(context,"Sorry! You are not privilaged to create/edit products");

       // }
    }

    private void moveToStockActivity(int position){//duplicated in createproductactivity
        SharedPreference sharedPreference = SharedPreference.getInstance();
        //if(sharedPreference.getInt(context,ConstantsUsed.MANAGE_STOCK)!=0) {

            CreateOrEditFlag.getInstance().setFlag(3);
            Intent stock = new Intent(context, StockActivity.class);
            Bundle extras = new Bundle();
            extras.putString(ConstantsUsed.EDIT_PRODUCT_KEY, ConstantsUsed.EDIT_PRODUCT_VALUE);
            extras.putString(ConstantsUsed.PRODUCT_ID_KEY, product.get(position).getProduct_id());
            stock.putExtras(extras);
            context.startActivity(stock);
        //}else{
          //  Validator.showToast(context,"Sorry! You are not privilaged to create/edit Stock");

       // }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return product.size();
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
