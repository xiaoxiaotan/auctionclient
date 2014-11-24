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
	// ��Activity��ֵ��
	private static final String ITEM_ID = "BID_ITEM_ID";
	private static final String ITEM = "BID_ITEM";

	private String mItem;
	private String mItemID;

	// ��itemתΪJSONObject����ʹ��
	private JSONObject currentItem = null;

	// �����ϵĿؼ�
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
	 *            ��ƷID
	 * @param item
	 *            ��Ʒʵ�壬JSON��ʽ��
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
			currentItem = new JSONObject(mItem); // ��ʼ��currentItem
			// ������ؼ���ֵ
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
	 * ���ģ��������壬�����û������
	 */
	private void ShowAddBid() {
		final AlertDialog.Builder addBidBuilder = new AlertDialog.Builder(
				getActivity());
		addBidBuilder.setTitle(R.string.add_bid);
		final EditText etBid = new EditText(getActivity());
		etBid.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		addBidBuilder.setView(etBid);
		addBidBuilder.setPositiveButton("���",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Long bidPrice = Long.valueOf(etBid.getText()
									.toString());
							if (addBid(bidPrice)) {
								// ��ǰ����ۼ�Ϊ��Ʒ����߼�
								tvMaxPrice.setText(String.valueOf(bidPrice));
							}
							onCreate(null);
						} catch (Exception e) {
							LogUtil.addErrorLog(
									"ItemDetailFragment.ShowAddBid", e);
						}
					}
				});
		addBidBuilder.setNegativeButton("ȡ��", null);
		addBidBuilder.show();
	}

	/*
	 * /* ��Ӿ�����Ϣ
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

			// ��֤�Ƿ���ӳɹ�
			int message = 0;
			// ����˷����쳣
			if (!json.has("result")) {
				message = R.string.add_bid_message_error;
				LogUtil.addLog("ItemDetailFragment.addBid", json.toString());
			} else if (json.getInt("result") > 0) { // �ɹ�
				message = R.string.add_bid_message_sucessfully;
				result = true;
			} else { // ����˲��������
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
