package comp5216.sydney.edu.au.a5216login.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.adapter.CurrItemAdapter;
import comp5216.sydney.edu.au.a5216login.entity.Item;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;

public class MyWishListItemActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Item> items = new ArrayList<>();

    CurrItemAdapter currItemAdapter;


    public final int EDIT_ITEM_REQUEST_CODE = 647;
    public final int ADD_ITEM_REQUEST_CODE = 648;
    String itemUrl = OKHttpTool.SERVER_URL + "/item/";
    String userUrl = OKHttpTool.SERVER_URL + "/user/";
    Bundle bundle;
    EditText editText;
    private ProgressDialog pDialog;
    Long userId;
    String invitationCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish_list);

        //get info from main activity
        bundle = getIntent().getBundleExtra("message");
        this.setTitle(bundle.get("listName").toString());

        userId=bundle.getLong("userId");
        invitationCode=bundle.get("invitationCode").toString();

        listView = (ListView) findViewById(R.id.listView2);
        editText = findViewById(R.id.EditItemName);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //get data
        getAllItems();

    }

    public void onAddItemClick(View view) {
        Intent intent = new Intent(MyWishListItemActivity.this, AddItemActivity.class);
        if (intent != null) {
            // brings up the second activity
            bundle.putString("invitationCode", invitationCode);
            bundle.putLong("userId", userId);
            intent.putExtra("message", bundle);
            startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
            currItemAdapter.notifyDataSetChanged();
        }
    }

    public void setupItemViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = items.get(position);
                bundle.putLong("id", item.getId());
                Log.i("MyWishListItemActivity",item.getId().toString());
                bundle.putString("itemName", item.getName());
                bundle.putString("price", String.valueOf(item.getPrice()));
                bundle.putInt("quantity",item.getQuantity());
                bundle.putString("location",item.getLocation());
                bundle.putString("catName",item.getCatName());
                bundle.putInt("complete",item.getComplete());
                Intent intent = new Intent(MyWishListItemActivity.this, EditItemActivity.class);
                if (intent != null) {
                    intent.putExtra("message", bundle);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                }
            }

        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long rowId) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyWishListItemActivity.this);
                builder.setTitle(R.string.dialog_delete_item_title)
                        .setMessage(R.string.dialog_delete_item_msg)
                        .setPositiveButton(R.string.delete_yes, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Long itemId = items.get(position).getId();
                                        deleteItem(itemId, position);

                                    }
                                })
                        .setNegativeButton(R.string.cancel, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User cancelled the dialog
                                        // Nothing happens
                                    }
                                });
                builder.create().show();
                return true;
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
        } else if (requestCode == ADD_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                String itemName = data.getExtras().getString("itemName");
                getAllItems();

                // items.add(new CurrItem(itemName,itemPrice,itemQty));
                //Toast.makeText(this, "add " + itemName, Toast.LENGTH_SHORT).show();

                currItemAdapter.notifyDataSetChanged();
            }
        }
    }

    public void getMoreInfo(View view) {
        Intent intent = new Intent(MyWishListItemActivity.this, MoreInfoActivity.class);
        startActivity(intent);
    }


    private void deleteItem(Long itemId, int position) {
        pDialog.setMessage("Loading...");
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.delete(itemUrl + itemId);
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                hideDialog();
                                Toast.makeText(MyWishListItemActivity.this, "Delete list:" + items.get(position).getName() + " succeed", Toast.LENGTH_SHORT).show();
                                getAllItems();
                            } else {
                                hideDialog();
                                Toast.makeText(MyWishListItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

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
                                currItemAdapter = new CurrItemAdapter(items, MyWishListItemActivity.this.getLayoutInflater());
                                // Connect the listView and the adapter
                                listView.setAdapter(currItemAdapter);
                                currItemAdapter.notifyDataSetChanged();
                                // Setup listView listeners
                                setupItemViewListener();
                                hideDialog();
                            } else {
                                Toast.makeText(MyWishListItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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

    public void ShowKeyboard(View view) {

    }
}
