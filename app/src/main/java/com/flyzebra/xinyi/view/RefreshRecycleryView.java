package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.TextView;

import com.flyzebra.xinyi.utils.FlyLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by FlyZebra on 2016/4/1.
 */
public class RefreshRecycleryView extends ViewGroup {
    private final int pull_Height = 200;
    private final int animScoller_time = 500;
    private final int mTouchSlop;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private LayoutManager mLayout;
    private TextView topView;
    private TextView botView;

    private int MAINVIEW = MainView.VIEW_NORMAL;
    private int PULLVIEW = PullView.PULL_NOMARL;
    private float down_y;
    private float mv_x, mv_y;
    private boolean TOP_MODE = true;
    private boolean BOT_MODE = false;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);
    private boolean isAttach;

    private AtomicBoolean isNeedRefresh = new AtomicBoolean(false);
    private ListenerTopRefresh listenerTopRefresh;
    private ListenerBottomRefresh listenerBottomRefresh;
    private ListenerLastItem listenerLastItem;

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

        topView = new TextView(context);
        topView.setText("下拉刷新...");
        topView.setTextSize(20);
        topView.setGravity(Gravity.CENTER);
        topView.setTextColor(0xFF000000);

        mRecyclerView = new RecyclerView(context, attrs);
        mRecyclerView.setVisibility(VISIBLE);
        mRecyclerView.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                listenerMianViewState();
            }
        });

        botView = new TextView(context);
        botView.setText("上拉添加...");
        botView.setTextSize(20);
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

    public void setRefrshTop(boolean flag) {
        TOP_MODE = flag;
    }

    public void setRefrshBottom(boolean flag) {
        BOT_MODE = flag;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
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

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (TOP_MODE || BOT_MODE) {
            if (listenerPull(ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean listenerPull(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                PULLVIEW = PullView.PULL_NOMARL;
                mv_x = ev.getX();
                mv_y = ev.getY();
                down_y = mv_x;
                break;
            case MotionEvent.ACTION_MOVE:
                //down_y==0 肯定不是本界面发起的消息，如果不是在本界面收到的DWON消息,可以处理跟其它控件的冲突
                if (down_y == 0) {
                    break;
                }
                ////横向的划动冲突解决
                if (Math.abs(ev.getX() - mv_x) > Math.abs(ev.getY() - mv_y) && (TOP_MODE || BOT_MODE)) {
                    if (PULLVIEW == PullView.PULL_TOP) {
                        PULLVIEW = PullView.PULL_NOMARL;
                        mRecyclerView.scrollToPosition(0);
                    }
                    if (PULLVIEW == PullView.PULL_BOTTOM) {
                        PULLVIEW = PullView.PULL_NOMARL;
                        mRecyclerView.scrollToPosition(mLayout.getItemCount() - 1);
                    }
                    scrollTo(0, 0);
                }

                //TOP弹出处理
                if (PULLVIEW == PullView.PULL_TOP && TOP_MODE) {
                    if (getScrollY() < (-pull_Height)) {
                        topView.setText("放开以刷新...");
                        isNeedRefresh.set(true);
                        //准备启动动画
                    } else {
                        isNeedRefresh.set(false);
                        topView.setText("下拉刷新...");
                        //准备变更动画
                    }
                    scrollBy(0, (int) (down_y - ev.getY()));
                    down_y = ev.getY();
                    if (getScrollY() >= 0) {
                        scrollTo(0, 0);
                        PULLVIEW = PullView.PULL_NOMARL;
                        mRecyclerView.scrollToPosition(0);
                    }
                    return true;
                }
                if ((MAINVIEW == MainView.VIEW_TOP || mLayout.getItemCount() == 0) && (ev.getY() > down_y) && TOP_MODE) {
                    isNeedRefresh.set(false);
                    PULLVIEW = PullView.PULL_TOP;
                    down_y = ev.getY();
                    mRecyclerView.scrollToPosition(0);
                    return true;
                }
                //BOT弹出处理
                if (PULLVIEW == PullView.PULL_BOTTOM && BOT_MODE) {
                    if (getScrollY() > (pull_Height)) {
                        botView.setText("放开以加载...");
                        isNeedRefresh.set(true);
                        //准备启动刷新任务
                    } else {
                        botView.setText("上拉加截...");
                        isNeedRefresh.set(false);
                    }
                    scrollBy(0, (int) (down_y - ev.getY()));
                    down_y = ev.getY();
                    if (getScrollY() <= 0) {
                        scrollTo(0, 0);
                        PULLVIEW = PullView.PULL_NOMARL;
                        mRecyclerView.scrollToPosition(mLayout.getItemCount() - 1);
                    }
                    return true;
                }
                if (MAINVIEW == MainView.VIEW_BOTTOM && (ev.getY() < down_y) && BOT_MODE) {
                    isNeedRefresh.set(false);
                    PULLVIEW = PullView.PULL_BOTTOM;
                    down_y = ev.getY();
                    mRecyclerView.scrollToPosition(mLayout.getItemCount() - 1);
                    return true;
                }
                down_y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollY() != 0) {
                    if (PULLVIEW == PullView.PULL_TOP && TOP_MODE) {
                        mRecyclerView.scrollToPosition(0);
                        topView.setText("正在刷新...");
                        //执行刷新任务
                        if (listenerTopRefresh != null && isNeedRefresh.get()) {
                            listenerTopRefresh.onRefrsh(topView);
                        }
                    }
                    if (PULLVIEW == PullView.PULL_BOTTOM && BOT_MODE) {
                        mRecyclerView.scrollToPosition(mLayout.getItemCount() - 1);
                        botView.setText("正在加载...");
                        //执行刷新任务
                        if (listenerBottomRefresh != null && isNeedRefresh.get()) {
                            listenerBottomRefresh.onRefrsh(topView);
                        }
                    }
                    //刷新完成执行以下动作
                    animScrollTo(getScrollX(), getScrollY(), 0, 0, animScoller_time);
                    return true;
                }
                break;
        }
        return false;
    }

    private void listenerMianViewState() {
        GridLayoutManager layoutManager;
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
//                FlyLog.i("<RefreshRecyclerView>-->listenerMianViewState->滚动到了顶部" + ((View) findView.invoke(mLayout, first)).getTop());
                MAINVIEW = MainView.VIEW_TOP;
            } else if (last == mLayout.getItemCount() - 1 && findView.invoke(mLayout, last) != null && ((View) findView.invoke(mLayout, last)).getBottom() == getHeight()) {
//                FlyLog.i("<RefreshRecyclerView>-->listenerMianViewState->滚动到了底部");
                MAINVIEW = MainView.VIEW_BOTTOM;
            } else if (last > mLayout.getItemCount() - 2) {
                if (MAINVIEW != MainView.VIEW_LAST) {
                    if (listenerLastItem != null) {
                        listenerLastItem.onLastItem();
                    }
//                    FlyLog.i("<RefreshRecyclerView>-->listenerMianViewState->滚动到最后一行");
                }
                MAINVIEW = MainView.VIEW_LAST;
            } else {
//                FlyLog.i("<RefreshRecyclerView>-->listenerMianViewState->没有状态发生");
                MAINVIEW = MainView.VIEW_NORMAL;
            }
        } catch (NoSuchMethodException e) {
            FlyLog.i("<RefreshRecyclerView>-->listenerMianViewState->NoSuchMethodException");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            FlyLog.i("<RefreshRecyclerView>-->listenerMianViewState->InvocationTargetException");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            FlyLog.i("<RefreshRecyclerView>-->listenerMianViewState->IllegalAccessException");
            e.printStackTrace();
        }

    }

    private void animScrollTo(final int sx, final int sy, final int dx, final int dy, final int times) {
        final int height = pull_Height;
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                int num = times / 40;
                int mx = (dx - sx) / num;
                int my = (dy - sy) / num;
                for (; ; ) {
                    //当弹出界面等于自身身高时停止滚动并等待更新任务完成
                    while ((Math.abs(getScrollY()) < height) && isNeedRefresh.get() && isAttach) {
                        try {
                            FlyLog.i("<RefreshRecycleryView>animScrollTo-->while");
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (Math.abs(getScrollY()) <= Math.abs(my)) {
                        break;
                    }
                    scrollBy(mx, my);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                scrollTo(0, 0);
                PULLVIEW = PullView.PULL_NOMARL;
                postInvalidate();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        isAttach = true;
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        isNeedRefresh.set(false);
        isAttach = false;
        super.onDetachedFromWindow();
    }

    public void refreshFinish() {
        isNeedRefresh.set(false);
    }

    public void setListenerLastItem(ListenerLastItem listenerLastItem) {
        this.listenerLastItem = listenerLastItem;
    }

    public void setListenerBottomRefresh(ListenerBottomRefresh listenerBottomRefresh) {
        this.listenerBottomRefresh = listenerBottomRefresh;
    }

    public void setListenerTopRefresh(ListenerTopRefresh listenerTopRefresh) {
        this.listenerTopRefresh = listenerTopRefresh;
    }

    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        mRecyclerView.addItemDecoration(decor, index);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    public interface ListenerTopRefresh {
        void onRefrsh(View view);

    }

    public interface ListenerBottomRefresh {
        void onRefrsh(View view);

    }

    public interface ListenerLastItem {
        void onLastItem();
    }

    public static class MainView {
        public static int VIEW_TOP = 1;
        public static int VIEW_BOTTOM = 2;
        public static int VIEW_LAST = 3;
        public static int VIEW_NORMAL = 4;
    }

    public static class PullView {
        public static int PULL_TOP = 1;
        public static int PULL_BOTTOM = 2;
        public static int PULL_NOMARL = 3;
    }

}
