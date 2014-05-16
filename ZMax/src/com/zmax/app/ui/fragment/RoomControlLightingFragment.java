package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.Light;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.SetLightTask;
import com.zmax.app.ui.RoomControlActivity.VerticalChangedCallback;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.SlidingUpPanelLayout;
import com.zmax.app.widget.SlidingUpPanelLayout.PanelSlideListener;

public class RoomControlLightingFragment extends Fragment {
	/*
	 * parent view
	 */
	protected View view;
	
	/* above views */
	private ImageView big_icon;
	private TextView tv_mode, tv_mode_detail;
	
	/* behind views */
	
	private ImageView iv_img, iv_hint_above;
	private TextView tv_mode_hint, tv_hint_above;
	private ImageButton ib_previous, ib_next;
	private Button btn_apply;
	private View dragview;
	
	private SetLightTask task;
	
	private Light light = null;
	
	private SlidingUpPanelLayout mLayout;
	VerticalChangedCallback callback;
	
	public RoomControlLightingFragment(VerticalChangedCallback callback, Light light) {
		this.light = light;
		this.callback = callback;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.room_control_lighting, null);
		
		// above
		big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_lighting);
		tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("灯光控制");
		tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.VISIBLE);
		tv_mode_detail.setText("当前灯光模式：明亮模式");
		if (light != null) {
			for (int i = 0; i < mode_patterns.length; i++) {
				if (light.pattern.equals(mode_patterns[i])) {
					tv_mode_detail.setText(String.format("当前灯光模式：%s", mode_names[i]));
					operaMode = i;
					curMode = i;
				}
			}
		}
		
		// behind
		iv_img = ((ImageView) view.findViewById(R.id.iv_img));
		iv_img.setImageResource(R.drawable.room_control_lighting_bright_mode);
		tv_mode_hint = (TextView) view.findViewById(R.id.tv_mode_hint);
		tv_mode_hint.setText("明亮模式");
		ib_previous = ((ImageButton) view.findViewById(R.id.ib_previous));
		ib_next = ((ImageButton) view.findViewById(R.id.ib_next));
		ib_previous.setOnClickListener(ImgClickListener);
		ib_next.setOnClickListener(ImgClickListener);
		btn_apply = (Button) view.findViewById(R.id.btn_apply);
		btn_apply.setOnClickListener(ImgClickListener);
		
		if (light != null) {
			for (int i = 0; i < mode_patterns.length; i++) {
				if (light.pattern.equals(mode_patterns[i])) {
					iv_img.setImageResource(mode_imgs[i]);
					tv_mode_hint.setText(mode_names[i]);
				}
			}
		}
		
		mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
		dragview = view.findViewById(R.id.room_control_behind_indicator);
		tv_hint_above = (TextView) view.findViewById(R.id.tv_hint_above);
		iv_hint_above = (ImageView) view.findViewById(R.id.iv_hint_above);
		
		dragview.setBackgroundResource(R.drawable.default_bg_repeat);
		mLayout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// Log.i("onPanelSlide, offset " + slideOffset);
			}
			
			@Override
			public void onPanelExpanded(View panel) {
				Log.i("onPanelExpanded");
				if (callback != null) {
					callback.onCallBack(false);
				}
				iv_hint_above.setBackgroundResource(R.drawable.room_control_behind_trigle);
				tv_hint_above.setText(getActivity().getString(R.string.slide_down_hint));
			}
			
			@Override
			public void onPanelCollapsed(View panel) {
				Log.i("onPanelCollapsed");
				if (callback != null) {
					callback.onCallBack(true);
				}
				iv_hint_above.setBackgroundResource(R.drawable.room_control_above_triangle);
				tv_hint_above.setText(getActivity().getString(R.string.slide_up_hint));
			}
			
			@Override
			public void onPanelAnchored(View panel) {
				Log.i("onPanelAnchored");
				
			}
		});
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	private void setTvAnimation(TextView textView) {
		
		Animation ani = new AlphaAnimation(0f, 1f);
		ani.setDuration(1500);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(Animation.INFINITE);
		textView.startAnimation(ani);
	}
	
	@Deprecated
	public List<View> getLightingView(final FragmentActivity fragmentActivity, LayoutInflater inflater) {
		
		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater));
		mList.add(getLightingBehind(inflater));
		
		return mList;
	}
	
	@Deprecated
	private View getAbove(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.room_control_above, null);
		big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_lighting);
		tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("灯光控制");
		tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.VISIBLE);
		tv_mode_detail.setText("当前灯光模式：明亮模式");
		if (light != null) {
			for (int i = 0; i < mode_patterns.length; i++) {
				if (light.pattern.equals(mode_patterns[i])) {
					tv_mode_detail.setText(String.format("当前灯光模式：%s", mode_names[i]));
					operaMode = i;
					curMode = i;
				}
			}
		}
		return view;
	}
	
	@Deprecated
	private View getLightingBehind(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.room_control_lighting_behind, null);
		iv_img = ((ImageView) view.findViewById(R.id.iv_img));
		iv_img.setImageResource(R.drawable.room_control_lighting_bright_mode);
		tv_mode_hint = (TextView) view.findViewById(R.id.tv_mode_hint);
		tv_mode_hint.setText("明亮模式");
		ib_previous = ((ImageButton) view.findViewById(R.id.ib_previous));
		ib_next = ((ImageButton) view.findViewById(R.id.ib_next));
		ib_previous.setOnClickListener(ImgClickListener);
		ib_next.setOnClickListener(ImgClickListener);
		btn_apply = (Button) view.findViewById(R.id.btn_apply);
		btn_apply.setOnClickListener(ImgClickListener);
		
		if (light != null) {
			for (int i = 0; i < mode_patterns.length; i++) {
				if (light.pattern.equals(mode_patterns[i])) {
					iv_img.setImageResource(mode_imgs[i]);
					tv_mode_hint.setText(mode_names[i]);
				}
			}
		}
		return view;
	}
	
	private int curMode = 0;
	private int operaMode = 0;
	public static String[] mode_names = { "明亮模式", "电视模式", "阅读模式", "睡眠模式" };
	public static int[] mode_imgs = { R.drawable.room_control_lighting_bright_mode, R.drawable.room_control_lighting_tv_mode,
			R.drawable.room_control_lighting_reading_mode, R.drawable.room_control_lighting_sleep_mode };
	
	public static String[] mode_patterns = { "bright", "tv", "read", "sleep" };
	
	private OnClickListener ImgClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ib_previous) {
				operaMode = (operaMode - 1 + 4) % 4;
				// tv_mode_hint.setText(mode_names[operaMode]);
				// tv_mode_detail.setText("当前灯光模式：" + mode_names[operaMode]);
				iv_img.setImageResource(mode_imgs[operaMode]);
			}
			else if (v.getId() == R.id.ib_next) {
				operaMode = (operaMode + 1) % 4;
				tv_mode_hint.setText(mode_names[operaMode]);
				tv_mode_detail.setText("当前灯光模式：" + mode_names[operaMode]);
				iv_img.setImageResource(mode_imgs[operaMode]);
			}
			else if (v.getId() == R.id.btn_apply) {
				set(mode_patterns[operaMode]);
			}
		}
	};
	
	private void set(String pattern) {
		task = new SetLightTask(getActivity(), new SetLightTask.TaskCallBack() {
			@Override
			public void onCallBack(Light result) {
				if (getActivity() == null) {
					return;
				}
				if (result == null) {
					if (!NetWorkHelper.checkNetState(getActivity()))
						Toast.makeText(getActivity(), getActivity().getString(R.string.httpProblem), 450).show();
					else
						Utility.toastResult(getActivity(), getActivity().getString(R.string.unkownError));
				}
				else if (result.status == 200) {
					curMode = operaMode;
				}
				else if (result.status == 401) {
					
					Utility.showTokenErrorDialog(getActivity(), result.message);
				}
				else {
					Utility.toastResult(getActivity(), result.message);
					
				}
				// if(result.intValue()!=200)
			}
		});
		task.execute(pattern);
	}
}
