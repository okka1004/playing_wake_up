package com.type;

import java.util.ArrayList;

import android.util.Log;

public class Alarm_info {
	private int onoff=0;
	private String noon="";
	private int hour=0;
	private int minute=0;
	private ArrayList<String> days;
	private String daytostr="";
	private int repeat=0;
	private int snooze=0;
	private String phone="";
	private String message="";
	private String musicpath="";
	private int vibrate=0;
	private int gameonoff=0;
	private int gametimeStr=0;
	private int index=0;
	
	public Alarm_info(){}
	
	public Alarm_info(
	int onoff,
	String noon,
	int hour,
	int minute,
	String days,
	int repeat,
	int snooze,
	String phone,
	String message,
	String musicpath,
	int vibrate,
	int gameonoff,
	int gametimeStr,
	int index)
	{
		this.onoff=onoff;
		this.noon = noon;
		this.hour = hour;
		this.minute = minute;
		daytostr += days;
		Log.d("texxx", "asfasfasdf");
		this.days = new ArrayList<String>();
		Log.d("texxx", daytostr);
//		if(daytostr == null)
//		{
//			Log.d("texxxx", "asfasfasdf4");
//			this.days = null;
//		}
//		else
//		{
//			Log.d("texxx12", "zxczxczxc");
//			for(int i =0; i<days.length();i++)
//			this.days.add(days.charAt(i)+"");
//		}
		this.repeat = repeat;
		this.snooze = snooze;
		this.phone = phone;
		this.message = message;
		this.musicpath = musicpath;
		this.vibrate=vibrate;
		this.gameonoff=gameonoff;
		this.gametimeStr=gametimeStr;
		this.index=index;
	}
	public int getindex(){
		return index;
	}
	public void setindex(int index){
		this.index = index;
	}
	public int getonoff(){
		return onoff;
	}
	public void setonoff(int onoff){
		this.onoff = onoff;
	}
	public int getGametimeStr() {
		return gametimeStr;
	}
	public void setGametimeStr(int gametimeStr) {
		this.gametimeStr = gametimeStr;
	}
	public int getGameonoff() {
		return gameonoff;
	}
	public void setGameonoff(int gameonoff) {
		this.gameonoff = gameonoff;
	}
	public int getVibrate() {
		return vibrate;
	}
	public void setVibrate(int vibrate) {
		this.vibrate = vibrate;
	}
	public String getDaytostr() {
		return daytostr;
	}
	public void setDaytostr(String daytostr) {
		this.daytostr = daytostr;
	}
	public String getNoon() {
		return noon;
	}
	public void setNoon(String noon) {
		this.noon = noon;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public ArrayList<String> getDays() {
		return days;
	}
	public void setDays(String days) {
		daytostr = days;
		this.days = new ArrayList<String>();
		if(days.equals(""))
			this.days = null;
		else
		{
			for(int i =0; i<days.length();i++)
			this.days.add(days.charAt(i)+"");
		}
	}
	public int getRepeat() {
		return repeat;
	}
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	public int getSnooze() {
		return snooze;
	}
	public void setSnooze(int snooze) {
		this.snooze = snooze;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMusicpath() {
		return musicpath;
	}
	public void setMusicpath(String musicpath) {
		this.musicpath = musicpath;
	}
}