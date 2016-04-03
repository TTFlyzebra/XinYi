package com.flyzebra.xinyi.ui;

import android.content.Context;
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
import com.flyzebra.xinyi.utils.FlyLog;
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
        FlyLog.i("<PoiFragment>onCreate");
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        poiTypeArr = ResUtils.getStringArray(activity, R.array.poi_type);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle onActivityCreated) {
        FlyLog.i("<PoiFragment>onActivityCreated");
        super.onActivityCreated(onActivityCreated);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FlyLog.i("<PoiFragment>onCreateView");
        View view = View.inflate(activity, R.layout.poi_fragment, null);
        viewPager = (ViewPager) view.findViewById(R.id.poi_vp_01);
        mPagerAdapter = new MyFragmentPagerAdapter(activity.getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
//        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.poi_stl_01);
        activity.toolBar_stl.setVisibility(View.VISIBLE);
        activity.toolBar_stl.setViewPager(viewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FlyLog.i("<PoiFragment>onViewCreated");
        activity.setToolbar(1);
    }

    @Override
    public void onDestroyView() {
        FlyLog.i("<PoiFragment>onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        FlyLog.i("<PoiFragment>onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        FlyLog.i("<PoiFragment>onDetach");
        super.onDetach();
    }

    @Override
    public void onStart() {
        FlyLog.i("<PoiFragment>onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        FlyLog.i("<PoiFragment>onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        FlyLog.i("<PoiFragment>onDestroy");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        FlyLog.i("<PoiFragment>onSaveInstanceState");
        super.onSaveInstanceState(outState);
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
