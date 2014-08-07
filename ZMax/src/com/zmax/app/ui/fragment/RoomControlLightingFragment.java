package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.zmax.app.R;
import com.zmax.app.chat.ChatHelper;
import com.zmax.app.chat.ClientCallback;
import com.zmax.app.model.Light;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetLightingStatusTask;
import com.zmax.app.task.LOAD_STATUS_ENUM;
import com.zmax.app.task.SetLightTask;
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

public class RoomControlLightingFragment extends Fragment implements RoomControlActivity.IUpdateRoomState {
	public static String[] mode_names = {"明亮模式", "电视模式", "阅读模式", "睡眠模式"};
	public static int[] mode_imgs = {R.drawable.room_control_lighting_bright_mode,
			R.drawable.room_control_lighting_tv_mode,
			R.drawable.room_control_lighting_reading_mode, R.drawable.room_control_lighting_sleep_mode};
	public static String[] mode_patterns = {"bright", "tv", "read", "sleep"};

	/* behind views */
	/*
	 * parent view
	 */
	protected View view;
	VerticalChangedCallback callback;
	String api_type = "GET";
	private Handler handler = new Handler();
	/* above views */
	private ImageView big_icon;
	private TextView tv_mode, tv_mode_detail;
	private ImageView iv_img, iv_hint_above;
	private TextView tv_mode_hint, tv_hint_above;
	private ImageButton ib_previous, ib_next;
	private Button btn_apply;
	private View dragview;
	private SetLightTask task;
	private Light light = null;
	private SlidingUpPanelLayout mLayout;
	private int curMode = 0;
	private int operaMode = 0;
	private OnClickListener ImgClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ib_previous){
				operaMode = (operaMode - 1 + 4) % 4;
				tv_mode_hint.setText(mode_names[operaMode]);
				tv_mode_detail.setText("当前灯光模式：" + mode_names[operaMode]);
				iv_img.setImageResource(mode_imgs[operaMode]);
			} else if (v.getId() == R.id.ib_next){
				operaMode = (operaMode + 1) % 4;
				tv_mode_hint.setText(mode_names[operaMode]);
				tv_mode_detail.setText("当前灯光模式：" + mode_names[operaMode]);
				iv_img.setImageResource(mode_imgs[operaMode]);
			} else if (v.getId() == R.id.btn_apply){
				set(mode_patterns[operaMode]);
			}
		}
	};
	private ClientCallback clientCallback = new ClientCallback() {

		@Override
		public void onGateEnter(JSONObject msg) {

		}

		@Override
		public void onConnectorEnter(JSONObject msg) {

		}

		@Override
		public void onEnterFailed(Exception e) {

		}

		@Override
		public void onChat(final String bodyMsg) {

		}

		@Override
		public void onKick(JSONObject bodyMsg) {

		}

		@Override
		public void onSendFailed(Exception e) {

		}

		@Override
		public void onForbidden(String errorMessage) {

		}

		@Override
		public void onDevise(final JSONObject devise) {

			handler.post(new Runnable() {
				@Override
				public void run() {
					try {
						if (devise!=null){
							Light result = JsonMapperUtils.toObject(devise.toString(), Light.class);
							if (api_type.equals("POST")){
								handlePostResult(result);
							} else if (api_type.equals("GET")){
								handleGetResult(result);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}
	};
	private IOCallback ioCallback = new IOCallback() {
		@Override
		public void onDisconnect() {

		}

		@Override
		public void onConnect() {

		}

		@Override
		public void onMessage(String s, IOAcknowledge ioAcknowledge) {

		}

		@Override
		public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {

		}

		@Override
		public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {

		}

		@Override
		public void onError(SocketIOException e) {

		}
	};
	private DialogFragment progressDialog;
	private GetLightingStatusTask getRoomStatusTask;
	private LOAD_STATUS_ENUM load_status_enum = LOAD_STATUS_ENUM.INIT;

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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void initData(Light light) {
		if (light != null){
			for (int i = 0; i < mode_patterns.length; i++) {
				if (light.pattern.equals(mode_patterns[i])){
					iv_img.setImageResource(mode_imgs[i]);
					tv_mode_hint.setText(mode_names[i]);
				}
			}
		}

		if (light != null){
			for (int i = 0; i < mode_patterns.length; i++) {
				if (light.pattern.equals(mode_patterns[i])){
					tv_mode_detail.setText(String.format("当前灯光模式：%s", mode_names[i]));
					operaMode = i;
					curMode = i;
				}
			}
		}

	}

	private void set(String pattern) {
		api_type = "POST";
		task = new SetLightTask(getActivity(), new SetLightTask.TaskCallBack() {
			@Override
			public void onCallBack(Light result) {
				handlePostResult(result);
			}
		});
		task.execute(pattern);
	}

	private void handlePostResult(Light result) {
		if (getActivity() == null){
			return;
		}
		if (result == null){
			if (!NetWorkHelper.checkNetState(getActivity()))
				Toast.makeText(getActivity(), getActivity().getString(R.string.httpProblem), 450).show();
			else
				Utility.toastResult(getActivity(), getActivity().getString(R.string.unkownError));
		} else if (result.status == 200){
			curMode = operaMode;
			Utility.toastResult(getActivity(), "已设置为" + mode_names[operaMode]);

		} else if (result.status == 401){

			Utility.showTokenErrorDialog(getActivity(), result.message);
		} else {
			Utility.toastResult(getActivity(), result.message);

		}
	}

	@Override
	public void onUpdateSelect() {
		ChatHelper.getHelper().setCallback(clientCallback);
		if (load_status_enum != LOAD_STATUS_ENUM.SUCCUSS)
			updateRoomState();
		Log.e("@#$");
	}

	@Override
	public void onUpdateUnselcet() {
		ChatHelper.getHelper().setCallback(null);

	}

	private void updateRoomState() {
		api_type = "GET";
		progressDialog = ProgressDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager()
		).setMessage("正在加载中...").setTitle("提示")
				.setCancelable(true).show();
		getRoomStatusTask = new GetLightingStatusTask(getActivity(), new GetLightingStatusTask.TaskCallBack() {
			@Override
			public void onCallBack(Light result) {
				handleGetResult(result);
			}
		});
		getRoomStatusTask.execute();
	}

	private void handleGetResult(Light result) {
		if (getActivity() == null) return;
		load_status_enum = LOAD_STATUS_ENUM.FAIL;
		if (progressDialog != null && progressDialog.getActivity() != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
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
