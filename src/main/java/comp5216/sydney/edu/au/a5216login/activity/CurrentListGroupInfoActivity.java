package comp5216.sydney.edu.au.a5216login.activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.a5216login.R;

public class CurrentListGroupInfoActivity extends AppCompatActivity {
    public int position = 0;
    EditText etItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


}
