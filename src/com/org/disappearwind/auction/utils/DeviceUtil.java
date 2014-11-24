/**
 * 
 */
package com.org.disappearwind.auction.utils;

import android.os.Build;
import android.telephony.TelephonyManager;

;

/**
 * 设备相关
 * 
 * @author zhaowh
 *
 */
public class DeviceUtil {
	private static TelephonyManager tm = (TelephonyManager) MyApplication.getContext()
			.getSystemService(MyApplication.getContext().TELEPHONY_SERVICE);
	private static Build bd = new Build();  
	
	/*
	 * 获取设备信息，后续可扩展
	 */
	public static String getDeviceInfo(){
		
		return bd.MODEL;
	}

}
