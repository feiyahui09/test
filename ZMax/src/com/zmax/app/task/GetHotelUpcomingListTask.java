package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.model.HotelList;
import com.zmax.app.net.NetAccessor;

public class GetHotelUpcomingListTask extends AsyncTask<String, Void, HotelList> {
	private Context context;
	private TaskCallBack callBack;
	
	public GetHotelUpcomingListTask(Context context, TaskCallBack callBack) {
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
	protected HotelList doInBackground(String... params) {
		
		HotelList hotelList = NetAccessor.getHotelUpcomingList(context );
		// if (cityLocation != null) DBAccessor.saveObject(cityLocation);
		return hotelList;
	}
	
	@Override
	protected void onPostExecute(HotelList result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(HotelList result);
	}
}
