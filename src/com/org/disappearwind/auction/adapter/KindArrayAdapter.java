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
 * 获取物品分类的Adapter
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
	 * 返回数据量
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return kindArray.length();
	}

	/*
	 * 获取Json格式的数据
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int index) {
		return kindArray.optJSONObject(index);
	}

	/*
	 * 获取当前分类的ID
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
		// 定义一个线性布局，作为根View
		LinearLayout container = new LinearLayout(ctx);
		// 设置为竖向线性布局
		container.setOrientation(0);
		// 左侧的图片
		ImageView imageView = new ImageView(ctx);
		imageView.setPadding(10, 0, 20, 0);
		imageView.setImageResource(R.drawable.kind);
		container.addView(imageView);
		// 右侧的分类名称和描述，用一个LinearLayout展现
		LinearLayout content = new LinearLayout(ctx);
		content.setOrientation(1);
		// 标题
		TextView tvTitle = new TextView(ctx);
		tvTitle.setTextSize(20);
		// 描述
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
