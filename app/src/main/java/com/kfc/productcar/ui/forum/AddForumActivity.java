package com.kfc.productcar.ui.forum;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.adapter.TheamAdapter;
import com.kfc.productcar.bean.Theam;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Ly on 2016/12/5.
 */

public class AddForumActivity extends BaseActivity {
    @InjectView(R.id.menu_click)
    TextView menuClick;
    @InjectView(R.id.edt_title)
    EditText edtTitle;
    @InjectView(R.id.edt_content)
    EditText edtContent;
    @InjectView(R.id.txt_editnumber)
    TextView edtNumber;
    @InjectView(R.id.txt_all_theam)
    TextView allTheam;
    @InjectView(R.id.img_picture)
    ImageView imgPicture;
    private String typeid;
    private static final String PHOTO_FILE_NAME = "/post.jpg";
    private boolean ifTakePhoto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_add);
        initTitle(this, R.id.menu_title, "发布新帖子");
        ButterKnife.inject(this);
        initBtnBack(this);
        menuClick.setText("发布");
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        edtTitle.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void initEvents() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RequestParams params = new RequestParams();
                        params.put("fid", "2");
                        HttpClient.post(CarConfig.GETTHREADCLASS, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String string = new String(responseBody);
                                try {
                                    JSONObject object = new JSONObject(string);
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objectString = array.getJSONObject(i);
                                        Gson gson = new Gson();
                                        theam = gson.fromJson(objectString.toString(), Theam.class);
                                        list.add(theam);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        }, AddForumActivity.this);
                    }
                });

            }
        }, 1000);
    }

    @OnClick({R.id.txt_all_theam, R.id.menu_click, R.id.img_picture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_all_theam:
                ForumTheamPopupWindow(allTheam);
                break;
            case R.id.menu_click:
                savePostData(typeid, edtTitle.getText().toString().trim(), edtContent.getText().toString().trim());
                break;
            case R.id.img_picture:
                View dialogview = View.inflate(this, R.layout.photo_choose_dialog, null);
                final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
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
        }
    }

    private Theam theam;
    private TheamAdapter adapter;
    private List<Theam> list = new ArrayList<>();
    private ListView listTheam;

    public void ForumTheamPopupWindow(View view) {
        View contentView = getLayoutInflater().inflate(
                R.layout.activity_forum_list_theam, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        listTheam = (ListView) contentView
                .findViewById(R.id.list_theam);

        adapter = new TheamAdapter(list, AddForumActivity.this);
        listTheam.setAdapter(adapter);
        listTheam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Theam theam = list.get(i);
                if (theam != null) {
                    typeid = theam.getTypeid();
                    allTheam.setText(theam.getName());
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        View content = (TextView) findViewById(R.id.txt_all_theam);
        popupWindow.showAsDropDown(content);

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart = edtTitle.getSelectionStart();
            editEnd = edtTitle.getSelectionEnd();
            edtNumber.setText(temp.length() + "/26");
            if (temp.length() > 26) {
                Toast.makeText(AddForumActivity.this,
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                edtTitle.setText(s);
                edtTitle.setSelection(tempSelection);
                edtTitle.setMaxLines(1);
            }
        }
    };

    private void savePostData(final String typeid, final String subject, final String message) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RequestParams params = new RequestParams();
                        params.put("fid", "2");
                        params.put("typeid", typeid);
                        params.put("subject", subject);
                        params.put("message", message);
                        HttpClient.post(CarConfig.NEWTHREAD, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String string = new String(responseBody);
                                Log.e("发帖", string);
                                try {
                                    JSONObject object = new JSONObject(string);
                                    if (object.getInt("code") == 0) {
                                        NumberDialogWindow(object.getString("msg"));
                                    } else if (object.getInt("code") == 1) {
                                        NumberDialogWindow(object.getString("msg"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        }, AddForumActivity.this);
                    }
                });

            }
        }, 1000);

    }
}
