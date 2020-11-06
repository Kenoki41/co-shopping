package comp5216.sydney.edu.au.a5216login.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.a5216login.R;

public class MoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        setTitle("More info");
    }
}
