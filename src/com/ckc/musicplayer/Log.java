package com.ckc.musicplayer;


import com.example.musicplayer.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
//����ͼƬ��ʾ
public class Log extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		ImageView iv = (ImageView)findViewById(R.id.welcome);
		AlphaAnimation aa = new AlphaAnimation(1f, 1f);
		//��������1.5��
		aa.setDuration(1500);
		iv.startAnimation(aa);
		
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent it = new Intent(Log.this,MusicPlayerActivity.class);
				startActivity(it);
				//1.5���ر�
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
