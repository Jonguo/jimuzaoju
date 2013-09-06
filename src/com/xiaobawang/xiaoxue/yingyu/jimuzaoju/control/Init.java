package com.xiaobawang.xiaoxue.yingyu.jimuzaoju.control;

import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist.GlobalValue;


public class Init
{
	private static Init instance = null;
	
	private Init()
	{
	}
	
	public static Init getInstance()
	{
		if(instance == null)
		{
			synchronized (Init.class)
			{
				if(instance == null)
					instance = new Init();
			}
		}
		return instance;
	}	
	
	public void init()
	{
		for(int i=0;i<GlobalValue.GradeNumbers;++i)
		{
			GlobalValue.Internal_DownloadPaths[i] = GlobalValue.INTERNAL_SDCARD + GlobalValue.rootPathString + "/" + String.valueOf(i+1);
			GlobalValue.EXternal_DownloadPaths[i] = GlobalValue.EXTERNAL_SDCARD + GlobalValue.rootPathString + "/" + String.valueOf(i+1);
		}
	}
}
