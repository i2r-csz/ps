package com.i2r.ps.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.ImageOptions;
import com.i2r.ps.R;
import com.i2r.ps.model.User;
import com.i2r.ps.util.CfManager;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.Utils;

public class UserActivity extends FragmentActivity {
	private String TAG = "UserActivity";

	private User user;
	private String outgoing_endorsement_state;
	private String incoming_endorsement_state;
	
	TextView tv_endorse;
	TextView tv_endorse_hint;
	TextView tv_endorse_yes;
	TextView tv_endorse_no;

	TextView tv_request_endorsement;
	TextView tv_request_endorsement_hint;
	TextView tv_be_endorsed_by_yes;
	TextView tv_be_endorsed_by_no;

	private ImageView iv_RequestToEndorse;
	private ImageView iv_RequestToBeEndorsed;

	private AQuery aq;
	
	private boolean hasConnection=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		Utils.init(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		aq = new AQuery(this);

		user = (User) getIntent().getExtras().getSerializable(User.UID);
		// post = (Post) getIntent().getExtras().getSerializable(Post.ID);
		
		outgoing_endorsement_state="";
		incoming_endorsement_state="";

		// LinearLayout view;
		// view = new LinearLayout(getContext());

		TextView tv_username = (TextView) findViewById(R.id.text_username);
		tv_username.setText(user.getName());

		ImageView iv_userPhoto = (ImageView) findViewById(R.id.user_photo);
		String url = Constants.URL_BASE + Constants.PATH_IMAGES
				+ user.getFilename();
		if (!"".equals(url)) {
			ImageOptions options = new ImageOptions();
			// options.round = 15;
			options.memCache = false;
			options.fileCache = true;
			aq.id(iv_userPhoto).image(url, options);
		} else {
			aq.id(iv_userPhoto).image(R.drawable.empty);
		}

		// TextView tv_cp_short_title = (TextView)
		// findViewById(R.id.text_CP_short_title);
		// TextView tv_ep_short_title = (TextView)
		// findViewById(R.id.text_EP_short_title);

		// TextView tv_cp_title = (TextView)
		// findViewById(R.id.text_contribution_power);
		// TextView tv_ep_title = (TextView)
		// findViewById(R.id.text_endorsement_power);

		TextView tv_cp = (TextView) findViewById(R.id.user_cp);
		// tv_cp.setText(""+user.getCp());
		tv_cp.setText("" + user.getDecimalFormatCP());

		TextView tv_ep = (TextView) findViewById(R.id.user_ep);
		// tv_ep.setText("" + user.getEp());
		tv_ep.setText("" + user.getDecimalFormatEP());

		// TextView tv2 = (TextView) findViewById(R.id.text2);
		// tv1.setText(user.getEmail());
		
		//request to endorse (action and button)

		iv_RequestToEndorse = (ImageView) findViewById(R.id.image_checkicon_1);
		iv_RequestToEndorse.setImageResource(R.drawable.check_icon_0);
		iv_RequestToBeEndorsed=(ImageView) findViewById(R.id.image_checkicon_2);
		iv_RequestToBeEndorsed.setImageResource(R.drawable.check_icon_0);
		

		
		//request to be endorsed by (action and button)
		tv_endorse = (TextView) findViewById(R.id.text_endorse);
		tv_endorse.setText(getText(R.string.not_endorsing));
		tv_endorse_hint = (TextView) findViewById(R.id.text_endorse_hint);
		tv_endorse_hint.setText(getText(R.string.request_to_endorse));
		
		tv_request_endorsement = (TextView) findViewById(R.id.text_be_endorsed_by);
		tv_request_endorsement.setText(getText(R.string.not_requested));
		tv_request_endorsement_hint = (TextView) findViewById(R.id.text_be_endorsed_by_hint);
		tv_request_endorsement_hint.setText(getText(R.string.request_for_endorsement));
		

		
		tv_endorse_yes = (TextView) findViewById(R.id.text_endorse_yes);
		tv_endorse_yes.setText(getText(R.string.empty_str));
		
		tv_endorse_no = (TextView) findViewById(R.id.text_endorse_no);
		tv_endorse_no.setText(getText(R.string.empty_str));
		
		
		tv_be_endorsed_by_yes = (TextView) findViewById(R.id.text_be_endorsed_by_yes);
		tv_be_endorsed_by_yes.setText(getText(R.string.empty_str));
		
		
		tv_be_endorsed_by_no = (TextView) findViewById(R.id.text_be_endorsed_by_no);
		tv_be_endorsed_by_no.setText(getText(R.string.empty_str));

		
		
		
		Check_Network_Chonnection();
		
		//get the endorsement relationship: endorser= mine, endorsee =the other user
		asyncJson_GetEndorsementState(CfManager.getInstantce().getUid(), user.getUid());
		//get the endorsement relationship: endorser= the other user, endorsee =mine
		asyncJson_GetEndorsementState(user.getUid(), CfManager.getInstantce().getUid());
		
		//display_buttons();
		//new DisplayUI().execute(null, null, null);

		
		// add Listener on button
		addListenerOnRequestToEndorseButton();
		addListenerOnRequestToBeEndorsedButton();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		onBackPressed();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);	
            }
		
	
	}
	
	


	public void display_buttons(){
		
		Log.d("ret", "display_buttons():outgoing_endorsement_state=" + outgoing_endorsement_state);
		Log.d("ret", "display_buttons():incoming_endorsement_state=" + incoming_endorsement_state);
		
		//display "request to endorse"
		if(outgoing_endorsement_state.equals(Constants.NO_ENDORSEMENT)){
			
			iv_RequestToEndorse.setImageResource(R.drawable.check_icon_0);
			
			tv_endorse.setText(getText(R.string.not_endorsing));
			tv_endorse_hint.setText(getText(R.string.request_to_endorse));
			tv_endorse_hint.setTextColor(Color.parseColor ("#0066FF"));
			tv_endorse_yes.setText(getText(R.string.empty_str));
			tv_endorse_no.setText(getText(R.string.empty_str));
		}
		else if(outgoing_endorsement_state.equals(Constants.REQUEST_TO_ENDORSE)){

			iv_RequestToEndorse.setImageResource(R.drawable.check_icon_1);
			
			tv_endorse.setText(getText(R.string.requested_to_endorse));
			tv_endorse_hint.setText(getText(R.string.waiting_for_response));
			tv_endorse_hint.setTextColor(Color.GRAY);
			tv_endorse_yes.setText(getText(R.string.empty_str));
			tv_endorse_no.setText(getText(R.string.empty_str));
		}
		else if(outgoing_endorsement_state.equals(Constants.REQUEST_TO_BE_ENDORSE)){
			
		    iv_RequestToEndorse.setImageResource(R.drawable.check_icon_1);
		    
			tv_endorse.setText(getText(R.string.endorse_user));
			//tv_endorse_hint.setText(getText(R.string.empty_str));
			tv_endorse_hint.setTextColor(Color.WHITE);
			tv_endorse_yes.setText(getText(R.string.yes));
			tv_endorse_no.setText(getText(R.string.no));

		}
		else if(outgoing_endorsement_state.equals(Constants.HAVING_ENDORSEMENT)){
			
			iv_RequestToEndorse.setImageResource(R.drawable.check_icon_3);
			tv_endorse.setText(getText(R.string.endorsed));
			tv_endorse_hint.setText(getText(R.string.revoke));
			tv_endorse_hint.setTextColor(Color.parseColor ("#0066FF"));
			tv_endorse_yes.setText(getText(R.string.empty_str));
			tv_endorse_no.setText(getText(R.string.empty_str));
		}
		
		
		//display "request to be endorsed"
		if(incoming_endorsement_state.equals(Constants.NO_ENDORSEMENT)){
			iv_RequestToBeEndorsed.setImageResource(R.drawable.check_icon_0);
			tv_request_endorsement.setText(getText(R.string.not_requested));
			tv_request_endorsement_hint.setText(getText(R.string.request_for_endorsement));
			tv_request_endorsement_hint.setTextColor(Color.parseColor ("#0066FF"));
			tv_be_endorsed_by_yes.setText(getText(R.string.empty_str));
			tv_be_endorsed_by_no.setText(getText(R.string.empty_str));
		}
		else if(incoming_endorsement_state.equals(Constants.REQUEST_TO_ENDORSE)){
			iv_RequestToBeEndorsed.setImageResource(R.drawable.check_icon_1);
			tv_request_endorsement.setText(getText(R.string.accept_endorsement));
			//tv_request_endorsement_hint.setText(getText(R.string.empty_str));
			tv_request_endorsement_hint.setTextColor(Color.WHITE);
			tv_be_endorsed_by_yes.setText(getText(R.string.yes));
			tv_be_endorsed_by_no.setText(getText(R.string.no));

		}
		else if(incoming_endorsement_state.equals(Constants.REQUEST_TO_BE_ENDORSE)){
			iv_RequestToBeEndorsed.setImageResource(R.drawable.check_icon_1);
			tv_request_endorsement.setText(getText(R.string.requested_for_endorsement));
			tv_request_endorsement_hint.setText(getText(R.string.waiting_for_response));
			tv_request_endorsement_hint.setTextColor(Color.GRAY);
			tv_be_endorsed_by_yes.setText(getText(R.string.empty_str));
			tv_be_endorsed_by_no.setText(getText(R.string.empty_str));
		}
		else if(incoming_endorsement_state.equals(Constants.HAVING_ENDORSEMENT)){
			iv_RequestToBeEndorsed.setImageResource(R.drawable.check_icon_3);
			tv_request_endorsement.setText(getText(R.string.endorsed_by_this_user));
			tv_request_endorsement_hint.setText(getText(R.string.revoke));
			tv_request_endorsement_hint.setTextColor(Color.parseColor ("#0066FF"));
			tv_be_endorsed_by_yes.setText(getText(R.string.empty_str));
			tv_be_endorsed_by_no.setText(getText(R.string.empty_str));
			
		}
		
	}
	

	


	public void addListenerOnRequestToEndorseButton() {

		//iv_RequestToEndorse.setOnClickListener(new OnClickListener() {
		tv_endorse_hint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				Check_Network_Chonnection();
				if(hasConnection){
					if(tv_endorse_hint.getText().equals(getText(R.string.request_to_endorse))){
						if (CfManager.getInstantce().getUid() == Constants.DEFAULT_USER_ID_WITHOUT_LOGIN) {
							Toast.makeText(UserActivity.this, "Please Login first",
									Toast.LENGTH_SHORT).show();
						} else {
							if(outgoing_endorsement_state.equals(Constants.REQUEST_TO_ENDORSE)){
								Toast.makeText(UserActivity.this, "Duplicate requests are not allowed.",
										Toast.LENGTH_SHORT).show();
							}
							else{
								Toast.makeText(UserActivity.this, "You have requested to endorese this user.",
										Toast.LENGTH_SHORT).show();
								asyncJson_SendEndorsement(CfManager.getInstantce().getUid(), user.getUid(), Constants.REQUEST_TO_ENDORSE);
							}
						}
						
					}
					else if(tv_endorse_hint.getText().equals(getText(R.string.revoke))){
						Toast.makeText(UserActivity.this, "You have rejected to endorse this user.",
								Toast.LENGTH_SHORT).show();
						asyncJson_SendEndorsement(CfManager.getInstantce().getUid(), user.getUid(), Constants.NO_ENDORSEMENT);
						
					}
					
					update_UI();
					
				}

			}

		});
		
		
		tv_endorse_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Check_Network_Chonnection();
				if(hasConnection){
					if(!tv_endorse_yes.getText().equals(getText(R.string.empty_str))){
						asyncJson_SendEndorsement(CfManager.getInstantce().getUid(), user.getUid(), Constants.HAVING_ENDORSEMENT);
						Toast.makeText(UserActivity.this, "You are now endorsing the user!",
								Toast.LENGTH_SHORT).show();
						update_UI();
					}
					
				}

			}

		});
		
		tv_endorse_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Check_Network_Chonnection();
				if(hasConnection){
					if(!tv_endorse_no.getText().equals(getText(R.string.empty_str))){
						asyncJson_SendEndorsement(CfManager.getInstantce().getUid(), user.getUid(), Constants.NO_ENDORSEMENT);
						Toast.makeText(UserActivity.this, "You have rejected to endorse the user!",
								Toast.LENGTH_SHORT).show();
						update_UI();
					}
				}


			}

		});

	}
	
	public void addListenerOnRequestToBeEndorsedButton() {

		//iv_RequestToBeEndorsed.setOnClickListener(new OnClickListener() {
		tv_request_endorsement_hint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Check_Network_Chonnection();
				if(hasConnection){
					if(tv_request_endorsement_hint.getText().equals(getText(R.string.request_for_endorsement))){
						if (CfManager.getInstantce().getUid() == Constants.DEFAULT_USER_ID_WITHOUT_LOGIN) {
							Toast.makeText(UserActivity.this, "Please Login first",
									Toast.LENGTH_SHORT).show();

						} else {
							if(incoming_endorsement_state.equals(Constants.REQUEST_TO_BE_ENDORSE)){
								Toast.makeText(UserActivity.this, "Duplicate requests are not allowed.",
										Toast.LENGTH_SHORT).show();
							}
							else{
								asyncJson_SendEndorsement(user.getUid(), CfManager.getInstantce().getUid(), Constants.REQUEST_TO_BE_ENDORSE);
								Toast.makeText(UserActivity.this, "You have requested the user to endorse you!",
										Toast.LENGTH_SHORT).show();
								
							}
						}
					}
					else if(tv_request_endorsement_hint.getText().equals(getText(R.string.revoke))){
					
							Toast.makeText(UserActivity.this, "You have rejected to be endorsed this user.",
									Toast.LENGTH_SHORT).show();
							asyncJson_SendEndorsement(user.getUid(), CfManager.getInstantce().getUid(), Constants.NO_ENDORSEMENT);
						
					}

					update_UI();
					
				}
				


			}

		});
		
		tv_be_endorsed_by_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {	
				
				Check_Network_Chonnection();
				if(hasConnection){
					if(!tv_be_endorsed_by_yes.getText().equals(getText(R.string.empty_str))){
						asyncJson_SendEndorsement(user.getUid(), CfManager.getInstantce().getUid(), Constants.HAVING_ENDORSEMENT);
						Toast.makeText(UserActivity.this, "You are now being endorsed by this user.",
								Toast.LENGTH_SHORT).show();
						update_UI();
					}
					
				}

			}

		});
		
		tv_be_endorsed_by_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Check_Network_Chonnection();
				if(hasConnection){
					if(!tv_be_endorsed_by_no.getText().equals(getText(R.string.empty_str))){
						asyncJson_SendEndorsement(user.getUid(), CfManager.getInstantce().getUid(), Constants.NO_ENDORSEMENT);
						Toast.makeText(UserActivity.this, "You have rejected to be endorsed by this user.",
								Toast.LENGTH_SHORT).show();
						update_UI();
					}
					
				}


			}

		});

	}
	
	private void Check_Network_Chonnection(){
	
		String url="http://54.254.151.32/mpost/getEndorsement?"+"endorser="+CfManager.getInstantce().getUid()+"&"+"endorsee="+user.getUid();
		Map<String, String> params = new HashMap<String, String>();


		aq.ajax(url, params, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String ret, AjaxStatus status) {
				hasConnection=false;
		
		       switch (status.getCode())
		        {
		            case AjaxStatus.TRANSFORM_ERROR:
		            	Toast.makeText(aq.getContext(), "TRANSFORM_ERROR: " + status.getCode(), Toast.LENGTH_LONG).show();
		            	break;
		            case AjaxStatus.NETWORK_ERROR:
		            	//Toast.makeText(aq.getContext(), "NETWORK_ERROR " + status.getCode(), Toast.LENGTH_LONG).show();
		            	Toast.makeText(aq.getContext(), "NETWORK_ERROR: Please connect the network to load your endorsement state.", Toast.LENGTH_LONG).show();
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
		            		
		            		if(ret.contains("Web Authentication")){
		            			Toast.makeText(aq.getContext(), "WEB AUTHENTICATION ERROR: Please Login your network.", Toast.LENGTH_LONG).show();
		            			Log.d("network_status","ret="+ret);
		            		}
		            		else{
		            			hasConnection=true;
		            		}
		            		
		            	}
		            	break;
		        }
			}
	       
		});
	}
	
	public void asyncJson_GetEndorsementState(int endorser_id, int endorsee_id) {

	
		String url="http://54.254.151.32/mpost/getEndorsement?"+"endorser="+endorser_id+"&"+"endorsee="+endorsee_id;
		Map<String, String> params = new HashMap<String, String>();
		
		//params.put(Constants.PARAM_ENDORSEMENT_ENDORSER, CfManager.getInstantce().getUid()+ "");
		//params.put(Constants.PARAM_ENDORSEMENT_ENDORSEE, user.getUid() + "");
		//params.put("Constants.PARAM_ENDORSEMENT_STATE", "0");
		
		Log.d("ret", "GetEndorsementState()endorser_id=" + CfManager.getInstantce().getUid()+ "");
		Log.d("ret", "GetEndorsementState()endorsee_id=" + user.getUid() + "");
		

		aq.ajax(url, params, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String ret, AjaxStatus status) {
				
				
				if (ret != null && status.getCode() == 200) {
					if (ret.contains("ERROR")) {
						//
						Toast toast = Toast.makeText(UserActivity.this,
								R.string.error_endorse, Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					} else {
						String ret_state="";
						String ret_endorser="";
						String ret_endorsee="";
						String my_id=CfManager.getInstantce().getUid()+"";
						String the_user_id=user.getUid()+"";
						
						try {
							JSONObject jsonObjectFromString = new JSONObject(ret);
							ret_state=jsonObjectFromString.get("state").toString();
							ret_endorser=jsonObjectFromString.get("endorser").toString();
							ret_endorsee=jsonObjectFromString.get("endorsee").toString();
		
							
							Log.d("ret", "GetEndorsementState(state is not zero)= " + ret);
							Log.d("ret", "GetEndorsementState(state is not zero)ret_state= " + ret_state);
							Log.d("ret", "GetEndorsementState(state is not zero)ret_endorser= " + ret_endorser);
							Log.d("ret", "GetEndorsementState(state is not zero)ret_endorsee= " + ret_endorsee);
						
							
							if(ret_endorser.equals(my_id) && ret_endorsee.equals(the_user_id) ){
								outgoing_endorsement_state=ret_state;
								display_buttons();
								Log.d("ret", "GetEndorsementState(state is not zero)outgoing_endorsement_state= " + outgoing_endorsement_state);
							}
							else if(ret_endorser.equals(the_user_id) &&(ret_endorsee.equals(my_id)) ){
								incoming_endorsement_state=ret_state;
								display_buttons();
								Log.d("ret", "GetEndorsementState(state is not zero)incoming_endorsement_state= " + incoming_endorsement_state);
							}
							else{
								Log.d("ret", "GetEndorsementState(state is not zero)= Error!!" );
								
							}
/*					
							
							if(current_state.equals(Constants.REQUEST_TO_ENDORSE)){
								//endorsement state=1;
								CfManager.getInstantce().setEndorsementState(Constants.REQUEST_TO_ENDORSE);
								iv_RequestToEndorse.setImageResource(R.drawable.check_icon_1);
								//iv_RequestToBeEndorsed.setImageResource(R.drawable.check_icon);
								
							}else if (current_state.equals(Constants.REQUEST_TO_BE_ENDORSE)) {
								//endorsement state=2;
								CfManager.getInstantce().setEndorsementState(Constants.REQUEST_TO_BE_ENDORSE);
								
								Log.d("ret", "GetEndorsementState()_state=REQUEST_TO_BE_ENDORSE" + jsonObjectFromString.get("state").toString());
								iv_RequestToEndorse.setImageResource(R.drawable.check_icon_1);
								//iv_RequestToBeEndorsed.setImageResource(R.drawable.check_icon_2);
							}
							else if(current_state.equals(Constants.HAVING_ENDORSEMENT)){ 
								//endorsement state=3;
								iv_RequestToEndorse.setImageResource(R.drawable.check_icon_3);
								//iv_RequestToBeEndorsed.setImageResource(R.drawable.check_icon_2);
								
							}
							*/
							
						} catch (JSONException e) {
							
							// TODO Auto-generated catch block
							Log.d("ret", "GetEndorsementState(state is zero)= " + ret);
							//endorsement state=0, no relationship between them
							outgoing_endorsement_state=Constants.NO_ENDORSEMENT;
							incoming_endorsement_state=Constants.NO_ENDORSEMENT;
							
							//CfManager.getInstantce().setEndorsementState(Constants.NO_ENDORSEMENT);
							//iv_RequestToEndorse.setImageResource(R.drawable.check_icon_0);
							
							//iv_RequestToBeEndorsed.setImageResource(R.drawable.check_icon);
							
							e.printStackTrace();
						}
						


					}
				}
			}
		});
	}

	public void asyncJson_SendEndorsement(final int endorser_id, final int endorsee_id, String state){

		
			String endorser=endorser_id+"";
			String endorsee=endorsee_id+"";
			Log.d("ret", "asyncJson_SendEndorsement_endorser_id=" + endorser_id);
			Log.d("ret", "asyncJson_SendEndorsementendorsee_id=" + endorsee_id);
			Log.d("ret", "asyncJson_SendEndorsementstate=" + state);
			
			if(endorser_id==endorsee_id){
				Toast.makeText(UserActivity.this, "You cannot send request to yourself!",
						Toast.LENGTH_SHORT).show();
				
			}
			else{
				String url = "http://54.254.151.32/mpost/endorse";
				Map<String, String> httpPOST_params = new HashMap<String, String>();
				
				httpPOST_params.put(Constants.PARAM_ENDORSEMENT_ENDORSER, endorser);
				httpPOST_params.put(Constants.PARAM_ENDORSEMENT_ENDORSEE, endorsee);
				httpPOST_params.put(Constants.PARAM_ENDORSEMENT_STATE, state);
	
				aq.ajax(url, httpPOST_params, String.class, new AjaxCallback<String>() {
					@Override
					public void callback(String url, String ret, AjaxStatus status) {
						Log.d("ret", "ret=" + ret);
						
						if (ret != null && status.getCode() == 200) {
							if (ret.contains("ERROR")) {
								//
								Toast toast = Toast.makeText(UserActivity.this,
										R.string.error_endorse, Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							} else {
								String req_state="";
									
	
								try {
									JSONObject jsonObjectFromString = new JSONObject(ret);
									req_state=jsonObjectFromString.get("state").toString();
									//CfManager.getInstantce().setEndorsementState(req_state);
	
									//Toast toast_state = Toast.makeText(UserActivity.this,
									//		"State:"+jsonObjectFromString.get("state").toString(), Toast.LENGTH_SHORT);
									//toast_state.setGravity(Gravity.CENTER, 0, 0);
									///toast_state.show();
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	
								if(req_state.equals(Constants.REQUEST_TO_ENDORSE)){
									// change button icon.
									//iv_RequestToEndorse
									//.setImageResource(R.drawable.check_icon_1);
									
									update_UI();
									
								}
								else if(req_state.equals(Constants.NO_ENDORSEMENT)){

									update_UI();
									
								}
								else if(req_state.equals(Constants.REQUEST_TO_BE_ENDORSE)){
									// change button icon.
									//iv_RequestToBeEndorsed
									//.setImageResource(R.drawable.check_icon_1);
									

									update_UI();
									
								}
								else if(req_state.equals(Constants.HAVING_ENDORSEMENT)){

									update_UI();
									
								}
								
							}
						}
					}
				});
				
	
			}
		

	}

	


	public void update_UI(){
		asyncJson_GetEndorsementState(CfManager.getInstantce().getUid(), user.getUid());
		asyncJson_GetEndorsementState(user.getUid(), CfManager.getInstantce().getUid());
		display_buttons();
		
	}
	
	
	
/*
	public class DisplayUI extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	
		protected Void doInBackground(Void... params) {
			display_buttons();
			return null;

		}

		 @Override
		 protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);	 
		 }
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
		}

	}
*/
	
	
	
	
}
