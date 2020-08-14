package com.crop.companion.ui.project_details.crop_timelapse;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.crop.companion.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    File[] images;

    ImageAdapter(Context context, File[] images){
        mContext = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Picasso.get()
                .load(images[position])
                .placeholder(R.drawable.h6viz)
                .into(imageView);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}