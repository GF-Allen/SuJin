package com.alenbeyond.sujin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alenbeyond.sujin.R;
import com.alenbeyond.sujin.activity.SujinActivity;
import com.alenbeyond.sujin.bean.SuJinHome;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlenBeyond on 2016/9/4.
 */
public class VpHomeAdapter extends PagerAdapter {

    private Context context;
    private List<SuJinHome> datas = new ArrayList<>();

    public VpHomeAdapter(Context context) {
        this.context = context;
    }

    public VpHomeAdapter(Context context, List<SuJinHome> datas) {
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
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = View.inflate(context, R.layout.adpater_vp_home, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, SujinActivity.class)
                        .putExtra("url",datas.get(position).getUrl()));
            }
        });
        ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
        TextView tvDes = (TextView) view.findViewById(R.id.tv_des);
        TextView tvLetter = (TextView) view.findViewById(R.id.tv_letter);
        TextView tvView = (TextView) view.findViewById(R.id.tv_view);
        TextView tvLike = (TextView) view.findViewById(R.id.tv_like);
        tvTitle.setText(datas.get(position).getTitle());
        tvDate.setText(datas.get(position).getDate());
        tvDes.setText(datas.get(position).getDes());
        tvLetter.setText("" + datas.get(position).getLetter());
        tvView.setText("" + datas.get(position).getView());
        tvLike.setText("" + datas.get(position).getLike());
        Glide.with(context).load(datas.get(position).getImage()).into(ivImage);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setDatas(List<SuJinHome> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }
}
