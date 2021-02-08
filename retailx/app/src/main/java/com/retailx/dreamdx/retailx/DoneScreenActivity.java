package com.retailx.dreamdx.retailx;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.POJO.BussinessInfo;
import com.retailx.dreamdx.retailx.POJO.Invoice;
import com.retailx.dreamdx.retailx.POJO.Person;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.POJO.ProductDetailsListGrid;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.PdfGenerator;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class DoneScreenActivity extends AppCompatActivity {

    DBHelper db;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    static int REQUEST_ENABLE_BT=1000;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    ArrayList<String> bluetoothNames;
    HashMap<String,BluetoothDevice> availableList=new HashMap<String,BluetoothDevice>();

    ArrayList<ProductDetailsListGrid> selectedList=null;
    TextView totalRs;


    String name="";
    String address="";
    String number="";
    String imagePath="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_done_screen);

        db=new DBHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.thank_you));

        setUPToggle();

        selectedList=Product.getSelectedListDupliacteRemoved();
        totalRs=findViewById(R.id.txtTotalRs);
        totalRs.setText(String.valueOf(Product.getSelectedProductTotal()));

        addBussinessInfoToInvoice();

        PdfGenerator.createAndDisplayPdf(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                PdfGenerator.createAndDisplayPdf(this);
        }
        }else if( requestCode==READ_EXTERNAL_STORAGE_REQUEST_CODE){

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewPdf("invoice.pdf", "Dir");
            }

        }
    }



    int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=2;
    int READ_EXTERNAL_STORAGE_REQUEST_CODE=3;
    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                PdfGenerator.createAndDisplayPdf(this);
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            PdfGenerator.createAndDisplayPdf(this);
            return true;
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                viewPdf("invoice.pdf", "Dir");
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            viewPdf("invoice.pdf", "Dir");
            return true;
        }
    }


    private void addBussinessInfoToInvoice() {
        Cursor infoCur = null;
        BussinessInfo bussinessObj=BussinessInfo.getInstance();

        try {
            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

            infoCur = db.getBussinessDetails(userId);
            while (infoCur.moveToNext()) {
                name = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_NAME)));
                number = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_NUMBER)));
                address = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_ADDRESS)));
                imagePath = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_LOGO)));

                bussinessObj.setBussinessName(name);
                bussinessObj.setBussinesPhoneNo(number);
                bussinessObj.setBussinessAddress(address);

                if (!imagePath.equalsIgnoreCase("NO_IMAGE") && imagePath != null) {
                          bussinessObj.setLogoPath(imagePath);
                }
            }

        } catch (Exception e) {

        }finally {
            if(infoCur!=null)
                infoCur.close();

            Invoice.getInstance().setBussinessInfoObj(bussinessObj);
        }
    }


    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile=null;
        //File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + directory + "/" + file);

        if (Build.VERSION.SDK_INT >= 19) {
            pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/" + directory + "/" + file);
        }else{
            pdfFile = new File(Environment.getExternalStorageDirectory() + "/Documents"+ "/" + directory + "/" + file);
        }

        Uri photoURI = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                pdfFile);

       // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(photoURI, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(DoneScreenActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void moveToMain(View v){

        Product.clearItems();
        Product.setDiscount(0.0,0.0);
        Person.resetCustomer();
        startActivity(new Intent(this,MainActivity.class));
    }

    LinearLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior=null;
    private void setUPToggle(){
        layoutBottomSheet=findViewById(R.id.bottom_sheet);

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

    }

    public void sharePdf(View v){
        isReadStoragePermissionGranted();


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

    public void closeBottonSheet(View v){
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        viewPdf("invoice.pdf", "Dir");

    }
    @Override
    public void onBackPressed() {
        if (true) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            super.onBackPressed();
        }
    }


    // this will send text data to be printed by the bluetooth printer
    void sendData() throws IOException {
        try {

// Bold
/*            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
// Height
            format[2] = ((byte)(0x10 | arrayOfByte1[2]));
// Width
            format[2] = ((byte) (0x20 | arrayOfByte1[2]));
// Underline
            format[2] = ((byte)(0x80 | arrayOfByte1[2]));
// Small
            format[2] = ((byte)(0x1 | arrayOfByte1[2]));

            //normal
            format[2] = ((byte)(0x0 | arrayOfByte1[2]));*/

            // the text typed by the user
            String msg2 = "\n";
            msg2 += "\n";
            msg2 += "\n";
            msg2 += "\u0020"+formatBeforePrinting("#"+Invoice.getInstance().getTransactionSummaryObj().getTransactionId(),40);
            msg2 += "\n";

            byte[] format = { 27, 33, 0 };
            byte[] arrayOfByte = { 27, 33, 0 };
            format[2] = ((byte)(0x8 | arrayOfByte[2]));
            mmOutputStream.write(format);
            mmOutputStream.write(msg2.getBytes(),0,msg2.getBytes().length);

            /*String imagePath = Invoice.getInstance().getBussinessInfoObj().getLogoPath();
            try {

                File imagefile = new File(imagePath);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(imagefile);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bm = BitmapFactory.decodeStream(fis);
                Bitmap lowResImg = Bitmap.createScaledBitmap(bm,100,100,true);
                byte[] b = Utils.decodeBitmap(lowResImg);
                printText(b);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PrintTools", "the file isn't exists");
            }*/

            String msgInfo = "\n";
            msgInfo += "--------------------------------";//32
            msgInfo += "\n";
            msgInfo += "\u0020"+formatBeforePrinting(Invoice.getInstance().getBussinessInfoObj().getBussinessName(),30);
            msgInfo += "\n";
            msgInfo += "\u0020"+formatBeforePrinting(Invoice.getInstance().getBussinessInfoObj().getBussinessAddress(),30);
            msgInfo += "\n";
            msgInfo += "\u0020"+formatBeforePrinting(Invoice.getInstance().getBussinessInfoObj().getBussinesPhoneNo(),30);
            msgInfo += "\n";
            msgInfo += "\u0020"+formatBeforePrinting(Invoice.getInstance().getTransactionSummaryObj().getTransactionDate(),30);
            msgInfo += "\n";
            if(Person.getSelectedCustomer()!=null  && !Person.getSelectedCustomer().getName().isEmpty()){
                msgInfo += "\u0020"+formatBeforePrinting("Customer:",10)+ formatBeforePrinting(Person.getSelectedCustomer().getName(),30);
                msgInfo += "\n";
            }
            msgInfo += "--------------------------------";

            byte[] formatInfo = { 27, 33, 0 };
            byte[] arrayOfByteInfo = { 27, 33, 0 };
            formatInfo[2] = ((byte)(0x0 | arrayOfByteInfo[2]));
            mmOutputStream.write(formatInfo);
            mmOutputStream.write(msgInfo.getBytes(),0,msgInfo.getBytes().length);

            String title = "\n";
            //title+="\u0020"+formatBeforePrinting("Ln",4)+"\u0020";
            title+="\u0020"+formatBeforePrinting("Item",4)+"\u0020";
            title+="\u0020"+formatBeforePrinting("Price",4)+"\u0020";
            title+="\u0020"+formatBeforePrinting("Qty",4)+"\u0020";
            title+="\u0020"+formatPrintLast("Amount",7)+"\u0020";

            byte[] formatTitle = { 27, 33, 0 };
            byte[] arrayOfByteTitle = { 27, 33, 0 };
            formatTitle[2] = ((byte)(0x8 | arrayOfByteTitle[2]));
            mmOutputStream.write(formatTitle);
            mmOutputStream.write(title.getBytes(),0,title.getBytes().length);


            String msg = "\n";
            msg += "--------------------------------";
            for(int i = 0; i < selectedList.size(); i++) {
                msg += "\n";
               // msg+="\u0020"+formatBeforePrinting(String.valueOf(i+1),4)+"\u0020";
                msg+="\u0020"+formatBeforePrinting(selectedList.get(i).getTitle(),30)+"\u0020";
                //msg += "\n";
                //msg+="\u0020"+formatBeforePrinting("",4)+"\u0020";
                msg+=formatBeforePrinting(AppFeatures.format(selectedList.get(i).getUnitPrice()),7)+"\u0020";
                msg+=formatBeforePrinting(String.valueOf(selectedList.get(i).getItemCount()),6)+"\u0020";
                msg+=formatPrintLast(AppFeatures.format(selectedList.get(i).getUnitPrice()*selectedList.get(i).getItemCount()),10
                )+"\u0020";
            }
            msg += "\n";
            msg += "--------------------------------";
            msg += "\n";
            if(Product.getDiscount()>0){
                msg += "\u0020"+formatBeforePrinting("Sub total",10)+ formatPrintLast(AppFeatures.format(Product.getSelectedProductTotal())+"/=",16);
                msg += "\n";
                msg += "\u0020"+formatBeforePrinting("Discounts",10)+formatPrintLast( AppFeatures.format(Product.getDiscount())+"/=",16);
                msg += "\n";
            }

            byte[] format2 = { 27, 33, 0 };
            byte[] arrayOfByte2 = { 27, 33, 0 };
            format2[2] = ((byte)(0x0 | arrayOfByte2[2]));
            mmOutputStream.write(format2);
            mmOutputStream.write(msg.getBytes(),0,msg.getBytes().length);

            String netAmt = "\n";
            netAmt += "\u0020"+formatBeforePrinting("Net Total",10)+ formatPrintLast(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount())+"/=",16);
            netAmt += "\n";

            //byte[] format3 = { 27, 33, 0 };
            //byte[] arrayOfByte3 = { 27, 33, 0 };
            //format3[2] = ((byte)(0x10 | arrayOfByte3[2]));
            mmOutputStream.write(format2);
            mmOutputStream.write(netAmt.getBytes(),0,netAmt.getBytes().length);

            String msg4 = "\n";
            msg4 += "\u0020"+formatBeforePrinting("Rounded",10)+ formatPrintLast(AppFeatures.formatInt((int)(Product.getSelectedProductTotal()-Product.getDiscount()))+"/=",16);
            msg4 += "\n";
            msg4 += "\u0020"+formatBeforePrinting(Invoice.getInstance().getTransactionSummaryObj().getPaymentType(),10)+ formatPrintLast(AppFeatures.format(Invoice.getInstance().getReceivedAmount())+"/=",16);
            msg4 += "\n";
            msg4 += "\u0020"+formatBeforePrinting("Change",10)+ formatPrintLast(AppFeatures.format(Invoice.getInstance().getBalance())+"/=",16);
            msg4 += "\n";
            msg4 += "\n";
            msg4 += "********** Thank You *********";
            msg4 += "\n";
            msg4 += "\n";
            msg4 += "\n";
            msg4 += "  To get Mobile Pos:0771749379  ";
            msg4 += "\n";
            msg4 += "\n";
            msg4 += "\n";
            msg4 += "\n";
            msg4 += "\n";


            byte[] format4 = { 27, 33, 0 };
            byte[] arrayOfByte4 = { 27, 33, 0 };
            format4[2] = ((byte)(0x0 | arrayOfByte4[2]));
            mmOutputStream.write(format4);
            mmOutputStream.write(msg4.getBytes(),0,msg4.getBytes().length);

        } catch (Exception e) {
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }finally{

            try {
                //closeBT();
            }catch (Exception e){
                Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private String formatPrintLast(String unformatted, int characterCount) {
        String text = unformatted;
        String original = "\u0020";

        for (int i=0; i<characterCount-text.length(); i++){
            original+="\u0020";
        }

        if(text.length()<=characterCount){
            original=original+text;

        }else{

            original=text;
        }

        return original;
    }



    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //myLabel.setText("Bluetooth Closed");
            Toast.makeText(this,"Bluetooth Closed",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }


    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    boolean invoicePrinting=false;
    public void openConnection(View v){
        connectToPrinter();
        /*String isBillPrint=SharedPreference.getInstance().getValue(DoneScreenActivity.this, "billprint");
        if(isBillPrint.equalsIgnoreCase("1"))
            connectToPrinter();
        else
            Validator.showToast(DoneScreenActivity.this,"Please subscribe to use this feature");*/

    }


        private void connectToPrinter() {
            BluetoothAdapter mBluetoothAdapter;
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


            if (mBluetoothAdapter == null) {
                Validator.showToast(this,"Your device does not support bluetooth");
            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    Validator.showToast(this,"PLEASE ENABLE BLUETOOTH");
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }else if(mBluetoothAdapter.isEnabled()){
                    findBT(mBluetoothAdapter);




                }
        }

    }

    // this will find a bluetooth printer device
    void findBT(BluetoothAdapter mBluetoothAdapter) {

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
                Validator.showToast(DoneScreenActivity.this,"NO printers are paired. Please pair a printer");
            }

        }catch(Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
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


    // tries to open a connection to the bluetooth printer device
    void openBT() throws Exception {

            if(mmDevice!=null) {

                // Standard SerialPortService ID
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                mmSocket.connect();
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();

                beginListenForData();

                //myLabel.setText("Bluetooth Opened");
                Toast.makeText(this, "Bluetooth Opened", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Could not connect with the device", Toast.LENGTH_LONG).show();

            }


    }

    private void showListDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(DoneScreenActivity.this);
        builderSingle.setIcon(android.R.drawable.ic_menu_add);
        builderSingle.setTitle("SELECT PRINTER : ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DoneScreenActivity.this, android.R.layout.select_dialog_singlechoice,bluetoothNames);


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                String deviceName = arrayAdapter.getItem(which);
                mmDevice=availableList.get(deviceName);

                try {
                    openBT();

                    PrintInvoiceAsync async = new PrintInvoiceAsync();
                    async.execute();

                }catch (IOException ie){

                }catch (Exception e){

                }finally {

                }


            }
        });
        builderSingle.show();
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
                        }catch (Exception e){
                            Validator.showToast(DoneScreenActivity.this,"ERROR");
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }


    public class PrintInvoiceAsync extends AsyncTask<Void, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Boolean doInBackground(Void... strings) {
            try {

                sendData();
             } catch (Exception e) {
                e.printStackTrace();
                invoicePrinting=false;
            }

            return true;
        }
        @Override
        protected void onPostExecute(Boolean bitmap) {
            super.onPostExecute(bitmap);
            invoicePrinting=false;
            try {
                Thread.sleep(3000);
                closeBT();
            }catch(IOException e){

            }catch(Exception e){

            }
        }
    }


}
