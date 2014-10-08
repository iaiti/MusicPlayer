package com.ckc.musicplayer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class ControlReceiver extends BroadcastReceiver {
	
	MediaPlayer mp3;
	int duration;
	int status; // ״̬
	int current ;
	int songnumber = 0;
	int songnumbertemp = 0;
	String playpath = Constant.playpath;
	File f = new File(playpath);
	List<String> filenames= Constant.getfilenames(f);;
	int totalsongs = filenames.size();
	int seekbarprogress;
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		// ����·��
		seekbarprogress = intent.getIntExtra("seekbarprogress", -1);
		// ���ղ�ͬ�İ�ť����
		switch (intent.getIntExtra("cmd", -1)) {
		case Constant.back:// ��һ��
			songnumber--;
			ensuresongs();
			status = Constant.playstatus;
			Log.d("songn", String.valueOf(songnumber));
			playsong(context);
			int i =1;
			break;

		case Constant.next:// ��һ��
			songnumber++;
			ensuresongs();
			status = Constant.playstatus;
			Log.d("songn", String.valueOf(songnumber));
			playsong(context);
			break;
			
		case Constant.play:
			songnumber = intent.getIntExtra("song", -1);
			status = Constant.playstatus;
			playsong(context);
			break;

		case Constant.pause:// �յ���ͣ����
			status = Constant.pausestatus;
			mp3.pause();// ��ͣ����
			break;

		case Constant.stop:// �յ�ֹͣ����
			status = Constant.stopstatus;// ����״ֵ̬��ֹͣ״̬
			mp3.stop();// ֹͣ����
			break;
		}
		// ���½���
		if(seekbarprogress!= -1){
			mp3.seekTo(seekbarprogress);
		}
		updateProgress(context);
	}

	//״̬����
	public void updateProgress(Context context) {
		// ����ActionΪConstant.updatestatus��Intent
		//��media�Ĵ���
		int i = 1;
		Intent intent = new Intent(Constant.updateaction);
		// �����º����״ֵ̬�ŵ�intent�� �Ա���ư�ť��״̬
		intent.putExtra("status", status);
		intent.putExtra("songname", filenames.get(songnumbertemp));
		context.sendBroadcast(intent);
	}

	//���ֲ��ŷ�����װ
	public void playsong(Context c) {
		final Context context = c;
		if (songnumber != -1) {
			songnumbertemp = songnumber;
			if (mp3 != null) {
				mp3.release(); // ���в��������ͷ�
			}
			try {
				mp3 = new MediaPlayer();
				mp3.setDataSource(playpath + "/" + filenames.get(songnumber));
				// ���в���ǰ��׼��������new��ʽ������MediaPlayerһ����Ҫprepare���򱨴� ������治֪��
				mp3.prepare();
				duration = mp3.getDuration();
				mp3.start();
			} catch (Exception e) {
				System.out.println("media erro!");
			}
			
			//���׸������������ת����һ��
			mp3.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer arg0) {
					arg0.stop();// �������Ž���ֹͣ �ָ�����ʼ״̬
					songnumber++;
					ensuresongs();
					status = Constant.playstatus;
					Log.d("songn", String.valueOf(songnumber));
					playsong(context);
				}
			});
			new Thread() {// ����һ���̲߳��ϸ��²��Ž���
				public void run() {
					looper: while (true) {
						if (status == Constant.stopstatus) {
							current = 0;
							break looper;
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (status == Constant.playstatus) {// ����ǰΪ����
							current = mp3.getCurrentPosition();
						}
						if (status == Constant.pausestatus) {// ��̬ͣ�� ��ȡ��ʱ�䲢����ת����Ӧ�Ľ���
							if(seekbarprogress!= -1){
								mp3.seekTo(seekbarprogress);
							}
							current = mp3.getCurrentPosition();
						}
						if (status == Constant.stopstatus) {
							duration = 0;
							current = 0;
						}
						// ����ActionΪConstant.updatestatus�����½��棩��Intent
						Intent intent = new Intent(Constant.updateaction);
						// �����º����״ֵ̬�ŵ�intent��
						intent.putExtra("status", Constant.update);
						intent.putExtra("duration", duration);
						intent.putExtra("current", current);
						//�����songnumber���-1  ���һ�����ܾ��ǿ��������
						//System.out.println(songnumbertemp);
						intent.putExtra("songname", filenames.get(songnumbertemp));
						// �㲥Intent
						context.sendBroadcast(intent);
						
					}
				}
			}.start();

		} else {
			mp3.start();// ��û���յ�path˵������ͣ��ļ������� ��ʼ�����д��벥�ŵ���Ŀ
		}
	}
	
	//ȡģ����֤��������Ų���Ϊ��
	public void ensuresongs(){
		songnumber = (songnumber+totalsongs)%totalsongs;
	}

}
