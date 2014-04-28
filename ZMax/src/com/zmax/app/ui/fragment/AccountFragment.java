package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.utils.Constant;
import com.zmax.app.utils.Utility;
import com.zmax.app.widget.HTML5WebView;

public class AccountFragment extends Fragment {
	
	private HTML5WebView wv_content;
	
	public AccountFragment() {
		this(R.color.white);
	}
	
	public AccountFragment(int colorRes) {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		wv_content = new HTML5WebView(getActivity());
		return wv_content.getLayout();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		wv_content.loadUrl(Constant.WAP.URL_MENBER);
		wv_content.requestFocus(View.FOCUS_DOWN);
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
}
