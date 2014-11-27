package com.org.disappearwind.auction;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.org.disappearwind.auction.adapter.JSONArrayAdapter;
import com.org.disappearwind.auction.utils.DialogUtil;
import com.org.disappearwind.auction.utils.HttpUtil;
import com.org.disappearwind.auction.utils.LogUtil;
import com.org.disappearwind.auction.utils.MyApplication;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link ItemAddFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class ItemAddFragment extends Fragment implements OnClickListener,
		OnDateSetListener {

	final Calendar calendar = Calendar.getInstance();

	EditText txtName;
	EditText txtDesc;
	EditText txtInitPrice;
	EditText txtAvailTime;
	Spinner spinnerKind;
	Button btnAdd;

	public static ItemAddFragment newInstance() {
		ItemAddFragment fragment = new ItemAddFragment();
		return fragment;
	}

	public ItemAddFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_add, container,
				false);
		txtName = (EditText) rootView.findViewById(R.id.txtName);
		txtDesc = (EditText) rootView.findViewById(R.id.txtDesc);
		txtInitPrice = (EditText) rootView.findViewById(R.id.txtInitPrice);
		txtAvailTime = (EditText) rootView.findViewById(R.id.txtAvailTime);
		spinnerKind = (Spinner) rootView.findViewById(R.id.spinnerKind);
		btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

		String currentDate =calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH);
		txtAvailTime.setText(currentDate);
		txtAvailTime.setOnClickListener(this);
		btnAdd.setOnClickListener(this);

		bindKind();
		return rootView;
	}

	/*
	 * 处理现实日期选择的控件
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.txtAvailTime) { // AvailTime需要日期选择
			DatePickerDialog dateDialog = new DatePickerDialog(getActivity(),
					this, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dateDialog.show();
		} else if (v.getId() == R.id.btnAdd) {
			addItem();
		}
	}

	/*
	 * 选日期的控件
	 */
	@Override
	public void onDateSet(DatePicker v, int year, int month, int day) {
		String selectedDate = year + "-" + month + "-" + day;
		txtAvailTime.setText(selectedDate);
	}

	/*
	 * 绑定分类的下拉框
	 */
	private void bindKind() {
		try {
			JSONArray kindList = new JSONArray(HttpUtil.postRequest(
					HttpUtil.URL_KIND_LIST, null));
			if (kindList != null) {
				JSONArrayAdapter adapter = new JSONArrayAdapter(kindList,
						"Name", getActivity());
				spinnerKind.setAdapter(adapter);
				spinnerKind.setPrompt("请选择分类");
			}
		} catch (Exception e) {
			LogUtil.addErrorLog("ItemAddFragment.bindKind", e);
		}
	}

	/*
	 * 添加物品
	 */
	private void addItem() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", txtName.getText().toString());
			params.put("desc", txtDesc.getText().toString());
			params.put("initprice", txtInitPrice.getText().toString());
			params.put("availtime", txtAvailTime.getText().toString());
			params.put("kind", String.valueOf(spinnerKind.getSelectedItemId()));
			String result = HttpUtil.postRequest(HttpUtil.URL_ITEM_ADD, params);
			DialogUtil.showDialog(getActivity(), result);

		} catch (Exception e) {
			LogUtil.addErrorLog("ItemAddFragment.addItem", e);
		}
	}
}
