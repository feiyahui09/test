package com.zmax.app.chat;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.zmax.app.R;
import com.zmax.app.adapter.GridViewFaceAdapter;
import com.zmax.app.chat.promelo.DataCallBack;
import com.zmax.app.model.UploadResult;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.UploadImgTask;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.JsonMapperUtils;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.PhoneUtil;
import com.zmax.app.utils.Utility;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.ProgressDialogFragment;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class ChatRoomActivity extends BaseFragmentActivity implements OnClickListener, ISimpleDialogListener {
	private Context mContext;
	
	private Button btn_Back, btn_Share, btn_send;
	private ImageView iv_pic, iv_emotion;
	private EditText et_edit;
	private ListView lv_chat;
	private ChatListAdapter adapter;
	private ChatHelper chatHelper;
	private TextView tv_title;
	private DialogFragment dialog;
	UploadImgTask uploadImgTask;
	
	private static final long CHAT_MUTE_DURATION = 10 * 60 * 1000;// default 10
	private static long last_chat_time = 0;
	private static String self_user_name = "围观淡定哥";
	private static int self_user_gender = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_room);
		init();
		initGridView();
		initChatPomelo();
	}
	
	private void init() {
		mContext = this;
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		btn_Back = (Button) findViewById(R.id.btn_back);
		btn_Share = (Button) findViewById(R.id.btn_share);
		btn_Share.setVisibility(View.VISIBLE);
		btn_Share.setBackgroundResource(R.drawable.chat_more_menu_sel);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		iv_emotion = (ImageView) findViewById(R.id.iv_emotion);
		btn_send = (Button) findViewById(R.id.btn_send);
		tv_title = (TextView) findViewById(R.id.tv_title);
		
		iv_pic.setOnClickListener(this);
		iv_emotion.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_Share.setOnClickListener(this);
		btn_Back.setOnClickListener(this);
		
		tv_title.setText("聊天室");
		et_edit = (EditText) findViewById(R.id.et_edit);
		et_edit.setOnClickListener(this);
		et_edit.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				chatHelper.send(et_edit.getText().toString(), new DataCallBack() {
					@Override
					public void responseData(final JSONObject arg0) {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								et_edit.clearComposingText();
								et_edit.setText("");
								Log.i("" + arg0.toString());
							}
						});
					}
				}, false);
				return false;
			}
		});
		
		et_edit.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (isIMMOrFaceShow) {
						isIMMOrFaceShow = false;
						et_edit.clearFocus();// 隐藏软键盘
						// et_edit.setVisibility(View.GONE);// 隐藏编辑框
						hideFace();// 隐藏表情
						return true;
					}
				}
				return false;
			}
		});
		lv_chat = (ListView) findViewById(R.id.list_view);
		adapter = new ChatListAdapter(mContext);
		lv_chat.setAdapter(adapter);
		
	}
	
	boolean isIMMOrFaceShow = false;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String name = Constant.getLogin().nick_name;
		if (TextUtils.isEmpty(name)) {
			return;
		}
		self_user_gender = Constant.getLogin().gender;
		self_user_name = name;
		
		// 修改发送的用户信息
		if (chatHelper != null) chatHelper.modifyInfo(self_user_name, self_user_gender);
		// 修改名字性别后，刷新原有聊天列表
		if (adapter != null && adapter.getCount() > 0) {
			for (ChatMsg message : adapter.getMsgList()) {
				if (message.item_type == ChatListAdapter.VALUE_RIGHT_TEXT || message.item_type == ChatListAdapter.VALUE_RIGHT_IMAGE) {
					message.gender = self_user_gender;
					message.from = self_user_name;
				}
			}
			adapter.notifyDataSetChanged();
		}
	}
	
	// 192.168.0.69 //测试环境
	// 192.168.10.46 //old
	private void initChatPomelo() {
		
		dialog = ProgressDialogFragment.createBuilder(this, getSupportFragmentManager()).setMessage("正在连接中...").setTitle("提示")
				.setCancelable(true).show();
		chatHelper = ChatHelper.getHelper();
		try {
			chatHelper.init(this, Constant.Chat.CHAT_SERVER_IP, Constant.Chat.CHAT_SERVER_PORT, Constant.getLogin().user_id,
					Constant.getLogin().pms_hotel_id, Constant.getLogin().auth_token, Constant.getLogin().nick_name,
					Constant.getLogin().gender, clientCallback, ioCallback);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
			case R.id.btn_send:
				if (Utility.isETNull(et_edit)) {
					Utility.toastResult(mContext, "输入内容不能为空哦！");
					return;
				}
				chatHelper.send(et_edit.getText().toString(), new DataCallBack() {
					@Override
					public void responseData(final JSONObject arg0) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								et_edit.clearComposingText();
								et_edit.setText("");
								Log.i("" + arg0.toString());
							}
						});
					}
				}, false);
				break;
			case R.id.iv_pic:
				
				uploadImgTask = new UploadImgTask(mContext, new UploadImgTask.TaskCallBack() {
					@Override
					public void onCallBack(UploadResult result) {
						if (result == null) {
							if (!NetWorkHelper.checkNetState(mContext))
								Utility.toastNetworkFailed(mContext);
							else
								Utility.toastFailedResult(mContext);
						}
						else if (result.status != 200) {
							Utility.toastResult(mContext, result.message);
						}
						else {
							Utility.toastResult(mContext, "ok .");
							
							chatHelper.send(result.image, new DataCallBack() {
								@Override
								public void responseData(final JSONObject arg0) {
									handler.post(new Runnable() {
										@Override
										public void run() {
											et_edit.clearComposingText();
											et_edit.setText("");
											Log.i("" + arg0.toString());
										}
									});
								}
							}, true);
						}
					}
				});
				uploadImgTask.execute("sdcard/Download/p1.png");
				
				// new AlertDialog.Builder(mContext).setTitle("title")
				// .setItems(R.array.pic_dialog_items, new
				// DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// if (which == 0) {
				// }
				// else if (which == 1) {
				// }
				// }
				// }).show();
				break;
			case R.id.iv_emotion:
				// et_edit.setVisibility(View.VISIBLE);
				// et_edit.requestFocus();
				// et_edit.requestFocusFromTouch();
				// imm.showSoftInput(et_edit, 0);//显示软键盘
				showOrHideIMM();
				break;
			
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_share:
				startActivity(new Intent(mContext, ChatMoreActivity.class));
				
				break;
			case R.id.et_edit:
				showIMM();
				break;
			default:
				break;
		}
	}
	
	private void show(ChatMsg chatMsg) {
		adapter.addItem(chatMsg);
		/**
		 * 做一个 下方有新消息的提示
		 */
		lv_chat.setSelection(lv_chat.getCount() - 1);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHelper != null) chatHelper.disConnect();
	}
	
	private InputMethodManager imm;
	private GridView mGridView;
	private GridViewFaceAdapter mGVFaceAdapter;
	
	// 初始化表情控件
	private void initGridView() {
		mGVFaceAdapter = new GridViewFaceAdapter(this);
		mGridView = (GridView) findViewById(R.id.gv_emotions);
		mGridView.setAdapter(mGVFaceAdapter);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 插入的表情
				SpannableString ss = new SpannableString(view.getTag(R.id.emotion_string).toString());
				Drawable d = getResources().getDrawable((int) mGVFaceAdapter.getItemId(position));
				d.setBounds(0, 0, PhoneUtil.dip2px(mContext, Constant.Chat.EMOTION_DIMEN),
						PhoneUtil.dip2px(mContext, Constant.Chat.EMOTION_DIMEN));// 设置表情图片的显示大小
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				ss.setSpan(span, 0, view.getTag(R.id.emotion_string).toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 在光标所在处插入表情
				et_edit.getText().insert(et_edit.getSelectionStart(), ss);
			}
		});
	}
	
	private void showIMM() {
		iv_emotion.setTag(1);
		showOrHideIMM();
	}
	
	private void showFace() {
		iv_emotion.setImageResource(R.drawable.chat_keyboard);
		iv_emotion.setTag(1);
		mGridView.setVisibility(View.VISIBLE);
	}
	
	private void hideFace() {
		iv_emotion.setImageResource(R.drawable.chat_emotion);
		iv_emotion.setTag(null);
		mGridView.setVisibility(View.GONE);
	}
	
	private void showOrHideIMM() {
		isIMMOrFaceShow = true;
		if (iv_emotion.getTag() == null) {
			// 显示表情
			showFace();
			// 隐藏软键盘
			imm.hideSoftInputFromWindow(et_edit.getWindowToken(), 0);
		}
		else {
			// 隐藏表情
			hideFace();
			// 显示软键盘
			imm.showSoftInput(et_edit, 0);
			
		}
	}
	
	private ClientCallback clientCallback = new ClientCallback() {
		
		@Override
		public void onSendFailed(Exception e) {
			
			handler.post(new Runnable() {
				@Override
				public void run() {
					dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setPositiveButtonText("确定")
							.setTitle("提示").setMessage("发送信息失败!").setRequestCode(TYPE_SEND_FAILED).show();
				}
			});
		}
		
		@Override
		public void onGateEnter(JSONObject msg) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					// showShortToast("已连接聊天室，正在为您转入对应聊天房间！");
					if (dialog != null && dialog.getActivity() != null) dialog.dismiss();
					handler.post(new Runnable() {
						@Override
						public void run() {
							Utility.toastResult(mContext, "欢迎进入聊天室!");
						}
					});
				}
			});
		}
		
		@Override
		public void onEnterFailed(Exception e) {
			dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setPositiveButtonText("确定").setTitle("提示")
					.setMessage("连接错误！请稍后再试!").setCancelable(false).setRequestCode(TYPE_CONNECT_FAILED).show();
		}
		
		@Override
		public void onConnectorEnter(final JSONObject body) {
			
			if (body == null) {
				if (dialog != null && dialog.getActivity() != null) dialog.dismiss();
				dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setPositiveButtonText("确定")
						.setTitle("提示").setMessage(getString(R.string.unkownError)).setCancelable(false).setRequestCode(TYPE_UNKOWNERROR)
						.show();
				return;
			}
			int code = 200;
			boolean isError;
			code = body.optInt("code", 200);
			final String message = body.optString("message");
			isError = body.optBoolean("error");
			
			if (!isError) {
				// handler.postDelayed(sendRunnable, 4000);
				// handler.post(new Runnable() {
				// @Override
				// public void run() {
				// Utility.toastResult(mContext, "欢迎进入聊天室!");
				// }
				// });
			}
			else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						String _msg = TextUtils.isEmpty(message) ? getString(R.string.unkownError) : message;
						dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setTitle("提示")
								.setCancelable(false).setMessage(_msg).setRequestCode(TYPE_CONNECTORENTER_ERROR)
								.setPositiveButtonText("确定").show();
					}
				});
				chatHelper.disConnect();
			}
		}
		
		@Override
		public void onChat(final String msg) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					/**
					 * 聊天没有聊天信息时，时间显示则以第一个发言人时间，隔10分钟没有人再次发言，时间显示则以下个发言时间
					 * 聊天室连续发言，则不需要显示每个用户发言时间
					 */
					if (System.currentTimeMillis() > last_chat_time + CHAT_MUTE_DURATION) {
						ChatMsg chatMsg = new ChatMsg();
						chatMsg.tipTime = new SimpleDateFormat("HH:mm").format(new Date());
						show(chatMsg);
					}
					last_chat_time = System.currentTimeMillis();
					
					ChatMsg chatMsg = JsonMapperUtils.toObject(msg, ChatMsg.class);
					// 自己发的信息，右边
					if (self_user_name.equals(chatMsg.from)) {
						if ("image".equals(chatMsg.type) || chatMsg.msg.content.endsWith(".png") || chatMsg.msg.content.endsWith(".jpg")) {
							chatMsg.item_type = ChatListAdapter.VALUE_RIGHT_IMAGE;
						}
						else
							chatMsg.item_type = ChatListAdapter.VALUE_RIGHT_TEXT;
					}
					else {
						// 其他人，左边
						if ("image".equals(chatMsg.type) || chatMsg.msg.content.endsWith(".png") || chatMsg.msg.content.endsWith(".jpg")) {
							chatMsg.item_type = ChatListAdapter.VALUE_LEFT_IMAGE;
						}
						else
							chatMsg.item_type = ChatListAdapter.VALUE_LEFT_TEXT;
					}
					show(chatMsg);
				}
			});
		}
		
		@Override
		public void onForbidden(final String msg) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					String _msg = TextUtils.isEmpty(msg) ? "你已经被禁言！" : msg;
					dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setPositiveButtonText("确定")
							.setTitle("提示").setMessage(_msg).setRequestCode(TYPE_FORBIDDEN).show();
				}
			});
		}
	};
	
	/**
	 * 这里用于处理socket timeout 的情况
	 */
	private IOCallback ioCallback = new IOCallback() {
		
		@Override
		public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
			
		}
		
		@Override
		public void onMessage(String arg0, IOAcknowledge arg1) {
			
		}
		
		@Override
		public void onError(SocketIOException arg0) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setTitle("提示").setCancelable(false)
							.setMessage("与聊天室连接超时！").setPositiveButtonText("确定").setRequestCode(TYPE_SOCKET_TIME_OUT).show();
				}
			});
		}
		
		@Override
		public void onDisconnect() {
		}
		
		@Override
		public void onConnect() {
			
		}
		
		@Override
		public void on(String arg0, IOAcknowledge arg1, Object... arg2) {
			
		}
	};
	
	@Override
	public void onNegativeButtonClicked(int arg0) {
		
	}
	
	@Override
	public void onPositiveButtonClicked(int arg0) {
		switch (arg0) {
			case TYPE_SOCKET_TIME_OUT:
				finish();
				break;
			case TYPE_FORBIDDEN:
				finish();
				break;
			case TYPE_CONNECTORENTER_ERROR:
				finish();
				break;
			case TYPE_UNKOWNERROR:
				finish();
				break;
			case TYPE_CONNECT_FAILED:
				finish();
				break;
			case TYPE_SEND_FAILED:
				// finish();
				break;
			default:
				break;
		}
		
	}
	
	static final int TYPE_SEND_FAILED = 5;
	static final int TYPE_UNKOWNERROR = 6;
	static final int TYPE_FORBIDDEN = 2;
	static final int TYPE_CONNECTORENTER_ERROR = 3;
	static final int TYPE_SOCKET_TIME_OUT = 1;
	static final int TYPE_CONNECT_FAILED = 4;
	
}
