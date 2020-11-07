package comp5216.sydney.edu.au.a5216login.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.adapter.CurrListAdapter;
import comp5216.sydney.edu.au.a5216login.entity.CoList;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;

public class CurrentListGroupInfoActivity extends AppCompatActivity {
    public int position = 0;
    EditText etItem;
    Long userId, listId, leaderId;
    String invitationCode;
    Bundle bundle;
    Button inviteBtn;
    ArrayAdapter<String> adapter;
    ListView listView;
    TextView createrTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        inviteBtn = findViewById(R.id.btnInvite);
        listView = (ListView) findViewById(R.id.list_view);
        createrTextView = (TextView) findViewById(R.id.createrTextView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get userId and I Code from Bundle
        bundle = getIntent().getBundleExtra("message");
        userId = bundle.getLong("userId");
        invitationCode = bundle.get("invitationCode").toString();
        listId = bundle.getLong("listId");

        // Display Leader

        // Display List View
        queryAllUser();

        // Assign function to button "Invitation Code"
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog();
            }
        });
    }

    /**
     *   Show invitation code and Invitation code copy function
     */
    public void onCreateDialog() {
        final AlertDialog.Builder alertCompat = new AlertDialog.Builder(this);
        alertCompat.setTitle("Invitation Code");
        alertCompat.setMessage("Your Invitation Code is: " + invitationCode);

        // Assign function to dialog button
        alertCompat.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Copy Code to Clipboard
        alertCompat.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", invitationCode);
                clipboard.setPrimaryClip(clip);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invitation Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });
        alertCompat.create();
        alertCompat.show();
    }

    /**
     *  Get all users name in the list;
     */
    private void queryAllUser() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get("http://101.37.22.230:8080/list/userNames?listId=" + userId);
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                List<String> list = new ArrayList<String>();
                                for(int i = 0; i < data.size(); i++){
                                    list.add(data.getString(i));
                                }

                                adapter = new ArrayAdapter<String>(CurrentListGroupInfoActivity.this, R.layout.list_user_adapter ,list);

                                // Connect the listView and the adapter
                                listView.setAdapter(adapter);


                            } else {
                                Toast.makeText(getApplicationContext(), "List Not Found",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * Display list creator
     */
    private void displayLeader(){
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get("http://101.37.22.230:8080/list/queryListInfo?invitationCode=" + invitationCode);
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                String leaderName = jsonObject.getString("leaderName");
                                createrTextView.setText("List Creator: " + leaderName);
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
