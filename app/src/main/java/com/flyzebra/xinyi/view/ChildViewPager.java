package com.flyzebra.xinyi.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.utils.FlyLog;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/25.
 */
public class ChildViewPager extends BaseChildView {

    private int current_item = 0;
    private IHttp iHttp = MyVolley.getInstance();
    private long delayMillis = 5000;
    private ViewPager viewPager;
    private NaviForViewPager countVP;
    private PlaysAdapter adapter;
    private Handler playsHander = new Handler(Looper.getMainLooper());
    private Runnable playsTask = new Runnable() {
        @Override
        public void run() {
            if (list != null && viewPager != null) {
                current_item++;
                if (current_item >= list.size()) {
                    current_item = 0;
                }
                viewPager.setCurrentItem(current_item);
            }
            playsHander.postDelayed(playsTask, delayMillis);
            FlyLog.i("<ChildViewPager>playsTask running.currentItem=" + current_item);
        }
    };

    public ChildViewPager(Context context) {
        super(context);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChildViewPager(Context context, ViewGroup parent, @LayoutRes int ResId) {
        super(context, parent, ResId);
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (ResId == 0) {
            ResId = R.layout.child_viewpager;
        }
        View view = setContentView(ResId);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        countVP = (NaviForViewPager) view.findViewById(R.id.count_vp);
        countVP.setSumItem(list == null ? 0 : list.size());
        adapter = new PlaysAdapter();
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        //下面这句放在onCread()后会立即执行，不知道原因(最后找到原因，当ChildView中的super(context)改为了this(context,null))
        playsHander.postDelayed(playsTask, delayMillis);
        FlyLog.i("<ChildViewPager>playsHander.postDelayed(playsTask,delayMillis):delayMillis=" + delayMillis);
        super.onStart();
    }

    @Override
    public void onStop() {
        if (playsHander != null && playsTask != null) {
            playsHander.removeCallbacks(playsTask);
        }
        FlyLog.i("<ChildViewPager>onStop");
        super.onStop();
    }

    @Override
    public void setData(List<Map<String, Object>> data) {
        super.setData(data);
        if (data != null) {
            countVP.setSumItem(data.size());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public List<Map<String, Object>> addData(List<Map<String, Object>> data) {
        data = super.addData(data);
        countVP.setSumItem(data.size());
        adapter.notifyDataSetChanged();
        FlyLog.i("<ChildViewPager>addData:listsize=" + data.size());
        return data;
    }

    public class PlaysAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
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
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            ImageView iv = new ImageView(context);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iHttp.upImageView(context, (String) list.get(position).get("path"), iv);
            iv.setTag(position);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int i = (int) v.getTag();
//                    //从list中判断点击的图片名称，执行相应动作，执行相应动作，下面为测试，从列表中删除点击的图片
////                    list.get(i).put("path", "http://192.168.1.88/ordermeal/images/aa4.jpg");
//                    list.remove(i);
//                    countVP.setSumItem(list.size());
//                    notifyDataSetChanged();
//                    FlyLog.i("<ChildViewPager><PlaysAdapter>instantiateItem->onClick:i=" + i + ",list.size()=" + list.size());
                }
            });
            container.addView(iv);
            return iv;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            countVP.setCurrentItem(position);
            current_item = position;
            super.setPrimaryItem(container, position, object);
        }
    }
}
