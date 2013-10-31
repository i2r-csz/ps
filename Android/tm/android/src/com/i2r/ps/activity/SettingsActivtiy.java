package com.i2r.ps.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

import com.i2r.ps.R;
import com.i2r.ps.activity.dialog.AboutDialog;
import com.i2r.ps.activity.dialog.FeedbackDialog;
import com.i2r.ps.activity.dialog.LoginDialog;
import com.i2r.ps.activity.dialog.ShareDialog;
import com.i2r.ps.util.CfManager;
import com.i2r.ps.util.Constants;

@SuppressWarnings("deprecation")
public class SettingsActivtiy extends PreferenceActivity {

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.activity_settings);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
				&& getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		setupLoginPref();

		setupAboutPref();
		setupFeedbackPref();
		setupSharePref();
	}

	private void setupLoginPref() {
		Preference loginPref = (Preference) findPreference(Constants.PREF_LOGIN);

		if (CfManager.getInstantce().getUid() > 0) {
			updateUser();
		} else {
			loginPref
					.setOnPreferenceClickListener(new OnPreferenceClickListener() {

						@Override
						public boolean onPreferenceClick(Preference preference) {
							if (CfManager.getInstantce().getUid() < 0) {
								LoginDialog.popupDialog(SettingsActivtiy.this);
							}
							return true;
						}
					});
		}

	}

	public void updateUser() {
		Preference loginPref = (Preference) findPreference(Constants.PREF_LOGIN);
		loginPref.setTitle(CfManager.getInstantce().getUserEmail());
	}

	private void setupAboutPref() {
		Preference sharePref = (Preference) findPreference(Constants.PREF_ABOUT);
		sharePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AboutDialog.popupAboutDialog(SettingsActivtiy.this);
				return true;
			}
		});
	}

	private void setupFeedbackPref() {
		Preference feedPref = (Preference) findPreference(Constants.PREF_FEEDBACK);
		feedPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				FeedbackDialog.popupFeedbackDialog(SettingsActivtiy.this);
				return true;
			}
		});
	}

	private void setupSharePref() {
		Preference sharePref = (Preference) findPreference(Constants.PREF_SHARE);
		sharePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				ShareDialog.popupAboutDialog(SettingsActivtiy.this);
				return true;
			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitle(R.string.title_settings);
	}

}
