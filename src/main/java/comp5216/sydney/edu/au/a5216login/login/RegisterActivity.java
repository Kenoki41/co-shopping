package comp5216.sydney.edu.au.a5216login.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.login.helper.Functions;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button btnRegister, btnLinkToLogin;
    private EditText inputName, inputEmail, inputPassword;
    private ProgressDialog pDialog;
    String url = OKHttpTool.SERVER_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputName = findViewById(R.id.edit_name);
        inputEmail = findViewById(R.id.edit_email);
        inputPassword = findViewById(R.id.edit_password);
        btnRegister = findViewById(R.id.button_register);
        btnLinkToLogin = findViewById(R.id.button_login);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        // Login button Click Event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hide Keyboard
                Functions.hideSoftKeyboard(RegisterActivity.this);

                String name = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if (Functions.isValidEmailAddress(email)) {
                        registerUser(name, email, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Link to Register Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void registerUser(final String name, final String email, final String password) {
        pDialog.setMessage("Registering...");
        showDialog();
        Map<String, String> registerReq;
        registerReq = new HashMap<>();
        registerReq.put("email", email);
        registerReq.put("password", password);
        registerReq.put("userName", name);

        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.post(url+"/user", JSON.toJSONString(registerReq));
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                Toast.makeText(getApplicationContext(), "Register Complete", Toast.LENGTH_SHORT).show();
                                Intent upanel = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(upanel);
                                finish();
                                hideDialog();
                            } else {
                                Toast.makeText(getApplicationContext(), "User Already Exist", Toast.LENGTH_SHORT).show();
                                hideDialog();
                            }
                        }
                    });
                } catch (IOException e) {
                    Log.i("IOException", "------------------------");
                    hideDialog();
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void showDialog() {
        if (!pDialog.isShowing()){
            pDialog.show();
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}