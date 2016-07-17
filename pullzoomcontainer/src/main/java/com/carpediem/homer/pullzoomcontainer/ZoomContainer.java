package com.carpediem.homer.pullzoomcontainer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by homer on 16-7-17.
 */
public class ZoomContainer extends FrameLayout {
    private float mScalePercent = 0.8f;

    public ZoomContainer(Context context,View child) {
        super(context);
        init(context);
        addView(child);
    }

    private void init(Context context) {
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //在这里进行缩放

        super.onDraw(canvas);

    }

}
