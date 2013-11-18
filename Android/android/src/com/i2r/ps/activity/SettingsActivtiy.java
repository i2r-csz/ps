package com.i2r.ps.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.i2r.ps.R;
import com.i2r.ps.activity.dialog.AboutDialog;
import com.i2r.ps.activity.dialog.FeedbackDialog;
import com.i2r.ps.activity.dialog.LoginDialog;
import com.i2r.ps.activity.dialog.ShareDialog;
import com.i2r.ps.model.User;
import com.i2r.ps.util.CfManager;
import com.i2r.ps.util.Constants;

@SuppressWarnings("deprecation")
public class SettingsActivtiy extends PreferenceActivity {
	private String TAG = "SettingsActivtiy";
	
	String get_requests_url=Constants.URL_BASE+Constants.PATH_GET_REQUESTS;
	private AQuery aq;
	//public JSONArray endorsing_req_list;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.activity_settings);
		aq = new AQuery(this);
		
		//endorsing_req_list=new JSONArray();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
				&& getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		setupLoginPref();

		setupAboutPref();
		setupFeedbackPref();
		setupSharePref();
		view_Endorsement_Requests();
		view_Endorsing_Requests();
		
		//Log.d(TAG, "req_endorsers= " + endorsing_req_list.length());
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

	
	private void view_Endorsement_Requests(){
		Preference EndorsementRequestPref = (Preference) findPreference(Constants.PREF_ENDORSEMENT_REQUESTS);
		EndorsementRequestPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(SettingsActivtiy.this,
						RequestsActivity.class);
				//User user = adapter.getItem(position - 1);
				intent.putExtra(Constants.REQUEST_TYPE, Constants.ENDORSEMENT_REQUESTS);
				startActivity(intent);
				return true;
			}
		});
	}
	
	private void view_Endorsing_Requests(){
		Preference Endorsing_RequestsPref = (Preference) findPreference(Constants.PREF_ENDORSING_REQUESTS);
		
		//asyncJson_GetEndorsingRequests();

		Endorsing_RequestsPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(SettingsActivtiy.this,
						RequestsActivity.class);
				//User user = adapter.getItem(position - 1);
				intent.putExtra(Constants.REQUEST_TYPE, Constants.ENDORSRING_REQUESTS);
				startActivity(intent);
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
