package com.zmax.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.ui.base.BaseActivity;

public class OrderFillActivity extends BaseActivity {
	public final static int PICK_CONTACT = 100;
	private Context mContext;
	
	private Button btn_Back, btn_Contacts;
	private TextView tv_title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_fill);
		init();
		initHeader();
		
	}
	
	private void init() {
		mContext = this;
		btn_Contacts = (Button) findViewById(R.id.btn_Contacts);
		btn_Contacts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType(ContactsContract.Contacts.CONTENT_TYPE);// vnd.android.cursor.dir/contact
				startActivityForResult(intent, PICK_CONTACT);
			}
		});
		
	}
	
	private void initHeader() {
		btn_Back = (Button) findViewById(R.id.btn_back);
		
		btn_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.order_fill));
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		
		switch (reqCode) {
			case (PICK_CONTACT):
				if (resultCode == Activity.RESULT_OK) {
					Uri contactData = data.getData();
					Cursor c = managedQuery(contactData, null, null, null, null);
					if (c.moveToFirst()) {
						String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
						String phoneNumber = null;
						if (hasPhone.equalsIgnoreCase("1")) {
							hasPhone = "true";
						}
						else {
							hasPhone = "false";
						}
						if (Boolean.parseBoolean(hasPhone)) {
							Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + 10086, null, null);
							while (phones.moveToNext()) {
								phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							}
							phones.close();
						}
					}
				}
				break;
		}
	}
}
