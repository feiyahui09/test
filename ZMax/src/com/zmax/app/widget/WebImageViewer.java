package com.zmax.app.widget;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.ui.base.BaseActivity;
import com.zmax.app.utils.Constant;

public class WebImageViewer extends BaseActivity {
	private Context mContext;
	private WebView webview;
	private String largeUrl;
	private ProgressBar pb;
	private byte[] loader;
	private Button btn_back;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_viewer);
		mContext = this;
		largeUrl = getIntent().getStringExtra(Constant.Chat.CHAT_IMG_LARGE_IMG_KEY);
		if (TextUtils.isEmpty(largeUrl)) {
			finish();
			return;
		}
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		pb = (ProgressBar) findViewById(R.id.pb);
		webview = (WebView) findViewById(R.id.webview);
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				pb.setVisibility(View.VISIBLE);
				pb.setProgress(newProgress);
				if (newProgress == 100) {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							pb.setVisibility(View.GONE);
						}
					}, 150);
				}
			}
			
		});
		loader = getAsset("img_loader.html");
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				goReset(loader, largeUrl);
				
			}
		}, 38);
		// String loaderHtml = new String(loader);
		// String loadingHtml = loaderHtml.replace("$IMAGES$", midUrl);
		// webview.getSettings().setBuiltInZoomControls(true); // 显示放大缩小
		// controler
		// webview.getSettings().setSupportZoom(true); // 可以缩放
		// webview.getSettings().setJavaScriptEnabled(true);
		// webview.getSettings().setUseWideViewPort(true); // 实现双击放大缩小
		// webview.getSettings().setLoadsImagesAutomatically(true);
		// webview.getSettings().setLoadWithOverviewMode(true);
		//
		// // webview.loadUrl("file:///android_asset/loading.gif");
		// // webview.loadData(loadingHtml, "text/html", "utf-8");
		//
		// webview.loadDataWithBaseURL("about:loading", loadingHtml,
		// "text/html",
		// "utf-8", "");
		// webview.loadDataWithBaseURL("about:loading",
		// "<center><img src="+midUrl+"></center>", "text/html", "utf-8", "");
		// webview.loadUrl("<center><img src="+midUrl+"></center>");
	}
	
	private void goReset(byte[] loader, String midUrl) {
		String loaderHtml = new String(loader);
		String loadingHtml = loaderHtml.replace("$IMAGES$", midUrl);
		webview.setBackgroundColor(getResources().getColor(R.color.black));
		webview.getSettings().setBuiltInZoomControls(true); // 显示放大缩小 controler
		webview.getSettings().setSupportZoom(true); // 可以缩放
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setUseWideViewPort(true); // 实现双击放大缩小
		webview.getSettings().setLoadsImagesAutomatically(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		
		// webview.loadUrl("file:///android_asset/loading.gif");
		// webview.loadData(loadingHtml, "text/html", "utf-8");
		
		webview.loadDataWithBaseURL("about:loading", loadingHtml, "text/html", "utf-8", "");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	
	private byte[] getAsset(String file) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			AssetManager assetManager = getAssets();
			InputStream is = assetManager.open(file);
			int oneByte = is.read();
			while (oneByte >= 0) {
				bos.write(oneByte);
				oneByte = is.read();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
}
