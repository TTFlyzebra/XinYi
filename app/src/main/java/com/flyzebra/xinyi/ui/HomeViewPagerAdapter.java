package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.ImageUtils;
import com.flyzebra.xinyi.openutils.VolleyUtils;
import com.flyzebra.xinyi.view.CountItemForViewPager;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/1.
 */
public class HomeViewPagerAdapter extends PagerAdapter {
    private List<Map<String, Object>> list;
    private Context context;
    private CountItemForViewPager countItemForViewPager;

    public HomeViewPagerAdapter(Context context, List<Map<String, Object>> list, CountItemForViewPager countItemForViewPager) {
        this.context = context;
        this.list = list;
        this.countItemForViewPager = countItemForViewPager;
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
        LayoutInflater lf = LayoutInflater.from(context);
        ImageView iv = (ImageView) lf.inflate(R.layout.iamgeview, null);
        VolleyUtils.ShowImageView((String) list.get(position).get("path"), iv);
        iv.setTag(position);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag();
                //从list中判断点击的图片名称，执行相应动作，执行相应动作
                Toast.makeText(context, (String) list.get(i).get("name"), Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(iv);
        return iv;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        countItemForViewPager.setCurrentItem(position + 1);
        HomeAcitivy.current_viewpager=position;
        super.setPrimaryItem(container, position, object);
    }
}
