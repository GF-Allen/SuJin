package com.alenbeyond.sujin.activity;

import android.graphics.Typeface;
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
import com.alenbeyond.sujin.service.PlayerService;
import com.dd.CircularProgressButton;

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
    private String url;
    private String musicUrl;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_sujin);
        ButterKnife.bind(this);
        initToolBar("素锦", false);
        btnWithText.setIndeterminateProgressMode(true);
        btnWithText.setProgress(50);
        url = getIntent().getStringExtra("url");
        tvDes.setMovementMethod(ScrollingMovementMethod.getInstance());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/PingFang_SC_Regular.ttf");
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
                musicUrl = suJinDes.getMusic();
                PlayerService.play(SujinActivity.this, musicUrl);
                btnWithText.setProgress(100);
                adapter = new VpImageAdapter(SujinActivity.this, suJinDes.getImages());
                vpImage.setAdapter(adapter);
            }
        });
    }

    public void startPlay(View view) {
        if (PlayerService.isPlaying()) {
            PlayerService.stop(SujinActivity.this);
            btnWithText.setProgress(0);
        } else {
            PlayerService.play(SujinActivity.this, musicUrl);
            btnWithText.setProgress(100);
        }
    }
}
