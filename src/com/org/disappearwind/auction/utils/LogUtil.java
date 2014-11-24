/**
 * 
 */
package com.org.disappearwind.auction.utils;

import android.util.Log;


/**
 * ��־��¼ͳһ����
 * @author zhaowh
 *
 */
public class LogUtil {
	/*
	 * ��¼��ͨ����־�������LogCat��
	 */
	public static void addLog(String source,String msg)
	{
		Log.e(source,msg);
	}
	/*
	 * ��¼�쳣��־�����Message�������ݶ�ȡ�쳣ջ
	 */
	public static void addErrorLog(String source,Exception e)
	{
		String msg = e.getMessage();
		if(msg == null || msg.equals("")){
			msg=e.getCause().getMessage();
		}
		LogUtil.addLog(source,msg);
	}
}
