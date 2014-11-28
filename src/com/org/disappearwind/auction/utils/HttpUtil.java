package com.org.disappearwind.auction.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.StrictMode;
import android.util.Log;

/**
 * Http 请求通用方法
 * 
 * @author zhaowh
 * @version 1.0
 * @create date 2014-11-18
 */
public class HttpUtil {
	// 统一的HttpClient对象
	public static HttpClient httpClient = new DefaultHttpClient();
	// 服务端地址
	public static final String URL_BASE = "http://192.168.67.110:8011/service/";
	// 各个接口的地址
	// 登录
	public static final String URL_LOGIN = "Login";
	// 获取分类列表
	public static final String URL_KIND_LIST = "GetKindList";
	// 添加分类
	public static final String URL_KIND_ADD = "AddKind";
	// 添加物品
	public static final String URL_ITEM_ADD = "AddItem";
	// 获取分类下的物品
	public static final String URL_LIST_ITEM_LOST = "GetLostItemList";
	// 获取分类下的物品
	public static final String URL_LIST_ITEM_KIND = "GetKindItemList";
	// 获取竞拍热门物品
	public static final String URL_LIST_ITEM_HOT = "GetHotItemList";
	// 获取用户上传的物品
	public static final String URL_LIST_ITEM_USER = "GetUserItemList";
	// 获取用户竞拍过的物品
	public static final String URL_LIST_ITEM_USER_BID = "GetUserBidItemList";
	// 竞拍
	public static final String URL_BID_ADD = "AddBid";

	/**
	 * 请求服务器地址并返回结果,强制同步方式，慎用！可能阻塞UI
	 * 
	 * @param url
	 *            请求的地址
	 * @return
	 * @throws Exception
	 */
	public static String getRequest(final String url) throws Exception {
		HttpGet get = new HttpGet(generateUrl(url));
		//强制同步执行，等待网络请求结束
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().build());
		// 发送Get请求，并获取返回的数据
		HttpResponse response = httpClient.execute(get);
		// 请求成功返回请求的数据，否则返回空
		if (response.getStatusLine().getStatusCode() == 200) {
			return EntityUtils.toString(response.getEntity());
		} else {
			Log.e("HttpUtil.getRequest", response
					.getStatusLine().getReasonPhrase());
			return response.getStatusLine().getReasonPhrase();
		}
	}

	/**
	 * Post方式请求数据
	 * 
	 * @param url
	 *            请求的Url
	 * @param params
	 *            POST的数据
	 * @return
	 * @throws Exception
	 */
	public static String postRequest(final String url,
			final Map<String, String> params) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// 创建HttpPost对象
						HttpPost post = new HttpPost(generateUrl(url));
						if (params != null) {
							// 组装POST的带的数据
							List<NameValuePair> postParams = new ArrayList<NameValuePair>();
							for (String key : params.keySet()) {
								postParams.add(new BasicNameValuePair(key,
										params.get(key)));
							}
							// TO Do ,get uid from sharedpreference
							if (!params.containsKey("uid")) {
								postParams.add(new BasicNameValuePair("uid",
										"1"));
							}
							post.setEntity(new UrlEncodedFormEntity(postParams,
									"utf-8"));
						}

						// 发送Post请求，并获取返回的数据
						HttpResponse response = httpClient.execute(post);
						// 请求成功返回请求的数据，否则返回空
						if (response.getStatusLine().getStatusCode() == 200) {
							return EntityUtils.toString(response.getEntity());
						} else {
							Log.e("HttpUtil.postRequest", params.toString());
							Log.e("HttpUtil.postRequest", response
									.getStatusLine().getReasonPhrase());
							return response.getStatusLine().getReasonPhrase();
						}
					}
				});
		new Thread(task).start();
		return task.get();
	}

	/*
	 * 处理url，如果没有http//或者https//则自动加URL_BASE
	 */
	public static String generateUrl(String url) {
		if (null == url || url.equals("")
				|| url.toLowerCase().contains("http://")
				|| url.toLowerCase().contains("https://")) {
			return url;
		} else {
			return URL_BASE + url;
		}
	}

	/*
	 * 添加所有接口都需要传的公共参数，比如uid，devicenum等
	 */
	private static Map<String, String> addCommonParams(
			Map<String, String> params) {
		// uid
		params.put("uid", SharedPrefUtil.getPref(SharedPrefUtil.USER_ID));
		// 设备信息
		params.put("device", DeviceUtil.getDeviceInfo());
		return params;
	}
}
