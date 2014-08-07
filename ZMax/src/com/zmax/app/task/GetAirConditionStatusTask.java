package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import com.zmax.app.model.AirCondition;
import com.zmax.app.model.Television;
import com.zmax.app.net.NetAccessor;

public class GetAirConditionStatusTask extends AsyncTask<String, Void, AirCondition> {
	private Context context;
	private TaskCallBack callBack;

	public GetAirConditionStatusTask(Context context, TaskCallBack callBack) {
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
		AirCondition result = null;
		try {
			result = NetAccessor.getAirCondition(context);
		} catch (Exception e) {
			result = null;
			e.printStackTrace();

		}
		return result;
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
