package comp5216.sydney.edu.au.a5216login.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp5216.sydney.edu.au.a5216login.R;
import comp5216.sydney.edu.au.a5216login.adapter.CurrListAdapter;
import comp5216.sydney.edu.au.a5216login.entity.CoList;
import comp5216.sydney.edu.au.a5216login.shoppingRecords.MonthList;
import comp5216.sydney.edu.au.a5216login.util.OKHttpTool;
import comp5216.sydney.edu.au.a5216login.util.RandomStringGenerator;


public class MainActivity extends AppCompatActivity {
    // Define variables
    ListView listView;
    ArrayList<CoList> lists = new ArrayList<>();
    ArrayAdapter<CoList> coListArrayAdapter;
    public final int OPEN_LIST_REQUEST_CODE = 648;
    private ProgressDialog pDialog;
    Bundle getDataBundle, bundle;
    String listName;
    String leaderName;
    String url = OKHttpTool.SERVER_URL + "/list/";
    private Long userId;
    String idString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.listView);
        bundle = new Bundle();
        //test data
        getDataBundle = getIntent().getExtras();
        idString = getDataBundle.getString("userId");
        userId = Long.parseLong(idString);
        bundle.putLong("userId", userId);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        queryAllLists();

    }

    public void onAddListClick(View view) {
        addListDialog();
    }

    public void onJoinListClick(View view) {
        joinListDialog();
    }

    private void joinListDialog() {
        final androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search_list, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Please input invitation code");
        dialogBuilder.setCancelable(false);

        final EditText searchList = dialogView.findViewById(R.id.etSearchList);


        dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (searchList.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "invitation code can not be empty!", Toast.LENGTH_LONG).show();
                } else {
                    queryList(searchList.getText().toString());
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();

        searchList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchList.getText().length() > 0) {
                    alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        alertDialog.show();
    }

    private void joinListConfirm(Long userId) {
        final androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_join_list_confirm, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Do you sure to join this list?");
        dialogBuilder.setCancelable(false);

        final TextView tvListName = dialogView.findViewById(R.id.tvListName);
        final TextView tvLeaderName = dialogView.findViewById(R.id.tvCreatorName);

        tvListName.setText("List name:  " + listName);
        tvLeaderName.setText("Leader name:  " + leaderName);

        dialogBuilder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                joinList(listName, (long) bundle.get("userId"), (String) bundle.get("invitationCode"));
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();
    }


    private void addListDialog() {
        final androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_new_list, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Create List");
        dialogBuilder.setCancelable(false);

        final EditText listName = dialogView.findViewById(R.id.createListName);
        RadioButton button1 = dialogView.findViewById(R.id.radio0);
        RadioButton button2 = dialogView.findViewById(R.id.radio1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1.setChecked(true);
                button2.setChecked(false);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button2.setChecked(true);
                button1.setChecked(false);
            }
        });
        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listName.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "List name can not be empty!", Toast.LENGTH_LONG).show();
                } else if (button1.isChecked()) {
                    createList(String.valueOf(listName.getText()), 1);
                } else {
                    createList(String.valueOf(listName.getText()), 2);
                }

            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();

        listName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (listName.getText().length() > 0) {
                    alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        alertDialog.show();
    }


    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long rowId) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_list_title)
                        .setMessage(R.string.dialog_delete_list_msg)
                        .setPositiveButton(R.string.delete_yes, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Long listId = lists.get(position).getListId();
                                        lists.remove(position);// Remove item from the ArrayList
                                        //remove from remote database
                                        deleteList(listId, position);
                                        //saveItemsToDatabase();
                                        //to update the list
                                    }


                                })
                        .setNegativeButton(R.string.cancel, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User cancelled the dialog
                                        // Nothing happens
                                        queryAllLists();
                                    }
                                });
                builder.create().show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CoList coList = lists.get(position);
                bundle.putString("invitationCode", coList.getInvitationCode());
                bundle.putString("listName", coList.getListName());
                bundle.putLong("leaderId", coList.getLeaderId());

                //distinguish shopping list and wish list
                if (coList.getType() == 1) {
                    Intent intent = new Intent(MainActivity.this, ShoppingListItemActivity.class);
                    if (intent != null) {
                        intent.putExtra("message", bundle);
                        // brings up the second activity
                        startActivityForResult(intent, OPEN_LIST_REQUEST_CODE);
                        coListArrayAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (coList.getLeaderId() == bundle.get("userId")) {

                        Intent intent = new Intent(MainActivity.this, MyWishListItemActivity.class);
                        if (intent != null) {
                            intent.putExtra("message", bundle);
                            // brings up the second activity
                            startActivityForResult(intent, OPEN_LIST_REQUEST_CODE);
                            coListArrayAdapter.notifyDataSetChanged();
                        }
                    }else {

                        Intent intent = new Intent(MainActivity.this, WishListItemActivity.class);
                        if (intent != null) {
                            intent.putExtra("message", bundle);
                            // brings up the second activity
                            startActivityForResult(intent, OPEN_LIST_REQUEST_CODE);
                            coListArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // Extract name value from result extras
//                String editedItem = data.getExtras().getString("item");
//                int position = data.getIntExtra("position", -1);
//                lists.get(position).setListName(editedItem);
//                Log.i("Updated Item in list:", editedItem + ",position:"
//                        + position);
//                Toast.makeText(this, "updated:" + editedItem, Toast.LENGTH_SHORT).show();
//
//                //saveItemsToDatabase();
//            }
//        }
        if (requestCode == OPEN_LIST_REQUEST_CODE) {
            //Toast.makeText(this, "Return from " +bundle.get("listName"), Toast.LENGTH_SHORT).show();
            queryAllLists();
        }

    }


    private boolean deleteList(Long listId, int position) {
        final Boolean[] flag = {true};
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.delete(url + listId + "?type=delete");
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                hideDialog();
                                queryAllLists();
                                Toast.makeText(MainActivity.this, "Delete list:" + lists.get(position).getListName() + " succeed", Toast.LENGTH_SHORT).show();
                            } else {
                                hideDialog();
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                flag[0] = false;
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    hideDialog();
                }

            }
        }.start();
        return flag[0];
    }

    private void queryAllLists() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get(url + "lists?userId=" + (Long) bundle.get("userId"));
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                List<CoList> coLists = JSONObject.parseArray(data.toJSONString(), CoList.class);
                                lists.clear();
                                lists.addAll(coLists);
                                //Collections.sort(lists);
                                //  lists.sort((o1, o2) -> o2.getTime().compareTo(o1.getTime()));
                                coListArrayAdapter = new CurrListAdapter(MainActivity.this, lists);

                                // Connect the listView and the adapter
                                listView.setAdapter(coListArrayAdapter);
                                coListArrayAdapter.notifyDataSetChanged();
                                // Setup listView listeners
                                setupListViewListener();
                            } else {
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void createList(String listName, int type) {
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    Map<String, Object> listInfo = new HashMap<>();
                    listInfo.put("userId", (Long) bundle.get("userId"));
                    listInfo.put("type", type);
                    listInfo.put("invitationCode", RandomStringGenerator.getRandomString(8));
                    listInfo.put("name", listName);

                    String responseStr = OKHttpTool.post(url + "create", JSON.toJSONString(listInfo));
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                Toast.makeText(MainActivity.this, "Create list succeed", Toast.LENGTH_SHORT).show();
                                hideDialog();
                                queryAllLists();
                            } else {
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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

    private void queryList(String invitationCode) {
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get(url + "queryListInfo?invitationCode=" + invitationCode);
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                JSONObject jsonObject = JSON.parseObject(responseStr);
                                JSONObject data = jsonObject.getJSONObject("data");
                                listName = data.getString("ListName");
                                leaderName = data.getString("leaderName");
                                Long userId = (Long) bundle.get("userId");
                                bundle.putString("invitationCode", invitationCode);
                                hideDialog();
                                joinListConfirm(userId);
                            } else {
                                hideDialog();
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

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

    private void joinList(String listName, long userId, String invitationCode) {
        showDialog();
        new Thread() {
            @Override
            public void run() {
                try {
                    String responseStr = OKHttpTool.get(url + "join?invitationCode=" + invitationCode + "&name=" + listName + "&userId=" + userId);
                    JSONObject jsonObject = JSON.parseObject(responseStr);
                    //在子线程中调用ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject.getIntValue("code") == 2000) {
                                Toast.makeText(MainActivity.this, "Join succeed!", Toast.LENGTH_SHORT).show();
                                hideDialog();
                                queryAllLists();
                            } else {
                                hideDialog();
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
            pDialog.setMessage("Loading...");
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void viewReport(View view){

        Intent intent1 = new Intent(MainActivity.this, MonthList.class);
        intent1.putExtra("userId", userId);

        startActivity(intent1);

    }


}
