package com.eric.common.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import com.eric.common.R;
import com.eric.common.activity.dialog.AboutDialog;
import com.eric.common.activity.dialog.FeedbackDialog;
import com.eric.common.activity.dialog.ShareDialog;
import com.eric.common.constants.CommonConstants;
import com.eric.common.utils.CommonUtils;

@SuppressWarnings("deprecation")
public class CommonSettingsActivity extends PreferenceActivity {

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.activity_settings);

		CommonUtils.init(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
				&& getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		setupAboutPref();
		setupFeedbackPref();
		setupSharePref();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		}
		return true;
	}

	private void setupAboutPref() {
		Preference sharePref = (Preference) findPreference(CommonConstants.PREF_ABOUT);
		sharePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AboutDialog.popupAboutDialog(CommonSettingsActivity.this);
				return true;
			}
		});
	}

	private void setupFeedbackPref() {
		Preference feedPref = (Preference) findPreference(CommonConstants.PREF_FEEDBACK);
		feedPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				FeedbackDialog.popupFeedbackDialog(CommonSettingsActivity.this);
				return true;
			}
		});
	}

	private void setupSharePref() {
		Preference sharePref = (Preference) findPreference(CommonConstants.PREF_SHARE);
		sharePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				ShareDialog.popupShareDialog(CommonSettingsActivity.this);
				return true;
			}

		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

		setTitle(R.string.app_name);
	}

}
