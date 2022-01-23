package com.bytedance.day20220123_1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    /**
     * button1位置标志
     */
    private boolean flag1 = false;

    /**
     * button2位置标志
     */
    private boolean flag2 = false;

    /**
     * button3位置标志
     */
    private boolean flag3 = false;

    /**
     * button1
     */
    private Button button1;

    /**
     * button2
     */
    private Button button2;

    /**
     * button3
     */
    private Button button3;

    /**
     * ClipLinearLayout对象
     */
    private ClipLinearLayout clipLinearLayout;

    /**
     * 沿Y轴平移距离
     */
    private int translationY = -100;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化View
        initView();
        // 初始化Action
        initAction();
    }

    /**
     * 初始化View
     */
    private void initView() {
        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);
        clipLinearLayout = findViewById(R.id.cll);
        clipLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Rect hitRect = new Rect();
                // 获取父布局当前有效可点击区域
                clipLinearLayout.getHitRect(hitRect);
                // 扩大父布局点击区域
                hitRect.top += translationY;
                // 创建代理对象
                TouchDelegate touchDelegate = new SimpleTouchDelegate(hitRect, clipLinearLayout);
                clipLinearLayout.setClickable(true);
                // 获取爷爷布局对象
                ViewParent viewParent = clipLinearLayout.getParent();
                if (viewParent instanceof ViewGroup) {
                    ((ViewGroup) viewParent).setClickable(true);
                    // 在爷爷布局里拦截事件分发
                    // 设置父布局的代理
                    ((ViewGroup) viewParent).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**
     * 初始化Action
     */
    private void initAction() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag1 == false) {
                    button1.setTranslationY(translationY);
                    flag1 = true;
                } else {
                    button1.setTranslationY(0);
                    flag1 = false;
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag2 == false) {
                    button2.setTranslationY(translationY);
                    flag2 = true;
                } else {
                    button2.setTranslationY(0);
                    flag2 = false;
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag3 == false) {
                    button3.setTranslationY(translationY);
                    flag3 = true;
                } else {
                    button3.setTranslationY(0);
                    flag3 = false;
                }
            }
        });
    }
}