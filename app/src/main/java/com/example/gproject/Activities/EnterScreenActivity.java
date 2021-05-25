package com.example.gproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.gproject.Adapters.LoginAdapter;
import com.example.gproject.Fragments.LoginTabFragment;
import com.example.gproject.Fragments.RegisterTabFragment;
import com.example.gproject.R;
import com.example.gproject.Utils.SharedPrefs;
import com.google.android.material.tabs.TabLayout;

public class EnterScreenActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    //for she shaghleeh
    private String LOGGED_IN_KEY = "first_ime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_screen);

        // check if the user is already logged in
        int userId = SharedPrefs.getInt(this, LOGGED_IN_KEY, -1);
        if(userId != -1){
            Intent intent = new Intent(this, MainActivity.class);
            // todo put the user id aas extr data for he main activity
            startActivity(intent);
            finish();
        }
        tabLayout = findViewById(R.id.login_tab_layout);
        viewPager = findViewById(R.id.login_view_pager);

        LoginTabFragment loginTabFragment = new LoginTabFragment();
        RegisterTabFragment registerTabFragment = new RegisterTabFragment();



        final LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), 0);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        loginAdapter.addFragment(loginTabFragment, "Log In");
        loginAdapter.addFragment(registerTabFragment, "Register");
        viewPager.setAdapter(loginAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
}