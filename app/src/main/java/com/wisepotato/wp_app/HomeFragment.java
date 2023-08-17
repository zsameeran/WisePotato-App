package com.wisepotato.wp_app;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {

    public static boolean isMainFrag = true;
    public static View mainView;
    public  ViewPager2 mainViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            mainView  = inflater.inflate(R.layout.fragment_home, container, false);
            mainViewPager = mainView.findViewById(R.id.Viewpager_view);
            return mainView;

    }



}