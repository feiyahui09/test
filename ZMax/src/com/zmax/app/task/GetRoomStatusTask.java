package com.zmax.app.task;

import android.content.Context;
import android.os.AsyncTask;

import com.zmax.app.R;
import com.zmax.app.model.RoomStatus;
import com.zmax.app.net.NetAccessor;

public class GetRoomStatusTask extends AsyncTask<String, Void, RoomStatus> {
	private Context context;
	private TaskCallBack callBack;
	
	public GetRoomStatusTask(Context context, TaskCallBack callBack) {
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
	protected RoomStatus doInBackground(String... params) {
		RoomStatus result = null;
		try {
			result = new RoomStatus();
			result.light = NetAccessor.getLight(context);
			result.airCondition = NetAccessor.getAirCondition(context);
			result.television = NetAccessor.getTelevision(context);
			if ((result.light != null && result.light.status == 200 && result.airCondition != null
					&& result.airCondition.respone_status == 200 && result.television != null && result.television.respone_status == 200)) {
				result.status = 200;
			}
			else {
				result.status = result.light.status;
				result.message = result.light.message;// result.light可能为空，但没关系，后面有处理
			}
		}
		
		catch (Exception e) {
			if (e.getMessage().equals(context.getString(R.string.tokenError))) {
				result.status = 401;
			}
			else
				result = null;
			e.printStackTrace();
			
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(RoomStatus result) {
		super.onPostExecute(result);
		callBack.onCallBack(result);
	}
	
	public interface TaskCallBack {
		
		public void onCallBack(RoomStatus result);
	}
}
