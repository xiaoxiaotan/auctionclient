package com.org.disappearwind.auction;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.org.disappearwind.auction.utils.DialogUtil;
import com.org.disappearwind.auction.utils.HttpUtil;
import com.org.disappearwind.auction.utils.LogUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link ItemDetailFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link ItemDetailFragment#newInstance} factory method to create an instance
 * of this fragment.
 *
 */
public class ItemDetailFragment extends DialogFragment implements
		OnClickListener {
	// 与Activity传值用
	private static final String ITEM_ID = "BID_ITEM_ID";
	private static final String ITEM = "BID_ITEM";

	private String mItem;
	private String mItemID;

	// 把item转为JSONObject对象使用
	private JSONObject currentItem = null;

	// 界面上的控件
	TextView tvItemName;
	TextView tvInitPrice;
	TextView tvMaxPrice;
	TextView tvAvailTime;
	TextView tvDesc;
	Button btnAddBid;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param itemID
	 *            物品ID
	 * @param item
	 *            物品实体，JSON格式的
	 * @return A new instance of fragment ItemDetailFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ItemDetailFragment newInstance(String itemID, String item) {
		ItemDetailFragment fragment = new ItemDetailFragment();
		Bundle args = new Bundle();
		args.putString(ITEM_ID, itemID);
		args.putString(ITEM, item);
		fragment.setArguments(args);
		return fragment;
	}

	public ItemDetailFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mItemID = getArguments().getString(ITEM_ID);
			mItem = getArguments().getString(ITEM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);
		try {
			currentItem = new JSONObject(mItem); // 初始化currentItem
			// 给界面控件赋值
			tvItemName = (TextView) rootView.findViewById(R.id.tvItemName);
			tvItemName.setText(currentItem.optString("Name"));
			tvInitPrice = (TextView) rootView
					.findViewById(R.id.tvItemInitPrice);
			tvInitPrice.setText(String.valueOf(currentItem
					.optDouble("InitPrice")));
			tvMaxPrice = (TextView) rootView.findViewById(R.id.tvItemMaxPrice);
			tvMaxPrice
					.setText(String.valueOf(currentItem.optDouble("MaxPrice")));
			tvAvailTime = (TextView) rootView
					.findViewById(R.id.tvItemAvailTime);
			tvAvailTime.setText(currentItem.optString("AvailTime"));
			tvDesc = (TextView) rootView.findViewById(R.id.tvItemDesc);
			tvDesc.setText(currentItem.optString("Desc"));

			btnAddBid = (Button) rootView.findViewById(R.id.btnAddBid);
			btnAddBid.setOnClickListener(this);
		} catch (Exception e) {
			LogUtil.addErrorLog("ItemDetailFragment.onCreateView", e);
		}
		return rootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		int viewID = v.getId();
		switch (viewID) {
		case R.id.btnAddBid:
			ShowAddBid();
			break;
		}
	}

	/*
	 * 竞拍，弹出窗体，处理用户输入等
	 */
	private void ShowAddBid() {
		final AlertDialog.Builder addBidBuilder = new AlertDialog.Builder(
				getActivity());
		addBidBuilder.setTitle(R.string.add_bid);
		final EditText etBid = new EditText(getActivity());
		etBid.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		addBidBuilder.setView(etBid);
		addBidBuilder.setPositiveButton("添加",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Long bidPrice = Long.valueOf(etBid.getText()
									.toString());
							if (addBid(bidPrice)) {
								// 当前竞标价即为物品的最高价
								tvMaxPrice.setText(String.valueOf(bidPrice));
							}
							onCreate(null);
						} catch (Exception e) {
							LogUtil.addErrorLog(
									"ItemDetailFragment.ShowAddBid", e);
						}
					}
				});
		addBidBuilder.setNegativeButton("取消", null);
		addBidBuilder.show();
	}

	/*
	 * /* 添加竞拍信息
	 */
	private boolean addBid(double bidPrice) {
		JSONObject json = null;
		boolean result = false;
		try {
			Resources res = getResources();
			Map<String, String> params = new HashMap<String, String>();
			params.put("itemid", mItemID);
			params.put("price", String.valueOf(bidPrice));

			json = new JSONObject(HttpUtil.postRequest(HttpUtil.URL_BID_ADD,
					params));

			// 验证是否添加成功
			int message = 0;
			// 服务端返回异常
			if (!json.has("result")) {
				message = R.string.add_bid_message_error;
				LogUtil.addLog("ItemDetailFragment.addBid", json.toString());
			} else if (json.getInt("result") > 0) { // 成功
				message = R.string.add_bid_message_sucessfully;
				result = true;
			} else { // 服务端不允许添加
				message = R.string.add_bid_message_failed;
			}
			DialogUtil.showDialog(getActivity(), res.getText(message)
					.toString());
		} catch (Exception e) {
			LogUtil.addErrorLog("ItemDetailFragment.addBid", e);
		}
		return result;
	}
}
