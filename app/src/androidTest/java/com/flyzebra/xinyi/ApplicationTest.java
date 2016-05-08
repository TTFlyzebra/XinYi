package com.flyzebra.xinyi;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.flyzebra.xinyi.utils.FlyLog;

import java.io.IOException;

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

    public void test10To5() {
        for (int i = 0; i < 10; i++) {
            int level = (int) (Math.random() * 50);
            int tempLevel = level;
            StringBuffer setpNum = new StringBuffer();
            while (tempLevel / 5 > 0) {
                setpNum.append(tempLevel % 5);
                tempLevel = tempLevel / 5;
            }
            setpNum.append(tempLevel);
            StringBuffer print = new StringBuffer();
            for (int j = 0; j < setpNum.length(); j++) {
                print.append(setpNum.charAt(setpNum.length() - 1 - j));
            }
            FlyLog.i("<ApplicationTest>test10To5 level=" + level + "," + print.toString() + "," + setpNum.toString());
        }
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