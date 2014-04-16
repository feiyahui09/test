package com.zmax.app.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zmax.app.ZMaxApplication;
import com.zmax.app.model.Act;
import com.zmax.app.model.CityLocation;
import com.zmax.app.model.Hotel;
import com.zmax.app.utils.Log;

/**
 * 数据库创建和升级入口
 * 在使用数据库前 在onCreate方法里创建相应的表
 * 在onUpgrade方法里先要删除相应的旧表
 * 
 * @author ynb
 * 
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String tag = DBHelper.class.getName();
	
	private static final int DATABASE_VERSION = 1;// zheli
	
	private static final String DATABASE_NAME = "db_zmax_app";
	
	private static DBHelper dbHelper;
	
	private static byte[] lock = new byte[0];
	
	public static DBHelper getInstance() {
		synchronized (lock) {
			if (dbHelper == null) dbHelper = new DBHelper(ZMaxApplication.getInstance());
		}
		return dbHelper;
	}
	
	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		Log.d("DBHelper > onCreate ");
		try {
			// 在此创建model对应的表
			TableUtils.createTable(connectionSource, CityLocation.class);
			TableUtils.createTable(connectionSource, Act.class);
			TableUtils.createTable(connectionSource, Hotel.class);
			
		}
		catch (SQLException e) {
			Log.e("Unable to create datbases");
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			Log.d("DBHelper > onUpgrade  , oldVer : " + oldVer + " , newVer : " + newVer);
			// 升级之前先删除旧表
			TableUtils.dropTable(connectionSource, CityLocation.class, true);
			TableUtils.dropTable(connectionSource, Act.class, true);
			TableUtils.dropTable(connectionSource, Hotel.class, true);
			
			onCreate(sqliteDatabase, connectionSource);
		}
		catch (SQLException e) {
			Log.e("Unable to upgrade database from version " + oldVer + " to new " + newVer);
		}
	}
	
	/**
	 * 创建对应model所需的DAO
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	public <T> Dao<T, Integer> createDao(Class<T> clazz) throws SQLException {
		return getDao(clazz);
	}
	
}
