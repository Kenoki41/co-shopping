package comp5216.sydney.edu.au.a5216login.shoppingRecords;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import comp5216.sydney.edu.au.a5216login.HttpReq.OKHttpTool;
import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.entity.Item;

//http://101.37.22.230:8080/item/list/conditions?catName=Transport&month=2020-11&userId=4
public class ShoppingRecordsMain extends AppCompatActivity {

    String url=OKHttpTool.SERVER_URL; //记得import OKHttpTool
    Long userId; //这个数据由登录界面传入，此处为Hardcode方便测试
    private int monthPosition;
    private String monthName;
    private String month;
    private String categoryName;
    private List<Item> itemsWithSpcifiedMonth;
    private List<Item> itemsWithSpcifiedMonthAndCategory;
    private View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_record);
        userId = getIntent().getLongExtra("userId", -1);


        monthName = getIntent().getStringExtra("monthName");
        monthPosition = getIntent().getIntExtra("position",-1) + 1;
        if(monthPosition < 10){
            month = "2020-0"+ monthPosition;
        }
        else{
            month = "2020-"+ monthPosition;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView111);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
         getSpecifiedMonthCategory(userId, month, recyclerView);

       // setOutput();



    }


    public void onClickPieChart(View v){

        Intent intent1 = new Intent(ShoppingRecordsMain.this, PieChartActivity.class);
        intent1.putExtra("userId", userId);
        intent1.putExtra("month", month);
        startActivity(intent1);
    }



    private void getSpecifiedMonthCategory(Long userId, String month, RecyclerView recyclerView){
        // Android 4.0 之后不能在主线程中请求HTTP请求
        // 否则会报错，每发送一次HTTP请求必须在一个新的thread中
        // 如果需要发送多次，需要在thread中嵌套thread
        // https://blog.csdn.net/qq_29477223/article/details/81027716

       // ArrayList<Item> returnList  = new ArrayList<Item>();

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
                                itemsWithSpcifiedMonth = JSONObject.parseArray(data.toJSONString(), Item.class); // 把返回值加载为Item的Object存到List中
                               // /item/list/conditions?month=2020-11&userId=4
                                //之后按需求拿数据，比如我要拿item的name全部放在一个String里
                                StringBuilder itemNames= new StringBuilder(); //可变字符序列
                                //First title list
                                ArrayList<Month> types = new ArrayList<>();
                                ArrayList<String> temItemCategory = new ArrayList<String>();
                               for(Item i: itemsWithSpcifiedMonth){
                                   temItemCategory.add(i.getCatName());




                                }

                                ArrayList<String>  itemCategory= new ArrayList<String>(new HashSet<String>(temItemCategory));
                               for(String categories : itemCategory){

                                   new Thread() {
                                       @Override
                                       public void run() {
                                           try {
                                               String responseStr = OKHttpTool.get(url + "/item/list/conditions?catName="+ categories + "&month=" + month + "&userId=" + userId);

                                               JSONObject jsonObject = JSON.parseObject(responseStr); // 倒入阿里的fastjson包 import com.alibaba.fastjson.JSONObject;
                                               //在子线程中调用ui线程
                                               runOnUiThread(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       if (jsonObject.getIntValue("code") == 2000) // 2000为服务器设置的success code，如2000则成功
                                                       {
                                                           JSONArray data = jsonObject.getJSONArray("data"); // 返回值全部存在data中
                                                           itemsWithSpcifiedMonthAndCategory = JSONObject.parseArray(data.toJSONString(), Item.class); // 把返回值加载为Item的Object存到List中
                                                           // /item/list/conditions?month=2020-11&userId=4
                                                           //之后按需求拿数据，比如我要拿item的name全部放在一个String里
                                                           StringBuilder itemNames= new StringBuilder(); //可变字符序列
                                                           ArrayList<Transaction> transactions = new ArrayList<Transaction>();
                                                           BigDecimal categoryTotalAmount = BigDecimal.valueOf(0);
                                                           for (Item item:itemsWithSpcifiedMonthAndCategory) {
                                                                transactions.add(new Transaction("$"+ (item.getPrice().multiply(new BigDecimal(item.getQuantity()))), ""+item.getDate(), item.getName()));
                                                               categoryTotalAmount = categoryTotalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));


                                                           }
                                                           Month category = new Month(categories, "$"+categoryTotalAmount, transactions );
                                                           types.add(category);


                                                           //set adapter
                                                           TransactionViewAdapter adapter = new TransactionViewAdapter(types);
                                                           recyclerView.setAdapter(adapter);



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

                               /* for(Month months: types){
                                    System.out.println("Category amount:" + months.getAmount());


                                }*/
                                System.out.println("Category amount:");
                                //启动Looper循环，否则Handler无法收到消息







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