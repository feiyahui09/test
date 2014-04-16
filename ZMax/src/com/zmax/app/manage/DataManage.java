package com.zmax.app.manage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.stmt.Where;
import com.zmax.app.db.DBAccessor;
import com.zmax.app.model.Act;
import com.zmax.app.model.Hotel;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.DefaultShared;
import com.zmax.app.utils.JsonMapperUtils;

//13416388156
public class DataManage {
	public static final String STRING_DIVIDER = "@@_@";
	
	@Deprecated
	public static void saveIndexActlist(List<Act> actList) {
		
		if (actList == null || actList.isEmpty()) return;
		DefaultShared.putString(Constant.SPFKEY.INDEX_ACTLIST_KEY, convertList2String(actList));
	}
	
	public static void saveIndexActlist2DB(List<Act> actList) {
		
		int size = (actList == null || actList.isEmpty()) ? 0 : actList.size() > 5 ? 5 : actList.size();
		DBAccessor.deleteALL(Act.class, null);
		for (int i = 0; i < size; i++) {
			DBAccessor.saveObject(actList.get(i));
		}
	}
	
	public static List<Act> getIndexActlist4DB() {
		List<Act> list = null;
		try {
			list = DBAccessor.queryAll(Act.class);
			if (list != null && !list.isEmpty()) list = list.size() > 5 ? list.subList(0, 5) : list;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void saveIndexHotellist2DB(List<Hotel> actList, boolean isUpcoming) {
		int size = (actList == null || actList.isEmpty()) ? 0 : actList.size();
		Where<Hotel, Integer> where = DBAccessor.getWhere(Hotel.class);
		try {
			where.eq("isUpcoming", isUpcoming);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBAccessor.deleteALL(Hotel.class, where);
		for (int i = 0; i < size; i++) {
			DBAccessor.saveObject(actList.get(i));
		}
	}
	
	public static List<Hotel> getIndexHotellist4DB(boolean isUpcoming) {
		List<Hotel> list = null;
		try {
			Map<String, Object> fieldValues = new HashMap<String, Object>();
			fieldValues.put("isUpcoming", isUpcoming);
			list = DBAccessor.queryAll(Hotel.class, fieldValues);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Deprecated
	private static String convertList2String(List<Act> actList) {
		String listStr = "";
		int size = actList.size() > 5 ? 5 : actList.size();
		for (int i = 0; i < size; i++) {
			if (i == size - 1)
				listStr += JsonMapperUtils.toJson(actList.get(i));
			else
				listStr += JsonMapperUtils.toJson(actList.get(i)) + STRING_DIVIDER;
		}
		return listStr;
		
	}
	
	@Deprecated
	public static List<Act> getIndexActlist() {
		List<Act> list = null;
		try {
			list = new ArrayList<Act>();
			String tmpStr = DefaultShared.getString(Constant.SPFKEY.INDEX_ACTLIST_KEY, "");
			
			String[] tmpStrs = tmpStr.split(STRING_DIVIDER);
			for (String string : tmpStrs) {
				list.add(JsonMapperUtils.toObject(string, Act.class));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Deprecated
	public static int getIndexActlistPageNum() {
		
		return DefaultShared.getInt(Constant.SPFKEY.INDEX_ACTLIST_PAGENUM_KEY, 1);
	}
}
