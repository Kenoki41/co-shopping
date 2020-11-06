package comp5216.sydney.edu.au.a5216login.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;

public class CompleteWishItemActivity extends Activity {
    TextView etItemName;
    TextView etItemPrice;
    TextView etItemQty;
    TextView etItemLoc;
    TextView cat;
    RadioButton yes;
    RadioButton no;
    String name;
    BigDecimal price;
    int quantity;
    String location;
    String catName;
    int complete;
    Bundle bundle;
    String url = OKHttpTool.SERVER_URL + "/item";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_wish_item);

        //Get the data from the main screen
        bundle = getIntent().getBundleExtra("message");
        //complete= Integer.parseInt(getIntent().getStringExtra("complete"));
        etItemName = findViewById(R.id.ItemName);
        etItemPrice = findViewById(R.id.ItemPrice);
        etItemQty = findViewById(R.id.ItemQuantity);
        etItemLoc = findViewById(R.id.ItemLocation);
        cat = findViewById(R.id.spinner);
        yes = findViewById(R.id.radio0);
        no = findViewById(R.id.radio1);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        name = (String) bundle.get("itemName");
        BigDecimal bd = new BigDecimal(bundle.get("price").toString());
        price = bd;
        quantity = (int) bundle.get("quantity");
        location = (String) bundle.get("location");
        catName = (String) bundle.get("catName");
        complete = bundle.getInt("complete");


        etItemName.setText(name);
        etItemPrice.setText(price.toString());
        etItemQty.setText(quantity + "");
        etItemLoc.setText(location);
        cat.setText(catName);
        if (complete == 0) {
            no.setChecked(true);
            yes.setChecked(false);
        } else {
            yes.setChecked(true);
            no.setChecked(false);
        }
    }


    public void onSubmit(View v) {
        name = etItemName.getText().toString();
        price = new BigDecimal(etItemPrice.getText().toString());
        quantity = Integer.parseInt(etItemQty.getText().toString());
        location = etItemLoc.getText().toString();
        catName = cat.getText().toString();
        if (yes.isChecked()) {
            complete = 1;
        } else {
            complete = 0;
        }

        updateItem(name,complete);
        setResult(RESULT_OK); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent

    }

    public void onCancel(View v) {
        finish();
    }


    public void updateItem(String name, int complete) {
        pDialog.setMessage("Loading...");
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    Map<String, Object> listInfo = new HashMap<>();
                    listInfo.put("id", bundle.getLong("id"));
                    listInfo.put("name",name);
                    listInfo.put("complete", complete);

                    String responseStr = OKHttpTool.put(url, JSON.toJSONString(listInfo));
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                Toast.makeText(CompleteWishItemActivity.this, "Successfully updated item completion status", Toast.LENGTH_SHORT).show();
                                hideDialog();
                            } else {
                                Toast.makeText(CompleteWishItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
