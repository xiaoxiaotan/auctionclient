/**
 * 
 */
package com.org.disappearwind.auction.utils;

import android.app.Application;
import android.content.Context;

/**
 * ȫ��Application ���ڻ�ȡȫ�ֵ�Context
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
	 * ��ȡȫ�ֵ�Context
	 */
	public static Context getContext() {
		return context;
	}
}
