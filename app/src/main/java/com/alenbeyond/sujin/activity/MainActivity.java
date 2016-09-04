package com.alenbeyond.sujin.activity;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;

import com.alenbeyond.sujin.R;
import com.alenbeyond.sujin.adapter.VpHomeAdapter;
import com.alenbeyond.sujin.bean.SuJinHome;
import com.alenbeyond.sujin.rx.ApiManager;
import com.alenbeyond.sujin.rx.MyObserver;
import com.alenbeyond.sujin.sql.DataBaseHelper;
import com.orhanobut.logger.Logger;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout mSrlFresh;
    @BindView(R.id.vp_home)
    ViewPager mVpHome;
    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;
    private VpHomeAdapter adapter;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setStatusTranslucent();
        showContent(false);
        adapter = new VpHomeAdapter(this);
        mVpHome.setAdapter(adapter);
        mSrlFresh.setColorSchemeResources(R.color.red, R.color.black);
        mSrlFresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataByOnline(0);
            }
        });
        new Thread() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSrlFresh.setRefreshing(true);
                    }
                });
            }
        }.start();
    }

    @Override
    protected void loadData() {
        try {
            List<SuJinHome> datas = DataBaseHelper.getHelper(this).getHomeDao().queryForAll();
            if (datas == null || datas.size() == 0) {
                loadDataByOnline(0);
            } else {
                setData(datas);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            loadDataByOnline(0);
        }
    }

    private void loadDataByOnline(int pager) {
        ApiManager.getObSujinHome(pager, new MyObserver<List<SuJinHome>>() {
            @Override
            public void onNext(List<SuJinHome> suJinHomes) {
                setData(suJinHomes);
            }
        });
    }

    private void setData(List<SuJinHome> datas) {
        Logger.d(datas);
        adapter.setDatas(datas);
        showContent(true);
    }

    private void showContent(boolean isShow) {
        mPbLoading.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);
        mVpHome.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }
}
