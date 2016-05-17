package com.flyzebra.xinyi.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/17.
 */
public class SerializableMap implements Serializable {
    private Map<String, Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
