package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.flyzebra.xinyi.utils.FlyLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by FlyZebra on 2016/4/1.
 */
public class RefreshRecycleryView extends ViewGroup {
    private final int pull_Height = 200;
    private final int mTouchSlop;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private LayoutManager mLayout;
    private TextView topView;
    private TextView botView;

    private Scroller topScroller;
    private int MAINVIEW = MainView.NORMAL;
    private int PULLVIEW = PullView.NORMAL;
    private float down_y;
    private float mv_x, mv_y;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);

    public RefreshRecycleryView(Context context) {
        this(context, null);
    }

    public RefreshRecycleryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecycleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        topScroller = new Scroller(context);

        topView = new TextView(context);
        topView.setText("下拉刷新");
        topView.setTextSize(40);
        topView.setGravity(Gravity.CENTER);
        topView.setTextColor(0xFF000000);

        mRecyclerView = new RecyclerView(context, attrs);
        mRecyclerView.setVisibility(VISIBLE);

        botView = new TextView(context);
        botView.setText("上拉添加");
        botView.setGravity(Gravity.CENTER);
        botView.setTextColor(0xFF000000);

        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        super.addView(mRecyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        super.addView(botView, LayoutParams.MATCH_PARENT, pull_Height);
        super.addView(topView, LayoutParams.MATCH_PARENT, pull_Height);
    }

    public void setLayoutManager(LayoutManager layout) {
        mLayout = layout;
        mRecyclerView.setLayoutManager(layout);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mRecyclerView != null) {
            mRecyclerView.layout(l, t, r, b);
        }
        if (topView != null) {
            topView.layout(l, -pull_Height, r, 0);
        }
        if (botView != null) {
            botView.layout(l, b, r, b + pull_Height);
        }
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        listenerMianViewState();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                PULLVIEW = PullView.NORMAL;
                mv_x = ev.getX();
                mv_y = ev.getY();
                down_y = mv_x;
                break;
        }
        if (listenerBottom(ev)) {
            return true;
        }
        if (listenerTop(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean listenerTop(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //down_y==0 肯定不是本界面发起的消息，如果不是在本界面收到的DWON消息,可以处理跟其它控件的冲突
                if (down_y == 0) {
                    break;
                }
                if (Math.abs(ev.getX() - mv_x) > Math.abs(ev.getY() - mv_y)) {
                    //横向的划动
                    if (PULLVIEW == PullView.TOP) {
                        PULLVIEW = PullView.NORMAL;
                        mRecyclerView.scrollToPosition(0);
                    }
                    scrollTo(0, 0);
                }
                if (PULLVIEW == PullView.TOP) {
                    if (getScrollY() < (-pull_Height * 1.2f)) {
                        topView.setText("松手刷新");
                        //准备启动刷新任务
                    } else {
                        scrollBy(0, (int) (down_y - ev.getY()));
                        down_y = ev.getY();
                        topView.setText("下拉刷新");
                    }
                    if (getScrollY() >= 0) {
                        scrollTo(0, 0);
                        PULLVIEW = PullView.NORMAL;
                        mRecyclerView.scrollToPosition(0);
                    }
//                    FlyLog.i("1111111111111111111111111111111111111");
                    return true;
                }
                if (MAINVIEW == MainView.TOP && ev.getY() > down_y) {
                    PULLVIEW = PullView.TOP;
                    down_y = ev.getY();
                    mRecyclerView.scrollToPosition(0);
//                    FlyLog.i("222222222222222222222222222222222222222");
                    return true;
                }
                down_y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollY() != 0) {
                    mRecyclerView.scrollToPosition(0);
                    topView.setText("正在刷新");
                    //执行刷新任务

                    //刷新完成扫行以下动作
                    animScrollTo(0, getScrollY(), 0, 0, 300);
//                    scrollTo(0, 0);
                    return true;
                }
//                mRecyclerView.setEnabled(true);
                break;
        }
        return false;
    }

    private boolean listenerBottom(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //down_y==0 肯定不是本界面发起的消息，如果不是在本界面收到的DWON消息,可以处理跟其它控件的冲突
                if (down_y == 0) {
                    break;
                }
                if (Math.abs(ev.getX() - mv_x) > Math.abs(ev.getY() - mv_y)) {
                    //横向的划动
                    if (PULLVIEW == PullView.BOT) {
                        PULLVIEW = PullView.NORMAL;
                        mRecyclerView.scrollToPosition(0);
                    }
                    scrollTo(0, 0);
                }
                if (PULLVIEW == PullView.BOT) {
                    if (getScrollY() > (pull_Height * 1.2f)) {
                        topView.setText("松手刷新");
                        //准备启动刷新任务
                    } else {
                        scrollBy(0, (int) (down_y - ev.getY()));
                        down_y = ev.getY();
                        topView.setText("下拉刷新");
                    }
                    if (getScrollY() >= 0) {
                        scrollTo(0, 0);
                        PULLVIEW = PullView.NORMAL;
                        mRecyclerView.scrollToPosition(0);
                    }
                    return true;
                }
                if (MAINVIEW == MainView.BOT && ev.getY() < down_y) {
                    PULLVIEW = PullView.BOT;
                    down_y = ev.getY();
                    mRecyclerView.scrollToPosition(0);
                    return true;
                }
                down_y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollY() != 0) {
                    mRecyclerView.scrollToPosition(0);
                    topView.setText("正在刷新");
                    //执行刷新任务

                    //刷新完成扫行以下动作
                    animScrollTo(0, getScrollY(), 0, 0, 300);
//                    scrollTo(0, 0);
                    return true;
                }
                break;
        }
        return false;
    }

    private void listenerMianViewState() {
        int first = -1;
        int last = -1;
        View firstView = null;
        View lastView = null;
        Method findFirst;
        Method findLast;
        Method findView;
        try {
            Class cls = mLayout.getClass();
            findFirst = cls.getDeclaredMethod("findFirstVisibleItemPosition");
            findLast = cls.getDeclaredMethod("findLastVisibleItemPosition");
            findView = cls.getDeclaredMethod("findViewByPosition", int.class);
            first = (int) findFirst.invoke(mLayout);
            last = (int) findLast.invoke(mLayout);
            if (first == 0 && findView.invoke(mLayout, first) != null && ((View) findView.invoke(mLayout, first)).getTop() == 0) {
//                FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->滚动到了顶部" + ((View) findView.invoke(mLayout, first)).getTop());
                MAINVIEW = MainView.TOP;
            } else if (last == mLayout.getItemCount() - 1 && findView.invoke(mLayout, last) != null && ((View) findView.invoke(mLayout, last)).getBottom() == getHeight()) {
                FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->滚动到了底部");
                MAINVIEW = MainView.BOT;
            } else if (last > mLayout.getItemCount() - 2) {
//                FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->滚动到最后一行");
                MAINVIEW = MainView.LASTITEM;
            } else {
//                FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->没有状态发生");
                MAINVIEW = MainView.NORMAL;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void animScrollTo(final int sx, final int sy, final int dx, final int dy, final int times) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                int num = times / 10;
                int my = (dy - sy) / num;
                int mx = (dx - sx) / num;
                for (int i = 1; i < num; i++) {
//                    FlyLog.i("<RefreshRecycleryView>animScrollTo");
                    scrollBy(mx, my);
                    postInvalidate();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                scrollTo(dx, dy);
                PULLVIEW = PullView.NORMAL;
                postInvalidate();
            }
        });
    }

    public static class MainView {
        public static int TOP = 1;
        public static int BOT = 2;
        public static int LASTITEM = 3;
        public static int NORMAL = 4;
    }

    public static class PullView {
        public static int TOP = 1;
        public static int BOT = 2;
        public static int NORMAL = 3;

    }
}
