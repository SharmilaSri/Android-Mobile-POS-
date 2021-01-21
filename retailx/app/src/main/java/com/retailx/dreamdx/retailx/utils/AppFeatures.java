package com.retailx.dreamdx.retailx.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AppFeatures {

    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }



    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }





    public static String getTimeStamp(){

        Date date = new Date();
        String dateString= dateToStringWdSecForIdGenration(date);
        return dateString;

    }

    public static String getTodayDateWithoutMinSec(){

        Date date = new Date();
        String dateString= dateToStringWithoutMinSec(date);
        return dateString;

    }

    public static String dateToStringWdSecForIdGenration(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        // Timestamp tsTemp = new Timestamp(time);
        String ts = format.format(date);
        return ts;
    }


    public static String dateToString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String ts = format.format(date);
        return ts;
    }

    public static boolean isDateExpired(String date){

        boolean isExpired=false;
        Date expiredDate = stringToDateWithoutMinSec(date);
        if (stringToDateWithoutMinSec(AppFeatures.getTimeStamp()).after(expiredDate))
            isExpired=true;

        return isExpired;
    }

    public static Date stringToDateWithoutMinSec(String aDate) {

        String aFormat="yyyy-MM-dd";
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    public static String dateToStringWithoutMinSec(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String ts = format.format(date);
        return ts;

    }



    public static String getTimeStamp(String dateOnly){

        Date date = new Date();
        String dateString= AppFeatures.dateToString(date);
        return dateString;
    }

    public static String dateToStringWithoutYear(Date date){
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        String ts = format.format(date);
        return ts;
    }

    public static  String getTimeStampWithoutYear(){
        Date date = new Date();
        String dateString= dateToStringWithoutYear(date);
        return dateString;
    }

    public static void vibrate(Context ctx){
        Vibrator v = (Vibrator)ctx. getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    public static String format(double number){
        DecimalFormat formatter = new DecimalFormat("#,###,###.##");
        return formatter.format(number);
    }

    public static String formatInt(int number){
        DecimalFormat formatter = new DecimalFormat("#,###,###.##");
        return formatter.format(number);
    }

    public static String formatToThreeDecimal(double number){
        DecimalFormat formatter = new DecimalFormat("#,###,###.###");
        return formatter.format(number);
    }
}
