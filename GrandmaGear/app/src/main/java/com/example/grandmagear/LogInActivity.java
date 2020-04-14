package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grandmagear.Patient_Main_Lobby.HomePage_MPP_1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

public class LogInActivity extends AppCompatActivity {

    protected EditText mEmail, mPassword;
    protected Button mLoginButton;
    protected ProgressBar mLoginProgressBar;
    protected FirebaseAuth firebaseAuth;
    protected TextView mCreateAccountTextView, mForgotPassTextView;
    protected SharedPreferencesHelper mSharedPreferencesHelper;
    protected SharedPreferencesHelper mSharedPreferences;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //check if user had previously accepted disclaimer. If not, open fragment
        mSharedPreferences = new SharedPreferencesHelper(LogInActivity.this,
                "DisclaimerPreferences");
        if(mSharedPreferences.getDisclaimerStatus() == null
                || mSharedPreferences.getDisclaimerStatus().equals("False")){
            DisclaimerFragment disclaimerFragment = new DisclaimerFragment();
            disclaimerFragment.setCancelable(false);
            disclaimerFragment.show(getSupportFragmentManager(), "DisclaimerFragment");
        }

        mSharedPreferencesHelper = new SharedPreferencesHelper(LogInActivity.this, "Login");
        setupUI();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void setupUI(){

        mEmail = findViewById(R.id.email_log_in);
        mPassword = findViewById(R.id.password_log_in);
        mLoginButton = findViewById(R.id.logInButton);
        mLoginProgressBar = findViewById(R.id.loginProgressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseHelper = new FirebaseHelper();
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();

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
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                mSharedPreferencesHelper.saveEmail(email);
                                mSharedPreferencesHelper.savePassword(password);
                                Toast.makeText(LogInActivity.this, "Logged In Successfully.", Toast.LENGTH_SHORT).show();
                                final boolean[] emails = new boolean[1];
                                FirebaseHelper.firebaseFirestore
                                        .collection(FirebaseHelper.userDB)
                                        .document(firebaseAuth.getCurrentUser().getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    FirebaseObjects.UserDBO user = documentSnapshot.toObject(FirebaseObjects.UserDBO.class);

                                                    mSharedPreferencesHelper.saveEmail
                                                            ((String) documentSnapshot.get(FirebaseObjects.Email));
                                                    mSharedPreferencesHelper.saveType((Boolean)
                                                            documentSnapshot.get(FirebaseObjects.Account_Type));
                                                    if ((Boolean)
                                                            documentSnapshot.get(FirebaseObjects.Account_Type)) {
                                                        Intent serviceIntent = new Intent(LogInActivity.this, FirestoreService.class);
                                                        startService(serviceIntent);
                                                        startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                                    } else {
                                                        Intent serviceIntent = new Intent(LogInActivity.this, FirestoreService.class);
                                                        startService(serviceIntent);
                                                        startActivity(new Intent(getApplicationContext(), HomePage_MPP_1.class));
                                                    }

                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(LogInActivity.this, "Please Verify Email.", Toast.LENGTH_SHORT).show();
                                mLoginProgressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(LogInActivity.this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
                            mLoginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        mCreateAccountTextView = findViewById(R.id.createAccountTextView);

        String text1 = "Need Account? Sign Up Here";
        SpannableString ss1 = new SpannableString(text1);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                //startActivity(intent);
                goToSyncDisclaimer();
            }
        };

        ss1.setSpan(clickableSpan1,14,26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCreateAccountTextView.setText(ss1);
        mCreateAccountTextView.setMovementMethod(LinkMovementMethod.getInstance());

        mForgotPassTextView = findViewById(R.id.forgotPass);

        String text2 = "Forgot Password?";
        SpannableString ss2 = new SpannableString(text2);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        };

        ss2.setSpan(clickableSpan2,0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mForgotPassTextView.setText(ss2);
        mForgotPassTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPassword.setText("");
        mEmail.setText("");
        mLoginProgressBar.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPassword.setText("");
        mEmail.setText("");
        mLoginProgressBar.setVisibility(View.GONE);
        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()){
            if(mSharedPreferencesHelper.getEmail() != null) {
                final String email = mSharedPreferencesHelper.getEmail();
                final String password = mSharedPreferencesHelper.getPassword();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                            final boolean[] emails = new boolean[1];
                            FirebaseHelper.firebaseFirestore
                                    .collection(FirebaseHelper.userDB)
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            mSharedPreferencesHelper.saveEmail
                                                    ((String)documentSnapshot.get(FirebaseObjects.Email));
                                            mSharedPreferencesHelper.saveType((Boolean)
                                                    documentSnapshot.get(FirebaseObjects.Account_Type));
                                            if((Boolean)
                                                    documentSnapshot.get(FirebaseObjects.Account_Type)){
                                                Intent serviceIntent = new Intent(LogInActivity.this, FirestoreService.class);
                                                startService(serviceIntent);
                                                startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                            }
                                            else {
                                                Intent serviceIntent = new Intent(LogInActivity.this, FirestoreService.class);
                                                startService(serviceIntent);
                                                startActivity(new Intent(getApplicationContext(), HomePage_MPP_1.class));
                                            }
                                        }
                                    });


                        }else{
                            ConnectivityManager connectivityManager
                                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                            if(activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                                if (mSharedPreferencesHelper.getEmail() != null && !mSharedPreferencesHelper.getEmail().isEmpty()) {
                                    if (Boolean.parseBoolean(mSharedPreferencesHelper.getType())) {
                                        Intent serviceIntent = new Intent(LogInActivity.this, FirestoreService.class);
                                        startService(serviceIntent);
                                        startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                    } else {
                                        Intent serviceIntent = new Intent(LogInActivity.this, FirestoreService.class);
                                        startService(serviceIntent);
                                        startActivity(new Intent(getApplicationContext(), HomePage_MPP_1.class));
                                    }
                                }
                            }
                            else {
                                Toast.makeText(LogInActivity.this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
                                mLoginProgressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
            }
        }

    }

    public void goToSyncDisclaimer(){
        BTSyncFrag btSyncFrag = new BTSyncFrag(this);
        btSyncFrag.setCancelable(false);
        btSyncFrag.show(getSupportFragmentManager(),"BTSyncFrag");
    }

}
