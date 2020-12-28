package com.juhi.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.juhi.fragments.MyAppointments;
import com.juhi.fragments.Packages;
import com.juhi.fragments.Profile;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment selectedFragment=null;

        switch (position)
        {
            case 0:
                selectedFragment=new Packages();
                break;
            case 1:
                selectedFragment=new MyAppointments();
            break;

        }
        return selectedFragment;

    }

    @Override
    public int getCount() {
        return 2;
    }
}
