package com.flyzebra.xinyi;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.flyzebra.xinyi.data.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
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

    public void testOkHttp3() throws IOException {
        Log.i(TAG, "Math.round(11.5)" + Math.round(11.5f));
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
}