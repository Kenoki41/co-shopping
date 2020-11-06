package comp5216.sydney.edu.au.a5216login.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;

public class AddItemActivity extends Activity {

    Bundle bundle;
    private ProgressDialog pDialog;
    EditText newItemName, newItemPrice, newItemQty, newItemLoc;
    Spinner spinner;
    String itemName;
    BigDecimal price;
    int quantity;
    String location;
    String cat;
    String itemUrl = OKHttpTool.SERVER_URL + "/item/";
    static final String[] cats = {"Food","Clothing","Transport","Housing","Else"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate the screen using the layout
        setContentView(R.layout.activity_create_new_item);

        //get info from main activity
        bundle = getIntent().getBundleExtra("message");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        newItemName = (EditText) findViewById(R.id.CreateItemName);
        newItemPrice = (EditText) findViewById(R.id.CreateItemPrice);
        newItemQty = (EditText) findViewById(R.id.CreateItemQuantity);
        newItemLoc = (EditText) findViewById(R.id.CreateItemLocation);
        spinner = findViewById(R.id.spCat);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat=cats[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void onSubmit(View v) {



        if (newItemName.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), " Item's name is required",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (newItemPrice.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), " Item's price is required",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (newItemQty.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), " Item's quantity is required",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        itemName = newItemName.getText().toString();
        price = new BigDecimal(newItemPrice.getText().toString());
        quantity = Integer.parseInt(newItemQty.getText().toString());
        location = newItemLoc.getText().toString();
        cat = spinner.getSelectedItem().toString();

        long userId = bundle.getLong("userId");
        Log.i("AddItemActivity",userId+"");
        String invitationCode = bundle.getString("invitationCode");
        Log.i("AddItemActivity",invitationCode);
        addNewItem(userId, itemName, cat, invitationCode, price, location);


        // Prepare data intent for sending it back
        Intent data = new Intent();

        // Pass relevant data back as a result
        data.putExtra("itemName", newItemName.getText().toString());

//        // Activity finished ok, return the data
//        startActivityForResult(data, ADD_ITEM_REQUEST_CODE);
//        currItemAdapter.notifyDataSetChanged();

        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent

    }

    private void addNewItem(long userId, String itemName, String cat, String invitationCode, BigDecimal price, String location) {
        pDialog.setMessage("Loading...");
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    Map<String, Object> listInfo = new HashMap<>();
                    listInfo.put("userId", userId);
                    listInfo.put("name", itemName);
                    listInfo.put("price", price);
                    listInfo.put("quantity", quantity);
                    listInfo.put("location", location);
                    listInfo.put("catName", cat);
                    listInfo.put("listCode", invitationCode);

                    String responseStr = OKHttpTool.post(itemUrl, JSON.toJSONString(listInfo));
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                Toast.makeText(AddItemActivity.this, "Add item succeed", Toast.LENGTH_SHORT).show();
                                hideDialog();
                            } else {
                                Toast.makeText(AddItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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


    public void onCancel(View v) {
        finish();
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
