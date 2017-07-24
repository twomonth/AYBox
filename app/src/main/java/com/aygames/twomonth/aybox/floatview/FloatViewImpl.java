package com.aygames.twomonth.aybox.floatview;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.activity.Activity_left;

/**
 * Created by MyPC on 2017/6/13.
 */

public class FloatViewImpl {
    private static FloatViewImpl instance = null;
    private static RelativeLayout mFloatLayout;
    private static WindowManager.LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    private static WindowManager mWindowManager;
    private ImageView mFloatView;
    private boolean isShow = false;
    private LayoutInflater inflater;
    private Context mContext;
    boolean isOne = true;
    private final int MOBILE_QUERY = 1;
    private static final String TAG ="FloatView";

    // handler处理程序
    private Handler hendler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MOBILE_QUERY:
                    wmParams.x = -100;
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams); // 当10秒到达后，作相应的操作。
                    TranslateAnimation animation = new TranslateAnimation(0.0f,
                            -mFloatView.getWidth() / 2, 0.0f, 0.0f);
                    animation.setDuration(2000);
                    animation.setFillAfter(true);
                    mFloatView.startAnimation(animation);
                    break;
            }
        };
    };

    /**
     * 创建对象的时候进行初始化的操作。 获取到ApplicationContext的对象
     *
     * @param context
     */
    private FloatViewImpl(Context context) {
        init(context.getApplicationContext());
    }

    /**
     * 用同步代码快的方式进行单例模式的添加。也就是说这个兑现是线程安全的。
     *
     * @param context
     * @return
     */
    public synchronized static FloatViewImpl getInstance(Context context) {
        if (instance == null) {
            instance = new FloatViewImpl(context);
        }
        return instance;
    }
    protected void init(Context context) {
        this.mContext = context;
        createFloatView();
    }
    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        // 获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) mContext
                .getSystemService(mContext.WINDOW_SERVICE);
        Log.i("FloatView", "mWindowManager--->" + mWindowManager); // 显示对象的hash值
        // 设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        this.inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.float_layout, null);
        // 添加mFloatLayout 。布局文件和相关的显示参数。就可以进行先关的布局文件的Windows相关的显示。
        mWindowManager.addView(mFloatLayout, wmParams);
        init();
    }

    private void init() {
        // 浮动窗口按钮
        mFloatView = (ImageView) mFloatLayout.findViewById(R.id.iv_float);
        // 设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            /**
             * 进行点击悬浮窗后进行位置的移动
             */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TranslateAnimation animation = new TranslateAnimation(0.0f,
                        0.0f, 0.0f, 0.0f);
                animation.setFillAfter(true);
                mFloatView.startAnimation(animation);
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX()
                        - mFloatView.getMeasuredWidth() / 2;
                Log.i(TAG, "RawX" + event.getRawX());
                Log.i(TAG, "X" + event.getX());
                Log.i(TAG, "Width:" + mFloatView.getMeasuredWidth());
                // 减25为状态栏的高度
                wmParams.y = (int) event.getRawY()
                        - mFloatView.getMeasuredHeight() / 2;
                Log.i(TAG, "RawY" + event.getRawY());
                Log.i(TAG, "Y" + event.getY());
                // 刷新
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        resetTime();
                        break;
                }
                return false; // 此处必须返回false，否则OnClickListener获取不到监听
            }
        });
        mFloatView.setOnClickListener(onclick);
    }

    // 传送msg
    private void resetTime() {
        hendler.removeMessages(MOBILE_QUERY);
        Message msg = hendler.obtainMessage(MOBILE_QUERY);
        hendler.sendMessageDelayed(msg, 1000);
    }

    /**
     * 点击事件。点击相应的控件进行相应的显示
     */
    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == mFloatView.getId()) {
                hidFloat();
                Intent intent = new Intent(mContext, Activity_left.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                return;
            }
        }
    };

    // 隐藏悬浮窗口
    public static void hidFloat() {
        // TODO Auto-generated method stub
        // 移除悬浮窗口
        mFloatLayout.setVisibility(View.GONE);
        Log.i("悬浮窗口", "hidFloat,gone");
    }

    // 显示悬浮窗口
    public static void ShowFloat() {
        // TODO Auto-generated method stub
        // 移除悬浮窗口
        mFloatLayout.setVisibility(View.VISIBLE);
        Log.i("悬浮窗口", "showFloat,visible");
    }
}
