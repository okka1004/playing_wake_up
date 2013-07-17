package com.example.alram2;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dbadapter.DBAdapter;
import com.example.game.GameActivity;
import com.type.Alarm_info;

public class SettingActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	static final int TIME_DIALOG_ID = 0;
	static final int CHECK_DIALOG_ID = 1;
	static final int SELECT_SNOOZE_ID = 2;
	static final int SELECT_IGNORED_ID = 3;


	private int selectedIgnored;
	private int ignoredStr;
	private Button btn_ignored;
	private Button btn_settingBack;
	private Button btn_settime;
	private Button btn_repeat;
	private Button btn_music;
	private Button btn_message;
	private Button btn_settingSave;
	private CheckBox checkbox;
	private Button btn_snooze;
	private int snoozeStr;
	private Vibrator vibrator;
	private Button btn_game;
	
	private int mHour;
	private int mMinute;
	private int mNoon;
	private String noon;
	private Alarm_info alarm_info;

	private int vibrate;
	private int index=0;

	ArrayList<String> SelectedDays;
	int selectedsnooze;
	DBAdapter mydb;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		alarm_info = new Alarm_info();

		btn_ignored = (Button) findViewById(R.id.btn_ignored);
		btn_settingBack = (Button) findViewById(R.id.btn_settingBack);
		btn_settingSave = (Button) findViewById(R.id.btn_settingSave);
		btn_settime = (Button) findViewById(R.id.btn_settime);
		btn_music = (Button) findViewById(R.id.btn_music);
		btn_repeat = (Button) findViewById(R.id.btn_repeat);
		btn_message = (Button) findViewById(R.id.btn_message);
		btn_snooze = (Button) findViewById(R.id.btn_snooze);
		btn_game=(Button)findViewById(R.id.btn_game);
		checkbox = (CheckBox) findViewById(R.id.chk_vibreate);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);



		btn_settingBack.setOnClickListener(this);
		btn_settingSave.setOnClickListener(this);
		btn_settime.setOnClickListener(this);
		btn_repeat.setOnClickListener(this);
		btn_music.setOnClickListener(this);
		btn_message.setOnClickListener(this);
		btn_snooze.setOnClickListener(this);
		checkbox.setOnCheckedChangeListener(this);
		btn_ignored.setOnClickListener(this);
		btn_game.setOnClickListener(this);

		index=getIntent().getIntExtra("index", 0);
		
		mydb = new DBAdapter(getApplicationContext());

		final Calendar c = Calendar.getInstance();

		mHour = c.get(Calendar.HOUR_OF_DAY);
		mNoon = c.get(Calendar.AM_PM);
		if (mNoon == 0) {
			alarm_info.setNoon("오전");
			noon = "오전";
		} else {
			alarm_info.setNoon("오후");
			noon = "오후";
			mHour -= 12;
		}

		mMinute = c.get(Calendar.MINUTE);



	}

	private void updateDisplay(String type) {

		if (type.equals("time"))
			btn_settime.setText(String.format("%s %d : %d", noon, mHour,
					mMinute));

		else if (type.equals("snooze")) {
			btn_snooze.setText(String.format("%s", selectedsnooze));
		} else if (type.equals("ignored")) {
			btn_ignored.setText(String.format("%s", selectedIgnored));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_settingBack:
			finish();
			break;
		case R.id.btn_settime:
			showDialog(TIME_DIALOG_ID);
			updateDisplay("time");
			break;
		case R.id.btn_repeat:
			showDialog(CHECK_DIALOG_ID);
			updateDisplay("days");
			break;
		case R.id.btn_music:
			Intent myIntent = new Intent(this, MusicActivity.class);
			startActivity(myIntent);
			break;
		case R.id.btn_message:
			Intent myIntent2 = new Intent(this, MessageActivity.class);
			startActivity(myIntent2);
			break;
		case R.id.btn_snooze:
			showDialog(SELECT_SNOOZE_ID);
			break;
		case R.id.btn_ignored:
			showDialog(SELECT_IGNORED_ID);
			updateDisplay("ignored");
			break;
		case R.id.btn_settingSave:
			SharedPreferences prefs = getSharedPreferences("setting",
					MODE_PRIVATE);
			alarm_info.setMusicpath(prefs.getString("musicpath", ""));
			alarm_info.setPhone(prefs.getString("phone", ""));
			alarm_info.setMessage(prefs.getString("message", ""));
			alarm_info.setVibrate(vibrate);
			alarm_info.setindex(++index);
			alarm_info.setRepeat(ignoredStr);
			alarm_info.setSnooze(snoozeStr);
			mydb.open();
			mydb.insert_alarmset(alarm_info);
			mydb.close();
			
			
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("message", "");
			editor.putString("musicpath", "");
			editor.putString("phone", "");
			editor.commit();
			
			finish();
			break;
		case R.id.btn_game:
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
			break;


		}
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourofday, int minute) {

			mHour = hourofday;
			mMinute = minute;

			alarm_info.setHour(mHour);
			alarm_info.setMinute(mMinute);

			updateDisplay("time");
		}
	};

	protected Dialog onCreateDialog(int id) {
		Log.e("e", "oncreatedialog");
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		case CHECK_DIALOG_ID:
			DialogSelectOption("days");
			break;
		case SELECT_SNOOZE_ID:
			DialogSelectOption("snooze");
			break;
		case SELECT_IGNORED_ID:
			DialogSelectOption("ignored");
			break;
		}
		return null;
	}

	private void DialogSelectOption(String type) {
		if (type.equals("days")) {
			Log.e("e", "확인3");
			final String items[] = { "월요일", "화요일", "수요일", "목요일", "금요일", "토요일",
					"일요일" };
			SelectedDays = new ArrayList<String>();

			AlertDialog.Builder ab = new AlertDialog.Builder(
					SettingActivity.this);
			ab.setTitle("알람반복");

			ab.setMultiChoiceItems(items, null,
					new DialogInterface.OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							if (isChecked) {
								SelectedDays.add(items[which]);
							} else if (SelectedDays.contains(items[which])) {
								SelectedDays.remove(SelectedDays
										.indexOf(items[which]));
							}
						}
					})
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String str = "";

									for (int i = 0; i < SelectedDays.size(); i++) {
										String dataOfCurrent = SelectedDays
												.get(i);
										SelectedDays.set(i, dataOfCurrent
												.substring(dataOfCurrent
														.indexOf("요일") - 1,
														dataOfCurrent
																.indexOf("요일")));
									}

									for (int j = 0; j < SelectedDays.size(); j++) {
										str += SelectedDays.get(j) + " ";
									}
									Log.d("TEXTTEXT", str);
									alarm_info.setDays(str);
									btn_repeat.setText(str);
									// Toast.makeText(this, str,
									// Toast.LENGTH_SHORT).show();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});

			AlertDialog aldialog = ab.create();
			aldialog.show();

			updateDisplay("time");
		} else if (type.equals("snooze")) {
			final String items[] = { "1분", "3분", "5분", "10분", "15분", "20분" };
			final int int_items[]={1, 3, 5, 10, 15, 20};
			AlertDialog.Builder ab = new AlertDialog.Builder(
					SettingActivity.this);
			ab.setTitle("알람미루기 간격");

			int selected = -1;
			
			ab.setSingleChoiceItems(items, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							selectedsnooze = int_items[which];
							updateDisplay("snooze");

						}

					})
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {


										snoozeStr = selectedsnooze;

										updateDisplay("snooze");


								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});

			AlertDialog aldialog = ab.create();
			aldialog.show();
		} else if (type.equals("ignored")) {
			final String items[] = { "1회", "2회", "3회", "5회", "10회" };
			final int int_items[]={1, 2, 3, 5, 10};
			AlertDialog.Builder ab = new AlertDialog.Builder(
					SettingActivity.this);
			ab.setTitle("알람무시가능횟수");

			int selected = -1;
			ab.setSingleChoiceItems(items, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							selectedIgnored = int_items[which];
							updateDisplay("ignored");

						}
					})
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									ignoredStr =selectedIgnored;
									
									
									updateDisplay("ignored");


								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});

			AlertDialog aldialog = ab.create();
			aldialog.show();
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

		if (buttonView.getId() == R.id.chk_vibreate) {
			if (isChecked) {
				vibrate = 1;
				Toast.makeText(getApplicationContext(), "Checked", 1).show();
				vibrator.vibrate(1000);

			} else {
				vibrate = 0;
				Toast.makeText(getApplicationContext(), "Not Checked", 0)
						.show();

			}

		}
	}

}