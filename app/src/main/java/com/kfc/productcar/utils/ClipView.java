package com.kfc.productcar.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ClipView extends View {
	public static final int BORDERDISTANCE = 100;

	private Context context;

	public ClipView(Context context) {
		this(context, null);
		this.context = context;
	}

	public ClipView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
	}

	public ClipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.context = context;
	}
	// 画周围阴影实现
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = this.getWidth();
		int height = this.getHeight();
		Paint mPaint = new Paint();
		// 边框长度，据屏幕左右边缘50px
		int borderlength = width - BORDERDISTANCE * 2;
		mPaint.setColor(0xaa000000);
		// 以下绘制透明暗色区域
		canvas.drawRect(0, 0, width, (height - borderlength) / 2, mPaint); // top
		canvas.drawRect(0, (height + borderlength) / 2, width, height, mPaint); // bottom
		canvas.drawRect(0, (height - borderlength) / 2, BORDERDISTANCE,
				(height + borderlength) / 2, mPaint);// left
		canvas.drawRect(borderlength + BORDERDISTANCE,
				(height - borderlength) / 2, width,
				(height + borderlength) / 2, mPaint);// right
		// 以下绘制边框线
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(2.0f);
		canvas.drawLine(BORDERDISTANCE, (height - borderlength) / 2, width
				- BORDERDISTANCE, (height - borderlength) / 2, mPaint);// top
		canvas.drawLine(BORDERDISTANCE, (height + borderlength) / 2, width
				- BORDERDISTANCE, (height + borderlength) / 2, mPaint);// bottom
		canvas.drawLine(BORDERDISTANCE, (height - borderlength) / 2,
				BORDERDISTANCE, (height + borderlength) / 2, mPaint);// left
		canvas.drawLine(width - BORDERDISTANCE, (height - borderlength) / 2,
				width - BORDERDISTANCE, (height + borderlength) / 2, mPaint);// right
	}
}
