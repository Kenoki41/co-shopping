package comp5216.sydney.edu.au.a5216login.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class EditItemActivity extends Activity {
    public int position = 0;

    EditText etItemName;
    EditText etItemPrice;
    EditText etItemQty;
    EditText etItemLoc;
    Spinner cat;
    RadioButton yes;
    RadioButton no;
    RadioGroup radioGroup;
    String name;
    BigDecimal price;
    int quantity;
    String location;
    String catName;
    int complete;
    Bundle bundle;
    String url = OKHttpTool.SERVER_URL + "/item";
    private ProgressDialog pDialog;

    public final int EDIT_ITEM_REQUEST_CODE = 647;
    public final int ADD_ITEM_REQUEST_CODE = 650;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate the screen using the layout
        setTitle("Edit Chosen Item");
        setContentView(R.layout.dialog_edit_curr_item);

        //Get the data from the main screen
        bundle = getIntent().getBundleExtra("message");
        //complete= Integer.parseInt(getIntent().getStringExtra("complete"));
        etItemName = (EditText) findViewById(R.id.ItemName);
        etItemPrice = (EditText) findViewById(R.id.ItemPrice);
        etItemQty = (EditText) findViewById(R.id.ItemQuantity);
        etItemLoc = (EditText) findViewById(R.id.ItemLocation);
        cat= findViewById(R.id.spinner);
        yes = findViewById(R.id.radio0);
        no = findViewById(R.id.radio1);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        name=(String)bundle.get("itemName");
        BigDecimal bd=new BigDecimal(bundle.get("price").toString());
        price= bd;
        quantity= (int) bundle.get("quantity");
        location = (String)bundle.get("location");
        catName = (String)bundle.get("catName");
        complete= (int) bundle.get("complete");



        etItemName.setText(name);
        etItemPrice.setText(price.toString());
        etItemQty.setText(quantity+"");
        etItemLoc.setText(location);
        switch (catName){
            case "Food":
                cat.setSelection(0);
                return;
            case "Clothing":
                cat.setSelection(1);
                return;
            case "Transport":
                cat.setSelection(2);
                return;
            case "Housing":
                cat.setSelection(3);
                return;
            default:
                cat.setSelection(4);
        }

        if (complete==0){
            no.setChecked(true);
        }
        else if(complete == 1) {
            yes.setChecked(true);
        } else {
            Toast.makeText(EditItemActivity.this, "no complete data", Toast.LENGTH_SHORT).show();
        }
//        //check whether the item is completed and initial the radio group
//        isComplete();



    }

    public void onSubmit(View v){
        name = etItemName.getText().toString();
        price=new BigDecimal(etItemPrice.getText().toString());
        quantity=Integer.parseInt(etItemQty.getText().toString());
        location= etItemLoc.getText().toString();
        catName=cat.getSelectedItem().toString();
        if (yes.isChecked()){
            complete=1;
        }else {
            complete=0;
        }

        updateItem(name,price,quantity,location,catName,complete);
        setResult(RESULT_OK); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent

    }

    public void onCancel(View v) {
        finish();
    }


    public void updateItem(String name, BigDecimal price , int quantity,String location,String catName,int complete){
        pDialog.setMessage("Loading...");
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    Map<String, Object> listInfo = new HashMap<>();
                    listInfo.put("id", bundle.getLong("id"));
                    Log.i("EditItemActivity", String.valueOf(bundle.getLong("id")));
                    listInfo.put("name", name);
                    listInfo.put("price", price);
                    listInfo.put("quantity", quantity);
                    listInfo.put("location", location);
                    listInfo.put("catName", catName);
                    listInfo.put("complete", complete);

                    String responseStr = OKHttpTool.put(url, JSON.toJSONString(listInfo));
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                Toast.makeText(EditItemActivity.this, "Edit item succeed", Toast.LENGTH_SHORT).show();
                                hideDialog();
                            } else {
                                Toast.makeText(EditItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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

    public void isComplete(){
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {

                    String responseStr = OKHttpTool.get("/isComplete?itemId="+bundle.getLong("id"));
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                int data = jsonObject.getInteger("data");
                                if (data==0){
                                    no.setChecked(true);
                                    yes.setChecked(false);
                                }
                                else {
                                    yes.setChecked(true);
                                    no.setChecked(false);
                                }
                                hideDialog();
                            } else {
                                Toast.makeText(EditItemActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
