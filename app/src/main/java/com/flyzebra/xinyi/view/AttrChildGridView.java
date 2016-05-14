package com.flyzebra.xinyi.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.ui.IAdapter;

import java.util.List;
import java.util.Map;

/**
 * 扩展ChildGridView 添加了显示产品属性功能
 * Created by Administrator on 2016/5/12.
 */
public class AttrChildGridView extends ChildGridView {
    public AttrChildGridView(Context context) {
        super(context);
    }

    public AttrChildGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AttrChildGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindView(View root, Map map) {
        super.bindView(root, map);
        LinearLayout ll = (LinearLayout) root.findViewById(R.id.p_item_01_ll_01);
        map.put("addview", ll);
    }

    @Override
    protected void SetItemViewData(Map<String, View> itemView, Map<String, Object> data) {
        super.SetItemViewData(itemView, data);
        LinearLayout ll = (LinearLayout) itemView.get("addview");
        ll.removeAllViews();
        List<Map<String, Object>> pattr = (List) data.get(IAdapter.PR1_PATTR);
        if (pattr==null) return;
        for (int i = 0; i < pattr.size(); i++) {
            String name = (String) pattr.get(i).get(IAdapter.PR1_PATTR_NAME);
            String color = (String) pattr.get(i).get(IAdapter.PR1_PATTR_COLOR);
            TextView tv = new TextView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(dip2px(1), 0, dip2px(1), 0);
            tv.setPadding(dip2px(5), 0, dip2px(5), 0);
            tv.setLayoutParams(lp);
            tv.setText(name);
            tv.setLines(1);
            tv.setBackgroundColor(Color.parseColor(color));
            tv.setTextColor(0xffffffff);
            ll.addView(tv);
        }
    }
}
