package com.juhi.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.juhi.R;
import com.juhi.adapter.ImageSliderAdapter;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class OnBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AboutScreen";
    ViewPager viewPager;
    ImageSliderAdapter imageSliderAdapter;
    WormDotsIndicator wormDotsIndicator;
    Button next,skip;
    int currentPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        viewPager=findViewById(R.id.about_viewpager);
        wormDotsIndicator=findViewById(R.id.indicator);
        next=findViewById(R.id.about_next);
        skip=findViewById(R.id.about_skip);
        next.setOnClickListener(this);
        skip.setOnClickListener(this);
        imageSliderAdapter=new ImageSliderAdapter(this);
        viewPager.setAdapter(imageSliderAdapter);
        wormDotsIndicator.setViewPager(viewPager);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.about_next:

                currentPosition++;
                if(currentPosition==2)
                {


                    startActivity(new Intent(this,LoginActivity.class));
                    finish();

                }
                else if (currentPosition==1)
                {
                    next.setText("Start");
                    viewPager.setCurrentItem(currentPosition);
                }

                else {
                    viewPager.setCurrentItem(currentPosition);
                }

                Log.d(TAG, "viewpager "+viewPager.getCurrentItem());
                Log.d(TAG, "currentposition "+currentPosition);

                break;
            case R.id.about_skip:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }
}
