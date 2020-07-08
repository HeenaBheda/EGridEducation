package com.cloudentro.taegrid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashBoard_Fragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_navigation_dashboard,container,false);
        initView();
      //  loadFragment(new Live_Class_Fragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.liveclass:
                        loadFragment(new Live_Class_Fragment());
                        return true;
                    case R.id.createclass:
                        loadFragment(new Creat_New_Class_Fragment());
                        return true;
                }
                return false;
            }
        });

        return view;
    }

    private void initView() {
        bottomNavigationView=view.findViewById(R.id.navigationBottom);
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

}
