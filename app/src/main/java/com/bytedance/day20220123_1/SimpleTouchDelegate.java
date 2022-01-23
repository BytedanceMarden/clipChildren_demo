package com.bytedance.day20220123_1;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;

public class SimpleTouchDelegate extends TouchDelegate {
    /**
     * 需要扩大触摸区域的view
     */
    private final View mDelegateView;

    /**
     * 定义了这个扩大的触摸区域
     */
    private final Rect mTargetBounds;

    /**
     * 实际触摸区域
     */
    private final Rect mActualBounds;

    /**
     * 相对于mTargetBounds溢出的触摸区域:实际上就是比mTargetBounds大一点的区域(宽高分别大8),其目的是消除触摸误差.
     */
    private final Rect mSlopBounds;

    /**
     * 获取的是触摸滑动距离的判断:就是触摸滑动距离为mSlop时候裁判为是在触摸滑动move.其默认值为8
     */
    private final int mSlop;

    /**
     * 判断代理的事件是否为down事件
     */
    private boolean mDelegateTargeted;

    /**
     * 构造方法
     */
    public SimpleTouchDelegate(Rect targetBounds, View delegateView) {
        super(targetBounds, delegateView);
        mSlop = ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
        mTargetBounds = new Rect();
        mSlopBounds = new Rect();
        mActualBounds = new Rect();
        setBounds(targetBounds, null);
        mDelegateView = delegateView;
    }

    /**
     * 设置代理区域
     *
     * @param desiredBounds
     * @param actualBounds
     */
    public void setBounds(Rect desiredBounds, Rect actualBounds) {
        mTargetBounds.set(desiredBounds);
        mSlopBounds.set(desiredBounds);
        mSlopBounds.inset(-mSlop, -mSlop);
    }


    /**
     * 重写onTouchEvent方法
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        boolean sendToDelegate = false;
        boolean hit = true;
        boolean handled = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTargetBounds.contains(x, y)) {
                    mDelegateTargeted = true;
                    sendToDelegate = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                sendToDelegate = mDelegateTargeted;
                if (sendToDelegate) {
                    if (!mSlopBounds.contains(x, y)) {
                        hit = false;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                sendToDelegate = mDelegateTargeted;
                mDelegateTargeted = false;
                break;
        }
        if (sendToDelegate) {
            if (hit) {
                // 原来逻辑：如果命中，则将MotionEvent坐标移动到目标View的中心
                // 现在逻辑：如果命中，不做处理。交给父View做进一步判断处理（判断坐标位置分发给子View）
                // event.setLocation(mDelegateView.getWidth() / 2, mDelegateView.getHeight() / 2);
            } else {
                int slop = mSlop;
                event.setLocation(-(slop * 2), -(slop * 2));
            }
            handled = mDelegateView.dispatchTouchEvent(event);
        }
        return handled;
    }
}
