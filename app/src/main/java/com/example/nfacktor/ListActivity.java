package com.example.nfacktor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayList<Data> cities = initCities();

        recyclerView = (RecyclerView) findViewById(R.id.rView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new CityAdapter(cities);
        recyclerView.setAdapter(adapter);


    }

    private ArrayList<Data> initCities() {
        ArrayList<Data> list = new ArrayList<>();

        list.add(new Data("Cinque Terre", "The coastline, the five villages in Italy.", "https://bit.ly/CBImageCinque"));
        list.add(new Data("Paris", "Paris is the capital city of France", "https://bit.ly/CBImageParis"));
        list.add(new Data("Rio de Janeiro", "Rio has been one of Brazil's most popular destinations.", "https://bit.ly/CBImageRio"));
        list.add(new Data("Sydney", "Sydney is the state capital of New South Wales.", "https://bit.ly/CBImageSydney"));
        list.add(new Data("Sydney", "Sydney is the state capital of New South Wales.", "https://bit.ly/CBImageSydney"));
        list.add(new Data("Sydney", "Sydney is the state capital of New South Wales.", "https://bit.ly/CBImageSydney"));
        list.add(new Data("Sydney", "Sydney is the state capital of New South Wales.", "https://bit.ly/CBImageSydney"));

        return list;
    }
}