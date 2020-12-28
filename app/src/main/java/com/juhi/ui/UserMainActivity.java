package com.juhi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.juhi.R;
import com.juhi.adapter.ViewPagerAdapter;

public class UserMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        viewPager=findViewById(R.id.UserMainActivity_viewpager);
        bottomNavigationView=findViewById(R.id.UserMainActivity_bottomNavigationView);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        auth= FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences("beautycart", Context.MODE_PRIVATE);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        sharedPreferences.edit().clear().apply();
        auth.signOut();
        startActivity(new Intent((this), LoginActivity.class));
        finish();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Menu_Packages:
                viewPager.setCurrentItem(0,true);
                break;
            case R.id.Menu_appointments:
                viewPager.setCurrentItem(1,true);
                break;
//            case R.id.Menu_profile:
//                viewPager.setCurrentItem(2,true);
//                break;
        }
        return false;
    }
}
