package com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist;

import java.io.File;
import java.io.FileFilter;

public class SuffixFileFilter implements FileFilter
{
	private String postfixString = "";
	
    public SuffixFileFilter(String postfix)
	{
		postfixString = postfix;
	}
	
	@Override
	public boolean accept(File pathname)
	{
		String filenameString = pathname.getName();
		if(filenameString.endsWith(postfixString)) 
		{
			return true;
		}
		return false;
	}

}
