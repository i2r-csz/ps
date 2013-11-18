package com.i2r.ps.activity.dialog;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eric.common.constants.CommonConstants;
import com.eric.common.utils.CommonUtils;
import com.i2r.ps.R;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.Utils;

public class FeedbackDialog {

	public static void popupFeedbackDialog(final Context ctx) {
		LinearLayout layout = new LinearLayout(ctx);
		layout.setOrientation(LinearLayout.VERTICAL);

		final EditText contentEt = new EditText(ctx);
		contentEt.setLines(5);
		layout.addView(contentEt);

		final EditText emailEt = new EditText(ctx);
		emailEt.setLines(1);
		emailEt.setHint(R.string.optional_email);
		layout.addView(emailEt);

		final AQuery aq = new AQuery(ctx);
		new AlertDialog.Builder(ctx)
				.setTitle(R.string.feed_back)
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

								if (!"".equals(feedback)) {
									Toast.makeText(
											ctx,
											ctx.getText(R.string.thanks_for_feedback),
											Toast.LENGTH_SHORT).show();

									PackageInfo info = Utils.getInstantce()
											.getPackageInfo();
									String packageName = info.packageName;
									Map<String, Object> params = new HashMap<String, Object>();
									params.put(Constants.PARAM_PACKAGE_NAME,
											packageName);
									params.put(
											Constants.PARAM_FEED_BACK_CONTENT,
											feedback);
									params.put(Constants.PARAM_FEED_BACK_EMAIL,
											email);

									params.put(
											Constants.PARAM_FEED_BACK_DEVICE_NAME,
											CommonUtils.getDeviceName());
									params.put(
											Constants.PARAM_FEED_BACK_ANDROID_VERION,
											CommonUtils.getAndroidVersion());

									aq.ajax(Constants.FEEDBACK_URL
											+ Constants.PATH_FEEDBACK, params,
											String.class,
											new AjaxCallback<String>() {
												@Override
												public void callback(
														String url, String ret,
														AjaxStatus status) {
													Log.i(FeedbackDialog.class
															.getName(), ret);
												}
											});

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
