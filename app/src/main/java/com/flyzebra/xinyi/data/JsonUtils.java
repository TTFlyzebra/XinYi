package com.flyzebra.xinyi.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
	
	public static String MapToJsonString(Map<?, ?> map) throws JSONException{
		JSONObject jsonObject = new JSONObject(map);
//		JSONArray jsonArray = new JSONArray(map);
//		Iterator<?> keys = map.keySet().iterator();
//		while (keys.hasNext()) {
//			String k = (String) keys.next();
//			Object value = map.get(k);
//			jsonObject.put(k,value);
//		}
//		jsonObject= jsonArray.toJSONObject(jsonArray);
//		jsonObject.put(jsonkey, jsonObject.toString());
		return jsonObject.toString();
	}
	
	public static List<Map<String,Object>> getList(String key,String jsonList){
		List<Map<String,Object>> list =  new ArrayList<Map<String,Object>>();
		try {
			JSONObject jsonObject = new JSONObject(jsonList);
			JSONArray jsonArray = jsonObject.getJSONArray(key);
			list =  new ArrayList<Map<String,Object>>();
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
			e.printStackTrace();
		}
		return list;
	}
	
//	public static Map<String, Object> getMap(String key, String jsonList) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			JSONObject jsonObject = new JSONObject(jsonList).getJSONObject(key);
//			Iterator<String> iterator = jsonObject.keys();
//			while (iterator.hasNext()) {
//				String json_key = iterator.next();
//				Integer json_value = jsonObject.getInt(json_key);
//				if (json_value == null) {
//					json_value = 0;
//				}
//				map.put(json_key, json_value);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return map;
//	}

	public static Map<String, Integer> getMap(String jsonBuymap) {
		Map<String, Integer> map = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonBuymap);
			Iterator<String> iterator = jsonObject.keys();
			map = new HashMap<String, Integer>();
			while(iterator.hasNext()){
				String json_key = iterator.next();
				Object json_value = jsonObject.get(json_key);
				if(json_value==null){
					json_value="";
				}
				map.put(json_key, (Integer) json_value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
}
