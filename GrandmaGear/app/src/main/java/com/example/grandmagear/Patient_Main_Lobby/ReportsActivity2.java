package com.example.grandmagear.Patient_Main_Lobby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.R;

import java.util.ArrayList;

public class ReportsActivity2 extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected ReportsViewAdapter reportsViewAdapter;
    protected ArrayList<String> reportTitle = new ArrayList<String>();
    protected ArrayList<String> reportText = new ArrayList<String>();
    protected ArrayList<String> reportTime = new ArrayList<String>();
    protected FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports2);

        /*show back button*/
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*setup back click*/
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
