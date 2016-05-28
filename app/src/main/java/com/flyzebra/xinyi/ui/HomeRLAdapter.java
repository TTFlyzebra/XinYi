package com.flyzebra.xinyi.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.URLS;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.SelectHttp;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.SerializableMap;
import com.flyzebra.xinyi.view.AttrChildGridView;
import com.flyzebra.xinyi.view.ChildGridView;
import com.flyzebra.xinyi.view.ChildViewPager;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by FlyZebra on 2016/3/26.
 */
public class HomeRLAdapter extends RecyclerView.Adapter<ViewHolder> implements IAdapter {
    private List<Map<String, Object>> list;
    private Context context;
    private IHttp iHttp = SelectHttp.getIHttp();

    public HomeRLAdapter(Context context, List<Map<String, Object>> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder hodler = null;
        switch (viewType) {
            case H_VIEWPAGER_SHOP:
                final ChildViewPager childViewPager = new ChildViewPager(context);
                childViewPager.setOnItemkClick(new ChildViewPager.OnItemClick() {
                    @Override
                    public void onItemClick(Map<String, Object> data, View v) {
                        startIntent(context, ShopInfoActivity.class, data, ShopInfoActivity.SHOP, childViewPager);
                    }
                });
                hodler = new ViewPageHolder(childViewPager);
                break;
            case H_GRIDVIEW_HOTS:
                ChildGridView hotsGridView = new ChildGridView(context)
                        .init(R.layout.product_item_02,
                                new int[]{R.id.p_item_02_tv_01},
                                new String[]{IAdapter.PR1_NAME},
                                new int[]{R.id.p_item_02_iv_01},
                                new String[]{IAdapter.PR1_IMGURL})
                        .setColumn(2)
                        .setTitle("热销产品")
                        .setTitleImage(R.drawable.ic_hot);

                hotsGridView.setOnItemClick(new ChildGridView.OnItemClick() {
                    @Override
                    public void onItemClidk(Map<String, Object> data, View v) {
//                                FlyLog.i("<MultiRListAdapter> childGridView.setOnItemClick:data=" + data);
                        startIntent(context, ProductInfoActivity.class, data, ProductInfoActivity.PRODUCT, v);
                    }
                });
                hodler = new GridViewHolder(hotsGridView);
                break;
            case H_GRIDVIEW_NEWS:
                AttrChildGridView attrChildGridView = new AttrChildGridView(context);
                attrChildGridView.init(R.layout.product_item_01,
                        new int[]{R.id.p_item_01_tv_01, R.id.p_item_01_tv_02, R.id.p_item_01_tv_03},
                        new String[]{IAdapter.PR1_NAME, IAdapter.PR1_DESCRIBE, IAdapter.PR1_PRICE},
                        new int[]{R.id.p_item_01_iv_01},
                        new String[]{IAdapter.PR1_IMGURL})
                        .setColumn(1)
                        .setTitle("新品上市");
                attrChildGridView.setOnItemClick(new ChildGridView.OnItemClick() {
                    @Override
                    public void onItemClidk(Map<String, Object> data, View v) {
//                                FlyLog.i("<MultiRListAdapter> childGridView.setOnItemClick:data=" + data);
                        startIntent(context, ProductInfoActivity.class, data, ProductInfoActivity.PRODUCT, v);
                    }
                });
                hodler = new GridViewHolder(attrChildGridView);
                break;
            case H_GRIDVIEW_TIMESHOP:
                ChildGridView timeShopGridView = new ChildGridView(context)
                        .init(R.layout.product_item_03,
                                new int[]{R.id.p_item_03_tv_01, R.id.p_item_03_tv_02, R.id.p_item_03_tv_03},
                                new String[]{IAdapter.PR1_NAME, IAdapter.PR1_DESCRIBE, IAdapter.PR1_PRICE},
                                new int[]{R.id.p_item_03_iv_01},
                                new String[]{IAdapter.PR1_IMGURL})
                        .setColumn(1)
                        .setTitle("限时抢购")
                        .setTitleImage(R.drawable.ic_hot);
                timeShopGridView.setOnItemClick(new ChildGridView.OnItemClick() {
                    @Override
                    public void onItemClidk(Map<String, Object> data, View v) {
//                                FlyLog.i("<MultiRListAdapter> childGridView.setOnItemClick:data=" + data);
                        startIntent(context, ProductInfoActivity.class, data, ProductInfoActivity.PRODUCT, v);
                    }
                });
                hodler = new GridViewHolder(timeShopGridView);
                break;
        }
        return hodler;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ViewPageHolder) {
            ChildViewPager view = ((ChildViewPager) holder.itemView);
            view.setShowImageSrc(new ChildViewPager.ShowImageSrc() {
                @Override
                public void setImageSrcWithUrl(Map data, ImageView iv) {
                    iHttp.upImageView(context, URLS.URL + data.get(IAdapter.SHOP_IMGURL), iv, R.drawable.load_480_320);
                }
            });
            view.setData((List<Map<String, Object>>) list.get(position).get(DATA));
            holder.itemView.setTag(position);
//            FlyLog.i("<MultiRListAdapter>onBindViewHolder->" + (List<Map<String, Object>>) list.get(position).get(DATA));
        } else if (holder instanceof GridViewHolder) {
            final ChildGridView view = (ChildGridView) holder.itemView;
            view.setShowImageSrc(new ChildGridView.ShowImageSrc() {
                @Override
                public void setImageSrcWithUrl(String url, ImageView iv) {
                    iHttp.upImageView(context, URLS.URL + url, iv);
                }
            });
            view.setData((List<Map<String, Object>>) list.get(position).get(DATA));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (Integer) list.get(position).get(TYPE);
    }

    @Override
    public List getList() {
        return list;
    }

    public class ViewPageHolder extends ViewHolder {
        public ViewPageHolder(View itemView) {
            super(itemView);
        }
    }

    public class GridViewHolder extends ViewHolder {
        public GridViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void startIntent(Context context, Class cls, Map map, String key, View view) {
        FlyLog.i("<HomeRLAdapter>startIntent:map"+map.toString());
        Intent intent = new Intent(context, cls);
        SerializableMap serializableMap = new SerializableMap();
        serializableMap.setMap(map);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, serializableMap);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view != null) {
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, view, "IMAGE01").toBundle());
        } else {
            context.startActivity(intent);
        }
    }

}
