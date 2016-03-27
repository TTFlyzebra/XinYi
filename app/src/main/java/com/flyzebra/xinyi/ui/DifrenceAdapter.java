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
import com.flyzebra.xinyi.model.MyHttp;
import com.flyzebra.xinyi.utils.FlyLog;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/26.
 */
public class DifrenceAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final int TYPE_VIEWPAGER = 1;
    private static final int TYPE_LISTVIE = 2;
    private List<Map<String, Object>> list;
    private Context context;

    public DifrenceAdapter(Context context, List<Map<String, Object>> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder hodler = null;
        switch (viewType) {
            case TYPE_VIEWPAGER:
                ViewPagerChildView childViewPager = new ViewPagerChildView(context);
//                view.setLayoutParams(parent.getLayoutParams());
                hodler = new ViewPageHolder(childViewPager);
                break;
            case TYPE_LISTVIE:
                View view = LayoutInflater.from(context).inflate(R.layout.home_listview_item, parent, false);
                hodler = new ListViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        list.remove(position);
                        FlyLog.i("<DifrenceAdapter>onCreateViewHolder->onClick:position=" + position);
                        notifyDataSetChanged();
                    }
                });
                break;
        }
        return hodler;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            ((ListViewHolder) holder).tv01.setText(String.valueOf(list.get(position).get("mealname")));
            ((ListViewHolder) holder).tv02.setText(String.valueOf(list.get(position).get("mealprice")));
            MyHttp.getInstance().upImageView(context, "http://192.168.1.88/ordermeal" + list.get(position).get("mealimage"), ((ListViewHolder) holder).iv01);
            holder.itemView.setTag(position);
        } else {
            ViewPagerChildView view = ((ViewPagerChildView) holder.itemView);
            view.setData((List<Map<String, Object>>) list.get(position).get("LIST"));
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = (String) list.get(position).get("TYPE");
        if (type == null) {
            return TYPE_LISTVIE;
        }
        if (type.equals("VIEWPAGER")) {
            return TYPE_VIEWPAGER;
        } else {
            return TYPE_LISTVIE;
        }
    }

    public class ViewPageHolder extends ViewHolder {
        public ViewPageHolder(View itemView) {
            super(itemView);
        }
    }

    public class ListViewHolder extends ViewHolder {
        TextView tv01;
        TextView tv02;
        ImageView iv01;

        public ListViewHolder(View itemView) {
            super(itemView);
            tv01 = (TextView) itemView.findViewById(R.id.tv01);
            tv02 = (TextView) itemView.findViewById(R.id.tv02);
            iv01 = (ImageView) itemView.findViewById(R.id.iv01);
        }
    }
}
