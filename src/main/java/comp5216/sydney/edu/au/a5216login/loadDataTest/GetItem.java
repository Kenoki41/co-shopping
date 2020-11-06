package comp5216.sydney.edu.au.a5216login.loadDataTest;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;

import comp5216.sydney.edu.au.a5216login.HttpReq.OKHttpTool;
import comp5216.sydney.edu.au.a5216login.entity.Item;


public class GetItem extends AppCompatActivity {
    String url=OKHttpTool.SERVER_URL; //记得import OKHttpTool
    int userId = 1; //这个数据由登录界面传入，此处为Hardcode方便测试


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getItem();




    }
    private void getItem(){
        // Android 4.0 之后不能在主线程中请求HTTP请求
        // 否则会报错，每发送一次HTTP请求必须在一个新的thread中
        // 如果需要发送多次，需要在thread中嵌套thread
        // https://blog.csdn.net/qq_29477223/article/details/81027716

        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get(url + "/item/list/all?userId=" + userId);
                    final JSONObject jsonObject = JSON.parseObject(responseStr); // 倒入阿里的fastjson包 import com.alibaba.fastjson.JSONObject;
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) // 2000为服务器设置的success code，如2000则成功
                            {
                                JSONArray data = jsonObject.getJSONArray("data"); // 返回值全部存在data中
                                List<Item> items = JSONObject.parseArray(data.toJSONString(), Item.class); // 把返回值加载为Item的Object存到List中

                                //之后按需求拿数据，比如我要拿item的name全部放在一个String里
                                StringBuilder itemNames= new StringBuilder(); //可变字符序列
                                for (Item item:items) {
                                    itemNames.append(item.getName()+" ");
                                    System.out.println(item.getName());
                                    System.out.println(item.getUserId());
                                    System.out.println(item.getDescription());
                                    System.out.println(item.getId());

                                }
                                // TextView中应该显示结果为：string abc 
                                // 因为用户ID为1的用户用两个item，一个item name是string，一个是abc
                                //textView.setText(itemNames.toString());




                            } else {
                                // code不是2000，失败，向用户报告，一般情况是list里面没东西
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

// 如果想要在线程中用toast message， 看这里：
// https://blog.csdn.net/qq_36807551/article/details/85316390