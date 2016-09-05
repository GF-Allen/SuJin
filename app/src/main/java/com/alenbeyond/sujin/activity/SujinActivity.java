package com.alenbeyond.sujin.activity;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.alenbeyond.sujin.R;
import com.alenbeyond.sujin.adapter.VpImageAdapter;
import com.alenbeyond.sujin.bean.SuJinDes;
import com.alenbeyond.sujin.rx.ApiManager;
import com.alenbeyond.sujin.rx.MyObserver;
import com.dd.CircularProgressButton;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SujinActivity extends BaseActivity {

    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stuff)
    TextView tvStuff;
    @BindView(R.id.vp_image)
    ViewPager vpImage;
    @BindView(R.id.btnWithText)
    CircularProgressButton btnWithText;

    private VpImageAdapter adapter;
    private MediaPlayer player;
    private String url;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_sujin);
        ButterKnife.bind(this);
        initToolBar("素锦", false);
        btnWithText.setIndeterminateProgressMode(true);
        btnWithText.setProgress(50);
        player = new MediaPlayer();
        url = getIntent().getStringExtra("url");
        tvDes.setMovementMethod(ScrollingMovementMethod.getInstance());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/PingFang_SC_Light.ttf");
        tvTitle.setTypeface(typeface);
        tvStuff.setTypeface(typeface);
        tvDes.setTypeface(typeface);
    }

    @Override
    protected void loadData() {
        ApiManager.getObSujinDes(url, new MyObserver<SuJinDes>() {

            @Override
            public void onNext(SuJinDes suJinDes) {
                String content = suJinDes.getContent();
                tvDes.setText(Html.fromHtml(content));
                tvStuff.setText(suJinDes.getStuff());
                tvTitle.setText(suJinDes.getTitle());
                try {
                    player.setDataSource(suJinDes.getMusic());
                    player.prepare();
                    player.start();
                    btnWithText.setProgress(100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                adapter = new VpImageAdapter(SujinActivity.this, suJinDes.getImages());
                vpImage.setAdapter(adapter);
            }
        });
    }

    public void startPlay(View view) {
        if (player.isPlaying()) {
            player.pause();
            btnWithText.setProgress(0);
        } else {
            player.start();
            btnWithText.setProgress(100);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
