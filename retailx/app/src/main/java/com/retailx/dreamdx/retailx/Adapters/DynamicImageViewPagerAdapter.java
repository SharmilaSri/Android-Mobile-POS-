package com.retailx.dreamdx.retailx.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.retailx.dreamdx.retailx.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DynamicImageViewPagerAdapter extends PagerAdapter {

    private Context context;
    private  ArrayList<String> imageUrls;

    public DynamicImageViewPagerAdapter(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Picasso.get().load(imageUrls.get(position)).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                .error(R.drawable.no_image).resize(110, 110).centerCrop()
                .into(imageView);

        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
