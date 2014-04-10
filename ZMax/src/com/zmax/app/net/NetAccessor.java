package com.zmax.app.net;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.zmax.app.model.CityLocation;
import com.zmax.app.model.FeeBack;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.JsonMapperUtils;
import com.zmax.app.utils.Log;

public class NetAccessor {
	
	private static final String TAG = NetAccessor.class.getSimpleName();
	
	public static CityLocation getCityLoacationByIp(Context context, String ak) {
		CityLocation cityLocation = null;
		try {
			JSONObject params = new JSONObject();
			params.put("ak", ak);
			
			String jsonString = HttpUtils.getByHttpClient(context, Constant.IP_LOCATION_URL, new BasicNameValuePair("ak", ak));
			// String jsonString = HttpUtil.sendRequest(context,
			// Constant.IP_LOCATION_URL+"ak="+ak, "POST", null);
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				cityLocation = new CityLocation();
				JSONObject result = new JSONObject(jsonString);
				if (result.has("content")) {
					JSONObject content = new JSONObject(result.optString("content"));
					if (content.has("address_detail")) {
						JSONObject address_detail = new JSONObject(content.optString("address_detail"));
						if (address_detail.has("province")) cityLocation.province = address_detail.optString("province");
						if (address_detail.has("city")) cityLocation.city = address_detail.optString("city");
						if (address_detail.has("city_code")) cityLocation.city_code = address_detail.optString("city_code");
					}
				}
			}
		}
		catch (Exception e) {
			Log.e(" getCityLoacationByIp Exception :" + e.toString());
			e.printStackTrace();
			
			cityLocation = null;
		}
		return cityLocation;
	}
	
	public static CityLocation getHotelsUpcoming(Context context, String ak) {
		CityLocation cityLocation = null;
		try {
			JSONObject params = new JSONObject();
			params.put("ak", ak);
			
			String jsonString = HttpUtils.getByHttpClient(context, Constant.IP_LOCATION_URL, new BasicNameValuePair("ak", ak));
			// String jsonString = HttpUtil.sendRequest(context,
			// Constant.IP_LOCATION_URL+"ak="+ak, "POST", null);
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				cityLocation = new CityLocation();
				JSONObject result = new JSONObject(jsonString);
				if (result.has("content")) {
					JSONObject content = new JSONObject(result.optString("content"));
					if (content.has("address_detail")) {
						JSONObject address_detail = new JSONObject(content.optString("address_detail"));
						if (address_detail.has("province")) cityLocation.province = address_detail.optString("province");
						if (address_detail.has("city")) cityLocation.city = address_detail.optString("city");
						if (address_detail.has("city_code")) cityLocation.city_code = address_detail.optString("city_code");
					}
				}
			}
		}
		catch (Exception e) {
			Log.e(" getCityLoacationByIp Exception :" + e.toString());
			e.printStackTrace();
			
			cityLocation = null;
		}
		return cityLocation;
	}
	
	public static FeeBack sendFeedBack(Context context, String contacts, String advise) {
		FeeBack feeBack = null;
		try {
			String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "feedbacks", new BasicNameValuePair("contacts",
					contacts), new BasicNameValuePair("advise", advise));
			Log.d("  responeString -->\n" + jsonString);
			feeBack = JsonMapperUtils.toObject(jsonString, FeeBack.class);
		}
		catch (Exception e) {
			Log.e("  Exception :" + e.toString());
			e.printStackTrace();
		}
		return feeBack;
	}
	
	/*
	 * 上报广告Log （01月新版）
	 * 
	 * @param context
	 * 
	 * @param logs
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	/*
	 * public static String uploadV2AdvertLogs(Context context, List<AdvertLog>
	 * advertLogList ) throws Exception{ Log.v( "****" + new
	 * Timestamp(System.currentTimeMillis()) + " start uploadV2AdvertLogs " +
	 * "****"); String jsonString = null; try{ // AdvertLogs logs = new
	 * AdvertLogs(); // logs.array.addAll(advertLogList); JSONObject params =
	 * new JSONObject(); JSONObject data = new JSONObject(); data.put("dt",
	 * "madvert_report"); data.put("logs", Util.logsToJson(advertLogList));
	 * params.put("data", data); params.put("cmd", "upload madvert report");
	 * params.put("cmdtype", "");
	 * 
	 * jsonString = HttpUtil.sendRequest(context,FANCY_URL, "POST",
	 * params.toString()); Log.d(
	 * "uploadV2AdvertLogs()  responeString -->"+jsonString); if
	 * (!TextUtils.isEmpty(jsonString)) { JSONObject result = new
	 * JSONObject(jsonString); if (result.has("rcd") && result.optInt("rcd") ==
	 * 0) { return jsonString; } }
	 * 
	 * }catch(Exception e){ Log.e(
	 * " NetAccessor uploadV2AdvertLogs Exception :"+e.toString()); throw e;
	 * }finally{ Log.v( "****" + new Timestamp(System.currentTimeMillis()) +
	 * " end uploadV2AdvertLogs ****"); } return null; }
	 */
	
}
