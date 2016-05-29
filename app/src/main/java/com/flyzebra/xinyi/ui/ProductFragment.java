package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.URLS;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by FlyZebra on 2016/3/26.
 */
public class ProductFragment extends BaseFragment {
    private ViewPager viewPager;
    private MyFragmentPagerAdapter mPagerAdapter;
    private List<Map<String, Object>> list;
    private static int currentItem = 0;

    public ProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle onActivityCreated) {
        FlyLog.i("<ProductFragment>onActivityCreated");
        super.onActivityCreated(onActivityCreated);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FlyLog.i("<ProductFragment>onCreateView");
        View view = View.inflate(activity, R.layout.fragment_product, null);
        viewPager = (ViewPager) view.findViewById(R.id.poi_vp_01);
        mPagerAdapter = new MyFragmentPagerAdapter(activity.getSupportFragmentManager());
        list = iHttp.readListFromCache(URLS.URL_PTYPE);
        if(list==null){
            list = new ArrayList<>();
        }
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(currentItem);
        activity.toolBar_stl.setViewPager(viewPager);
//        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.poi_stl_01);
        iHttp.getString(URLS.URL_PTYPE, HTTPTAG, new IHttp.HttpResult() {
            @Override
            public void succeed(Object object) {
                upData(object);
            }
            @Override
            public void failed(Object object) {
            }
        });
        return view;
    }

    private void upData(Object object) {
        if (object != null) {
            try {
                List<Map<String, Object>> tempList = JsonUtils.json2List(new JSONArray(object.toString()));
                if (tempList != null) {
                    list.clear();
                    list.addAll(tempList);
                    if(getActivity()!=null){
                        mPagerAdapter.notifyDataSetChanged();
                    }
                    if (getActivity()!=null){
                        activity.toolBar_stl.setViewPager(viewPager);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FlyLog.i("<ProductFragment>onViewCreated");
    }

    @Override
    public void onStart() {
        FlyLog.i("<ProductFragment>onStart");
        super.onStart();
        activity.setToolbar(1);
    }

    @Override
    public void onStop() {
        FlyLog.i("<ProductFragment>onStop");
        super.onStop();
        currentItem = viewPager.getCurrentItem();
    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter implements IAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ProductFragment_type.newInstance(Integer.valueOf((String) list.get(position).get(IAdapter.PTYPE_ID)), position);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (String) list.get(position).get(IAdapter.PTYPE_NAME);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public List getList() {
            return list;
        }
    }
}
