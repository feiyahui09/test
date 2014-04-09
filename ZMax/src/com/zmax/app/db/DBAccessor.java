package com.zmax.app.db;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;
import com.zmax.app.model.ActList;

/**
 * 数据库访问类
 * 
 * @author ynb
 * 
 */
public final class DBAccessor {
	
	/**
	 * 
	 * @param <T>
	 * @param t
	 * @return 大于零 保存成功
	 */
	@SuppressWarnings("unchecked")
	public static <T> int saveObject(T t) {
		int result = 0;
		try {
			Dao<T, Integer> dao = (Dao<T, Integer>) DBHelper.getInstance().createDao(t.getClass());
			result = dao.create(t);
		}
		catch (SQLException e) {
		}
		return result;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param t
	 * @return 大于零 更新成功
	 */
	@SuppressWarnings("unchecked")
	public static <T> int updateObject(T t) {
		int result = 0;
		try {
			Dao<T, Integer> dao = (Dao<T, Integer>) DBHelper.getInstance().createDao(t.getClass());
			result = dao.update(t);
		}
		catch (SQLException e) {
		}
		return result;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param t
	 * @return 大于零 删除成功
	 */
	@SuppressWarnings("unchecked")
	public static <T> int deleteObject(T t) {
		int result = 0;
		try {
			Dao<T, Integer> dao = (Dao<T, Integer>) DBHelper.getInstance().createDao(t.getClass());
			result = dao.delete(t);
		}
		catch (SQLException e) {
		}
		return result;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param datas
	 * @param clazz
	 * @return 大于零 删除成功
	 */
	public static <T> int delete(Collection<T> datas, Class<T> clazz) {
		int result = 0;
		if (datas == null || datas.size() == 0) return result;
		try {
			Dao<T, Integer> dao = (Dao<T, Integer>) DBHelper.getInstance().createDao(clazz);
			result = dao.delete(datas);
		}
		catch (SQLException e) {
		}
		return result;
	}
	
	/**
	 * 按条件删除数据
	 * 
	 * @param <T>
	 * @param clazz
	 * @param idName
	 *            id字段名
	 * @return 大于零 删除成功
	 */
	public static <T> int deleteALL(Class<T> clazz, Where<T, Integer> where) {
		int result = 0;
		try {
			Dao<T, Integer> dao = (Dao<T, Integer>) DBHelper.getInstance().createDao(clazz);
			DeleteBuilder<T, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.setWhere(where);
			result = deleteBuilder.delete();
		}
		catch (SQLException e) {
		}
		return result;
	}
	
	/**
	 * 取得where语句
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> Where<T, Integer> getWhere(Class<T> clazz) {
		Where<T, Integer> where = null;
		try {
			Dao<T, Integer> dao = (Dao<T, Integer>) DBHelper.getInstance().createDao(clazz);
			DeleteBuilder<T, Integer> deleteBuilder = dao.deleteBuilder();
			where = deleteBuilder.where();
		}
		catch (SQLException e) {
		}
		return where;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> queryAll(Class<T> clazz) {
		List<T> result = null;
		try {
			Dao<T, Integer> dao = (Dao<T, Integer>) DBHelper.getInstance().createDao(clazz);
			result = dao.queryForAll();
		}
		catch (SQLException e) {
		}
		return result;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fieldValues
	 * @return
	 */
	public static <T> List<T> queryAll(Class<T> clazz, Map<String, Object> fieldValues) {
		List<T> result = null;
		try {
			Dao<T, Integer> dao = (Dao<T, Integer>) DBHelper.getInstance().createDao(clazz);
			result = dao.queryForFieldValuesArgs(fieldValues);
		}
		catch (SQLException e) {
		}
		return result;
	}
	
}
