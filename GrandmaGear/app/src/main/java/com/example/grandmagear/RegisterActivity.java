package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "_RegisterActivity";

    protected EditText mName, mEmail, mPassword;
    protected Button mRegisterButton;
    protected ProgressBar mRegisterProgressBar;
    protected FirebaseAuth firebaseAuth;
    protected FirebaseFirestore firebaseFirestore;
    protected String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

    void setupUI(){
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegisterButton = findViewById(R.id.registerButton);
        mRegisterProgressBar = findViewById(R.id.registerProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            finish();
        }

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String name = mName.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must be >= 6 characters.");
                    return;
                }

                mRegisterProgressBar.setVisibility(View.VISIBLE);

                /*Register the user in Firebase*/

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User Created!", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name", name);
                            user.put("Email", email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                }
                            });
                            documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "A user with this email already exists!", Toast.LENGTH_SHORT).show();
                            mRegisterProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEmail.setText("");
        mPassword.setText("");
        mName.setText("");
        mRegisterProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEmail.setText("");
        mPassword.setText("");
        mName.setText("");
        mRegisterProgressBar.setVisibility(View.GONE);
    }
}
