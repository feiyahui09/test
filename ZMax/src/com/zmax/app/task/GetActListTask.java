package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.model.ActList;
import com.zmax.app.net.NetAccessor;

public class GetActListTask extends AsyncTask<String, Void, ActList> {
	private Context context;
	private TaskCallBack callBack;
	
	public GetActListTask(Context context, TaskCallBack callBack) {
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
	protected ActList doInBackground(String... params) {
		
		ActList actList = NetAccessor.getActList(context, params[0], params[1], params[2]);
		// if (cityLocation != null) DBAccessor.saveObject(cityLocation);
		return actList;
	}
	
	@Override
	protected void onPostExecute(ActList result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(ActList result);
	}
}
