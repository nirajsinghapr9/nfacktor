package com.example.nfacktor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nfacktor.fragments.FragmentSlider;
import com.example.nfacktor.fragments.OnBoarding;

import java.util.List;

public class SliderActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        getSupportFragmentManager().beginTransaction().add(R.id.layout,new FragmentSlider()).commit();
    }
}