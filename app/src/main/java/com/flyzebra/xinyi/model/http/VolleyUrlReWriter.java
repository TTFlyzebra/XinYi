package com.flyzebra.xinyi.model.http;

import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.flyzebra.xinyi.utils.FlyLog;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * 把Host换成IP地址
 * 如http://www.flyzebra.com/stites换成http://192.168.1.1/stites
 * 在自定义Https的情况下会无法认证证书，所以https不转换
 * Created by Administrator on 2016/5/18.
 */
public class VolleyUrlReWriter implements HurlStack.UrlRewriter{
    @Override
    public String rewriteUrl(String originalUrl) {
        if(originalUrl.indexOf("https://")==0) return originalUrl;
        String reUrl = originalUrl;
        try {
            URL url = new URL(originalUrl);
            String host = url.getHost();
            String IPAddress = GetInetAddress(host);
            if(IPAddress!=null){
                reUrl = originalUrl.replaceFirst(host,IPAddress);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FlyLog.i("<VolleyUrlReWriter>rewriteUrl:reUrl="+reUrl);
        return reUrl;
    }

    public static String GetInetAddress(String  host){
        String IPAddress = null;
        InetAddress ReturnStr1 = null;
        try {
            ReturnStr1 = InetAddress.getByName(host);
            IPAddress = ReturnStr1.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return  IPAddress;
        }
        return IPAddress;
    }
}
