package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import com.zmax.app.model.Light;
import com.zmax.app.model.RoomStatus;
import com.zmax.app.net.NetAccessor;

public class GetLightingStatusTask extends AsyncTask<String, Void, Light> {
	private Context context;
	private TaskCallBack callBack;

	public GetLightingStatusTask(Context context, TaskCallBack callBack) {
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
	protected Light doInBackground(String... params) {
		Light result = null;
		try {
			result = NetAccessor.getLight(context);
		}
		catch (Exception e) {
			result = null;
			e.printStackTrace();
			
		}
		return result;
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
