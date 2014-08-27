package com.zmax.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.zmax.app.R;
import com.zmax.app.model.ActDetailContent;
import com.zmax.app.ui.ActDetailActivity;
import com.zmax.app.ui.ActDetailActivity.RefreshDataCallBack;

public class ActDetailFirstFragment extends Fragment implements RefreshDataCallBack {
    private android.os.Handler handler = new android.os.Handler();
    private TextView tv_city, tv_date;
    private String city, date;

    private View rl_content, iv_bottom;

    public ActDetailFirstFragment() {

        setRetainInstance(true);
    }

    @Override
    public void onDataRefresh(ActDetailContent detailContent) {
        if (detailContent == null) return;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAnimation();
            }
        }, 133)
        ;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.act_detail_first, null);
        tv_city = (TextView) v.findViewById(R.id.tv_city);
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        rl_content = (View) v.findViewById(R.id.rl_content);
        iv_bottom = (View) v.findViewById(R.id.iv_bottom);
        iv_bottom.setVisibility(View.GONE);
        rl_content.setVisibility(View.GONE);
        if (getArguments().isEmpty()) {
            city = savedInstanceState.getString("city");
            date = savedInstanceState.getString("date");
        } else {
            city = getArguments().getString("city");
            date = getArguments().getString("date");

        }

        tv_city.setText("" + city);
        tv_date.setText("" + date);
        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("date", date);
        outState.putString("city", city);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getAnimation() {
        iv_bottom.setVisibility(View.VISIBLE);
        rl_content.setVisibility(View.VISIBLE);
        Animation contentAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_top_in);
        rl_content.startAnimation(contentAnimation);

        Animation bottomAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_top_out);
        iv_bottom.startAnimation(bottomAnimation);
        bottomAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ((ActDetailActivity) getActivity()).hideOrShowPointer(false);

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                iv_bottom.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
	                    if(getActivity()!=null)
                        ((ActDetailActivity) getActivity()).hideOrShowPointer(true);
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
