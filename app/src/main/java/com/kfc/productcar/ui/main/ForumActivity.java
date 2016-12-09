package com.kfc.productcar.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.ForumList;
import com.kfc.productcar.ui.forum.AddForumActivity;
import com.kfc.productcar.ui.forum.ForumDetailsActivity;
import com.kfc.productcar.ui.home.DealerDetailsActivity;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ForumActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    @InjectView(R.id.menu_click)
    TextView menuClick;
    @InjectView(R.id.pullLoadMoreRecyclerViewForum)
    PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    @InjectView(R.id.btn_forum_type)
    TextView ForumType;
    @InjectView(R.id.btn_forum_theam)
    TextView ForumTheam;
    @InjectView(R.id.btn_forum_more)
    TextView ForumMore;

    private int curPage = 1;

    private List<ForumList> list;
    private ForumList forumList;
    private ForumActivity.ForumListAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        initTitle(this, R.id.menu_title, "车友圈");
        ButterKnife.inject(this);
        menuClick.setText("发新帖");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }

        recyclerView = pullLoadMoreRecyclerView.getRecyclerView();
        //recyclerView.setVerticalScrollBarEnabled(true);
        //pullLoadMoreRecyclerView.setRefreshing(false);
        pullLoadMoreRecyclerView.setPullRefreshEnable(false);
        pullLoadMoreRecyclerView.setPushRefreshEnable(true);
        pullLoadMoreRecyclerView.setFooterViewText("加载中……");
        //设置上拉刷新文字颜色
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.gray);
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        pullLoadMoreRecyclerView.setFooterViewBackgroundColor(R.color.main_title);
        pullLoadMoreRecyclerView.setLinearLayout();
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        list = new ArrayList<>();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData("", "", "", "");
    }

    @Override
    public void onLoadMore() {
        curPage = curPage + 1;
        loadData(null, null, null, null);
    }

    private void loadData(final String threadtype, final String dateline, final String orderby, final String filter) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        RequestParams params = new RequestParams();
                        params.put("fid", "2");
                        params.put("threadtype", threadtype);
                        params.put("typeid", "");
                        params.put("dateline", dateline);
                        params.put("orderby", orderby);
                        params.put("page", curPage);

                        params.put("filter", filter);
                        HttpClient.post(CarConfig.FORUMLIST, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String string = new String(responseBody);
                                Log.e("帖子列表", string);
                                try {
                                    JSONObject object = new JSONObject(string);
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objectString = array.getJSONObject(i);
                                        Gson gson = new Gson();
                                        forumList = gson.fromJson(objectString.toString(), ForumList.class);
                                        list.add(forumList);
                                        adapter = new ForumActivity.ForumListAdapter(list, ForumActivity.this);
                                        pullLoadMoreRecyclerView.setAdapter(adapter);


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        }, ForumActivity.this);
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                });

            }
        }, 1000);


    }

    @OnClick({R.id.btn_forum_type, R.id.btn_forum_theam, R.id.btn_forum_more, R.id.menu_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forum_type:
                ForumTypePopupWindow(ForumType, "type");
                break;
            case R.id.btn_forum_theam:
                ForumTypePopupWindow(ForumType, "theam");
                break;
            case R.id.btn_forum_more:
                ForumMorePopupWindow(ForumMore);
                break;
            case R.id.menu_click:
                Intent intent = new Intent(this, AddForumActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void ForumTypePopupWindow(View view, String Identification) {
        View contentView = getLayoutInflater().inflate(
                R.layout.activity_forum_type, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        if (Identification.equals("type")) {
            ((TextView) contentView
                    .findViewById(R.id.type_essence)).setText(getString(R.string.essence));
            ((TextView) contentView
                    .findViewById(R.id.type_newest)).setText(getString(R.string.newest));
            ((TextView) contentView
                    .findViewById(R.id.type_population)).setText(getString(R.string.population));
            ((TextView) contentView
                    .findViewById(R.id.type_hot)).setText(getString(R.string.hot));
            ((TextView) contentView
                    .findViewById(R.id.type_essence)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData(null, null, null, "digest&digest=1");
                    popupWindow.dismiss();
                }
            });
            ((TextView) contentView
                    .findViewById(R.id.type_newest)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData(null, null, "lastpost", "lastpost");
                    popupWindow.dismiss();
                }
            });
            ((TextView) contentView
                    .findViewById(R.id.type_population)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData(null, null, "heats", "heat");
                    popupWindow.dismiss();
                }
            });
            ((TextView) contentView
                    .findViewById(R.id.type_hot)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData(null, null, null, "hot");
                    popupWindow.dismiss();
                }
            });
        } else if (Identification.equals("theam")) {
            ((TextView) contentView
                    .findViewById(R.id.type_essence)).setText(getString(R.string.benz_vehicles));
            ((TextView) contentView
                    .findViewById(R.id.type_newest)).setText(getString(R.string.xinghui_life));
            ((TextView) contentView
                    .findViewById(R.id.type_population)).setText(getString(R.string.meet_ups));
            ((TextView) contentView
                    .findViewById(R.id.type_hot)).setText(getString(R.string.warm_heart));
            ((TextView) contentView
                    .findViewById(R.id.type_essence)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData(null, null, null, "digest&digest=1");
                    popupWindow.dismiss();
                }
            });
            ((TextView) contentView
                    .findViewById(R.id.type_newest)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData(null, null, "lastpost", "lastpost");
                    popupWindow.dismiss();
                }
            });
            ((TextView) contentView
                    .findViewById(R.id.type_population)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData(null, null, "heats", "heat");
                    popupWindow.dismiss();
                }
            });
            ((TextView) contentView
                    .findViewById(R.id.type_hot)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData(null, null, null, "hot");
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        View content = (LinearLayout) findViewById(R.id.linear_forum);
        popupWindow.showAsDropDown(content);

    }

    protected int mScreenWidth;

    public void ForumMorePopupWindow(View view) {
        View contentView = getLayoutInflater().inflate(
                R.layout.activity_forum_more, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        ViewGroup.LayoutParams layoutParams = ((TextView) contentView
                .findViewById(R.id.more_a_week)).getLayoutParams();
        ViewGroup.LayoutParams layoutParamsone = ((TextView) contentView
                .findViewById(R.id.more_one_month)).getLayoutParams();
        ViewGroup.LayoutParams layoutParamsthree = ((TextView) contentView
                .findViewById(R.id.more_three_month)).getLayoutParams();
        layoutParams.width = mScreenWidth / 2;
        layoutParamsone.width = mScreenWidth / 2;
        layoutParamsthree.width = mScreenWidth / 2;
//        ((TextView) contentView
//                .findViewById(R.id.type_essence)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadData(null, null, null, "digest&digest=1");
//                popupWindow.dismiss();
//            }
//        });
//        ((TextView) contentView
//                .findViewById(R.id.type_newest)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadData(null, null, "lastpost", "lastpost");
//                popupWindow.dismiss();
//            }
//        });
//        ((TextView) contentView
//                .findViewById(R.id.type_population)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadData(null, null, "heats", "heat");
//                popupWindow.dismiss();
//            }
//        });
//        ((TextView) contentView
//                .findViewById(R.id.type_hot)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadData(null, null, null, "hot");
//                popupWindow.dismiss();
//            }
//        });

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        View content = (LinearLayout) findViewById(R.id.linear_forum);
        popupWindow.showAsDropDown(content);

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


    public class ForumListAdapter extends RecyclerView.Adapter<ForumActivity.ForumListAdapter.ViewHolder> {

        private List<ForumList> list;
        private Context context;


        public ForumListAdapter(List<ForumList> list, Context context) {
            this.list = list;
            this.context = context;

        }

        @Override
        public ForumActivity.ForumListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ForumActivity.ForumListAdapter.ViewHolder(View.inflate(context, R.layout.forum_list, null));
        }

        @Override
        public void onBindViewHolder(ForumActivity.ForumListAdapter.ViewHolder holder, final int position) {
            //forum_image;
            holder.forum_title.setText(list.get(position).getTitle());
            holder.forum_number.setText(list.get(position).getViewnum());
            holder.forum_commit.setText(list.get(position).getPostnum());
            holder.forum_time.setText(list.get(position).getDateline());
            holder.forum_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SharedPreferences share=
                    Intent intent = new Intent(ForumActivity.this, ForumDetailsActivity.class);
                    intent.putExtra("url", list.get(position).getUrl());
                    intent.putExtra("recommend", list.get(position).getRecommend_add());
                    intent.putExtra("favtimes", list.get(position).getFavtimes());
                    intent.putExtra("tid", list.get(position).getTid());
                    startActivity(intent);
                }
            });
            //Glide.with(context).load(list.get(position).getImg()).into(holder.forum_image);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView forum_image;
            private TextView forum_title, forum_number, forum_commit, forum_time;
            private LinearLayout forum_list;

            public ViewHolder(View convertView) {
                super(convertView);
                forum_image = (ImageView) convertView.findViewById(R.id.forum_img);
                forum_list = (LinearLayout) convertView.findViewById(R.id.forum_list);
                forum_title = (TextView) convertView.findViewById(R.id.forum_title);
                forum_number = (TextView) convertView.findViewById(R.id.forum_number);
                forum_commit = (TextView) convertView.findViewById(R.id.forum_commit);
                forum_time = (TextView) convertView.findViewById(R.id.forum_time);
            }
        }

    }
}
