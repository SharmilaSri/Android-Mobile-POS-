package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.POJO.TransactionDeatils;
import com.retailx.dreamdx.retailx.POJO.TransactionSummary;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.TransHistoryActivity;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.util.ArrayList;

public class TransactionReceyclerAdapter extends RecyclerView.Adapter<TransactionReceyclerAdapter.MyViewHolder> {


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        TextView transId;
        TextView transDate;
        TextView totalAmount;
        LinearLayout delete;
        //ImageView imageview ;
        LinearLayout layout ;
        public MyViewHolder(View convertView) {
            super(convertView);
            layout = (LinearLayout) convertView.findViewById(R.id.layout_linear);

            transId = (TextView) convertView.findViewById(R.id.id_trans_id);
            transDate = (TextView) convertView.findViewById(R.id.id_trans_date);
            delete = (LinearLayout) convertView.findViewById(R.id.deleteIcon);
            totalAmount = (TextView) convertView.findViewById(R.id.id_trans_total_amount);
            //imageview = (ImageView) convertView.findViewById(R.id.image_grid);
        }
    }

    private ArrayList<TransactionSummary> tranSummary;
    private ArrayList<TransactionDeatils> transDetails;
    LinearLayout layout;
    private String title="products";
    private Context context;
    int flag=0;//0-summary,1 for details


    public TransactionReceyclerAdapter(Activity context, ArrayList<TransactionSummary> tranSummary) {

        this.context=context;
        this.tranSummary=tranSummary;
    }




    // Create new views (invoked by the layout manager)
    @Override
    public TransactionReceyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trans_summary, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(flag==0) {


            viewHolder.transId.setText(tranSummary.get(position).getTransactionId());
            viewHolder.totalAmount.setText(String.valueOf(tranSummary.get(position).getPaymentType()));
            viewHolder.transDate.setText(tranSummary.get(position).getTransactionDate());
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.confirm_delete))
                            .setMessage(context.getResources().getString(R.string.confirm_delete_msg))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                    DBHelper db=new DBHelper(context);
                                    db.deleteTransactionDetails(tranSummary.get(position).getTransactionId());
                                    db.deleteTransactionSummary(tranSummary.get(position).getTransactionId());
                                    tranSummary.remove(position);
                                    notifyDataSetChanged();
                                    ((TransHistoryActivity) context).setUpbtnTotal();
                                    Validator.showToast(context,"Deleted");
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(R.drawable.ic_delete_black_24dp)
                            .show();

                }
            });
            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    transactionDetailsDialog dialog = new transactionDetailsDialog((TransHistoryActivity) context, tranSummary.get(position));
                    dialog.show();

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    ((TransHistoryActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int displayWidth = displayMetrics.widthPixels;
                    int displayHeight = displayMetrics.heightPixels;

                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    int dialogWindowWidth = (int) (displayWidth * 0.94f);
                    // Set alert dialog height equal to screen height 90%
                    int dialogWindowHeight = (int) (displayHeight * 0.9f);
                    layoutParams.width = dialogWindowWidth;
                    layoutParams.height = dialogWindowHeight;
                    dialog.getWindow().setAttributes(layoutParams);
                }
            });
        }

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(flag==0)
            return tranSummary.size();
        else
            return transDetails.size();
    }


}
