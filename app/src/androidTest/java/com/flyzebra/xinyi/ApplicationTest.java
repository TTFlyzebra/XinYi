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

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testUser() throws IOException, ClassNotFoundException {
        User user = new User(1,"flyzebra","13888888888");
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("/mnt/sdcard/testuser.txt"));
        out.writeObject(user);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("/mnt/sdcard/testuser.txt"));
        User user1 = (User)in.readObject();
        in.close();
        Log.i("com.flyzebra",user1.getUserId()+","+user1.getPhoneNum()+","+user1.getUserName()+","+user1.getPassword());
    }
}