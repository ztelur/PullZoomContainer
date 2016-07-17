package com.carpediem.homer.pullzoomcontainer;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by homer on 16-7-17.
 */
public class PullZoomList extends ListView {
    /**
     * 实现思路:
     *
     * 1 是让ListHeader实现缩放，所以必须先获得Header的引用
     *      1.1 Header的设置时间
     *      1.2 getHeaderView,由于没有现成的方法，并且我们给headerView加一个container,所以需要重载addHeaderView.
     *          但是这个样子的话，如果是多个header就不行了，所以必须是获得headerview的那个container。对这个container进行修改。
     *          两个方案:1 getChild(1) or override addHeaderView
     * 2 需要改写onTouchEvent,更改其触摸事件处理
     *      普通的ListView,当滑动到顶端时，一定是无法再次滑动了。所以缩放是在HeaderView就在顶端时进行的。所以这个时间点我们需要注意
     *       方案１：DragHelper
     *       方案2:自己写
     * 3 如何实现动画
     *      需要注意的是：View的大小改变了。并且其内部内容缩放了。所以，View大小的改变是关键
     *      但是如果保证，view大小变化，其内部content也会变化呢？
     */

    private Context mContext;
    private View mHeader;
    private ViewDragHelper mViewDragHelper;
    private float mTouchSlop;
    private boolean mIntercept = false;
    private float mLastY;

    public PullZoomList(Context context) {
        super(context);
        init(context);
    }

    public PullZoomList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullZoomList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // do nothing
        mContext  = context;
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();

    }

    @Override
    public void addHeaderView(View v) {
        mHeader = v;
        super.addHeaderView(v);
    }

    @Override
    public void addHeaderView(View v, Object data, boolean isSelectable) {
        mHeader = v;
        super.addHeaderView(v, data, isSelectable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mIntercept) {
            Log.e("test","onTouchEvent");
            return super.onTouchEvent(ev);
        }
        //只需要记录y值的变化就行了。
        int index = MotionEventCompat.getActionIndex(ev);
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN:
                mLastY = MotionEventCompat.getY(ev,index);
                break;
            case MotionEvent.ACTION_UP:
                mLastY = -1f;
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = MotionEventCompat.getY(ev,index);
                handleTouchEvent(mLastY,currentY);
                mLastY = currentY;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            default:

        }
        return true;
    }

    private void handleTouchEvent(float lastY,float currentY) {
        Log.e("test","handleTouchEvent");
        float deltaY = currentY - lastY;
        if (mTouchSlop < Math.abs(deltaY)) {
            int height = (int) (mHeader.getHeight() + deltaY);
            ViewGroup.LayoutParams param = mHeader.getLayoutParams();
            param.height = height;
            mHeader.setLayoutParams(param);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("test","onInterceptTouchEvent");
        boolean superResult = super.onInterceptTouchEvent(ev);
        //当superResult = false,并且Header已经在最上边啦。
        mIntercept = !superResult;
        return true;
    }
}
