package com.alenbeyond.sujin.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by AlenBeyond on 2016/9/7.
 */
public class NoDragSwipeRefreshLayout extends SwipeRefreshLayout {
    public NoDragSwipeRefreshLayout(Context context) {
        super(context);
    }

    public NoDragSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
