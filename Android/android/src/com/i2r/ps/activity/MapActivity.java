package com.i2r.ps.activity;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.i2r.ps.R;
import com.i2r.ps.activity.adapter.DateDeserializer;
import com.i2r.ps.db.DbManager;
import com.i2r.ps.model.Post;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.HttpService;
import com.i2r.ps.util.Utils;

public class MapActivity extends FragmentActivity {
	private static final String TAG = "NearbyActivity";

	private LinearLayout footerLl;

	private GoogleMap mMap;

	private List<Post> posts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Utils.init(this);

		setUpMapIfNeeded();

		footerLl = (LinearLayout) findViewById(R.id.footer_ll);

		try {
			MapsInitializer.initialize(this);
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// default location
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				Constants.MAP_DEFAULT_LAT, Constants.MAP_DEFAULT_LNG),
				Constants.MAP_DEFAULT_LEVEL2));

		posts = DbManager.getInstance().getPosts(0);

		new UpdateTask().execute(new Void[] {});
		updateMarkers();
	}

	public void onRefreshClick(View view) {
		new UpdateTask().execute(new Void[] {});
	}

	private void updateMarkers() {
		mMap.clear();

		for (int position = 0; position < posts.size(); position++) {
			mMap.addMarker(createCameraMarker(position));
		}

		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				Intent intent = new Intent(MapActivity.this, PostActivity.class);
				String indexStr = marker.getTitle();
				int index = Integer.parseInt(indexStr);
				Post post = posts.get(index);
				intent.putExtra(Post.ID, post);
				startActivity(intent);
				return true;
			}
		});
	}

	private MarkerOptions createCameraMarker(int position) {
		Post post = posts.get(position);
		return new MarkerOptions()
				.position(new LatLng(post.getLat(), post.getLng()))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.report))
				.title(position + "");
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
	protected void onResume() {
		super.onResume();

		setTitle(R.string.title_mapview);
	}

	class UpdateTask extends AsyncTask<Void, Void, Void> {
		boolean isError;

		@Override
		protected void onPreExecute() {
			footerLl.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			String url = Constants.URL_BASE + Constants.PATH_POST_LIST;
			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put(Constants.PARAM_PAGE_NUM, 0 + "");
			try {
				String postsStr = HttpService.doPost(url, kvPairs, HTTP.UTF_8);

				GsonBuilder gsonB = new GsonBuilder();
				gsonB.registerTypeAdapter(Date.class, new DateDeserializer());
				Gson gson = gsonB.create();
				Type type = new TypeToken<List<Post>>() {
				}.getType();
				List<Post> posts = gson.fromJson(postsStr, type);

				// save
				DbManager.getInstance().savePosts(posts);
			} catch (Exception e) {
				isError = true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			footerLl.setVisibility(View.GONE);

			if (isError) {
				Toast.makeText(MapActivity.this, R.string.error_to_load_posts,
						Toast.LENGTH_SHORT).show();
			} else {
				posts = DbManager.getInstance().getPosts(0);
				updateMarkers();
			}
		}

	}
}
