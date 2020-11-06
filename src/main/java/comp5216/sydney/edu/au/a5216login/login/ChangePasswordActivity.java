package comp5216.sydney.edu.au.a5216login.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.login.helper.Functions;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;

public class ChangePasswordActivity extends AppCompatActivity {

    Button btnChangePassword;
    Bundle bundle;
    EditText newPassword;
    String url = OKHttpTool.SERVER_URL;
    ProgressDialog pDialog;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        newPassword = findViewById(R.id.new_password);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        bundle = getIntent().getExtras();
        init();
    }

    private void init(){
        // Change password and return results
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide Keyboard
                Functions.hideSoftKeyboard(ChangePasswordActivity.this);

                String userId = bundle.getString("userId");
                String pwd = newPassword.getText().toString();
                if (!pwd.isEmpty()) {
                    changePassword(pwd, userId);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void changePassword(String password, String userId){
        pDialog.setMessage("Checking in ...");
        showDialog();
        Map<String, String> userInput = new HashMap<>();
        userInput.put("password", password);
        userInput.put("userId", userId);

        new Thread(){
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.put(url + "/login/pwd", JSON.toJSONString(userInput));
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    if(jsonObject.getIntValue("code") == 2000){
                        Intent upanel = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        startActivity(upanel);
                        finish();
                        hideDialog();
                    } else {
                        hideDialog();
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Unexpected Error Try Again", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                } catch (IOException e){
                    Log.i("IOException in Verifying Stage!", "------------------------");
                    hideDialog();
                    e.printStackTrace();
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