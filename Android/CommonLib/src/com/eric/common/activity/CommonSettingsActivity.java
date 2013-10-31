package com.eric.common.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.util.Log;
import android.view.MenuItem;

import com.eric.common.R;
import com.eric.common.activity.dialog.AboutDialog;
import com.eric.common.activity.dialog.FeedbackDialog;
import com.eric.common.activity.dialog.ShareDialog;
import com.eric.common.activity.dialog.ThanksDialog;
import com.eric.common.cf.CommonCfManager;
import com.eric.common.constants.CommonConstants;
import com.eric.common.payment.IabHelper;
import com.eric.common.payment.IabResult;
import com.eric.common.payment.Inventory;
import com.eric.common.payment.Purchase;
import com.eric.common.utils.CommonUtils;

@SuppressWarnings("deprecation")
public class CommonSettingsActivity extends PreferenceActivity implements
		IabHelper.OnIabPurchaseFinishedListener,
		IabHelper.OnIabSetupFinishedListener,
		IabHelper.QueryInventoryFinishedListener {
	private static String TAG = "CommonSettingsActivity";

	/************************************************************************************
	 * Payment.
	 * 
	 *************************************************************************************/
	private IabHelper mHelper;
	private String base64EncodedPublicKey;
	private String sku;

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

		base64EncodedPublicKey = getText(
				R.string.str_base_64_encoded_public_key).toString();
		if (!"".equals(base64EncodedPublicKey)) {
			sku = getText(R.string.sku_adv).toString();

			mHelper = new IabHelper(this, base64EncodedPublicKey);
			mHelper.enableDebugLogging(true, TAG);

			mHelper.startSetup(this);
		}

		setupRecommendPref();
		setupAboutPref();
		setupFeedbackPref();
		setupSharePref();

		setupPayPref();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		}
		return true;
	}

	private void setupRecommendPref() {
		Preference recommendPref = (Preference) findPreference(CommonConstants.PREF_RECOMMEND);
		if (recommendPref != null) {
			String isForSS = getText(R.string.is_for_samsung).toString();
			if (!"".equals(isForSS)) {
				PreferenceGroup prefGroup = (PreferenceGroup) findPreference(CommonConstants.KEY_COMMON);
				prefGroup.removePreference(recommendPref);
			} else {
				int groudIp = getResources().getInteger(R.integer.group_id);
				if (groudIp == 0) {
					getPreferenceScreen().removePreference(recommendPref);
				}
				recommendPref
						.setOnPreferenceClickListener(new OnPreferenceClickListener() {
							@Override
							public boolean onPreferenceClick(
									Preference preference) {
								Intent intent = new Intent(
										CommonSettingsActivity.this,
										GroupAppsActivity.class);
								startActivity(intent);
								return true;
							}
						});
			}
		}

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
		if (mHelper != null) {
			mHelper.dispose();
		}
		mHelper = null;
	}

	@Override
	protected void onResume() {
		super.onResume();

		setTitle(R.string.app_name);
	}

	private void setupPayPref() {
		Preference payPref = (Preference) findPreference(CommonConstants.PREF_PAY);

		String isForSS = getText(R.string.is_for_samsung).toString();
		if (!"".equals(isForSS)) {
			PreferenceGroup prefGroup = (PreferenceGroup) findPreference(CommonConstants.KEY_COMMON);
			prefGroup.removePreference(payPref);
			return;
		}

		boolean isPaidUser = CommonCfManager.getInstantce().isPaidUser();
		if (payPref != null) {
			if (!isPaidUser) {
				if (mHelper != null) {
					payPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
						@Override
						public boolean onPreferenceClick(Preference preference) {
							Log.d(TAG, "Pay onPreferenceClick");

							try {
								mHelper.launchPurchaseFlow(
										CommonSettingsActivity.this, sku, 1,
										CommonSettingsActivity.this, "");
							} catch (Exception e) {
								Log.d(TAG,
										"Pay onPreferenceClick "
												+ e.getStackTrace());
							}

							return true;
						}
					});
				}
			} else {
				payPref.setTitle(R.string.paid_user);
				payPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						AboutDialog
								.popupAboutDialog(CommonSettingsActivity.this);
						return true;
					}
				});
			}
		}
	}

	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase info) {
		Log.d(TAG, "onIabPurchaseFinished Start");

		if (result.isSuccess()) {
			if (info.getSku().equals(sku)) {
				CommonCfManager.getInstantce().setPaidUser(true);

				String title = getText(R.string.str_thanks).toString();
				String content = getText(R.string.str_restart_app).toString();
				ThanksDialog.popupDialog(CommonSettingsActivity.this, title,
						content);
			}
		} else {
			Log.d(TAG, "Error purchasing: " + result);
		}

		Log.d(TAG, "onIabPurchaseFinished End");
	}

	@Override
	public void onIabSetupFinished(IabResult result) {
		Log.d(TAG, "onIabSetupFinished Start");

		if (result.isSuccess()) {

			// Query purchased items.
			mHelper.queryInventoryAsync(this);

		} else {
			Log.d(TAG, "Problem setting up In-app Billing: " + result);
		}

		Log.d(TAG, "onIabSetupFinished End");
	}

	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inv) {
		Log.d(TAG, "onQueryInventoryFinished Start");

		if (result.isSuccess()) {
			boolean isPaidUser = inv.hasPurchase(sku);
			CommonCfManager.getInstantce().setPaidUser(isPaidUser);

		} else {
			Log.d(TAG, "Problem onQueryInventoryFinished: " + result);
		}

		Log.d(TAG, "onQueryInventoryFinished End");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);

		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}

		Log.d(TAG, "onActivityResult End");
	}
}
