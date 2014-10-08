package com.ckc.musicplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Constant {
	//����
	public final static int play = 1;
	public final static int pause = 2;
	public final static int stop = 3;
	public final static int next = 4;
	public final static int back = 5;

	//״̬
	public final static int playstatus = 6;
	public final static int pausestatus = 7;
	public final static int stopstatus= 8;
	public final static int update = 9;
	
	//����intent�Ĳ�ͬ����
	public final static String controlaction = "action_control";
	public final static String updateaction = "action_update";
	
	//����·��
	public final static String playpath ="/sdcard"; 
	 //Ŀ¼��ȡ��  ��ʵǧǧ�����ľ���ֱ��ɨ�� �ø�������ʽƥ��
	
	 public static List<String> getfilenames(File file){
		 	String filename;
		 	List<String> filenamestemp = new ArrayList<String>();
		 	File[] fileArray =file.listFiles();
			for (File f : fileArray) {
				if(f.isFile()){
					filename = f.getName();
					//System.out.println(filename);
					//System.out.println("tests2");
					filenamestemp.add(filename);
				}
			}
			return filenamestemp;
	}

}
