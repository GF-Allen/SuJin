package com.alenbeyond.sujin.activity;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alenbeyond.sujin.R;
import com.alenbeyond.sujin.adapter.VpHomeAdapter;
import com.alenbeyond.sujin.bean.SuJinHome;
import com.alenbeyond.sujin.rx.ApiManager;
import com.alenbeyond.sujin.rx.MyObserver;
import com.alenbeyond.sujin.service.PlayerService;
import com.alenbeyond.sujin.sql.DataBaseHelper;
import com.j256.ormlite.dao.Dao;
import com.orhanobut.logger.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
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
    private List<SuJinHome> datas;

    private int page = 1;
    private Dao<SuJinHome, Integer> homeDao;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setStatusTranslucent();
        try {
            homeDao = DataBaseHelper.getHelper(this).getHomeDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        datas = new ArrayList<>();
        adapter = new VpHomeAdapter(this);
        mVpHome.setAdapter(adapter);
        showContent(false);
//        mSrlFresh.setColorSchemeResources(R.color.red, R.color.black);
//        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadDataByOnline(page);
//            }
//        };
//        mSrlFresh.setOnRefreshListener(onRefreshListener);
//        mSrlFresh.post(new Runnable() {
//            @Override
//            public void run() {
//                mSrlFresh.setRefreshing(true);
//            }
//        });

        mVpHome.setOnTouchListener(new View.OnTouchListener() {

            int startX;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (datas.size() > 1 && mVpHome.getCurrentItem() == datas.size() - 1 && !isLoading) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = (int) motionEvent.getX();
                            Logger.d("StartX" + startX);
                            break;
                        case MotionEvent.ACTION_UP:
                            int endX = (int) motionEvent.getX();
                            int d = endX - startX;
                            Logger.d(startX + ":" + endX + ":" + d);
                            if (Math.abs(d) > 10 && d < 0) {
                                page++;
                                loadDataByOnline(page);
                            }
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void loadData() {
//        try {
//            List<SuJinHome> dbDatas = homeDao.queryForAll();
//            if (dbDatas.size() == 0) {
//                loadDataByOnline(page);
//            } else {
//                setData(dbDatas);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        loadDataByOnline(page);

    }

    private boolean isLoading = false;

    private void loadDataByOnline(int page) {
        showContent(false);
        isLoading = true;
        ApiManager.getObSujinHome(page, new MyObserver<List<SuJinHome>>() {
            @Override
            public void onNext(List<SuJinHome> suJinHomes) {
                try {
                    setData(suJinHomes);
                    homeDao.create(suJinHomes);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setData(List<SuJinHome> data) {

        if (data == null || data.size() == 0) {
            if (page > 1) {
                page--;
            }
            Snackbar.make(mVpHome, "加载失败", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSrlFresh.setRefreshing(true);
                    loadDataByOnline(page);
                }
            }).setActionTextColor(getResources().getColor(R.color.colorAccent)).show();
        } else {
            datas.addAll(data);
            adapter.setDatas(datas);
        }
//        mSrlFresh.setRefreshing(false);
        showContent(true);
        isLoading = false;
    }

    private void showContent(boolean isShow) {
        mPbLoading.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);
//        mVpHome.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (PlayerService.isPlaying()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("是否进入后台播放");
                builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataBaseHelper.getHelper(this).close();
        homeDao = null;
    }
}
