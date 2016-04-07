package com.flyzebra.xinyi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
 * 自定义适应ScrollView的GridView，需要在代码中设置ScrollView，这控件垃圾的要死，性能低下
 * sv = (ScrollView) findViewById(R.id.act_solution_4_sv);
 * sv.smoothScrollTo(0, 0);
 * Created by FlyZebra on 2016/3/1.
 */
public class GridViewForScrollView extends GridView {

    public GridViewForScrollView(Context context) {
        super(context);
    }
    public GridViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public GridViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int mBigChildHeight = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
            if (!(heightMode == MeasureSpec.EXACTLY && lp.height == 0 && lp.weight > 0)) {
                measureChildWithMargins(child, MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY), 0, heightMeasureSpec, 0);
            }
            mBigChildHeight = child.getMeasuredHeight();
            break;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        int childcount = getAdapter().getCount();
        int columns = getNumColumns();
        int number = childcount == 0 ? 0 : childcount % getNumColumns() > 0 ? childcount / columns + 1 : childcount / columns;
        int mMaxHeight = number * mBigChildHeight;
        heightMeasureSpec = Math.max(MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.EXACTLY), heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
