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
	// ��������е�Ԫ��
	EditText txtUserName, txtPwd;
	Button btnLogin, btnCancel;

	/*
	 * ���»����OnCreate������ҳ��ؼ�
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// ��ȡ�û���������
		txtUserName = (EditText) findViewById(R.id.userNameEditText);
		txtPwd = (EditText) findViewById(R.id.userPwdEditText);
		// ��ȡ��ť
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		// �󶨰�ť���¼�
		// btnCancel.setOnClickListener(new HomeListener(this));
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ��У���û����������
				if (validate()) {
					if (doLogin()) {
						showMainActivity();
					}
				}
			}
		});
	}
	/*
	 * ��App��������
	 */
	private void showMainActivity(){
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
	}

	/*
	 * У���û�������
	 */
	private boolean validate() {
		String userName = txtUserName.getText().toString().trim();
		if (userName.equals("")) {
			DialogUtil.showDialog(this, "�û���������д��");
			return false;
		}
		String pwd = txtPwd.getText().toString().trim();
		if (pwd.equals("")) {
			DialogUtil.showDialog(this, "���������д��");
			return false;
		}
		return true;
	}

	/*
	 * �����û���¼
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

			// ��֤�Ƿ��¼�ɹ�
			if (json.getInt("uid") > 0) {
				//�����û�ID
				SharedPrefUtil.savePref(SharedPrefUtil.USER_ID, json.getString("uid"));
				return true;
			} else {
				DialogUtil.showDialog(this, "�û������������");
			}
		} catch (Exception e) {
			DialogUtil.showDialog(this, "��������Ӧ�쳣�����Ժ����ԣ�");
			Log.e("Login", e.getMessage());
		}
		return false;
	}
}
