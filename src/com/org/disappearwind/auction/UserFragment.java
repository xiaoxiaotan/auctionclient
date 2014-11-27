package com.org.disappearwind.auction;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.org.disappearwind.auction.utils.DialogUtil;
import com.org.disappearwind.auction.utils.HttpUtil;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class UserFragment extends ListFragment {

	private int containerId = 0;

	public static UserFragment newInstance() {
		return new UserFragment();
	}

	public UserFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container != null) {
			containerId = container.getId();
		}
		return inflater.inflate(R.layout.fragment_user, container, false);
	}

	/*
	 * 选中某一条数据的事件
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
		case 0: // 添加分类
			showAddKindView(l.getContext());
			break;
		case 1: // 添加物品
			ItemAddFragment itemAddFragment = ItemAddFragment.newInstance();
			showFragment(itemAddFragment);
			break;
		case 2: // 查看自己的物品
			ItemListFragment itemListFragment = ItemListFragment.newInstance(
					ItemListFragment.ITEM_LIST_TYPE.USER.toString(), null);
			itemListFragment.AllowBid =false;
			showFragment(itemListFragment);
			break;
		case 3: // 查看参与竞拍的物品
			showInProcessMsg();
			break;
		case 4: // 查看竞得的物品
			showInProcessMsg();
			break;
		case 5: // 关于·····················
			showInProcessMsg();
			break;
		}
	}
	/*
	 * 弹出还未开发的提示
	 */
	private void showInProcessMsg(){
		Toast.makeText(getActivity(), "功能正在开发，还未上线！敬请期待！",Toast.LENGTH_SHORT).show();
	}
	/*
	 * 打开Fragment
	 */
	private void showFragment(Fragment fragment){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(containerId, fragment, getTag());
		transaction.addToBackStack(getTag());
		transaction.commit();
	}

	/*
	 * 添加分类的窗体
	 */
	private void showAddKindView(Context ctx) {
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
