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
	 * ѡ��ĳһ�����ݵ��¼�
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
		case 0: // ��ӷ���
			showAddKindView(l.getContext());
			break;
		case 1: // �����Ʒ
			ItemAddFragment itemAddFragment = ItemAddFragment.newInstance();
			showFragment(itemAddFragment);
			break;
		case 2: // �鿴�Լ�����Ʒ
			ItemListFragment itemListFragment = ItemListFragment.newInstance(
					ItemListFragment.ITEM_LIST_TYPE.USER.toString(), null);
			itemListFragment.AllowBid =false;
			showFragment(itemListFragment);
			break;
		case 3: // �鿴���뾺�ĵ���Ʒ
			showInProcessMsg();
			break;
		case 4: // �鿴���õ���Ʒ
			showInProcessMsg();
			break;
		case 5: // ���ڡ�����������������������������������������
			showInProcessMsg();
			break;
		}
	}
	/*
	 * ������δ��������ʾ
	 */
	private void showInProcessMsg(){
		Toast.makeText(getActivity(), "�������ڿ�������δ���ߣ������ڴ���",Toast.LENGTH_SHORT).show();
	}
	/*
	 * ��Fragment
	 */
	private void showFragment(Fragment fragment){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(containerId, fragment, getTag());
		transaction.addToBackStack(getTag());
		transaction.commit();
	}

	/*
	 * ��ӷ���Ĵ���
	 */
	private void showAddKindView(Context ctx) {
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
