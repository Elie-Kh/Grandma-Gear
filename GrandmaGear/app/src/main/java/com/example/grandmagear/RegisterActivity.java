package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    protected EditText mUsername, mEmail, mPassword;
    protected Button mRegisterButton;
    protected ProgressBar mRegisterProgressBar;
    protected FirebaseAuth firebaseAuth;

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
        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegisterButton = findViewById(R.id.registerButton);
        mRegisterProgressBar = findViewById(R.id.registerProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            finish();
        }

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

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
                            Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mRegisterProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}
