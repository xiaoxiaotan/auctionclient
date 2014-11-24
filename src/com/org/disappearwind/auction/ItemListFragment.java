package com.org.disappearwind.auction;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.org.disappearwind.auction.adapter.ItemArrayAdapter;
import com.org.disappearwind.auction.utils.HttpUtil;
import com.org.disappearwind.auction.utils.LogUtil;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link ItemListFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link ItemListFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class ItemListFragment extends ListFragment {
	/*
	 * 列表的类型，如取最热门、分类下的、用户的等等
	 */
	public static enum ITEM_LIST_TYPE {
		LOST, KIND, HOT, USER,
	}

	private static final String LIST_TYPE = "LIST_TYPE";
	private static final String LIST_PARAM = "LIST_PARAM";

	public static boolean AllowBid = true;

	private String mListType;
	private String mListParam;

	/**
	 * 创建ItemListFragment
	 *
	 * @param listType
	 *            列表的类型，如取最热门、分类下的、用户的等等
	 * @param listParam
	 *            列表取数据的参数，如用户ID，分类ID等，默认为0
	 * @return A new instance of fragment ItemListFragment.
	 */
	public static ItemListFragment newInstance(String listType, String listParam) {
		ItemListFragment fragment = new ItemListFragment();
		Bundle args = new Bundle();
		args.putString(LIST_TYPE, listType);
		args.putString(LIST_PARAM, listParam);
		fragment.setArguments(args);
		return fragment;
	}

	public ItemListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mListType = getArguments().getString(LIST_TYPE);
			mListParam = getArguments().getString(LIST_PARAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		JSONArray itemArray = GetListData();
		setListAdapter(new ItemArrayAdapter(itemArray, this.getActivity()));
		return inflater.inflate(R.layout.fragment_item_list, container, false);
	}

	/*
	 * 选中某一条数据的事件
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (AllowBid) {
			try {
				JSONObject selectedItem = (JSONObject) l.getAdapter().getItem(
						position);
				String itemID = selectedItem.getString("ID");
				String item = selectedItem.toString();
				ItemDetailFragment bidFragment = ItemDetailFragment
						.newInstance(itemID, item);
				FragmentTransaction fTran = getFragmentManager()
						.beginTransaction();
				bidFragment.show(fTran, "tt");
			} catch (Exception e) {
				Log.e("KindActivity.onListItemClick", e.getMessage());
			}
		}
	}

	/*
	 * 根据ListType从服务端取数据
	 */
	private JSONArray GetListData() {
		JSONArray itemList = null;
		Map<String, String> params = new HashMap<String, String>();
		String postResult = "";
		String url = "";
		try {
			ITEM_LIST_TYPE type = ITEM_LIST_TYPE.valueOf(mListType);
			switch (type) {
			case LOST:
				url = HttpUtil.URL_LIST_ITEM_LOST;
				break;
			case KIND:
				url = HttpUtil.URL_LIST_ITEM_KIND;
				params.put("kindid", mListParam);
				break;
			case HOT:
				url = HttpUtil.URL_LIST_ITEM_HOT;
				break;
			case USER:
				url = HttpUtil.URL_LIST_ITEM_USER;
				break;
			}
			postResult = HttpUtil.postRequest(url, params);
			itemList = new JSONArray(postResult);
		} catch (Exception e) {
			LogUtil.addLog("ItemListFragment.GetListData", postResult);
			LogUtil.addErrorLog("ItemListFragment.GetListData", e);
		}
		return itemList;
	}
}
