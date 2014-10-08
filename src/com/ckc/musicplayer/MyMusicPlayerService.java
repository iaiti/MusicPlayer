package com.ckc.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

//��̨���ŵ�Service��
public class MyMusicPlayerService extends Service {
	ControlReceiver cr;// ����Intent�����߶�������

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		cr = new ControlReceiver();
		// ����ý�岥��������
		cr.mp3 = new MediaPlayer();
		// ��ʼ״̬Ϊֹͣ״̬
		cr.status = Constant.stopstatus;
		// ��̬ע��㲥 ���ղ��š���ͣ��ֹͣ����Intent��CommandReceiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.controlaction);
		this.registerReceiver(cr, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ȡ��ע����ղ��š���ͣ��ֹͣ����Intent��CommandReceiver
		this.unregisterReceiver(cr);
		// �ͷŲ���������
		cr.mp3.release();
	}

	@Override
	public void onStart(Intent intent, int id) {
		cr.updateProgress(this.getApplicationContext());
	}
}
