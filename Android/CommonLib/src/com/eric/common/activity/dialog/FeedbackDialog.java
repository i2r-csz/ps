package com.eric.common.activity.dialog;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eric.common.R;
import com.eric.common.constants.CommonConstants;
import com.eric.common.utils.CommonUtils;

public class FeedbackDialog {

	public static void popupFeedbackDialog(final Context ctx) {
		LinearLayout layout = new LinearLayout(ctx);
		layout.setOrientation(LinearLayout.VERTICAL);

		final EditText contentEt = new EditText(ctx);
		contentEt.setLines(5);
		layout.addView(contentEt);

		final EditText emailEt = new EditText(ctx);
		emailEt.setLines(1);
		emailEt.setHint(R.string.email);
		layout.addView(emailEt);

		final AQuery aq = new AQuery(ctx);
		new AlertDialog.Builder(ctx)
				.setTitle(R.string.pref_feedback)
				.setView(layout)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String feedback = "";
								if (contentEt.getText() != null) {
									feedback = contentEt.getText().toString();
								}
								String email = "";
								if (emailEt.getText() != null) {
									email = emailEt.getText().toString();
								}

								if (!"".equals(feedback) && !"".equals(email)) {
									Toast toast = Toast.makeText(
											ctx,
											ctx.getText(R.string.str_thanks_for_feedback),
											Toast.LENGTH_SHORT);
									toast.setGravity(Gravity.CENTER, 0, 0);
									toast.show();

									PackageInfo info = CommonUtils
											.getPackageInfo(ctx);
									String packageName = info.packageName;
									Map<String, Object> params = new HashMap<String, Object>();
									params.put(
											CommonConstants.PARAM_PACKAGE_NAME,
											packageName);
									params.put(
											CommonConstants.PARAM_FEED_BACK_CONTENT,
											feedback);
									params.put(
											CommonConstants.PARAM_FEED_BACK_EMAIL,
											email);
									params.put(
											CommonConstants.PARAM_FEED_BACK_DEVICE_NAME,
											CommonUtils.getDeviceName());
									params.put(
											CommonConstants.PARAM_FEED_BACK_ANDROID_VERION,
											CommonUtils.getAndroidVersion());

									aq.ajax(CommonConstants.URL_BASE
											+ CommonConstants.PATH_FEEDBACK,
											params, String.class,
											new AjaxCallback<String>() {
												@Override
												public void callback(
														String url, String ret,
														AjaxStatus status) {
													Log.i(FeedbackDialog.class
															.getName(), ret);
												}
											});

								} else {
									Toast toast = Toast.makeText(
											ctx,
											ctx.getText(R.string.str_plz_fill_all),
											Toast.LENGTH_SHORT);
									toast.setGravity(Gravity.CENTER, 0, 0);
									toast.show();
								}
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).show();
	}
}
