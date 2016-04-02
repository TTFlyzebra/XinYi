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

import com.flyzebra.googleui.SlidingTabLayout;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.utils.ResUtils;

/**
 * Created by FlyZebra on 2016/3/26.
 */
public class PoiFragment extends Fragment {
    private MainActivity activity;
    private IHttp iHttp = MyVolley.getInstance();
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter mPagerAdapter;
    private String[] poiTypeArr;

    public PoiFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setToolbar(1);

        poiTypeArr = ResUtils.getStringArray(activity, R.array.poi_type);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.poi_fragment, null);
        viewPager = (ViewPager) view.findViewById(R.id.poi_vp_01);
        mPagerAdapter = new MyFragmentPagerAdapter(activity.getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.poi_stl_01);
        slidingTabLayout.setViewPager(viewPager);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PoiFragment_type.newInstance(poiTypeArr[position]);
        }

        @Override
        public int getCount() {
            return poiTypeArr == null ? 0 : poiTypeArr.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return poiTypeArr[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
