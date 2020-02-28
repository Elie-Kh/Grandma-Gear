package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    protected EditText mEmail, mPassword;
    protected Button mLoginButton;
    protected ProgressBar mLoginProgressBar;
    protected FirebaseAuth firebaseAuth;
    protected TextView mCreateAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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

        mEmail = findViewById(R.id.email_log_in);
        mPassword = findViewById(R.id.password_log_in);
        mLoginButton = findViewById(R.id.logInButton);
        mLoginProgressBar = findViewById(R.id.loginProgressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
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

                mLoginProgressBar.setVisibility(View.VISIBLE);

                /*authenticate the user*/

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, "Logged In Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserActivity.class));
                        }else{
                            Toast.makeText(LogInActivity.this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
                            mLoginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        mCreateAccountTextView = findViewById(R.id.createAccountTextView);

       String text = "Need Account? Sign Up Here";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        };

        ss.setSpan(clickableSpan,14,26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCreateAccountTextView.setText(ss);
        mCreateAccountTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPassword.setText("");
        mEmail.setText("");
        mLoginProgressBar.setVisibility(View.GONE);
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), UserActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPassword.setText("");
        mEmail.setText("");
        mLoginProgressBar.setVisibility(View.GONE);
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), UserActivity.class));
        }
    }
}
