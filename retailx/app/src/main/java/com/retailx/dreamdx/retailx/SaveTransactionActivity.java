package com.retailx.dreamdx.retailx;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.Adapters.AddCheckInfoDialog;
import com.retailx.dreamdx.retailx.Adapters.PaymentMethodGridAdapter;
import com.retailx.dreamdx.retailx.POJO.ChequeDetails;
import com.retailx.dreamdx.retailx.POJO.Invoice;
import com.retailx.dreamdx.retailx.POJO.PaymentMethod;
import com.retailx.dreamdx.retailx.POJO.Person;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.POJO.ProductDetailsListGrid;
import com.retailx.dreamdx.retailx.POJO.TransactionSummary;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.interfaces.ChequeInfoAddedListener;
import com.retailx.dreamdx.retailx.interfaces.PaymentMethodSelectedListener;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class SaveTransactionActivity extends AppCompatActivity {
    private GridView payMthodGrid;
    TextView txtViewbillAmount;
    TextInputEditText EdtTxtreceivedAmount;
    TextView btnBalance;
    double Balance=0.0;
    Button btnSaveOrder;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_save_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.summary));

        db=new DBHelper(this);

        setUpSavedOrder();

        setUpPaymentMethodGrid();

        setUPViews();
        transId="";


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
                setUPViews();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!transId.isEmpty()) {

            showStartANewSaleDialog(this);
        }
    }



    private void setUPViews(){

        txtViewbillAmount=findViewById(R.id.txt_bill_amount);
        EdtTxtreceivedAmount=findViewById(R.id.id_received_amount);
        btnSaveOrder=findViewById(R.id.id_txt_save_order);


        EdtTxtreceivedAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // Toast.makeText(SaveTransactionActivity.this,"before",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(EdtTxtreceivedAmount.getText().toString()!=null  && ! EdtTxtreceivedAmount.getText().toString().isEmpty()  ) {
                        if(Double.parseDouble(EdtTxtreceivedAmount.getText().toString())>0)
                            Balance=Double.parseDouble(EdtTxtreceivedAmount.getText().toString())
                                    - (Product.getSelectedProductTotal()-Product.getDiscount());
                            btnBalance.setText(AppFeatures.format(Balance));
                    }else{
                        btnBalance.setText("00");
                    }
                }catch (Exception e){
                    Toast.makeText(SaveTransactionActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
               // Toast.makeText(SaveTransactionActivity.this,"afterTextChanged",Toast.LENGTH_SHORT).show();

            }
        });
        btnBalance=findViewById(R.id.id_txt_balance);

        txtViewbillAmount.setText(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount()));

        if(!token.isEmpty())
            EdtTxtreceivedAmount.setText(String.valueOf(Product.getSelectedProductTotal()-Product.getDiscount()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_trans, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if  (id == android.R.id.home) {
            startActivity(new Intent(SaveTransactionActivity.this,MainActivity.class));
        }

        return  true;
    }

    HashMap<String,BluetoothDevice> availableList=new HashMap<String,BluetoothDevice>();


    ArrayList<ProductDetailsListGrid> selectedList=null;



    void closeBT() throws IOException {
        try {
            stopWorker = true;
            if(mmOutputStream!=null)
                mmOutputStream.close();
            if(mmInputStream!=null)
                mmInputStream.close();
            if(mmSocket!=null)
                mmSocket.close();
            //myLabel.setText("Bluetooth Closed");
            startNewSale();

        } catch (Exception e) {
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    // this will find a bluetooth printer device
   /* void findBT(BluetoothAdapter mBluetoothAdapter) {

        try {

            bluetoothNames = new ArrayList<>();
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth device found.", Toast.LENGTH_LONG).show();
            } else if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            } else {

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {

                        bluetoothNames.add(device.getName());
                        availableList.put(device.getName(), device);


                        // RPP300 is the name of the bluetooth printer device
                        // we got this name from the list of paired devices
                        if (device.getName().equals("printer001")) {
                            mmDevice = device;
                            break;
                        }
                    }
                }
            }

            }catch(Exception e){
                Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
    }*/

    private void setUpPaymentMethodGrid(){
        payMthodGrid=findViewById(R.id.id_paymethod_grid);
        ArrayList<PaymentMethod> paylist=new ArrayList<>();

        paylist.add(new PaymentMethod(R.drawable.cash_btn_selector,"CASH"));
        paylist.add(new PaymentMethod(R.drawable.deb_btn_selector,"DEBIT"));
        paylist.add(new PaymentMethod(R.drawable.credit_btn_selector,"CREDIT"));
        paylist.add(new PaymentMethod(R.drawable.check_btn_selector,"CHEQUE"));
        paylist.add(new PaymentMethod(R.drawable.qr_code,"QR"));
        paylist.add(new PaymentMethod(R.drawable.other,"OTHER"));

        PaymentMethod.setPaymentMethodList(paylist);

        PaymentMethodGridAdapter gridAdapter=new PaymentMethodGridAdapter(this,PaymentMethod.getPaymentMethodList());
        gridAdapter.setListener(new PaymentMethodSelectedListener() {
            @Override
            public void notifyPaymentMethodSelected(String method) {

                PaymentMethod.getInstance().setMethodName(method);
                paymentMethodSelected(method);

            }
        });
        payMthodGrid.setAdapter(gridAdapter);



    }



    private void paymentMethodSelected(final String methodName){

        if(methodName.equalsIgnoreCase("CASH")) {


            if(EdtTxtreceivedAmount.getText().toString()!=null  && ! EdtTxtreceivedAmount.getText().toString().isEmpty() ){
                Product.setReceivedAmount(Double.parseDouble(EdtTxtreceivedAmount.getText().toString()));
                Product.setBalance(Balance);

                if(Balance<0){
                    Validator.showToast(SaveTransactionActivity.this,"Invalid Balance");
                }else{
                    Invoice.getInstance().setBalance(Balance);
                    Invoice.getInstance().setReceivedAmount(Double.parseDouble(EdtTxtreceivedAmount.getText().toString()));
                    transId=ConstantsUsed.TRANSACTION+ AppFeatures.getTimeStamp();
                    saveOrder(methodName,transId);
                }
            }else{
                EdtTxtreceivedAmount.setError(getResources().getString(R.string.empty_field));
                EdtTxtreceivedAmount.setHintTextColor(Color.RED);
            }

        }else if(methodName.equalsIgnoreCase("CHEQUE")){

            String cheque=SharedPreference.getInstance().getValue(SaveTransactionActivity.this, "cheque");
            if(cheque.equalsIgnoreCase("1")) {
                AddCheckInfoDialog dialog = new AddCheckInfoDialog(SaveTransactionActivity.this);
                dialog.setCancelable(false);
                dialog.setListener(new ChequeInfoAddedListener() {
                    @Override
                    public void checkInfoAdded(ChequeDetails obj) {

                        Product.setReceivedAmount(obj.getChequeAmount());
                        Product.setBalance(0);

                        String chqueId="C"+AppFeatures.getTimeStamp();
                        transId=ConstantsUsed.TRANSACTION+ AppFeatures.getTimeStamp();
                        String userId = SharedPreference.getInstance().getValue(SaveTransactionActivity.this, Constants.USER_ID);
                        db.insertChequeDetails(userId,chqueId,obj.getChequeNo(),obj.getChequeAmount()
                        ,obj.getChequeBank(),obj.getChequeDate(),transId,"PENDING",1);

                        saveOrder(methodName,transId);

                    }
                });
                dialog.show();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
            } else
                Validator.showToast(SaveTransactionActivity.this,"Please subscribe to use this feature");


        }else if(methodName.equalsIgnoreCase("CREDIT")){

            String credit=SharedPreference.getInstance().getValue(SaveTransactionActivity.this, "creditcard");
            if(credit.equalsIgnoreCase("1")) {
                String creditId="C"+AppFeatures.getTimeStamp();
                transId=ConstantsUsed.TRANSACTION+ AppFeatures.getTimeStamp();
                String userId = SharedPreference.getInstance().getValue(SaveTransactionActivity.this, Constants.USER_ID);
                db.insertCreditDetails(userId,creditId, transId, 1);
                saveOrder(methodName,transId);
            } else
                Validator.showToast(SaveTransactionActivity.this,"Please subscribe to use this feature");

        }

    }

    String custId="0";
    String transId="";
    private void  saveOrder(String method,String transId){//this is called when transaction is completed and save details to DB
        try{
            if(!token.isEmpty()){
                db.deleteTransactionSummary(token);
                db.deleteTransactionDetails(token);
                transId=token;
            }


            double itemTotalCount=Product.getSelectedItemCount();
            String date=AppFeatures.getTimeStamp("date");
            double productTotal=Product.getSelectedProductTotal();
            double discount=Product.getDiscount();

            if(Person.getSelectedCustomer()!=null)
                custId=Person.getSelectedCustomer().getId();

            TransactionSummary transSumObj=TransactionSummary.getInstance();
            transSumObj.setCustId(custId);
            transSumObj.setTransactionId(transId);
            transSumObj.setTotalItemCount(itemTotalCount);
            transSumObj.setTransactionDate(date);
            transSumObj.setTotalAmount(productTotal);
            transSumObj.setTotalDiscount(discount);
            transSumObj.setPaymentType(method);

            Invoice.getInstance().setTransactionSummaryObj(transSumObj);

            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

            db.insertTransactionSummary(userId,custId,transId,itemTotalCount,date,productTotal,0.0,productTotal,discount,method,1,0,1);

            ArrayList<ProductDetailsListGrid>seldDuplicateRmdList=new ArrayList<>();
            seldDuplicateRmdList=Product.getSelectedListDupliacteRemoved();
            for(int i=0;i<seldDuplicateRmdList.size();i++){
                double itemCount=seldDuplicateRmdList.get(i).getItemCount();
                double unitPrice=seldDuplicateRmdList.get(i).getUnitPrice();
                double unitBuyingPrice=seldDuplicateRmdList.get(i).getUnit_buying_price();
                double total=itemCount * unitPrice;
                double totalBuying=itemCount*unitBuyingPrice;
                String productId=seldDuplicateRmdList.get(i).getProductId();

                db.insertTransactionDetails(transId,productId,itemCount,total,totalBuying,AppFeatures.getTimeStamp("date"),0.0,1);

                double stockInhand=seldDuplicateRmdList.get(i).getStockInHand();
                double enterValue=0;
                enterValue=stockInhand-itemCount;

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

    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    public  void showStartANewSaleDialog(Context ctx){
        new AlertDialog.Builder(ctx)
                .setMessage("DO YOU WANT TO START A NEW SALE ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startNewSale();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void startNewSale(){


        Product.clearItems();
        Product.setDiscount(0.0,0.0);
        Person.resetCustomer();

        // Continue with delete operation
        Intent i = new Intent(SaveTransactionActivity.this,MainActivity.class);
        startActivity(i);
    }



    private void saveOrderToDB(String transId,String paymentType,int flag){
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
                    transId,itemTotalCount,date,productTotal,0.0,productTotal,discount,paymentType,1,flag,1);


            ArrayList<ProductDetailsListGrid>seldDuplicateRmdList=new ArrayList<>();
            seldDuplicateRmdList=Product.getSelectedListDupliacteRemoved();
            for(int i=0;i<seldDuplicateRmdList.size();i++){
                double itemCount=seldDuplicateRmdList.get(i).getItemCount();
                double unitPrice=seldDuplicateRmdList.get(i).getUnitPrice();
                double unitPriceBuying=seldDuplicateRmdList.get(i).getUnitPrice();
                double total=itemCount * unitPrice;
                double totalBuying=itemCount * unitPriceBuying;
                String productId=seldDuplicateRmdList.get(i).getProductId();

                db.insertTransactionDetails(transId,productId,itemCount,total,totalBuying,AppFeatures.getTimeStamp("date"),0.0,1);

                double stockInhand=seldDuplicateRmdList.get(i).getStockInHand();
                double enterValue=0;
                enterValue=stockInhand-itemCount;
                if(enterValue>0)
                    db.updateProductStockInHand(productId,enterValue);
                else
                    db.updateProductStockInHand(productId,0);

            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

        }finally {
            //createAndDisplayPdf(transId);
        }
    }



    boolean invoicePrinting=false;
    public void openConnection(){

        transId = ConstantsUsed.TRANSACTION+ AppFeatures.getTimeStamp();

        saveOrderToDB(transId,"SAVE_ORDER",1);

        BluetoothAdapter mBluetoothAdapter;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if (mBluetoothAdapter == null) {
            Validator.showToast(this,"Your device does not support bluetooth");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Validator.showToast(this,"PLEASE ENABLE BLUETOOTH");
            }else if(mBluetoothAdapter.isEnabled()){

                findBTNew(mBluetoothAdapter);


            }
        }




    }

        ArrayList<String> saveOrderOptionList;
        String unitType="1";//1 for unit or 0 for fraction
    public void saveOrderDialog(View v){
            saveOrderOptionList =new ArrayList<>();//reset
            saveOrderOptionList.add("Print Token & start a new sale");
            saveOrderOptionList.add("Save & start a new sale");
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(SaveTransactionActivity.this);
            builderSingle.setIcon(android.R.drawable.ic_menu_add);
            builderSingle.setTitle("Save order options");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SaveTransactionActivity.this,
                    android.R.layout.select_dialog_singlechoice, saveOrderOptionList);


            builderSingle.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0){
                        dialog.dismiss();
                        openConnection();
                    }else if(which==1){
                        dialog.dismiss();
                        transId = ConstantsUsed.TRANSACTION+ AppFeatures.getTimeStamp();
                        saveOrderToDB(transId,"SAVE_ORDER",1);
                        startNewSale();
                    }
                }
            });
            builderSingle.show();
        }


    // this will find a bluetooth printer device
    void findBTNew(BluetoothAdapter mBluetoothAdapter) {

        try {

            bluetoothNames=new ArrayList<>();
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    bluetoothNames.add(device.getName());
                    availableList.put(device.getName(),device);
                }

                showListDialog();

            }else{
                Validator.showToast(SaveTransactionActivity.this,"NO printers are paired. Please pair a printer");
            }

        }catch(Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void showListDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SaveTransactionActivity.this);
        builderSingle.setIcon(android.R.drawable.ic_menu_add);
        builderSingle.setTitle("SELECT PRINTER : ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SaveTransactionActivity.this, android.R.layout.select_dialog_singlechoice,bluetoothNames);


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String deviceName = arrayAdapter.getItem(which);
                mmDevice=availableList.get(deviceName);

                try {
                    dialog.dismiss();
                    openBT();

                    PrintInvoiceAsync async = new PrintInvoiceAsync();
                    async.execute();

                }catch (IOException ie){

                }catch (Exception e){

                }


            }
        });
        builderSingle.show();
    }

    public class PrintInvoiceAsync extends AsyncTask<Void, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Boolean doInBackground(Void... strings) {
            try {

                sendData_token();
            } catch (Exception e) {
            }

            return true;
        }
        @Override
        protected void onPostExecute(Boolean bitmap) {
            super.onPostExecute(bitmap);
            try {
                closeBT();
            }catch(IOException e){

            }catch(Exception e){

            }
        }
    }


    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    ArrayList<String> bluetoothNames;
    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            //myLabel.setText("Bluetooth Opened");
            Toast.makeText(this,"Please Wait.."
                    ,Toast.LENGTH_LONG).show();


        } catch (Exception e) {
           
            Toast.makeText(this,"open BT"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }


    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //myLabel.setText(data);
                                                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();

                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }


    // this will find a bluetooth printer device
    void findBT() {

        try {

            bluetoothNames=new ArrayList<>();
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                //myLabel.setText("No bluetooth adapter available");

                Toast.makeText(this,"Bluetooth device found.",Toast.LENGTH_LONG).show();

            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    bluetoothNames.add(device.getName());


                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("printer001")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            /*ListView arrayList=findViewById(R.id.list);
            ArrayAdapter adapter=new ArrayAdapter(this,R.layout.bluetooth_list_item, R.id.tvName, bluetoothNames);
            arrayList.setAdapter(adapter);*/
        }catch(Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }



    // this will send text data to be printed by the bluetooth printer
    void sendData_token() throws IOException {
        try {

            // the text typed by the user
            String msg = "\n";
            msg += "\n";
            msg += "\n";
            msg += "# TOKEN";
            msg += "\n";
            msg += "\n";

            msg +=  "*********** "+"\u0020"+"\u0020"+transId+ "\u0020"+"\u0020"+"***************";

            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";

            mmOutputStream.write(msg.getBytes());


        } catch (Exception e) {
            Toast.makeText(this,"Send data"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    private String formatBeforePrinting(String unformated,int characterCount){

        String text=unformated;
        String original="\u0020";
        for (int i=0;i<characterCount;i++){
            original+="\u0020";
        }
        if(text.length()<=characterCount){
            original=original.replaceFirst(original.substring(0,text.length()),text);
        }else{
            original=text;
        }

        return original;
    }

    // this will send text data to be printed by the bluetooth printer
/*    void sendData() throws IOException {
        try {

            selectedList=Product.getSelectedListDupliacteRemoved();
            String msg = "\n";
            msg += "\n";
            msg += "# RECEIPT";
            msg += "\n";
            msg += "\n";
            msg += "----------------------------------------------";
            msg += "\n";
            msg+="\u0020"+formatBeforePrinting("Ln",4)+"\u0020";
            msg+="\u0020"+formatBeforePrinting("Item",14)+"\u0020";
            msg+="\u0020"+formatBeforePrinting("Price",7)+"\u0020";
            msg+="\u0020"+formatBeforePrinting("Qty",4)+"\u0020";
            msg+="\u0020"+formatBeforePrinting("Total",10)+"\u0020";
            msg += "\n";
            msg += "----------------------------------------------";
            msg += "\n";


            for(int i = 0; i < selectedList.size(); i++) {

                msg += "\n";

                msg+="\u0020"+formatBeforePrinting(String.valueOf(selectedList.get(i).getItemCount()),4)+"\u0020";
                msg+="\u0020"+formatBeforePrinting(String.valueOf(selectedList.get(i).getUnitPrice()),7)+"\u0020";
                msg+="\u0020"+formatBeforePrinting(String.valueOf(selectedList.get(i).getUnitPrice()*selectedList.get(i).getItemCount()),10)+"\u0020";

            }

            msg += "\n";
            msg += "\n";
            msg += "----------------------------------------------";
            msg += "\n";
           *//* msg += "\n";
            msg += "\u0020"+formatBeforePrinting("SUB TOTAL",28)+ formatBeforePrinting("Rs "+Product.getSelectedProductTotal()+"/=",16);
           *//* msg += "\n";
     *//* msg += "\u0020"+formatBeforePrinting("DISCOUNT",28)+formatBeforePrinting("Rs "+Product.getDiscount()+"/=",16);
            msg += "\n";*//*
            msg += "\u0020"+formatBeforePrinting(" TOTAL",28)+ formatBeforePrinting(String.valueOf(Product.getSelectedProductTotal()-Product.getDiscount())+"/=",16);
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "****************** THANK YOU ******************";
            msg += "\n";
            msg += "@copy rights reserved by dreamfx (pvt) ltd";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            mmOutputStream.write(msg.getBytes());


        } catch (Exception e) {
            Toast.makeText(this,"open BT"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }finally{

            try {
                closeBT();
            }catch (Exception e){
                Toast.makeText(this,"open BT"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }
    }*/
}


