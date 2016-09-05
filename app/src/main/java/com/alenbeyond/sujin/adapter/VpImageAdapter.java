package com.alenbeyond.sujin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alenbeyond.sujin.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by AlenBeyond on 2016/9/5.
 */
public class VpImageAdapter extends PagerAdapter {

    private Context context;
    private List<String> datas;

    public VpImageAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(R.color.black);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(datas.get(position)).asBitmap().into(imageView);
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,PhotoDetailActivity.class)
                        .putExtra("image_url",datas.get(position)));
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
