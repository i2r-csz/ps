package com.i2r.ps.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class HttpService {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0";
	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";

	/**
	 * This method will do post funcion.
	 * 
	 * @param url
	 * @param kvPairs
	 * @param charset
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> kvPairs, String charset)
			throws ClientProtocolException, IOException {
		// set timeout
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				Constants.HTTP_REQUEST_TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParameters,
				Constants.HTTP_REQUEST_TIME_OUT);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		
		if(url.contains(" ")){
			url=url.replace(" ", "%20");
		}

		HttpPost httppost = new HttpPost(url);
		httppost.setHeader(HTTP.USER_AGENT, USER_AGENT);
		httppost.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE);

		if (kvPairs != null && kvPairs.isEmpty() == false) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					kvPairs.size());
			String k, v;
			Iterator<String> itKeys = kvPairs.keySet().iterator();

			while (itKeys.hasNext()) {
				k = itKeys.next();
				v = kvPairs.get(k);
				nameValuePairs.add(new BasicNameValuePair(k, v));
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
		}

		HttpResponse response = httpclient.execute(httppost);
	
		// log response status
		String ret = null;
		if (response != null) {
			ret = EntityUtils.toString(response.getEntity(), charset);
		}
		
		return ret;
	}

	/**
	 * This method will do the http get function.
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String doGet(String url, String charset)
			throws ClientProtocolException, IOException {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				Constants.HTTP_REQUEST_TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParameters,
				Constants.HTTP_REQUEST_TIME_OUT);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		
		if(url.contains(" ")){
			url=url.replace(" ", "%20");
		}

		HttpGet httpget = new HttpGet(url);
		httpget.setHeader(HTTP.USER_AGENT, USER_AGENT);
		httpget.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE);

		HttpResponse response = httpclient.execute(httpget);
		// log response status
		// Log.i(HttpService.class.toString(), response.getStatusLine().toString());
		String ret = null;
		if (response != null) {
			ret = EntityUtils.toString(response.getEntity(), charset);
		}
		return ret;
	}
}
