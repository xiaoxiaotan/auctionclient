package com.org.disappearwind.auction;

import com.org.disappearwind.auction.utils.SharedPrefUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private final int TAB_COUNT = 4;
	// 定义Tab内的Fragment
	ItemListFragment hotItemListFragment;
	ItemListFragment lostItemListFragment;
	ItemListFragment userItemListFragment;
	KindFragment kindFragment;

	// 定义底部Tab工具栏的元素
	// Tab1
	View tab1Layout;
	TextView tab1Text;
	ImageView tab1Image;
	// Tab2
	View tab2Layout;
	TextView tab2Text;
	ImageView tab2Image;
	// Tab3
	View tab3Layout;
	TextView tab3Text;
	ImageView tab3Image;
	// Tab4
	View tab4Layout;
	TextView tab4Text;
	ImageView tab4Image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 判断是否登录过（配置信息用有uid），如果是首次进入，则跳入登录页
		if (!SharedPrefUtil.isExist( SharedPrefUtil.USER_ID)) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else {
			setContentView(R.layout.activity_main);
			initView();
			// 刚开始启动时加载第一个Tab
			setTabSelection(1);
		}
	}

	/*
	 * 初始化界面中的控件
	 */
	private void initView() {
		tab1Layout = findViewById(R.id.tab1);
		tab1Layout.setOnClickListener(this);
		tab1Text = (TextView) findViewById(R.id.tab1_text);
		tab1Image = (ImageView) findViewById(R.id.tab1_image);

		tab2Layout = findViewById(R.id.tab2);
		tab2Layout.setOnClickListener(this);
		tab2Text = (TextView) findViewById(R.id.tab2_text);
		tab2Image = (ImageView) findViewById(R.id.tab2_image);

		tab3Layout = findViewById(R.id.tab3);
		tab3Layout.setOnClickListener(this);
		tab3Text = (TextView) findViewById(R.id.tab3_text);
		tab3Image = (ImageView) findViewById(R.id.tab3_image);

		tab4Layout = findViewById(R.id.tab4);
		tab4Layout.setOnClickListener(this);
		tab4Text = (TextView) findViewById(R.id.tab4_text);
		tab4Image = (ImageView) findViewById(R.id.tab4_image);
	}

	/*
	 * 设置选中的Tab页签，并加载对应的Fragment
	 */
	private void setTabSelection(int index) {
		// 每次选中新的页签前先清除上次选中的状态
		clearTabSelection();
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		// 展现当前Tab前先隐藏所有已经显示的Tab,防止多个Fragment在界面上显示
		hideTabFragments(transaction);
		switch (index) {
		case 1:
			tab1Text.setTextColor(getResources().getColor(
					R.color.tab_text_selected));
			tab1Image.setImageResource(R.drawable.tab1_selected);

			if (kindFragment == null) {
				kindFragment = KindFragment.newInstance();
				transaction.add(R.id.content, kindFragment, "tab1");
			} else {
				// KindFragment可能被被ItemListFragment替代
				transaction
						.show(getFragmentManager().findFragmentByTag("tab1"));
			}
			break;
		case 2:
			tab2Text.setTextColor(getResources().getColor(
					R.color.tab_text_selected));
			tab2Image.setImageResource(R.drawable.tab2_selected);
			if (hotItemListFragment == null) {
				hotItemListFragment = ItemListFragment.newInstance(
						ItemListFragment.ITEM_LIST_TYPE.HOT.toString(), "");
				transaction.add(R.id.content, hotItemListFragment, "tab2");
			} else {
				transaction.show(hotItemListFragment);
			}
			break;
		case 3:
			tab3Text.setTextColor(getResources().getColor(
					R.color.tab_text_selected));
			tab3Image.setImageResource(R.drawable.tab3_selected);
			if (lostItemListFragment == null) {
				lostItemListFragment = ItemListFragment.newInstance(
						ItemListFragment.ITEM_LIST_TYPE.LOST.toString(), "");
				transaction.add(R.id.content, lostItemListFragment, "tab3");
			} else {
				transaction.show(lostItemListFragment);
			}
			break;
		case 4:
			tab4Text.setTextColor(getResources().getColor(
					R.color.tab_text_selected));
			tab4Image.setImageResource(R.drawable.tab4_selected);
			if (userItemListFragment == null) {
				userItemListFragment = ItemListFragment.newInstance(
						ItemListFragment.ITEM_LIST_TYPE.User.toString(), "");
				transaction.add(R.id.content, userItemListFragment, "tab4");
			} else {
				transaction.show(userItemListFragment);
			}
			break;
		}
		transaction.commit();
	}

	/*
	 * 清除所有Tab页签的选中状态
	 */
	private void clearTabSelection() {
		tab1Text.setTextColor(getResources().getColor(R.color.tab_text));
		tab2Text.setTextColor(getResources().getColor(R.color.tab_text));
		tab3Text.setTextColor(getResources().getColor(R.color.tab_text));
		tab4Text.setTextColor(getResources().getColor(R.color.tab_text));
		
		tab1Image.setImageResource(R.drawable.tab1_selected);
		tab2Image.setImageResource(R.drawable.tab2_selected);
		tab3Image.setImageResource(R.drawable.tab3_selected);
		tab4Image.setImageResource(R.drawable.tab4_selected);
	}

	/*
	 * 隐藏所有Tab
	 */
	private void hideTabFragments(FragmentTransaction transaction) {
		Fragment fragment = null;
		for (int i = 1; i <= TAB_COUNT; i++) {
			fragment = getFragmentManager().findFragmentByTag("tab" + i);
			if (fragment != null) {
				transaction.hide(fragment);
			}
		}
	}

	/*
	 * Tab 页签的切换事件
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab1:
			setTabSelection(1);
			break;
		case R.id.tab2:
			setTabSelection(2);
			break;
		case R.id.tab3:
			setTabSelection(3);
			break;
		case R.id.tab4:
			setTabSelection(4);
			break;
		}

	}
}
