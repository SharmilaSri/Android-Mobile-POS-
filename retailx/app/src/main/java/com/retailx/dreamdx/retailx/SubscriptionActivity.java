package com.retailx.dreamdx.retailx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.Adapters.DynamicImageViewPagerAdapter;
import com.retailx.dreamdx.retailx.Adapters.SubscriptionTypeRecylerAdapter;
import com.retailx.dreamdx.retailx.POJO.SubscriptionType;
import com.retailx.dreamdx.retailx.apicalls.ApiCalls;
import com.retailx.dreamdx.retailx.apicalls.VolleyCalls;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SubscriptionActivity extends AppCompatActivity {


    static android.support.v4.view.ViewPager vpPager;
    LinearLayout subTypeOneLayout,subTypeTwoLayout,subTypeThreeLayout;
    TextView subTypeoneTxt,subTypeTwoTxt,subTypeThreeTxt;
    TextView txtOne,txtTwo,txtThree,txtFour;
    LinearLayout inetrnetAvailble,noInternet;

    private static ArrayList<String> imageUrls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_subscription);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subscription");


        setUpViews();
        SubscriptionType.getInstance().getSubObj().setId(-1);


        if(UtilityFunctions.isNetworkConnected(this)){
            inetrnetAvailble.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.GONE);

            interNetAvailableFunctions();

        }else{
            inetrnetAvailble.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }


    }

    private void interNetAvailableFunctions(){

        ApiCalls apicalls=new VolleyCalls();
        apicalls.connectToNetworkGet(this, ConstantsUsed.URL_SUBSCRIPTION,ConstantsUsed.TYPE_SUBSCRIPTION);



        oneMonthselected(null);

    }

    public static  void setUpSusbcriptionTypesDynamically(Context ctx,ArrayList<SubscriptionType> subTypeList){
        layoutManager = new GridLayoutManager(ctx, subTypeList.size());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SubscriptionTypeRecylerAdapter(ctx,subTypeList,null);
        recyclerView.setAdapter(mAdapter);
    }

    public static void onSuccessSubscription(JSONObject jsonObj, Context ctx){

        ArrayList<SubscriptionType> subTypeList=new ArrayList<>();
        imageUrls = new ArrayList<>();

        try {
            JSONArray images = jsonObj.getJSONArray("add_images");

            if(images.length()>0) {
                for (int i = 0; i < images.length(); i++) {
                    imageUrls.add(images.getString(i));
                }
            }

            JSONArray subData = jsonObj.getJSONArray("subscription_data");
            if(subData.length()>0) {
                for (int i = 0; i < subData.length(); i++) {
                    subTypeList.add(new SubscriptionType(subData.getJSONObject(i).getString("description"),subData.getJSONObject(i).getDouble("price"),
                            subData.getJSONObject(i).getString("title"),subData.getJSONObject(i).getString("discount_label"),subData.getJSONObject(i).getInt("id"),
                            subData.getJSONObject(i).getString("title_label")));
                }
            }

            setUpSusbcriptionTypesDynamically(ctx,subTypeList);

        }catch(Exception e){

        }finally{

            if(!imageUrls.isEmpty()){
                DynamicImageViewPagerAdapter adapter = new DynamicImageViewPagerAdapter(ctx, imageUrls);
                vpPager.setAdapter(adapter);
                setupAutoPager();
            }
        }
    }


    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager layoutManager;
    private void setUpViews(){
        inetrnetAvailble=findViewById(R.id.internet_available);
        noInternet=findViewById(R.id.internet_not_available);

        vpPager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);


        txtOne=findViewById(R.id.one_month);
        txtTwo=findViewById(R.id.two_month);
        txtThree=findViewById(R.id.three_month);
        txtFour=findViewById(R.id.four_month);


    }



    static Timer timer;
    private static int currentPage = 0;
    private static void setupAutoPager()
    {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run()
            {

                vpPager.setCurrentItem(currentPage, true);
                if(currentPage == 3)
                {
                    currentPage = 0;
                }
                else
                {
                    ++currentPage ;
                }
            }
        };


        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 3000);
    }

    public void oneMonthselected(View v){
        SubscriptionType.getInstance().getSubObj().setSelectedMonth(1);

        txtOne.setBackground(getResources().getDrawable(R.drawable.bg_curve_blue_full));
        txtOne.setTextColor(Color.WHITE);

        txtTwo.setBackgroundColor(Color.WHITE);
        txtThree.setBackgroundColor(Color.WHITE);
        txtFour.setBackgroundColor(Color.WHITE);

        txtTwo.setTextColor(Color.BLACK);
        txtThree.setTextColor(Color.BLACK);
        txtFour.setTextColor(Color.BLACK);

    }

    public void threeMonthSelected(View v){
        oneMonthselected(null);
        SubscriptionType.getInstance().getSubObj().setSelectedMonth(3);

        txtOne.setBackgroundColor(Color.WHITE);
        txtOne.setTextColor(Color.BLACK);

        txtTwo.setBackground(getResources().getDrawable(R.drawable.bg_curve_blue_full));
        txtTwo.setTextColor(Color.WHITE);

    }

    public void sixMonthSelected(View v){
        threeMonthSelected(null);
        SubscriptionType.getInstance().getSubObj().setSelectedMonth(6);

        txtTwo.setBackgroundColor(Color.WHITE);
        txtTwo.setTextColor(Color.BLACK);

        txtThree.setBackground(getResources().getDrawable(R.drawable.bg_curve_blue_full));
        txtThree.setTextColor(Color.WHITE);
    }

    public void oneYearSelected(View v){
        sixMonthSelected(null);
        SubscriptionType.getInstance().getSubObj().setSelectedMonth(12);

        txtThree.setBackgroundColor(Color.WHITE);
        txtThree.setTextColor(Color.BLACK);

        txtFour.setBackground(getResources().getDrawable(R.drawable.bg_curve_blue_full));
        txtFour.setTextColor(Color.WHITE);
    }




    public void moveToPaymentScreen(View v){

        if(UtilityFunctions.isNetworkConnected(this)){
            inetrnetAvailble.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.GONE);

            String userId = SharedPreference.getInstance().getValue(this, Constants.USER_ID);

            if(SubscriptionType.getInstance().getId()!=-1){
                ApiCalls apicalls = new VolleyCalls();
                Map<String, String> params = new HashMap<String, String>();
                params.put(ConstantsUsed.USER_ID, userId);
                params.put(ConstantsUsed.SUB_ID, String.valueOf(SubscriptionType.getInstance().getId()));
                apicalls.connectToNetworkPost(this, ConstantsUsed.URL_POST_SUBSCRIPTION, ConstantsUsed.TYPE_POST_SUB, params);
            }else{
                Validator.showToast(SubscriptionActivity.this,"Please select a Subscription Plan");
            }

        }else{
            inetrnetAvailble.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }
        //startActivity(new Intent(this,PaymentScreenActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }
}
