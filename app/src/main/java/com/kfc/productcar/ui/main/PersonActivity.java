package com.kfc.productcar.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.UserInfo;
import com.kfc.productcar.ui.person.BoundPhoneActivity;
import com.kfc.productcar.ui.person.CarFriendsActivity;
import com.kfc.productcar.ui.person.FunctionSettingActivity;
import com.kfc.productcar.ui.person.HotLineActivity;
import com.kfc.productcar.ui.person.MessageActivity;
import com.kfc.productcar.ui.person.MyCollectionActivity;
import com.kfc.productcar.ui.person.PersonalDataActivity;
import com.kfc.productcar.utils.Encryption;
import com.kfc.productcar.utils.FileUtil;
import com.kfc.productcar.utils.HttpClient;
import com.kfc.productcar.utils.PreviewActivity;
import com.kfc.productcar.utils.SysFuncManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class PersonActivity extends BaseActivity {
    public final static String SHARED_PREFERENCES_LOGIN = "shared_preferences_login";
    public final static String KEY_LOGIN_USERNAME = "key_login_username";
    public final static String SHARED_PREFERENCES_DEALERID="shared_preferences_dealerid";
    public final static String KEY_DEALERID="key_dealerid";
    @InjectView(R.id.person_image)
    RoundedImageView personImage;
    @InjectView(R.id.person_name)
    TextView personName;
    @InjectView(R.id.person_grade)
    TextView personGrade;
    @InjectView(R.id.person_mileage)
    TextView personMileage;

    private boolean ifTakePhoto = true;
    private String personImg = "";
    private UserInfo userInfo;

    private static final String PHOTO_FILE_NAME = "/temp_photo.jpg";

    @SuppressLint("SdCardPath")
    String pePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        initTitle(this, R.id.menu_title, "个人中心");
        ButterKnife.inject(this);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        //用户名
        String userName = share.getString(KEY_LOGIN_USERNAME, "");
        personName.setText(Encryption.decrypt(userName));
        RequestParams params=new RequestParams();
        HttpClient.post(CarConfig.USERBASEINFO, params, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string=new String(responseBody);
                Log.i("用户信息",string);
                try {
                    JSONObject object=new JSONObject(string);
                    Gson gson=new Gson();
                    userInfo = gson.fromJson(object.toString(), UserInfo.class);
                    loadData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        },PersonActivity.this);

    }

    private void loadData() {
        personGrade.setText(userInfo.getData().getGrouptitle().toString());
        Glide.with(this).load(userInfo.getData().getAvatar().toString()).into(personImage);
        personMileage.setText(userInfo.getData().getExtcredits1()+"");
        //personGold.setText(userInfo.getData().getExtcredits2()+"");
    }

    @Override
    protected void initEvents() {

    }

    @OnClick({R.id.person_right, R.id.person_image, R.id.person_data, R.id.person_authentication, R.id.person_friend, R.id.person_representative, R.id.person_settings, R.id.person_collection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_right:
                Intent mintent=new Intent(PersonActivity.this, MessageActivity.class);
                startActivity(mintent);
                break;
            case R.id.person_image:
                View dialogview=View.inflate(this,R.layout.photo_choose_dialog,null);
                final Dialog dialog = new Dialog(this,R.style.transparentFrameWindowStyle);
                dialog.setContentView(dialogview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                Window window = dialog.getWindow();
                window.setWindowAnimations(R.style.main_menu_animstyle);
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.x = 0;
                wl.y = getWindowManager().getDefaultDisplay().getHeight();
                wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
                wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog.onWindowAttributesChanged(wl);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                TextView photo_camera = (TextView) dialogview.findViewById(R.id.photo_camera);
                TextView photo_selected = (TextView) dialogview.findViewById(R.id.photo_selected);
                TextView photo_cancel = (TextView) dialogview.findViewById(R.id.photo_cancel);
                photo_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                   public void onClick(View view) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                                Environment.getExternalStorageDirectory(),
                                PHOTO_FILE_NAME)));
                        startActivityForResult(intent, 2);
                        dialog.dismiss();
                    }
                });
                photo_selected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ifTakePhoto = false;
                        Intent intent = new Intent(Intent.ACTION_PICK, null);

                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 1);
                        dialog.dismiss();
                    }
                });
                photo_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.person_data:
                Intent intent=new Intent(PersonActivity.this, PersonalDataActivity.class);
                startActivity(intent);
                break;
            case R.id.person_authentication:
                Intent boundintent=new Intent(PersonActivity.this, BoundPhoneActivity.class);
                startActivity(boundintent);
                break;
            case R.id.person_friend:
                Intent fintent=new Intent(PersonActivity.this, CarFriendsActivity.class);
                fintent.putExtra("icon",userInfo.getData().getAvatar().toString());
                startActivity(fintent);
                break;
            case R.id.person_representative:
                SharedPreferences share = getSharedPreferences(
                        SHARED_PREFERENCES_DEALERID, Context.MODE_PRIVATE);
                Intent rintent=new Intent(PersonActivity.this, HotLineActivity.class);
                rintent.putExtra("dealerid",  share.getInt(KEY_DEALERID,-1));
                startActivity(rintent);
                break;
            case R.id.person_settings:
                Intent sintent=new Intent(PersonActivity.this, FunctionSettingActivity.class);
                startActivity(sintent);
                break;
            case R.id.person_collection:
                Intent cintent=new Intent(PersonActivity.this, MyCollectionActivity.class);
                startActivity(cintent);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            startPhotoZoom(data.getData());

        } else if (requestCode == 2) {

            if (hasSdcard()) {
                File temp = new File(Environment.getExternalStorageDirectory()
                        + PHOTO_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
            }

        } else if (resultCode == 3 && requestCode == 3) {
            if (data != null) {
                setPicToView(data);
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent(PersonActivity.this,
                PreviewActivity.class);
        intent.setDataAndType(uri, "image/*");
        startActivityForResult(intent, 3);

    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        if (bitmap != null) {
            personImage.setImageBitmap(bitmap);
            Drawable drawable = new BitmapDrawable(bitmap);
            BitmapDrawable bd = (BitmapDrawable) drawable;
            Bitmap bm = bd.getBitmap();

            personImg = SysFuncManager.SysTime() + ".jpg";
            @SuppressLint("SdCardPath") final String filepath = "/mnt/sdcard/ProductCarHead/" + personImg;
            //
            @SuppressLint("SdCardPath") String folderpath = "/mnt/sdcard/ProductCarHead/";
            File file = new File(folderpath);

            if (!file.exists()) {
                file.mkdir();
            }

            FileUtil fileUtil = new FileUtil(this);
            fileUtil.saveBitmapToApp(filepath, bm);
            pePath = "/mnt/sdcard/ProductCarHead/" + personImg;
            Log.i("上传图像地址",pePath);
            RequestParams params=new RequestParams();
            File file1=new File(pePath);
            try {
                params.put("Filedata",file1);
                HttpClient.post(CarConfig.POSTPHOTO, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String string=new String(responseBody);
                        Log.i("上传图片",string);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                },PersonActivity.this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * Double click exit function
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 1500);
        } else {
            finish();
            System.exit(0);
        }
    }
}
