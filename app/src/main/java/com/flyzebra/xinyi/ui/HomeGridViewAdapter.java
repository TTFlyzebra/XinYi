package com.flyzebra.xinyi.ui;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;

public class HomeGridViewAdapter extends BaseAdapter {
	private Context context;
	private class ViewHolder {
		public TextView tv01 = null;
		public TextView tv02 = null;
		public TextView tv03 = null;
		public ImageView iv01 = null;
	}

	private List<Map<String, Object>> list;
	private int idListview;
	public HomeGridViewAdapter(Context context, List<Map<String, Object>> list,int idListview) {
		this.list = list;
		this.idListview = idListview;
		this.context = context;
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
			holder.tv01 = (TextView) convertView.findViewById(R.id.tv01);
			holder.tv02 = (TextView) convertView.findViewById(R.id.tv02);
			holder.tv03 = (TextView) convertView.findViewById(R.id.tv03);
			holder.iv01 = (ImageView) convertView.findViewById(R.id.iv01);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}		
		return convertView;
	}

}
