package com.flyzebra.xinyi.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.ImageLoaderConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/29.
 */
public class HomeAcitivy extends BaseActivity {
    private ViewPager viewPager;
    private MyViewPageAdapter myViewPageAdapter;
    //ViewPage List;Key字包含图片名字=name，图片路径=path
    private List<Map<String, Object>> viewPager_list;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base_bt_01.setImageResource(R.drawable.ic_menu_deal_on);
        base_tv_01.setTextColor(getResources().getColor(R.color.menu_select_on));
    }

    @Override
    public void onCreateAndaddView(LinearLayout root) {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image)
                .showImageForEmptyUri(R.drawable.image)
                .showImageOnFail(R.drawable.image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        LayoutInflater lf = LayoutInflater.from(this);
        LinearLayout ll = (LinearLayout) lf.inflate(R.layout.home_view, null);
        root.addView(ll);
        //初始化ViewPager显示的内容
        viewPager_list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("name", "s1");
        map1.put("path", "http://192.168.1.88/ordermeal/images/s1.jpg");
        viewPager_list.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("name", "s2");
        map2.put("path", "http://192.168.1.88/ordermeal/images/s2.jpg");
        viewPager_list.add(map2);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("name", "s3");
        map3.put("path", "http://192.168.1.88/ordermeal/images/s3.jpg");
        viewPager_list.add(map3);

        viewPager = (ViewPager) ll.findViewById(R.id.home_viewpager);
        myViewPageAdapter = new MyViewPageAdapter();
        viewPager.setAdapter(myViewPageAdapter);
    }

    public class MyViewPageAdapter extends PagerAdapter {

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return viewPager_list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * Determines whether a page View is associated with a specific key object
         * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
         * required for a PagerAdapter to function properly.
         *
         * @param view   Page View to check for association with <code>object</code>
         * @param object Object to check for association with <code>view</code>
         * @return true if <code>view</code> is associated with the key object <code>object</code>
         */
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
                    Toast.makeText(HomeAcitivy.this,(String)viewPager_list.get(i).get("name"),Toast.LENGTH_LONG).show();
                }
            });
            container.addView(iv);
            return iv;
        }
    }
}
