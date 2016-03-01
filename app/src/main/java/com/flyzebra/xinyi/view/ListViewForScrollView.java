package com.flyzebra.xinyi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义适应ScrollView的ListView，需要在代码中设置ScrollView
 * sv = (ScrollView) findViewById(R.id.act_solution_4_sv);
 * sv.smoothScrollTo(0, 0);
 * Created by FlyZebra on 2016/2/29.
 */
public class ListViewForScrollView extends ListView {
    public ListViewForScrollView(Context context) {
        super(context);

    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
