package com.kfc.productcar.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.R;

import java.io.FileNotFoundException;

public class PreviewActivity extends Activity {

	private ClipImageView imageView;
	private Button btnY, btnN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_image);
		imageView = (ClipImageView) findViewById(R.id.src_pic);
		String url = getIntent().getStringExtra("url");
		Uri uri = getIntent().getData();
		Bitmap bmp = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		try {
			bmp = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageView.setImageBitmap(bmp);
		btnN = (Button) findViewById(R.id.btn_crop_n);
		btnN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreviewActivity.this.finish();
			}
		});
		btnY = (Button) findViewById(R.id.btn_crop_y);

		btnY.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 此处获取剪裁后的bitmap
				Bitmap bitmap = imageView.clip();
				BaseActivity.bitmap = bitmap;
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				// byte[] bitmapByte = baos.toByteArray();

				Intent intent = new Intent();
				// intent.putExtra("bitmap", bitmapByte);
				setResult(3, intent);
				finish();

			}
		});
	}


}
