package com.i2r.ps.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.ImageOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.i2r.ps.R;
import com.i2r.ps.model.Post;
import com.i2r.ps.util.CfManager;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.Utils;

public class PostActivity extends FragmentActivity {
	private String TAG = "PostActivity";

	private Post post;

	private GoogleMap mMap;

	private AQuery aq;

	private MenuItem menu_thumb_up_item;
	private MenuItem menu_thumb_down_item;
	
	private String THUMBS_URL=Constants.URL_BASE+Constants.PATH_THUMBS;
	private String current_thumbs_vlaue="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		Utils.init(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		


		

		aq = new AQuery(this);

		post = (Post) getIntent().getExtras().getSerializable(Post.ID);
		
		//menu_thumb_up_item = (MenuItem)findViewById(R.id.action_thumb_up);
		//menu_thumb_up_item.setIcon(R.drawable.thumb_up2);
		
		
		Get_Current_Thumbs_value();

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

	
		
		//aq.id(R.id.post_date).text(post.getCreated_on().toGMTString());
		aq.id(R.id.post_date).text(post.getCreated_on().toString());
		aq.id(R.id.category_tv).text("Category: "+post.getCategoryText(post.getCategory()));
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
		
		/*
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		}
		return true;
		*/
		
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		onBackPressed();
	    		return true;
	        case R.id.action_thumb_up:
	        	//increase votes;
	        	//menu_item = item;
	        	asyncJson_ThumbUpDown(post.getId()+"", CfManager.getInstantce().getUid()+"", Constants.THUMBS_UP_VALUE);
	            return true;
	        case R.id.action_thumb_down:
	            //decrease votes;
	        	//menu_item = item;
	        	asyncJson_ThumbUpDown(post.getId()+"", CfManager.getInstantce().getUid()+"", Constants.THUMBS_DOWN_VALUE);
	      
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        	
	        	
	    }
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    menu_thumb_up_item = (MenuItem) menu.findItem(R.id.action_thumb_up);
	    menu_thumb_down_item= (MenuItem) menu.findItem(R.id.action_thumb_down);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	

	
	
	
	public void asyncJson_ThumbUpDown(String post_id, String user_id, String Thumbs_value){
		Get_Current_Thumbs_value();

		Log.d("ret_thumbs", "asyncJson_ThumbUpDown_post_id=" + post_id);
		Log.d("ret_thumbs", "asyncJson_ThumbUpDown_user_id=" + user_id);
		Log.d("ret_thumbs", "asyncJson_ThumbUpDown_Thumbs_value=" + Thumbs_value);
		
		if(Thumbs_value.equals(current_thumbs_vlaue)){
			Toast.makeText(PostActivity.this, "Duplicate thrumbs up/down!",
					Toast.LENGTH_SHORT).show();
			
		}
		else{
			Map<String, String> params = new HashMap<String, String>();
			
			params.put(Constants.PARAM_THUMBS_POST_ID, post_id);
			params.put(Constants.PARAM_THUMBS_USER_ID, user_id);
			params.put(Constants.PARAM_THUMBS_VALUE, Thumbs_value);

			aq.ajax(THUMBS_URL, params, String.class, new AjaxCallback<String>() {
				@Override
				public void callback(String url, String ret, AjaxStatus status) {
					Log.d("ret_thumbs", "ret_thumbs=" + ret);
					Log.d("ret_thumbs", "ret_thumbs THUMBS_URL=" + THUMBS_URL);

					
					
					if (ret != null && status.getCode() == 200) {

							try {
								JSONObject jsonObjectFromString = new JSONObject(ret);
								String ret_thumb_value=jsonObjectFromString.get("value").toString();
								current_thumbs_vlaue=ret_thumb_value;
								if(ret_thumb_value.equals(Constants.THUMBS_UP_VALUE)){
									
									Toast.makeText(PostActivity.this, "Thumbs up the report!",
											Toast.LENGTH_SHORT).show();
									 menu_thumb_up_item.setIcon(R.drawable.thumb_up2);
									 menu_thumb_down_item.setIcon(R.drawable.thumb_down1);
									
									Log.d("ret_thumbs", "ret_thumb_value=" + ret_thumb_value);
								}
								else if(ret_thumb_value.equals(Constants.THUMBS_NORMAL_VALUE)){
									Toast.makeText(PostActivity.this, "Reset the report!",
											Toast.LENGTH_SHORT).show();
									Log.d("ret_thumbs", "ret_thumb_value=" + ret_thumb_value);
								}
								else if(ret_thumb_value.equals(Constants.THUMBS_DOWN_VALUE)){
									Toast.makeText(PostActivity.this, "Thumbs down the report!",
											Toast.LENGTH_SHORT).show();
									menu_thumb_up_item.setIcon(R.drawable.thumb_up1);
									menu_thumb_down_item.setIcon(R.drawable.thumb_down2);
									Log.d("ret_thumbs", "ret_thumb_value=" + ret_thumb_value);
								}


							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


			
					}
				}
			});
			

		}

	}
	
	
	
	
	public void Get_Current_Thumbs_value(){
		String post_id=post.getId()+"";
		String user_id=CfManager.getInstantce().getUid()+"";
		
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.PARAM_THUMBS_POST_ID, post_id);
		params.put(Constants.PARAM_THUMBS_USER_ID, user_id);

		aq.ajax(THUMBS_URL, params, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String ret, AjaxStatus status) {
		
		       switch (status.getCode())
		        {
		            case AjaxStatus.TRANSFORM_ERROR:
		            	Toast.makeText(aq.getContext(), "TRANSFORM_ERROR: " + status.getCode(), Toast.LENGTH_LONG).show();
		            	break;
		            case AjaxStatus.NETWORK_ERROR:
		            	//Toast.makeText(aq.getContext(), "NETWORK_ERROR " + status.getCode(), Toast.LENGTH_LONG).show();
		            	Toast.makeText(aq.getContext(), "NETWORK_ERROR: Please connect the network first.", Toast.LENGTH_LONG).show();
		            	break;
		            case AjaxStatus.AUTH_ERROR:
		            	Toast.makeText(aq.getContext(), "AUTH_ERROR" + status.getCode(), Toast.LENGTH_LONG).show();
		            	break;
		            case AjaxStatus.NETWORK:
		            	Toast.makeText(aq.getContext(), "NETWORK" + status.getCode(), Toast.LENGTH_LONG).show();
		            	break;
		            default:
		            	if(CfManager.getInstantce().getUid() == Constants.DEFAULT_USER_ID_WITHOUT_LOGIN){
		            		Toast.makeText(aq.getContext(), "LOGIN ERROR: Please Login first.", Toast.LENGTH_LONG).show();
		            		//Toast.makeText(aq.getContext(), "OTHER ERROR" + status.getCode(), Toast.LENGTH_LONG).show();
		            	}
		            	else{
		            		
							try {
								JSONObject jsonObjectFromString = new JSONObject(ret);
								current_thumbs_vlaue=jsonObjectFromString.get("value").toString();
								display_menu_item();
								Log.d("ret_thumbs", "Get_Current_Thumbs_value()=" + current_thumbs_vlaue);

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
		            	}
		            	break;
		        }
			}
	       
		});
	}
	
	
	public void display_menu_item(){
		if(current_thumbs_vlaue.equals(Constants.THUMBS_UP_VALUE)){
			menu_thumb_up_item.setIcon(R.drawable.thumb_up2);
			menu_thumb_down_item.setIcon(R.drawable.thumb_down1);
		}
		else if(current_thumbs_vlaue.equals(Constants.THUMBS_NORMAL_VALUE)){
			menu_thumb_up_item.setIcon(R.drawable.thumb_up1);
			menu_thumb_down_item.setIcon(R.drawable.thumb_down1);
		}
		else if(current_thumbs_vlaue.equals(Constants.THUMBS_DOWN_VALUE)){
			menu_thumb_up_item.setIcon(R.drawable.thumb_up1);
			menu_thumb_down_item.setIcon(R.drawable.thumb_down2);
		}
		
	}

}
