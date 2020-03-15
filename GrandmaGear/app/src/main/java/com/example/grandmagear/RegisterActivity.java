package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "_RegisterActivity";

    protected EditText mName, mEmail, mPassword, mAge, mWeight, mHeight;
    protected Button mRegisterButton;
    protected Spinner mRegisterSpinner;
    protected ProgressBar mRegisterProgressBar;
    protected FirebaseAuth firebaseAuth;
    protected FirebaseFirestore firebaseFirestore;
    protected String userID;
    protected boolean acc_type;

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
        mAge = findViewById(R.id.age);
        mWeight = findViewById(R.id.weight);
        mHeight = findViewById(R.id.height);
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
                    acc_type = false;
                }else{
                    mAge.setVisibility(View.VISIBLE);
                    mHeight.setVisibility(View.VISIBLE);
                    mWeight.setVisibility(View.VISIBLE);
                    acc_type = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                final String password = mPassword.getText().toString().trim();
                final String name = mName.getText().toString();
                final String age = mAge.getText().toString();
                final String weight = mWeight.getText().toString();
                final String height = mHeight.getText().toString();

                if(TextUtils.isEmpty(name)){
                    mName.setError("Name is required");
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    return;
                }

                if(!isValidEmail(email)){
                    mEmail.setError("Email is not valid");
                }

                if(TextUtils.isEmpty(age)){
                    mAge.setError("Age is required");
                }

                if(Integer.parseInt(age) < 12){
                    mAge.setError("You are to young to use this app");
                }

                if(TextUtils.isEmpty(weight)){
                    mWeight.setError("Weight is required");
                }

                if(TextUtils.isEmpty(height)){
                    mHeight.setError("Height is required");
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
                            //TODO: Remove the ";" and the comment delimiter, and create the user.
                            if(acc_type) {
                                FirebaseObjects.UserDBO newUser = new FirebaseObjects.UserDBO(email, name, password, acc_type);
                                FirebaseHelper firebaseHelper = new FirebaseHelper();
                                firebaseHelper.AddUser(newUser);
                            }else{
                                FirebaseObjects.UserDBO newUser = new FirebaseObjects.UserDBO(email, name, password, acc_type, Integer.parseInt(age),
                                        Integer.parseInt(weight), Integer.parseInt(height));
                                FirebaseHelper firebaseHelper = new FirebaseHelper();
                                firebaseHelper.AddUser(newUser);
                            }
                            //TODO: Delete comments below and replace with "FirebaseHelper.AddUser(newUser);"
//
//                            userID = firebaseAuth.getCurrentUser().getUid();
//                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
//                            Map<String,Object> user = new HashMap<>();
//                            user.put("Name", name);
//                            user.put("Email", email);
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
//                                }
//                            });
//                            documentReference.set(user).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d(TAG, "onFailure: " + e.toString());
//                                }
//                            });
                            //TODO: Include deleting this line with all above comments
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

    public boolean isValidEmail(String email){
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
