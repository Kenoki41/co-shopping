package comp5216.sydney.edu.au.a5216login.shoppingRecords;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.IOException;
import java.util.List;

import comp5216.sydney.edu.au.a5216login.HttpReq.OKHttpTool;
import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.entity.Item;

public class PieChartActivity extends AppCompatActivity {
    // Create the object of TextView and PieChart class
    TextView tvR, tvPython, tvCPP, tvJava;
    PieChart pieChart;
    String url=OKHttpTool.SERVER_URL; //记得import OKHttpTool
    private Long userId;
    private String month;
    private List<Item> foodItems;
    private List<Item> ClothingItems;
    private List<Item> transportItems;
    private double totalSpend;
    private double totalTransportSpend;
    private double totalFoodSpend;
    private double totalClothingSpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piechart_activity);

        // Link those objects with their respective
        // id's that we have given in .XML file
        tvR = findViewById(R.id.tvR);
        tvPython = findViewById(R.id.tvPython);
        tvCPP = findViewById(R.id.tvCPP);
        tvJava = findViewById(R.id.tvJava);
        pieChart = findViewById(R.id.piechart);

        // Link those objects with their
        // respective id's that
        // we have given in .XML file
        tvR = findViewById(R.id.tvR);
        tvPython = findViewById(R.id.tvPython);
        tvCPP = findViewById(R.id.tvCPP);
        tvJava = findViewById(R.id.tvJava);
        pieChart = findViewById(R.id.piechart);

        //get userid and month
        userId = getIntent().getLongExtra("userId", -1);
        month = getIntent().getStringExtra("month");
        System.out.println("User ID: " + userId);

        //
        /*
        setFood(userId, month, "Food");

        setClothing(userId, month, "Clothing");

        setTransport(userId, month, "Transport");

       System.out.println( tvCPP.getText().toString());*/
        setElse(userId, month, "Food", "Clothing", "Transport");

        // Creating a method setData()
        // to set the text in text view and pie chart

    }




    private void setElse(Long userId, String month, String food, String clothing, String transport){
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get(url + "/item/list/conditions?month=" + month + "&userId=" + userId);

                    JSONObject jsonObject = JSON.parseObject(responseStr); // 倒入阿里的fastjson包 import com.alibaba.fastjson.JSONObject;
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) // 2000为服务器设置的success code，如2000则成功
                            {
                                JSONArray data = jsonObject.getJSONArray("data"); // 返回值全部存在data中
                                List<Item> totalItems = JSONObject.parseArray(data.toJSONString(), Item.class); // 把返回值加载为Item的Object存到List中
                                // /item/list/conditions?month=2020-11&userId=4
                                //之后按需求拿数据，比如我要拿item的name全部放在一个String里
                                StringBuilder itemNames= new StringBuilder(); //可变字符序列
                                //First title list
                                totalSpend = 0;
                                for(Item i: totalItems){
                                    totalSpend += i.getPrice().doubleValue() * (i.getQuantity());
                                }

                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            String responseStr = OKHttpTool.get(url + "/item/list/conditions?catName="+ food + "&month=" + month + "&userId=" + userId);

                                            JSONObject jsonObject = JSON.parseObject(responseStr); // 倒入阿里的fastjson包 import com.alibaba.fastjson.JSONObject;
                                            //在子线程中调用ui线程
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (jsonObject.getIntValue("code") == 2000) // 2000为服务器设置的success code，如2000则成功
                                                    {
                                                        JSONArray data = jsonObject.getJSONArray("data"); // 返回值全部存在data中
                                                        foodItems = JSONObject.parseArray(data.toJSONString(), Item.class); // 把返回值加载为Item的Object存到List中
                                                        // /item/list/conditions?month=2020-11&userId=4
                                                        //之后按需求拿数据，比如我要拿item的name全部放在一个String里
                                                        StringBuilder itemNames= new StringBuilder(); //可变字符序列
                                                        //First title list
                                                       totalFoodSpend = 0;
                                                        for(Item i: foodItems){
                                                            totalFoodSpend += i.getPrice().doubleValue() * (i.getQuantity());
                                                        }

                                                        tvR.setText(String.format("%.1f",(totalFoodSpend/totalSpend) * 100));
                                                        pieChart.addPieSlice(
                                                                new PieModel(
                                                                        "Food",
                                                                        (float) (Double.parseDouble(tvR.getText().toString())),
                                                                        Color.parseColor("#FFA726")));
                                                        // To animate the pie chart

                                                        new Thread() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    String responseStr = OKHttpTool.get(url + "/item/list/conditions?catName="+ clothing + "&month=" + month + "&userId=" + userId);

                                                                    JSONObject jsonObject = JSON.parseObject(responseStr); // 倒入阿里的fastjson包 import com.alibaba.fastjson.JSONObject;
                                                                    //在子线程中调用ui线程
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            if (jsonObject.getIntValue("code") == 2000) // 2000为服务器设置的success code，如2000则成功
                                                                            {
                                                                                JSONArray data = jsonObject.getJSONArray("data"); // 返回值全部存在data中
                                                                                ClothingItems = JSONObject.parseArray(data.toJSONString(), Item.class); // 把返回值加载为Item的Object存到List中
                                                                                // /item/list/conditions?month=2020-11&userId=4
                                                                                //之后按需求拿数据，比如我要拿item的name全部放在一个String里
                                                                                StringBuilder itemNames= new StringBuilder(); //可变字符序列
                                                                                //First title list
                                                                                totalClothingSpend = 0;
                                                                                for(Item i: ClothingItems){
                                                                                    totalClothingSpend += i.getPrice().doubleValue()* (i.getQuantity());
                                                                                }

                                                                                tvPython.setText(String.format("%.1f",(totalClothingSpend/totalSpend) * 100));
                                                                                pieChart.addPieSlice(
                                                                                        new PieModel(
                                                                                                "Clothing",
                                                                                                (float) (Double.parseDouble(tvPython.getText().toString())),
                                                                                                Color.parseColor("#66BB6A")));


                                                                                new Thread() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        try {
                                                                                            String responseStr = OKHttpTool.get(url + "/item/list/conditions?catName="+ transport + "&month=" + month + "&userId=" + userId);

                                                                                            JSONObject jsonObject = JSON.parseObject(responseStr); // 倒入阿里的fastjson包 import com.alibaba.fastjson.JSONObject;
                                                                                            //在子线程中调用ui线程
                                                                                            runOnUiThread(new Runnable() {
                                                                                                @Override
                                                                                                public void run() {
                                                                                                    if (jsonObject.getIntValue("code") == 2000) // 2000为服务器设置的success code，如2000则成功
                                                                                                    {
                                                                                                        JSONArray data = jsonObject.getJSONArray("data"); // 返回值全部存在data中
                                                                                                        transportItems = JSONObject.parseArray(data.toJSONString(), Item.class); // 把返回值加载为Item的Object存到List中
                                                                                                        // /item/list/conditions?month=2020-11&userId=4
                                                                                                        //之后按需求拿数据，比如我要拿item的name全部放在一个String里
                                                                                                        StringBuilder itemNames= new StringBuilder(); //可变字符序列
                                                                                                        //First title list
                                                                                                       totalTransportSpend = 0;
                                                                                                        for(Item i: transportItems){
                                                                                                            totalTransportSpend += i.getPrice().doubleValue()* (i.getQuantity());
                                                                                                        }

                                                                                                        tvCPP.setText(String.format("%.1f",(totalTransportSpend/totalSpend) * 100));
                                                                                                        pieChart.addPieSlice(
                                                                                                                new PieModel(
                                                                                                                        "Transport",
                                                                                                                        (float) (Double.parseDouble(tvCPP.getText().toString())),
                                                                                                                        Color.parseColor("#EF5350")));


                                                                                                      System.out.println(totalFoodSpend);
                                                                                                      System.out.println(totalClothingSpend);
                                                                                                      System.out.println(totalTransportSpend);

                                                                                                        tvJava.setText(String.format("%.1f",((totalSpend-(totalFoodSpend+totalClothingSpend+totalTransportSpend))/totalSpend) * 100));

                                                                                                        // Set the data and color to the pie chart



                                                                                                        pieChart.addPieSlice(
                                                                                                                new PieModel(
                                                                                                                        "Else",
                                                                                                                        (float) (Double.parseDouble(tvJava.getText().toString())),
                                                                                                                        Color.parseColor("#29B6F6")));

                                                                                                        //set layout
                                                                                                        pieChart.startAnimation();










                                                                                                    } else {
                                                                                                        // code不是2000，失败，向用户报告，一般情况是list里面没东西
                                                                                                        System.out.println("Fail!");
                                                                                                    }


                                                                                                }


                                                                                            });

                                                                                        } catch (IOException e) {
                                                                                            Log.i("IOException", "------------------------");
                                                                                            e.printStackTrace();
                                                                                        }





                                                                                    }


                                                                                }.start();









                                                                            } else {
                                                                                // code不是2000，失败，向用户报告，一般情况是list里面没东西
                                                                                System.out.println("Fail!");
                                                                            }


                                                                        }


                                                                    });

                                                                } catch (IOException e) {
                                                                    Log.i("IOException", "------------------------");
                                                                    e.printStackTrace();
                                                                }





                                                            }


                                                        }.start();










                                                    } else {
                                                        // code不是2000，失败，向用户报告，一般情况是list里面没东西
                                                        System.out.println("Fail!");
                                                    }


                                                }


                                            });

                                        } catch (IOException e) {
                                            Log.i("IOException", "------------------------");
                                            e.printStackTrace();
                                        }





                                    }


                                }.start();









                            } else {
                                // code不是2000，失败，向用户报告，一般情况是list里面没东西
                                System.out.println("Fail!");
                            }


                        }


                    });

                } catch (IOException e) {
                    Log.i("IOException", "------------------------");
                    e.printStackTrace();
                }





            }


        }.start();




    }







}










