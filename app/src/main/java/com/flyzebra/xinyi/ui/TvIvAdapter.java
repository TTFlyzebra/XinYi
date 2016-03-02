package com.flyzebra.xinyi.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TvIvAdapter extends BaseAdapter{
    private List<Map<String, Object>> list;
    private int idListview;
    private int tvid[];
    private String tvkey[];
    private int ivid[];
    private String ivkey[];
    private ItemClick mItemClick;
    private SetImageView mSetImageView;
    private Context context;
    // 这义回调接口

    /**
     * 自定义通用包含TextView ImageVIew的Adapter 支挂回调
     *
     */
    public TvIvAdapter(Context context, List<Map<String, Object>> list, int idListview,
                       int tvid[], String tvkey[], int ivid[], String ivkey[],ItemClick mItemClick,SetImageView setImageView) {
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

    public interface ItemClick {
        public void mItemClick(View v);
    }

    public interface SetImageView {
        public void setImageView(String url,ImageView iv);
    }

    private class ViewHolder {
        public List<TextView> tv = new ArrayList<TextView>();
        public List<ImageView> iv = new ArrayList<ImageView>();
    }


    @Override
    public int getCount() {
        return list.size();
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
            for (int i = 0; i < tvid.length; i++) {
                holder.tv.add((TextView) convertView.findViewById(tvid[i]));
            }
            for (int i = 0; i < ivid.length; i++) {
                holder.iv.add((ImageView) convertView.findViewById(ivid[i]));
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        for (int i = 0; i < holder.tv.size(); i++) {
            holder.tv.get(i).setText((String) list.get(position).get(tvkey[i]));
        }
        for (int i = 0; i < holder.iv.size(); i++) {
            if (mItemClick != null) {
                holder.iv.get(i).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClick.mItemClick(v);
                    }
                });
                holder.iv.get(i).setTag(position);
            }
            if(mSetImageView!=null){
                mSetImageView.setImageView((String) list.get(position).get(ivkey[i]), holder.iv.get(i));
            }
        }
        return convertView;
    }

}
