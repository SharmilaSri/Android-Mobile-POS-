package com.retailx.dreamdx.retailx.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.AddCustomerActivity;
import com.retailx.dreamdx.retailx.CreateCategoryActivity;
import com.retailx.dreamdx.retailx.ListItemActivity;
import com.retailx.dreamdx.retailx.MainActivity;
import com.retailx.dreamdx.retailx.POJO.Category;
import com.retailx.dreamdx.retailx.POJO.CreateOrEditFlag;
import com.retailx.dreamdx.retailx.POJO.ListType;
import com.retailx.dreamdx.retailx.POJO.Person;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.SaveTransactionActivity;
import com.retailx.dreamdx.retailx.UserAuthorisationActivity;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.interfaces.SupplierAmountChangedListener;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter {

    private static Activity mContext;
    private ArrayList<String> mList = new ArrayList<>();
    private ArrayList<Person> personList = new ArrayList<>();
    private ArrayList<Category> categoryList = new ArrayList<>();
    DBHelper db;

    public ListAdapter(Activity context, ArrayList<String> list) {
        super(context, 0 , list);
        mContext = context;
        mList = list;
    }


    public ListAdapter(Activity context, ArrayList<Category> list,String test) {
        super(context, 0 , list);
        mContext = context;
        categoryList = list;
    }

    public ListAdapter(Activity context, ArrayList<Person> list, int flag) {
        super(context, 0 , list);
        mContext = context;
        personList = list;
    }





    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        LinearLayout lay=(LinearLayout) listItem.findViewById(R.id.id_list_layout);
        final ImageView delete=(ImageView) listItem.findViewById(R.id.id_delete_product);
        final TextView token = (TextView) listItem.findViewById(R.id.saved_order_id);
        final TextView amount = (TextView) listItem.findViewById(R.id.id_amount);
        db=new DBHelper(mContext);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext)
                        .setTitle(mContext.getResources().getString(R.string.confirm_delete))
                        .setMessage(mContext.getResources().getString(R.string.confirm_delete_msg))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                deleteFromList(position);
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


        if(ListType.getInstance().getType().equalsIgnoreCase("SAVED_ORDER") ) {
            token.setText(mList.get(position));
        }else if(ListType.getInstance().getType().equalsIgnoreCase("CUST") ||
                ListType.getInstance().getType().equalsIgnoreCase("SUP") ) {

            token.setText(personList.get(position).getName());
            amount.setText(String.valueOf(personList.get(position).getAmount()));

        }else if(ListType.getInstance().getType().equalsIgnoreCase("CATEGORY_LIST")){
            token.setText(categoryList.get(position).getTitle());
            delete.setVisibility(View.GONE);

            if(categoryList.get(position).getTitle().equalsIgnoreCase("ADD NEW")){
               // delete.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_add_box_black_24dp));
            }
        }else if(ListType.getInstance().getType().equalsIgnoreCase("USER")){
            token.setText(personList.get(position).getName());
            amount.setText(personList.get(position).getNumber());
            delete.setVisibility(View.GONE);

        }
        lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ListType.getInstance().getType().equalsIgnoreCase("SAVED_ORDER")) {
                    Intent newIntent = new Intent(mContext, SaveTransactionActivity.class);
                    newIntent.putExtra("TOKEN", token.getText().toString());
                    mContext.startActivity(newIntent);
                }else  if(ListType.getInstance().getType().equalsIgnoreCase("CUST")) {
                        Person.setSelectedCustomer(personList.get(position));
                        Intent intent=new Intent(mContext, MainActivity.class);
                        intent.putExtra("token",personList.get(position).getId());
                        mContext.startActivity(intent);
                        //mContext.startActivity(new Intent(mContext, MainActivity.class));
                }else if(ListType.getInstance().getType().equalsIgnoreCase("USER")){
                    Intent newIntent = new Intent(mContext, UserAuthorisationActivity.class);
                    newIntent.putExtra(ConstantsUsed.USER_ID,personList.get(position).getId());
                    mContext.startActivity(newIntent);

                }else if(ListType.getInstance().getType().equalsIgnoreCase("CATEGORY_LIST")){
                    if(position==categoryList.size()-1) {
                        CreateOrEditFlag.getInstance().setFlag(3);
                        mContext.startActivity(new Intent(mContext, CreateCategoryActivity.class));
                    }else
                        moveToEditCategory(categoryList.get(position).getTitle(),categoryList.get(position).getCatId());
                } else if(ListType.getInstance().getType().equalsIgnoreCase("SUP")){
                    AddSupplierAmount dialog = new AddSupplierAmount(mContext, personList.get(position).getId());
                    dialog.setCancelable(false);
                    dialog.setListener(new SupplierAmountChangedListener() {
                        @Override
                        public void amountAdded(double total) {
                            DBHelper db=new DBHelper(mContext);
                            db.updateSupplierAmount(personList.get(position).getId(),total+personList.get(position).getAmount());
                            ((ListItemActivity)mContext).setUpListSuppliers();
                        }

                        @Override
                        public void amountReset(double total) {
                            DBHelper db=new DBHelper(mContext);
                            db.updateSupplierAmount(personList.get(position).getId(),total);
                            ((ListItemActivity)mContext).setUpListSuppliers();
                        }
                    });
                    dialog.show();


                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
                }

            }
        });

        lay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(ListType.getInstance().getType().equalsIgnoreCase("CUST")) {
                    Intent intent=new Intent(mContext, AddCustomerActivity.class);
                    intent.putExtra("token",personList.get(position).getId());
                    mContext.startActivity(intent);
                }
                return false;
            }
        });

        return listItem;
    }

    private void deleteFromList(int position){
        if(ListType.getInstance().getType().equalsIgnoreCase("SAVED_ORDER")) {


            db.deleteSavedTransactionDetails(mList.get(position));
            db.deleteSavedTransactionSummary(mList.get(position));

            ((ListItemActivity)mContext).setUpList();
        }else  if(ListType.getInstance().getType().equalsIgnoreCase("CUST")) {

            DBHelper db=new DBHelper(mContext);
            db.deleteCustomerWdId(personList.get(position).getId());
            ((ListItemActivity)mContext).setUpCustList();

        }else if(ListType.getInstance().getType().equalsIgnoreCase("SUP")) {
            DBHelper db=new DBHelper(mContext);
            db.deleteSupplierWdId(personList.get(position).getId());
            ((ListItemActivity)mContext).setUpListSuppliers();

        }else if(ListType.getInstance().getType().equalsIgnoreCase("CATEGORY_LIST")) {
        }
    }

    private void moveToEditCategory(String tag,String id){
        CreateOrEditFlag.getInstance().setFlag(3);
        Intent editCat=new Intent(getContext(), CreateCategoryActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ConstantsUsed.EDIT_CAT_KEY,tag);
        extras.putString(ConstantsUsed.EDIT_CAT_ID,id);
        editCat.putExtras(extras);
        getContext().startActivity(editCat);
    }
}
