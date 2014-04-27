package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.model.BaseModel;
import com.zmax.app.net.NetAccessor;

public class ModifyChatUserInfoTask extends AsyncTask<String, Void, BaseModel> {
	private Context context;
	private TaskCallBack callBack;
	
	public ModifyChatUserInfoTask(Context context, TaskCallBack callBack) {
		super();
		this.context = context;
		this.callBack = callBack;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected BaseModel doInBackground(String... params) {
		BaseModel result = NetAccessor.vetfityNameDup(context, params[0], params[1], params[2]);
		return result;
	}
	
	@Override
	protected void onPostExecute(BaseModel result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(BaseModel result);
	}
}
