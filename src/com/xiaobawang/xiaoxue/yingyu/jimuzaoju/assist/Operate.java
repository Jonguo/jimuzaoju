package com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.xiaobawang.qita.geren.api.UserInfo;
import com.xiaobawang.qita.geren.api.UserInfoHelper;


public class Operate
{
	private static Operate instance = null;
	
	private Operate(){};
	
	public static Operate getInstance()
	{
		if(instance == null)
		{
			synchronized (Operate.class)
			{
				if(instance==null)
					instance = new Operate();
			}
		}
		return instance;
	}
	
	public void cancelToast()
	{
		if(GlobalValue.toastOnlyOne!=null)
		     GlobalValue.toastOnlyOne.cancel();		
	}
	
	public void showToast(String string,Context context)
	{
		cancelToast();
		GlobalValue.toastOnlyOne = Toast.makeText(context, string, Toast.LENGTH_SHORT);
		GlobalValue.toastOnlyOne.show();
	}

	public void setGrade(Activity activity)
	{
		UserInfoHelper myHelper = new UserInfoHelper(activity);
		UserInfo userInfo = myHelper.getCurrentUserInfo();
		if(userInfo !=null)
		{
			GlobalValue.currentGrade = userInfo.getGrade();
			GlobalValue.currentGrade -= 1;
			GlobalValue.currentGrade = GlobalValue.currentGrade%6;
		}		
	}	
	
}
