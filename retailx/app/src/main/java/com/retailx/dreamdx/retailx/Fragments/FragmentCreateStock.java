package com.retailx.dreamdx.retailx.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.retailx.dreamdx.retailx.CreateProductStockActivity;
import com.retailx.dreamdx.retailx.R;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

public class FragmentCreateStock  extends Fragment {

    EditText etStockOnHand,etStockMinimum;
    Button btnSave;
    DBHelper db;
    public FragmentCreateStock(){

    }

    public static FragmentCreateStock newInstance(String catTitle,int flag) {

        Bundle args = new Bundle();
        args.putString(ConstantsUsed.TITLE, catTitle);
        args.putInt(ConstantsUsed.FLAG_EDIT_VIEW,flag);
        FragmentCreateStock fragment = new FragmentCreateStock();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_stock, container, false);

        db=new DBHelper(getActivity());

        etStockOnHand=view.findViewById(R.id.edt_stock_in_hand);
        etStockMinimum=view.findViewById(R.id.et_stock_minimum);
        btnSave=view.findViewById(R.id.btnSaveStock);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!etStockOnHand.getText().toString().isEmpty() ){
                    int stockHand=Integer.parseInt(etStockOnHand.getText().toString());
                    if(!((CreateProductStockActivity) getActivity()).productId.isEmpty()){
                       // db.updateProductStockInHand(((CreateProductStockActivity) getActivity()).productId,stockHand);
                    }


                }

                if(!etStockMinimum.getText().toString().isEmpty()){
                    int stockMin=Integer.parseInt(etStockMinimum.getText().toString());

                    if(!((CreateProductStockActivity) getActivity()).productId.isEmpty()){
                        db.updateProductStockMinimum(((CreateProductStockActivity) getActivity()).productId,stockMin);
                    }

                }

            }
        });
        return  view;
    }


    }
