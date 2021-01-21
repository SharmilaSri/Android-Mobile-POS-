package com.retailx.dreamdx.retailx;


import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.retailx.dreamdx.retailx.POJO.ListType;
import com.retailx.dreamdx.retailx.Fragments.FragmentMain;
import com.retailx.dreamdx.retailx.Fragments.FragmentMainPagerAdaptor;
import com.retailx.dreamdx.retailx.POJO.Category;
import com.retailx.dreamdx.retailx.POJO.CreateOrEditFlag;
import com.retailx.dreamdx.retailx.POJO.Person;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.Service.FloatingViewService;
import com.retailx.dreamdx.retailx.Syncronisation.SyncFunctions;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.CircleAnimationUtil;
import com.retailx.dreamdx.retailx.utils.CircularImageView;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView gridview;
    DBHelper db;
    private TabLayout tabLayout;
    FragmentMainPagerAdaptor fragPageAdapter;
    String currentCategory = "ALL";
    Toolbar toolbar;
    Button txtTotal, txtItem;
    public static String selectedTabTitle=ConstantsUsed.ALL;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;

    public static String shouldShow="true";
    String userId ="-1";
    public static SharedPreference sharedPreference = SharedPreference.getInstance();
    boolean isAllowThread=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_layout_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        attachBarcodeReaderFragment("ALL");


        userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

        setUpView();

        setUpNavigationDrawer();

        setUpTabs(getApplicationContext());

        if(Person.getSelectedCustomer()!=null)
            getSupportActionBar().setTitle(Person.getSelectedCustomer().getName().toUpperCase());
        else
            getSupportActionBar().setTitle(getResources().getString(R.string.cart));

         thread = new Thread(new Runnable() {
            @Override
            public void run() {

                    if (UtilityFunctions.isNetworkConnected(MainActivity.this) || UtilityFunctions.isInternetAvailable()) {
                        String userId = "-1";
                        userId = SharedPreference.getInstance().getValue(MainActivity.this, Constants.USER_ID);
                        if (userId != null && !userId.equalsIgnoreCase("-1")) {
                            SyncFunctions.syncWithServer(MainActivity.this, userId);
                        }

                }
                    handler.postDelayed(this, 3 * 1000);
            }
        });

    }

    Thread thread3;
    Handler handler = new Handler();
    Thread thread=null;
    private void syncWithVolley(){
        handler.post(thread);

    }



    private void setUpNavigationDrawer(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView emailNav = (TextView)hView.findViewById(R.id.email_nav);
        TextView nameNav = (TextView)hView.findViewById(R.id.name_nav);
        CircularImageView image=(CircularImageView)hView.findViewById(R.id.imageView_nav);
        emailNav.setText("Eclipse (Pvt) Ltd");
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldShow="false";
                startActivity(new Intent(MainActivity.this,BussinesInfoActivity.class));
            }
        });

        String is_admin=SharedPreference.getInstance().getValue(this,ConstantsUsed.IS_ADMIN);
        /*if(is_admin!=null && is_admin.equalsIgnoreCase("0")){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.add_user).setVisible(false);
        }*/

        displayImage(nameNav,image);

        //displayEmail(emailNav);
    }

    private void displayEmail(TextView emailTxtV){
        Cursor infoCur = null;
        String email="";

        try {

            infoCur = db.getUSerDetails();
            while (infoCur.moveToNext()) {
                email = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_USER_EMAIL)));
                emailTxtV.setText(email);
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally{
            if(infoCur!=null)
                infoCur.close();
        }
    }

    private void displayImage(TextView nameNav, CircularImageView circleImageView){

        Cursor infoCur = null;
        String name="";
        String email="";
        String imagePath="";
        String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

        try {


            infoCur = db.getBussinessDetails(userId);
            while (infoCur.moveToNext()) {
                imagePath = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_LOGO)));

                if(!imagePath.equalsIgnoreCase("NO_IMAGE") && imagePath!=null) {
                    //circleImageView.setImageBitmap(UtilityFunctions.setPicImageRounded(imagePath, circleImageView, this));
                    if(imagePath.startsWith("http")){
                        Picasso.get().load(imagePath).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                                .error(R.drawable.no_image).resize(110, 110).centerCrop()
                                .into(circleImageView);
                    }else {
                        File f = new File(imagePath);
                        Picasso.get().load(f).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                                .error(R.drawable.no_image).resize(150, 150).centerCrop()
                                .into(circleImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Toast.makeText(MainActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }

                }
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally{
            if(infoCur!=null)
                infoCur.close();
            //nameNav.setText(SharedPreference.getInstance().getValue(this, Constants.EMAIL));
            nameNav.setText("Eclipse (Pvt) Ltd");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        isAllowThread=true;
        shouldShow="true";
        setTotalText_(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount()),String.valueOf(Product.getSelectedItemCount())+ " " + "ITEMS");
        if(FloatingViewService.isRunningFlag){
            Product.installTempDetails(this);
            FloatingViewService.stopFloatingService(this);
        }
        syncWithVolley();

    }



    public FragmentMain attachBarcodeReaderFragment(String catTitle) {
        Category.getCategoryInstance().setSelectedCategory(catTitle);//this is used in create product as get category
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        FragmentMain fragment = FragmentMain.newInstance(catTitle,0);
        fragmentTransaction.replace(R.id.main_fm_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    private void setUpView() {
        db = new DBHelper(this);
        txtTotal = (Button) findViewById(R.id.id_btn_total);
        txtItem = (Button) findViewById(R.id.id_btn_total_items);
        destView = (LinearLayout) findViewById(R.id.dest_layout);
        txtTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkCount();

            }
        });

        txtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCount();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    private void checkCount() {
        if (Product.getSelectedItemCount() > 0.0) {
            shouldShow="false";
            startActivity(new Intent(MainActivity.this, SelectedProductsListActivity.class));
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.empty_basket))
                    .setMessage(getResources().getString(R.string.empty_basketsg))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(R.drawable.ic_add_shopping_cart_black_24dp)
                    .show();
        }
    }

    public  void setUpTabs(Context ctx) {
        DBHelper db=new DBHelper(ctx);
        Category.clear(MainActivity.this);
        Category.addCategoryToList(new Category("ALL","0",""));
        Cursor catCur = null;

        try {
            String userId = SharedPreference.getInstance().getValue(ctx, Constants.USER_ID);

            catCur = db.getAllCategoryList(userId);
            while (catCur.moveToNext()) {
                String catTitle = catCur.getString(catCur.getColumnIndex((ConstantsUsed.COLUMN_CAT_TITLE)));
                String catId = catCur.getString(catCur.getColumnIndex((ConstantsUsed.COLUMN_CAT_ID)));
                String catDesc = catCur.getString(catCur.getColumnIndex((ConstantsUsed.COLUMN_CAT_DESCRIPTION)));
                Category.addCategoryToList(new Category(catTitle,catId,catDesc));
            }
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.removeAllTabs();//remove all d tabs before doing a sync wd server

            int i = 0;
            for (i = 0; i < Category.getCategoryList().size(); i++) {
                tabLayout.addTab(tabLayout.newTab().setText(Category.getCategoryList().get(i).getTitle()));

            }
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    selectTab(tab);
                }
                private void selectTab(TabLayout.Tab tab) {
                    currentCategory=Category.getCategoryList().get(tab.getPosition()).getTitle();
                    selectedTabTitle=Category.getCategoryList().get(tab.getPosition()).getTitle();
                    attachBarcodeReaderFragment(selectedTabTitle);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        selectTab(tab);

                        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                selectTab(tab);
                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab arg0) {
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab arg0) {
                            }
                        });

                    }
                }


                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

            });

            tabLayout.getTabAt(0).select();


        } catch (Exception e) {

        } finally {
            if (catCur != null)
                catCur.close();
        }

        for (int i=0;i<Category.getCategoryList().size();i++){
            View tabView= ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            tabView.setTag( Integer.valueOf(i));
            tabView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    moveToEditCategory(Category.getCategoryList().get((Integer)v.getTag()).getTitle(),
                            Category.getCategoryList().get((Integer)v.getTag()).getCatId());
                    return false;

                }
            });
        }

    }

    private void moveToEditCategory(String tag,String id){
        shouldShow="false";
        CreateOrEditFlag.getInstance().setFlag(1);
        Intent editCat=new Intent(MainActivity.this, CreateCategoryActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ConstantsUsed.EDIT_CAT_KEY,tag);
        extras.putString(ConstantsUsed.EDIT_CAT_ID,id);
        editCat.putExtras(extras);
        startActivity(editCat);
    }

    @Override
    protected void onStop() {
        super.onStop();
            isAllowThread=false;
            //SyncFunctions.stopAll();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                //askPermission();
            } else {
               // launchFloater("onstop");
            }

    }

    @Override
    protected void onPause() {
        super.onPause();

        isAllowThread=false;
        SyncFunctions.stopAll();
        if(handler != null)
            handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.warning))
                    .setMessage(getResources().getString(R.string.warning_msg))

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            shouldShow="false";

                            exitApp();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .setIcon(R.drawable.ic_exit_to_app_black_24dp)
                    .show();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_people){
            String addCust=SharedPreference.getInstance().getValue(MainActivity.this, "customerprofile");
            shouldShow="false";
            ListType.getInstance().setType("CUST");
            startActivity(new Intent(this, ListItemActivity.class));
            /*if(addCust.equalsIgnoreCase("1")) {
                shouldShow="false";
                ListType.getInstance().setType("CUST");
                startActivity(new Intent(this, ListItemActivity.class));
            }else
                Validator.showToast(MainActivity.this,"Please subscribe to use this feature");
*/


            return true;
        }else if(id == R.id.action_syncronise){
            if(UtilityFunctions.isNetworkConnected(this) || UtilityFunctions.isInternetAvailable()) {
                SyncFunctions.syncImagesWithServer(this,SharedPreference.getInstance().getValue(this,Constants.USER_ID));
            }else{
                Validator.showToast(this,"NO INTERNET");
            }
        }

        return true;

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.bussiness_info) {
            shouldShow="false";
            startActivity(new Intent(this,BussinesInfoActivity.class));
            //Validator.showToast(MainActivity.this,"Function is yet to be released");

        } else if (id == R.id.edit_product) {
            shouldShow="false";
            Validator.showToast(MainActivity.this,"Edit Product");

        } else if (id == R.id.current_plan) {
            Validator.showToast(MainActivity.this,"Function is yet to be released");

        } else if (id == R.id.logout) {
            shouldShow="false";
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.warning))
                    .setMessage(getResources().getString(R.string.warning_msg))

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            shouldShow="false";

                            exitApp();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .setIcon(R.drawable.ic_exit_to_app_black_24dp)
                    .show();


            //Validator.showToast(MainActivity.this,"Function is yet to be released");
        } else if (id == R.id.nav_reports) {
            shouldShow="false";
            String viewReport=SharedPreference.getInstance().getValue(MainActivity.this, "viewreport");
            startActivity(new Intent(this,ChartsActivity.class));

            /*if(viewReport.equalsIgnoreCase("1"))
                startActivity(new Intent(this,ChartsActivity.class));
            else
                Validator.showToast(MainActivity.this,"Please subscribe to use this feature");
*/
        } else if(id==R.id.saved_orders){
            shouldShow="false";

            ListType.getInstance().setType("SAVED_ORDER");
            startActivity(new Intent(this, ListItemActivity.class));
        } else if(id==R.id.products){
            shouldShow="false";
            int edit=SharedPreference.getInstance().getInt(MainActivity.this, ConstantsUsed.CREATE_EDIT_PRODUCT);
            startActivity(new Intent(this, EditProduct.class));

            /*if(edit==1)
                startActivity(new Intent(this, EditProduct.class));
            else
                Validator.showToast(MainActivity.this,"You are not privilaged to use this feature");*/
        }else if(id==R.id.trans_history){
            shouldShow="false";
           int viewHistory=SharedPreference.getInstance().getInt(MainActivity.this, ConstantsUsed.VIEW_ALL_TAX);
            startActivity(new Intent(this, TransHistoryActivity.class));

            /*if(viewHistory==1)
                startActivity(new Intent(this, TransHistoryActivity.class));
            else
                Validator.showToast(MainActivity.this,"You are not privilaged to use this feature");*/

        }/*else if(id==R.id.subscription){
            shouldShow="false";
            startActivity(new Intent(this, SubscriptionActivity.class));
        }*//*else if(id==R.id.add_user){
            String addUser=SharedPreference.getInstance().getValue(MainActivity.this, "multiusers");
            startActivity(new Intent(this, ListItemActivity.class));

            if(addUser.equalsIgnoreCase("1")) {
                shouldShow="false";
                ListType.getInstance().setType("USER");
                startActivity(new Intent(this, ListItemActivity.class));
            }else
                Validator.showToast(MainActivity.this,"Please subscribe to use this feature");



        }*/else if(id==R.id.settings){
            shouldShow="false";
            startActivity(new Intent(this, SettingActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exitApp(){
        shouldShow="false";
        UtilityFunctions.logout(this);

    }

  public void setTotalText_(String totalTxt, String itemCount) {

        if(totalTxt.equalsIgnoreCase("0")){
            txtItem .setText(getResources().getString(R.string.empty_basket));
            txtTotal.setText("");
        }else {
            txtTotal.setText(totalTxt);
            txtItem.setText(itemCount);
        }
    }

    public void removeList(String productId) {

        Product.removeItem(productId,this);
    }


    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SYSTEM_ALERT_WINDOW_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //launchFloater("request");
                } else {
                   // Toast.makeText(this,"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private void launchFloater(String call){

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if(shouldShow.equalsIgnoreCase("true")) {
                Product.saveTempDetails(this);
                startSrviceOnthread();
            }
        } else if (Settings.canDrawOverlays(this)) {
            if(shouldShow.equalsIgnoreCase("true")) {
                Product.saveTempDetails(this);
                startSrviceOnthread();
            }
        } else {
            askPermission();
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
    }

    private void startSrviceOnthread(){
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(ConstantsUsed.SERVICE_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(shouldShow.equalsIgnoreCase("true"))
                        startService(new Intent(MainActivity.this, FloatingViewService.class));
                }
            }
        };
        timer.start();
    }

    LinearLayout destView;
    public void makeFlyAnimation(ImageView targetView) {

        if(targetView!=null) {
            new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(500).setDestView(destView).setAnimationListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // Toast.makeText(getContext(), "Continue Shopping...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).startAnimation();
        }


    }



}
