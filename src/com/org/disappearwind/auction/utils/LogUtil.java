/**
 * 
 */
package com.org.disappearwind.auction.utils;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;


/**
 * 日志记录统一处理
 * @author zhaowh
 *
 */
public class LogUtil {
	/*
	 * 记录普通的日志，输出到LogCat中
	 */
	public static void addLog(String source,String msg)
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put(SQLiteUtil.SQL_LOG_CLOUMN_SOURCE, source);
		params.put(SQLiteUtil.SQL_LOG_CLOUMN_MESSAGE, msg);
		SQLiteUtil.insertLog(params);
		Log.e(source,msg);
	}
	/*
	 * 记录异常日志，如果Message中无数据读取异常栈
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
