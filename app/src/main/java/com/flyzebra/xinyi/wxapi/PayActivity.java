package com.flyzebra.xinyi.wxapi;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.GetHttp;
import com.flyzebra.xinyi.ui.BaseActivity;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {
    IHttp iHttp = GetHttp.getIHttp();
    @Bind(R.id.appay_btn)
    Button appayBtn;

    private IWXAPI api;

    private String testpayurl = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");

    }

    private boolean isPaySupported() {
        return api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    @OnClick(R.id.appay_btn)
    public void onClick() {
        appayBtn.setEnabled(false);
        Toast.makeText(PayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        iHttp.getString(testpayurl, HTTPTAG, new IHttp.HttpResult() {
            @Override
            public void succeed(Object object) {
                try {
                    if (object != null) {
                        JSONObject json = new JSONObject((String) object);
                        if (null != json && !json.has("retcode")) {
                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = json.getString("appid");
                            req.partnerId = json.getString("partnerid");
                            req.prepayId = json.getString("prepayid");
                            req.nonceStr = json.getString("noncestr");
                            req.timeStamp = json.getString("timestamp");
                            req.packageValue = json.getString("package");
                            req.sign = json.getString("sign");
                            req.extData = "app data"; // optional
                            Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.registerApp("wxb4ba3c02aa476ea1");
                            api.sendReq(req);
                        } else {
                            Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                            Toast.makeText(PayActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("PAY_GET", "服务器请求错误");
                        Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("PAY_GET", "异常：" + e.getMessage());
                    Toast.makeText(PayActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                appayBtn.setEnabled(true);
            }
            @Override
            public void failed(Object object) {
                appayBtn.setEnabled(true);
            }
        });

    }
}
