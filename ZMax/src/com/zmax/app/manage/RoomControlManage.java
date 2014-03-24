package com.zmax.app.manage;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zmax.app.R;

public class RoomControlManage {
	public static final int ROOM_CONTROL_LIGHTING = 1;
	public static final int ROOM_CONTROL_TV = 2;
	public static final int ROOM_CONTROL_AIR_CONDITION = 3;

	public static List<View> getAirconditionView(
			final FragmentActivity fragmentActivity, LayoutInflater inflater) {

		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater, ROOM_CONTROL_AIR_CONDITION));
		mList.add(geAirconditionBehind(inflater));

		return mList;
	}

	private static View geAirconditionBehind(LayoutInflater inflater) {
		final View view = inflater.inflate(
				R.layout.room_control_aircondition_behind, null);

		return view;
	}

	public static List<View> getLightingView(
			final FragmentActivity fragmentActivity, LayoutInflater inflater) {

		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater, ROOM_CONTROL_LIGHTING));
		mList.add(getLightingBehind(inflater));

		return mList;
	}

	private static View getLightingBehind(LayoutInflater inflater) {
		final View view = inflater.inflate(
				R.layout.room_control_lighting_behind, null);
		((RadioGroup) view.findViewById(R.id.rg_lighting_model))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rb_lighting:

							break;
						case R.id.rb_tv:

							break;
						case R.id.rb_reading:

							break;
						case R.id.rb_sleep:

							break;

						default:
							break;
						}
					}
				});
		((RadioButton) view.findViewById(R.id.rb_lighting)).setChecked(true);
		return view;
	}

	public static List<View> getTVView(final FragmentActivity fragmentActivity,
			LayoutInflater inflater) {

		List<View> mList = new ArrayList<View>();
		mList.add(getAbove(inflater, ROOM_CONTROL_TV));
		mList.add(getTVBehind(inflater));

		return mList;
	}

	private static View getAbove(LayoutInflater inflater, int select) {
		final View view = inflater.inflate(R.layout.room_control_above, null);
		ImageView big_icon = ((ImageView) view.findViewById(R.id.iv_big_logo));
		if (select == ROOM_CONTROL_TV)
			big_icon.setImageResource(R.drawable.room_control_above_tv);
		else if (select == ROOM_CONTROL_LIGHTING)
			big_icon.setImageResource(R.drawable.room_control_above_lighting);
		else if (select == ROOM_CONTROL_AIR_CONDITION)
			big_icon.setImageResource(R.drawable.room_control_above_aircondition);

		return view;
	}

	private static View getTVBehind(LayoutInflater inflater) {
		final LinearLayout ll_digital, ll_orient;
		RadioGroup rg_model;
		RadioButton rb_orient;
		final View view = inflater.inflate(R.layout.room_control_tv_behind,
				null);

		ll_digital = (LinearLayout) view.findViewById(R.id.ll_digital);
		ll_orient = (LinearLayout) view.findViewById(R.id.ll_orient);
		rg_model = ((RadioGroup) view.findViewById(R.id.rg_model));
		rb_orient = ((RadioButton) view.findViewById(R.id.rb_orient));

		rg_model.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_digital) {
					ll_digital.setVisibility(View.VISIBLE);
					ll_orient.setVisibility(View.GONE);
				} else if (checkedId == R.id.rb_orient) {
					ll_digital.setVisibility(View.GONE);
					ll_orient.setVisibility(View.VISIBLE);
				}
			}
		});

		rb_orient.setChecked(true);
		return view;
	}
}
