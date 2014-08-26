package com.zmax.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import com.zmax.app.R;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.PhoneUtil;
import com.zmax.app.widget.HTML5WebView;

public class WebViewActivity extends BaseActivity {

	HTML5WebView wv_content;
	private TextView tv_title;
	private Context context;
	private String title, type, spf_key, documents_content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.default_layout);
		init();
	}

	private void init() {
		context = this;
		LinearLayout pLayout = new LinearLayout(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		pLayout.setLayoutParams(layoutParams);
		pLayout.setBackgroundColor(getResources().getColor(R.color.white));
		pLayout.setOrientation(LinearLayout.VERTICAL);
		wv_content = new HTML5WebView(this);
		String action = getIntent().getAction();
		pLayout.addView(getHeader(),
				new LayoutParams(LayoutParams.MATCH_PARENT, PhoneUtil.dip2px(context, PhoneUtil.isLowDensity(context)
						? 36 : 48)));
		pLayout.addView(wv_content.getLayout());
		setContentView(pLayout);

		if (action.equals(Constant.WAP.ACTION_HOTEL)){
			initHotelDetal();
		} else if (action.equals(Constant.WAP.ACTION_MENBER)){
			initMember();
		}
	}

	private void initHotelDetal() {
		String hotel_id = getIntent().getStringExtra(Constant.WAP.HOTEL_ID_KEY);
		String fromDate = getIntent().getStringExtra(Constant.WAP.FROM_DATE_KEY);
		String toDate = getIntent().getStringExtra(Constant.WAP.TO_DATE_KEY);

		if (!TextUtils.isEmpty(hotel_id)){
			wv_content.loadUrl(Constant.WAP.URL_HOTEL + "/" + hotel_id + "?fromDate=" + fromDate
					+ "&toDate=" + toDate);
		}
	}

	private void initMember() {
		wv_content.loadUrl(Constant.WAP.URL_MENBER);
	}

	private ViewGroup getHeader() {
		ViewGroup group = (ViewGroup) getLayoutInflater().inflate(R.layout.detail_header, null);
		Button btn_back = (Button) group.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});


		if (getIntent().getAction().equals(Constant.WAP.ACTION_HOTEL)){
			TextView tv_title = (TextView) group.findViewById(R.id.tv_title);
			tv_title.setText("酒店详情");
		}
		return group;
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
}
