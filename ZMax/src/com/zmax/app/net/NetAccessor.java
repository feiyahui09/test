package com.zmax.app.net;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.zmax.app.model.CityLocation;
import com.zmax.app.utils.Constant;
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
			Log.d( "getCityLoacationByIp responeString -->\n" + jsonString);
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
			Log.e( " getCityLoacationByIp Exception :" + e.toString());
			e.printStackTrace();
			
			cityLocation = null;
		}
		return cityLocation;
	}
	/*
	 * public static AdvertList getV2AdvertList(Context context, String
	 * positionName, int count, String density ,String longitude , String
	 * latitude) throws Exception{ Log.v( "****" + new
	 * Timestamp(System.currentTimeMillis()) + " start getV2AdvertList " +
	 * "****"); AdvertList advertList = new AdvertList(); try{ JSONObject params
	 * = new JSONObject(); JSONObject data = new JSONObject(); data.put("dt",
	 * "madvert_request"); data.put("positionName", positionName);
	 * data.put("count", count); data.put("clientOS", "android");
	 * data.put("density", density); data.put("longitude", longitude);
	 * data.put("latitude", latitude); params.put("data", data);
	 * params.put("cmd", "get madvert list"); params.put("cmdtype", "");
	 * 
	 * String jsonString = HttpUtil.sendRequest(context,FANCY_URL, "POST",
	 * params.toString()); // if(positionName.equals("SearchKeyword")) { //
	 * jsonString =
	 * "{\"data\":{\"dt\":\"madvert_response\",\"madverts\":{\"dt\":\"array\",\"array\":[{\"dt\":\"madvert\",\"id\":2,\"actionType\":2,\"actionContent\":\"com.myzaker.ZAKER_Phone\",\"word\":\"ZAKER\",\"msgTitle\":\"ZAKER\",\"msgContent\":\"ZAKER领略真正阅读之美\",\"msgIcon\":\"http://125.88.74.85:8000/res/apprepo/icon.png\",\"picture\":\"http://125.88.74.85/res/images2.1/banner/1004X262/list4.jpg\"},{\"dt\":\"madvert\",\"id\":2,\"actionType\":2,\"actionContent\":\"com.tencent.mobileqq\",\"word\":\"手机QQ2013\",\"msgTitle\":\"手机QQ2013\",\"msgContent\":\"手机QQ2013手机QQ2013手机QQ2013手机QQ2013\",\"msgIcon\":\"http://125.88.74.85:8000/res/apprepo/icon.png\",\"picture\":\"http://125.88.74.85/res/images2.1/banner/1004X262/list4.jpg\"},{\"dt\":\"madvert\",\"id\":2,\"actionType\":2,\"actionContent\":\"com.myzaker.ZAKERShopping\",\"word\":\"ZAKER橱窗\",\"msgTitle\":\"ZAKER橱窗\",\"msgContent\":\"ZAKER橱窗ZAKER橱窗ZAKER橱窗ZAKER橱窗ZAKER橱窗\",\"msgIcon\":\"http://125.88.74.85:8000/res/apprepo/icon.png\",\"picture\":\"http://125.88.74.85/res/images2.1/banner/1004X262/list4.jpg\"},{\"dt\":\"madvert\",\"id\":2,\"actionType\":2,\"actionContent\":\"telecom.mdesk.widgetprovider\",\"word\":\"应用超市独立版\",\"msgTitle\":\"应用超市独立版\",\"msgContent\":\"天翼云桌面应用超市独立版本\",\"msgIcon\":\"http://125.88.74.85:8000/res/apprepo/icon.png\",\"picture\":\"http://125.88.74.85/res/images2.1/banner/1004X262/list4.jpg\"},{\"dt\":\"madvert\",\"id\":2,\"actionType\":2,\"actionContent\":\"com.rovio.angrybirds\",\"word\":\"愤怒的小鸟\",\"msgTitle\":\"愤怒的小鸟\",\"msgContent\":\"愤怒的小鸟愤怒的小鸟愤怒的小鸟愤怒的小鸟愤怒的小鸟\",\"msgIcon\":\"http://125.88.74.85:8000/res/apprepo/icon.png\",\"picture\":\"http://125.88.74.85/res/images2.1/banner/1004X262/list4.jpg\"}]}},\"cmd\":\"get madvert list\",\"rcd\":0}"
	 * ; // } Log.d( "getV2AdvertList()  responeString -->"+jsonString); if
	 * (!TextUtils.isEmpty(jsonString)) { JSONObject result = new
	 * JSONObject(jsonString); if (result.has("rcd")) { advertList.rcd =
	 * result.optInt("rcd"); } if (result.has("data")) { JSONObject resultData =
	 * result.optJSONObject("data"); if (resultData.has("madverts")) {
	 * JSONObject madverts = resultData.optJSONObject("madverts"); if
	 * (madverts.has("array")) { JSONArray madvertArray =
	 * madverts.optJSONArray("array"); if(madvertArray != null &&
	 * madvertArray.length() > 0){ AdvertObj advertObj = null; for (int i = 0; i
	 * < madvertArray.length(); i++) { JSONObject appObj =
	 * madvertArray.optJSONObject(i); if (appObj != null) { advertObj = new
	 * AdvertObj(); advertObj.dt = appObj.optString("dt"); advertObj.id =
	 * appObj.optInt("id"); advertObj.picture = appObj.optString("picture");
	 * advertObj.word = appObj.optString("word"); advertObj.showTime =
	 * appObj.optInt("showTime"); advertObj.actionType =
	 * appObj.optInt("actionType"); advertObj.actionContent =
	 * appObj.optString("actionContent"); advertObj.msgTitle =
	 * appObj.optString("msgTitle"); advertObj.msgContent =
	 * appObj.optString("msgContent"); advertObj.msgIcon =
	 * appObj.optString("msgIcon"); advertList.list.add(advertObj); } } } } }
	 * 
	 * } } }catch(Exception e){ Log.e(
	 * " NetAccessor getV2AdvertList Exception :"+e.toString()); throw e;
	 * }finally{ Log.v( "****" + new Timestamp(System.currentTimeMillis()) +
	 * " end getV2AdvertList ****"); } return advertList; }
	 *//**
	 * 上报广告Log （01月新版）
	 * 
	 * @param context
	 * @param logs
	 * @return
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
