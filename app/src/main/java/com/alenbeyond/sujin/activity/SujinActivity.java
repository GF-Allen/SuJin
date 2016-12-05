package com.alenbeyond.sujin.activity;

import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alenbeyond.sujin.R;
import com.alenbeyond.sujin.adapter.VpImageAdapter;
import com.alenbeyond.sujin.bean.SuJinDes;
import com.alenbeyond.sujin.rx.ApiManager;
import com.alenbeyond.sujin.rx.MyObserver;
import com.alenbeyond.sujin.service.PlayerService;
import com.alenbeyond.sujin.utils.NetUtils;
import com.alenbeyond.sujin.utils.UiUtils;
import com.dd.CircularProgressButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

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
    @BindView(R.id.view_point)
    View point;
    @BindView(R.id.ll_points)
    LinearLayout llPoints;

    private VpImageAdapter adapter;
    private String url;
    private String musicUrl;

    private int mInterval;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_sujin);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        initToolBar(title, true);
        mInterval = UiUtils.dip2px(this, 5);
        btnWithText.setIndeterminateProgressMode(true);
        btnWithText.setProgress(50);
        url = getIntent().getStringExtra("url");
        tvDes.setMovementMethod(ScrollingMovementMethod.getInstance());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/PingFang_SC_Regular.ttf");
        tvTitle.setTypeface(typeface);
        tvStuff.setTypeface(typeface);
        tvDes.setTypeface(typeface);


        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) point.getLayoutParams();
        vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                params.leftMargin = (int) (mInterval + position * mInterval * 3 + mInterval * 3 * positionOffset);
                point.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                if (NetUtils.isWifi(SujinActivity.this)) {
                    PlayerService.play(SujinActivity.this, musicUrl);
                    btnWithText.setProgress(100);
                } else {
                    btnWithText.setProgress(0);
                }
                adapter = new VpImageAdapter(SujinActivity.this, suJinDes.getImages());
                vpImage.setAdapter(adapter);
                for (int i = 0; i < suJinDes.getImages().size(); i++) {
                    View view = new View(SujinActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mInterval, mInterval);
                    params.leftMargin = mInterval;
                    params.rightMargin = mInterval;
                    view.setLayoutParams(params);
                    view.setBackgroundResource(R.drawable.shape_point);
                    llPoints.addView(view);
                }
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
