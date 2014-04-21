package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.Login;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.net.NetWorkHelper;

public class LoginPlayZmaxTask extends AsyncTask<String, Void, Login> {
	private Context context;
	private TaskCallBack callBack;
	private Login loginResult;
	
	public LoginPlayZmaxTask(Context context, TaskCallBack callBack) {
		super();
		this.context = context;
		this.callBack = callBack;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (!NetWorkHelper.checkNetState(context))
			Toast.makeText(context, context.getResources().getString(R.string.httpProblem), 300).show();
	}
	
	@Override
	protected Login doInBackground(String... params) {
		loginResult = NetAccessor.loginPlayZMAX(context);
		return loginResult;
	}
	
	@Override
	protected void onPostExecute(Login result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		public void onCallBack(Login loginResult);
	}
}
