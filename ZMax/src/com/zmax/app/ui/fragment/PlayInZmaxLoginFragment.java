package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.zmax.app.R;
import com.zmax.app.model.Login;
import com.zmax.app.task.LoginPlayZmaxTask;
import com.zmax.app.ui.MainActivity;

public class PlayInZmaxLoginFragment extends Fragment {
	
	private View view;
	private Button btn_login;
	private Spinner sp_hotels;
	private LoginPlayZmaxTask loginPlayZmaxTask;
	
	public PlayInZmaxLoginFragment() {
		this(R.color.white);
	}
	
	public PlayInZmaxLoginFragment(int colorRes) {
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.playzmax_login, null);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goPlayZmax();
			}
		});
		
		sp_hotels = (Spinner) view.findViewById(R.id.sp_hotels);
		fromHotelsSpinner(sp_hotels);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}
	
	private void goPlayZmax() {
		loginPlayZmaxTask = new LoginPlayZmaxTask(getActivity(), new LoginPlayZmaxTask.TaskCallBack() {
			@Override
			public void onCallBack(Login loginResult) {
				if (loginResult != null && loginResult.status == 200) {
					((MainActivity) getActivity()).switchContent(new PlayInZmaxFragment(loginResult));
				}
			}
		});
		loginPlayZmaxTask.execute();
		
	}
	
	private void fromHotelsSpinner(Spinner spinner) {
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.playzmax_login_spinner_item, mStrings);
		adapter.setDropDownViewResource(R.layout.playzmax_login_dropdown_spinner_item);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0) ((TextView) view).setText("" + adapter.getItem(position));
				
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				((TextView) view).setText("选择入住酒店");
				
			}
		});
		
		spinner.setAdapter(adapter);
		
	}
	
	private static final String[] mStrings = { "选择入住酒店", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune" };
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
}
