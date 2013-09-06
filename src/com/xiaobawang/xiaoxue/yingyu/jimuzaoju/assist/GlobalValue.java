package com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist;

import java.util.LinkedList;
import java.util.List;

import android.widget.Toast;

public class GlobalValue
{
	public static Toast toastOnlyOne = null;
	public static int currentGrade = 0;
	public static String currentBook = null;
	public static int GradeNumbers = 6;
	
	public static final String INTERNAL_SDCARD = "/mnt/sdcard/";
	public static final String EXTERNAL_SDCARD = "/mnt/external_sd/";
	
	

	
	public static List<String> unitNames = new LinkedList<String>();
	
	public static List<String> BookNames = new LinkedList<String>();
	
	public static List<String> sentences_english = new LinkedList<String>();
	public static List<String> sentences_chinese = new LinkedList<String>();
	
	public static String rootPathString = "xiaobawang/xiaoxue/yingyu/jimuzaoju";
	
	public static String[] EXternal_DownloadPaths = new String[GradeNumbers];
	public static String[] Internal_DownloadPaths = new String[GradeNumbers];
	
	
	
}
