package com.example.grandmagear.Patient_Main_Lobby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.example.grandmagear.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReportsActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected ReportsViewAdapter reportsViewAdapter;
    protected ArrayList<String> reportTitle = new ArrayList<String>();
    protected ArrayList<String> reportText = new ArrayList<String>();
    protected ArrayList<String> reportTime = new ArrayList<String>();
    protected FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        setupUI();

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

    public void setupUI(){
        firebaseHelper = new FirebaseHelper();
        recyclerView = findViewById(R.id.reportsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportTitle.add("hi");
        reportText.add("yo");
        reportTime.add("55");
        reportsViewAdapter = new ReportsViewAdapter(reportTitle, reportText, reportTime);
        recyclerView.setAdapter(reportsViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    public void createReports(){
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                .whereEqualTo(FirebaseObjects.ID, firebaseHelper.getCurrentUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                int bpm = (int) doc.get(FirebaseObjects.Heartrate);
                            }
                        }
                    }
                });
    }
}
