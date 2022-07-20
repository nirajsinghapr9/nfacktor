package com.example.nfacktor.fragments;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class OnBoarding implements Serializable {
    private Drawable image;

    public OnBoarding(Drawable image) {
        this.image = image;
    }

    public Drawable getImage() {
        return image;
    }
}
