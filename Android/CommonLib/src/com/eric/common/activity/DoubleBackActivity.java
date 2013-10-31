package com.eric.common.activity;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.eric.common.R;

public class DoubleBackActivity extends Activity {
	private boolean doubleBackToExitPressedOnce;

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast toast = Toast.makeText(this, R.string.str_twice_to_exit,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;

			}
		}, 2000);
	}
}
