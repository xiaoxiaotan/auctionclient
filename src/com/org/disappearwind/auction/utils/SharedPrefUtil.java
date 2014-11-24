/**
 * 
 */
package com.org.disappearwind.auction.utils;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * App配置信息相关
 * 
 * @author zhaowh
 *
 */
public class SharedPrefUtil {
	// 用户信息的配置Key
	private final static String PREF_NAME= "auctionpref";
	//全局Context
	private final static Context ctx = MyApplication.getContext();
	//用户ID
	public final static String USER_ID = "uid";

	/*
	 * 保存信息：单条
	 */
	public static void savePref(String key,String value) {
		if(value != null && !value.equals("")){
			SharedPreferences.Editor prefEditor = ctx.getSharedPreferences(PREF_NAME, 0).edit();
			prefEditor.putString(key, value);
			prefEditor.commit();
		}
	}
	/*
	 * 保存信息：多条
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
	 * 获取配置
	 */
	public static String getPref(String key){
		SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME, 0);
		return pref.getString(key, null);
	}
	/*
	 * 判断是否存在key
	 */
	public static boolean isExist(String key){
		return ctx.getSharedPreferences(PREF_NAME, 0).contains(key);
	}
}
