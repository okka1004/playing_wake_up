package com.example.alram2;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.listview.ListActivity;

class Mp3Filter implements FilenameFilter {
	@Override
	public boolean accept(File dir, String name) {
		Log.d("", name);
		return (name.endsWith(".mp3"));
	}
}

public class MusicActivity extends ListActivity implements OnItemClickListener {


	
	
	private static final String MEDIA_PATH = new String("/sdcard/OllehMusic/DRM/");
	private static ArrayList<String> songs = new ArrayList<String>();
	private MediaPlayer mp = new MediaPlayer();
	private int currentPosition = 0;
	ListView listMusic;
	String bell;
	String bellPath;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);

		listMusic = (ListView) findViewById(R.id.listView_music);
		listMusic.setAdapter(updateSongList());

		if (songs.isEmpty()) {
			Dialog msgBox = new AlertDialog.Builder(this)
					.setTitle("음악 파일이 없습니다!")
					.setMessage("/sdcard/alarm2 밑에 음악 파일을 넣어주세요.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).create();
			msgBox.show();
		}
		
		listMusic.setOnItemClickListener(this);
	}

	public ArrayAdapter<String> updateSongList() {
		File home = new File(MEDIA_PATH);

		for (File file : home.listFiles(new Mp3Filter())) {
			songs.add(file.getName());
			Log.d("adfdfs", file.getName());
		}
		ArrayAdapter<String> songList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, songs);

		return songList;

	}

	public void onListItemClick(ListView i, View v, int position, long id) {
		currentPosition = position;
		playSong(MEDIA_PATH + songs.get(position));
	}

	private void playSong(String songPath) {
		try {
			mp.reset();
			mp.setDataSource(songPath);
			mp.prepare();
			mp.start();

			mp.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer arg0) {
					nextSongs();
				}
			});
		} catch (Exception e) {
			Log.v(getString(R.string.app_name), e.getMessage());
		}
	}

	private void nextSongs() {
		if (++currentPosition >= songs.size()) {
			currentPosition = 0;
		} else {
			playSong(MEDIA_PATH + songs.get(currentPosition));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ListView listMusic=(ListView)parent;
		
		bell=(String)listMusic.getItemAtPosition(position);
		Toast.makeText(MusicActivity.this, bell, Toast.LENGTH_LONG).show();
		bellPath=MEDIA_PATH+bell;

		SharedPreferences prefs=getSharedPreferences("setting", MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("musicpath", bellPath);
		editor.commit();

		finish();
	}
	public void onNothingSelected(AdapterView<?> parent){
		
	}
}