package com.example.nfacktor.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.nfacktor.Data;
import com.example.nfacktor.LoginActivity;
import com.example.nfacktor.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSlider#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSlider extends Fragment {

    ViewPager viewPager;
    TabLayout indicator;
    TextView next, prev;
    View root;
    SliderAdapter sliderAdapter;
    List<OnBoarding> list = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSlider() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSlider.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSlider newInstance(String param1, String param2) {
        FragmentSlider fragment = new FragmentSlider();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_slider, container, false);
        initView();
        return root;
    }

    private void initView() {

        sliderAdapter=new SliderAdapter(getContext(), list);
        data();

        viewPager=root.findViewById(R.id.viewPager);
        indicator=root.findViewById(R.id.indicator);
        addOnPageChangeListener();
        next=root.findViewById(R.id.tvNext);
        prev=root.findViewById(R.id.tvPrevious);
        viewPager.setAdapter(sliderAdapter);
        indicator.setupWithViewPager(viewPager, true);
        next.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() == list.size() - 1) {
                Intent i= new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
            else
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        });
        prev.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() == list.size() - 4) {

            }
            else
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        });
    }

    @SuppressLint("NewApi")
    private void data() {
        list.add(new OnBoarding(getContext().getDrawable(R.drawable.slider_1)));
        list.add(new OnBoarding(getContext().getDrawable(R.drawable.ic_slider_2)));
        list.add(new OnBoarding(getContext().getDrawable(R.drawable.slider_3)));
        list.add(new OnBoarding(getContext().getDrawable(R.drawable.slider_4)));
        sliderAdapter.notifyDataSetChanged();
    }

    private void addOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                prev.setVisibility(position == list.size() - 4 ? View.GONE: View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}