package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/3/5.
 */
public class PoiRVAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<Map<String,Objects>> list;
    private int resId;

    public PoiRVAdapter(Context context, List<Map<String, Objects>> list, int resId){
        this.context = context;
        this.list = list;
        this.resId = resId;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
