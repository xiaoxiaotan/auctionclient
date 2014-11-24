package com.org.disappearwind.auction;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.org.disappearwind.auction.utils.DialogUtil;
import com.org.disappearwind.auction.utils.HttpUtil;
import com.org.disappearwind.auction.utils.LogUtil;
import com.org.disappearwind.auction.adapter.KindArrayAdapter;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class KindFragment extends ListFragment {
	KindArrayAdapter listAdapter;

	// 盛放本KindFragment的容器ID，用与打开ItemListFragment
	private int containerId = 0;

	/**
	 * 创建KindFragment
	 *
	 * @return A new instance of fragment KindFragment.
	 */
	public static KindFragment newInstance() {
		KindFragment fragment = new KindFragment();
		return fragment;
	}

	public KindFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_kind, container,
				false);
		if (container != null) {
			containerId = container.getId();
		}
		JSONArray itemArray = GetListData();
		listAdapter = new KindArrayAdapter(itemArray, this.getActivity());
		setListAdapter(listAdapter);
		return rootView;
	}

	/*
	 * 选中某一条数据的事件
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		try {
			JSONObject selectedItem = (JSONObject) listAdapter
					.getItem(position);
			String itemID = selectedItem.getString("ID");
			ItemListFragment itemListFragment = ItemListFragment.newInstance(
					ItemListFragment.ITEM_LIST_TYPE.KIND.toString(), itemID);
			FragmentTransaction transaction = getActivity()
					.getFragmentManager().beginTransaction();
			transaction.replace(containerId, itemListFragment, getTag());
			transaction.addToBackStack(getTag());
			transaction.commit();
		} catch (Exception e) {
			Log.e("KindFragment.onListItemClick", e.getMessage());
		}
	}

	/*
	 * 从服务端获取数据
	 */
	private JSONArray GetListData() {
		JSONArray itemList = null;
		Map<String, String> params = new HashMap<String, String>();
		try {
			itemList = new JSONArray(HttpUtil.postRequest(
					HttpUtil.URL_KIND_LIST, params));
		} catch (Exception e) {
			LogUtil.addErrorLog("KindFragment.GetListData", e);
		}
		return itemList;
	}
}
