package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.BarcodeScanActivity;
import com.retailx.dreamdx.retailx.POJO.OpenBalReductionDetails;
import com.retailx.dreamdx.retailx.POJO.OpeningBalance;
import com.retailx.dreamdx.retailx.POJO.ProductDetailsListGrid;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.SelectedProductsListActivity;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.interfaces.DiscountAddedListener;
import com.retailx.dreamdx.retailx.utils.AppFeatures;

import java.util.ArrayList;

public class DetailsListAdapter
        extends ArrayAdapter {


    private final Activity context;
    private  ArrayList<ProductDetailsListGrid> product;
    private  ArrayList<String> carList;
    int flag=0;
    String flagInHistoryScreen="0";//flag 1 to hide delete button
    private  ArrayList<OpenBalReductionDetails> details;
    private  static double totalAmountEnteredForReason=0.0;
    DBHelper db=null;
    TextView productPriceTxt=null;

    DiscountAddedListener listener;
    public void setListener(DiscountAddedListener listener){
        this.listener=listener;
    }

    public DetailsListAdapter(Activity context, ArrayList<ProductDetailsListGrid> product) {
        super(context, R.layout.list_item_product, product);

        this.context=context;
        this.product=product;

    }

    public DetailsListAdapter(Activity context, ArrayList<ProductDetailsListGrid> product,String flag) {
        super(context, R.layout.list_item_product, product);

        this.context=context;
        this.product=product;
        this.flagInHistoryScreen=flag;

    }

    public DetailsListAdapter(Activity context, ArrayList<OpenBalReductionDetails> details, int flag) {
        super(context, R.layout.list_item_product, details);

        this.context=context;
        this.details=details;
        this.flag=flag;
        db=new DBHelper(context);
        totalAmountEnteredForReason= OpeningBalance.getInstance().getEnteredOpeningBal();

    }


    double total=0.00;

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=null;
        if(flag==0) {
            rowView = inflater.inflate(R.layout.list_item_product, null, true);

            TextView productNameTxt = (TextView) rowView.findViewById(R.id.id_product_name);
            productPriceTxt = (TextView) rowView.findViewById(R.id.id_prodcut_price);
            TextView productQuantity = (TextView) rowView.findViewById(R.id.id_product_quantity);
            final TextView unitPrice = (TextView) rowView.findViewById(R.id.id_unit_price);
            ImageView delete = (ImageView) rowView.findViewById(R.id.id_delete_product);

            int productCount = 0;
            //productCount = Product.getse.get(i).getItemCount();
            double totalOneItemPrice = 0.0;
            totalOneItemPrice = product.get(position).getItemTotal();
            unitPrice.setText(AppFeatures.format(product.get(position).getUnitPrice()));
            productNameTxt.setText(product.get(position).getTitle());
            productPriceTxt.setText(AppFeatures.format(product.get(position).getUnitPrice() * product.get(position).getItemCount()));
            productQuantity.setText(String.valueOf(product.get(position).getItemCount()));
            if(product.get(position).getMeasureCount()>0){
                productPriceTxt.setText(AppFeatures.format(product.get(position).getUnitPrice() * product.get(position).getMeasureCount()));

            }

            if(flagInHistoryScreen.equalsIgnoreCase("1")){
                delete.setVisibility(View.GONE);

            }else {

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (context.getClass().getName().equals(SelectedProductsListActivity.class.getName()))
                            ((SelectedProductsListActivity) context).removeList(product.get(position).getProductId());
                        else if (context.getClass().getName().equals(BarcodeScanActivity.class.getName()))
                            ((BarcodeScanActivity) context).removeList(product.get(position).getProductId());

                    }
                });


                LinearLayout linear = rowView.findViewById(R.id.id_list_layout);
                linear.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final double currentUnitPrice = product.get(position).getUnitPrice();
                        AddDiscountDialog discount = new AddDiscountDialog(context, currentUnitPrice);
                        discount.setCancelable(false);
                        discount.setTitle("SET DISCOUNT FOR :" + AppFeatures.format(currentUnitPrice));
                        discount.show();
                        discount.setListener(new DiscountAddedListener() {
                            @Override
                            public void discountAdded(double amount, double total,double rate) {
                                product.get(position).setUnitPrice(total);

                                ((SelectedProductsListActivity) context).addDiscount(amount * product.get(position).getItemCount(), total,rate);

                            }
                        });
                        return false;
                    }
                });


            }

        }
        return rowView;

    };
}
