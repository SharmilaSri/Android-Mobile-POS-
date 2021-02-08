package com.retailx.dreamdx.retailx;


import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderFragment;
import com.retailx.dreamdx.retailx.Adapters.AddDiscountDialog;
import com.retailx.dreamdx.retailx.Adapters.AddUnitOfmeasurement;
import com.retailx.dreamdx.retailx.Adapters.DetailsListAdapter;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.interfaces.DiscountAddedListener;
import com.retailx.dreamdx.retailx.interfaces.UnitOfMeasurementChangedListener;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.util.ArrayList;
import java.util.List;

public class BarcodeScanActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener  {

    private static final String TAG = "Barcode-reader";
    double currentValue=0.0;

    // intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String BarcodeObject = "Barcode";
    TextView tvSubTotal,tvTotalDiscount;

    // helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    Button btnTotal,btnItem;

    LinearLayout layoutBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Scan Products");

        setUPView();

        addBarcodeReaderFragment();
    }



    BarcodeReaderFragment readerFragment;
    private void addBarcodeReaderFragment() {
        readerFragment = BarcodeReaderFragment.newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void goToSaveTransaction(View v){
        if(Product.getSelectedItemCount()>=1)
            startActivity(new Intent(this,SelectedProductsListActivity.class));
        else{
            new AlertDialog.Builder(this)
                    .setTitle("EMPTY BASKET")
                    .setMessage("PLEASE SELECT ATLEAST ONE ITEM TO CONTINUE ")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    //.setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public void onScanned(Barcode barcode) {

        displayList( barcode);

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    BottomSheetBehavior sheetBehavior=null;
    private void setUPView(){
        btnTotal=findViewById(R.id.id_btn_total);
        btnItem=findViewById(R.id.id_btn_total_items);
        layoutBottomSheet=findViewById(R.id.bottom_sheet);
        tvSubTotal=findViewById(R.id.text_sub_total);
        tvTotalDiscount=findViewById(R.id.text_bill_discount);

         sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                       // btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                       // btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        list = (ListView) findViewById(R.id.id_products_list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                // String price=String.valueOf(productList.get(position).getUnit_selling_price());
                // ((MainActivity)getActivity()).setTotalText(price);

            }

        });

        setupListBtn("call on create");
    }

    public void closeBottonSheet(View v){
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void toggleBottomSheet(View v) {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //btnBottomSheet.setText("Expand sheet");
        }
    }


    private void setupListBtn(String tag){
        tvTotalDiscount.setText(AppFeatures.format(Product.getDiscount()));
        tvSubTotal.setText(AppFeatures.format(Product.getSelectedProductTotal()));

        btnTotal.setText(String.valueOf(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount())));
        btnItem.setText(String.valueOf(Product.getSelectedItemCount())+" "+"ITEMS");
        DetailsListAdapter adapter = new DetailsListAdapter(this,
                Product.getSelectedListDupliacteRemoved());
        list.setAdapter(adapter);

    }

    private void setupListBtn(){

        tvTotalDiscount.setText(AppFeatures.format(Product.getDiscount()));
        tvSubTotal.setText(AppFeatures.format(Product.getSelectedProductTotal()));

        btnTotal.setText(String.valueOf(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount())));
        btnItem.setText(String.valueOf(Product.getSelectedItemCount())+" "+"ITEMS");
        DetailsListAdapter adapter = new DetailsListAdapter(this,
                Product.getSelectedListDupliacteRemoved());
        list.setAdapter(adapter);

        if(Product.getSelectedItemCount()==0){
            Intent data = new Intent(this,MainActivity.class);

            this.setResult(CommonStatusCodes.SUCCESS, data);
            this.finish();
        }else{
            AppFeatures.vibrate(this);
        }
    }


    ListView list;
    ArrayList<Product>productList=new ArrayList<>();

    private  void displayList(Barcode barcode){
        DBHelper db=new DBHelper(this);
        Cursor barCode=db.getProduct(barcode.rawValue);

        //Product.getProductList(this).clear();
        if((barCode != null) && (barCode.getCount() > 0)) {

           beepAndVibrate();

            while (barCode.moveToNext()) {
                final String product_title = barCode.getString(barCode.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_TITLE)));
                final double unit_selling_price = barCode.getDouble(barCode.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE)));
                final double unit_buying_price = barCode.getDouble(barCode.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE)));
                final String product_id= barCode.getString(barCode.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_ID)));
                final int stockInHand= barCode.getInt(barCode.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_STOCK_IN_HAND)));
                final int stockMin= barCode.getInt(barCode.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_STOCK_MINIMUM)));
                final String unitType = barCode.getString(barCode.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE)));


                if(unitType.equalsIgnoreCase("1")) {
                    Product.setSelectedList(new Product(product_title,
                            unit_selling_price, unit_buying_price
                            , "NO_IMAGE", product_id, stockInHand, stockMin, unitType, 0.0, ""), getApplicationContext());
                    setupListBtn();
                }else if(unitType.equalsIgnoreCase("0")){

                    AddUnitOfmeasurement dialog = new AddUnitOfmeasurement(BarcodeScanActivity.this, "");
                    dialog.setCancelable(false);
                    dialog.setListener(new UnitOfMeasurementChangedListener() {
                        @Override
                        public void unitOfmeasurementAdded(double amount) {

                            Product.setSelectedList(new Product(product_title,
                                    unit_selling_price, unit_buying_price
                                    , "NO_IMAGE", product_id, stockInHand, stockMin, unitType, amount, ""), getApplicationContext());

                            setupListBtn();
                        }
                    });
                    dialog.show();

                  /*  DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int displayWidth = displayMetrics.widthPixels;
                    int displayHeight = displayMetrics.heightPixels;

                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    int dialogWindowWidth = (int) (displayWidth * 0.94f);
                    // Set alert dialog height equal to screen height 80%
                    int dialogWindowHeight = (int) (displayHeight * 0.8f);
                    layoutParams.width = dialogWindowWidth;
                    layoutParams.height = dialogWindowHeight;
                    dialog.getWindow().setAttributes(layoutParams);*/
                }
            }
        }else{
           // Validator.showToast(this,"This product's barcode is not available");
        }

        //setupListBtn();
        //ProductGridAdapter.m

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        startActivity(new Intent(this,MainActivity.class));
    }

    private void beepAndVibrate(){
        readerFragment.playBeep();

        AppFeatures.vibrate(BarcodeScanActivity.this);
    }


    public void removeList(String productId) {

        Product.removeItem(productId,this);
        setupListBtn();
    }

    public void addDiscount(View v){

        closeBottonSheet(null);

        currentValue=Product.getSelectedProductTotal()-Product.getDiscount();

        AddDiscountDialog discount=new AddDiscountDialog(this,currentValue);
        discount.setCancelable(false);
        discount.setTitle("SET DISCOUNT FOR :"+AppFeatures.format(currentValue));
        discount.show();
        discount.setListener(new DiscountAddedListener() {
            @Override
            public void discountAdded(double amount,double total,double rate) {


                addDiscount( amount, total,rate);

            }
        });

    }

    public void clearCart(View v){
        closeBottonSheet(null);

        Product.clearItems();
        Product.setDiscount(0.0,0.0);

        setupListBtn();

    }

    public  void addDiscount(double amount,double total,double rate){
        Product.setDiscount(Product.getDiscount()+amount,Product.getDiscountRate()+rate);

        setupListBtn();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
