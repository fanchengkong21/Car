package com.kfc.productcar.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kfc.productcar.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QrcodeActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = QrcodeActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private QRCodeView mQRCodeView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.inject(this);

        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpotDelay(50);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startSpotDelay(50);
        mQRCodeView.startCamera();
//      mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("code", result);
        setResult(RESULT_OK, intent);
        finish();
        vibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }
    @OnClick({R.id.qrcode_back, R.id.qrcode_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qrcode_back:
                finish();
                break;
            case R.id.qrcode_photo:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false),
                        REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mQRCodeView.showScanRect();
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(QrcodeActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("code", result);
                        setResult(RESULT_OK, intent);
                        finish();
                        //Toast.makeText(QrcodeActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }



}


