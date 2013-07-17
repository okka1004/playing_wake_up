package com.example.listview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dbadapter.DBAdapter;
import com.example.alram2.R;
import com.type.Alarm_info;

public class Adapter_alarmlist extends ArrayAdapter<Alarm_info> implements OnLongClickListener{

	private CheckBox chk_alarm[];
	private TextView itemTime;
	private TextView itemDays;
	private Button btnDelete;
	private Context mContext;
	ArrayList<Alarm_info> listdata;
	Alarm_info t_data;

	public Adapter_alarmlist(Context context, ArrayList<Alarm_info> listdata) {
		super(context, 0, listdata);
		this.listdata = listdata;
		mContext=context;
		chk_alarm = new CheckBox[listdata.size()];
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.listitem, null);
		}
		
		t_data = getItem(position);
		chk_alarm[position] = (CheckBox) v.findViewById(R.id.chk_alarm);
		chk_alarm[position].setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					DBAdapter mydb=new DBAdapter(mContext);
					mydb.open();
					mydb.update_onoff(listdata.get(position).getindex(), 0);
					mydb.close();
					Toast.makeText(mContext, "On", 1).show();
				}
				else{
					DBAdapter mydb=new DBAdapter(mContext);
					mydb.open();
					mydb.update_onoff(listdata.get(position).getindex(), 1);
					mydb.close();
					Toast.makeText(mContext, "Off", 1).show();
				}
			}
		});
		itemTime = (TextView) v.findViewById(R.id.itemTime);
		itemDays = (TextView) v.findViewById(R.id.itemDays);

		itemTime.setText(t_data.getNoon() + " " + t_data.getHour() + " : "
				+ t_data.getMinute());
		itemDays.setText(t_data.getDaytostr());
		v.setOnLongClickListener(this);

		return v;
	}

	@Override
	public boolean onLongClick(View arg0) {

		
		return false;
	}

}
