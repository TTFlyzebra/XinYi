package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.UILImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

/*
*list的Key值约定
* imgaepaht-图像的路径，如http://127.0.0.1/image/1.png
* name-名称
* price-价格
 */
public class HomeGridViewAdapter extends BaseAdapter {
	private final String TV01 = "name";
	private final String TV02 = "price";
	private final String IV01 = "imagepath";
	private Context context;
	private class ViewHolder {
		public TextView tv01 = null;
		public TextView tv02 = null;
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
			holder.iv01 = (ImageView) convertView.findViewById(R.id.iv01);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv01.setText((String) list.get(position).get(TV01));
		holder.tv02.setText(String.valueOf(list.get(position).get(TV02)));
		ImageLoader.getInstance().displayImage((String) list.get(position).get(IV01),
				holder.iv01, UILImageUtils.getDisplayImageOptions(R.drawable.image, R.drawable.image, R.drawable.image));
		return convertView;
	}

}
