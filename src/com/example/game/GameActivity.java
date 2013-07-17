package com.example.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.alram2.R;
import com.type.Alarm_info;

public class GameActivity extends Activity implements OnClickListener{

	private Button btn_gameback;
	private Button btn_gamesave;
	private ToggleButton toggleBtn_game;
	private Button btn_gametime;
	
	private Alarm_info alarm_info;
	private int onoff;
	private String gametime;
	private int gametimeStr;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		alarm_info = new Alarm_info();
		
		btn_gameback = (Button) findViewById(R.id.btn_gameback);
		btn_gamesave = (Button) findViewById(R.id.btn_gamesave);
		toggleBtn_game = (ToggleButton) findViewById(R.id.toggleBtn_game);
		btn_gametime = (Button) findViewById(R.id.btn_gametime);
		
		btn_gameback.setOnClickListener(this);
		btn_gamesave.setOnClickListener(this);
		btn_gametime.setOnClickListener(this);
		toggleBtn_game.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_gameback:
			finish();
			break;
		case R.id.btn_gamesave:
			alarm_info.setGameonoff(onoff);
			alarm_info.setSnooze(gametimeStr);
			finish();
			break;
		case R.id.toggleBtn_game:
			if(toggleBtn_game.isChecked())
			{
				btn_gametime.setEnabled(true);
				onoff=1;
			}
			else{
				btn_gametime.setEnabled(false);
				onoff=0;
			}
			break;
			
		case R.id.btn_gametime:
			showDialog();
			break;
		}
	}

	private void showDialog() {
		final String items[] = {"5초", "10초", "15초", "20초", "30초"};

		AlertDialog.Builder ab = new AlertDialog.Builder(
				GameActivity.this);
		ab.setTitle("게임시간설정");
		
		int selected = -1;
		ab.setSingleChoiceItems(items, selected,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						gametime = items[which];
					}
				})
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								gametimeStr = Integer.parseInt(gametime
										.substring(0, 1));

								updateDisplay();
								
					
							}

							private void updateDisplay() {
								// TODO Auto-generated method stub
//								btn_gametime.setText(getText.toString.));
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
