package com.zmax.app.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.umeng.analytics.MobclickAgent;
import com.zmax.app.R;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;

public class FlashActivity extends BaseFragmentActivity implements AMapLocationListener {
	static final int delayMillis = 9500;
	LocationManagerProxy mAMapLocationManager;
	AMapLocation aMapLocation;
	private Handler handler = new Handler();
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=this;
        MobclickAgent.setDebugMode(false);
        MobclickAgent.updateOnlineConfig( this );

		RelativeLayout layout = new RelativeLayout(this);
		setContentView(layout);
		layout.setBackgroundResource(R.drawable.flash);
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1600);
		layout.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				goNext();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
		});
		goLocating();
     }



    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void goNext() {
		if (DefaultShared.getBoolean("IS_FIRST_INSTALLED", true)) {
			Intent intent = new Intent(this, WelcomeActivity.class);
			intent.setAction(Constant.ACTION_WELCOME_FROM_INDEX);
			startActivity(intent);
			finish();
		}
		else {
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setAction(Constant.DialogCode.ACTION_BACK_INDEX);
			startActivity(intent);
			finish();
		}
		DefaultShared.putBoolean("IS_FIRST_INSTALLED", false);
	}

	private void goLocating() {
		Runnable afterCheckingUpdateRunnable = new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mContext,"正在定位中",Toast.LENGTH_SHORT).show();
				activate();
			}
		};

		runOnUiThread(afterCheckingUpdateRunnable);
	}

	public void activate() {
//		ToastHelper.createProgress(this, "正在定位中...").show();
		if (mAMapLocationManager == null){
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);//
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
			 */
			// Location SDK定位采用GPS和网络混合定位方式，时间最短是5000毫秒，否则无效
			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, this);

			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if (aMapLocation == null){
						deactivate();// 销毁掉定位
					}
				}
			}, delayMillis);

		}
	}
	public void deactivate() {

		if (mAMapLocationManager != null){
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		this.aMapLocation = arg0;// 判断超时机制
		if (arg0 == null){
			Constant.CUR_CITY=null;
		} else {
			String city = aMapLocation.getCity();
			Constant.CUR_CITY= city.substring(0,city.length()-1 );
		}
		deactivate();
		goNext();
	}

}
