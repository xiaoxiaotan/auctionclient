/**
 * 
 */
package com.org.disappearwind.auction.utils;

import android.app.AlertDialog;
import android.content.Context;

/**
 * ������ʾͳһ����
 * @author zhaowh
 *
 */
public class DialogUtil {
	
	/*
	 * ʹ��ȫ�ֵ�Context
	 */
	public static void showDialog(String msg)
	{
		showDialog(MyApplication.getContext(),msg);
	}
	/*
	 * ������ʾ��ֻ��ȷ����ť
	 */
	public static void showDialog(Context ctx,String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg).setCancelable(false);
		builder.setPositiveButton("ȷ��", null);
		builder.create().show();
	}
}
