package com.juhi.adapter;


import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.juhi.R;

public class ImageSliderAdapter extends PagerAdapter {
    LayoutInflater inflater;
    int[] imgId = {R.drawable.logo, R.drawable.logo};
    String[] imgContent = {"Shine among crowd", "Only for women"};

    public ImageSliderAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.image_slider_container, container, false);

        final ImageView imageView = imageLayout.findViewById(R.id.image_slider_img);
        TextView textView = imageLayout.findViewById(R.id.image_slider_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(imgContent[position]);
        imageView.setImageResource(imgId[position]);

        container.addView(imageLayout, 0);


        return imageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
