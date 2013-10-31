package com.eric.common.activity.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ThanksDialog {

	public static void popupDialog(Context ctx, String title, String content) {
		new AlertDialog.Builder(ctx)
				.setTitle(title)
				.setMessage(content)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).show();
	}
}
