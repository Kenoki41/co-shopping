package comp5216.sydney.edu.au.a5216login.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.adapter.CurrItemAdapter;
import comp5216.sydney.edu.au.a5216login.entity.Item;
import comp5216.sydney.edu.au.a5216login.login.LoginActivity;
import comp5216.sydney.edu.au.a5216login.login.helper.SessionManager;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;

public class WishListItemActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Item> items = new ArrayList<>();

    CurrItemAdapter currItemAdapter;

    public final int EDIT_ITEM_REQUEST_CODE = 647;
    public final int ADD_ITEM_REQUEST_CODE = 648;
    String itemUrl = OKHttpTool.SERVER_URL + "/item/";
    String userUrl = OKHttpTool.SERVER_URL + "/user/";
    Bundle bundle;
    TextView textView;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get info from main activity
        bundle = getIntent().getBundleExtra("message");
        this.setTitle(bundle.get("listName").toString());

        //populate the screen using the layout
        setContentView(R.layout.activity_curr_wish_list);
        listView = (ListView) findViewById(R.id.listView);
        textView = findViewById(R.id.tvCreator);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // session manager
        session = new SessionManager(getApplicationContext());

        //get creator name
        getCreatorName(bundle.get("leaderId"));
        //get data
        getAllItems();


    }

    public void setupItemViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = items.get(position);
                bundle.putLong("id", item.getId());
                bundle.putString("itemName", item.getName());
                bundle.putString("price", String.valueOf(item.getPrice()));
                bundle.putInt("quantity",item.getQuantity());
                bundle.putString("location",item.getLocation());
                bundle.putString("catName",item.getCatName());
                bundle.putInt("complete",item.getComplete());
                Intent intent = new Intent(WishListItemActivity.this, CompleteWishItemActivity.class);
                if (intent != null) {
                    intent.putExtra("message", bundle);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getAllItems();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.log_out:
                Intent intent = new Intent(WishListItemActivity.this, LoginActivity.class);
                //set the login status to false , clean the userId
                session.setLogin(false);
                session.setUserId("");
                // brings up the second activity
                startActivity(intent);
                return true;


            case R.id.group_info:
                intent = new Intent(WishListItemActivity.this, CurrentListGroupInfoActivity.class);
                if (intent != null) {
                    // brings up the second activity
                    startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
                    currItemAdapter.notifyDataSetChanged();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getCreatorName(Object leaderId) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get(userUrl + leaderId);
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String leaderName = data.getString("userName");
                                textView.setText("Creator: "+leaderName);
                            } else {
                                //Toast.makeText(WishListItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void getAllItems() {
        pDialog.setMessage("Loading...");
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get(itemUrl + "list/all?listCode=" + bundle.get("invitationCode"));
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                List<Item> CurItems = JSONObject.parseArray(data.toJSONString(), Item.class);
                                items.clear();
                                items.addAll(CurItems);
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Collections.sort(items, new Comparator<Item>() {
                                    @Override
                                    public int compare(Item i1, Item i2) {
                                        // 直接反过来比较
                                        String date1 = formatter.format(i1.getOriginalDate());
                                        String date2 = formatter.format(i2.getOriginalDate());
                                        return date2.compareTo(date1);
                                    }
                                });
                                currItemAdapter = new CurrItemAdapter(items, WishListItemActivity.this.getLayoutInflater());
                                // Connect the listView and the adapter
                                listView.setAdapter(currItemAdapter);
                                currItemAdapter.notifyDataSetChanged();
                                // Setup listView listeners
                                setupItemViewListener();
                                hideDialog();
                            } else {
                                Toast.makeText(WishListItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    hideDialog();
                }

            }
        }.start();
    }


    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




}
