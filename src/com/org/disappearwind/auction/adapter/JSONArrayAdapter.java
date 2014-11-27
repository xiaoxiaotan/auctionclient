/**
 * 
 */
package com.org.disappearwind.auction.adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.org.disappearwind.auction.R;
import com.org.disappearwind.auction.utils.LogUtil;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 获取JSON数据的dapter，取某一Key的所有数据
 * 
 * @author zhaowh
 *
 */
public class JSONArrayAdapter extends BaseAdapter {
	private JSONArray jsonArray;
	private String jsonKey;
	private Context ctx;

	public JSONArrayAdapter(JSONArray jsonArray, String jsonKey, Context ctx) {
		this.jsonArray = jsonArray;
		this.jsonKey = jsonKey;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return jsonArray.length();
	}

	@Override
	public Object getItem(int index) {
		return jsonArray.optJSONObject(index);
	}

	@Override
	public long getItemId(int index) {
		try {
			return ((JSONObject) getItem(index)).getLong("ID");
		} catch (Exception e) {
			LogUtil.addErrorLog("JSONArrayAdapter.getItemId", e);
		}
		return -1;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		TextView tv = new TextView(ctx);
		tv.setTextSize(R.dimen.label_font_size);
		try {
			JSONObject item = (JSONObject) getItem(index);
			tv.setText(item.getString(jsonKey));
			tv.setTextColor(Color.BLUE ); 
		} catch (Exception e) {
			LogUtil.addErrorLog("JSONArrayAdapter.getView", e);
		}
		return tv;
	}
}
