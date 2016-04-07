package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
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
    private final String IV01 = "IV01";
    private final String TV01 = "TV01";
    private Context context;
    private LinearLayout titleView;
    private List<Map<String, View>> itemViewList;
    private ImageView titleImageView;
    private TextView titleTitle;
    private TextView titleButton;
    private int mBigChildHeight;
    private boolean isAttach = false;
    private List<Map<String, Object>> list;

    private int column = 3;
    private int childMargin = 3;
    private int TitleHeight = 48;
    private float textSize = 18;
    private int textColor = 0xff000000;
    private OnItemClick mOnItemClick;
    private ShowImageSrc mShowImageSrc;

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

//        titleView = LayoutInflater.from(context).inflate(R.layout.child_gridview_title, null);
//        titleIv = (ImageView) titleView.findViewById(R.id.child_gridview_title_iv01);
//        titleTvHeader = (TextView) titleView.findViewById(R.id.child_gridview_title_tv01);
//        titleTvMore = (TextView) titleView.findViewById(R.id.child_gridview_title_tv02);
        this.addView(titleView);
    }

    public void setTitleImage(@DrawableRes int ResID) {
        if (titleImageView != null) {
            titleImageView.setImageResource(ResID);
        }
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

    public void setTitle(String title) {
        if (titleTitle != null) {
            titleTitle.setText(title);
        }
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

    public void setTitleButtonName(String title) {
        if (titleButton != null) {
            titleButton.setText(title);
        }
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
                if (i == 0) {
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
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        int number = itemViewList == null ? 0 : itemViewList.size() % column > 0 ? itemViewList.size() / column + 1 : itemViewList.size() / column;
        int mMaxHeight = titleView.getMeasuredHeight() + number * mBigChildHeight;
        heightMeasureSpec = Math.max(MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.EXACTLY), heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        FlyLog.i("<ChildGridView>onMeasure:mBigChildHeight=" + mBigChildHeight);
        FlyLog.i("<ChildGridView>onMeasure:width=" + MeasureSpec.getSize(widthMeasureSpec) + ",height=" + MeasureSpec.getSize(heightMeasureSpec));
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

    public void addItemView(Context context, final int i) {
        Map map = new HashMap();
        View view = LayoutInflater.from(context).inflate(R.layout.child_gridview_item, null);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.OnItemClidk(list.get(i));
                }
            }
        });
        map.put(ROOT, view);
        ImageView iv01 = (ImageView) view.findViewById(R.id.child_gridview_item_iv01);
        map.put(IV01, iv01);
        TextView tv01 = (TextView) view.findViewById(R.id.child_gridview_item_tv01);
        map.put(TV01, tv01);
        itemViewList.add(map);
        this.addView(view);
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
        ImageView iv01 = (ImageView) itemView.get(IV01);
        TextView tv01 = (TextView) itemView.get(TV01);
        if (mShowImageSrc != null) {
            mShowImageSrc.setImageSrcWithUrl(iv01, (String) data.get("path"));
        }
        tv01.setText((String) data.get("name"));
        FlyLog.i("<ChildGridView>ShowItemViewData:path=" + data.get("path") + ",name=" + data.get("name"));
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

    public void setColumn(int column) {
        this.column = column;
    }

    public void setChildMargin(int childMargin) {
        this.childMargin = childMargin;
    }

    public void setTitleHeight(int titleHeight) {
        TitleHeight = titleHeight;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public void setShowImageSrc(ShowImageSrc mShowImageSrc) {
        this.mShowImageSrc = mShowImageSrc;
    }

    public interface OnItemClick {
        void OnItemClidk(Map<String, Object> data);
    }

    public interface ShowImageSrc {
        void setImageSrcWithUrl(ImageView iv, String url);
    }

}
