package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;

import java.util.List;

/**
 * Created by FlyZebra on 2016/3/5.
 */
public class PoiRVAdapter extends RecyclerView.Adapter<PoiRVAdapter.MyViewHolder>{
    private Context context;
    private List<ApplicationInfo> list;
    private int resId;

    public PoiRVAdapter(Context context, List<ApplicationInfo> list, int resId){
        this.context = context;
        this.list = list;
        this.resId = resId;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(resId, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv01.setText(list.get(position).loadLabel(context.getPackageManager()));
        holder.iv01.setImageDrawable(list.get(position).loadIcon(context.getPackageManager()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv01;
        TextView tv01;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv01 = (ImageView) itemView.findViewById(R.id.iv01);
            tv01 = (TextView) itemView.findViewById(R.id.tv01);
        }
    }
}
