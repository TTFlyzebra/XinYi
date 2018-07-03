package com.flyzebra.xinyi.ui;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.GpsTools;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

public class BaiduMapActivity extends Activity {
	//	private BDLocationListener myListener = new MyLocationListener();
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private String mGPS = null;
	private LocationManager locationManager;

	private LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				FlyLog.i("<BaiduMapActivity>onLocationChanged"+location);
				GoMyGPS(location);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	private List<String> list_provider;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SDKInitializer.initialize(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidumap);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMaxAndMinZoomLevel(18, 18);


//		Intent intent = getIntent();
//		mGPS = intent.getStringExtra("GPS");
//		mGPS = "113.808237,23.056401GPS:113.808237,23.056401";
//		GoMyGPS(mGPS);

		// GPS
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		list_provider = locationManager.getProviders(true);
		for(String provider:list_provider){
			FlyLog.i("<BaiduMapActivity>"+provider);
			locationManager.requestLocationUpdates(provider, 1000, 1, mLocationListener);
		}

//		locationManager.requestLocationUpdates("gps", 1000, 1, mLocationListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		//取消定位监听
		locationManager.removeUpdates(mLocationListener);
		super.onDestroy();
	}

	private void GoMyGPS(Location location) {
		double lng = location.getLongitude();
		double lat = location.getLatitude();
		LatLng latlng = new LatLng(lat, lng);
		LatLng mLatLng = GpsTools.ConvertGpsToBaidu(latlng);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 构造定位数据
		MyLocationData locData = new MyLocationData.Builder().accuracy(30)
				.direction(0).latitude(mLatLng.latitude)
				.longitude(mLatLng.longitude).build();
		FlyLog.i("<BaiduMapActivity>GoMyGPS"+mLatLng.toString());
		// 设置定位数据
		mBaiduMap.setMyLocationData(locData);
		// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
		BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
		MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, false,customMarker);
		mBaiduMap.setMyLocationConfigeration(config);
	}

	private void GoMyGPS(String mGPS) {
		if (mGPS.equals("") == false&&mGPS!=null) {
			mMapView.showZoomControls(false);
			mBaiduMap = mMapView.getMap();
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			mBaiduMap.setMaxAndMinZoomLevel(18, 18);
			String gpss[] = mGPS.split("GPS:");
			String gps[] = gpss[0].split(",");
			double lng = Double.parseDouble(gps[0]);
			double lat = Double.parseDouble(gps[1]);
			LatLng latlng = new LatLng(lat, lng);
			LatLng mLatLng = GpsTools.ConvertGpsToBaidu(latlng);
			// 开启定位图层
			mBaiduMap.setMyLocationEnabled(true);
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder().accuracy(30)
					.direction(0).latitude(mLatLng.latitude)
					.longitude(mLatLng.longitude).build();
			FlyLog.i("<BaiduMapActivity>GoMyGPS"+mLatLng.toString());
			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
			BitmapDescriptor customMarker = BitmapDescriptorFactory
					.fromResource(R.drawable.icon_geo);
			MyLocationConfiguration config = new MyLocationConfiguration(
					MyLocationConfiguration.LocationMode.FOLLOWING, false,
					customMarker);
			mBaiduMap.setMyLocationConfigeration(config);
			// 当不需要定位图层时关闭定位图层
//			 mBaiduMap.setMyLocationEnabled(true);
		}
	}

}
