package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.Adapters.AddDiscountDialog;
import com.retailx.dreamdx.retailx.Adapters.DetailsListAdapter;
import com.retailx.dreamdx.retailx.POJO.Person;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.POJO.ProductDetailsListGrid;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.interfaces.DiscountAddedListener;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.util.ArrayList;

public class SelectedProductsListActivity extends AppCompatActivity {
    ListView list;
    Button totalTxt,totalItem;
    LinearLayout layoutBottomSheet;
    double currentValue=0.0;
    TextView tvSubTotal,tvTotalDiscount;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_products_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.bill));

        db=new DBHelper(this);

        currentValue=Product.getSelectedProductTotal();
        setUpListBtnInitial();


        setUpSavedOrder();
    }

    String token="";
    private void setUpSavedOrder(){
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
            token = extras.getString("TOKEN","");

        if(token!=null && !token.isEmpty()){
            Cursor infoCur = null;
            Cursor productDetCur=null;

            String title="";
            int stockInHand=0;
            int stockMin=0;
            double unitPrice=0.0;
            double unitPriceBuying=0.0;
            String imagePath="";
            int totalUnit=0;
            String unitType="0";
            String serialCode="";

            try {
                infoCur = db.getTransactionDetailsWdTransId(token.trim(),this);
                while (infoCur.moveToNext()) {
                    String productId= infoCur.getString(infoCur.getColumnIndex(ConstantsUsed.COLUMN_PRODUCT_ID));
                    totalUnit=infoCur.getInt(infoCur.getColumnIndex(ConstantsUsed.COLUMN_TOTAL_ITEM_UNITS));
                    //Toast.makeText(this,productId,Toast.LENGTH_SHORT).show();

                    productDetCur= db.getProductDetailsWdProductId(productId);
                    while (productDetCur.moveToNext()) {
                        title = productDetCur.getString(productDetCur.getColumnIndex(ConstantsUsed.COLUMN_PRODUCT_TITLE));
                        stockInHand = productDetCur.getInt(productDetCur.getColumnIndex(ConstantsUsed.COLUMN_PRODUCT_STOCK_IN_HAND));
                        stockMin = productDetCur.getInt(productDetCur.getColumnIndex(ConstantsUsed.COLUMN_PRODUCT_STOCK_MINIMUM));
                        unitPrice = productDetCur.getDouble(productDetCur.getColumnIndex(ConstantsUsed.COLUMN_PRODUCT_SELLING_PRICE));
                        unitPriceBuying = productDetCur.getDouble(productDetCur.getColumnIndex(ConstantsUsed.COLUMN_PRODUCT_BUYING_PRICE));
                        imagePath = productDetCur.getString(productDetCur.getColumnIndex(ConstantsUsed.COLUMN_PRODUCT_IMAGE_PATH));
                        unitType=productDetCur.getString(productDetCur.getColumnIndex(ConstantsUsed.COLUMN_PRODUCT_UNIT_OF_MEASURE));
                        serialCode=productDetCur.getString(productDetCur.getColumnIndex(ConstantsUsed.COLUMN_PRODUCT_SERIAL_CODE));

                    }


                    for (int i=0;i<totalUnit;i++) {
                        Product.setSelectedList(new Product(title, unitPrice,unitPriceBuying, imagePath, productId, stockInHand, stockMin,unitType,0.0,serialCode),getApplicationContext());
                    }
                }
            }catch (Exception e){
                Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }finally {
                displayListBtnValue();
            }
        }
    }

    public void clearCart(View v){
        closeBottonSheet();

        Product.clearItems();
        Product.setDiscount(0.0,0.0);

        setUpListBtn();

    }

    public void closeBottonSheet(View v){
        closeBottonSheet();
    }

    private void closeBottonSheet(){
        totalItem.setEnabled(true);
        totalTxt.setEnabled(true);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void addDiscount(View v){

        closeBottonSheet();

        SharedPreference sharedPreference = SharedPreference.getInstance();

        if(sharedPreference.getInt(this, ConstantsUsed.CREATE_DISCOUNT)!=0){
            currentValue = Product.getSelectedProductTotal() - Product.getDiscount();

            AddDiscountDialog discount = new AddDiscountDialog(this, currentValue);
            discount.setCancelable(false);
            discount.setTitle("SET DISCOUNT FOR :" + AppFeatures.format(currentValue));
            discount.show();


            discount.setListener(new DiscountAddedListener() {
                @Override
                public void discountAdded(double amount, double total, double rate) {


                    addDiscount(amount, total, rate);

                }
            });
        }else{
            Validator.showToast(this,"Your are not privilaged to give discounts");
        }

    }


    public  void addDiscount(double amount,double total,double rate){
        Product.setDiscount(Product.getDiscount()+amount,rate);

        setUpListBtn();

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

    BottomSheetBehavior sheetBehavior=null;
    private void setUpListBtnInitial() {
        list = findViewById(R.id.id_products_list);
        totalTxt = findViewById(R.id.id_btn_total);
        totalItem = findViewById(R.id.id_btn_total_items);
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
                        totalItem.setEnabled(false);
                        totalTxt.setEnabled(false);
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

        displayListBtnValue();

    }

    private void displayListBtnValue(){
        DetailsListAdapter adapter = new DetailsListAdapter(this,
                Product.getSelectedListDupliacteRemoved());
        list.setAdapter(adapter);


        totalTxt.setText(String.valueOf(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount())));
        totalItem.setText(String.valueOf(Product.getSelectedItemCount())+" "+"ITEMS");

        tvTotalDiscount.setText(AppFeatures.format(Product.getDiscount()));
        tvSubTotal.setText(AppFeatures.format(Product.getSelectedProductTotal()));
    }

    private void setUpListBtn() {
        displayListBtnValue();

        if(Product.getSelectedItemCount()==0){
            Product.setDiscount(0.0,0.0);
            Intent data = new Intent(this,MainActivity.class);
            startActivity(data);
        }else{
                AppFeatures.vibrate(this);
        }

    }




    public void goToSaveTransaction(View v){

        if(token!=null && !token.isEmpty()){
            returnSalesDialog();
        }else
            startActivity(new Intent(this,SaveTransactionActivity.class));
    }

    private void returnSalesDialog(){

    }

    public void returnSales(){
        String sarId=ConstantsUsed.SALES_RETURN+ AppFeatures.getTimeStamp();

        try
        {
            DBHelper db=new DBHelper(this);
            String custId="0";
            double itemTotalCount=Product.getSelectedItemCount();
            String date=AppFeatures.getTimeStamp("date");
            double productTotal=Product.getSelectedProductTotal();
            double discount=Product.getDiscount();
            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);



            if(Person.getSelectedCustomer()!=null)
                custId=Person.getSelectedCustomer().getId();

            db.insertTransactionSummary(userId,custId,
                    sarId,itemTotalCount,date,productTotal,0.0,productTotal,discount,"SALES_RETURN",1,2,1);


            ArrayList<ProductDetailsListGrid> seldDuplicateRmdList=new ArrayList<>();
            seldDuplicateRmdList=Product.getSelectedListDupliacteRemoved();
            for(int i=0;i<seldDuplicateRmdList.size();i++){
                double itemCount=seldDuplicateRmdList.get(i).getItemCount();
                double unitPrice=seldDuplicateRmdList.get(i).getUnitPrice();
                double unitPriceBuying=seldDuplicateRmdList.get(i).getUnitPrice();
                double total=itemCount * unitPrice;
                double totalBuying=itemCount * unitPriceBuying;
                String productId=seldDuplicateRmdList.get(i).getProductId();

                db.insertTransactionDetails(sarId,productId,itemCount,total,totalBuying,AppFeatures.getTimeStamp("date"),0.0,1);

                double stockInhand=seldDuplicateRmdList.get(i).getStockInHand();
                double enterValue=0;
                enterValue=stockInhand+itemCount;
                if(enterValue>0)
                    db.updateProductStockInHand(productId,enterValue);
                else
                    db.updateProductStockInHand(productId,0);

            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

        }finally {
            moveToDoneScreen();
        }
    }

    public void moveToDoneScreen(){

        startActivity(new Intent(getApplicationContext(),DoneScreenActivity.class));

    }
    public void removeList(String productId) {
      //  Toast.makeText(this,"called",Toast.LENGTH_SHORT).show();

        Product.removeItem(productId,this);
        setUpListBtn();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if  (id == android.R.id.home) {
            startActivity(new Intent(SelectedProductsListActivity.this,MainActivity.class));
        }

        return  true;
    }

    @Override
    public void onBackPressed() {

        if(token!=null && !token.isEmpty()){
            Validator.showToast(this,"IT IS a sales return. Remove items from the list");
        }else{
            startActivity(new Intent(this,MainActivity.class));

        }
    }


}
