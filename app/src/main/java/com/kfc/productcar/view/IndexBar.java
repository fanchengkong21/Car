package com.kfc.productcar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jackiechan on 16/1/12.
 * 索引导航条
 */
public class IndexBar extends View {
    //用于显示的字母
    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"
            , "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint paint;//画笔
    private int cellWidth, cellHeight;//每个字所占的格子的宽高
    private OnIndexBarSelectListener onIndexBarSelectListener;
    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setColor(Color.RED);//颜色
        paint.setTextSize(40);//字体大小
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置为错题
    }

    /**
     * 绘制具体界面
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {
            if (cellWidth == 0) cellWidth = getMeasuredWidth();
            if (cellHeight == 0) cellHeight = getMeasuredHeight() / 26;//获取宽高度
            String word = words[i];
            //x 一直不变都是格子宽度的一半减去字体宽度的一半
            //y格子所在索引乘以格子的高度 加上格子高度的一半再加上字体高度的一半
            float x = 0;
            float y = 0;
            float wordWidth = paint.measureText(word);//获取字母的宽度
            x = cellWidth / 2 - wordWidth / 2;
            Rect rect = new Rect();
            paint.getTextBounds(word, 0, word.length(), rect);
            float wordHeight = rect.height();// 获取字体的高度
            y = i * cellHeight + cellHeight / 2 + wordHeight / 2;
            paint.setColor(Color.parseColor(lastIndex==i?"#666000":"#FF0000"));
            canvas.drawText(word, x, y, paint);
        }
    }

    private int lastIndex=-1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN://按下去
            case MotionEvent.ACTION_MOVE://滑动
                float y = event.getY();//获取按下去的坐标点
                int index = (int) (y / cellHeight);//获取到触摸时候的处于的索引值
                if (index == lastIndex||index>=words.length) {//如果两次滑动的时候的索引一致或者是当前索引大于总长度(为什么出现大于,因为数据运算的偏差,导致最下面有一点点不在范围内的区域)
                    return true;
                } else {
                    lastIndex = index;
                    Log.e("自定义", "" + words[index]);
                    if (onIndexBarSelectListener != null) {
                        onIndexBarSelectListener.onIndexBarSelect(words[index]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP://抬起
                lastIndex=-1;
                break;
        }

        invalidate();//刷新界面
        return true;
    }

    public void setOnIndexBarSelectListener(OnIndexBarSelectListener onIndexBarSelectListener) {
        this.onIndexBarSelectListener = onIndexBarSelectListener;
    }

    public  interface  OnIndexBarSelectListener{
        void onIndexBarSelect(String word);
    }

}
