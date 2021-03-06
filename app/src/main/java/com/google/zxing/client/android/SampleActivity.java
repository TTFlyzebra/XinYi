package com.google.zxing.client.android;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.utils.ResUtils;


public class SampleActivity extends Activity {
    public static final int AC_REQUESTCODE_01 = 11;
    private final String TAG = "com.flyzebra";
    private TextView ac_main_tv01;
    private Button ac_main_bt01;
    private ImageView ac_main_iv01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxing_sample_activity);
        ac_main_tv01 = (TextView) this.findViewById(R.id.ac_main_tv01);
        ac_main_iv01 = (ImageView) this.findViewById(R.id.ac_main_iv01);
        ac_main_bt01 = (Button) this.findViewById(R.id.ac_main_bt01);
        final DisplayMetrics dm = ResUtils.getMetrices(SampleActivity.this);
        ac_main_bt01.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SampleActivity.this, CaptureActivity.class);
                intent.setAction(Intents.Scan.ACTION);
//                intent.putExtra(Intents.Scan.WIDTH, dm.widthPixels*2/3);
//                intent.putExtra(Intents.Scan.HEIGHT, dm.widthPixels*2/3);
                startActivityForResult(intent, AC_REQUESTCODE_01);
            }
        });
        Intent intent = new Intent(SampleActivity.this, CaptureActivity.class);
        intent.setAction(Intents.Scan.ACTION);
        startActivityForResult(intent, AC_REQUESTCODE_01);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == AC_REQUESTCODE_01) {
            if (resultCode == RESULT_OK) {
                String text = intent.getStringExtra("CODETEXT");
                Log.i(TAG, "intent.getStringExtra()" + text);
                ac_main_tv01.setText(text);
                Bitmap bm = intent.getParcelableExtra("BITMAP");
                ac_main_iv01.setImageBitmap(bm);
            }
        }
    }
}
