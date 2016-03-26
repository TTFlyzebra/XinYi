package com.flyzebra.xinyi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/3/24.
 */
public class FlyAddGridView extends ViewGroup implements IFlyAddGridView {
    private int gridnums = 1;
    private LinearLayout menuItemLayout;
    private LinearLayout gridItemLayout;
    private int columns = 2;
    private int width;
    private int height;

    public FlyAddGridView(Context context) {
        super(context);
        init();
    }

    public FlyAddGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlyAddGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FlyAddGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        menuItemLayout = createMenuItem();
        gridItemLayout = createGridItem();
        this.addView(menuItemLayout, 0);
        for (int i = 1; i <= gridnums; i++) {
            this.addView(gridItemLayout, i);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        menuItemLayout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        gridItemLayout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int rows = (getChildCount() - 2 + columns) / columns;
        int finalHeight = menuItemLayout.getHeight() + gridItemLayout.getHeight() * rows;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int num = getChildCount();
    }

    @Override
    public LinearLayout createMenuItem() {
        return null;
    }

    @Override
    public LinearLayout createGridItem() {
        return null;
    }
}
