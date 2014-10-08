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
	
	int status=Constant.stopstatus;	//��ʼ״̬Ϊֹͣ״̬
    Activity activity;	//��Ӧ��Activity
    String songname;
    //���캯�� ����
    public UIUpdate(Activity ac) {
    	this.activity=ac; 
    }
    
	@Override
	public void onReceive(Context context, Intent intent) {
		int tempStatus=intent.getIntExtra("status", -1);
		ImageButton play=(ImageButton)activity.findViewById(R.id.play);
		switch(tempStatus){
		  case Constant.playstatus://���� ��Ϊ��ͣͼƬ	 
			 play.setImageResource(android.R.drawable.ic_media_pause);	
			 status=tempStatus;
		  break;
		  
		  case Constant.stopstatus:  //ֹͣ����ͣ״̬ ��Ϊ����ͼƬ	
		  case Constant.pausestatus:
			  play.setImageResource(android.R.drawable.ic_media_play);	
			  status=tempStatus;
		  break;
		  
		  case Constant.update://���½��������Ѳ���ʱ������ʱ��͸�����
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
	
	//�������ĺ���ת����  mi:ss��ʽ��ʱ���ַ���
	public String fromMsToMinute(int ms){
		ms=ms/1000;
		int minute=ms/60;
		int second=ms%60;			
		return minute+":"+((second>9)?second:"0"+second);		
	}
}
