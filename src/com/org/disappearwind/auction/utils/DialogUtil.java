/**
 * 
 */
package com.org.disappearwind.auction.utils;

import android.app.AlertDialog;
import android.content.Context;

/**
 * 弹出提示统一处理
 * @author zhaowh
 *
 */
public class DialogUtil {
	/*
	 * 弹出提示框，只有确定按钮
	 */
	public static void showDialog(Context ctx,String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg).setCancelable(false);
		builder.setPositiveButton("确定", null);
		builder.create().show();
	}
}
