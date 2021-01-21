package com.retailx.dreamdx.retailx.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.retailx.dreamdx.retailx.R;

public class Validator {

    public static boolean validateEmail(String email) {
        /*final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern) )
        {

            return true;
        } else
            return  false;*/
        /*boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        return check;*/

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validatePhoneNumber(String phonenumber) {
        return android.util.Patterns.PHONE.matcher(phonenumber).matches();
    }

    public static void showToast(Context ctx,String msg) {

        new AlertDialog.Builder(ctx)
                .setTitle("Warning")
                .setMessage(msg.toUpperCase())

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(ctx.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })

                .setIcon(R.drawable.ic_info_black_24dp)
                .show();
    }

}
