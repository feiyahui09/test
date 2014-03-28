package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.model.CityLocation;
import com.zmax.app.net.NetAccessor;

public class GetCityLocationTask extends AsyncTask<String, Void, CityLocation> {
	private Context context;
	private TaskCallBack callBack;
	
	public GetCityLocationTask(Context context, TaskCallBack callBack) {
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
	protected CityLocation doInBackground(String... params) {
		
		return NetAccessor.getCityLoacationByIp(context, params[0]);
	}
	
	@Override
	protected void onPostExecute(CityLocation result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(CityLocation result);
	}
}
