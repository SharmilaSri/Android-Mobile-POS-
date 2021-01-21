package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.AboutUs;
import com.retailx.dreamdx.retailx.ChangeLanguageActivity;
import com.retailx.dreamdx.retailx.HelpActivity;
import com.retailx.dreamdx.retailx.POJO.CreateOrEditFlag;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.ResetPasswordActivity;
import com.retailx.dreamdx.retailx.UtilityFunctions;

import java.util.ArrayList;


public class SettingListAdapter extends RecyclerView.Adapter<SettingListAdapter.viewHolder> {

    public static class viewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView itemNametxt;

        public viewHolder(View convertView) {
            super(convertView);
            itemNametxt = (TextView) convertView.findViewById(R.id.labelId);

        }
    }

    private ArrayList<String> list;
    LinearLayout layout;
    private Context context;


    public SettingListAdapter(Activity context, ArrayList<String> list) {

        this.context=context;
        this.list=list;

    }



    // Create new views (invoked by the layout manager)
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_setting, parent, false);

        viewHolder vh = new viewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(viewHolder viewHolder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        viewHolder.itemNametxt.setText(list.get(position));
        viewHolder.itemNametxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    context.startActivity(new Intent(context, ResetPasswordActivity.class));

                }
               if(position==1){
                    context.startActivity(new Intent(context, ChangeLanguageActivity.class));

                }else if(position==2){
                    CreateOrEditFlag.getInstance().setFlag(1);
                    context.startActivity(new Intent(context, HelpActivity.class));

                }else if(position==3){
                   // context.startActivity(new Intent(context, AboutUs.class));
                   new AlertDialog.Builder(context)
                           .setTitle(context.getResources().getString(R.string.warning))
                           .setMessage(context.getResources().getString(R.string.warning_msg))

                           // Specifying a listener allows you to take an action before dismissing the dialog.
                           // The dialog is automatically dismissed when a dialog button is clicked.
                           .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {

                                   UtilityFunctions.logout(context);

                               }
                           })

                           .setNegativeButton(context.getResources().getString(R.string.no), null)
                           .setIcon(R.drawable.ic_exit_to_app_black_24dp)
                           .show();
                }/*else if(position==4){
                    new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.warning))
                            .setMessage(context.getResources().getString(R.string.warning_msg))

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    UtilityFunctions.logout(context);

                                }
                            })

                            .setNegativeButton(context.getResources().getString(R.string.no), null)
                            .setIcon(R.drawable.ic_exit_to_app_black_24dp)
                            .show();
                }*/
            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

}
