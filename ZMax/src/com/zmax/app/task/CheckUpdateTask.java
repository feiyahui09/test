package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.Update;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.net.NetWorkHelper;

public class CheckUpdateTask extends AsyncTask<String, Void, Update> {
	private Context context;
	private TaskCallBack callBack;
	
	public CheckUpdateTask(Context context, TaskCallBack callBack) {
		super();
		this.context = context;
		this.callBack = callBack;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (!NetWorkHelper.checkNetState(context))
			Toast.makeText(context, context.getResources().getString(R.string.httpProblem), 300).show();
	}
	
	@Override
	protected Update doInBackground(String... params) {
		
		Update update = NetAccessor.checkUpdateVersion(context, params[0]);
		// if (cityLocation != null) DBAccessor.saveObject(cityLocation);
		return update;
	}
	
	@Override
	protected void onPostExecute(Update result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(Update result);
	}
}
