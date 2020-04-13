package com.example.grandmagear.Patient_Main_Lobby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.grandmagear.FirebaseHelper;
import com.example.grandmagear.FirebaseObjects;
import com.example.grandmagear.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ReportsActivity2 extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected String WearerID;
    protected ReportsViewAdapter reportsViewAdapter;
    protected ArrayList<String> reportTitle = new ArrayList<String>();
    protected ArrayList<String> reportText = new ArrayList<String>();
    protected ArrayList<String> reportTime = new ArrayList<String>();
    protected FirebaseHelper firebaseHelper;
    protected FirebaseObjects.UserDBO userDBO;
    protected FirebaseObjects.DevicesDBO devicesDBO;
    protected ArrayList<FirebaseObjects.Notifications> reports;
    protected boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports2);

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
        Bundle b = getIntent().getExtras();
        WearerID = b.getString("wearerID_MPP_2");
        firebaseHelper = new FirebaseHelper();
        recyclerView = findViewById(R.id.reports2RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        createReports();
        reportsViewAdapter = new ReportsViewAdapter(reportTitle, reportText, reportTime, check);
        recyclerView.setAdapter(reportsViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    public void createReports(){
        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.deviceDB)
                .document(WearerID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        devicesDBO = doc.toObject(FirebaseObjects.DevicesDBO.class);
                        firebaseHelper.firebaseFirestore.collection(FirebaseHelper.userDB)
                                .document(devicesDBO.getId())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot doc = task.getResult();
                                        userDBO = doc.toObject(FirebaseObjects.UserDBO.class);
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
                });
    }
}
