package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.Login;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.net.NetWorkHelper;

public class SetTelevisionTask extends AsyncTask<String, Void, Integer> {
	private Context context;
	private TaskCallBack callBack;
	
	public SetTelevisionTask(Context context, TaskCallBack callBack) {
		super();
		this.context = context;
		this.callBack = callBack;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		Integer loginResult = Integer.valueOf(NetAccessor.setTelevision(context, params[0]));
		return loginResult;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		public void onCallBack(Integer result);
	}
}
