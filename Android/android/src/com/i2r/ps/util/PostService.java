package com.i2r.ps.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.i2r.ps.activity.adapter.PostDeserializer;
import com.i2r.ps.db.DbManager;
import com.i2r.ps.model.Post;

public class PostService {
	

	//
	public static boolean sendPost(Post post) throws ClientProtocolException,
			IOException {
		boolean isSuccess = false;

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Constants.URL_BASE
				+ Constants.PATH_POST_UPLOAD);

		//
		MultipartEntity entity = new MultipartEntity();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		post.getImage().compress(Bitmap.CompressFormat.JPEG, 100, bao);
		byte[] ba = bao.toByteArray();
		entity.addPart(Constants.PARAM_POST_IMAGE, new ByteArrayBody(ba,
				"x.png"));

		entity.addPart(Constants.PARAM_POST_DESC,
				new StringBody(post.getDescription()));
		entity.addPart(Constants.PARAM_POST_TAG, new StringBody(post.getTag()));
		entity.addPart(Constants.PARAM_POST_SEVERITY,
				new StringBody(post.getSeverity() + ""));
		entity.addPart(Constants.PARAM_POST_UID, new StringBody(CfManager
				.getInstantce().getUid() + ""));
		entity.addPart(Constants.PARAM_POST_LAT, new StringBody(post.getLat()
				+ ""));
		entity.addPart(Constants.PARAM_POST_LNG, new StringBody(post.getLng()
				+ ""));
		entity.addPart(Constants.PARAM_POST_CATEGORY,
				new StringBody(post.getCategory() + ""));

		entity.addPart(Constants.PARAM_POST_ADDRESS,
				new StringBody(post.getAddress()));

		httppost.setEntity(entity);

		HttpResponse httpresponse = httpclient.execute(httppost);

		HttpEntity responseentity = httpresponse.getEntity();

		if (responseentity != null) {

			// To check the response from server
			String result = EntityUtils.toString(responseentity);

			GsonBuilder gsonB = new GsonBuilder();
			gsonB.registerTypeAdapter(Post.class, new PostDeserializer());

			Gson gson = gsonB.create();
			try {
				Post dbPost = gson.fromJson(result, Post.class);
				
				if (dbPost != null) {
					DbManager.getInstance().savePosts(dbPost);
					isSuccess = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return isSuccess;
	}
}
