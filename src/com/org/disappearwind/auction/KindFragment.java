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
	Button btnAdd;
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
		btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				// 打开添加分类的窗体
				ShowAddKindView(source.getContext());
			}
		});

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

	/*
	 * 添加分类的窗体
	 */
	private void ShowAddKindView(Context ctx) {
		// 采用弹出形式
		final AlertDialog.Builder addKindBuilder = new AlertDialog.Builder(ctx);
		addKindBuilder.setTitle(R.string.add_kind);
		// 从XML中加载界面
		LayoutInflater inflater = LayoutInflater.from(ctx);
		final View addKindView = inflater.inflate(R.layout.fragment_kind_add,
				null);
		addKindBuilder.setView(addKindView);
		// 添加按钮
		addKindBuilder.setPositiveButton("添加",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String kindName = ((EditText) addKindView
								.findViewById(R.id.txtKindName)).getText()
								.toString();
						String kindDesc = ((EditText) addKindView
								.findViewById(R.id.txtKindDesc)).getText()
								.toString();
						if (validateKind(addKindBuilder.getContext(), kindName,
								kindDesc)) {
							addKind(kindName, kindDesc);
							onCreate(null);
						}
					}
				});
		addKindBuilder.setNegativeButton("取消", null);
		addKindBuilder.show();
	}

	/*
	 * 验证用户输入的分类信息
	 */
	private boolean validateKind(Context ctx, String kindName, String kindDesc) {
		if (kindName.equals("")) {
			DialogUtil.showDialog(ctx, "名称不能为空！");
			return false;
		}
		if (kindDesc.equals("")) {
			DialogUtil.showDialog(ctx, "描述不能为空！");
			return false;
		}
		return true;
	}

	/*
	 * 调用服务端添加分类
	 */
	private void addKind(String kindName, String kindDesc) {
		JSONObject json;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", kindName);
			params.put("desc", kindDesc);

			String result = HttpUtil.postRequest(HttpUtil.URL_KIND_ADD, params);
		} catch (Exception e) {
			DialogUtil.showDialog(getActivity(), "服务器响应异常，请稍后再试！");
			Log.e("KindFragment.AddKind", e.getMessage());
		}
	}
}
