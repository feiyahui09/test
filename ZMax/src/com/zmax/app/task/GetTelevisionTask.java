package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.model.AirCondition;
import com.zmax.app.model.Television;
import com.zmax.app.net.NetAccessor;

public class GetTelevisionTask extends AsyncTask<String, Void, Television> {
	private Context context;
	private TaskCallBack callBack;
	
	public GetTelevisionTask(Context context, TaskCallBack callBack) {
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
	protected Television doInBackground(String... params) {
		Television television = NetAccessor.getTelevision(context);
		return television;
	}
	
	@Override
	protected void onPostExecute(Television result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(Television result);
	}
}
