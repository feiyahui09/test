package com.zmax.app.net;

import android.content.Context;
import android.text.TextUtils;
import com.zmax.app.model.*;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.EncoderHandler;
import com.zmax.app.utils.JsonMapperUtils;
import com.zmax.app.utils.Log;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NetAccessor {

    private static final String TAG = NetAccessor.class.getSimpleName();
    static int time = 0;

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
                        if (address_detail.has("province"))
                            cityLocation.province = address_detail.optString("province");
                        if (address_detail.has("city")) cityLocation.city = address_detail.optString("city");
                        if (address_detail.has("city_code"))
                            cityLocation.city_code = address_detail.optString("city_code");
                    }
                }
            }
        } catch (Exception e) {
            cityLocation = null;
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
            // Log.d("  responeString -->\n" + jsonString);
            if (!TextUtils.isEmpty(jsonString)) {
                actList = JsonMapperUtils.toObject(jsonString, ActList.class);
            }
        } catch (Exception e) {
            actList = null;
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
        } catch (Exception e) {
            actList = null;
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
        } catch (Exception e) {
            update = null;
            Log.e("   Exception :" + e.toString());
            e.printStackTrace();
        }
        return update;
    }

    public static VertifyNameResult vetfityNameDup(Context context, String user_id, String gender, String nick_name) {
        VertifyNameResult result = null;
        try {
            String req_time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(System.currentTimeMillis() + Constant.SYN_TIME_INTERVAL);
            Log.i("req_time:  " + req_time);
            Log.i("SYN_TIME_INTERVAL:  " + Constant.SYN_TIME_INTERVAL);

            time++;
            String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "chat/user_info", new BasicNameValuePair("user_id",
                            user_id + ""), new BasicNameValuePair("gender", gender + ""), new BasicNameValuePair("nick_name", nick_name),
                    new BasicNameValuePair("Zmax-Auth-Token", Constant.getLogin().auth_token), new BasicNameValuePair("pms_hotel_id",
                            Constant.getLogin().pms_hotel_id), new BasicNameValuePair("auth_time", req_time), new BasicNameValuePair(
                            "publich_key", EncoderHandler.getPublicKey(Constant.getLogin().pms_hotel_id, req_time))
            );
            Log.d("  responeString -->\n" + jsonString);
            if (!TextUtils.isEmpty(jsonString)) {
                result = JsonMapperUtils.toObject(jsonString, VertifyNameResult.class);
            }
        } catch (Exception e) {
            result = null;
            Log.e("   Exception :" + e.toString());
            e.printStackTrace();
        }
        return result;
    }

    @Deprecated
    public static void testPost(Context context) {

        HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "devices/scenes", new BasicNameValuePair("time", "asd"),
                new BasicNameValuePair("devices_and_opera[]", "asd"), new BasicNameValuePair("devices_and_opera[]", "asd"),
                new BasicNameValuePair("user_id", "asd"), new BasicNameValuePair("room_id", "asd"), new BasicNameValuePair("hotel_id",
                        "asd")
        );

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
        } catch (Exception e) {
            hotelList = null;
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
        } catch (Exception e) {
            hotelList = null;
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
        } catch (Exception e) {
            actDetail = null;
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
        } catch (Exception e) {
            documents = null;
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
        } catch (Exception e) {
            feeBack = null;
            Log.e("  Exception :" + e.toString());
            e.printStackTrace();
        }
        return feeBack;
    }
    public static BaseModel logOut(Context context, String token     ) {
        BaseModel baseModel = null;
        try {
            String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "users/logout", new BasicNameValuePair("token",
                    token) );
            Log.d("  responeString -->\n" + jsonString);
            baseModel = JsonMapperUtils.toObject(jsonString, BaseModel.class);
        } catch (Exception e) {
            baseModel = null;
            Log.e("  Exception :" + e.toString());
            e.printStackTrace();
        }
        return baseModel;
    }
    public static Login loginPlayZMAX(Context context,  String pms_hotel_id,String room_num, String id_number) {
        Login login = null;
        try {
            String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "users/login", new BasicNameValuePair(
		            "pms_hotel_id", pms_hotel_id),new BasicNameValuePair("room_num",
                    room_num), new BasicNameValuePair("id_number", id_number));
            Log.d("  responeString -->\n" + jsonString);
            login = JsonMapperUtils.toObject(jsonString, Login.class);
        } catch (Exception e) {
            login = null;
            Log.e("  Exception :" + e.toString());
            e.printStackTrace();
        }
        return login;
    }

    /**
     * @param context
     * @param pattern bright/tv/read/sleep
     * @return
     */
    public static Light setLight(Context context, String pattern) {
        Light light = null;
        try {
            String req_time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(System.currentTimeMillis() + Constant.SYN_TIME_INTERVAL);
            Log.i("req_time:  " + req_time);
            Log.i("SYN_TIME_INTERVAL:  " + Constant.SYN_TIME_INTERVAL);
            String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "devices/scene/regular/switch",
                    new BasicNameValuePair("pattern", pattern), new BasicNameValuePair("Zmax-Auth-Token", Constant.getLogin().auth_token),
                    new BasicNameValuePair("pms_hotel_id", Constant.getLogin().pms_hotel_id),
                    new BasicNameValuePair("auth_time", req_time),
                    new BasicNameValuePair("publich_key", EncoderHandler.getPublicKey(Constant.getLogin().pms_hotel_id, req_time)));
            Log.d("  responeString -->\n" + jsonString);
            if (!TextUtils.isEmpty(jsonString)) {
                light = JsonMapperUtils.toObject(jsonString, Light.class);
            }
        } catch (Exception e) {
            light = null;
            Log.e("  Exception :" + e.toString());
            e.printStackTrace();
        }
        return light;
    }

    /**
     * @param context
     * @param push_button 开关：on， 确定：sub，频道加：chu，频道减：chd，声音加：volu，声音减：vold，数字0～9:
     *                    0～9，静音：sil，av/tv转换：at
     * @return
     */
    public static Television setTelevision(Context context, String push_button) {
        Television television = null;
        try {
            String req_time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(System.currentTimeMillis() + Constant.SYN_TIME_INTERVAL);
            Log.i("req_time:  " + req_time);
            Log.i("SYN_TIME_INTERVAL:  " + Constant.SYN_TIME_INTERVAL);
            String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "devices/television", new BasicNameValuePair(
                            "push_button", push_button), new BasicNameValuePair("Zmax-Auth-Token", Constant.getLogin().auth_token),
                    new BasicNameValuePair("pms_hotel_id", Constant.getLogin().pms_hotel_id),
                    new BasicNameValuePair("auth_time", req_time),
                    new BasicNameValuePair("publich_key", EncoderHandler.getPublicKey(Constant.getLogin().pms_hotel_id, req_time))
            );
            Log.d("  responeString -->\n" + jsonString);
            if (!TextUtils.isEmpty(jsonString)) {
                television = JsonMapperUtils.toObject(jsonString, Television.class);
            }
        } catch (Exception e) {
            television = null;
            Log.e("  Exception :" + e.toString());
            e.printStackTrace();
        }
        return television;
    }

    /**
     * @param context
     * @param opera_type
     * @param opera_data 控制温度：opera_type ： temperature， opera_data： 5～35
     *                   控制模式：opera_type ： schema，
     *                   opera_data：（col：制冷、hot：制热、nat：通风/睡眠；）
     *                   控制阀门： opera_type ： on_off， opera_data： 0，1
     *                   控制风机模式：opera_type ： air_blower， opera_data： (
     *                   low：低速、mid：中速、hig：高速、auto：自动；)
     *                   控制开关机：opera_type ： status， opera_data： 0，1
     * @return
     */
    public static AirCondition setAirCondition(Context context, String opera_type, String opera_data) {
        AirCondition airCondition = null;
        try {
            String req_time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(System.currentTimeMillis() + Constant.SYN_TIME_INTERVAL);
            Log.i("req_time:  " + req_time);
            Log.i("SYN_TIME_INTERVAL:  " + Constant.SYN_TIME_INTERVAL);
            String jsonString = HttpUtils.postByHttpClient(context, Constant.ZMAX_URL + "devices/air_condiction", new BasicNameValuePair(
                            "opera_type", opera_type), new BasicNameValuePair("opera_data", opera_data), new BasicNameValuePair("Zmax-Auth-Token",
                            Constant.getLogin().auth_token), new BasicNameValuePair("pms_hotel_id", Constant.getLogin().pms_hotel_id),
                    new BasicNameValuePair("auth_time", req_time),
                    new BasicNameValuePair("publich_key", EncoderHandler.getPublicKey(Constant.getLogin().pms_hotel_id, req_time))
            );
            Log.d("  responeString -->\n" + jsonString);
            if (!TextUtils.isEmpty(jsonString)) {
                airCondition = JsonMapperUtils.toObject(jsonString, AirCondition.class);
            }
        } catch (Exception e) {
            airCondition = null;
            Log.e("  Exception :" + e.toString());
            e.printStackTrace();
        }
        return airCondition;
    }

    public static AirCondition getAirCondition(Context context) {
        AirCondition airCondition = null;
        try {
            airCondition = new AirCondition();
            String req_time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(System.currentTimeMillis() + Constant.SYN_TIME_INTERVAL);
            Log.i("req_time:  " + req_time);
            Log.i("SYN_TIME_INTERVAL:  " + Constant.SYN_TIME_INTERVAL);
            String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "devices/air_condiction", new BasicNameValuePair(
                    "Zmax-Auth-Token", Constant.getLogin().auth_token), new BasicNameValuePair("pms_hotel_id",
                    Constant.getLogin().pms_hotel_id), new BasicNameValuePair("auth_time", req_time), new BasicNameValuePair("publich_key",
                    EncoderHandler.getPublicKey(Constant.getLogin().pms_hotel_id, req_time)));
            Log.d("  responeString -->\n" + jsonString);
            if (!TextUtils.isEmpty(jsonString)) {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject.has("air_conditioning"))
                    airCondition = JsonMapperUtils.toObject(jsonObject.optString("air_conditioning"), AirCondition.class);
                airCondition.respone_status = jsonObject.optInt("status");
                airCondition.message = jsonObject.optString("message");
            }
        } catch (Exception e) {
            airCondition = null;
            Log.e("   Exception :" + e.toString());
            e.printStackTrace();
        }
        return airCondition;

    }

    public static Television getTelevision(Context context) {
        Television television = null;
        try {
            television = new Television();
            String req_time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(System.currentTimeMillis() + Constant.SYN_TIME_INTERVAL);
            Log.i("req_time:  " + req_time);
            Log.i("SYN_TIME_INTERVAL:  " + Constant.SYN_TIME_INTERVAL);
            String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "devices/television", new BasicNameValuePair(
                    "Zmax-Auth-Token", Constant.getLogin().auth_token), new BasicNameValuePair("pms_hotel_id",
                    Constant.getLogin().pms_hotel_id), new BasicNameValuePair("auth_time", req_time), new BasicNameValuePair("publich_key",
                    EncoderHandler.getPublicKey(Constant.getLogin().pms_hotel_id, req_time)));
            Log.d("  responeString -->\n" + jsonString);
            if (!TextUtils.isEmpty(jsonString)) {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject.has("television"))
                    television = JsonMapperUtils.toObject(jsonObject.optString("television"), Television.class);
                television.respone_status = jsonObject.optInt("status");
                television.message = jsonObject.optString("message");
            }
        } catch (Exception e) {
            television = null;
            Log.e("   Exception :" + e.toString());
            e.printStackTrace();
        }
        return television;

    }

    public static Light getLight(Context context) throws Exception {
        Light airCondition = null;
        try {
            String req_time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(System.currentTimeMillis() + Constant.SYN_TIME_INTERVAL);
            Log.i("req_time:  " + req_time);
            Log.i("SYN_TIME_INTERVAL:  " + Constant.SYN_TIME_INTERVAL);
            String jsonString = HttpUtils.getByHttpClient(context, Constant.ZMAX_URL + "devices/scene", new BasicNameValuePair(
                    "Zmax-Auth-Token", Constant.getLogin().auth_token), new BasicNameValuePair("pms_hotel_id",
                    Constant.getLogin().pms_hotel_id), new BasicNameValuePair("auth_time", req_time), new BasicNameValuePair("publich_key",
                    EncoderHandler.getPublicKey(Constant.getLogin().pms_hotel_id, req_time)));
            Log.d("  responeString -->\n" + jsonString);
            if (!TextUtils.isEmpty(jsonString)) {
                airCondition = JsonMapperUtils.toObject(jsonString, Light.class);
            }
        } catch (Exception e) {
            airCondition = null;
            Log.e("   Exception :" + e.toString());
            throw e;
        }
        return airCondition;

    }

}
