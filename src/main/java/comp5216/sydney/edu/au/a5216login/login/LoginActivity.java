package comp5216.sydney.edu.au.a5216login.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.activity.MainActivity;
import comp5216.sydney.edu.au.a5216login.login.helper.Functions;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;
import comp5216.sydney.edu.au.a5216login.login.helper.SessionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnLinkToRegister, btnForgotPass;
    private EditText inputEmail, inputPassword;
    private ProgressDialog pDialog;

    private SessionManager session;

    String url = OKHttpTool.SERVER_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.lTextEmail);
        inputPassword = findViewById(R.id.lTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);
        btnForgotPass = findViewById(R.id.btnForgotPassword);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // session manager
        session = new SessionManager(getApplicationContext());


        // check user is already logged in
        if (session.isLoggedIn()) {
            String userId = session.getUserId();
            Bundle b = new Bundle();
            b.putString("userId", userId);
            Intent upanel = new Intent(LoginActivity.this, MainActivity.class);
            upanel.putExtras(b);
            upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(upanel);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProcess();
            }
        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        // Forgot Password Dialog
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordDialog();
            }
        });

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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



    private void loginProcess() {
        if(inputEmail.getText().toString().matches("") && inputPassword.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(), "Enter Detail for Login", Toast.LENGTH_SHORT).show();
        } else {
            pDialog.setMessage("Logging in ...");
            showDialog();
            new Thread() {
                @Override
                public void run() {
                    try {
                        String responseStr = OKHttpTool.get(url + "/user/userId/email?email=" + inputEmail.getText().toString().trim());
                        String pwd = inputPassword.getText().toString().trim();
                        Map<String, String> userInput = new HashMap<>();
                        JSONObject jsonObject = JSON.parseObject(responseStr);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (jsonObject.getIntValue("code") == 2000) {
                                    int data = jsonObject.getIntValue("data");
                                    String userId = String.valueOf(data);
                                    userInput.put("userId", userId);
                                    userInput.put("password", pwd);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String loginStr = OKHttpTool.post(url + "/login/pwd", JSON.toJSONString(userInput));
                                                JSONObject loginObj = JSON.parseObject(loginStr);
                                                if (loginObj.getIntValue("code") == 2000) {
                                                    Bundle b = new Bundle();
                                                    b.putString("userId", userId);
                                                    Intent upanel = new Intent(LoginActivity.this, MainActivity.class);
                                                    upanel.putExtras(b);
                                                    upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(upanel);
                                                    session.setLogin(true);
                                                    session.setUserId(userId);
                                                    finish();
                                                    hideDialog();
                                                } else {
                                                    Looper.prepare();
                                                    hideDialog();
                                                    Toast.makeText(getApplicationContext(), "Login Information Not Correct!", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                }
                                                hideDialog();
                                            } catch (IOException e) {
                                                Log.i("IOException Verifying!", "------------------------");
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                } else {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "Login Information Not Correct!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (IOException e) {
                        Log.i("IOException", "------------------------");
                        e.printStackTrace();
                        hideDialog();
                    }
                }
            }.start();
        }
    }

    private void forgotPasswordDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reset_password, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Forgot Password");
        dialogBuilder.setCancelable(false);

        final EditText mEditEmail = dialogView.findViewById(R.id.editEmail);

        dialogBuilder.setPositiveButton("Reset",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // empty
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();

        mEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mEditEmail.getText().length() > 0){
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setEnabled(false);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = mEditEmail.getText().toString();

                        if (!email.isEmpty()) {
                            if (Functions.isValidEmailAddress(email)) {
                                resetPassword(email);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Fill all values!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        alertDialog.show();
    }

    private void resetPassword(final String email) {

        pDialog.setMessage("Please wait...");
        showDialog();

        new Thread(){
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get(url + "/login/securityCode?email=" + email);
                    JSONObject jsonObject = JSON.parseObject(responseStr);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(jsonObject.getIntValue("code") == 2000) {
                                Bundle b = new Bundle();
                                b.putString("email", email);
                                Intent upanel = new Intent(LoginActivity.this, EmailVerify.class);
                                upanel.putExtras(b);
                                upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(upanel);
                                finish();
                            } else {

                                Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}