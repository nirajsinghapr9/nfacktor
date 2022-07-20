package com.example.nfacktor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    ImageView next;
    EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        number=findViewById(R.id.etMobile);
        Intent intent = getIntent();
        String str = intent.getStringExtra("number");
        number.setText(str);
        next=findViewById(R.id.next);
        next.setOnClickListener(view -> {
            if(number.getText().toString()=="" || number.getText().toString().length()<4){
                number.requestFocus();
                number.setError("Please enter valid number");
            }else{
                Intent i=new Intent(LoginActivity.this, OtpActivity.class);
                i.putExtra("number",number.getText().toString());
                startActivity(i);
                finish();
            }
        });
    }
}