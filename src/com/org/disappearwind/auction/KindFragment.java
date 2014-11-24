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

	// ʢ�ű�KindFragment������ID�������ItemListFragment
	private int containerId = 0;

	/**
	 * ����KindFragment
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
				// ����ӷ���Ĵ���
				ShowAddKindView(source.getContext());
			}
		});

		JSONArray itemArray = GetListData();
		listAdapter = new KindArrayAdapter(itemArray, this.getActivity());
		setListAdapter(listAdapter);
		return rootView;
	}

	/*
	 * ѡ��ĳһ�����ݵ��¼�
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
	 * �ӷ���˻�ȡ����
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
	 * ��ӷ���Ĵ���
	 */
	private void ShowAddKindView(Context ctx) {
		// ���õ�����ʽ
		final AlertDialog.Builder addKindBuilder = new AlertDialog.Builder(ctx);
		addKindBuilder.setTitle(R.string.add_kind);
		// ��XML�м��ؽ���
		LayoutInflater inflater = LayoutInflater.from(ctx);
		final View addKindView = inflater.inflate(R.layout.fragment_kind_add,
				null);
		addKindBuilder.setView(addKindView);
		// ��Ӱ�ť
		addKindBuilder.setPositiveButton("���",
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
		addKindBuilder.setNegativeButton("ȡ��", null);
		addKindBuilder.show();
	}

	/*
	 * ��֤�û�����ķ�����Ϣ
	 */
	private boolean validateKind(Context ctx, String kindName, String kindDesc) {
		if (kindName.equals("")) {
			DialogUtil.showDialog(ctx, "���Ʋ���Ϊ�գ�");
			return false;
		}
		if (kindDesc.equals("")) {
			DialogUtil.showDialog(ctx, "��������Ϊ�գ�");
			return false;
		}
		return true;
	}

	/*
	 * ���÷������ӷ���
	 */
	private void addKind(String kindName, String kindDesc) {
		JSONObject json;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", kindName);
			params.put("desc", kindDesc);

			String result = HttpUtil.postRequest(HttpUtil.URL_KIND_ADD, params);
		} catch (Exception e) {
			DialogUtil.showDialog(getActivity(), "��������Ӧ�쳣�����Ժ����ԣ�");
			Log.e("KindFragment.AddKind", e.getMessage());
		}
	}
}
