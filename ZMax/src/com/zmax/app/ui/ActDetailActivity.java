package com.zmax.app.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.model.ActDetail;
import com.zmax.app.model.ActDetailContent;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetActDetailTask;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.ActDetailFirstFragment;
import com.zmax.app.ui.fragment.ActDetailSecondFragment;
import com.zmax.app.ui.fragment.ActDetailThirdFragment;
import com.zmax.app.utils.*;
import eu.inmite.android.lib.dialogs.ProgressDialogFragment;

public class ActDetailActivity extends BaseFragmentActivity {

    String pattern = "#ZMAX客户端#我在ZMAX发现一个很棒的活动【%s,将于%s在%s举行】，更多好玩的活动尽在ZMAX APP，快来下载吧~%s";
    private Button btn_Back, btn_Share;
    private ViewPager pager;
    private ActDetailAdapter adapter;
    private ActDetailContent detailContent;
    private GetActDetailTask getActDetailTask;
    private String city, date;
    private LinearLayout ll_bg;
    private DialogFragment progressDialog;
    private ImageView iv_left, iv_right;
    private int curPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail);
        init(savedInstanceState);
        initHeader();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        if (detailContent != null) {
            Log.i("savedInstanceState  begin");
            outState.putSerializable("detailContent", detailContent);
        }
    }

    private void init(Bundle savedInstanceState) {
        city = getIntent().getStringExtra(Constant.Acts.CITY_KEY);
        date = getIntent().getStringExtra(Constant.Acts.DATE_KEY);

        iv_left = (ImageView) findViewById(R.id.iv_left_pointer);
        iv_right = (ImageView) findViewById(R.id.iv_right_pointer);
        iv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curPosition--;
                pager.setCurrentItem(curPosition);
            }
        });
        iv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curPosition++;
                pager.setCurrentItem(curPosition);
            }
        });
        ll_bg = (LinearLayout) findViewById(R.id.ll_bg);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new ActDetailAdapter(this);
        pager.setAdapter(adapter);

        Fragment fragment = new ActDetailFirstFragment();
        Bundle args = new Bundle();
        args.putString("city", city);
        args.putString("date", date);
        fragment.setArguments(args);
        adapter.addTab(fragment);
        adapter.addTab(new ActDetailSecondFragment());
        adapter.addTab(new ActDetailThirdFragment());
        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                curPosition = i;
                if (i == 0) {
                    iv_right.setVisibility(View.VISIBLE);
                    iv_left.setVisibility(View.GONE);
                } else if (i == 1) {
                    iv_right.setVisibility(View.VISIBLE);
                    iv_left.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    iv_left.setVisibility(View.VISIBLE);
                    iv_right.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        if (!NetWorkHelper.checkNetState(this)) {
            Utility.toastNetworkFailed(this);
            return;
        }
        progressDialog = ProgressDialogFragment.createBuilder(this, getSupportFragmentManager()).setMessage("正在加载中...").setTitle("提示")
                .setCancelable(true).show();
        getActDetailTask = new GetActDetailTask(this, new GetActDetailTask.TaskCallBack() {

            @Override
            public void onCallBack(ActDetail result) {
                if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismissAllowingStateLoss();

                if (result != null && result.status == 200 && result.event != null) {
                    detailContent = result.event;
                    initData(detailContent);
                } else
                    Utility.toastFailedResult(ActDetailActivity.this);

            }
        });

        int actid = getIntent().getIntExtra(Constant.Acts.ID_KEY, -1);

//		if (savedInstanceState != null && savedInstanceState.containsKey("detailContent")) {
//			Log.i("savedInstanceState used");
//			initData((ActDetailContent) savedInstanceState.getSerializable("detailContent"));
//		}
//		else
        if (actid > 0) {
            getActDetailTask.execute(String.valueOf(actid));
            Log.i("savedInstanceState not used");

        }

    }

    public void hideOrShowPointer(boolean isShow) {
        if (isShow) {
            if(curPosition==0) {
                iv_right.setVisibility(View.VISIBLE);
                iv_left.setVisibility(View.GONE);
            }
        } else {
            iv_right.setVisibility(View.GONE);
            iv_left.setVisibility(View.GONE);
        }
    }

    private void initData(ActDetailContent result) {
        Handler handler = new Handler();
        // 延迟刷新界面，确保fragment刷新在activitycreated后
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < adapter.getCount(); i++) {
                    Fragment fragment = adapter.getItem(i);
                    if (fragment instanceof RefreshDataCallBack) {
                        ((RefreshDataCallBack) fragment).onDataRefresh(detailContent);
                    }
                }
            }
        }, 200);

        ImageLoader.getInstance().loadImage(detailContent.poster, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                try {

                    if (Build.VERSION.SDK_INT >= 16) {
                        ll_bg.setBackground(new BitmapDrawable(StackBlurManager.fastblur(ActDetailActivity.this, loadedImage, 11)));
                    } else {
                        ll_bg.setBackgroundDrawable(new BitmapDrawable(StackBlurManager.fastblur(ActDetailActivity.this, loadedImage, 11)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Error e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initHeader() {
        btn_Back = (Button) findViewById(R.id.btn_back);
        btn_Back.setBackgroundResource(R.drawable.top_back_sel);
        btn_Share = (Button) findViewById(R.id.btn_share);
        btn_Share.setVisibility(View.VISIBLE);
        btn_Share.setBackgroundResource(R.drawable.top_share_sel);
        btn_Back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_Share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initShareContent();

            }
        });
	    ((TextView)findViewById(R.id.tv_title)).setText("活动详情");

    }

    private void initShareContent() {
        try {
            Constant.Share.SHARE_CONTENT = String.format(pattern, detailContent.name, DateTimeUtils.friendly_time(detailContent.start_date),
                    city, Constant.Share.SHARE_URL);
            Constant.Share.SHARE_TITLE = "ZMAX活动分享";
            new ShareUtils().showShare(ActDetailActivity.this, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public interface RefreshDataCallBack {

        public void onDataRefresh(ActDetailContent detailContent);

    }
}
