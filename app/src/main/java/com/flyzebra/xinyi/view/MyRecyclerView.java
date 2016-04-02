package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by Administrator on 2016/3/31.
 */
public class MyRecyclerView extends RecyclerView {
    private Adapter mAdapter;
    private int mTouchSlop;//用来判断是不是滑动
    private boolean isMoveTop = false;
    private boolean isMoveBottom = false;
    private boolean isLastCount = false;
    private float start_y;
    private float end_y;
    private View parentView;
    private View headerView;

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initContext(context);
    }

    private void initContext(Context context) {
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        super.setAdapter(adapter);
    }

    public boolean isLastCount() {
        return isLastCount;
    }

    public boolean isMoveTop() {
        return isMoveTop;
    }

    public boolean isMoveBottom() {
        return isMoveBottom;
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        LinearLayoutManager lm = (LinearLayoutManager) getLayoutManager();
        if (lm.findFirstVisibleItemPosition() == 0 && lm.findViewByPosition(lm.findFirstVisibleItemPosition()).getTop() == 0) {
//            FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->滚动到了顶部");
            isMoveTop = true;
        } else {
            if (isMoveTop) {
//                FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->滚动离开顶部");
                isMoveTop = false;
            }
        }
        if (lm.findViewByPosition(lm.findLastVisibleItemPosition()).getBottom() == getHeight() && lm.findLastVisibleItemPosition() == lm.getItemCount() - 1) {
//            FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->滚动到了底部");
            isMoveBottom = true;
        } else {
            if (isMoveBottom) {
//                FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->滚动离开底部");
                isMoveBottom = false;
            }
        }
        if (lm.findLastVisibleItemPosition() > lm.getItemCount() - 2) {
//            FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->滚动到最后一行");
            isLastCount = false;
        } else {
            if (isLastCount) {
//                FlyLog.i("<RefreshRecyclerView>-->dispatchTouchEvent->当前位置已不是最后一行");
                isLastCount = true;
            }
        }
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                FlyLog.i(" MotionEvent.ACTION_DOWN:");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                end_y = e.getY();
//                if (isMoveTop) {
//                    if(start_y == 0){
//                        start_y = e.getY();
//                    }
////                    FlyLog.i("parentView.scorllBy-->"+(start_y-e.getY()));
////                    if (start_y < e.getY()) {
////                        parentView.scrollBy(0, (int) (start_y-e.getY()));
////                        FlyLog.i("parentView.scorllBy-->" + (start_y - e.getY())+",isMoveTop="+isMoveTop);
////                        start_y = e.getY();
////                        return true;
////                    }
//                    if ((int)(end_y-start_y)>mTouchSlop) {
//                        FlyLog.i("start_y=" + start_y + ",e.getY()=" + end_y);
//                        headerView.setVisibility(VISIBLE);
//                        parentView.scrollTo(0, (int) (headerView.getWidth() + start_y - end_y));
//                        FlyLog.i("parentView.scrollTo()-x"+(int) (headerView.getWidth() + start_y - end_y));
////                        ViewHelper.setTranslationY(headerView, start_y - e.getY() - headerView.getHeight());
////                        ViewHelper.setPivotY(headerView, 0);
////                        headerView.invalidate();
////                        parentView.scrollTo(0, (int) (headerView.getWidth()-(e.getY()-start_y)));
//                    }else{
//                        start_y = e.getY();
//                        isMoveTop = false;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                start_y = 0;
//                FlyLog.i("ACTION_UP start_y="+start_y+",e.getY()="+end_y);
//                isMoveTop = false;
//                headerView.setVisibility(GONE);
//                parentView.scrollTo(0, 0);
//                break;
//        }
        return super.onTouchEvent(e);
    }

}
