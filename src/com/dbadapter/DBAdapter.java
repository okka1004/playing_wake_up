package com.dbadapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.type.Alarm_info;

public class DBAdapter {
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	private static String DB_PATH = "/data/data/com.example.alram2/databases/";
	private static String DB_NAME = "Mydb.sqlite";

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(this.context);

		try {
			DBHelper.createDatabase();
			DBHelper.openDataBase();
		} catch (IOException e) {
			Log.d("DBAdapter", e.toString());
		} catch (SQLException e) {
			Log.d("DBAdapter", e.toString());
		}
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		private SQLiteDatabase sqlite;
		private final Context crContext;

		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, 3);
			this.crContext = context;
		}

		public void createDatabase() throws IOException {
			if (!checkDatabase()) {
				this.getReadableDatabase();
				try {
					copyDatabase();
				} catch (IOException e) {
					throw new Error("Error copying database");
				}
			}
		}

		public boolean checkDatabase() {
			SQLiteDatabase checkDB = null;
			try {
				String myPath = DB_PATH + DB_NAME;
				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.NO_LOCALIZED_COLLATORS);

			} catch (SQLiteException e) {
				Log.d("checkDatabase", e.toString());
			}

			if (checkDB != null) {
				checkDB.close();
			}
			return checkDB != null ? true : false;
		}

		private void copyDatabase() throws IOException {
			InputStream myInput = crContext.getAssets().open(DB_NAME);

			String outFileName = DB_PATH + DB_NAME;
			OutputStream myOutput = new FileOutputStream(outFileName);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myOutput.close();
			myInput.close();
		}

		public void openDataBase() throws SQLException {
			String myPath = DB_PATH + DB_NAME;
			sqlite = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			Log.e("tag", "DB오픈");
		}

		@Override
		public synchronized void close() {
			if (sqlite != null)
				sqlite.close();
			super.close();
			Log.e("tag", "DB클로즈");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	// 스케줄 추가
	public void insert_alarmset(Alarm_info alarm) {
		ContentValues values = new ContentValues();
		values.put("onoff", alarm.getonoff());
		values.put("noon", alarm.getNoon());
		values.put("hour", alarm.getHour());
		values.put("minute", alarm.getMinute());
		values.put("repeat", alarm.getRepeat());
		values.put("snooze", alarm.getSnooze());
		values.put("phone", alarm.getPhone());
		values.put("message", alarm.getMessage());
		values.put("music", alarm.getMusicpath());
		values.put("days", alarm.getDaytostr());
		values.put("vibrate", alarm.getVibrate());
		values.put("gameonoff", alarm.getGameonoff());
		values.put("gametimeStr", alarm.getGametimeStr());
		values.put("num", alarm.getindex());

		db.insert("alarmlist", null, values);
	}

	public void update_onoff(int rowid, int order) {
		String sql;
		Log.e("dd", rowid + "");
		if (order == 0)
			sql = "UPDATE alarmlist SET onoff = '1' WHERE onoff = '0' and num = '"
					+ rowid + "'";
		else
			sql = "UPDATE alarmlist SET onoff = '0' WHERE onoff = '1' and num = '"
					+ rowid + "'";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		c.close();

	}

	public void updateIndexNumOfAlarm(int newIndex) {
		
		String query = "update alarmlist set num='" + newIndex + "'";
		Log.d("", query);
		Cursor res = db.rawQuery(query, null);
		res.moveToFirst();
		res.close();

	}

	public ArrayList<Alarm_info> get_Alarm_list() {
		Log.e("tag", "get alarm list");
		ArrayList<Alarm_info> alarmlist = new ArrayList<Alarm_info>();
		Alarm_info alarm_node;

		alarm_node = new Alarm_info();
		Cursor cursor = db.rawQuery("SELECT * FROM alarmlist", null);

		Log.e("tag", "i have data");
		while (cursor.moveToNext()) {
			Log.e("tag", "i have data2");
			alarm_node = new Alarm_info(cursor.getInt(cursor
					.getColumnIndex("onoff")), cursor.getString(cursor
					.getColumnIndex("noon")), cursor.getInt(cursor
					.getColumnIndex("hour")), cursor.getInt(cursor
					.getColumnIndex("minute")), cursor.getString(cursor
					.getColumnIndex("days")), cursor.getInt(cursor
					.getColumnIndex("repeat")), cursor.getInt(cursor
					.getColumnIndex("snooze")), cursor.getString(cursor
					.getColumnIndex("phone")), cursor.getString(cursor
					.getColumnIndex("message")), cursor.getString(cursor
					.getColumnIndex("music")), cursor.getInt(cursor
					.getColumnIndex("vibrate")), cursor.getInt(cursor
					.getColumnIndex("gameonoff")), cursor.getInt(cursor
					.getColumnIndex("gametimeStr")), cursor.getInt(cursor
					.getColumnIndex("num")));
			Log.e("tag", "i have data3");
			Log.d("aasdfasdf", "alarm get comple" + alarmlist.size());
			alarmlist.add(alarm_node);
		}
		cursor.close();
		Log.d("aasdfasdf", "alarm get comple");
		return alarmlist;
	}

	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}
	
	public void deleteRow(int position, ArrayList<Alarm_info> registeredAlarmList) {
		int deletedRowId = position + 1;
		String query = "DELETE FROM alarmlist WHERE num='"
				+ deletedRowId + "'";
		
		db.execSQL(query);
		registeredAlarmList.remove(position);
		
		for (int i = 1; i <= registeredAlarmList.size(); i++) {
			query = "UPDATE alarmlist SET num=" + i + " WHERE num=" 
										+ registeredAlarmList.get(i-1).getindex();
			
			Cursor c = db.rawQuery(query, null);
			c.moveToFirst();
			c.close();
		}
	}
}