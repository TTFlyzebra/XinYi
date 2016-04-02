package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
 * Created by Administrator on 2016/3/26.
 */
public class PoiFragment extends Fragment {
    private static final String HTTPTAG = "PoiFragment" + Math.random();
    private MainActitity actitity;
    private IHttp iHttp = MyVolley.getInstance();
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String[] poiTypeArr;

    public PoiFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actitity = (MainActitity) getActivity();
        poiTypeArr = ResUtils.getStringArray(actitity, R.array.poi_type);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(actitity, R.layout.poi_fragment, null);
        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.poi_stl_01);
        viewPager = (ViewPager) view.findViewById(R.id.poi_vp_01);
        slidingTabLayout.setViewPager(viewPager);
        return view;
    }

    @Override
    public void onDestroy() {
        iHttp.cancelAll(HTTPTAG);
        super.onDestroy();
    }

    private class MyFragFragmentViewPgaer extends FragmentPagerAdapter {

        public MyFragFragmentViewPgaer(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return poiTypeArr == null ? 0 : poiTypeArr.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return poiTypeArr[position];
        }
    }
}
