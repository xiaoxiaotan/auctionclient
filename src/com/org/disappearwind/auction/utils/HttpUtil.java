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
 * Http ����ͨ�÷���
 * 
 * @author zhaowh
 * @version 1.0
 * @create date 2014-11-18
 */
public class HttpUtil {
	// ͳһ��HttpClient����
	public static HttpClient httpClient = new DefaultHttpClient();
	// ����˵�ַ
	public static final String URL_BASE = "http://192.168.67.110:8011/service/";
	// �����ӿڵĵ�ַ
	// ��¼
	public static final String URL_LOGIN = "Login";
	// ��ȡ�����б�
	public static final String URL_KIND_LIST = "GetKindList";
	// ��ӷ���
	public static final String URL_KIND_ADD = "AddKind";
	// �����Ʒ
	public static final String URL_ITEM_ADD = "AddItem";
	// ��ȡ�����µ���Ʒ
	public static final String URL_LIST_ITEM_LOST = "GetLostItemList";
	// ��ȡ�����µ���Ʒ
	public static final String URL_LIST_ITEM_KIND = "GetKindItemList";
	// ��ȡ����������Ʒ
	public static final String URL_LIST_ITEM_HOT = "GetHotItemList";
	// ��ȡ�û��ϴ�����Ʒ
	public static final String URL_LIST_ITEM_USER = "GetUserItemList";
	// ��ȡ�û����Ĺ�����Ʒ
	public static final String URL_LIST_ITEM_USER_BID = "GetUserBidItemList";
	// ����
	public static final String URL_BID_ADD = "AddBid";

	/**
	 * �����������ַ�����ؽ��,ǿ��ͬ����ʽ�����ã���������UI
	 * 
	 * @param url
	 *            ����ĵ�ַ
	 * @return
	 * @throws Exception
	 */
	public static String getRequest(final String url) throws Exception {
		HttpGet get = new HttpGet(generateUrl(url));
		//ǿ��ͬ��ִ�У��ȴ������������
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().build());
		// ����Get���󣬲���ȡ���ص�����
		HttpResponse response = httpClient.execute(get);
		// ����ɹ�������������ݣ����򷵻ؿ�
		if (response.getStatusLine().getStatusCode() == 200) {
			return EntityUtils.toString(response.getEntity());
		} else {
			Log.e("HttpUtil.getRequest", response
					.getStatusLine().getReasonPhrase());
			return response.getStatusLine().getReasonPhrase();
		}
	}

	/**
	 * Post��ʽ��������
	 * 
	 * @param url
	 *            �����Url
	 * @param params
	 *            POST������
	 * @return
	 * @throws Exception
	 */
	public static String postRequest(final String url,
			final Map<String, String> params) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// ����HttpPost����
						HttpPost post = new HttpPost(generateUrl(url));
						if (params != null) {
							// ��װPOST�Ĵ�������
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

						// ����Post���󣬲���ȡ���ص�����
						HttpResponse response = httpClient.execute(post);
						// ����ɹ�������������ݣ����򷵻ؿ�
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
	 * ����url�����û��http//����https//���Զ���URL_BASE
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
	 * ������нӿڶ���Ҫ���Ĺ�������������uid��devicenum��
	 */
	private static Map<String, String> addCommonParams(
			Map<String, String> params) {
		// uid
		params.put("uid", SharedPrefUtil.getPref(SharedPrefUtil.USER_ID));
		// �豸��Ϣ
		params.put("device", DeviceUtil.getDeviceInfo());
		return params;
	}
}
