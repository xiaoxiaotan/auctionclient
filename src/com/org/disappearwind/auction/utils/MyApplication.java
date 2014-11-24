/**
 * 
 */
package com.org.disappearwind.auction.utils;

import android.app.Application;
import android.content.Context;

/**
 * 全局Application 用于获取全局的Context
 * @author zhaowh
 *
 */
public class MyApplication extends Application {
	private static Context context;
	
	@Override
	public void onCreate(){
		context = getApplicationContext();
	}
	/*
	 * 获取全局的Context
	 */
	public static Context getContext() {
		return context;
	}
}
