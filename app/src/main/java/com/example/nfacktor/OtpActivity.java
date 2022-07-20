package com.example.nfacktor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.poovam.pinedittextfield.LinePinField;

public class OtpActivity extends AppCompatActivity {
    Button verify;
    LinePinField linePinField;
    TextView number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp);
        number=findViewById(R.id.number);
        linePinField=findViewById(R.id.etOtp);
        verify = findViewById(R.id.verify);
        Intent intent = getIntent();
        String str = intent.getStringExtra("number");


        Spannable wordtoSpan = new SpannableString("OTP sent to "+ str);

        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 13+str.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        number.setText(wordtoSpan);

        verify.setOnClickListener(view -> {
            if(linePinField.getText().toString().length()<4){
                linePinField.requestFocus();
               linePinField.setError("Enter valid otp");
            }else {
                Intent i = new Intent(this, MainActivity2.class);
                startActivity(i);
                finish();
            }
        });

        number.setOnClickListener(view -> {
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra("number", str);
            startActivity(i);
            finish();
        });
    }
}