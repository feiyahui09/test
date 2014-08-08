package com.zmax.app.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
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
import com.zmax.app.chat.ChatHelper;
import com.zmax.app.chat.ClientCallback;
import com.zmax.app.model.AirCondition;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetAirConditionStatusTask;
import com.zmax.app.task.LOAD_STATUS_ENUM;
import com.zmax.app.task.SetAirConditionTask;
import com.zmax.app.ui.RoomControlActivity;
import com.zmax.app.ui.RoomControlActivity.VerticalChangedCallback;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.JsonMapperUtils;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.SlidingUpPanelLayout;
import com.zmax.app.widget.SlidingUpPanelLayout.PanelSlideListener;
import eu.inmite.android.lib.dialogs.ProgressDialogFragment;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomControlAirConditionFragment extends Fragment implements RoomControlActivity.IUpdateRoomState {

	  String api_type = "GET";
	protected View view;
	TextView tv_temperature, tv_air_blower, tv_schema;
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
	private Handler handler = new Handler();
	private VerticalChangedCallback callback;
	private SetAirConditionTask task;
	private boolean isEnable;
	private SlidingUpPanelLayout mLayout;
	/**
	 * @param context
	 * @param opera_type
	 * @param opera_data
	 * 控制温度：opera_type ： temperature， opera_data： 5～35
	 * 控制模式：opera_type ： schema，
	 * opera_data：（col：制冷、hot：制热、nat：通风/睡眠；）
	 * 控制阀门： opera_type ： on_off， opera_data： 0，1
	 * 控制风机模式：opera_type ： air_blower， opera_data： (
	 * low：低速、mid：中速、hig：高速、auto：自动；)
	 * 控制开关机：opera_type ： status， opera_data： 0，1
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
 	private String opera_type;
	private Object[] params;
	private DialogFragment progressDialog;
	private GetAirConditionStatusTask getRoomStatusTask;
	private LOAD_STATUS_ENUM load_status_enum = LOAD_STATUS_ENUM.INIT;

	public RoomControlAirConditionFragment(VerticalChangedCallback callback, AirCondition airCondition) {
		this.callback = callback;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.room_control_aircondition, null);
		// above
		ImageView big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_aircondition);
		TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("空调");
		TextView tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.GONE);

		// behind

		Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(), "font.TTF");
		View ib_on, btn_schema, ib_tmp_up, ib_wind_down, ib_tmp_down, ib_wind_up, tv_temp_add, tv_wind_minus,
				tv_temp_minus, tv_wind_add;
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

		final TextView tv_hint_above = (TextView) view.findViewById(R.id.tv_hint_above);
		final ImageView iv_hint_above = (ImageView) view.findViewById(R.id.iv_hint_above);
		mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
		mLayout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// Log.i("onPanelSlide, offset " + slideOffset);
			}

			@Override
			public void onPanelExpanded(View panel) {
				Log.i("onPanelExpanded");
				if (callback != null){
					callback.onCallBack(false);
				}
				iv_hint_above.setBackgroundResource(R.drawable.room_control_behind_trigle);
				tv_hint_above.setText(getActivity().getString(R.string.slide_down_hint));
			}

			@Override
			public void onPanelCollapsed(View panel) {
				Log.i("onPanelCollapsed");
				if (callback != null){
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

	@Deprecated
	public List<View> getAirconditionView(final FragmentActivity fragmentActivity, LayoutInflater inflater) {

		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater));
		mList.add(geAirconditionBehind(inflater, fragmentActivity));

		return mList;
	}

	@Deprecated
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

	@Deprecated
	private View geAirconditionBehind(LayoutInflater inflater, Context context) {

		Typeface fontFace = Typeface.createFromAsset(context.getAssets(), "font.TTF");
		View ib_on, btn_schema, ib_tmp_up, ib_wind_down, ib_tmp_down, ib_wind_up, tv_temp_add, tv_wind_minus,
				tv_temp_minus, tv_wind_add;
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

		return view;
	}

	private void initData(AirCondition airCondition) {
		if (airCondition == null) return;
		onStatusChanged(airCondition.status);
		if (airCondition.status == 1){
			cur_air_blower = airCondition.air_blower;
			cur_schema = airCondition.schema;
			cur_temperature = airCondition.setting_temperature;
			setTvAirBlower();
			setTvSchema();
			setTvTemperature();
		}
	}

	/**
	 * @param opera_type 必选
	 * @param params     isTemperatureUp
	 *                   可选，控制温度加减
	 */
	private synchronized void set(final String opera_type, Object... params) {
		api_type="POST";
		this.opera_type = opera_type;
		this.params = params;
		if (this.opera_type.equals("air_blower")){
			if (this.params != null && this.params.length > 0 && this.params[0] instanceof String){
				int mode;
				if (this.params[0].equals("up")){
					mode = (AIR_BLOWER.indexOf(cur_air_blower) + 1) % AIR_BLOWER.size();
					opera_air_blower = AIR_BLOWER.get(mode);
				} else if (this.params[0].equals("down")){
					mode = (AIR_BLOWER.indexOf(cur_air_blower) - 1 + AIR_BLOWER.size()) % AIR_BLOWER.size();
					opera_air_blower = AIR_BLOWER.get(mode);
				}
			}

		} else if (this.opera_type.equals("temperature")){
			if (!isEnable) return;
			if (this.params != null && this.params.length > 0 && this.params[0] instanceof String){
				if (this.params[0].equals("up")){
					opera_temperature = (cur_temperature + 1);
				} else if (this.params[0].equals("down")){
					opera_temperature = (cur_temperature - 1);
				}
			}
		} else if (this.opera_type.equals("schema")){
			if (!isEnable) return;
			opera_schema = SCHEMA.get((SCHEMA.indexOf(cur_schema) + 1) % SCHEMA.size());
		} else if (this.opera_type.equals("status")){
			opera_status = STATUS.get((STATUS.indexOf(cur_status) + 1) % STATUS.size());
		}
		task = new SetAirConditionTask(getActivity(), new SetAirConditionTask.TaskCallBack() {
			@Override
			public void onCallBack(AirCondition result) {
				handelPostResult(result);
			}
		});

		String opera_data = "";
		if (this.opera_type.equals("air_blower")){
			opera_data = opera_air_blower;
		} else if (this.opera_type.equals("temperature")){
			// Log.e("================" + new Date() + "============");
			// Log.e("opera_temperature  " + opera_temperature);
			// Log.e("cur_temperature  " + cur_temperature);
			if (opera_temperature < 16 || opera_temperature > 30){
				Utility.toastResult(getActivity(), "请设置16~30℃范围的的温度！");
				return;
			} else
				opera_data = opera_temperature + "";
		} else if (this.opera_type.equals("schema")){
			opera_data = opera_schema;
		} else if (this.opera_type.equals("status")){
			opera_data = opera_status + "";
		}

		task.execute(this.opera_type, opera_data);
	}

	private void handelPostResult(AirCondition result) {
		if (getActivity() == null){
			return;
		}
		if (result == null){
			if (!NetWorkHelper.checkNetState(getActivity()))
				Toast.makeText(getActivity(), getActivity().getString(R.string.httpProblem), 450).show();
			else
				Utility.toastResult(getActivity(), getActivity().getString(R.string.unkownError));
		} else if (result.status == 200){
			if (opera_type.equals("air_blower")){
				cur_air_blower = opera_air_blower;
				setTvAirBlower();
			} else if (opera_type.equals("temperature")){
				cur_temperature = opera_temperature;
				setTvTemperature();
			} else if (opera_type.equals("schema")){
				cur_schema = opera_schema;
				setTvSchema();
			} else if (opera_type.equals("status")){
				cur_status = opera_status;
				onStatusChanged(cur_status);
			}
		} else if (result.status == 401){

			Utility.showTokenErrorDialog(getActivity(), result.message);
		} else {
			Utility.toastResult(getActivity(), result.message);

		}
	}

	private void setTvTemperature() {
		tv_temperature.setText(cur_temperature + "");

	}

	private void onStatusChanged(int isOpen) {
		cur_status = isOpen;
		opera_status = isOpen;
		if (isOpen == 1){
			tv_temperature.setVisibility(View.VISIBLE);
			tv_schema.setVisibility(View.VISIBLE);
			tv_air_blower.setVisibility(View.VISIBLE);
			isEnable = true;
		} else {
			tv_temperature.setVisibility(View.GONE);
			tv_schema.setVisibility(View.GONE);
			tv_air_blower.setVisibility(View.GONE);
			isEnable = false;
		}
	}

	private void setTvAirBlower() {
		String schema = "";
		if (cur_air_blower.equals("low")){
			schema = "低速";
		} else if (cur_air_blower.equals("mid")){
			schema = "中速";
		} else if (cur_air_blower.equals("hig")){
			schema = "高速";
		} else
			schema = "自动";
		tv_air_blower.setText(schema);
	}

	private void setTvSchema() {
		String schema = "";
		int schema_icon = 0;
		if (cur_schema.equals("col")){
			schema = "冷风";
			schema_icon = R.drawable.room_control_air_control_schema_col;
		} else if (cur_schema.equals("hot")){
			schema = "暖风";
			schema_icon = R.drawable.room_control_air_control_schema_hot;
		} else {
			schema = "自然风";
			schema_icon = R.drawable.room_control_air_control_schema_nat;
		}
		tv_schema.setText(schema);
		tv_schema.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(schema_icon), null,
				null, null);
	}

	@Override
	public void onSelect() {
		if (load_status_enum != LOAD_STATUS_ENUM.SUCCUSS)
			updateRoomState();
		Log.e("@#$");
	}

	@Override
	public void onUpdate(JSONObject jsonObject) {
		AirCondition result = JsonMapperUtils.toObject(jsonObject.toString(), AirCondition.class);
		if (result == null || !result.device.equals("air_condiction"))
			return;
		if (api_type.equals("POST")){
			handelPostResult(result);
		} else if (api_type.equals("GET")){
			handleGetResult(result);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void updateRoomState() {
			api_type="GET";
		progressDialog = ProgressDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager()
		).setMessage("正在加载中.." +
				".").setTitle("提示")
				.setCancelable(true).show();
		getRoomStatusTask = new GetAirConditionStatusTask(getActivity(), new GetAirConditionStatusTask.TaskCallBack() {
			@Override
			public void onCallBack(AirCondition result) {
				handleGetResult(result);
			}
		});
		getRoomStatusTask.execute();
	}

	private void handleGetResult(AirCondition result) {
		if (getActivity() == null) return;
		load_status_enum = LOAD_STATUS_ENUM.FAIL;
		if (progressDialog != null) progressDialog.dismiss();
		if (result == null){
			if (!NetWorkHelper.checkNetState(getActivity()))
				Toast.makeText(getActivity(), getActivity().getString(R.string.httpProblem), 450).show();
			else
				Toast.makeText(getActivity(), getActivity().getString(R.string.unkownError), 450).show();
		} else if (result.status == 401){

			Utility.showTokenErrorDialog(getActivity(), "" + result.message);
		} else if (result.status == 403){
			try {
				Constant.SYN_TIME_INTERVAL = new SimpleDateFormat("yyyyMMddHHmmss").parse(result.sys_time)
						.getTime() - System.currentTimeMillis();
				updateRoomState();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (result.status != 200)
			Toast.makeText(getActivity(), "" + result.message, 450).show();
		else {
			load_status_enum = LOAD_STATUS_ENUM.SUCCUSS;
			initData(result);
		}
	}
}
