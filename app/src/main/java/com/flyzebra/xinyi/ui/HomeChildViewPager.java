package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.fly.FlyLog;
import com.flyzebra.xinyi.utils.HttpUtils;
import com.flyzebra.xinyi.utils.ResourceUtils;
import com.flyzebra.xinyi.view.AutoSizeWithChildViewPager;
import com.flyzebra.xinyi.view.ChildView;
import com.flyzebra.xinyi.view.CountItemForViewPager;

import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/25.
 */
public class HomeChildViewPager extends ChildView {
    private long delayMillis = 5000;
    private AutoSizeWithChildViewPager viewPager;
    private CountItemForViewPager countVP;
    private ViewPagerAdapter adapter;

    private Handler playsHander = new Handler(Looper.getMainLooper());
    private Runnable playsTask = new Runnable() {
        @Override
        public void run() {
            if (adapter != null && viewPager != null) {
                int current_item = adapter.getCurrent_item();
                current_item++;
                if (current_item >= list.size()) {
                    current_item = 0;
                }
                adapter.setCurrent_item(current_item);
                viewPager.setCurrentItem(current_item);
            }
            playsHander.postDelayed(playsTask, delayMillis);
            FlyLog.i("<HomeChildViewPager>playsTask running.currentItem=" + adapter.getCurrent_item());
        }
    };

    public HomeChildViewPager(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void SetLayoutParams() {
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        int padding = (int) ResourceUtils.getDimension(context, R.dimen.childpadding);
        setPadding(padding, padding, padding, padding);
        FlyLog.i("<HomeChildViewPager>SetLayoutParams->padding=" + padding);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        setContentView(context, R.layout.viewpager);
        viewPager = (AutoSizeWithChildViewPager) findViewById(R.id.viewpager);
        list = HttpUtils.getViewPagerList();
        countVP = (CountItemForViewPager) findViewById(R.id.count_vp);
        countVP.setSumItem(list.size());
        adapter = new ViewPagerAdapter(getContext(), list, countVP);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        //下面这句放在onCread()后会立即执行，不知道原因(原因为在ChildView中初始化super(context)的执行顺序
//        playsHander.postDelayed(playsTask,delayMillis);
        playsHander.postDelayed(playsTask, delayMillis);
        FlyLog.i("<HomeChildViewPager>playsHander.postDelayed(playsTask,delayMillis):delayMillis=" + delayMillis);
        super.onStart();
    }

    @Override
    public void onStop() {
        if (playsHander != null && playsTask != null) {
            playsHander.removeCallbacks(playsTask);
        }
        FlyLog.i("<HomeChildViewPager>onStop");
        super.onStop();
    }

    @Override
    public void setData(List<Map<String, Object>> data) {
        super.setData(data);
        countVP.setSumItem(list.size());
        adapter.notifyDataSetChanged();
        FlyLog.i("<HomeChildViewPager>setData:listsize=" + list.size());
    }

    @Override
    public void addData(List<Map<String, Object>> data) {
        super.addData(data);
        countVP.setSumItem(list.size());
        adapter.notifyDataSetChanged();
        FlyLog.i("<HomeChildViewPager>addData:listsize=" + list.size());
    }
}
