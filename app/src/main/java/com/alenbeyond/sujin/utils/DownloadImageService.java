package com.alenbeyond.sujin.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by AlenBeyond on 2016/9/5.
 */
public class DownloadImageService implements Runnable  {

    private String url;
    private Context context;
    private ImageDownLoadCallBack callBack;

    public DownloadImageService(Context context, String url, ImageDownLoadCallBack callBack) {
        this.url = url;
        this.callBack = callBack;
        this.context = context;
    }

    @Override
    public void run() {
        File file = null;
        try {
            file = Glide.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                callBack.onDownLoadSuccess(file);
            } else {
                callBack.onDownLoadFailed();
            }
        }
    }

    public interface ImageDownLoadCallBack {

        void onDownLoadSuccess(File file);

        void onDownLoadFailed();
    }
}
