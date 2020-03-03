package com.example.grandmagear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PatientActivity extends AppCompatActivity {

    protected Button mHelpButton;
    protected Button mOKButton;
    protected EditText mDeviceID;
    protected ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        initializePage();
    }

    protected void initializePage(){
        mHelpButton = findViewById(R.id.help_button);
        mOKButton = findViewById(R.id.ok_button);
        mDeviceID = findViewById(R.id.device_id_edit_text);
        mLogo = findViewById(R.id.app_logo);

        mLogo.setImageResource(R.drawable.sooken);

        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpFragment helpFragment = new HelpFragment();
                helpFragment.show(getSupportFragmentManager(),"HelpFragment");
            }
        });

        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceID = mDeviceID.getText().toString();
                if(deviceID.trim().length() < 5){
                    Toast.makeText(PatientActivity.this, "Invalid ID",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    //Check if device ID is in database. If not, create it.
                }
            }
        });


    }
}
