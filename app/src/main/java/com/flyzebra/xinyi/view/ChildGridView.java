package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.ui.IAdapter;
import com.flyzebra.xinyi.utils.FlyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/31.
 */
public class ChildGridView extends LinearLayout {
    private final String ROOT = "ROOT";
    private Context context;
    private LinearLayout titleView;
    private List<Map<String, View>> itemViewList;
    private ImageView titleImageView;
    private TextView titleTitle;
    private TextView titleButton;
    private int mBigChildHeight;
    private boolean isAttach = false;
    private List<Map<String, Object>> list;

    private int column = 2;
    private int childMargin = 3;
    private int TitleHeight = 48;
    private float textSize = 18;
    private int textColor = 0xff000000;
    private OnItemClick mOnItemClick;
    private ShowImageSrc mShowImageSrc;

    private int layoutResID = R.layout.child_gridview_item;
    private int[] textViewID = {R.id.child_gridview_item_tv01};
    private String[] textViewKey = {IAdapter.P2_NAME};
    private int[] imageViewID = {R.id.child_gridview_item_iv01};
    private String[] imageViewKEY = {IAdapter.P2_IMG_URL};

    public ChildGridView(Context context) {
        this(context, null);
    }

    public ChildGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContext(context);
    }

    public ChildGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContext(context);
    }

    public ChildGridView init(@LayoutRes int layoutResID, @LayoutRes int[] textViewID, String[] textViewKey, int[] imageViewID, String[] imageViewKEY) {
        this.layoutResID = layoutResID;
        this.textViewID = new int[textViewID.length];
        System.arraycopy(textViewID, 0, this.textViewID, 0, textViewID.length);

        this.textViewKey = new String[textViewKey.length];
        System.arraycopy(textViewKey, 0, this.textViewKey, 0, textViewKey.length);

        this.imageViewID = new int[imageViewID.length];
        System.arraycopy(imageViewID, 0, this.imageViewID, 0, imageViewID.length);

        this.imageViewKEY = new String[imageViewKEY.length];
        System.arraycopy(imageViewKEY, 0, this.imageViewKEY, 0, imageViewKEY.length);
        return this;
    }

    private void initContext(Context context) {
        this.setOrientation(VERTICAL);
        this.context = context;

        titleView = new LinearLayout(context);
        LinearLayout.LayoutParams lpL = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dip2px(TitleHeight));
        lpL.gravity = Gravity.CENTER;
        titleView.setLayoutParams(lpL);
        titleView.setOrientation(HORIZONTAL);
        titleView.setBackgroundColor(0xffffffff);

        createTitleImage(android.R.drawable.star_big_on);
        createTitleTitle("最新产品");
        createTitleButton("查看更多");

        this.addView(titleView);
    }

    public ChildGridView setTitleImage(@DrawableRes int ResID) {
        if (titleImageView != null) {
            titleImageView.setImageResource(ResID);
        }
        return this;
    }

    private void createTitleImage(@DrawableRes int ResID) {
        titleImageView = new ImageView(context);
        LinearLayout.LayoutParams lpI = new LinearLayout.LayoutParams(0, dip2px(TitleHeight), 0.1f);
        lpI.gravity = Gravity.CENTER;
        titleImageView.setLayoutParams(lpI);
        int padding = dip2px(5);
        titleImageView.setPadding(padding, padding, padding, padding);
        titleImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        titleImageView.setImageResource(ResID);
        titleView.addView(titleImageView);
    }

    public ChildGridView setTitle(String title) {
        if (titleTitle != null) {
            titleTitle.setText(title);
        }
        return this;
    }

    public void createTitleTitle(String title) {
        titleTitle = new TextView(context);
        LinearLayout.LayoutParams lpt1 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.5f);
        lpt1.gravity = Gravity.CENTER;
        titleTitle.setLayoutParams(lpt1);
        titleTitle.setText(title);
        titleTitle.setTextSize(textSize);
        titleTitle.setTextColor(textColor);
        titleTitle.setGravity(Gravity.LEFT);
        titleView.addView(titleTitle);
    }

    public ChildGridView setTitleButtonName(String title) {
        if (titleButton != null) {
            titleButton.setText(title);
        }
        return this;
    }

    public void createTitleButton(String name) {
        titleButton = new TextView(context);
        LinearLayout.LayoutParams lpt2 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f);
        lpt2.gravity = Gravity.CENTER;
        titleButton.setLayoutParams(lpt2);
        titleButton.setText(name);
        titleButton.setTextSize(textSize);
        titleButton.setPadding(0, 0, 20, 0);
        titleButton.setTextColor(textColor);
        titleButton.setGravity(Gravity.RIGHT);
        titleView.addView(titleButton);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mBigChildHeight = 0;
        final int count = getChildCount();
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
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
                if (titleView != null && i == 0) {
                    measureChildWithMargins(child, MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY), 0, heightMeasureSpec, 0);
                } else {
                    int childWidth = MeasureSpec.getSize(widthMeasureSpec) / column;
                    int childWidthMS = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                    measureChildWithMargins(child, childWidthMS, 0, heightMeasureSpec, 0);
                }
            }
            final int childHeight = child.getMeasuredHeight();
            final int totalLength = mBigChildHeight;
            FlyLog.i("<ChildGridView>onMeasure:childHeight=" + childHeight);
            mBigChildHeight = Math.max(totalLength, childHeight);
        }
        int number = itemViewList == null ? 0 : itemViewList.size() % column > 0 ? itemViewList.size() / column + 1 : itemViewList.size() / column;
        int mMaxHeight = titleView.getMeasuredHeight() + number * mBigChildHeight;
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mMaxHeight);//此处设置的值为控件将要显示的高度和宽度
        FlyLog.i("<ChildGridView>setMeasuredDimension:width=" + MeasureSpec.getSize(widthMeasureSpec) + ",height=" + mMaxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        FlyLog.i("<ChildGridView>onLayout:l=" + l + ",t=" + t + ",r=" + r + ",b=" + b);
        int top = 0;
        if (isAttach) {
            if (titleView != null) {
                titleView.layout(l + childMargin, 0 + childMargin * 2, r - childMargin, titleView.getMeasuredHeight());
                top = top + titleView.getMeasuredHeight();
            }
            if (list == null) return;
            int num = list.size();
            int width = r / column;
            for (int i = 0; i < num; i++) {
                View view = itemViewList.get(i).get(ROOT);
                int ctop = top + (i / column * mBigChildHeight);
                int left = (i % column) * (width);
                view.layout(left + childMargin, ctop + childMargin, left + width - childMargin, ctop + mBigChildHeight - childMargin);
                FlyLog.i("<ChildGridView>onLayout-item:l=" + left + ",t=" + top + ",r=" + (left + width) + ",b=" + (top + mBigChildHeight) + ",mBigChildHeight" + mBigChildHeight);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttach = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttach = false;
    }

    public void addItemView(Context context, final int num) {
        Map map = new HashMap();
        View root = LayoutInflater.from(context).inflate(layoutResID, null);
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    //为了图元共亨动画，转入VIEW
                    mOnItemClick.OnItemClidk(list.get(num), v);
                }
            }
        });
        map.put(ROOT, root);

        for (int n = 0; n < textViewID.length; n++) {
            TextView tv = (TextView) root.findViewById(textViewID[n]);
            map.put(textViewID[n], tv);
        }

        for (int n = 0; n < imageViewID.length; n++) {
            ImageView iv = (ImageView) root.findViewById(imageViewID[n]);
            map.put(imageViewID[n], iv);
        }
        itemViewList.add(map);
        this.addView(root);
    }

    public void setData(List<Map<String, Object>> list) {
        this.list = list;
        if (itemViewList == null) {
            itemViewList = new ArrayList<>();
        }
        for (int i = 0; i < list.size(); i++) {
            if (itemViewList.size() == i) {
                addItemView(context, i);
            }
            View root = itemViewList.get(i).get(ROOT);
            if (root.getVisibility() == GONE) {
                root.setVisibility(VISIBLE);
            }
            ShowItemViewData(itemViewList.get(i), list.get(i));
        }
        //隐藏多余的列表
        if (itemViewList.size() > list.size()) {
            for (int i = list.size(); i < itemViewList.size(); i++) {
                itemViewList.get(i).get(ROOT).setVisibility(GONE);
            }
        }

    }

    public void ShowItemViewData(Map<String, View> itemView, Map<String, Object> data) {
        for (int i = 0; i < textViewID.length; i++) {
            TextView tv = (TextView) itemView.get(textViewID[i]);
            tv.setText((String) data.get(textViewKey[i]));
            tv.setTextColor(textColor);
        }
        for (int i = 0; i < imageViewID.length; i++) {
            ImageView iv = (ImageView) itemView.get(imageViewID[i]);
            if (mShowImageSrc != null) {
                mShowImageSrc.setImageSrcWithUrl((String) data.get(imageViewKEY[i]), iv);
            }
        }
        postInvalidate();
    }

    public int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public ChildGridView setColumn(int column) {
        this.column = column;
        return this;
    }

    public ChildGridView setChildMargin(int childMargin) {
        this.childMargin = childMargin;
        return this;
    }

    public ChildGridView setTitleHeight(int titleHeight) {
        TitleHeight = titleHeight;
        return this;
    }

    public ChildGridView setTextSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public ChildGridView setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public ChildGridView setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
        return this;
    }

    public ChildGridView setShowImageSrc(ShowImageSrc mShowImageSrc) {
        this.mShowImageSrc = mShowImageSrc;
        return this;
    }

    public interface OnItemClick {
        void OnItemClidk(Map<String, Object> data, View v);
    }

    public interface ShowImageSrc {
        void setImageSrcWithUrl(String url, ImageView iv);
    }

}
