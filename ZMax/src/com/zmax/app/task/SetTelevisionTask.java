package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.Login;
import com.zmax.app.model.Television;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.net.NetWorkHelper;

public class SetTelevisionTask extends AsyncTask<String, Void, Television> {
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
	protected Television doInBackground(String... params) {
		return NetAccessor.setTelevision(context, params[0]);
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
