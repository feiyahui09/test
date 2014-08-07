package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import com.zmax.app.model.Television;
import com.zmax.app.net.NetAccessor;

public class GetTVStatusTask extends AsyncTask<String, Void, Television> {
	private Context context;
	private TaskCallBack callBack;

	public GetTVStatusTask(Context context, TaskCallBack callBack) {
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
		Television result = null;
		try {
			result = NetAccessor.getTelevision(context);
		} catch (Exception e) {
			result = null;
			e.printStackTrace();

		}
		return result;
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
