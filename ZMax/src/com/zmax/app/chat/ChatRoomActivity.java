package com.zmax.app.chat;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmax.app.R;
import com.zmax.app.ZMaxApplication;
import com.zmax.app.adapter.GridViewFaceAdapter;
import com.zmax.app.chat.promelo.DataCallBack;
import com.zmax.app.model.UploadResult;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.task.UploadImgTask;
import com.zmax.app.ui.MainActivity;
import com.zmax.app.ui.base.BaseFragmentActivity;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.FileUtil;
import com.zmax.app.utils.ImageUtils;
import com.zmax.app.utils.JsonMapperUtils;
import com.zmax.app.utils.Log;
import com.zmax.app.utils.MediaUtils;
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
						hideAndHideIMM();
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
		
		// 强行重新初始化，更改聊天室图片缓存目录
		ImageLoader.getInstance().destroy();
		ZMaxApplication.initImageLoader4Chat();
		
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
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ImageLoader.getInstance().destroy();
		ZMaxApplication.initImageLoader();
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
				hideAndHideIMM();
				et_edit.requestFocus();
				chatHelper.send(et_edit.getText().toString(), new DataCallBack() {
					@Override
					public void responseData(final JSONObject arg0) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// et_edit.clearComposingText();
								et_edit.setText("");
								Log.i("" + arg0.toString());
							}
						});
					}
				}, false);
				break;
			case R.id.iv_pic:
				hideAndHideIMM();
				CharSequence[] items = { "图片", "拍照" };
				imageChooseItem(items);
				
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
				hideAndHideIMM();
				finish();
				break;
			case R.id.btn_share:
				hideAndHideIMM();
				startActivity(new Intent(mContext, ChatMoreActivity.class));
				
				break;
			case R.id.et_edit:
				showIMM();
				break;
			default:
				break;
		}
	}
	
	private String theOrgin;
	private String theThumbnail, theLarge;
	private File imgFile;
	
	public void imageChooseItem(CharSequence[] items) {
		AlertDialog imageDialog = new AlertDialog.Builder(this).setTitle("上传图片").setIcon(android.R.drawable.btn_star)
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 手机选图
						if (item == 0) {
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							intent.setType("image/*");
							startActivityForResult(Intent.createChooser(intent, "选择图片"), ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
						}
						// 拍照
						else if (item == 1) {
							String savePath = "";
							// 判断是否挂载了SD卡
							String storageState = Environment.getExternalStorageState();
							if (storageState.equals(Environment.MEDIA_MOUNTED)) {
								savePath = Constant.Chat.CHAT_IMG_CACHE_ABS_PATH;// 存放照片的文件夹
								File savedir = new File(savePath);
								if (!savedir.exists()) {
									savedir.mkdirs();
								}
							}
							
							// 没有挂载SD卡，无法保存文件
							if (TextUtils.isEmpty(savePath)) {
								Utility.toastResult(mContext, "无法保存照片，请检查SD卡是否挂载");
								return;
							}
							
							String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
							String fileName = "thumb_" + timeStamp + ".jpg";// 照片命名
							File out = new File(savePath, fileName);
							Uri uri = Uri.fromFile(out);
							
							theOrgin = savePath + fileName;// 该照片的绝对路径
							
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
							
							// if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
							// startActivityForResult(intent,
							// ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA_KITKAT);
							// }else
							startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
						}
					}
				}).create();
		
		imageDialog.show();
	}
	
	ChatMsg curUploadChatMsg = new ChatMsg();
	
	private void showImgNow(String thumbImagePath) {
		curUploadChatMsg.from = Constant.getLogin().nick_name;
		curUploadChatMsg.gender = Constant.getLogin().gender;
		curUploadChatMsg.type = "image";
		curUploadChatMsg.item_type = ChatListAdapter.VALUE_RIGHT_IMAGE;
		ChatMsgContent subMsg = new ChatMsgContent();
		subMsg.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		subMsg.content = thumbImagePath;
		curUploadChatMsg.msg = subMsg;
		show(curUploadChatMsg);
		
	}
	
	private void addImage(String imagePath) {
		curUploadChatMsg.msg.content = imagePath;
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (resultCode != RESULT_OK) return;
		
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1 && msg.obj != null) {
					
					final String large = ((Bundle) msg.obj).getString(Constant.Chat.CHAT_UPLOAD_IMG_LARGE_KEY);
					final String thumb = ((Bundle) msg.obj).getString(Constant.Chat.CHAT_UPLOAD_IMG_THUMB_KEY);
					
					// 显示图片
					// curUploadChatMsg = new ChatMsg();
					// showImgNow(thumb);
					Utility.toastResult(mContext, "图片上传中...");
					uploadImgTask = new UploadImgTask(mContext, new UploadImgTask.TaskCallBack() {
						@Override
						public void onCallBack(UploadResult result) {
							if (result == null) {
								if (!NetWorkHelper.checkNetState(mContext))
									Utility.toastNetworkFailed(mContext);
								else
									Utility.toastResult(mContext, "上传失败，请稍后再试！");
							}
							else if (result.status != 200) {
								Utility.toastResult(mContext, result.error);
							}
							else {
								Utility.toastResult(mContext, "图片上传成功！");
								// addImage(large);
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
					uploadImgTask.execute(thumb);
				}
			}
		};
		
		new Thread() {
			public void run() {
				Bitmap bitmap = null;
				
				if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD) {
					if (data == null) return;
					
					Uri thisUri = data.getData();
					String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(thisUri);
					
					// 如果是标准Uri
					if (TextUtils.isEmpty(thePath)) {
						theOrgin = ImageUtils.getAbsoluteImagePath((Activity) mContext, thisUri);
					}
					else {
						theOrgin = thePath;
					}
					
					final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
					
					// DocumentProvider
					if (isKitKat && DocumentsContract.isDocumentUri(mContext, thisUri)) {
						// ExternalStorageProvider
						if (ImageUtils.isExternalStorageDocument(thisUri)) {
							final String docId = DocumentsContract.getDocumentId(thisUri);
							final String[] split = docId.split(":");
							final String type = split[0];
							
							if ("primary".equalsIgnoreCase(type)) {
								theOrgin = Environment.getExternalStorageDirectory() + "/" + split[1];
							}
							
							// TODO handle non-primary volumes
						}
						// DownloadsProvider
						else if (ImageUtils.isDownloadsDocument(thisUri)) {
							
							final String id = DocumentsContract.getDocumentId(thisUri);
							final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
									Long.valueOf(id));
							
							theOrgin = getDataColumn(mContext, contentUri, null, null);
						}
						// MediaProvider
						else if (ImageUtils.isMediaDocument(thisUri)) {
							final String docId = DocumentsContract.getDocumentId(thisUri);
							final String[] split = docId.split(":");
							final String type = split[0];
							
							Uri contentUri = null;
							if ("image".equals(type)) {
								contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
							}
							else if ("video".equals(type)) {
								contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
							}
							else if ("audio".equals(type)) {
								contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
							}
							
							final String selection = "_id=?";
							final String[] selectionArgs = new String[] { split[1] };
							
							theOrgin = getDataColumn(mContext, contentUri, selection, selectionArgs);
						}
						
					}
					
					String attFormat = FileUtil.getFileFormat(theOrgin);
					if (!"photo".equals(MediaUtils.getContentType(attFormat))) {
						Log.i("attFormat:" + attFormat);
						Log.i("MediaUtils.getContentType(attFormat):" + MediaUtils.getContentType(attFormat));
						handler.post(new Runnable() {
							@Override
							public void run() {
								
								Toast.makeText(mContext, "无效图片类型！", Toast.LENGTH_SHORT).show();
							}
						});
						return;
					}
					
					// 获取图片缩略图 只有Android2.1以上版本支持
					String imgName = FileUtil.getFileName(theOrgin);
					bitmap = ImageUtils.loadImgThumbnail((Activity) mContext, imgName, MediaStore.Images.Thumbnails.MICRO_KIND);
					
					if (bitmap == null && !TextUtils.isEmpty(theOrgin)) {
						bitmap = ImageUtils.loadImgThumbnail(theOrgin, 100, 100);
					}
				}
				// 拍摄图片
				else if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
					if (bitmap == null && !TextUtils.isEmpty(theOrgin)) {
						bitmap = ImageUtils.loadImgThumbnail(theOrgin, 100, 100);
					}
				}
				
				if (bitmap != null) {
					// 存放照片的文件夹
					String savePath = Constant.Chat.CHAT_IMG_CACHE_ABS_PATH;
					File savedir = new File(savePath);
					if (!savedir.exists()) {
						savedir.mkdirs();
					}
					
					String largeFileName = FileUtil.getFileName(theOrgin);
					String largeFilePath = savePath + largeFileName;
					// 判断是否已存在缩略图
					if (largeFileName.startsWith("thumb_") && new File(largeFilePath).exists()) {
						theThumbnail = largeFilePath;
						imgFile = new File(theThumbnail);
					}
					else {
						// 生成上传的800宽度图片
						String thumbFileName = "thumb_" + largeFileName;
						theThumbnail = savePath + thumbFileName;
						if (new File(theThumbnail).exists()) {
							imgFile = new File(theThumbnail);
						}
						else {
							try {
								// 压缩上传的图片
								ImageUtils.createImageThumbnail(mContext, theOrgin, theThumbnail, Utility.getChatThumbImgSize(mContext),
										100);
								imgFile = new File(theThumbnail);
							}
							catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					// 保存动弹临时图片
					// ((AppContext)
					// getApplication()).setProperty(tempTweetImageKey,
					// theThumbnail);
					
					Message msg = new Message();
					msg.what = 1;
					Bundle bundle = new Bundle();
					bundle.putString(Constant.Chat.CHAT_UPLOAD_IMG_THUMB_KEY, theThumbnail);
					bundle.putString(Constant.Chat.CHAT_UPLOAD_IMG_LARGE_KEY, theOrgin);
					msg.obj = bundle;
					Log.d("theThumbnail: " + theThumbnail);
					Log.d("theLarge: " + theOrgin);
					
					handler.sendMessage(msg);
				}
			};
		}.start();
	}
	
	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };
		
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		}
		finally {
			if (cursor != null) cursor.close();
		}
		return null;
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
	
	private void hideAndHideIMM() {
		isIMMOrFaceShow = false;
		et_edit.clearFocus();// 隐藏软键盘
		hideFace();// 隐藏表情
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
							.setTitle("提示").setMessage("发送信息失败!").setRequestCode(Constant.DialogCode.TYPE_SEND_FAILED).show();
				}
			});
		}
		
		@Override
		public void onGateEnter(JSONObject msg) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					// showShortToast("已连接聊天室，正在为您转入对应聊天房间！");
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (dialog != null && dialog.getActivity() != null) dialog.dismiss();
							Utility.toastResult(mContext, "欢迎进入聊天室!");
						}
					});
				}
			});
		}
		
		@Override
		public void onEnterFailed(Exception e) {
			dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setPositiveButtonText("确定").setTitle("提示")
					.setMessage("连接错误！请稍后再试!").setCancelable(false).setRequestCode(Constant.DialogCode.TYPE_CONNECT_FAILED).show();
		}
		
		@Override
		public void onConnectorEnter(final JSONObject body) {
			
			if (body == null) {
				if (dialog != null && dialog.getActivity() != null) dialog.dismiss();
				dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setPositiveButtonText("确定")
						.setTitle("提示").setMessage(getString(R.string.unkownError)).setCancelable(false)
						.setRequestCode(Constant.DialogCode.TYPE_UNKOWNERROR).show();
				return;
			}
			boolean isError;
			final int code = body.optInt("code");
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
						if (code == 401) {
							SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setPositiveButtonText("确定")
									.setTitle("提示").setMessage(getString(R.string.tokenError))
									.setRequestCode(Constant.DialogCode.TYPE_TOKEN_ERROR).setCancelable(false).show();
						}
						else
							dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setTitle("提示")
									.setCancelable(false)
									.setMessage(TextUtils.isEmpty(message) ? getString(R.string.unkownError) : message)
									.setRequestCode(Constant.DialogCode.TYPE_CONNECTORENTER_ERROR).setPositiveButtonText("确定").show();
						
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
							.setTitle("提示").setMessage(_msg).setRequestCode(Constant.DialogCode.TYPE_FORBIDDEN).show();
				}
			});
		}
		
		@Override
		public void onKick(JSONObject body) {
			int code = body.optInt("code");
			
			if (code != 503) return;
			final String msg = body.optString("message");
			handler.post(new Runnable() {
				@Override
				public void run() {
					String _msg = TextUtils.isEmpty(msg) ? "聊天室正在维护中,请稍后再试！" : msg;
					dialog = SimpleDialogFragment.createBuilder(mContext, getSupportFragmentManager()).setPositiveButtonText("确定")
							.setTitle("提示").setMessage(_msg).setRequestCode(Constant.DialogCode.TYPE_FORBIDDEN).show();
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
							.setMessage("与聊天室连接超时！").setPositiveButtonText("确定").setRequestCode(Constant.DialogCode.TYPE_SOCKET_TIME_OUT)
							.show();
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
			case Constant.DialogCode.TYPE_SOCKET_TIME_OUT:
				finish();
				break;
			case Constant.DialogCode.TYPE_FORBIDDEN:
				finish();
				break;
			case Constant.DialogCode.TYPE_CONNECTORENTER_ERROR:
				finish();
				break;
			case Constant.DialogCode.TYPE_UNKOWNERROR:
				finish();
				break;
			case Constant.DialogCode.TYPE_CONNECT_FAILED:
				finish();
				break;
			case Constant.DialogCode.TYPE_SEND_FAILED:
				// finish();
				break;
			case Constant.DialogCode.TYPE_TOKEN_ERROR:
				Constant.saveLogin(null);
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setAction(Constant.DialogCode.ACTION_BACK_LOGIN);
				startActivity(intent);
				finish();
				break;
			default:
				break;
		}
		
	}
	
}
