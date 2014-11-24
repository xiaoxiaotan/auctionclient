/**
 * 
 */
package com.org.disappearwind.auction.adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.org.disappearwind.auction.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ��ȡ��Ʒ�����Adapter
 * 
 * @author zhaowh
 *
 */
public class KindArrayAdapter extends BaseAdapter {
	private JSONArray kindArray;
	private Context ctx;

	public KindArrayAdapter(JSONArray kindArray, Context ctx) {
		this.kindArray = kindArray;
		this.ctx = ctx;
	}

	/*
	 * ����������
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return kindArray.length();
	}

	/*
	 * ��ȡJson��ʽ������
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int index) {
		return kindArray.optJSONObject(index);
	}

	/*
	 * ��ȡ��ǰ�����ID
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int index) {
		try {
			return ((JSONObject) getItem(index)).getLong("ID");
		} catch (Exception e) {
			Log.e("KindArrayAdapter.getItemId", e.getMessage());
		}
		return -1;
	}

	/*
	 * getView
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// ����һ�����Բ��֣���Ϊ��View
		LinearLayout container = new LinearLayout(ctx);
		// ����Ϊ�������Բ���
		container.setOrientation(0);
		// ����ͼƬ
		ImageView imageView = new ImageView(ctx);
		imageView.setPadding(10, 0, 20, 0);
		imageView.setImageResource(R.drawable.kind);
		container.addView(imageView);
		// �Ҳ�ķ������ƺ���������һ��LinearLayoutչ��
		LinearLayout content = new LinearLayout(ctx);
		content.setOrientation(1);
		// ����
		TextView tvTitle = new TextView(ctx);
		tvTitle.setTextSize(20);
		// ����
		TextView tvDesc = new TextView(ctx);
		try {
			JSONObject item = (JSONObject) getItem(index);
			String name = item.getString("Name");
			String desc = item.getString("Desc");
			tvTitle.setText(name);
			tvDesc.setText(desc);
		} catch (Exception e) {
			Log.e("KindArrayAdapter.getView", e.getMessage());
		}
		content.addView(tvTitle);
		content.addView(tvDesc);
		container.addView(content);
		return container;
	}
}
