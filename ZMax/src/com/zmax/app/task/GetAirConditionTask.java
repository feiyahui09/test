package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.model.AirCondition;
import com.zmax.app.net.NetAccessor;

public class GetAirConditionTask extends AsyncTask<String, Void, AirCondition> {
	private Context context;
	private TaskCallBack callBack;
	
	public GetAirConditionTask(Context context, TaskCallBack callBack) {
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
	protected AirCondition doInBackground(String... params) {
		AirCondition actList = NetAccessor.getAirCondition(context);
		return actList;
	}
	
	@Override
	protected void onPostExecute(AirCondition result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(AirCondition result);
	}
}
