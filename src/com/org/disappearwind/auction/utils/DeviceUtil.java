/**
 * 
 */
package com.org.disappearwind.auction.utils;

import android.os.Build;
import android.telephony.TelephonyManager;

;

/**
 * �豸���
 * 
 * @author zhaowh
 *
 */
public class DeviceUtil {
	private static TelephonyManager tm = (TelephonyManager) MyApplication.getContext()
			.getSystemService(MyApplication.getContext().TELEPHONY_SERVICE);
	private static Build bd = new Build();  
	
	/*
	 * ��ȡ�豸��Ϣ����������չ
	 */
	public static String getDeviceInfo(){
		
		return bd.MODEL;
	}

}
