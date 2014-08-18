package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.zmax.app.R;
import com.zmax.app.chat.ChatHelper;
import com.zmax.app.chat.ClientCallback;
import com.zmax.app.model.Television;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetTelevisionTask;
import com.zmax.app.task.LOAD_STATUS_ENUM;
import com.zmax.app.task.SetTelevisionTask;
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

public class RoomControlTVFragment extends Fragment implements RoomControlActivity.IUpdateRoomState {

	protected View view;
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ib_on:
					set("on");
					break;
				case R.id.btn_at:
					set("at");
					break;
				case R.id.btn_volu:
					set("volu");
					break;
				case R.id.btn_vold:
					set("vold");
					break;
				case R.id.btn_chd:
					set("chd");
					break;
				case R.id.btn_chu:
					set("chu");
					break;
				case R.id.btn_no_0:
					set("0");
					break;
				case R.id.btn_no_1:
					set("1");
					break;
				case R.id.btn_no_2:
					set("2");
					break;
				case R.id.btn_no_3:
					set("3");
					break;
				case R.id.btn_no_4:
					set("4");
					break;
				case R.id.btn_no_5:
					set("5");
					break;
				case R.id.btn_no_6:
					set("6");
					break;
				case R.id.btn_no_7:
					set("7");
					break;
				case R.id.btn_no_8:
					set("8");
					break;
				case R.id.btn_no_9:
					set("9");
					break;
				case R.id.cb_sil:
					set("sil");
					break;
				default:
					break;
			}
		}
	};
	String api_type = "GET";
	private LOAD_STATUS_ENUM load_status_enum = LOAD_STATUS_ENUM.INIT;
	private Handler handler = new Handler();
	private VerticalChangedCallback callback;
	private SetTelevisionTask task;
	private boolean isEnable;
	private SlidingUpPanelLayout mLayout;
 	private String push_button;
 	private DialogFragment progressDialog;
	private GetTelevisionTask getRoomStatusTask;

	/**
	 * behind views
	 */
	public RoomControlTVFragment() {
	}

	public RoomControlTVFragment(VerticalChangedCallback callback, Television television) {
		this.callback = callback;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.room_control_tv, null);
		// above
		ImageView big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_tv);
		TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("电视");
		TextView tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.GONE);

		// behind
		final LinearLayout ll_digital, ll_orient;
		RadioGroup rg_model;
		RadioButton rb_orient, rb_digital;
		View ib_on, btn_at, btn_volu, btn_chd, btn_vold, btn_chu, btn_no_0, btn_no_1, btn_no_2, btn_no_3, btn_no_4,
				btn_no_5, btn_no_6, btn_no_7, btn_no_8, btn_no_9;
		CheckBox cb_sil;

		cb_sil = (CheckBox) view.findViewById(R.id.cb_sil);
		ll_digital = (LinearLayout) view.findViewById(R.id.ll_digital);
		ll_orient = (LinearLayout) view.findViewById(R.id.ll_orient);
		rg_model = ((RadioGroup) view.findViewById(R.id.rg_model));
		rb_orient = ((RadioButton) view.findViewById(R.id.rb_orient));
		rb_digital = ((RadioButton) view.findViewById(R.id.rb_digital));

		ib_on = view.findViewById(R.id.ib_on);
		btn_at = view.findViewById(R.id.btn_at);
		btn_volu = view.findViewById(R.id.btn_volu);
		btn_chd = view.findViewById(R.id.btn_chd);
		btn_vold = view.findViewById(R.id.btn_vold);
		btn_chu = view.findViewById(R.id.btn_chu);
		btn_no_0 = view.findViewById(R.id.btn_no_0);
		btn_no_1 = view.findViewById(R.id.btn_no_1);
		btn_no_2 = view.findViewById(R.id.btn_no_2);
		btn_no_3 = view.findViewById(R.id.btn_no_3);
		btn_no_4 = view.findViewById(R.id.btn_no_4);
		btn_no_5 = view.findViewById(R.id.btn_no_5);
		btn_no_6 = view.findViewById(R.id.btn_no_6);
		btn_no_7 = view.findViewById(R.id.btn_no_7);
		btn_no_8 = view.findViewById(R.id.btn_no_8);
		btn_no_9 = view.findViewById(R.id.btn_no_9);

		ib_on.setOnClickListener(onClickListener);
		btn_at.setOnClickListener(onClickListener);
		btn_volu.setOnClickListener(onClickListener);
		btn_chd.setOnClickListener(onClickListener);
		btn_vold.setOnClickListener(onClickListener);
		btn_chu.setOnClickListener(onClickListener);
		btn_no_0.setOnClickListener(onClickListener);
		btn_no_1.setOnClickListener(onClickListener);
		btn_no_2.setOnClickListener(onClickListener);
		btn_no_3.setOnClickListener(onClickListener);
		btn_no_4.setOnClickListener(onClickListener);
		btn_no_5.setOnClickListener(onClickListener);
		btn_no_6.setOnClickListener(onClickListener);
		btn_no_7.setOnClickListener(onClickListener);
		btn_no_8.setOnClickListener(onClickListener);
		btn_no_9.setOnClickListener(onClickListener);
		cb_sil.setOnClickListener(onClickListener);

		rg_model.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_digital){
					ll_digital.setVisibility(View.VISIBLE);
					ll_orient.setVisibility(View.GONE);
				} else if (checkedId == R.id.rb_orient){
					ll_digital.setVisibility(View.GONE);
					ll_orient.setVisibility(View.VISIBLE);
				}
			}
		});
		rb_orient.setChecked(true);

		mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
		final TextView tv_hint_above = (TextView) view.findViewById(R.id.tv_hint_above);
		final ImageView iv_hint_above = (ImageView) view.findViewById(R.id.iv_hint_above);
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

	private void initData(Television television) {

		if (television == null) return;
		isEnable = television.status == 1 ? true : false;
	}

	@Deprecated
	private View getAbove(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.room_control_above, null);
		ImageView big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		big_icon.setImageResource(R.drawable.room_control_above_tv);
		TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode_tile);
		tv_mode.setText("电视");
		TextView tv_mode_detail = ((TextView) view.findViewById(R.id.tv_mode_detail));
		tv_mode_detail.setVisibility(View.GONE);

		return view;
	}

	private void set(final String push_button) {
		if (!isEnable && !push_button.equals("on")) return;
		api_type = "POST";
		task = new SetTelevisionTask(getActivity(), new SetTelevisionTask.TaskCallBack() {
			@Override
			public void onCallBack(Television result) {
				handlePostResult(result, push_button);
			}
		});
		this.push_button = push_button;
		task.execute(this.push_button);
	}

	private void handlePostResult(Television result, String push_button) {
		if (getActivity() == null){
			return;
		}
		if (result == null){
			if (!NetWorkHelper.checkNetState(getActivity()))
				Toast.makeText(getActivity(), getActivity().getString(R.string.httpProblem), 450).show();
			else
				Utility.toastResult(getActivity(), getActivity().getString(R.string.unkownError));
		} else if (result.status == 200){

			if (push_button.equals("on")) isEnable = !isEnable;

		} else if (result.status == 401){

			Utility.showTokenErrorDialog(getActivity(), result.message);
		} else {
			Utility.toastResult(getActivity(), result.message);
		}
	}

	@Override
	public void onSelect() {
		if (load_status_enum != LOAD_STATUS_ENUM.SUCCUSS)
			updateRoomState();
		Log.e("@#$");
	}

	@Override
	public void onUpdate(JSONObject jsonObject) {
		Television television = JsonMapperUtils.toObject(jsonObject.toString(), Television.class);
		if (television == null || !television.device.equals("television"))
			return;
		if (api_type.equals("POST")){
			handlePostResult(television, push_button);
		} else if (api_type.equals("GET")){
			handelGetResult(television);

		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void updateRoomState() {
		api_type = "GET";
		progressDialog = ProgressDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager()
		).setMessage("正在加载中...").setTitle("提示")
				.setCancelable(true).show();
		getRoomStatusTask = new GetTelevisionTask(getActivity(), new GetTelevisionTask.TaskCallBack() {
			@Override
			public void onCallBack(Television result) {
				handelGetResult(result);
			}
		});
		getRoomStatusTask.execute();
	}

	private void handelGetResult(Television result) {
		if (getActivity() == null) return;
		load_status_enum = LOAD_STATUS_ENUM.FAIL;
		if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismiss();
		if (result == null){
			if (!NetWorkHelper.checkNetState(getActivity()))
				Toast.makeText(getActivity(), getActivity().getString(R.string.httpProblem), 450).show();
			else
				Toast.makeText(getActivity(), getActivity().getString(R.string.unkownError), 450).show();
		} else if (result.respone_status == 401){

			Utility.showTokenErrorDialog(getActivity(), "" + result.message);
		} else if (result.respone_status == 403){
			try {
				Constant.SYN_TIME_INTERVAL = new SimpleDateFormat("yyyyMMddHHmmss").parse(result.sys_time)
						.getTime() - System.currentTimeMillis();
				updateRoomState();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (result.respone_status != 200)
			Toast.makeText(getActivity(), "" + result.message, 450).show();
		else {
			load_status_enum = LOAD_STATUS_ENUM.SUCCUSS;
			initData(result);
		}
	}

}
