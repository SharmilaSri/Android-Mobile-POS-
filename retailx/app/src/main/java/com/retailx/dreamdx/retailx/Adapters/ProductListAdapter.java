package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.retailx.dreamdx.retailx.interfaces.ProductItemActionListener;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
//import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter {


    private final Activity context;
    private  ArrayList<Product> product;
    LinearLayout layout;
    static setImageAsync asynTask;
    private String title="products";


    public ProductListAdapter(Activity context, ArrayList<Product> product,String title) {
        super(context, R.layout.grid_item_product, product);

        this.context=context;
        this.product=product;
        this.title=title;

    }
    double total=0.00;

    ProductItemActionListener listener;
    public void setListener(ProductItemActionListener listener){
        this.listener=listener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView productNameTxt ;
        TextView productPriceTxt;
        ImageView imageview ;
        LinearLayout layout ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View result;

        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_product_edit, parent, false);
            viewHolder.productNameTxt = (TextView) convertView.findViewById(R.id.id_product_name);
            viewHolder.productPriceTxt = (TextView) convertView.findViewById(R.id.id_prodcut_price);
            viewHolder.imageview = (ImageView) convertView.findViewById(R.id.image_grid);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout_linear);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }



            viewHolder.productNameTxt.setText(product.get(position).getProduct_title());
            final String imagePath = product.get(position).getImagePath();

            if (product.get(position).getUnit_selling_price() > 0.0 && title.equalsIgnoreCase("products"))
                viewHolder.productPriceTxt.setText(String.valueOf(product.get(position).getUnit_selling_price()));
            else if(title.equalsIgnoreCase("stock") && !product.get(position).getProduct_title().equalsIgnoreCase("ADD NEW"))
                viewHolder.productPriceTxt.setText(String.valueOf(product.get(position).getStockInHand()));

        if (!imagePath.equalsIgnoreCase("LAST_IMAGE")) {
            if (!imagePath.equalsIgnoreCase("NO_IMAGE")) {

                File f = new File(product.get(position).getImagePath());

               /* Picasso.with(context)
                        .load(f)
                        .placeholder(R.drawable.login_man)
                        .error(R.drawable.login_man)
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

            viewHolder.imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.icong_plus_bg));

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

            return convertView;

    };

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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


    private void switchToEdit(int position,int flag){//duplicated if prouctgridadapter
        MainActivity.shouldShow="false";
        CreateOrEditFlag.getInstance().setFlag(flag);
        Intent editProduct=new Intent(context, CreateProductActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ConstantsUsed.EDIT_PRODUCT_KEY,ConstantsUsed.EDIT_PRODUCT_VALUE);
        extras.putString(ConstantsUsed.PRODUCT_ID_KEY,product.get(position).getProduct_id());
        editProduct.putExtras(extras);
        context.startActivity(editProduct);
    }

    private void moveToStockActivity(int position){//duplicated in createproductactivity
        CreateOrEditFlag.getInstance().setFlag(3);
        Intent stock=new Intent(context, StockActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ConstantsUsed.EDIT_PRODUCT_KEY,ConstantsUsed.EDIT_PRODUCT_VALUE);
        extras.putString(ConstantsUsed.PRODUCT_ID_KEY,product.get(position).getProduct_id());
        stock.putExtras(extras);
        context.startActivity(stock);
    }
}
