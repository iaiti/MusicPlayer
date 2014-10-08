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
	int status; // 状态
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
		// 播放路径
		seekbarprogress = intent.getIntExtra("seekbarprogress", -1);
		// 接收不同的按钮请求
		switch (intent.getIntExtra("cmd", -1)) {
		case Constant.back:// 上一首
			songnumber--;
			ensuresongs();
			status = Constant.playstatus;
			Log.d("songn", String.valueOf(songnumber));
			playsong(context);
			int i =1;
			break;

		case Constant.next:// 下一首
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

		case Constant.pause:// 收到暂停命令
			status = Constant.pausestatus;
			mp3.pause();// 暂停播放
			break;

		case Constant.stop:// 收到停止命令
			status = Constant.stopstatus;// 更新状态值到停止状态
			mp3.stop();// 停止播放
			break;
		}
		// 更新界面
		if(seekbarprogress!= -1){
			mp3.seekTo(seekbarprogress);
		}
		updateProgress(context);
	}

	//状态更新
	public void updateProgress(Context context) {
		// 创建Action为Constant.updatestatus的Intent
		//报media的错误
		int i = 1;
		Intent intent = new Intent(Constant.updateaction);
		// 将更新后的新状态值放到intent中 以便控制按钮的状态
		intent.putExtra("status", status);
		intent.putExtra("songname", filenames.get(songnumbertemp));
		context.sendBroadcast(intent);
	}

	//音乐播放方法封装
	public void playsong(Context c) {
		final Context context = c;
		if (songnumber != -1) {
			songnumbertemp = songnumber;
			if (mp3 != null) {
				mp3.release(); // 已有播放器则释放
			}
			try {
				mp3 = new MediaPlayer();
				mp3.setDataSource(playpath + "/" + filenames.get(songnumber));
				// 进行播放前的准备工作，new方式创建的MediaPlayer一定需要prepare否则报错 这个还真不知道
				mp3.prepare();
				duration = mp3.getDuration();
				mp3.start();
			} catch (Exception e) {
				System.out.println("media erro!");
			}
			
			//单首歌曲播放完毕跳转到下一首
			mp3.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer arg0) {
					arg0.stop();// 歌曲播放结束停止 恢复到初始状态
					songnumber++;
					ensuresongs();
					status = Constant.playstatus;
					Log.d("songn", String.valueOf(songnumber));
					playsong(context);
				}
			});
			new Thread() {// 启动一个线程不断更新播放进度
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
						if (status == Constant.playstatus) {// 若当前为播放
							current = mp3.getCurrentPosition();
						}
						if (status == Constant.pausestatus) {// 暂停态则 获取总时间并且跳转到相应的进度
							if(seekbarprogress!= -1){
								mp3.seekTo(seekbarprogress);
							}
							current = mp3.getCurrentPosition();
						}
						if (status == Constant.stopstatus) {
							duration = 0;
							current = 0;
						}
						// 创建Action为Constant.updatestatus（更新界面）的Intent
						Intent intent = new Intent(Constant.updateaction);
						// 将更新后的新状态值放到intent中
						intent.putExtra("status", Constant.update);
						intent.putExtra("duration", duration);
						intent.putExtra("current", current);
						//这里的songnumber变成-1  最后一个功能就是卡在这里的
						//System.out.println(songnumbertemp);
						intent.putExtra("songname", filenames.get(songnumbertemp));
						// 广播Intent
						context.sendBroadcast(intent);
						
					}
				}
			}.start();

		} else {
			mp3.start();// 若没有收到path说明是暂停后的继续播放 开始播放有传入播放的曲目
		}
	}
	
	//取模，保证歌曲的序号不会为负
	public void ensuresongs(){
		songnumber = (songnumber+totalsongs)%totalsongs;
	}

}
