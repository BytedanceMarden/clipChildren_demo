package com.bytedance.day20220123_1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ClipLinearLayout extends LinearLayout {
    /**
     * 构造方法1
     *
     * @param context
     */
    public ClipLinearLayout(Context context) {
        this(context, null);
    }

    /**
     * 构造方法2
     *
     * @param context
     * @param attrs
     */
    public ClipLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法3
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ClipLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写onTouchEvent方法
     * 判断点击位置是否落在某个子View内，并将事件分发给该子View
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取坐标相对屏幕的位置
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        View child;
        //检测坐标是否落在对应的子布局内
        if ((child = checkChildTouch(rawX, rawY)) != null) {
            //若是将坐标值修改为子布局中心点
            event.setLocation(child.getWidth() / 2, child.getHeight() / 2);
            //分发事件给子布局
            return child.dispatchTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断指定坐标点落在哪个View中
     *
     * @param x
     * @param y
     * @return
     */
    private View checkChildTouch(float x, float y) {
        int outLocation[] = new int[2];
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                //获取View 在屏幕上的可见坐标
                child.getLocationOnScreen(outLocation);
                //点击坐标是否落在View 的可见区域，若是则将事件分发给它
                boolean hit = (x >= outLocation[0] && y > outLocation[1]
                        && x <= outLocation[0] + child.getWidth() && y <= outLocation[1] + child.getHeight());
                if (hit)
                    return child;
            }
        }
        return null;
    }
}
