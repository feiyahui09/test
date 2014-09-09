package com.zmax.app.utils;

import android.os.Environment;
import android.text.TextUtils;
import com.zmax.app.model.Act;
import com.zmax.app.model.Login;

import java.util.ArrayList;
import java.util.List;

public class Constant {

	public static String ZMAX_URL = "http://app.zmaxhotels.cc/api/v1/";
	// public static final String ZMAX_URL = "http://zmax.bestapp.us/api/v1/";
	//public static String ZMAX_URL = "http://zmaxtest.bestapp.us/api/v1/";



	public static final boolean LOGD_ENABLE = true;
	public static final boolean LOG_SDCARD_ENABLE = true; // 是否打印日志在SDcard上，正式发版前记得设置为false
	public static final String LOG_FILE_NAME = "com.zmax.app" + "_log.txt"; // 根据包名改变日志文件名字
	public static final String TEST_ICON_URI = "https://encrypted-tbn0.gstatic" +
			".com/images?q=tbn:ANd9GcQQGKJtgkUvw47FeyYwTia3chxBqChjxqKgRmX_QItRWAhVqJjc";
	public static final String SDCARD_CACHE_PATH = "/zmax/cacheImg";
	public static final String FEEDBACK_SENDED_ACTION = "feedback_sended_action";
	public static final String ACTION_WELCOME_FROM_INDEX = "action_welcome_from_index";
	public static final String ACTION_WELCOME_FROM_SETTING = "action_welcome_from_setting";
	public static final int PER_NUM_GET_ACTLIST = 5;
	public static final int PER_NUM_GET_HOTELLIST = 100;
	//  百度地图定位
	public static String MAP_SDK_KEY = "seIhcSk2TiTqdHTjyGG8sjCn";// 个人
	// public static String MAP_AK = "Io8gL4Aybx3CZsdpSoKCqZPB";// 个人
	public static String MAP_AK = "nGf4WdZTlTgAjFSQ0AtuSWjc";// zmax
	public static String IP_LOCATION_URL = "http://api.map.baidu.com/location/ip";
	public static long SYN_TIME_INTERVAL = 0;
	/**
	 * inital data
	 */
	public static boolean isLocateEnd = false;
	public static boolean isActlistEnd = false;
	public static boolean isHotellistEnd = false;
	public static String CUR_CITY = "";
	private static Login login;

	public static void saveLogin(Login loginResult) {
		Constant.login = loginResult;
		String jsonStr = JsonMapperUtils.toJson(loginResult);
		DefaultShared.putString(Constant.SPFKEY.LOGIN_INFO_KEY, jsonStr);
	}

	public static void modifyLogin(int _gender, String _nick_name) {
		if (Constant.login == null) return;
		Constant.login.gender = _gender;
		Constant.login.nick_name = _nick_name;
		String jsonStr = JsonMapperUtils.toJson(Constant.login);
		DefaultShared.putString(Constant.SPFKEY.LOGIN_INFO_KEY, jsonStr);
	}

	public static Login getLogin() {
		if (Constant.login != null) return Constant.login;
		String jsonStr = DefaultShared.getString(Constant.SPFKEY.LOGIN_INFO_KEY, "");
		Login login = null;
		if (!TextUtils.isEmpty(jsonStr)){
			login = JsonMapperUtils.toObject(jsonStr, Login.class);
			Constant.login = login;
		}
		return login;
	}





	public static List<Act> getFalseData(boolean bo) {

		List<Act> mList = new ArrayList<Act>();

		for (int i = 0; i < 30; i++) {
			Act activityDetail = new Act();

			mList.add(activityDetail);

		}

		return mList;
	}







	public enum LOAD_STATE {

		SUCCESS, INITIAL, LOADING
	}

	public static class DialogCode {
		public static final int TYPE_SEND_FAILED = 5;
		public static final int TYPE_UNKOWNERROR = 6;
		public static final int TYPE_FORBIDDEN = 2;
		public static final int TYPE_CONNECTORENTER_ERROR = 3;
		public static final int TYPE_SOCKET_TIME_OUT = 1;
		public static final int TYPE_CONNECT_FAILED = 4;

		public static final int TYPE_TOKEN_ERROR = 404;
		public static final String ACTION_BACK_LOGIN = "action_back_login";
		public static final String ACTION_BACK_INDEX = "action_back_index";

	}

	public static class Documents {

		public static final String DOCUMENTS_TYPE_KEY = "documents_type_key";
		public static final int PROTOCAL_TYPE = 1;
		public static final int GUIDE_TYPE = 2;
		public static final int RIGHT_TYPE = 3;

		public static final String PROTOCAL_SPF_KEY = "protocal_spf_key";
		public static final String GUIDE_SPF_KEY = "guide_spf_key";
		public static final String RIGHT_SPF_KEY = "right_spf_key";

	}

	public static class Acts {

		public static final String DATE_SPF_KEY = "date_spf_key";
		public static final String CITY_SPF_KEY = "city_spf_key";
		public static final String ID_KEY = "act_id_key";
		public static final String HOTEL_ID_KEY = "hotel_id_key";
		public static final String DATE_KEY = "act_date_key";
		public static final String CITY_KEY = "act_city_key";

	}

	public static class Share {

		public static final String SHARE_URL = "http://app.zmaxhotels.cc/app/download";
		public static String SHARE_TITLE = "share_title";
		public static String SHARE_CONTENT = "share_content";

	}

	public static class Chat {
		//test
		//	public static final String CHAT_SERVER_IP = "14.23.159.74";
			public static final String CHAT_SERVER_IP = "http://app.zmaxhotels.cc";

		public static final int CHAT_SERVER_PORT = 3014;
		public static final int EMOTION_DIMEN = 23;
		public static final String SELF_ID = "self_id";

		public static final String SELF_NAME = "self_name";
		public static final String SELF_GENDER = "self_gender";

		public static final String CHAT_UPLOAD_IMG_LARGE_KEY = "chat_upload_img_large_key";
		public static final String CHAT_UPLOAD_IMG_THUMB_KEY = "chat_upload_img_thumb_key";

		public static final String CHAT_IMG_CACHE_ABS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath
				() + "/zmax/Camera/";
		public static final String CHAT_IMG_CACHE_PATH = "/zmax/Camera/";
		public static final String CHAT_IMG_LARGE_IMG_KEY = "chat_img_large_img_key";

	}

	public static class SPFKEY {
		public static final String LOGIN_INFO_KEY = "login_info_key";
		public static final String CITY_LOCATION_KEY = "city_location_key";
		public static final String INDEX_ACTLIST_KEY = "index_actlist_key";
		public static final String INDEX_ACTLIST_PAGENUM_KEY = "index_actlist_pagenum_key";
		public static final String INDEX_HOTELLIST_KEY = "index_hotellist_key";

	}

	public static class WAP {

		public static final String URL_HOTEL = "http://m.7daysinn.cn/innIndex";
		public static final String URL_MENBER = "http://m.7daysinn.cn/member/myinfo";
		public static final String WAP_TYPE_KEY = "wap_type_key";
		public static final String ACTION_HOTEL = "action_hotel";
		public static final String ACTION_MENBER = "action_menber";
		public static final String HOTEL_ID_KEY = "hotel_id_key";
		public static final String FROM_DATE_KEY = "from_date_key";
		public static final String TO_DATE_KEY = "to_date_key";

	}
}
