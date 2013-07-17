package com.example.listview;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.dbadapter.DBAdapter;
import com.example.alram2.R;
import com.example.alram2.SettingActivity;
import com.type.Alarm_info;

public class ListActivity extends Activity implements OnClickListener,
		OnItemLongClickListener {

	private ListView listView_alram;
	private Adapter_alarmlist adapter_alarm;
	private ArrayList<Alarm_info> listdata;
	public static DBAdapter mydb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_list);

		mydb = new DBAdapter(getApplicationContext());
		mydb.open();
		listdata = mydb.get_Alarm_list();
		mydb.close();

		if (listdata.isEmpty())
			listdata = new ArrayList<Alarm_info>();
		adapter_alarm = new Adapter_alarmlist(this, listdata);

		listView_alram = (ListView) findViewById(R.id.listView_alram);

		Button btn_add = (Button) findViewById(R.id.btn_add);
		Button btn_back1 = (Button) findViewById(R.id.btn_back1);

		listView_alram.setOnItemLongClickListener(this);
		btn_back1.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		adapter_alarm.notifyDataSetChanged();
	}
	
	public void refreshList() {
		mydb.open();
		
//		for (int j = 1; j <= listdata.size(); j++) {
//			mydb.updateIndexNumOfAlarm(j);
//			Log.d("우헤헿", "i " + j+1 + " j " + j);
//		}
		listdata = mydb.get_Alarm_list();
		
		mydb.close();

		adapter_alarm = new Adapter_alarmlist(this, listdata);
		listView_alram.setAdapter(adapter_alarm);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back1:
			finish();
			break;
		case R.id.btn_add:
			if (listdata.size() < 1) {
				Intent myIntent = new Intent(this, SettingActivity.class);
				myIntent.putExtra("index", 0);
				startActivity(myIntent);
				break;
			} else {
				Intent myIntent = new Intent(this, SettingActivity.class);
				myIntent.putExtra("index", listdata.get(listdata.size() - 1)
						.getindex());
				startActivity(myIntent);
				break;
			}
		}
	}

	@Override
	public boolean onItemLongClick(final AdapterView<?> parent, View v,
			final int position, long arg3) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

		dialogBuilder.setTitle("알람삭제n");
		dialogBuilder.setMessage("알람을 삭제하시겠습니까?");
		dialogBuilder.setPositiveButton("네",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mydb.open();
						mydb.deleteRow(position, listdata);
						mydb.close();
						
						refreshList();
					}
				});
		dialogBuilder.setNegativeButton("아니오",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialogBuilder.show();

		return false;

		/*
		 * Dialog msgBox = new AlertDialog.Builder(this) .setTitle("알람 삭제")
		 * .setMessage("알람을 삭제하시겠습니까?") .setPositiveButton("네", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * mydb.deleteRow(Integer.toString(parent.getId()+1)); }
		 * }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * finish(); } }).create(); msgBox.show();
		 * 
		 * return false;
		 */

	}

}