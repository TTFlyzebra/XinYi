package com.flyzebra.xinyi.view;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/25.
 */
public interface IChildView {
    void onCreate();

    void onStart();

    void onStop();

    void onDestory();

    List<Map<String, Object>> getData();

    void setData(List<Map<String, Object>> list);

    List<Map<String, Object>> addData(List<Map<String, Object>> list);
}
