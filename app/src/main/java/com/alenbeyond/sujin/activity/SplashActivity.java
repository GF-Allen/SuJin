package com.alenbeyond.sujin.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import com.alenbeyond.sujin.R;
import com.alenbeyond.sujin.bean.SuJinHome;
import com.alenbeyond.sujin.rx.ApiManager;
import com.alenbeyond.sujin.rx.MyObserver;
import com.alenbeyond.sujin.sql.DataBaseHelper;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_motto1)
    TextView tvMotto1;
    @BindView(R.id.tv_motto2)
    TextView tvMotto2;
    private static final long TIME_OUT = 3000;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/PingFang_SC_Light.ttf");
        tvName.setTypeface(typeface);
        tvMotto1.setTypeface(typeface);
        tvMotto2.setTypeface(typeface);
    }

    @Override
    public void initData() {
        long startTime = SystemClock.currentThreadTimeMillis();

        loadData();

        long endTime = SystemClock.currentThreadTimeMillis();
        long d = endTime - startTime;

        if (d > TIME_OUT) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, TIME_OUT - d);
        }
    }

    @Override
    protected void loadData() {
        ApiManager.getObSujinHome(0, new MyObserver<List<SuJinHome>>() {
            @Override
            public void onNext(List<SuJinHome> suJinHomes) {
                DataBaseHelper helper = DataBaseHelper.getHelper(SplashActivity.this);
                try {
                    helper.getHomeDao().create(suJinHomes);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
