package com.example.game;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.alram2.MainActivity;
import com.example.alram2.R;

public class Asynctask extends Activity {
	private Imageview m_Imageview_Maker;

	private int mCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_Imageview_Maker = new Imageview(this);
		m_Imageview_Maker.setBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.rocket));
		setContentView(m_Imageview_Maker);

		mCount = 0;
		new ExampleAsyncTask().execute("");
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			Log.d("TEST", "touch");
			mCount++;
		}
		return super.dispatchTouchEvent(event);
	}

	class Imageview extends View {
		public Bitmap m_bitmap = null;
		public Bitmap background_bitmap = null;
		public float b_x = 0;
		public float b_y = 0;
		public float m_fX = 0;
		public float m_fY = 0;

		public Imageview(Context context) {
			super(context);
		}

		public void setBitmap(Bitmap bitmap) {
			m_bitmap = bitmap;
			m_fX = (getWindowManager().getDefaultDisplay().getWidth() - m_bitmap
					.getWidth()) / 2;
			m_fY = getWindowManager().getDefaultDisplay().getHeight();
			new BitmapFactory();
			background_bitmap = BitmapFactory.decodeResource(
					getResources(), R.drawable.background);
			b_x = getWidth();
			b_y = getHeight();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(background_bitmap, b_x, b_y, null);
			canvas.drawBitmap(m_bitmap, m_fX, m_fY, null);
			
			super.onDraw(canvas);
		}

		public void MoveImage() {
			if (m_fY <= -(m_bitmap.getHeight())) {
				m_fY = getWindowManager().getDefaultDisplay().getHeight();
			} else {
				m_fY = m_fY - (60);
			}
			postInvalidate();
		}
	}

	public class ExampleAsyncTask extends AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected void onProgressUpdate(Integer... progress) {

		}

		@Override
		protected void onPostExecute(Long result) {

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		public Long doInBackground(String... params) {

			while (true) {
				if (mCount == 10) {
					m_Imageview_Maker.MoveImage();
					Log.e("test", "fx = "+m_Imageview_Maker.m_fX+"fy = " + m_Imageview_Maker.m_fY);
					mCount = 0;

				}
				if (m_Imageview_Maker.m_fY < 0) {
					Context context = getApplicationContext();
					Intent intent = new Intent(context, MainActivity.class);
					startActivity(intent);
					break;
				}
			}
			return null;
		}
	}
}