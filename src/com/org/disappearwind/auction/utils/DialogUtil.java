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
