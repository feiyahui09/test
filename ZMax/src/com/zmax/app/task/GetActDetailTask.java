package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.model.ActDetail;
import com.zmax.app.net.NetAccessor;

public class GetActDetailTask extends AsyncTask<String, Void, ActDetail> {
	private Context context;
	private TaskCallBack callBack;
	
	public GetActDetailTask(Context context, TaskCallBack callBack) {
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
	protected ActDetail doInBackground(String... params) {
		
		ActDetail actDetail = NetAccessor.getActDetail(context, params[0]);
		// if (cityLocation != null) DBAccessor.saveObject(cityLocation);
		return actDetail;
	}
	
	@Override
	protected void onPostExecute(ActDetail result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(ActDetail result);
	}
}
