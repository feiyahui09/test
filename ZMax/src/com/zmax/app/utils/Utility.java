package com.zmax.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utility {
	
	public static void goDialPhone(Context context, String phoNum) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoNum));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		
	}
	
	public static String getDate() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
	public static int getRandom(int range) {
		Random random = new Random(System.currentTimeMillis());
		return random.nextInt(range);
	}
	
}
