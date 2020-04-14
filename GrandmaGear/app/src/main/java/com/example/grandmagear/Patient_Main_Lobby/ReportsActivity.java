package com.example.grandmagear.Patient_Main_Lobby;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;

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
import java.util.HashMap;

public class ReportsActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected ReportsViewAdapter reportsViewAdapter;
    protected ArrayList<String> reportTitle = new ArrayList<String>();
    protected ArrayList<String> reportText = new ArrayList<String>();
    protected ArrayList<String> reportTime = new ArrayList<String>();
    protected FirebaseHelper firebaseHelper;
    protected FirebaseObjects.UserDBO userDBO;
    protected ArrayList<FirebaseObjects.Notifications> reports;
    protected boolean check = false;

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
        createReports();
        reportsViewAdapter = new ReportsViewAdapter(reportTitle, reportText, reportTime, check,this);
        recyclerView.setAdapter(reportsViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    public void createReports(){
       firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
               .document(firebaseHelper.getCurrentUserID()).get()
               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       userDBO = task.getResult().toObject(FirebaseObjects.UserDBO.class);
                       reports = userDBO.notifications;
                       for(FirebaseObjects.Notifications entry : reports){
                           reportTitle.add(entry.getNotificationTitle());
                           reportText.add(entry.getNotificationText());
                           reportTime.add(entry.getNotificationTime());
                       }
                       reportsViewAdapter.notifyDataSetChanged();
                   }
               });
    }


}
