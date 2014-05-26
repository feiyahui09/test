package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.chat.Uploader;
import com.zmax.app.model.UploadResult;
import com.zmax.app.utils.Constant;

public class UploadImgTask extends AsyncTask<Object, Void, UploadResult> {
	private Context context;
	private TaskCallBack callBack;
	
	public UploadImgTask(Context context, TaskCallBack callBack) {
		super();
		this.context = context;
		this.callBack = callBack;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		
		
	}

	@Override
	protected UploadResult doInBackground(Object... params) {
		Uploader up = new Uploader(Constant.ZMAX_URL + "chat/image/upload", params[0]);
		return up.callPost();
	}
	
	@Override
	protected void onPostExecute(UploadResult result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		public void onCallBack(UploadResult result);
	}
}
