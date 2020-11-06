package comp5216.sydney.edu.au.a5216login.shoppingRecords;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import comp5216.sydney.edu.au.a5216login.R;

public class MonthList extends AppCompatActivity {


    // Define variables
    ListView listView;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    private Long userId;
 

    public final int EDIT_ITEM_REQUEST_CODE = 647;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use "activity_main.xml" as the layout
        setContentView(R.layout.month_list);

        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.lstView);

        //get userId
        userId = getIntent().getLongExtra("userId", -1);


        // Create an ArrayList of String
        items = new ArrayList<String>();
        items.add("January");
        items.add("February");
        items.add("March");
        items.add("April");
        items.add("May");
        items.add("June");
        items.add("July");
        items.add("August");
        items.add("September");
        items.add("October");
        items.add("November");
        items.add("December");


        // Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        // Connect the listView and the adapter
        listView.setAdapter(itemsAdapter);

        // Setup listView listeners
        setupListViewListener();
    }




    private void setupListViewListener() {


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String updateItem = (String) itemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);

                Intent intent = new Intent(MonthList.this, ShoppingRecordsMain.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("userId", userId);
                    intent.putExtra("monthName", updateItem);
                    intent.putExtra("position", position);
                    // brings up the second activity

                }
                startActivity(intent);
            }
        });
    }
}
