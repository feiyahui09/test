package com.zmax.app.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.zmax.app.R;
import com.zmax.app.model.Documents;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetDocumentsTask;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;
import com.zmax.app.utils.JsonMapperUtils;

public class DocumentsActivity extends BaseActivity {
	
	private Button btn_Back;
	private TextView tv_title;
	private TextView wv_content;
	private Context context;
	private GetDocumentsTask getDocumentsTask;
	private String title, type, spf_key, documents_content;
	private Documents old;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.documents);
		init();
		initHeader();
		
		old = JsonMapperUtils.toObject(DefaultShared.getString(spf_key, ""), Documents.class);
//		if (!NetWorkHelper.checkNetState(this)) {
//			if (old != null) initData(old);
//			return;
//		}
		getDocumentsTask = new GetDocumentsTask(this, new GetDocumentsTask.TaskCallBack() {
			
			@Override
			public void onCallBack(Documents document) {
				
				if (document != null) {
					if (document.status == 200) {
						initData(document);
						DefaultShared.putString(spf_key, JsonMapperUtils.toJson(document));
					}
					else if (document.status == 304) {
						if (old != null) initData(old);
					}
					else {
						if (old != null) initData(old);
						Toast.makeText(context, document.message, 333).show();
					}
				}
			}
			
		});
		if (old != null && !TextUtils.isEmpty(old.updated_time))
			getDocumentsTask.execute(type, old.updated_time);
		else
			getDocumentsTask.execute(type, "11111111111111");
		// getDocumentsTask.execute(type, "20140101132405");
		
	}
	
	private void init() {
		context = this;
		wv_content = (TextView) findViewById(R.id.tv_content);
		int typeID = getIntent().getIntExtra(Constant.Documents.DOCUMENTS_TYPE_KEY, 1);
		
		switch (typeID) {
			case Constant.Documents.PROTOCAL_TYPE:
				title = "用户协议	";
				type = "protocol";
				spf_key = Constant.Documents.PROTOCAL_SPF_KEY;
				break;
			case Constant.Documents.GUIDE_TYPE:
				title = "ZMAX指南";
				type = "guide";
				spf_key = Constant.Documents.GUIDE_SPF_KEY;
				break;
			case Constant.Documents.RIGHT_TYPE:
				title = "会员权益";
				spf_key = Constant.Documents.RIGHT_SPF_KEY;
				type = "right";
				break;
			default:
				break;
		}
		
	}
	
	private void initHeader() {
		findViewById(R.id.btn_share).setVisibility(View.GONE);
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("" + title);
	}
	
	private void initData(Documents document) {
		documents_content = document.content;
		tv_title.setText("" + document.title);
		// tv_content.setText(""+document.content);
		wv_content.setMovementMethod(ScrollingMovementMethod.getInstance());// 滚动
		wv_content.setText(Html.fromHtml(documents_content, imageGetter, null));
		
	}
	
	Drawable drawable = null;
	final Html.ImageGetter imageGetter = new Html.ImageGetter() {
		
		public Drawable getDrawable(String source) {
			
			// URL url;
			// try {
			// url = new URL(source);
			// drawable = Drawable.createFromStream(url.openStream(), "");
			// }
			// catch (Exception e) {
			// e.printStackTrace();
			// return null;
			// }
			
			ImageLoader.getInstance().loadImage(source, new ImageLoadingListener() {
				
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
					// TODO Auto-generated method stub
					drawable = new BitmapDrawable(loadedImage);
					drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							wv_content.setText(Html.fromHtml(documents_content, imageGetter, null));
							
						}
					}, 122);
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
			
			return drawable;
		}
	};
	/*
	 * private void initData1(Documents document) {
	 * tv_title.setText("" + document.title);
	 * // wv_content.getSettings().setDefaultFontSize(21);
	 * wv_content.setFocusableInTouchMode(false);
	 * wv_content.setFocusable(false);
	 * wv_content.setBackgroundColor(getResources().getColor(android.R.color.
	 * transparent));
	 * wv_content.loadData( document.content, "text/html", "UTF-8");
	 * }
	 *//** 全局web样式 */
	/*
	 * public final static String WEB_STYLE =
	 * "<style>* { line-height:150%;background-color:#ededed;color=#404040,font-family:SimSun; margin:0;padding:0;"
	 * + "word-wrap: break-word; } " + "p {color:#404040;} a {color:#3E62A6;}" +
	 * "img { max-width:100%; margin:0 auto; } ";
	 */
	
}
