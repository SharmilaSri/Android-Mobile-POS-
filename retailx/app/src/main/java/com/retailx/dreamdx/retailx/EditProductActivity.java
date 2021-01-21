package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import com.retailx.dreamdx.retailx.Fragments.FragmentMainPagerAdaptor;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;

import java.util.ArrayList;

public class EditProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    DBHelper db;
    GridView gridview;
    private TabLayout tabLayout;
    ViewPager viewPager;
    FragmentMainPagerAdaptor fragPageAdapter;
    ArrayList<String> titleList;
    String currentCategory = "ALL";
    Button txtTotal, txtItem;
    public static String selectedTabTitle=ConstantsUsed.ALL;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //final FragmentManager supportFragmentManager = getSupportFragmentManager();
        UtilityFunctions.attachBarcodeReaderFragment("ALL",EditProductActivity.this,getSupportFragmentManager(),1);

        db = new DBHelper(this);

        setUpTabs();

       // CreateOrEditFlag.getInstance().setFlag(1);

    }





    SearchView searchView;
    private SimpleCursorAdapter mAdapter=null;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_search) {


            //Validator.showToast(EditProductActivity.this,"Function is yet to be released");
            return true;
        }else*/ if(id==android.R.id.home){
            startActivity(new Intent(this,MainActivity.class));
            return true;

        }



        return true;

    }


    private void setUpTabs() {

        titleList = new ArrayList<>();
        titleList.add(ConstantsUsed.ALL);
        Cursor catCur = null;

        try {
            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

            catCur = db.getAllCategoryList(userId);
            while (catCur.moveToNext()) {
                String catTitle = catCur.getString(catCur.getColumnIndex((ConstantsUsed.COLUMN_CAT_TITLE)));
                titleList.add(catTitle);
            }
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            int i = 0;
            for (i = 0; i < titleList.size(); i++) {
                tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
            }
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    selectTab(tab);
                    viewPager.setCurrentItem(tab.getPosition());
                }
                private void selectTab(TabLayout.Tab tab) {
                    currentCategory=titleList.get(tab.getPosition());
                    selectedTabTitle=titleList.get(tab.getPosition());
                    //final FragmentManager supportFragmentManager = getSupportFragmentManager();
                    UtilityFunctions.attachBarcodeReaderFragment(selectedTabTitle,EditProductActivity.this,getSupportFragmentManager(),1);
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

    }




}
