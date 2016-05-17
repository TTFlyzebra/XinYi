package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.utils.JsonUtils;
import com.flyzebra.xinyi.view.RefreshRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * FlyZebra
 * Created by FlyZebra on 2016/4/2.
 */
public class ProductFragment_type extends BaseFragment {
    private static final String ARG_TYPE = "TYPE";
    private static final String ARG_RESID = "RESID";
    @Bind(R.id.poi_type_rv_01)
    RefreshRecyclerView poiTypeRv01;
    @Bind(R.id.newwork_error)
    LinearLayout newworkError;
    private int ptype_id;
    private int ResID;
    private MainActivity activity;
    private List<Map<String, Object>> list;
    private ProductAdapter mAdapter;
    private View rootView;

    public ProductFragment_type() {
    }

    public static ProductFragment_type newInstance(int ptype_id, int ResID) {
        Bundle args = new Bundle();
        ProductFragment_type fragment = new ProductFragment_type();
        args.putInt(ARG_TYPE, ptype_id);
        args.putInt(ARG_RESID, ResID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ptype_id = bundle.getInt(ARG_TYPE);
            ResID = bundle.getInt(ARG_RESID);
        }
        HTTPTAG = HTTPTAG + ptype_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(activity).inflate(R.layout.fragment_product_type, container, false);
        ButterKnife.bind(this, rootView);

        poiTypeRv01.setId(ResID);
        poiTypeRv01.setLayoutManager(new LinearLayoutManager(activity));
        list = iHttp.readListFromCache(Constant.URL_PRT + "/type/" + ptype_id);
        if (list == null) {
            list = new ArrayList<>();
        }
        mAdapter = new ProductAdapter(activity, list);
        poiTypeRv01.setAdapter(mAdapter);
        //从磁盘读出数据
        //从网络更新数据
        iHttp.upRecyclerViewData(Constant.URL_PRT + "/type/" + ptype_id, list, mAdapter, HTTPTAG);
        poiTypeRv01.setListenerTopRefresh(new RefreshRecyclerView.ListenerTopRefresh() {
            @Override
            public void onRefrsh(View view) {
                upThisFragmentData();
            }
        });
        poiTypeRv01.setListenerLastItem(new RefreshRecyclerView.ListenerLastItem() {
            @Override
            public void onLastItem() {
            }
        });

        newworkError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upThisFragmentData();
            }
        });
        return rootView;
    }

    private void upThisFragmentData() {
        iHttp.getString(Constant.URL_PRT + "/type/" + ptype_id, HTTPTAG, new IHttp.HttpResult() {
            @Override
            public void succeed(Object object) {
                if (object != null && !object.equals("")) {
                    try {
                        List<Map<String, Object>> tempList = JsonUtils.json2List(new JSONArray(object.toString()));
                        list.clear();
                        list.addAll(tempList);
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                poiTypeRv01.setVisibility(View.VISIBLE);
                newworkError.setVisibility(View.GONE);
                poiTypeRv01.refreshSuccess();
            }

            @Override
            public void failed(Object object) {
                poiTypeRv01.refreshFailed();
                poiTypeRv01.setVisibility(View.GONE);
                newworkError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
