package com.alenbeyond.sujin.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import com.alenbeyond.sujin.R;
import com.alenbeyond.sujin.activity.BaseActivity;
import com.alenbeyond.sujin.utils.DownloadImageService;
import com.alenbeyond.sujin.utils.FileUtils;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoDetailActivity extends BaseActivity {

    @BindView(R.id.iv_image)
    ImageView ivImage;

    private String image_url;

    @Override
    protected void getIntentData() {
        image_url = getIntent().getStringExtra("image_url");
    }

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_photo_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void loadData() {
        Glide.with(this).load(image_url).asBitmap().into(ivImage);
    }

    @OnClick({R.id.iv_save, R.id.iv_cancel})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_save:
                String name = image_url.split("wallhaven-")[1];
                final String target = FileUtils.getPubAlbumPath() + name;
                File file = new File(target);
                if (file.exists()) {
                    Snackbar.make(ivImage, "文件已存在", Snackbar.LENGTH_LONG).show();
                    return;
                }
                DownloadImageService service = new DownloadImageService(this, image_url, new DownloadImageService.ImageDownLoadCallBack() {
                    @Override
                    public void onDownLoadSuccess(File file) {
                        String old = file.getAbsolutePath();
                        FileUtils.copyFile(old, target);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(target))));//通知相册更新
                        Snackbar.make(ivImage, "保存成功", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownLoadFailed() {
                        Snackbar.make(ivImage, "保存失败", Snackbar.LENGTH_LONG).show();
                    }
                });
                new Thread(service).start();
                break;
            case R.id.iv_cancel:
                finish();
                break;
        }
    }
}
