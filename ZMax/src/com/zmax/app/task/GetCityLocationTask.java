package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.db.DBAccessor;
import com.zmax.app.db.DBHelper;
import com.zmax.app.model.CityLocation;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.net.NetWorkHelper;
import com.zmax.app.utils.Utility;

public class GetCityLocationTask extends AsyncTask<String, Void, CityLocation> {
	private Context context;
	private TaskCallBack callBack;
	
	public GetCityLocationTask(Context context, TaskCallBack callBack) {
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
	protected CityLocation doInBackground(String... params) {
		
		CityLocation cityLocation = NetAccessor.getCityLoacationByIp(context, params[0]);
//		if (cityLocation != null) DBAccessor.saveObject(cityLocation);
		return cityLocation;
	}
	
	@Override
	protected void onPostExecute(CityLocation result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(CityLocation result);
	}
}
