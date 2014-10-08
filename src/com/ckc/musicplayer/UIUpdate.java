package com.ckc.musicplayer;


import com.example.musicplayer.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UIUpdate extends BroadcastReceiver{
	
	int status=Constant.stopstatus;	//初始状态为停止状态
    Activity activity;	//对应的Activity
    String songname;
    //构造函数 传参
    public UIUpdate(Activity ac) {
    	this.activity=ac; 
    }
    
	@Override
	public void onReceive(Context context, Intent intent) {
		int tempStatus=intent.getIntExtra("status", -1);
		ImageButton play=(ImageButton)activity.findViewById(R.id.play);
		switch(tempStatus){
		  case Constant.playstatus://播放 则为暂停图片	 
			 play.setImageResource(android.R.drawable.ic_media_pause);	
			 status=tempStatus;
		  break;
		  
		  case Constant.stopstatus:  //停止、暂停状态 改为播放图片	
		  case Constant.pausestatus:
			  play.setImageResource(android.R.drawable.ic_media_play);	
			  status=tempStatus;
		  break;
		  
		  case Constant.update://更新进度条及已播放时间与总时间和歌曲名
			  ProgressBar pb=(ProgressBar)activity.findViewById(R.id.seekBar1);
			  int duration=intent.getIntExtra("duration", 0);
			  int current=intent.getIntExtra("current", 0);	
			  songname = intent.getStringExtra("songname");
// System.out.println(songname);
			  pb.setMax(duration);
			  pb.setProgress(current);
			  TextView tv=(TextView)activity.findViewById(R.id.textView1);
			  
			  tv.setText(songname.substring(0, songname.length()-4)+"\n"+
			  fromMsToMinute(current)+"/"+fromMsToMinute(duration));
	      break;
		}
	}
	
	//将歌曲的毫秒转换成  mi:ss格式的时间字符串
	public String fromMsToMinute(int ms){
		ms=ms/1000;
		int minute=ms/60;
		int second=ms%60;			
		return minute+":"+((second>9)?second:"0"+second);		
	}
}
