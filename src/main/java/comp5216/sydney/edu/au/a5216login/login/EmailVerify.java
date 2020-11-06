package comp5216.sydney.edu.au.a5216login.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import comp5216.sydney.edu.au.a5216login.login.helper.Functions;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;
import comp5216.sydney.edu.au.a5216login.login.helper.SessionManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import comp5216.sydney.edu.au.a5216login.R;


public class EmailVerify extends AppCompatActivity {
    private static final String TAG = EmailVerify.class.getSimpleName();
    String url = OKHttpTool.SERVER_URL;

    private EditText textVerifyCode;
    private Button btnVerify, btnResend;
    private TextView otpCountDown;

    private SessionManager session;
    private ProgressDialog pDialog;

    private static final String FORMAT = "%02d:%02d";

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        textVerifyCode = findViewById(R.id.verify_code);
        btnVerify = findViewById(R.id.btnVerify);
        btnResend = findViewById(R.id.btnResendCode);
        otpCountDown = findViewById(R.id.otpCountDown);

        bundle = getIntent().getExtras();

        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide Keyboard
                Functions.hideSoftKeyboard(EmailVerify.this);

                String email = bundle.getString("email");
                String otp = textVerifyCode.getText().toString();
                if (!otp.isEmpty()) {
                    verifyCode(email, otp);
                } else {
                    textVerifyCode.setError("Please enter verification code");
                }
            }
        });

        btnResend.setEnabled(false);
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = bundle.getString("email");
                resendCode(email);
            }
        });

        countDown();
    }

    private void countDown() {
        new CountDownTimer(120000, 1000) { // adjust the milli seconds here

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void onTick(long millisUntilFinished) {
                otpCountDown.setVisibility(View.VISIBLE);
                otpCountDown.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) ));
            }

            public void onFinish() {
                otpCountDown.setVisibility(View.GONE);
                btnResend.setEnabled(true);
            }
        }.start();
    }

    private void verifyCode(final String email, final String securityCode) {
        // Tag used to cancel the request
        String tag_string_req = "req_verify_code";

        pDialog.setMessage("Checking in ...");
        showDialog();


        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get(url + "/user/userId/email?email=" + email);
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000){
                                int data = jsonObject.getIntValue("data");
                                String userId = String.valueOf(data);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String resStr = OKHttpTool.get(url + "/login/verifyCode?securityCode=" + securityCode+ "&userId=" + userId );
                                            JSONObject loginObj = JSON.parseObject(resStr);
                                            if(loginObj.getIntValue("code") == 2000){
                                                Bundle b = new Bundle();
                                                b.putString("userId", userId);
                                                Intent upanel = new Intent(EmailVerify.this, ChangePasswordActivity.class);
                                                upanel.putExtras(b);
                                                upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(upanel);
                                                finish();
                                                hideDialog();
                                            } else {
                                                Looper.prepare();
                                                hideDialog();
                                                Toast.makeText(getApplicationContext(), "Verify Code Not Correct!", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }
                                        } catch (IOException e) {
                                            Log.i("verify code URL not correct", "------------------------");
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                            } else {
                                Log.i("Code not success!", "------------------------");
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();



    }

    private void resendCode(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_resend_code";

        pDialog.setMessage("Resending code ...");
        showDialog();


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

    @Override
    public void onResume(){
        super.onResume();
        countDown();
    }
}