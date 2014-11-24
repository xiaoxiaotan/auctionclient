/**
 * 
 */
package com.org.disappearwind.auction.utils;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * App������Ϣ���
 * 
 * @author zhaowh
 *
 */
public class SharedPrefUtil {
	// �û���Ϣ������Key
	private final static String PREF_NAME= "auctionpref";
	//ȫ��Context
	private final static Context ctx = MyApplication.getContext();
	//�û�ID
	public final static String USER_ID = "uid";

	/*
	 * ������Ϣ������
	 */
	public static void savePref(String key,String value) {
		if(value != null && !value.equals("")){
			SharedPreferences.Editor prefEditor = ctx.getSharedPreferences(PREF_NAME, 0).edit();
			prefEditor.putString(key, value);
			prefEditor.commit();
		}
	}
	/*
	 * ������Ϣ������
	 */
	public static void savePref(Map<String, String> values) {
		if(values != null && values.size() > 0){
			SharedPreferences.Editor prefEditor = ctx.getSharedPreferences(PREF_NAME, 0).edit();
			for(String key : values.keySet()){
				prefEditor.putString(key, values.get(key));
			}
			prefEditor.commit();
		}
	}
	/*
	 * ��ȡ����
	 */
	public static String getPref(String key){
		SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME, 0);
		return pref.getString(key, null);
	}
	/*
	 * �ж��Ƿ����key
	 */
	public static boolean isExist(String key){
		return ctx.getSharedPreferences(PREF_NAME, 0).contains(key);
	}
}
