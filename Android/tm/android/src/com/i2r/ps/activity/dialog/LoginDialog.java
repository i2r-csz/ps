package com.i2r.ps.activity.dialog;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.i2r.ps.R;
import com.i2r.ps.activity.SettingsActivtiy;
import com.i2r.ps.util.CfManager;
import com.i2r.ps.util.Constants;

public class LoginDialog {

	public static void popupDialog(final SettingsActivtiy activity) {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		LayoutInflater inflater = activity.getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_login, null);

		builder.setView(view)
				// Add action buttons
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								EditText userEmailEt = (EditText) view
										.findViewById(R.id.user_email_et);

								EditText userPsdEt = (EditText) view
										.findViewById(R.id.user_psd_et);

								String userEmail = "";
								String userPsd = "";
								if (userEmailEt.getText() != null) {
									userEmail = userEmailEt.getText()
											.toString();
								}
								if (userPsdEt.getText() != null) {
									userPsd = userPsdEt.getText().toString();
								}

								if (!"".equals(userEmail)
										&& !"".equals(userPsd)) {
									login(userEmail, userPsd, activity, dialog);
								}
							}

						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		builder.create().show();
	}

	private static void login(final String userEmail, String userPsd,
			final SettingsActivtiy activity, final DialogInterface dialog) {
		AQuery aq = new AQuery(activity);

		String url = Constants.URL_BASE + Constants.PATH_LOGIN;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.PARAM_USER_EMAIL, userEmail);
		params.put(Constants.PARAM_USER_PSD, userPsd);

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject ret, AjaxStatus status) {
				if (ret != null && status.getCode() == 200) {
					int uid = -1;
					try {
						uid = ret.getInt("uid");
					} catch (Exception e) {
					}
					if (uid > 0) {
						Toast.makeText(activity, R.string.str_login_success,
								Toast.LENGTH_SHORT).show();

						CfManager.getInstantce().setUid(uid);
						CfManager.getInstantce().setUserEmail(userEmail);

						activity.updateUser();
						dialog.dismiss();
					} else {
						Toast.makeText(activity, R.string.error_not_match,
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(activity, R.string.error_login,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
