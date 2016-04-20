package com.flyzebra.xinyi.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.utils.FlyLog;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;


public class ScanRQCodeActivity extends Activity {
    public static final int AC_REQUESTCODE_01 = 11;
    private TextView scan_rq_tv01;
    private ImageView scan_rq_iv01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanrqcode_activity);
        scan_rq_tv01 = (TextView) this.findViewById(R.id.scan_rq_tv01);
        scan_rq_iv01 = (ImageView) this.findViewById(R.id.scan_rq_iv01);
        scan_rq_iv01.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = new Intent(ScanRQCodeActivity.this, CaptureActivity.class);
        intent.setAction(Intents.Scan.ACTION);
//        intent.putExtra(Intents.Scan.WIDTH, dm.widthPixels * 2 / 3);
//        intent.putExtra(Intents.Scan.HEIGHT, dm.widthPixels * 2 / 3);
        startActivityForResult(intent, AC_REQUESTCODE_01);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == AC_REQUESTCODE_01) {
            if (resultCode == RESULT_OK) {
                String text = intent.getStringExtra("CODETEXT");
                FlyLog.i("<ScanRQCodeActivity> R esult scan info:" + text);
                if (text != null) {
                    scan_rq_tv01.setText(text);
                } else {
                    finish();
                }
                Bitmap bm = intent.getParcelableExtra("BITMAP");
                if (bm != null) {
                    scan_rq_iv01.setImageBitmap(bm);
                }
            }
        }
    }
}
