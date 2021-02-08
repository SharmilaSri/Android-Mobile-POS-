package com.retailx.dreamdx.retailx.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.POJO.SubscriptionType;
import com.retailx.dreamdx.retailx.R;

import java.util.ArrayList;


public class SubscriptionTypeRecylerAdapter extends RecyclerView.Adapter<SubscriptionTypeRecylerAdapter.ViewHolder> {


    private String[] mDataset;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView subTypesubTypeTitleLabelLabel;
        TextView subTypeTtitle ,subDescription;
        TextView subTypePrice;
        LinearLayout layout ;
        public ViewHolder(View convertView) {
            super(convertView);
            subTypeTtitle = (TextView) convertView.findViewById(R.id.sub_type_title);
            subTypePrice = (TextView) convertView.findViewById(R.id.sub_type_price);
            subTypesubTypeTitleLabelLabel = (TextView) convertView.findViewById(R.id.sub_type_title_label);
            subDescription = (TextView) convertView.findViewById(R.id.sub_type_description);
            layout = (LinearLayout) convertView.findViewById(R.id.layout);

        }
    }

    private ArrayList<SubscriptionType> subTypeList;
    LinearLayout layout;
    private String title="products";
    private Context context;


    public SubscriptionTypeRecylerAdapter(Context context, ArrayList<SubscriptionType> subTypeList, String title) {

        this.context=context;
        this.subTypeList=subTypeList;
        this.title=title;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public SubscriptionTypeRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_subscription_types, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        viewHolder.subTypesubTypeTitleLabelLabel.setText(subTypeList.get(position).getTitleLabel());
        viewHolder.subTypePrice.setText(String.valueOf(subTypeList.get(position).getPrice()));
        viewHolder.subTypeTtitle.setText(String.valueOf(subTypeList.get(position).getTile()));
        viewHolder.subDescription.setText(String.valueOf(subTypeList.get(position).getDiscountLabel()));

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.layout.setSelected(true);
                SubscriptionType.getInstance().setSubObj(new SubscriptionType(subTypeList.get(position).getDescription(),subTypeList.get(position).getPrice(), subTypeList.get(position).getTile(),subTypeList.get(position).getDiscountLabel(), subTypeList.get(position).getId(), subTypeList.get(position).getTitleLabel()));
            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return subTypeList.size();
    }


}
