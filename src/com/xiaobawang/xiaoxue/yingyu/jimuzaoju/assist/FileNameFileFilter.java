package com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist;

import java.io.File;
import java.io.FileFilter;

public class FileNameFileFilter implements FileFilter
{
	private String fileName = "";
	
    public FileNameFileFilter(String fileName)
	{
		this.fileName = fileName;
	}
	
	@Override
	public boolean accept(File pathname)
	{
		String filenameString = pathname.getName();
		if(filenameString.equals(fileName)) 
		{
			return true;
		}
		return false;
	}

}
