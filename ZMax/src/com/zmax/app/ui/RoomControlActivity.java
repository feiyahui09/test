package com.zmax.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;
import com.zmax.app.R;
import com.zmax.app.adapter.ActDetailAdapter;
import com.zmax.app.chat.ChatHelper;
import com.zmax.app.chat.ClientCallback;
import com.zmax.app.model.RoomStatus;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.GetRoomStatusTask;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.ui.fragment.RoomControlAirConditionFragment;
import com.zmax.app.ui.fragment.RoomControlLightingFragment;
import com.zmax.app.ui.fragment.RoomControlTVFragment;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.SmartViewPager;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.ProgressDialogFragment;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RoomControlActivity extends BaseFragmentActivity implements ISimpleDialogListener {

	private static final String TAG = RoomControlActivity.class.getSimpleName();
	public static boolean isCurAbove = true;
	private ViewGroup above_content_header;
	private Button btn_Back;
	private TextView tv_title;
	private SmartViewPager pager;
	private ActDetailAdapter adapter;
	private Context mContext;
	private VerticalChangedCallback callback;
	private PageChangedCallback pageChangedCallback;
	private ImageView iv_right, iv_left;
	private int curPageIndex = 0;
	private GetRoomStatusTask getRoomStatusTask;
	private DialogFragment progressDialog;
	private ChatHelper chatHelper;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_control);
		initHeader();
//		initChatPomelo();
		try {
			updateRoomStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateRoomStatus() {
		init();
		adapter.addTab(new RoomControlLightingFragment(callback, null));
		adapter.addTab(new RoomControlAirConditionFragment(callback, null));
		adapter.addTab(new RoomControlTVFragment(callback, null));
		adapter.notifyDataSetChanged();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					onPagerSelect(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 500);
	}

	private void initChatPomelo() {
		progressDialog = ProgressDialogFragment.createBuilder(this, getSupportFragmentManager()).setMessage("正在加载中.." +
				".").setTitle("提示")
				.setCancelable(true).show();
		chatHelper = ChatHelper.getHelper();
		try {
			chatHelper.init(this, Constant.Chat.CHAT_SERVER_IP, Constant.Chat.CHAT_SERVER_PORT,
					Constant.getLogin().user_id,
					Constant.getLogin().pms_hotel_id, Constant.getLogin().auth_token, Constant.getLogin().nick_name,
					Constant.getLogin().gender, new ClientCallback() {
						@Override
						public void onGateEnter(JSONObject msg) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									if (progressDialog != null && progressDialog.getActivity() != null)
										progressDialog.dismissAllowingStateLoss();
									init();
									adapter.addTab(new RoomControlLightingFragment(callback, null));
									adapter.addTab(new RoomControlAirConditionFragment(callback, null));
									adapter.addTab(new RoomControlTVFragment(callback, null));
									adapter.notifyDataSetChanged();

								}
							});
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									try {
										onPagerSelect(0);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}, 500);
						}

						@Override
						public void onConnectorEnter(JSONObject msg) {

						}

						@Override
						public void onEnterFailed(Exception e) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									init();
								}
							});

						}

						@Override
						public void onChat(String bodyMsg) {

						}

						@Override
						public void onKick(JSONObject bodyMsg) {
							Log.i(bodyMsg.toString());

						}

						@Override
						public void onSendFailed(Exception e) {

						}

						@Override
						public void onForbidden(String errorMessage) {

						}

						@Override
						public void onZmax(final JSONObject result) {
							Log.i(result.toString());
							handler.post(new Runnable() {
								@Override
								public void run() {
									try {
										if (result != null && result.has("device")){
											String device = result.optString("device");
											if (device.equals("scene")){
												((IUpdateRoomState) adapter.getItem(0)).onUpdateByPomelo(result);
											} else if (device.equals("air_condiction")){
												((IUpdateRoomState) adapter.getItem(1)).onUpdateByPomelo(result);
											} else if (device.equals("television")){
												((IUpdateRoomState) adapter.getItem(2)).onUpdateByPomelo(result);
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

						}
					}, new IOCallback() {
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
						public void onMessage(JSONObject object, IOAcknowledge ioAcknowledge) {

						}

						@Override
						public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {

						}

						@Override
						public void onError(SocketIOException e) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									init();
								}
							});

						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHelper != null) chatHelper.disConnect();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Deprecated
	private void updateRoomState() {

		progressDialog = ProgressDialogFragment.createBuilder(this, getSupportFragmentManager()).setMessage("正在加载中.." +
				".").setTitle("提示")
				.setCancelable(true).show();

		getRoomStatusTask = new GetRoomStatusTask(this, new GetRoomStatusTask.TaskCallBack() {
			@Override
			public void onCallBack(RoomStatus result) {
				if (progressDialog != null && progressDialog.getActivity() != null) progressDialog.dismissAllowingStateLoss();
				if (result == null){
					if (!NetWorkHelper.checkNetState(mContext))
						Toast.makeText(mContext, mContext.getString(R.string.httpProblem), 450).show();
					else
						Toast.makeText(mContext, mContext.getString(R.string.unkownError), 450).show();
				} else if (result.status == 401){

					Utility.showTokenErrorDialog(RoomControlActivity.this, "" + result.message);
				} else if (result.status == 403){
					try {
						Constant.SYN_TIME_INTERVAL = new SimpleDateFormat("yyyyMMddHHmmss").parse(result.sys_time)
								.getTime() - System.currentTimeMillis();
						updateRoomState();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (result.status != 200)
					Toast.makeText(mContext, "" + result.message, 450).show();
				else {
					initData(result);
				}

			}
		});
		getRoomStatusTask.execute();
	}

	private void init() {
		mContext = this;
		iv_right = (ImageView) findViewById(R.id.iv_right);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_right.setVisibility(View.VISIBLE);
		iv_left.setVisibility(View.VISIBLE);
		iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (curPageIndex > 0) pager.setCurrentItem(--curPageIndex);
			}
		});
		iv_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (curPageIndex < adapter.getCount() - 1) pager.setCurrentItem(++curPageIndex);
			}
		});

		pager = (SmartViewPager) findViewById(R.id.pager);
		adapter = new ActDetailAdapter(this);
		pager.setAdapter(adapter);
		// retained buffer size 6
		pager.setOffscreenPageLimit(3);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				onPagerSelect(position);
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
		callback = new VerticalChangedCallback() {

			@Override
			public void onCallBack(boolean isCurAbove) {
				if (isCurAbove){

					showIndicator();
					pager.setCanScroll(true);
				} else {
					hideIndicator();
					pager.setCanScroll(false);
				}

			}
		};
		pageChangedCallback = new PageChangedCallback() {

			@Override
			public void onPageChanegdCallBack(int index) {

				pager.setCurrentItem(index);

			}
		};
	}

	private void onPagerSelect(int position) {
		curPageIndex = position;
		showIndicator();
		ArrayList<Fragment> fragments = adapter.getFragments();
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			if (fragment instanceof IUpdateRoomState){
				IUpdateRoomState iUpdateRoomState = (IUpdateRoomState) fragment;
				if (i == position)
					iUpdateRoomState.onSelect();
			}
		}
	}

	private void initData(RoomStatus result) {

		adapter.addTab(new RoomControlLightingFragment(callback, result.light));
		adapter.addTab(new RoomControlAirConditionFragment(callback, result.airCondition));
		adapter.addTab(new RoomControlTVFragment(callback, result.television));
		adapter.notifyDataSetChanged();
		pager.setCurrentItem(0);
	}

	private void initHeader() {
		above_content_header = (ViewGroup) findViewById(R.id.above_content_header);
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.room_control));
	}

	private void hideIndicator() {
		iv_right.setVisibility(View.GONE);
		iv_left.setVisibility(View.GONE);
	}

	private void showIndicator() {

		if (curPageIndex == 0){
			iv_right.setVisibility(View.VISIBLE);
			iv_left.setVisibility(View.GONE);
		} else if (curPageIndex == adapter.getCount() - 1){

			iv_right.setVisibility(View.GONE);
			iv_left.setVisibility(View.VISIBLE);
		} else {
			iv_right.setVisibility(View.VISIBLE);
			iv_left.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onNegativeButtonClicked(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPositiveButtonClicked(int arg0) {
		switch (arg0) {
			case Constant.DialogCode.TYPE_TOKEN_ERROR:
				Constant.saveLogin(null);
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.setAction(Constant.DialogCode.ACTION_BACK_LOGIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				break;

			default:
				break;
		}
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


	public interface IUpdateRoomState {
		public void onSelect();

		public void onUpdateByPomelo(JSONObject result);

	}

	public interface VerticalChangedCallback {
		public void onCallBack(boolean isCurAbove);
	}

	public interface PageChangedCallback {
		public void onPageChanegdCallBack(int index);
	}
}
