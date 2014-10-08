package com.ckc.musicplayer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.musicplayer.R;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MusicPlayerActivity extends Activity {
	
	AudioManager am ;
	int defaultsong ;//Ĭ�ϲ��ŵĸ�������һ��ʼֱ�ӵ�����Ű�ť
	String playpath = Constant.playpath; //����·��
	List<String> filenames;
	UIUpdate uiupdate;
	ImageButton stop;
	ImageButton play;
	ImageButton next;
	ImageButton back;
	SeekBar sb;
	int i = 0;
	//�Զ���˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,1,1,"�˳�"	);
		return super.onCreateOptionsMenu(menu);
	}

	//�˵����¼���
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() ==1){
			//����ֹͣ��
			Intent intent=new Intent(Constant.controlaction);
			intent.putExtra("cmd", Constant.stop);
			MusicPlayerActivity.this.sendBroadcast(intent);
			//��̨����ֹͣ
			Intent serviceintent = new Intent(this,MyMusicPlayerService.class);
			stopService(serviceintent);
			//״̬��ȡ��
			NotificationManager notificationManager = (NotificationManager) this   
                    .getSystemService(NOTIFICATION_SERVICE);   
            notificationManager.cancel(0);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	

	//����������ʾ��֪ͨ��
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try
        {
    		//��ȡ֪ͨ������  
        	NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        	Intent intent = new Intent(this,MusicPlayerActivity.class);
        	PendingIntent pi = PendingIntent.getActivity(this,0, intent, 0);
	    	
        	//����Notification
        	Notification myNotification = new Notification();
        	//����Notification��ͼ��
	    	myNotification.icon = R.drawable.music;
	    	//����֪ͨ����ʾ����Ϣ����������PendingIntent����ʵ���������������activity��
	    	myNotification.setLatestEventInfo(this, "T-T����������", "����о�ϲ", pi);
	    	//����Notification
	    	nm.notify(0,myNotification);
        }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musicplayer_main);
		//��ȡimagebutton�����úͲ��Ž���UIUpdate�ĳ�ʼ��
		
		play = (ImageButton)findViewById(R.id.play);
		stop = (ImageButton)findViewById(R.id.stop);
		back = (ImageButton)findViewById(R.id.back);
		next = (ImageButton)findViewById(R.id.next);
		uiupdate = new UIUpdate(this);
		sb = (SeekBar)findViewById(R.id.seekBar1);
		//�����б�
		File f = new File(playpath);
		filenames = Constant.getfilenames(f);
		
		ListView listview = (ListView)findViewById(R.id.listView1);
		listview.setAdapter(new ArrayAdapter<String>(this, 
							android.R.layout.simple_expandable_list_item_1,
							filenames));
		defaultsong = 0;//Ĭ�ϲ����б��һ�׸�
		
		//seekbar�ļ���
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Intent intent = new Intent(Constant.controlaction);
				intent.putExtra("seekbarprogress",sb.getProgress() );
				sendBroadcast(intent);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		});
		//�б�������
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					Intent intent=new Intent(Constant.controlaction);
					intent.putExtra("cmd", Constant.play);
					intent.putExtra("song", arg2);
					MusicPlayerActivity.this.sendBroadcast(intent);
			}
		});

		//ֹͣ�����Intent
		stop.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {				
							Intent intent=new Intent(Constant.controlaction);
							intent.putExtra("cmd", Constant.stop);
							MusicPlayerActivity.this.sendBroadcast(intent);
					}        		
	        	}
	    );
	        
	    //���� ��ͣ��ť��Ӽ�����
        play.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					if(uiupdate.status==Constant.playstatus){
						Intent intent=new Intent(Constant.controlaction);
						intent.putExtra("cmd", Constant.pause);
						MusicPlayerActivity.this.sendBroadcast(intent);
					}
					else if(uiupdate.status==Constant.stopstatus){
						Intent intent=new Intent(Constant.controlaction);
						intent.putExtra("cmd", Constant.play);
						intent.putExtra("song", defaultsong);
						MusicPlayerActivity.this.sendBroadcast(intent);
					} else {//����ǰΪ��ͣ״̬�򷢳�������������
						Intent intent=new Intent(Constant.controlaction);
						intent.putExtra("cmd", Constant.play);
						MusicPlayerActivity.this.sendBroadcast(intent);
					}
				}        		
           });
        
        next.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		//System.out.println("next");
	        		Intent intent=new Intent(Constant.controlaction);
					intent.putExtra("cmd", Constant.next);
					MusicPlayerActivity.this.sendBroadcast(intent);
        		
        	}        		
        });
        back.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        			Intent intent=new Intent(Constant.controlaction);
        			intent.putExtra("cmd", Constant.back);
        			MusicPlayerActivity.this.sendBroadcast(intent);
        	}        		
        });
	        
	    //��̬ע��㲥   ����UIUpdate ��ʱ��任
        IntentFilter filter=new IntentFilter();
		filter.addAction(Constant.updateaction);
		this.registerReceiver(uiupdate, filter);	
		this.startService(new Intent(this,MyMusicPlayerService.class));
	}

	
	//test listview
/*	private List<String> getSongs(){
		List<String> songs = new ArrayList<String>();
		songs.add("С�Ǵ���");
		songs.add("wait for you");
		return songs;
	}*/
	
	//���ؼ��˳�
	 public boolean onKeyDown( int keyCode,KeyEvent keyevent){
		 am =(AudioManager)getSystemService(Service.AUDIO_SERVICE);
	    if(keyCode == 4){
	    	System.exit(0);
	    	return true;
	    }
	    else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
	    	 am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
	    			 AudioManager.FLAG_SHOW_UI);
	    	 return true;
	    }
	     
	     if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
	    	 am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
	    			 AudioManager.FLAG_SHOW_UI);
	    	 return true; 
	     }
	    else
	    return super.onKeyDown(keyCode, keyevent);   
	 }
}
