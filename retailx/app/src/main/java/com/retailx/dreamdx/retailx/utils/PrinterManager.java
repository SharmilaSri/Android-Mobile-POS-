package com.retailx.dreamdx.retailx.utils;

public class PrinterManager {

/*    private void connectToPrinter(Context ctx) {
        BluetoothAdapter mBluetoothAdapter;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if (mBluetoothAdapter == null) {
            Validator.showToast(ctx,"Your device does not support bluetooth");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Validator.showToast(ctx,"PLEASE ENABLE BLUETOOTH");
            }else{
                findBT(mBluetoothAdapter);

                if(availableList.isEmpty())
                    Validator.showToast(ctx,"NO PRINTER ARE CONNECTED");
                else
                    showListDialog();

            }
        }

    }*/


 /*   private void showListDialog(final Context ctx){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ctx);
        builderSingle.setIcon(android.R.drawable.ic_menu_add);
        builderSingle.setTitle("SELECT PRINTER : ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.select_dialog_singlechoice,bluetoothNames);


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
                dialog.dismiss();

                if(mmDevice!=null){
                    try {
                        openBT();

                        DoneScreenActivity.PrintInvoiceAsync async = new DoneScreenActivity.PrintInvoiceAsync();
                        async.execute();

                    }catch (IOException ie){
                        Validator.showToast(ctx,"Printing failed");

                    }catch (Exception e){
                        Validator.showToast(ctx,"Printing failed");
                    }

                }else{
                    dialog.dismiss();
                }


            }
        });
        builderSingle.show();
    }*/


    // tries to open a connection to the bluetooth printer device
    void openBT() throws Exception {

        /*if(mmDevice!=null) {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            //myLabel.setText("Bluetooth Opened");
            Toast.makeText(ctx, "Bluetooth Opened", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Could not connect with the device", Toast.LENGTH_LONG).show();

        }*/


    }
}
