package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.ImageLoaderConfig;
import com.flyzebra.xinyi.view.AutoSizeWithChildViewPager;
import com.flyzebra.xinyi.view.CountItemForViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/29.
 */
public class HomeAcitivy extends BaseActivity {
    private AutoSizeWithChildViewPager viewPager;
    private MyViewPageAdapter myViewPageAdapter;
    //ViewPage List;Key字包含图片名字=name，图片路径=path
    private List<Map<String, Object>> viewPager_list;
    private DisplayImageOptions options;

    private CountItemForViewPager countItemForViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base_bt_01.setImageResource(R.drawable.ic_menu_deal_on);
        base_tv_01.setTextColor(getResources().getColor(R.color.menu_select_on));
    }

    @Override
    public void onCreateAndaddView(LinearLayout root) {
        options = ImageLoaderConfig.getDisplayImageOptions(R.drawable.image,R.drawable.image,R.drawable.image);

        LayoutInflater lf = LayoutInflater.from(this);
        LinearLayout ll = (LinearLayout) lf.inflate(R.layout.home_view, null);
        root.addView(ll);
        //初始化ViewPager显示的内容
        viewPager_list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("name", "1");
        map1.put("path", "http://192.168.1.88/ordermeal/images/aa1.jpg");
        viewPager_list.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("name", "2");
        map2.put("path", "http://192.168.1.88/ordermeal/images/aa2.jpg");
        viewPager_list.add(map2);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("name", "3");
        map3.put("path", "http://192.168.1.88/ordermeal/images/aa3.jpg");
        viewPager_list.add(map3);
        Map<String, Object> mp4 = new HashMap<String, Object>();
        mp4.put("name", "4");
        mp4.put("path", "http://192.168.1.88/ordermeal/images/aa3.jpg");
        viewPager_list.add(mp4);

        viewPager = (AutoSizeWithChildViewPager) ll.findViewById(R.id.home_viewpager);
        myViewPageAdapter = new MyViewPageAdapter();
        viewPager.setAdapter(myViewPageAdapter);
        //自定义ViewPager导航条
        countItemForViewPager = (CountItemForViewPager) ll.findViewById(R.id.home_civp);
        countItemForViewPager.setSumItem(viewPager_list.size());
        countItemForViewPager.setCurrentItem(1);
    }

    public class MyViewPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return viewPager_list.size();
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
            LayoutInflater lf = LayoutInflater.from(HomeAcitivy.this);
            ImageView iv = (ImageView) lf.inflate(R.layout.iamgeview, null);
            ImageLoader.getInstance().displayImage((String)viewPager_list.get(position).get("path"), iv, options);
            iv.setTag(position);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag();
                    //从list中判断点击的图片名称，执行相应动作，执行相应动作
                    Toast.makeText(HomeAcitivy.this,(String)viewPager_list.get(i).get("name"),Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(iv);
            return iv;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            countItemForViewPager.setCurrentItem(position + 1);
        }
    }
}
