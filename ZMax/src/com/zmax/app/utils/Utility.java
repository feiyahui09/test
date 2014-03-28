package com.zmax.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utility {
	
	public static void goDialPhone(Context context, String phoNum) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoNum));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		
	}
}
