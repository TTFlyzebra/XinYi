package com.flyzebra.xinyi;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.flyzebra.xinyi.data.ListMapTest;
import com.flyzebra.xinyi.data.User;
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.utils.FlyLog;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public static final String TAG = "com.flyzebra";

    public ApplicationTest() {
        super(Application.class);
    }

    public void testName() throws Exception {
        ListMapTest listMapTest = new ListMapTest();
        List<Map<String, Object>> parentlist = new ArrayList<>();
        List<Map<String, Object>> childList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("LIST", TestHttp.getViewPagerList2());
            parentlist.add(map);
        }
        childList = (List<Map<String, Object>>) parentlist.get(0).get("LIST");
        FlyLog.i("parentlist.get(0).get(LIST))=childList:" + (parentlist.get(0).get("LIST")).equals(childList));
        Map<String, Object> m1 = new HashMap<>();
        m1.put("CHILDMAP", 1);
        childList.add(m1);
        FlyLog.i("parentlist.get(1).get(LIST))=childList:" + (parentlist.get(1).get("LIST")).equals(childList));
        for (int i = 0; i < 5; i++) {
            FlyLog.i(i + "-paretlist.list" + (parentlist.get(i).get("LIST")).toString() + "------childList=" + childList.toString());
        }
        Map<String, Object> m2 = new HashMap<>();
        m2.put("CHILDMAP", 200);
        childList.add(m2);
        for (int i = 0; i < 5; i++) {
            FlyLog.i(i + "-paretlist.list" + (parentlist.get(i).get("LIST")).toString() + "-----childList=" + childList.toString());
        }
        Map<String, Object> m3 = new HashMap<>();
        m3.put("CHILDMAP", 300);
        ((List<Map<String, Object>>) parentlist.get(0).get("LIST")).add(m3);
        for (int i = 0; i < 5; i++) {
            FlyLog.i(i + "-paretlist.list" + (parentlist.get(i).get("LIST")).toString() + "-----childList=" + childList.toString());
        }

    }

    public void testUser() throws IOException, ClassNotFoundException {
        User user = new User(1, "flyzebra", "13888888888");
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("/mnt/sdcard/testuser.txt"));
        out.writeObject(user);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("/mnt/sdcard/testuser.txt"));
        User user1 = (User) in.readObject();
        in.close();
        Log.i("com.flyzebra", user1.getUserId() + "," + user1.getPhoneNum() + "," + user1.getUserName() + "," + user1.getPassword());
    }

    public void testFinal() {
        boolean flag = true;
        FlyLog.i("testFinal=" + flag);
        finalTest.testBoolean(flag);
        flag = false;
        FlyLog.i("testFinal=" + flag);
    }

    public void testOkHttp3() throws IOException {
        FlyLog.i("Math.round(11.5)" + Math.round(11.5f));
        Log.i(TAG, "Math.round(11.5)" + Math.round(11.5d));
        Log.i(TAG, "Math.round(-11.5)" + Math.round(-11.5f));
        Log.i(TAG, "testOkHttp3-------------------------------------->");
        //官网例子
//        GetExample example = new GetExample();
//        String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
//        Log.i("com.flyzebra",response.length()+"----"+response);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo")
                .build();
        //同步GET
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("response isSuccessful false!");
//        Headers responseHeaders = response.headers();
//        for (int i = 0; i < responseHeaders.size(); i++) {
//            System.out.println(responseHeaders.name(i) + ":" + responseHeaders.value(i));
//            Log.i(TAG, responseHeaders.name(i) + ":" + responseHeaders.value(i));
//        }
        String resultStr = response.body().string();
        Log.i(TAG, "同步GET-->" + resultStr);

        //异步GET
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "testOkHttp3->onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("response isSuccessful false!");
//                Headers responseHeaders = response.headers();
//                for (int i = 0; i < responseHeaders.size(); i++) {
//                    System.out.println(responseHeaders.name(i) + ":" + responseHeaders.value(i));
//                    Log.i(TAG, responseHeaders.name(i) + ":" + responseHeaders.value(i));
//                }
                String resultStr = response.body().string();
                Log.i(TAG, "异步GET--->" + resultStr);
            }
        });
    }

    public static class finalTest {
        public static synchronized void testBoolean(final boolean fb) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FlyLog.i("Final Boolean=" + fb);
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FlyLog.i("Final Boolean=" + fb);
                    }
                }
            }).start();
        }
    }

    public class GetExample {
        OkHttpClient client = new OkHttpClient();

        public GetExample(OkHttpClient client) {
            this.client = client;
        }

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)

                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

}