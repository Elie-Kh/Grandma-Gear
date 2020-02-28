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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    protected EditText mEmail;
    protected Button mPassword;
    protected ProgressBar mProgressBar;
    protected FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setupUI();
    }

    public void setupUI(){
        mEmail = findViewById(R.id.forgotPassEmail);
        mPassword = findViewById(R.id.sendEmailButton);
        mProgressBar = findViewById(R.id.forgotPassProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset sent to your email", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, LogInActivity.class);
                            startActivity(intent);
                        }else{
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(ForgotPasswordActivity.this, "A User with this email does not exist", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}
