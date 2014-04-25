package com.zmax.app.net;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.zmax.app.model.ActDetail;
import com.zmax.app.model.ActList;
import com.zmax.app.model.AirCondition;
import com.zmax.app.model.CityLocation;
import com.zmax.app.model.Documents;
import com.zmax.app.model.FeeBack;
import com.zmax.app.model.HotelList;
import com.zmax.app.model.Light;
import com.zmax.app.model.Login;
import com.zmax.app.model.Television;
import com.zmax.app.model.Update;
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
	
	public static ActList getActList(Context context, String city_name, String page_num, String per) {
		ActList actList = null;
		try {
			String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "events", new BasicNameValuePair("city_name",
					city_name), new BasicNameValuePair("page_num", page_num), new BasicNameValuePair("per", per));
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				actList = JsonMapperUtils.toObject(jsonString, ActList.class);
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return actList;
	}
	
	public static ActList getActListInHotel(Context context, String id, String page_num, String per) {
		ActList actList = null;
		try {
			String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "hotels/" + id + "/events", new BasicNameValuePair(
					"page_num", page_num), new BasicNameValuePair("per", per));
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				actList = JsonMapperUtils.toObject(jsonString, ActList.class);
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return actList;
	}
	
	public static Update checkUpdateVersion(Context context, String tag) {
		Update update = null;
		try {
			String jsonString = HttpUtils
					.getByHttpClient(context, Constant.ZMAX_URL + "versions/check", new BasicNameValuePair("tag", tag));
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				update = JsonMapperUtils.toObject(jsonString, Update.class);
				JSONObject jsonObject = new JSONObject(jsonString);
				if (jsonObject.has("package")) update.package_name = jsonObject.optString("package");
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return update;
	}
	
	public static void testPost(Context context) {
		
		HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "devices/scenes", new BasicNameValuePair("time", "asd"),
				new BasicNameValuePair("devices_and_opera[]", "asd"), new BasicNameValuePair("devices_and_opera[]", "asd"),
				new BasicNameValuePair("user_id", "asd"), new BasicNameValuePair("room_id", "asd"), new BasicNameValuePair("hotel_id",
						"asd"));
		
	}
	
	/**
	 * get opened hotels
	 * 
	 * @param context
	 * @param city_name
	 * @param page_num
	 * @param per
	 * @return
	 */
	public static HotelList getHotelList(Context context, String city_name, String page_num, String per) {
		HotelList hotelList = null;
		try {
			String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "hotels", new BasicNameValuePair("city_name",
					city_name), new BasicNameValuePair("page_num", page_num), new BasicNameValuePair("per", per));
			Log.d(" responeString-->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				hotelList = JsonMapperUtils.toObject(jsonString, HotelList.class);
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return hotelList;
		
	}
	
	public static HotelList getHotelUpcomingList(Context context) {
		HotelList hotelList = null;
		try {
			String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "hotels/upcoming");
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				hotelList = JsonMapperUtils.toObject(jsonString, HotelList.class);
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return hotelList;
		
	}
	
	public static ActDetail getActDetail(Context context, String id) {
		ActDetail actDetail = null;
		try {
			String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "events/" + id);
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				actDetail = JsonMapperUtils.toObject(jsonString, ActDetail.class);
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return actDetail;
		
	}
	
	public static Documents getDocuments(Context context, String type, String updated_time) {
		Documents documents = null;
		try {
			String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "documents/" + type, new BasicNameValuePair(
					"updated_time", updated_time));
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				documents = JsonMapperUtils.toObject(jsonString, Documents.class);
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return documents;
		
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
	
	public static Login loginPlayZMAX(Context context) {
		Login login = null;
		try {
			String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "users/login");
			Log.d("  responeString -->\n" + jsonString);
			login = JsonMapperUtils.toObject(jsonString, Login.class);
		}
		catch (Exception e) {
			Log.e("  Exception :" + e.toString());
			e.printStackTrace();
		}
		return login;
	}
	
	/**
	 * 
	 * @param context
	 * @param pattern
	 *            bright/tv/read/sleep
	 * @return
	 */
	public static int setLight(Context context, String pattern) {
		int status = 0;
		try {
			String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "devices/light", new BasicNameValuePair("pattern",
					pattern));
			if (!TextUtils.isEmpty(jsonString)) {
				JSONObject jsonObject = new JSONObject(jsonString);
				Log.d("  responeString -->\n" + jsonString);
				status = jsonObject.getInt("status");
			}
		}
		catch (Exception e) {
			Log.e("  Exception :" + e.toString());
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * 
	 * @param context
	 * @param push_button
	 *            开关：on， 确定：sub，频道加：chu，频道减：chd，声音加：volu，声音减：vold，数字0～9:
	 *            0～9，静音：sil，av/tv转换：at
	 * @return
	 */
	public static int setTelevision(Context context, String push_button) {
		int status = 0;
		try {
			String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "devices/television", new BasicNameValuePair(
					"push_button", push_button));
			if (!TextUtils.isEmpty(jsonString)) {
				JSONObject jsonObject = new JSONObject(jsonString);
				Log.d("  responeString -->\n" + jsonString);
				status = jsonObject.getInt("status");
			}
		}
		catch (Exception e) {
			Log.e("  Exception :" + e.toString());
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * 
	 * @param context
	 * @param opera_type
	 * @param opera_data
	 *            控制温度：opera_type ： temperature， opera_data： 5～35
	 *            控制模式：opera_type ： schema，
	 *            opera_data：（col：制冷、hot：制热、nat：通风/睡眠；）
	 *            控制阀门： opera_type ： on_off， opera_data： 0，1
	 *            控制风机模式：opera_type ： air_blower， opera_data： (
	 *            low：低速、mid：中速、hig：高速、auto：自动；)
	 *            控制开关机：opera_type ： status， opera_data： 0，1
	 * @return
	 */
	public static int setAirCondition(Context context, String opera_type, String opera_data) {
		int status = 0;
		try {
			String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "devices/air_condiction", new BasicNameValuePair(
					"opera_type", opera_type), new BasicNameValuePair("opera_data", opera_data));
			if (!TextUtils.isEmpty(jsonString)) {
				JSONObject jsonObject = new JSONObject(jsonString);
				Log.d("  responeString -->\n" + jsonString);
				status = jsonObject.getInt("status");
			}
		}
		catch (Exception e) {
			Log.e("  Exception :" + e.toString());
			e.printStackTrace();
		}
		return status;
	}
	
	public static AirCondition getAirCondition(Context context) {
		AirCondition airCondition = null;
		try {
			String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "devices/air_condiction");
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				airCondition = JsonMapperUtils.toObject(jsonString, AirCondition.class);
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return airCondition;
		
	}
	
	public static Television getTelevision(Context context) {
		Television airCondition = null;
		try {
			String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "devices/television");
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				airCondition = JsonMapperUtils.toObject(jsonString, Television.class);
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return airCondition;
		
	}
	
	@Deprecated
	public static Light getLight(Context context) {
		Light airCondition = null;
		try {
			String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "devices/light");
			Log.d("  responeString -->\n" + jsonString);
			if (!TextUtils.isEmpty(jsonString)) {
				airCondition = JsonMapperUtils.toObject(jsonString, Light.class);
			}
		}
		catch (Exception e) {
			Log.e("   Exception :" + e.toString());
			e.printStackTrace();
		}
		return airCondition;
		
	}
}
