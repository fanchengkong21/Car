package com.kfc.productcar.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/20  12:28
 * @PackageName com.kfc.productcar.utils
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class CountDownTimerUtils extends CountDownTimer {
    //要显示倒计时的控件
    private TextView mTextView;

    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    /**
     *  固定间隔被调用
     * @param millisUntilFinished:倒计时剩余时间
     */
    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + "秒后重新发送");  //设置倒计时时间
    }

    /**
     *  倒计时完成时被调用
     */
    @Override
    public void onFinish() {
        mTextView.setText("重新获取验证码");
        mTextView.setClickable(true);//重新获得点击
    }

}
