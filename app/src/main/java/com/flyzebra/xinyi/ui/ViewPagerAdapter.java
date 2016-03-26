package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.fly.FlyLog;
import com.flyzebra.xinyi.model.Http;
import com.flyzebra.xinyi.model.IHttp;
import com.flyzebra.xinyi.view.CountItemForViewPager;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/1.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private int current_item = 0;
    private List<Map<String, Object>> list;
    private Context context;
    private CountItemForViewPager countItemForViewPager;
    private IHttp iHttp = Http.getInstance();

    public ViewPagerAdapter(Context context, List<Map<String, Object>> list, CountItemForViewPager countItemForViewPager) {
        this.context = context;
        this.list = list;
        this.countItemForViewPager = countItemForViewPager;
    }

    public int getCurrent_item() {
        return current_item;
    }

    public void setCurrent_item(int current_item) {
        this.current_item = current_item;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = (ImageView) LayoutInflater.from(context).inflate(R.layout.home_viewpager_img, null);
        iHttp.upImageView(context, (String) list.get(position).get("path"), iv);
        iv.setTag(position);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag();
                //从list中判断点击的图片名称，执行相应动作，执行相应动作
                FlyLog.i("<ViewPagerAdapter>instantiateItem->onClick:i=" + i);
            }
        });
        container.addView(iv);
        return iv;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        countItemForViewPager.setCurrentItem(position);
        current_item = position;
        super.setPrimaryItem(container, position, object);
    }
}
