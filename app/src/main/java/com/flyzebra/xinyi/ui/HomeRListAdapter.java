package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.HttpAdapter;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/26.
 */
public class HomeRListAdapter extends RecyclerView.Adapter<ViewHolder> implements HttpAdapter {
    private List<Map<String, Object>> list;
    private Context context;
    private IHttp iHttp = MyVolley.getInstance();
    private OnItemClick onItemClick;


    public HomeRListAdapter(Context context, List<Map<String, Object>> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder hodler = null;
        switch (viewType) {
            case H_VIEWPAGER:
                ViewPagerChildView childViewPager = new ViewPagerChildView(context);
//                view.setLayoutParams(parent.getLayoutParams());
                hodler = new ViewPageHolder(childViewPager);
                break;
            case H_RCLIST:
                View view = LayoutInflater.from(context).inflate(R.layout.product_item_01, parent, false);
                hodler = new Product01(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClick != null) {
                            int position = (int) v.getTag();
                            onItemClick.onItemClick(position);
                        }
                    }
                });
                break;
        }
        return hodler;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof Product01) {
            ((Product01) holder).tv01.setText(String.valueOf(list.get(position).get(P1_NAME)));
            ((Product01) holder).tv02.setText(String.valueOf(list.get(position).get(P1_PRICE)));
            iHttp.upImageView(context, "http://192.168.1.88/ordermeal" + list.get(position).get(P1_IMG_URL), ((Product01) holder).iv01);
            holder.itemView.setTag(position);
        } else if (holder instanceof ViewPageHolder) {
            ViewPagerChildView view = ((ViewPagerChildView) holder.itemView);
            view.setData((List<Map<String, Object>>) list.get(position).get(DATA));
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = (String) list.get(position).get(TYPE);
        if (type == null) {
            return H_RCLIST;
        } else if (type.equals(VIEWPAGER)) {
            return H_VIEWPAGER;
        }
        return H_RCLIST;
    }

    @Override
    public List getList() {
        return list;
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    public class Product01 extends ViewHolder {
        TextView tv01;
        TextView tv02;
        ImageView iv01;

        public Product01(View itemView) {
            super(itemView);
            tv01 = (TextView) itemView.findViewById(R.id.p_item_01_tv_01);
            tv02 = (TextView) itemView.findViewById(R.id.p_item_01_tv_02);
            iv01 = (ImageView) itemView.findViewById(R.id.p_item_01_iv_01);
        }
    }

    public class ViewPageHolder extends ViewHolder {
        public ViewPageHolder(View itemView) {
            super(itemView);
        }
    }


}
