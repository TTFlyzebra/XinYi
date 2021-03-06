package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TvIvAdapter extends BaseAdapter implements IAdapter<List<Map<String, Object>>> {
    private List<Map<String, Object>> list;
    private int idListview;
    private int tvid[];
    private String tvkey[];
    private int ivid[];
    private String ivkey[];
    private ItemClick mItemClick;
    private SetImageView mSetImageView;
    private Context context;
    private SimpleAdapter a;
    /**
     * 自定义通用包含TextView ImageVIew的Adapter 支持回调
     *
     */
    public TvIvAdapter(Context context, List<Map<String, Object>> list, int idListview,
                       int tvid[], String tvkey[], int ivid[], String ivkey[],
                       ItemClick mItemClick,SetImageView setImageView) {
        this.context = context;
        this.list = list;
        this.idListview = idListview;
        this.mItemClick = mItemClick;
        this.tvid = new int[tvid.length];
        this.tvkey = new String[tvkey.length];
        System.arraycopy(tvkey, 0, this.tvkey, 0, tvkey.length);
        System.arraycopy(tvid, 0, this.tvid, 0, tvid.length);
        this.ivid = new int[ivid.length];
        this.ivkey = new String[ivkey.length];
        System.arraycopy(ivid, 0, this.ivid, 0, ivid.length);
        System.arraycopy(ivkey,0,this.ivkey,0,ivkey.length);
        this.mItemClick = mItemClick;
        this.mSetImageView=setImageView;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(idListview, null);
            holder.tv_list.clear();
            for (int i = 0; i < tvid.length; i++) {
                holder.tv_list.add((TextView) convertView.findViewById(tvid[i]));
            }
            holder.iv_list.clear();
            for (int i = 0; i < ivid.length; i++) {
                holder.iv_list.add((ImageView) convertView.findViewById(ivid[i]));
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        for (int i = 0; i < holder.tv_list.size(); i++) {
            holder.tv_list.get(i).setText(String.valueOf(list.get(position).get(tvkey[i])));
        }
        for (int i = 0; i < holder.iv_list.size(); i++) {
            if (mItemClick != null) {
                holder.iv_list.get(i).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClick.mItemClick(v);
                    }
                });
                holder.iv_list.get(i).setTag(position);
            }
            if(mSetImageView!=null){
                mSetImageView.setImageView((String) list.get(position).get(ivkey[i]), holder.iv_list.get(i));
            }
        }
        return convertView;
    }

    @Override
    public List<Map<String, Object>> getList() {
        return list;
    }

    public interface ItemClick {
        void mItemClick(View v);
    }

    public interface SetImageView {
        void setImageView(String url, ImageView iv);
    }

    private class ViewHolder {
        public List<TextView> tv_list = new ArrayList<TextView>();
        public List<ImageView> iv_list = new ArrayList<ImageView>();
    }

}
