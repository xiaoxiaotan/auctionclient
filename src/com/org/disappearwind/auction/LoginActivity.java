package com.org.disappearwind.auction;

import org.json.JSONObject;

import java.util.*;

import com.org.disappearwind.auction.utils.DialogUtil;
import com.org.disappearwind.auction.utils.HttpUtil;
import com.org.disappearwind.auction.utils.SharedPrefUtil;
import com.org.disappearwind.auction.KindFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class LoginActivity extends Activity {

	final String URL_LOGIN = "login";
	// 定义界面中的元素
	EditText txtUserName, txtPwd;
	Button btnLogin, btnCancel;

	/*
	 * 重新基类的OnCreate，加载页面控件
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// 获取用户名和密码
		txtUserName = (EditText) findViewById(R.id.userNameEditText);
		txtPwd = (EditText) findViewById(R.id.userPwdEditText);
		// 获取按钮
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		// 绑定按钮的事件
		// btnCancel.setOnClickListener(new HomeListener(this));
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 先校验用户输入的数据
				if (validate()) {
					if (doLogin()) {
						showMainActivity();
					}
				}
			}
		});
	}
	/*
	 * 打开App的主界面
	 */
	private void showMainActivity(){
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
	}

	/*
	 * 校验用户的输入
	 */
	private boolean validate() {
		String userName = txtUserName.getText().toString().trim();
		if (userName.equals("")) {
			DialogUtil.showDialog(this, "用户名必须填写！");
			return false;
		}
		String pwd = txtPwd.getText().toString().trim();
		if (pwd.equals("")) {
			DialogUtil.showDialog(this, "密码必须填写！");
			return false;
		}
		return true;
	}

	/*
	 * 处理用户登录
	 */
	private boolean doLogin() {
		String userName = txtUserName.getText().toString().trim();
		String pwd = txtPwd.getText().toString().trim();
		JSONObject json;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", userName);
			params.put("pwd", pwd);

			json = new JSONObject(HttpUtil.postRequest(HttpUtil.URL_BASE
					+ URL_LOGIN, params));

			// 验证是否登录成功
			if (json.getInt("uid") > 0) {
				//保存用户ID
				SharedPrefUtil.savePref(SharedPrefUtil.USER_ID, json.getString("uid"));
				return true;
			} else {
				DialogUtil.showDialog(this, "用户名或密码错误！");
			}
		} catch (Exception e) {
			DialogUtil.showDialog(this, "服务器响应异常，请稍后再试！");
			Log.e("Login", e.getMessage());
		}
		return false;
	}
}
