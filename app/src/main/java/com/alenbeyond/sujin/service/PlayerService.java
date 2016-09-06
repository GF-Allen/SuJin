package com.alenbeyond.sujin.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.alenbeyond.sujin.constants.MyAction;

import java.io.IOException;

public class PlayerService extends Service {


    private static MediaPlayer mPlayer;

    private String currentUrl = "";

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
        mPlayer.setLooping(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        switch (action) {
            case MyAction.ACTION_PLAYER_START:
                String url = intent.getStringExtra("url");
                if (currentUrl.equals(url)) {
                    mPlayer.start();
                } else {
                    try {
                        mPlayer.reset();
                        mPlayer.setDataSource(url);
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                currentUrl = url;
                break;
            case MyAction.ACTION_PLAYER_PAUSE:
                mPlayer.pause();
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * 播放音乐
     *
     * @param context
     * @param url
     */
    public static void play(Context context, String url) {
        Intent intent = new Intent(MyAction.ACTION_PLAYER_START);
        intent.setClass(context, PlayerService.class);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    /**
     * 暂停播放
     *
     * @param context
     */
    public static void stop(Context context) {
        Intent intent = new Intent(MyAction.ACTION_PLAYER_PAUSE);
        intent.setClass(context, PlayerService.class);
        context.startService(intent);
    }

    /**
     * 获取播放状态
     *
     * @return
     */
    public static boolean isPlaying() {
        if (mPlayer == null) {
            return false;
        }
        return mPlayer.isPlaying();
    }
}
