package com.zmax.app.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.ser.ScalarSerializerBase;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.adapter.RoomControlAdapter;
import com.zmax.app.model.AirCondition;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.SetAirConditionTask;
import com.zmax.app.ui.RoomControlActivity.PageChangedCallback;
import com.zmax.app.ui.RoomControlActivity.VerticalChangedCallback;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.VerticalViewPager;

public class RoomControlAirConditionFragment extends Fragment {
	
	protected View view;
	
	private VerticalViewPager vpager;
	
	private RoomControlAdapter adapter;
	
	private VerticalChangedCallback callback;
	
	private SetAirConditionTask task;
	private AirCondition airCondition;
	private boolean isEnable;
	
	public RoomControlAirConditionFragment(VerticalChangedCallback callback) {
		this.callback = callback;
		setRetainInstance(true);
	}
	
	public RoomControlAirConditionFragment(VerticalChangedCallback callback, AirCondition airCondition) {
		this.callback = callback;
		this.airCondition = airCondition;
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.room_control_fragment, null);
		vpager = (VerticalViewPager) view.findViewById(R.id.vpager);
		adapter = new RoomControlAdapter(getActivity(), null);
		vpager.setAdapter(adapter);
		adapter.addViews(getAirconditionView(getActivity(), inflater));
		vpager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if (callback != null) {
					
					callback.onCallBack(position == 0 ? true : false);
				}
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 相当于Fragment的onResume
			if (callback != null) {
				if (adapter != null && adapter.getCount() > 0)
					callback.onCallBack(vpager.getCurrentItem() == 0 ? true : false);
				else
					callback.onCallBack(true);
				
			}
		}
	}
	
	public List<View> getAirconditionView(final FragmentActivity fragmentActivity, LayoutInflater inflater) {
		
		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater));
		mList.add(geAirconditionBehind(inflater, fragmentActivity));
		
		return mList;
	}
	
	private View getAbove(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.room_control_above, null);
		ImageView big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_aircondition);
		TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("空调");
		TextView tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.GONE);
		return view;
	}
	
	TextView tv_temperature, tv_air_blower, tv_schema;
	
	private View geAirconditionBehind(LayoutInflater inflater, Context context) {
		
		Typeface fontFace = Typeface.createFromAsset(context.getAssets(), "font.TTF");
		View ib_on, btn_schema, ib_tmp_up, ib_wind_down, ib_tmp_down, ib_wind_up, tv_temp_add, tv_wind_minus, tv_temp_minus, tv_wind_add;
		final View view = inflater.inflate(R.layout.room_control_aircondition_behind, null);
		tv_temperature = (TextView) view.findViewById(R.id.tv_temperature);
		tv_air_blower = (TextView) view.findViewById(R.id.tv_air_blower);
		tv_schema = (TextView) view.findViewById(R.id.tv_schema);
		tv_temperature.setTypeface(fontFace);
		tv_temperature.setText("26");
		
		ib_on = view.findViewById(R.id.ib_on);
		btn_schema = view.findViewById(R.id.btn_schema);
		ib_tmp_up = view.findViewById(R.id.ib_tmp_up);
		ib_wind_down = view.findViewById(R.id.ib_wind_down);
		ib_tmp_down = view.findViewById(R.id.ib_tmp_down);
		ib_wind_up = view.findViewById(R.id.ib_wind_up);
		tv_temp_add = view.findViewById(R.id.tv_temp_add);
		tv_wind_minus = view.findViewById(R.id.tv_wind_minus);
		tv_temp_minus = view.findViewById(R.id.tv_temp_minus);
		tv_wind_add = view.findViewById(R.id.tv_wind_add);
		
		ib_on.setOnClickListener(onClickListener);
		btn_schema.setOnClickListener(onClickListener);
		ib_tmp_up.setOnClickListener(onClickListener);
		ib_wind_down.setOnClickListener(onClickListener);
		ib_tmp_down.setOnClickListener(onClickListener);
		ib_wind_up.setOnClickListener(onClickListener);
		
		initData();
		return view;
	}
	
	private void initData() {
		if (airCondition == null) return;
		onStatusChanged(airCondition.status);
		if (airCondition.status == 1) {
			cur_air_blower = airCondition.air_blower;
			cur_schema = airCondition.schema;
			cur_temperature = airCondition.setting_temperature;
			setTvAirBlower();
			setTvSchema();
			setTvTemperature();
		}
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ib_on:
					set("status");
					break;
				case R.id.btn_schema:
					set("schema");
					break;
				case R.id.ib_tmp_up:
					set("temperature", "up");
					break;
				case R.id.ib_wind_down:
					set("air_blower", "down");
					break;
				case R.id.ib_tmp_down:
					set("temperature", "down");
					break;
				case R.id.ib_wind_up:
					set("air_blower", "up");
					break;
				
				default:
					break;
			}
		}
	};
	/**
	 * 
	 * @param context
	 * @param opera_type
	 * @param opera_data
	 *            控制温度：opera_type ： temperature， opera_data： 5～35
	 *            控制模式：opera_type ： schema，
	 *            opera_data：（col：制冷、hot：制热、nat：通风/睡眠；）
	 *            控制阀门： opera_type ： on_off， opera_data： 0，1
	 *            控制风机模式：opera_type ： air_blower， opera_data： (
	 *            low：低速、mid：中速、hig：高速、auto：自动；)
	 *            控制开关机：opera_type ： status， opera_data： 0，1
	 * @return
	 */
	private List<String> AIR_BLOWER = Arrays.asList("low", "mid", "hig", "auto");
	private List<String> SCHEMA = Arrays.asList("col", "hot", "nat");
	private List<Integer> STATUS = Arrays.asList(0, 1);
	
	private String cur_air_blower = "auto";
	private int cur_temperature = 26;
	private String cur_schema = "nat";
	private int cur_status = 0;
	
	private String opera_air_blower = "auto";
	private int opera_temperature = 26;
	private String opera_schema = "nat";
	private int opera_status = 0;
	
	/**
	 * 
	 * @param opera_type
	 *            必选
	 * @param params
	 *            isTemperatureUp
	 *            可选，控制温度加减
	 */
	private synchronized void set(final String opera_type, Object... params) {
		if (opera_type.equals("air_blower")) {
			if (params != null && params.length > 0 && params[0] instanceof String) {
				int mode;
				if (params[0].equals("up")) {
					mode = (AIR_BLOWER.indexOf(cur_air_blower) + 1) % AIR_BLOWER.size();
					opera_air_blower = AIR_BLOWER.get(mode);
				}
				else if (params[0].equals("down")) {
					mode = (AIR_BLOWER.indexOf(cur_air_blower) - 1 + AIR_BLOWER.size()) % AIR_BLOWER.size();
					opera_air_blower = AIR_BLOWER.get(mode);
				}
			}
			
		}
		else if (opera_type.equals("temperature")) {
			if (!isEnable) return;
			if (params != null && params.length > 0 && params[0] instanceof String) {
				if (params[0].equals("up")) {
					opera_temperature = (cur_temperature + 1);
				}
				else if (params[0].equals("down")) {
					opera_temperature = (cur_temperature - 1);
				}
			}
		}
		else if (opera_type.equals("schema")) {
			if (!isEnable) return;
			opera_schema = SCHEMA.get((SCHEMA.indexOf(cur_schema) + 1) % SCHEMA.size());
		}
		else if (opera_type.equals("status")) {
			opera_status = STATUS.get((STATUS.indexOf(cur_status) + 1) % STATUS.size());
		}
		task = new SetAirConditionTask(getActivity(), new SetAirConditionTask.TaskCallBack() {
			@Override
			public void onCallBack(AirCondition result) {
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
					if (opera_type.equals("air_blower")) {
						cur_air_blower = opera_air_blower;
						setTvAirBlower();
					}
					else if (opera_type.equals("temperature")) {
						cur_temperature = opera_temperature;
						setTvTemperature();
					}
					else if (opera_type.equals("schema")) {
						cur_schema = opera_schema;
						setTvSchema();
					}
					else if (opera_type.equals("status")) {
						cur_status = opera_status;
						onStatusChanged(cur_status);
					}
				}
				else {
					Utility.toastResult(getActivity(), result.message);
 		
				}
			}
		});
		
		String opera_data = "";
		if (opera_type.equals("air_blower")) {
			opera_data = opera_air_blower;
		}
		else if (opera_type.equals("temperature")) {
			// Log.e("================" + new Date() + "============");
			// Log.e("opera_temperature  " + opera_temperature);
			// Log.e("cur_temperature  " + cur_temperature);
			if (opera_temperature < 16 || opera_temperature > 30) {
				Utility.toastResult(getActivity(), "请设置16~30℃范围的的温度！");
				return;
			}
			else
				opera_data = opera_temperature + "";
		}
		else if (opera_type.equals("schema")) {
			opera_data = opera_schema;
		}
		else if (opera_type.equals("status")) {
			opera_data = opera_status + "";
		}
		
		task.execute(opera_type, opera_data);
	}
	
	private void setTvTemperature() {
		tv_temperature.setText(cur_temperature + "");
		
	}
	
	private void onStatusChanged(int isOpen) {
		cur_status = isOpen;
		opera_status = isOpen;
		if (isOpen == 1) {
			tv_temperature.setVisibility(View.VISIBLE);
			tv_schema.setVisibility(View.VISIBLE);
			tv_air_blower.setVisibility(View.VISIBLE);
			isEnable = true;
		}
		else {
			tv_temperature.setVisibility(View.GONE);
			tv_schema.setVisibility(View.GONE);
			tv_air_blower.setVisibility(View.GONE);
			isEnable = false;
		}
	}
	
	private void setTvAirBlower() {
		String schema = "";
		if (cur_air_blower.equals("low")) {
			schema = "低速";
		}
		else if (cur_air_blower.equals("mid")) {
			schema = "中速";
		}
		else if (cur_air_blower.equals("hig")) {
			schema = "高速";
		}
		else
			schema = "自动";
		tv_air_blower.setText(schema);
	}
	
	private void setTvSchema() {
		String schema = "";
		int schema_icon = 0;
		if (cur_schema.equals("col")) {
			schema = "冷风";
			schema_icon = R.drawable.room_control_air_control_schema_col;
		}
		else if (cur_schema.equals("hot")) {
			schema = "暖风";
			schema_icon = R.drawable.room_control_air_control_schema_hot;
		}
		else {
			schema = "自然风";
			schema_icon = R.drawable.room_control_air_control_schema_nat;
		}
		tv_schema.setText(schema);
		tv_schema.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(schema_icon), null, null, null);
	}
}
