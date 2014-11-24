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
 * SQLite ���ݿ�������
 * 
 * @author zhaowh
 *
 */
public class SQLiteUtil {

	private static SQLiteDatabase db;
	private static DBHelper dbHelper;
	// ���ݿ�ı���
	public static final String DB_NAME = "auction.db3";
	// ���ݿ�İ汾��
	public static final int DB_VERSION = 1;
	// ��־���
	public static final String SQL_LOG_CREATE = "create table if not exists Log (source varchar, message varchar)";
	public static final String SQL_LOG_INSERT = "insert into Log values('%s','%s')";
	public static final String SQL_LOG_SELECT = "select * from Log";
	public static final String SQL_LOG_CLOUMN_SOURCE = "source";
	public static final String SQL_LOG_CLOUMN_MESSAGE = "messsage";

	// �������ݵ��࣬��̳� SQLiteOpenHelper
	private static class DBHelper extends SQLiteOpenHelper {
		// ������������ģʽ
		private static DBHelper mInstance = null;

		public DBHelper() {
			super(MyApplication.getContext(), DB_NAME, null, DB_VERSION);
		}

		// ����ģʽ����ȡΨһ��һ��ʵ��
		static synchronized DBHelper getInstance() {
			if (mInstance == null) {
				mInstance = new DBHelper();
			}
			return mInstance;
		}

		// ����ʱ�����
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_LOG_CREATE);
		}

		// ����ʱ����
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	// ��һЩ��ʼ������
	static {
		dbHelper = DBHelper.getInstance();
		db = dbHelper.getWritableDatabase();
	}

	// ִ��ҵ�����
	/*
	 * ������־
	 */
	public static void insertLog(Map<String, String> params) {
		String sqlText = String.format(SQL_LOG_INSERT,
				params.get(SQL_LOG_CLOUMN_SOURCE),
				params.get(SQL_LOG_CLOUMN_MESSAGE));
		db.execSQL(sqlText);
	}
	/*
	 * ����־�������и��α�
	 */
	public static Cursor readLog(){
		return db.rawQuery(SQL_LOG_SELECT,null);
	}
}
