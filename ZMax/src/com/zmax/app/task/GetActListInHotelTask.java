package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.ActList;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.net.NetWorkHelper;

public class GetActListInHotelTask extends AsyncTask<String, Void, ActList> {
	private Context context;
	private TaskCallBack callBack;
	
	public GetActListInHotelTask(Context context, TaskCallBack callBack) {
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
	protected ActList doInBackground(String... params) {
		
		ActList actList = NetAccessor.getActListInHotel(context, params[0], params[1], params[2]);
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
