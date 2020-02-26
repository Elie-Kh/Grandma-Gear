package com.example.grandmagear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LogInActivity extends AppCompatActivity {

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
}
