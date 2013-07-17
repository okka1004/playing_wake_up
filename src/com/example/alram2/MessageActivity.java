package com.example.alram2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.dbadapter.DBAdapter;
import com.type.Alarm_info;

public class MessageActivity extends Activity implements OnClickListener {

	private Button btn_adress;
	private Button btn_save;
	private Button btn_msgback;
	private EditText edt_phone;
	private EditText edt_msgMain;

	ToggleButton toggleBtn_message = null;

	DBAdapter mydb;
	private Alarm_info alarm_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		alarm_info = new Alarm_info();
		mydb = new DBAdapter(getApplicationContext());
		SharedPreferences pref = getSharedPreferences("setting", MODE_PRIVATE);
		Editor editor = pref.edit();

		toggleBtn_message = (ToggleButton) findViewById(R.id.toggleBtn_message);
		btn_adress = (Button) findViewById(R.id.btn_adress);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_msgback = (Button) findViewById(R.id.btn_msgback);
		edt_phone = (EditText) findViewById(R.id.edt_phone);
		edt_msgMain = (EditText) findViewById(R.id.edt_msgMain);
		
		btn_adress.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		btn_msgback.setOnClickListener(this);
		toggleBtn_message.setOnClickListener(this);
		
		btn_adress.setEnabled(false);
		edt_msgMain.setEnabled(false);
		toggleBtn_message.setChecked(false);
		
		editor.putString("phone", "");
		editor.commit();
		
		edt_phone.setText("");
		Log.d("asdfsaf", "");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences pref = getSharedPreferences("setting", MODE_PRIVATE);
		String adress = pref.getString("phone", "");

		edt_phone.setText(adress);
		Log.d("asdfsaf", adress);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.toggleBtn_message:
			if (toggleBtn_message.isChecked()) {
				btn_adress = (Button) findViewById(R.id.btn_adress);
				btn_adress.setEnabled(true);

				edt_phone = (EditText) findViewById(R.id.edt_phone);
				edt_phone.setEnabled(false);
				
				edt_msgMain = (EditText) findViewById(R.id.edt_msgMain);
				edt_msgMain.setEnabled(true);
			} else {
				btn_adress = (Button) findViewById(R.id.btn_adress);
				btn_adress.setEnabled(false);

				edt_phone = (EditText) findViewById(R.id.edt_phone);
				edt_phone.setEnabled(false);
				
				edt_msgMain = (EditText) findViewById(R.id.edt_msgMain);
				edt_msgMain.setEnabled(false);
			}
			break;
		case R.id.btn_adress:
			Log.d("", "clicked");
			Intent myIntent = new Intent(this, AdresslistActivity.class);
			startActivity(myIntent);
			break;
		case R.id.btn_msgback:
			finish();
			break;
		case R.id.btn_save:
			SharedPreferences prefs = getSharedPreferences("setting",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("message", edt_msgMain.getText().toString());
			
			editor.commit();
			
			
			finish();
			break;
		}
	}
}
