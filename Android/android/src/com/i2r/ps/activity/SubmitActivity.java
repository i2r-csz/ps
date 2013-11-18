package com.i2r.ps.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.i2r.ps.R;
import com.i2r.ps.model.Post;
import com.i2r.ps.util.CfManager;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.PostService;
import com.i2r.ps.util.Utils;

public class SubmitActivity extends FragmentActivity {
	private static final String TAG = "CameraActivity";

	private static final int CAMERA_PIC_REQUEST = 1001;
	private static final int GPS_ENABLE_REQUEST = 1002;

	private static Uri outputFileUri;

	private Bitmap image;

	private ImageButton postIb;

	private TextView descEt;
	private TextView severityV;
	private TextView tagEt;
	private EditText locationEt;
	private Spinner categorySpin;

	private LocationManager lm;
	private Location curLocation;

	private GoogleMap mMap;

	private AlertDialog aDialog;
	private ProgressDialog mDialog;

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			Log.i(TAG, "onLocationChanged Start :" + location.getLatitude()
					+ " " + location.getLongitude());

			curLocation = location;
			updateUserLocation();

			Log.i(TAG, "onLocationChanged End");
		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate Start");
		this.curLocation=new Location("user_loc");

		setContentView(R.layout.activity_submit);
		Utils.init(this);
		//Rotate phone will not restart this activity
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//
		postIb = (ImageButton) findViewById(R.id.post_ib);

		//
		descEt = (TextView) findViewById(R.id.descEt);
		tagEt = (TextView) findViewById(R.id.tagEt);
		categorySpin = (Spinner) findViewById(R.id.category_spin);
		locationEt = (EditText) findViewById(R.id.location_et);
		

		

		categorySpin.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				
				if (position == 1) {
					locationEt.setVisibility(View.VISIBLE);
					
				} else {
					locationEt.setVisibility(View.GONE);
				}
				
				update_category_selection();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});

		SeekBar seekBar = (SeekBar) findViewById(R.id.severitySb);
		severityV = (TextView) findViewById(R.id.severityV);
		seekBar.incrementProgressBy(1);
		seekBar.setProgress(Constants.SERVERITY_DEFAULT_LEVEL);
		severityV.setText(Constants.SERVERITY_DEFAULT_LEVEL + "");
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				severityV.setText(progress + "");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		//
		setUpMapIfNeeded();

		// default location
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				Constants.MAP_DEFAULT_LAT, Constants.MAP_DEFAULT_LNG),
				Constants.MAP_DEFAULT_LEVEL2));

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
		locationListener);
		}

		if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
		locationListener);
		// showGPSEnableDialog();
		}

		
		
		
		

		Log.i(TAG, "onCreate End");
	}

	private void clear() {
		descEt.setText("");
		
		Log.i(TAG, "clear Start");
		
		postIb.setImageDrawable(getResources().getDrawable(
				R.drawable.take_photo));
		/*
		if (image != null) {
			image.recycle();
		}
		*/
		
		Log.i(TAG, "clear End");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume Start");
		detectLocation();
		
		update_category_selection();


		if (CfManager.getInstantce().getUid() < 0) {
			Toast.makeText(this, R.string.error_login_first, Toast.LENGTH_SHORT)
					.show();
		}

		setTitle(R.string.title_submit);
		Log.i(TAG, "onResume End");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause Start");
		
		update_category_selection();
				
		lm.removeUpdates(locationListener);
		/*
		if (image != null) {
			image.recycle();
		}
		*/
		Log.i(TAG, "onPause End");
	}
	
	
	protected void onDestroy(){
		super.onDestroy();
		if (image != null) {
			image.recycle();
		}
		
	}

	private void setUpMapIfNeeded() {
		Log.i(TAG, "setUpMapIfNeeded Start");

		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}

		Log.i(TAG, "setUpMapIfNeeded End");
	}

	private void updateUserLocation() {
		Log.i(TAG, "updateUserLocation Start");

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				curLocation.getLatitude(), curLocation.getLongitude()),
				Constants.MAP_DEFAULT_LEVEL2));

		Log.i(TAG, "updateUserLocation End");
	}

	/**
	 * Call intent to retrieve photo.
	 * 
	 * @param v
	 */
	public void takePhoto(View v) {
		Log.i(TAG, "takePhoto Start");
		
		/*
		if (image != null) {
			image.recycle();
		}
		*/

		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
		
		

		Log.i(TAG, "takePhoto End");
	}

	/**
	 * Callback from GSP settings or photo capturing.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult Start" + requestCode);

		update_category_selection();
		
		if (resultCode == RESULT_OK) {
			if (requestCode == CAMERA_PIC_REQUEST && data != null) {
				// image = Utils.getInstantce().readBitmap(outputFileUri);
				/*
				if (image != null) {
					image.recycle();
				}
				*/
				image = (Bitmap) data.getExtras().get("data");
				postIb.setImageBitmap(image);
			} else if (requestCode == GPS_ENABLE_REQUEST) {
				if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					detectLocation();
				}
			}
		}
		Log.i(TAG, "onActivityResult End" + requestCode);
	}

	/**
	 * Detect the location by GPS if enabled, otherwise by Network.
	 */
	private void detectLocation() {
		Log.i(TAG, "detectLocation Start");

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locationListener);

		Log.i(TAG, "detectLocation End");
	}

	/**
	 * Show GPS enable dialog.
	 */
	private void showGPSEnableDialog() {
		Log.i(TAG, "showGPSEnableDialog Start");

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(getText(R.string.gps_disabled))
				.setPositiveButton(getText(R.string.gps_go_settings),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(callGPSSettingIntent,
										GPS_ENABLE_REQUEST);
							}
						})
				.setNegativeButton(R.string.gps_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();

		Log.i(TAG, "showGPSEnableDialog End");
	}

	public void onSubmit(View v) {
		
		update_category_selection();
		
		if (CfManager.getInstantce().getUid() < 0) {
			Toast.makeText(this, R.string.error_login_first, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (aDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(R.string.validate_error));
			aDialog = builder.create();
			aDialog.setCancelable(true);
		}
		if (validate()) {
			// submit to server.
			Post post = new Post();
			post.setDescription(descEt.getEditableText().toString());
			post.setTag(tagEt.getEditableText().toString());
			post.setSeverity(1);
			post.setImage(image);
			post.setLat(curLocation.getLatitude());
			post.setLng(curLocation.getLongitude());
			post.setCategory(categorySpin.getSelectedItemPosition());
			post.setSubmitTime();
			Log.i(TAG,"category_id (onSubmit)="+categorySpin.getSelectedItemPosition());
			if (locationEt.getText() != null) {
				post.setAddress(locationEt.getText().toString());
			}
			
			Log.d("User_name","(onSubmit)"+post.getUsername());

			new SendPostTask().execute(new Post[] { post });
		} else {
			aDialog.show();
		}
	}

	private boolean validate() {
		boolean valid = true;
		// validate
		if (image == null) {
			aDialog.setMessage(getText(R.string.error_image_empty));
			valid = false;
		}

		if (valid) {
			if (descEt.getEditableText() == null
					|| descEt.getEditableText().toString().trim().equals("")) {
				aDialog.setMessage(getText(R.string.error_description_empty));

				valid = false;
			}
		}

		if (valid) {
			if (tagEt.getEditableText() == null) {
				aDialog.setMessage(getText(R.string.error_tag_empty));
				valid = false;
			}
		}

		return valid;
	}

	private void update_category_selection(){
		int category_id=categorySpin.getSelectedItemPosition();
		categorySpin.setSelection(category_id);
		Log.i(TAG,"category_id (update_category_selection)="+category_id);
		
	}
	
	
	class SendPostTask extends AsyncTask<Post, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			if (mDialog == null) {
				mDialog = new ProgressDialog(SubmitActivity.this);
				mDialog.setMessage(getResources().getString(R.string.submit));
				mDialog.setCancelable(false);
			}
			mDialog.show();
		}

		@Override
		protected Boolean doInBackground(Post... params) {
			Boolean isSuccess = true;
			Post post = params[0];

			try {
				isSuccess = PostService.sendPost(post);
			} catch (ClientProtocolException e) {
				isSuccess = false;
				e.printStackTrace();
			} catch (IOException e) {
				isSuccess = false;
				e.printStackTrace();
			}

			return isSuccess;
		}

		@Override
		protected void onPostExecute(Boolean isSuccess) {
			mDialog.dismiss();

			if (isSuccess) {
				Toast toast = Toast.makeText(SubmitActivity.this,
						R.string.str_thanks_for_post, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

				clear();
			} else {
				Toast toast = Toast.makeText(SubmitActivity.this,
						R.string.error_to_submit, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}

	}

}
