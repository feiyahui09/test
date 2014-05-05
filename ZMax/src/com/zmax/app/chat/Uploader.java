package com.zmax.app.chat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.zmax.app.model.UploadResult;
import com.zmax.app.utils.JsonMapperUtils;
import com.zmax.app.utils.Log;

import android.util.Base64;

public class Uploader {
	private String url;
	private String fileName;
	
	public Uploader(String url, String fileName) {
		this.url = url;
		this.fileName = fileName;
		Log.i("urL: " + url);
		Log.i("fileName: " + fileName);
	}
	
	public UploadResult upload()  {
		UploadResult result = null;
		try {
			JSONObject jsonObject = constructPictureJson();
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpPost postMethod = new HttpPost(url);
			postMethod.setEntity(new StringEntity(jsonObject.toString()));
			postMethod.setHeader("Accept", "application/json");
			postMethod.setHeader("Content-type", "application/json");
			postMethod.setHeader("Data-type", "json");
			
			String jsonStr = httpClient.execute(postMethod, responseHandler);
			result = JsonMapperUtils.toObject(jsonStr, UploadResult.class);
			Log.i("result:  " + result);
		}
		catch (Exception e) {
			result = null;
			Log.e(e.toString());
		}
		return result;
	}
	
	public JSONObject constructPictureJson() throws JSONException, IOException {
		String[] file = fileName.split("/");
		JSONObject picturefile = new JSONObject();
		picturefile.put("original_filename", "base64:" + file[file.length - 1]);
		picturefile.put("filename", file[file.length - 1]);
		picturefile.put("file", encodePicture(fileName));
		JSONObject picture = new JSONObject();
		picture.put("picture", picturefile);
		
		return picture;
	}
	
	public String encodePicture(String fileName) throws IOException {
		File picture = new File(fileName);
		return Base64.encodeToString(FileUtils.readFileToByteArray(picture), Base64.DEFAULT);
	}
	
}
