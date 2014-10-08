package com.ckc.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

//后台播放的Service类
public class MyMusicPlayerService extends Service {
	ControlReceiver cr;// 命令Intent接收者对象引用

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		cr = new ControlReceiver();
		// 创建媒体播放器对象
		cr.mp3 = new MediaPlayer();
		// 初始状态为停止状态
		cr.status = Constant.stopstatus;
		// 动态注册广播 接收播放、暂停、停止命令Intent的CommandReceiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.controlaction);
		this.registerReceiver(cr, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消注册接收播放、暂停、停止命令Intent的CommandReceiver
		this.unregisterReceiver(cr);
		// 释放播放器对象
		cr.mp3.release();
	}

	@Override
	public void onStart(Intent intent, int id) {
		cr.updateProgress(this.getApplicationContext());
	}
}
