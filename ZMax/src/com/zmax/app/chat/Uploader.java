package com.zmax.app.chat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;

import com.zmax.app.model.UploadResult;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.EncoderHandler;
import com.zmax.app.utils.JsonMapperUtils;
import com.zmax.app.utils.Log;

public class Uploader {
	private String url;
	private String fileName;
	
	private Bitmap bitmap;
	
	public Uploader(String url, Object object) {
		this.url = url;
		if (object instanceof String)
			this.fileName = (String) object;
		else if (object instanceof Bitmap) this.bitmap = (Bitmap) object;
		Log.i("urL: " + url);
		Log.i("fileName: " + fileName);
	}
	
	@Deprecated
	public UploadResult upload() {
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
			// auth
			String req_time = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("pms_hotel_id", Constant.getLogin().pms_hotel_id));
			params.add(new BasicNameValuePair("auth_time", req_time));
			params.add(new BasicNameValuePair("Zmax-Auth-Token", Constant.getLogin().auth_token));
			params.add(new BasicNameValuePair("publich_key", EncoderHandler.getPublicKey(Constant.getLogin().pms_hotel_id, req_time)));
			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			Log.d(url.toString());
			Log.d(params.toString());
			postMethod.setEntity(urlEncoded);
			
			String jsonStr = httpClient.execute(postMethod, responseHandler);
			result = JsonMapperUtils.toObject(jsonStr, UploadResult.class);
			Log.i("result:  " + result);
		}
		catch (Exception e) {
			result = null;
			Log.e(e.toString());
			e.printStackTrace();
		}
		return result;
	}
	
	public JSONObject constructPictureJson() throws JSONException, IOException {
		JSONObject picture = new JSONObject();
		
		if (!TextUtils.isEmpty(fileName)) {
			String[] file = fileName.split("/");
			JSONObject picturefile = new JSONObject();
			picturefile.put("original_filename", "base64:" + file[file.length - 1]);
			picturefile.put("filename", file[file.length - 1]);
			picturefile.put("file", encodePicture(fileName));
			picture.put("picture", picturefile);
		}
		else if (bitmap != null) {
			JSONObject picturefile = new JSONObject();
			picturefile.put("original_filename", "png");
			picturefile.put("filename", "png");
			picturefile.put("file", encodePicture(bitmap));
			picture.put("picture", picturefile);
		}
		return picture;
		
	}
	
	public String encodePicture(String fileName) throws IOException {
		File picture = new File(fileName);
		return Base64.encodeToString(FileUtils.readFileToByteArray(picture), Base64.DEFAULT);
	}
	
	public String encodePicture(Bitmap bitmap) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
	}
	
	public UploadResult callPost() {
		UploadResult uploadResult = null;
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder build = MultipartEntityBuilder.create();
		File file = new File(fileName);
		build.addPart("pic", new FileBody(file));
		build.addTextBody("pms_hotel_id", Constant.getLogin().pms_hotel_id);
		String req_time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		build.addTextBody("auth_time", req_time);
		build.addTextBody("Zmax-Auth-Token", Constant.getLogin().auth_token);
		build.addTextBody("publich_key", EncoderHandler.getPublicKey(Constant.getLogin().pms_hotel_id, req_time));
		HttpEntity ent = build.build();
		
		post.setEntity(ent);
		HttpResponse resp = null;
		try {
			resp = client.execute(post);
			HttpEntity resEnt = resp.getEntity();
			String result = EntityUtils.toString(resEnt);
			uploadResult = JsonMapperUtils.toObject(result, UploadResult.class);
			Log.i("result:  " + result);
		}
		catch (Exception e) {
			uploadResult = null;
			e.printStackTrace();
		}
		
		return uploadResult;
		
	}
}
