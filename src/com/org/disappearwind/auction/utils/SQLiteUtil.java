/**
 * 
 */
package com.org.disappearwind.auction.utils;

import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite 数据库操作相关
 * 
 * @author zhaowh
 *
 */
public class SQLiteUtil {

	private static SQLiteDatabase db;
	private static DBHelper dbHelper;
	// 数据库的表名
	public static final String DB_NAME = "auction.db3";
	// 数据库的版本号
	public static final int DB_VERSION = 1;
	// 日志相关
	public static final String SQL_LOG_CREATE = "create table if not exists Log (source varchar, message varchar)";
	public static final String SQL_LOG_INSERT = "insert into Log values('%s','%s')";
	public static final String SQL_LOG_SELECT = "select * from Log";
	public static final String SQL_LOG_CLOUMN_SOURCE = "source";
	public static final String SQL_LOG_CLOUMN_MESSAGE = "messsage";

	// 处理数据的类，需继承 SQLiteOpenHelper
	private static class DBHelper extends SQLiteOpenHelper {
		// 定义自身，单例模式
		private static DBHelper mInstance = null;

		public DBHelper() {
			super(MyApplication.getContext(), DB_NAME, null, DB_VERSION);
		}

		// 单例模式，获取唯一的一个实例
		static synchronized DBHelper getInstance() {
			if (mInstance == null) {
				mInstance = new DBHelper();
			}
			return mInstance;
		}

		// 创建时构造表
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_LOG_CREATE);
		}

		// 升级时处理
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	// 做一些初始化工作
	static {
		dbHelper = DBHelper.getInstance();
		db = dbHelper.getWritableDatabase();
	}

	// 执行业务操作
	/*
	 * 插入日志
	 */
	public static void insertLog(Map<String, String> params) {
		String sqlText = String.format(SQL_LOG_INSERT,
				params.get(SQL_LOG_CLOUMN_SOURCE),
				params.get(SQL_LOG_CLOUMN_MESSAGE));
		db.execSQL(sqlText);
	}
	/*
	 * 读日志，返回有个游标
	 */
	public static Cursor readLog(){
		return db.rawQuery(SQL_LOG_SELECT,null);
	}
}
