package com.retailx.dreamdx.retailx;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.utils.AppFeatures;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;
import com.retailx.dreamdx.retailx.utils.ExceptionHandler;
import com.retailx.dreamdx.retailx.utils.Validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartsActivity extends AppCompatActivity {

    String[] dayOfWeekList;
    ArrayList<String> productList;
    ArrayList<String> displayNameList;
    ArrayList<Product> availableProducts;
    DBHelper db;
    TextView txtTotalTxt,txtRevenue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_charts);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.reports));

        db=new DBHelper(this);
        txtTotalTxt=findViewById(R.id.txt_total_sale);
        txtRevenue=findViewById(R.id.id_revenue);

        setUpDays();

        availableProducts=Product.getProductList();

        if(totalSellingList !=null)
            txtTotalTxt.setText(String.valueOf(totalSellingList.get(6)));

        if(totalSellingList !=null && totalBuyingList !=null)
            txtRevenue.setText(String.valueOf(totalSellingList.get(6)-totalBuyingList.get(6)));
        drawLineGraph();
        GraphView graph = (GraphView) findViewById(R.id.graph_bar_highest);

        drawBarChart(maxlist,graph);


        graph = (GraphView) findViewById(R.id.graph_bar_lowest);

        drawBarChart(minlist,graph);

        setUpDisplayName(1,keysMax);
        setUpDisplayName(0,keysMin);

    }

    private void setUpDisplayName(int flag,List<String> keys ){

        displayNameList=new ArrayList<>();

        try {
            for (int i = 0; i < keys.size(); i++) {
                String productId = keys.get(i);

                if(productId.equalsIgnoreCase("NO_KEY")){
                    displayNameList.add("-");
                }else{
                    for (int j = 0; j < availableProducts.size(); j++) {
                        if (productId.equalsIgnoreCase(availableProducts.get(j).getProduct_id())) {
                            displayNameList.add(availableProducts.get(j).getProduct_title());
                        }
                    }
                }
            }
        }catch (Exception e){

        }finally {
            if(!displayNameList.isEmpty()){

                if(flag==1) {

                    ((TextView) findViewById(R.id.max_day_1)).setText(displayNameList.get(0));
                    ((TextView) findViewById(R.id.max_day_2)).setText(displayNameList.get(1));
                    ((TextView) findViewById(R.id.max_day_3)).setText(displayNameList.get(2));
                    ((TextView) findViewById(R.id.max_day_4)).setText(displayNameList.get(3));
                    ((TextView) findViewById(R.id.max_day_5)).setText(displayNameList.get(4));
                    ((TextView) findViewById(R.id.max_day_6)).setText(displayNameList.get(5));
                    ((TextView) findViewById(R.id.max_day_7)).setText(displayNameList.get(6));
                }else if(flag==0){
                    ((TextView) findViewById(R.id.min_day_1)).setText(displayNameList.get(0));
                    ((TextView) findViewById(R.id.min_day_2)).setText(displayNameList.get(1));
                    ((TextView) findViewById(R.id.min_day_3)).setText(displayNameList.get(2));
                    ((TextView) findViewById(R.id.min_day_4)).setText(displayNameList.get(3));
                    ((TextView) findViewById(R.id.min_day_5)).setText(displayNameList.get(4));
                    ((TextView) findViewById(R.id.min_day_6)).setText(displayNameList.get(5));
                    ((TextView) findViewById(R.id.min_day_7)).setText(displayNameList.get(6));
                }
            }
        }
    }





    public void goToWeb(View v){

        Validator.showToast(this,"This function is yet to be released");
    }

    ArrayList<Integer> totalSellingList =new ArrayList<>();
    private void setUpChartTotalSellingData(String date){

        Cursor productCur;
        Cursor unitCur;

        try{
            unitCur = db.getSumOfTotalSaleWdDate(date, this);
            while (unitCur.moveToNext()) {

                int total = unitCur.getInt(unitCur.getColumnIndex("Total"));
                totalSellingList.add(total);
            }
            if(unitCur==null){
                totalSellingList.add(0);
            }

        }catch (Exception e){
           // Validator.showToast(this,e.getMessage().toString());
        }finally{

        }

    }

    ArrayList<Integer> totalBuyingList =new ArrayList<>();
    private void setUpChartTotalBuyingData(String date){

        Cursor productCur;
        Cursor unitCur;

        try{
            unitCur = db.getSumOfTotalBuyingPriceDate(date, this);
            while (unitCur.moveToNext()) {

                int total = unitCur.getInt(unitCur.getColumnIndex("Total"));
                totalBuyingList.add(total);
            }
            if(unitCur==null){
                totalBuyingList.add(0);
            }

        }catch (Exception e){
            Validator.showToast(this,e.getMessage().toString());
        }finally{

        }

    }

    HashMap<String,Integer> mapUnitsProductId;
    ArrayList<Integer> maxlist;
    ArrayList<Integer> minlist;
    private void setUpChartData(String date){
        productList=new ArrayList<>();
        mapUnitsProductId =new HashMap<>();
        Cursor productCur;
        Cursor unitCur;

        try{
            productCur = db.getDistinctProductId(date,this);
            while (productCur.moveToNext()) {
                String productId = productCur.getString(productCur.getColumnIndex((ConstantsUsed.COLUMN_PRODUCT_ID)));
                productList.add(productId);
            }

            if(productList!=null && productList.size()>0){
                int i=0;
                for (i=0;i<productList.size();i++) {
                    unitCur = db.getSumOfUnitItemsWdProductId(date, productList.get(i), this);
                    while (unitCur.moveToNext()) {

                        int total = unitCur.getInt(unitCur.getColumnIndex("Total"));
                        mapUnitsProductId.put(productList.get(i),total);
                    }
                }
            }

        }catch (Exception e){

        }finally{
            if(mapUnitsProductId !=null && mapUnitsProductId.size()>0){
                findMax();
                findMin();
            }else{
                maxlist.add(0);
                minlist.add(0);
                keysMax.add("NO_KEY");
                keysMin.add("NO_KEY");
            }
        }

    }

    private void findMax(){
        max = Collections.max(mapUnitsProductId.values());
        maxlist.add(max);

        for (Map.Entry<String, Integer> entry : mapUnitsProductId.entrySet()) {
            if (entry.getValue()==max) {
                keysMax.add(entry.getKey());
            }
        }
    }

    int min=0;
    private void findMin(){
        min = Collections.min(mapUnitsProductId.values());
        minlist.add(min);

        for (Map.Entry<String, Integer> entry : mapUnitsProductId.entrySet()) {
            if (entry.getValue()==min) {
                keysMin.add(entry.getKey());
            }
        }
    }

    int max=0;
    List<String> keysMax = new ArrayList<>();
    List<String> keysMin = new ArrayList<>();
    private void setUpDays(){
        dayOfWeekList =new String[7];
        maxlist=new ArrayList<>();
        minlist=new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("EE");
        Date date = new Date();
        String dayOfTheWeek = sdf.format(date);
        String dateString= AppFeatures.dateToString(date);

        date=addDays(date,-6);
        dayOfTheWeek = sdf.format(date);
        dateString=AppFeatures.dateToString(date);
        setUpChartData(dateString);
        setUpChartTotalSellingData(dateString);
        setUpChartTotalBuyingData(dateString);
        dayOfWeekList[0]=dayOfTheWeek;

        date=addDays(date,+1);
        dayOfTheWeek = sdf.format(date);
        dateString=AppFeatures.dateToString(date);
        setUpChartData(dateString);
        setUpChartTotalSellingData(dateString);
        setUpChartTotalBuyingData(dateString);
        dayOfWeekList[1]=dayOfTheWeek;

        date=addDays(date,+1);
        dayOfTheWeek = sdf.format(date);
        dateString=AppFeatures.dateToString(date);
        setUpChartData(dateString);
        setUpChartTotalSellingData(dateString);
        setUpChartTotalBuyingData(dateString);
        dayOfWeekList[2]=dayOfTheWeek;

        date=addDays(date,+1);
        dayOfTheWeek = sdf.format(date);
        dateString=AppFeatures.dateToString(date);
        setUpChartData(dateString);
        setUpChartTotalSellingData(dateString);
        setUpChartTotalBuyingData(dateString);
        dayOfWeekList[3]=dayOfTheWeek;

        date=addDays(date,+1);
        dayOfTheWeek = sdf.format(date);
        dateString=AppFeatures.dateToString(date);
        setUpChartData(dateString);
        setUpChartTotalSellingData(dateString);
        setUpChartTotalBuyingData(dateString);
        dayOfWeekList[4]=dayOfTheWeek;

        date=addDays(date,+1);
        dayOfTheWeek = sdf.format(date);
        dateString=AppFeatures.dateToString(date);
        setUpChartData(dateString);
        setUpChartTotalSellingData(dateString);
        setUpChartTotalBuyingData(dateString);
        dayOfWeekList[5]="Yday";

        date=addDays(date,+1);
        dayOfTheWeek = sdf.format(date);
        dateString=AppFeatures.dateToString(date);
        setUpChartData(dateString);
        setUpChartTotalSellingData(dateString);
        setUpChartTotalBuyingData(dateString);
        dayOfWeekList[6]="Tday";
    }

    private void drawBarChart(ArrayList<Integer> list,GraphView graph){
        //Toast.makeText(this,String.valueOf(maxlist.get(2)),Toast.LENGTH_SHORT).show();
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, list.get(0)),
                new DataPoint(1, list.get(1)),
                new DataPoint(2, list.get(2)),
                new DataPoint(3, list.get(3)),
                new DataPoint(4, list.get(4)),
                new DataPoint(5, list.get(5)),
                new DataPoint(6, list.get(6))
        });
        graph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(dayOfWeekList);
        //staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);
    }

    private Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    private void drawLineGraph(){



// generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();

        GraphView graph = (GraphView) findViewById(R.id.graph_line);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, totalSellingList.get(0)),
                new DataPoint(1, totalSellingList.get(1)),
                new DataPoint(2, totalSellingList.get(2)),
                new DataPoint(3, totalSellingList.get(3)),
                new DataPoint(4, totalSellingList.get(4)),
                new DataPoint(5, totalSellingList.get(5)),
                new DataPoint(6, totalSellingList.get(6))
        });
        graph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(dayOfWeekList);
        //staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }
}
