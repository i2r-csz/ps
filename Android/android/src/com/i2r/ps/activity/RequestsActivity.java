package com.i2r.ps.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.i2r.ps.R;
import com.i2r.ps.model.User;
import com.i2r.ps.util.CfManager;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.Utils;

public class RequestsActivity extends Activity {
	private String TAG = "RequestsActivity";

	ListView listView_requests ;
	 

	private String get_requests_path=Constants.URL_BASE+Constants.PATH_GET_REQUESTS;
	private String url;
	private AQuery aq;
	private JSONArray req_list;
	private int request_type=0;
    private ArrayAdapter<JSONObject> adapter;
    private boolean hasConnection=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_requests);
		Utils.init(this);
		listView_requests = (ListView) findViewById(R.id.requests);
		

		aq = new AQuery(this);
		request_type = (Integer) getIntent().getExtras().getSerializable(Constants.REQUEST_TYPE);
		req_list = new JSONArray();
		
	
		url="";
		if(request_type==Constants.ENDORSRING_REQUESTS){
			url = get_requests_path+"?"+"uid="+CfManager.getInstantce().getUid()+"&"+"endorse=0";
		}
		else if(request_type==Constants.ENDORSEMENT_REQUESTS){
			url = get_requests_path+"?"+"uid="+CfManager.getInstantce().getUid()+"&"+"endorse=1";
			
		}
		Check_Network_Chonnection(url);
		
			
	
		
		
		listView_requests.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Log.d(TAG, "onItemClick: position= " + position);
				

				JSONObject user_request = adapter.getItem(position);
				User user = new User();
				String str_rq_id="";
				try {
					if(request_type==Constants.ENDORSRING_REQUESTS){
						str_rq_id=user_request.get("endorser").toString();
					}
					else if(request_type==Constants.ENDORSEMENT_REQUESTS){
						str_rq_id=user_request.get("endorsee").toString();
					}
					
					Log.d(TAG, "onItemClick: request_id= " + str_rq_id);
					asyncJson_GetUser(str_rq_id);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				

			}
		});

	
	}
		
		

	private void asyncJson_GetEndorsingRequests(String url) {

		
		
		Map<String, String> params = new HashMap<String, String>();		

		aq.ajax(url, params, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String ret, AjaxStatus status) {
				
				if (ret != null && status.getCode() == 200) {
					
					try {
						JSONArray json_array = new JSONArray(ret);
						req_list=json_array;
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//req_list = new JSONArray(ret);
					
					
					Log.d(TAG, "endorsing_req_list_length= " + req_list.length());
					Log.d(TAG, "ret= " + ret);
					updateUI();


				}
			}
		});
		
		updateUI();
	}


	
	
	private void Check_Network_Chonnection(String url){
	
		
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
		            			asyncJson_GetEndorsingRequests(url);
		            			hasConnection=true;
		            		}
		            		
		            	}
		            	break;
		        }
		       updateUI();
			}
	       
		});
	}
	
	
	
	
	private void updateUI() {
		Log.d(TAG, "updateUI= " + "???");
		
		adapter = new ArrayAdapter<JSONObject>(this, R.layout.item_request) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Log.d(TAG, "updateUI= " + "getView");
				
				LinearLayout view;
				if (convertView == null) {
					view = new LinearLayout(getContext());

					String inflater = Context.LAYOUT_INFLATER_SERVICE;
					LayoutInflater vi;
					vi = (LayoutInflater) getContext().getSystemService(
							inflater);
					vi.inflate(R.layout.item_request, view, true);
				} else {
					view = (LinearLayout) convertView;
				}
				JSONObject endorsement_relationship = getItem(position);
				Log.d(TAG, "request_user_name= " + "???");
				Log.d(TAG, "position= " + position);
				String request_user_name="";
				try {
					if(request_type==Constants.ENDORSRING_REQUESTS){
						request_user_name=endorsement_relationship.get("endorser_name").toString();

						
					}
					else if(request_type==Constants.ENDORSEMENT_REQUESTS){
						request_user_name=endorsement_relationship.get("endorsee_name").toString();
					}
					Log.d(TAG, "request_user_name= " + request_user_name);
					TextView request_name = (TextView) view.findViewById(R.id.request_user_name);
					request_name.setText(request_user_name);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d(TAG, "request_user_name= " + "???");
				}
				return view;
			}
		};
		
		listView_requests.setAdapter(adapter); 

		//adapter.clear();
		//adapter.notifyDataSetChanged();
		for(int i=0; i<req_list.length();i++){
			
			try {
				JSONObject json_obj;
				json_obj = req_list.getJSONObject(i);
				Log.d(TAG, "json_obj= " + json_obj.get("endorser_name").toString());
				adapter.add(json_obj);
				adapter.notifyDataSetChanged();
				Log.d(TAG, "adapter.getCount()= " + adapter.getCount());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		if(req_list.length()==0){
			JSONObject zero_requests= new JSONObject();
			try {
				if(request_type==Constants.ENDORSRING_REQUESTS){
					zero_requests.put("endorser_name", "No requests");
					adapter.add(zero_requests);
					adapter.notifyDataSetChanged();
				}
				else if(request_type==Constants.ENDORSEMENT_REQUESTS){
					zero_requests.put("endorsee_name", "No requests");
					adapter.add(zero_requests);
					adapter.notifyDataSetChanged();
				}

				Log.d(TAG, "adapter.isEmpty= " + adapter.getCount());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "adapter.isEmpty= " + adapter.getCount());
				e.printStackTrace();
			}

		}
		Log.d(TAG, "adapter.isEmpty= " + adapter.getCount());
		
		
	}


	private void asyncJson_GetUser(String uid){
		String url=Constants.URL_BASE+Constants.PATH_GET_USER+uid;
		Map<String, String> params = new HashMap<String, String>();		

		aq.ajax(url, params, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String ret, AjaxStatus status) {
				
				if (ret != null && status.getCode() == 200) {
					
					User user = new User();
					Gson gson = new Gson();
					user = gson.fromJson (ret, User.class);
					Log.d(TAG, "asyncJson_GetUser: useremail= " + user.getEmail());
					Log.d(TAG, "asyncJson_GetUser: user_CP= " + user.getCp());
					
					Intent intent = new Intent(RequestsActivity.this,
							UserActivity.class);			
					intent.putExtra(User.UID, user);
					startActivity(intent);

				}
			}
		});
		
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		//setTitle(R.string.title_requests);
		if(request_type==Constants.ENDORSRING_REQUESTS){
			setTitle(R.string.title_requests);	
			setTitle("Who requested to endorse you");
		}
		else if(request_type==Constants.ENDORSEMENT_REQUESTS){
			setTitle("Who asked for your endorsement");
		}
		
	}
}
