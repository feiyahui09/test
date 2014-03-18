package com.zmax.app.widget;

import com.zmax.app.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ShareDialog extends Dialog implements
		android.view.View.OnClickListener {

	private static final String share_content = "#ZMAX客户端#我在ZMAX发现一个很棒的活动【Z-Cocktail品鉴会，将于2014-05-01 18点在ZMAX武汉光谷潮漫酒店举行】，"
			+ "更多好玩的活动尽在ZMAX APP，快来下载吧~http://www.163.com";

	private Context mContext;
	private LinearLayout ll_sina_weibo, ll_wx, ll_wx_friends, ll_tencent_weibo,
			ll_qq, ll_qzone, ll_msg, ll_mail;

	public ShareDialog(Context context) {
		this(context, 0);
		mContext = context;
	}

	public ShareDialog(Context context, int theme) {
		super(context, R.style.ShareDialog);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_dialog);
		init();
	}

	private void init() {
		ll_sina_weibo = (LinearLayout) findViewById(R.id.ll_sina_weibo);
		ll_wx = (LinearLayout) findViewById(R.id.ll_wx);
		ll_wx_friends = (LinearLayout) findViewById(R.id.ll_wx_friends);
		ll_tencent_weibo = (LinearLayout) findViewById(R.id.ll_tencent_weibo);
		ll_qq = (LinearLayout) findViewById(R.id.ll_qq);
		ll_qzone = (LinearLayout) findViewById(R.id.ll_qzone);
		ll_msg = (LinearLayout) findViewById(R.id.ll_msg);
		ll_mail = (LinearLayout) findViewById(R.id.ll_mail);

		ll_sina_weibo.setOnClickListener(this);
		ll_wx.setOnClickListener(this);
		ll_wx_friends.setOnClickListener(this);
		ll_tencent_weibo.setOnClickListener(this);
		ll_qq.setOnClickListener(this);
		ll_qzone.setOnClickListener(this);
		ll_msg.setOnClickListener(this);
		ll_mail.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_sina_weibo:
			ShareAppManager.shareTo(mContext, "com.sina.weibo",
					"com.sina.weibo.EditActivity", share_content);
			break;
		case R.id.ll_wx:
			ShareAppManager.shareTo(mContext, "com.tencent.mm",
					"com.tencent.mm.ui.tools.ShareImgUI", share_content);
			break;
		case R.id.ll_wx_friends:
			ShareAppManager.shareTo(mContext, "com.tencent.mm",
					"com.tencent.mm.ui.tools.ShareToTimeLineUI", share_content);
			break;
		case R.id.ll_tencent_weibo:

			break;

		case R.id.ll_qq:

			break;
		case R.id.ll_qzone:

			break;
		case R.id.ll_msg:

//			ShareAppManager.shareTo(mContext, "com.android.mms",
//					"com.android.mms.ui.ComposeMessageActivity", share_content);
 				ShareAppManager.shareTo(mContext, "com.android.mms",
 					"com.android.mms.ui.NewMessageActivity", share_content);
			break;
		case R.id.ll_mail:
			ShareAppManager.shareTo(mContext, "com.sina.weibo",
					"com.android.mms.ui.ComposeMessageActivity", share_content);
			break;

		default:
			break;
		}// TODO Auto-generated method stub

	}
}
