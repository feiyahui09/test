package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.model.Light;
import com.zmax.app.net.NetAccessor;

public class SetLightTask extends AsyncTask<String, Void, Light> {
	private Context context;
	private TaskCallBack callBack;
	
	public SetLightTask(Context context, TaskCallBack callBack) {
		super();
		this.context = context;
		this.callBack = callBack;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Light doInBackground(String... params) {
		return NetAccessor.setLight(context, params[0]);
	}
	
	@Override
	protected void onPostExecute(Light result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		public void onCallBack(Light result);
	}
}
