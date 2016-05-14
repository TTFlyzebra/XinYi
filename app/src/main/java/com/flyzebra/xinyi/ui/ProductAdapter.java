package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.utils.DisplayUtils;
import com.flyzebra.xinyi.utils.ResUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/26.
 */
public class ProductAdapter extends RecyclerView.Adapter<ViewHolder> implements IAdapter {
    private List<Map<String, Object>> list;
    private Context context;
    private IHttp iHttp = MyVolley.getInstance();

    public ProductAdapter(Context context, List<Map<String, Object>> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_01, parent, false);
        ViewHolder hodler = new ListViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
//                        list.remove(position);
            }
        });
        return hodler;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            ((ListViewHolder) holder).tv01.setText(String.valueOf(list.get(position).get(PR1_NAME)));
            ((ListViewHolder) holder).tv02.setText(String.valueOf(list.get(position).get(PR1_DESCRIBE)));
            ((ListViewHolder) holder).tv03.setText(String.valueOf(list.get(position).get(PR1_PRICE)));
            iHttp.upImageView(context, Constant.URL + list.get(position).get(PR1_IMGURL), ((ListViewHolder) holder).iv01);
            List<Map<String, Object>> attr = (List<Map<String, Object>>) list.get(position).get(PR1_PATTR);
            ((ListViewHolder) holder).ll01.removeAllViews();
            for (int i = 0; i < attr.size(); i++) {
                String name = (String) attr.get(i).get(PR1_PATTR_NAME);
                String color = (String) attr.get(i).get(PR1_PATTR_COLOR);
                TextView tv = new TextView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                lp.setMargins(DisplayUtils.dip2px(context,1), 0, DisplayUtils.dip2px(context, 1), 0);
                tv.setPadding(DisplayUtils.dip2px(context, 5), 0, DisplayUtils.dip2px(context, 5), 0);
                tv.setLayoutParams(lp);
                tv.setText(name);
                tv.setLines(1);
                tv.setBackgroundColor(Color.parseColor(color));
                tv.setTextColor(0xffffffff);
                ((ListViewHolder) holder).ll01.addView(tv);
            }
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public List getList() {
        return list;
    }

    public class ListViewHolder extends ViewHolder {
        TextView tv01;
        TextView tv02;
        TextView tv03;
        ImageView iv01;
        LinearLayout ll01;

        public ListViewHolder(View itemView) {
            super(itemView);
            tv01 = (TextView) itemView.findViewById(R.id.p_item_01_tv_01);
            tv02 = (TextView) itemView.findViewById(R.id.p_item_01_tv_02);
            tv03 = (TextView) itemView.findViewById(R.id.p_item_01_tv_03);
            iv01 = (ImageView) itemView.findViewById(R.id.p_item_01_iv_01);
            ll01 = (LinearLayout) itemView.findViewById(R.id.p_item_01_ll_01);
        }
    }
}
