package com.example.alram2;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dbadapter.DBAdapter;
import com.example.listview.ListActivity;
import com.type.Alarm_info;

public class MainActivity extends Activity implements OnClickListener {

	DBAdapter mydb;
	
	private TextView textView_date;
	private TextView textView_time;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private int mNoon;
	private String noon;
	
	private int setHour;
	private int setMinute;
	private int snooze;
	private int vibrate;
	private int Ignored;
	private String setnoon;
	private String msgMain;
	private String bellPath;
	private NotificationManager Noti;

	ArrayList<Alarm_info> list_data;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView_date = (TextView) findViewById(R.id.textView_date);
		textView_time = (TextView) findViewById(R.id.textView_time);
		
		final Calendar c = Calendar.getInstance();
		
		Noti = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mNoon = c.get(Calendar.AM_PM);
		if (mNoon == 0) {
			noon = "오전";
		} else {
			noon = "오후";
			mHour -= 12;
		}
		mMinute = c.get(Calendar.MINUTE);

		updateDisplay();

		Button btn_setting = (Button) findViewById(R.id.btn_setting);
		btn_setting.setOnClickListener(this);

		File musicDirectory = new File("/sdcard/alarm2");
		if (!musicDirectory.exists()) {
			musicDirectory.mkdir();
		}

	}
	private void updateDisplay() {
		textView_date.setText(String.format("%d년 %d월 %d일", mYear, mMonth + 1, mDay));
		textView_time.setText(String.format("%s %d시 %d분", noon, mHour, mMinute));
	}
	@Override
	public void onClick(View v) {

		Intent myIntent = new Intent(this, ListActivity.class);
		startActivity(myIntent);
	}
	public void alarm_manager(String on_off) {
		// 알람 등록하기
		SharedPreferences prefs=getSharedPreferences("setting", MODE_PRIVATE);

		String onoff=prefs.getString("on_off", "");
		if (onoff=="on") {
			Log.i("alarm", "setAlarm");
			AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

			Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
			PendingIntent sender = PendingIntent.getBroadcast(
					MainActivity.this, 0, intent, 0);
			// requestCode에 숫자를 넣어주면 된다. 그리고 여러개 알람을 등록하려면 다른숫자로 지정해주면된다.

			Calendar calendar = Calendar.getInstance();

			// 알람시간 calendar에 set해주기
			
			mydb = new DBAdapter(getApplicationContext());
			mydb.open();
			list_data = mydb.get_Alarm_list();
			mydb.close();
			for(Alarm_info temp : list_data)
			{
			
			calendar.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
					temp.getHour(), temp.getMinute());
			calendar.set(Calendar.SECOND, 0);

			// 알람 예약
			am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);

			Toast.makeText(
					MainActivity.this,
					"시간설정:"
							+ Integer.toString(calendar
									.get(calendar.HOUR_OF_DAY)) + ":"
							+ Integer.toString(calendar.get(calendar.MINUTE)),
					Toast.LENGTH_LONG).show();

			am.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, sender);
			}
		}
		// 알람 삭제하기
		else if (onoff=="off") {

			Toast.makeText(MainActivity.this, "알람중지", Toast.LENGTH_LONG).show();

			AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

			Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
			PendingIntent sender = PendingIntent.getBroadcast(
					MainActivity.this, 0, intent, 0);
			// requestCode에 지울 해당 requestCode를 지정해주면 된다.
			am.cancel(sender);
		}
	}
	public class AlarmReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			pendingIntent();
		}
	}
	//알람의 설정 시각에 발생하는 인텐트 작성
    private PendingIntent pendingIntent() {
    	Intent i = new Intent(getApplicationContext(), MainActivity.class);
    	PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
    	
    	int icon = R.drawable.icon;
    	String tickerText = "알람테스트";
    	long when = System.currentTimeMillis();
    	
    	Notification notifi = new Notification(icon, tickerText, when);
    	notifi.sound = Uri.parse(bellPath);
    	//notifi.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
    	notifi.vibrate = new long[]{1000};
    	notifi.setLatestEventInfo(MainActivity.this, "AlarmTest", "제발 울려라!!", pi);
    	Noti.notify(1234, notifi);
    	
    	return pi;
    }
}