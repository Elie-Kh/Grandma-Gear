package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "_RegisterActivity";

    protected EditText mFirstName, mLastName, mEmail, mPassword, mAge, mWeight, mHeight, mDevice;
    protected Button mRegisterButton;
    protected Spinner mRegisterSpinner;
    protected ProgressBar mRegisterProgressBar;
    protected FirebaseAuth firebaseAuth;
    protected FirebaseFirestore firebaseFirestore;
    protected FirebaseHelper firebaseHelper;
    protected SharedPreferencesHelper mSharedPreferencesHelper;
    protected String userID;
    protected boolean save = true;
    protected boolean acc_type = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mSharedPreferencesHelper = new SharedPreferencesHelper(RegisterActivity.this, "Login");
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
        mFirstName = findViewById(R.id.firstName);
        mLastName = findViewById(R.id.lastName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mAge = findViewById(R.id.age);
        mWeight = findViewById(R.id.weight);
        mHeight = findViewById(R.id.height);
        mDevice = findViewById(R.id.deviceID);
        mRegisterButton = findViewById(R.id.registerButton);
        mRegisterProgressBar = findViewById(R.id.registerProgressBar);
        mRegisterSpinner = findViewById(R.id.registerSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.selection_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRegisterSpinner.setAdapter(adapter);
        mRegisterSpinner.setSelection(0);

        mRegisterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = parent.getItemAtPosition(position).toString();

                if(selected_item.equals("Follower")){
                    mAge.setVisibility(View.GONE);
                    mHeight.setVisibility(View.GONE);
                    mWeight.setVisibility(View.GONE);
                    mDevice.setVisibility(View.GONE);
                    acc_type = true;
                }else{
                    mAge.setVisibility(View.VISIBLE);
                    mHeight.setVisibility(View.VISIBLE);
                    mWeight.setVisibility(View.VISIBLE);
                    mDevice.setVisibility(View.VISIBLE);
                    acc_type = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseHelper = new FirebaseHelper();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            finish();
        }

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String firstName = mFirstName.getText().toString();
                final String lastName = mLastName.getText().toString();
                final String age = mAge.getText().toString();
                final String weight = mWeight.getText().toString();
                final String height = mHeight.getText().toString();
                final String deviceID = mDevice.getText().toString();
                save = true;

                if(TextUtils.isEmpty(firstName) || firstName.trim().isEmpty()){
                    mFirstName.setError("First Name is required");
                    save = false;
                }

                if(TextUtils.isEmpty(lastName) || lastName.trim().isEmpty()){
                    mLastName.setError("Last Name is required");
                    save = false;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    save = false;
                    return;
                }

                if(!isValidEmail(email)){
                    mEmail.setError("Email is not valid");
                    save = false;
                }

                if(!acc_type) {
                    if (TextUtils.isEmpty(age) && mAge.getText().toString().equals("")) {
                        mAge.setError("Age is required");
                        save = false;
                    }else if (Integer.parseInt(age) < 12){
                            mAge.setError("You are to young to use this app");
                        save = false;
                        }

                    if (TextUtils.isEmpty(weight) && mWeight.getText().toString().equals("")) {
                        mWeight.setError("Weight is required");
                        save = false;
                    }else if(Integer.parseInt(weight) < 80 || Integer.parseInt(weight) > 400){
                        mWeight.setError("Weight must be between 80 lbs and 400 lbs");
                        save = false;
                    }

                    if (TextUtils.isEmpty(height) && mHeight.getText().toString().equals("")) {
                        mHeight.setError("Height is required");
                        save = false;
                    }else if(Integer.parseInt(height) < 120 || Integer.parseInt(height) > 250){
                        mHeight.setError("Height must be between 120 cm and 250 cm");
                        save = false;
                    }
                    if(TextUtils.isEmpty(deviceID) || deviceID.trim().isEmpty()){
                        mDevice.setError("Device ID required");
                        save = false;
                    }else if(deviceID.length() < 5){
                        mDevice.setError("ID must be 5 digits");
                        save = false;
                    }
                    if(TextUtils.isEmpty(deviceID) || deviceID.trim().isEmpty()){
                        mDevice.setError("Device ID required");
                        save = false;
                    }else if(deviceID.length() < 5){
                        mDevice.setError("ID must be 5 numbers");
                        save = false;
                    }
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required.");
                    save = false;
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must be >= 6 characters.");
                    save = false;
                    return;
                }

                if(save) {
                    mRegisterProgressBar.setVisibility(View.VISIBLE);

                    /*Register the user in Firebase*/

                    firebaseAuth
                            .createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User Created!", Toast.LENGTH_SHORT).show();
                                //TODO: Remove the ";" and the comment delimiter, and create the user.
                                if (acc_type) {
                                    final FirebaseObjects.UserDBO newUser = new FirebaseObjects.UserDBO(email, firstName, lastName, password, acc_type, false);
                                    FirebaseHelper firebaseHelper = new FirebaseHelper();
                                    firebaseHelper.AddUser(newUser);

                                } else {
                                    final FirebaseObjects.UserDBO newUser = new FirebaseObjects.UserDBO(email, firstName, lastName, password, acc_type, false, Integer.parseInt(age),
                                            Integer.parseInt(weight), Integer.parseInt(height));
                                    firebaseHelper.AddUser(newUser);
                                    final FirebaseObjects.DevicesDBO newDevice = new FirebaseObjects.DevicesDBO(deviceID);
                                    firebaseHelper.addDevice(newDevice);
                                }
                                mSharedPreferencesHelper.saveEmail(email);
                                mSharedPreferencesHelper.savePassword(password);
                                mSharedPreferencesHelper.saveType(acc_type);
                                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "A user with this email already exists!", Toast.LENGTH_SHORT).show();
                                mRegisterProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEmail.setText("");
        mPassword.setText("");
        mFirstName.setText("");
        mLastName.setText("");
        mAge.setText("");
        mHeight.setText("");
        mWeight.setText("");
        mRegisterProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEmail.setText("");
        mPassword.setText("");
        mFirstName.setText("");
        mLastName.setText("");
        mAge.setText("");
        mHeight.setText("");
        mWeight.setText("");
        mRegisterProgressBar.setVisibility(View.GONE);
    }

    public boolean isValidEmail(String email){
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
