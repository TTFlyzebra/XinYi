package com.flyzebra.xinyi.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.ResUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by FlyZebra on 2016/4/1.
 */
public class RefreshRecyclerView extends ViewGroup {
    private int pull_Height = 200;
    private int animScoller_time = 500;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private LayoutManager mLayout;
    private View topView;
    private View botView;
    private int childMargin = 0;
    private AtomicBoolean isGetChildMargin = new AtomicBoolean(false);

    private int RLIST = LIST.SCROLL;
    private int SHOW = PULL.NORMAL;
    private float down_y;
    private float mv_x, mv_y;
    private boolean TOP_MODE = false;
    private boolean BOT_MODE = false;
    private boolean isAttach;

    private AtomicBoolean isNeedRefresh = new AtomicBoolean(false);
    private AtomicBoolean cancleSmooth = new AtomicBoolean(false);
    private ListenerTopRefresh listenerTopRefresh;
    private ListenerBottomRefresh listenerBottomRefresh;
    private ListenerLastItem listenerLastItem;
    private ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private RecyclerView.OnScrollListener onScrollLisenter = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            executors.execute(new SetMainViewState());
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //当没有滚动条出现时的情况，如不处理，下拉无反应
            executors.execute(new SetMainViewState());
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    scrollBy(msg.arg1, msg.arg2);
                    break;
                case 2:
                    scrollTo(msg.arg1, msg.arg2);
                    break;
            }
        }
    };

    public RefreshRecyclerView(Context context) {
        super(context);
        mRecyclerView = new RecyclerView(context);
        init(context);
    }


    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRecyclerView = new RecyclerView(context, attrs);
        init(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRecyclerView = new RecyclerView(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        pull_Height = ResUtils.getMetrices((Activity) context).heightPixels / 10;

        topView = createPullTextView(context);

        botView = createPullTextView(context);

        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        super.addView(mRecyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        super.addView(botView, LayoutParams.MATCH_PARENT, pull_Height);
        super.addView(topView, LayoutParams.MATCH_PARENT, pull_Height);
    }

    private View createPullTextView(Context context) {
        TextView topView = new TextView(context);
        topView.setTextSize(20);
        topView.setGravity(Gravity.CENTER);
        topView.setTextColor(0xFF000000);
        return topView;
    }

    public void setLayoutManager(LayoutManager layout) {
        mLayout = layout;
        mRecyclerView.setLayoutManager(layout);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setRefrshTop(boolean flag) {
        TOP_MODE = flag;
    }

    public void setRefrshBottom(boolean flag) {
        BOT_MODE = flag;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        FlyLog.i("<RefreshRecyclerView>onLayout::l=" + l + ",t=" + t + ",r=" + r + ",b=" + b);
        if (isAttach) {
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
    }

    public void setPullViewHeight(int heigth) {
        pull_Height = heigth;
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
            if (PerfromPullView(ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean PerfromPullView(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mv_x = ev.getX();
                mv_y = down_y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //down_y==0 肯定不是本界面发起的消息，如果不是在本界面收到的DWON消息,可以处理跟其它控件的冲突
                if (down_y == 0) {
                    break;
                }
                //横向的划动冲突解决
                if (Math.abs(ev.getX() - mv_x) > Math.abs(ev.getY() - mv_y) && (TOP_MODE || BOT_MODE)) {
                    if (SHOW == PULL.TOP) {
                        mRecyclerView.scrollToPosition(0);
                    }
                    if (SHOW == PULL.BOTTOM) {
                        mRecyclerView.scrollToPosition(mLayout.getItemCount() - 1);
                    }
                    if (SHOW != PULL.TOP && SHOW != PULL.BOTTOM) {
                        scrollTo(0, 0);
                    }
                }
                //BOT弹出处理
                cancleSmooth.set(true);
                if (checkTopBottomState(ev)) return true;
                //TOP弹出处理
                if (checkTopPullState(ev)) return true;
                down_y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollY() != 0) {
                    if (SHOW == PULL.TOP && TOP_MODE) {
                        mRecyclerView.scrollToPosition(0);
                        perfromRefreshTop();
                    } else if (SHOW == PULL.BOTTOM && BOT_MODE) {
                        mRecyclerView.scrollToPosition(mLayout.getItemCount() - 1);
                        perfromRefreshBottom();
                    }
                    return true;
                }
                break;
        }
        return SHOW == PULL.TOP || SHOW == PULL.BOTTOM;
    }

    private boolean checkTopPullState(MotionEvent ev) {
        if (SHOW == PULL.TOP && TOP_MODE) {
            setTopViewState();
            scrollBy(0, (int) (down_y - ev.getY()));
            if (getScrollY() >= 0) {
                scrollTo(0, 0);
                SHOW = PULL.NORMAL;
                mRecyclerView.scrollToPosition(0);
            } else {
                SHOW = PULL.TOP;
            }
            down_y = ev.getY();
            return true;
        }

        if ((RLIST == LIST.TOP || mLayout.getItemCount() == 0) && (ev.getY() > down_y) && TOP_MODE) {
            isNeedRefresh.set(false);
            SHOW = PULL.TOP;
            down_y = ev.getY();
            mRecyclerView.scrollToPosition(0);
            return true;
        }
        return false;
    }

    private boolean checkTopBottomState(MotionEvent ev) {
        if (SHOW == PULL.BOTTOM && BOT_MODE) {
            setBottomViewState();
            scrollBy(0, (int) (down_y - ev.getY()));
            if (getScrollY() <= 0) {
                scrollTo(0, 0);
                SHOW = PULL.NORMAL;
                mRecyclerView.scrollToPosition(mLayout.getItemCount() - 1);
            } else {
                SHOW = PULL.BOTTOM;
            }
            down_y = ev.getY();
            return true;
        }
        if (RLIST == LIST.BOTTOM && (ev.getY() < down_y) && BOT_MODE) {
            isNeedRefresh.set(false);
            SHOW = PULL.BOTTOM;
            down_y = ev.getY();
            mRecyclerView.scrollToPosition(mLayout.getItemCount() - 1);
            return true;
        }
        return false;
    }

    private void setTopViewState() {
        if (getScrollY() < (-pull_Height)) {
            ((TextView) topView).setText("放开以刷新...");
            isNeedRefresh.set(true);
            //准备启动动画
        } else {
            isNeedRefresh.set(false);
            ((TextView) topView).setText("下拉刷新...");
            //准备变更动画
        }
    }

    private void setBottomViewState() {
        if (getScrollY() > (pull_Height)) {
            ((TextView) botView).setText("放开以加载...");
            isNeedRefresh.set(true);
            //准备启动刷新任务
        } else {
            isNeedRefresh.set(false);
            ((TextView) botView).setText("上拉加截...");
        }
    }

    private void perfromRefreshTop() {
        ((TextView) topView).setText("正在刷新...");
        //执行刷新任务
        if (listenerTopRefresh != null && isNeedRefresh.get()) {
            listenerTopRefresh.onRefrsh(topView);
        }
        //刷新完成执行以下动作
        smoothScrollToY(getScrollY(), 0, animScoller_time);
    }

    private void perfromRefreshBottom() {
        ((TextView) botView).setText("正在加载...");
        //执行刷新任务
        if (listenerBottomRefresh != null && isNeedRefresh.get()) {
            listenerBottomRefresh.onRefrsh(topView);
        }
        //刷新完成执行以下动作
        smoothScrollToY(getScrollY(), 0, animScoller_time);
    }

//    /**
//     用在ViewPager中不这样设置会提示找不到ID，解决方案1，如下所示，或者生成后重新分配ID
//     java.lang.IllegalArgumentException: Wrong state class, expecting View State but received class
//     android.support.v7.widget.RecyclerView$SavedState instead. This usually happens when two views
//     of different type have the same id in the same hierarchy. This view's id is id/poi_type_rv_01.
//     Make sure other views do not use the same id.
//     * @param state
//     */
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            super.onRestoreInstanceState(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    private void smoothScrollToY(int sy, int dy, int times) {
        cancleSmooth.set(false);
        FlyLog.i("<RefreshRecycleryView>animScrollTo");
        executors.submit(new SmoothScroll(sy, dy, times));
    }

    @Override
    protected void onAttachedToWindow() {
        isAttach = true;
        mRecyclerView.addOnScrollListener(onScrollLisenter);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        isNeedRefresh.set(false);
        isAttach = false;
        mRecyclerView.removeOnScrollListener(onScrollLisenter);
        super.onDetachedFromWindow();
    }

    public void refreshSuccess() {
        if (SHOW == PULL.TOP) {
            ((TextView) topView).setText("更新成功...");
        } else if (SHOW == PULL.BOTTOM) {
            ((TextView) botView).setText("更新成功...");
        }
        isNeedRefresh.set(false);
    }

    public void refreshFailed() {
        if (SHOW == PULL.TOP) {
            ((TextView) topView).setText("网络连接失败...");
        } else if (SHOW == PULL.BOTTOM) {
            ((TextView) botView).setText("网络连接失败...");
        }
        isNeedRefresh.set(false);
    }

    public void setListenerLastItem(ListenerLastItem listenerLastItem) {
        this.listenerLastItem = listenerLastItem;
    }

    public void setListenerBottomRefresh(ListenerBottomRefresh listenerBottomRefresh) {
        BOT_MODE = true;
        this.listenerBottomRefresh = listenerBottomRefresh;
    }

    public void setListenerTopRefresh(ListenerTopRefresh listenerTopRefresh) {
        TOP_MODE = true;
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

    public interface LIST {
        int TOP = 1;
        int BOTTOM = 2;
        int LAST = 3;
        int EMPTY = 4;
        int SCROLL = 0;
    }

    public interface PULL {
        int TOP = 1;
        int BOTTOM = 2;
        int NORMAL = 0;
    }

    private class SetMainViewState implements Runnable {
        @Override
        public void run() {
            int first = -8;
            int last = -8;
            View firstView = null;
            View lastView = null;
            LinearLayoutManager mGLayout = (LinearLayoutManager) mLayout;
            first = mGLayout.findFirstVisibleItemPosition();
            if (first == RecyclerView.NO_POSITION) {
                RLIST = LIST.EMPTY;
                return;
            }
            last = mGLayout.findLastVisibleItemPosition();
            firstView = mGLayout.findViewByPosition(0);
            lastView = mGLayout.findViewByPosition(mLayout.getItemCount() - 1);
            if (first == 0 && !isGetChildMargin.get()) {
                View secondView = mGLayout.findViewByPosition(1);
                if (secondView != null && firstView != null) {
                    int firstBootm = firstView.getBottom();
                    int secondTop = secondView.getTop();
                    childMargin = (secondTop - firstBootm) / 2;
                    isGetChildMargin.set(true);
                }
            }
            if (first == 0 && firstView.getTop() == childMargin) {
                RLIST = LIST.TOP;
            } else if ((last == mLayout.getItemCount() - 1) && (lastView.getBottom() == getHeight() - childMargin)) {
                RLIST = LIST.BOTTOM;
            } else if (last > mLayout.getItemCount() - 2) {
                if (RLIST != LIST.LAST) {
                    if (listenerLastItem != null) {
                        listenerLastItem.onLastItem();
                    }
                }
                RLIST = LIST.LAST;
            } else {
                RLIST = LIST.SCROLL;
            }
//            FlyLog.i("<RefreshRecyclerView>-->SetMainViewState:RLIST=" + RLIST + ",first=" + first + ",last" + last + ",top=" + firstView.getTop() + ",bottom=" + lastView.getBottom() + ",height=" + getHeight());
            FlyLog.i("<RefreshRecyclerView>-->SetMainViewState:RLIST=" + RLIST);
        }
    }

    private class SmoothScroll implements Runnable {
        int sy, dy, times;

        public SmoothScroll(int sy, int dy, int times) {
            this.sy = sy;
            this.dy = dy;
            this.times = times;
        }

        @Override
        public void run() {
            int height = pull_Height;
            int num = times / 40;
            int my = (dy - sy) / num;
            for (int i = 0; i < num; i++) {
                if (cancleSmooth.get()) return;
                //当弹出界面等于自身身高时停止滚动并等待更新任务完成
                int scroolly = getScrollY();
                while (isNeedRefresh.get() && (Math.abs(scroolly) < height) && isAttach) {
                    if (cancleSmooth.get()) return;
                    if ((Math.abs(scroolly) < height)) {
                        if (SHOW == PULL.TOP) {
                            Message.obtain(mHandler, 2, 0, -height).sendToTarget();
                        } else if (SHOW == PULL.BOTTOM) {
                            Message.obtain(mHandler, 2, 0, height).sendToTarget();
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (Math.abs(getScrollY()) <= Math.abs(my)) break;
                Message.obtain(mHandler, 1, 0, my).sendToTarget();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message.obtain(mHandler, 2, 0, 0).sendToTarget();
            SHOW = PULL.NORMAL;
        }
    }

}
