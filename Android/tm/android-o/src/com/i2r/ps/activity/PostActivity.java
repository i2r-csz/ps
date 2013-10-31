package com.i2r.ps.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.i2r.ps.R;
import com.i2r.ps.model.Post;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.Utils;

public class PostActivity extends FragmentActivity {
	private String TAG = "PostActivity";

	private Post post;

	private GoogleMap mMap;

	private AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		Utils.init(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		aq = new AQuery(this);

		post = (Post) getIntent().getExtras().getSerializable(Post.ID);

		String url = Constants.URL_BASE + Constants.PATH_POST_IMAGE
				+ post.getImage_file();
		if (!"".equals(url)) {
			ImageOptions options = new ImageOptions();
			// options.round = 15;
			options.memCache = false;
			options.fileCache = true;
			aq.id(R.id.post_iv).image(url, options);
		} else {
			aq.id(R.id.post_iv).image(R.drawable.empty);
		}

		aq.id(R.id.post_date).text(post.getCreated_on().toGMTString());
		aq.id(R.id.post_username).text(post.getUsername());
		aq.id(R.id.desc_tv).text(post.getDescription());

		String indiSevTxt = getResources().getString(
				R.string.individual_severity, post.getSeverity());
		String aggSevTxt = getResources().getString(
				R.string.aggregate_severity, post.getSeverity());
		aq.id(R.id.indi_severity_tv).text(indiSevTxt);
		aq.id(R.id.agg_severity_tv).text(aggSevTxt);

		setUpMapIfNeeded();

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(post.getLat(), post.getLng()),
				Constants.MAP_DEFAULT_LEVEL2));

		mMap.addMarker(createCameraMarker(post));
	}

	private MarkerOptions createCameraMarker(Post post) {
		return new MarkerOptions()
				.position(new LatLng(post.getLat(), post.getLng()))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.report))
				.title(post.getDescription());
	}

	private void setUpMapIfNeeded() {
		Log.i(TAG, "setUpMapIfNeeded Start");

		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}

		Log.i(TAG, "setUpMapIfNeeded End");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		}
		return true;
	}
}
