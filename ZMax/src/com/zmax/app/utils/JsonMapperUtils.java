package com.zmax.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/**
 * json 解析类
 * 
 * @author ynb
 * 
 */
public class JsonMapperUtils {
	
	private static final String TAG = JsonMapperUtils.class.getSimpleName();
	
	private static ObjectMapper mMapper;
	
	private static byte[] lock = new byte[0];
	
	public static ObjectMapper getDefaultObjectMapper() {
		synchronized (lock) {
			if (mMapper == null) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					SerializationConfig seriConf = mapper.getSerializationConfig();
					seriConf.setSerializationInclusion(Inclusion.NON_NULL);
					mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
					mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
					mapper.configure(Feature.AUTO_CLOSE_SOURCE, false);
					// mapper.registerSubtypes(//
					// User.class
					// );
					mMapper = mapper;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		return mMapper;
	}
	
	/**
	 * 对象转成json
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
	public static <T> String toJson(T t) {
		if (t == null) return null;
		try {
			return getDefaultObjectMapper().writeValueAsString(t);
		}
		catch (Exception e) {
			Log.w( e.toString());
			return null;
		}
		
	}
	
	/**
	 * json 转成 对象
	 * 
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		try {
			return getDefaultObjectMapper().readValue(json, clazz);
		}
		catch (JsonParseException e) {
			Log.d( e.getMessage());
			e.printStackTrace();
			return null;
		}
		catch (JsonMappingException e) {
			Log.d( e.getMessage());
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			Log.d( e.getMessage());
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			Log.w( e.toString());
			return null;
		}
	}
	
	/**
	 * json二进制流转成对象
	 * 
	 * @param <T>
	 * @param in
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(InputStream in, Class<T> clazz) {
		try {
			return getDefaultObjectMapper().readValue(in, clazz);
		}
		catch (Exception e) {
			Log.w( e.toString());
			return null;
		}
	}
	
	public static <T> JavaType constructListType(Class<T> clazz) {
		return TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz);
	}
}
