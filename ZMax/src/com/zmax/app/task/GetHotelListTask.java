package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zmax.app.R;
import com.zmax.app.model.HotelList;
import com.zmax.app.net.NetAccessor;
import com.zmax.app.net.NetWorkHelper;

public class GetHotelListTask extends AsyncTask<String, Void, Void> {
	private Context context;
	private TaskCallBack callBack;
	private HotelList hotelList, upcomingHotelList;
	
	public GetHotelListTask(Context context, TaskCallBack callBack) {
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
	protected Void doInBackground(String... params) {
		
		hotelList = NetAccessor.getHotelList(context, params[0], params[1], params[2]);
		upcomingHotelList = NetAccessor.getHotelUpcomingList(context);
		// if (cityLocation != null) DBAccessor.saveObject(cityLocation);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		callBack.onCallBack(hotelList, upcomingHotelList);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(HotelList hotelList, HotelList upcomingHotelList);
	}
}
