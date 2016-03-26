package com.flyzebra.xinyi.utils;

import com.flyzebra.xinyi.fly.FlyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtils {
	
	public static String MapToJsonString(Map<?, ?> map) throws JSONException{
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toString();
	}

	public static void getList(List<Map<String, Object>> list, JSONObject jsonObject, String jsonKey) {
		try {
			JSONArray jsonArray = null;
			if (jsonKey == null) {
				jsonArray = new JSONArray(jsonObject);
			} else {
				jsonArray = jsonObject.getJSONArray(jsonKey);
			}
			for(int i= 0;i<jsonArray.length();i++){
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				Map<String,Object> map = new HashMap<String,Object>();
				Iterator<String> iterator = jsonObject2.keys();
				while(iterator.hasNext()){
					String json_key = iterator.next();
					Object json_value = jsonObject2.get(json_key);
					if(json_value==null){
						json_value="";
					}
					map.put(json_key, json_value);
				}
				list.add(map);
			}
		} catch (JSONException e) {
			FlyLog.i("JsonUtils->getList-->JSONException:" + e.toString());
			e.printStackTrace();
		}
	}

	public static Map<String, Object> getMap(String jsonList, String jsonKey) {
		Map<String, Object> map = null;
		try {
			JSONObject jsonObject = null;
			if (jsonKey == null) {
				jsonObject = new JSONObject(jsonList);
			} else {
				jsonObject = new JSONObject(jsonList).getJSONObject(jsonKey);
			}
			Iterator<String> iterator = jsonObject.keys();
			map = new HashMap<String, Object>();
			while (iterator.hasNext()) {
				String json_key = iterator.next();
				Object json_value = jsonObject.getInt(json_key);
				if (json_value == null) {
					json_value = "";
				}
				map.put(json_key, json_value);
			}
		} catch (JSONException e) {
			FlyLog.i("JsonUtils->getMap-->JSONException:" + e.toString());
			e.printStackTrace();
		}
		return map;
	}
}
