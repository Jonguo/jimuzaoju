package com.xiaobawang.xiaoxue.yingyu.jimuzaoju.control;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import android.os.Environment;

import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist.FileNameFileFilter;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist.GlobalValue;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist.Tool;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.settings.ConstValues;

public class FileOperate
{
	private static FileOperate instance = null;
	
	private File[] mDownloadDirectorys = new File[GlobalValue.GradeNumbers];
	
	private FileOperate() throws Exception
	{
		init();
	}
	
	private void init() throws Exception
	{
		for(int i=0;i<GlobalValue.GradeNumbers;++i)
		{
			try
			{
				mDownloadDirectorys[i] = new File(GlobalValue.Internal_DownloadPaths[i]);
				makeDir(mDownloadDirectorys[i]);				
			} catch (Exception e)
			{
				throw new Exception("内存卡异常");
			}

		}
	}
	
	private void makeDir(File file)
	{
		if(!file.exists())
		{
			file.mkdirs();
		}
		else if(!file.isDirectory())
		{
			file.delete();
			file.mkdirs();
		}
	}
	
	public static FileOperate getInstance() throws Exception
	{
		if(instance == null)
		{
			synchronized (FileOperate.class)
			{
				if(instance == null)
					instance = new FileOperate();
			}
		}
		return instance;
	}
	
	public static boolean isExternalStorageWritable() 
	{
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state))
	    {
	        return true;
	    }
	    return false;
	}
	
	private void testStorage() throws Exception
	{
		if (!isExternalStorageWritable())
		{
			throw new Exception("内存卡异常");
		}		
	}
	
	public void getUnitName(List<String> UnitNames,int grade,String bookName) throws Exception
	{
		testStorage();
		UnitNames.clear();
		File bookFile = getBookFile(grade, bookName);
		readUnitName(UnitNames, bookFile);
	}
	
	private File getBookFile(int grade,String bookName) throws Exception
	{
		File[] bookFiles =  mDownloadDirectorys[grade].listFiles(new FileNameFileFilter(bookName + ConstValues.filePostfixString));
		
		if(bookFiles==null||bookFiles.length<=0||bookFiles[0].length()<=0)
		{
			throw new Exception("句库损坏");
		}
		File bookFile = bookFiles[0];
		return bookFile;
	}
	
	private void readUnitName(List<String> UnitNames,File bookFile)
	{
		RandomAccessFile raf = null;
		try
		{
			raf = new RandomAccessFile(bookFile, "r");
			raf.seek(4);
			byte[] b1 = new byte[8];
			raf.read(b1);
			int[] lengths = Tool.byteArrayToint(b1);
			int indexLength = lengths[0];
			int unitLength = lengths[1];
			byte[] b2 = new byte[unitLength];
			raf.seek(12+indexLength*4);
			raf.read(b2);
			String s= new String(b2,"UTF-8");
			String[] unitNameStrings = s.split("\r\n");
			
			for(int i=0;i<unitNameStrings.length;++i)
			{
				UnitNames.add(unitNameStrings[i]);
			}		
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void getBookName(List<String> BookNames,int grade) throws Exception
	{
		testStorage();
		BookNames.clear();
		try
		{
			File[] bookFiles = mDownloadDirectorys[grade].listFiles();
			if (bookFiles != null)
			{
				for (int i = 0; i < bookFiles.length; ++i)
				{
					BookNames.add(Tool.removeSuffix(bookFiles[i].getName()));
				}
			}		
		} catch (Exception e)
		{
			throw new Exception("文件异常");
		}

	}
	
	public void getSentence(List<String> sentences_english,List<String> sentences_chinese, String bookName,int grade,int index) throws Exception
	{
		File bookFile = getBookFile(grade, bookName);

		RandomAccessFile raf = null;
		try
		{
			raf = new RandomAccessFile(bookFile, "r");
			raf.seek(4);
			int indexlength = raf.readInt();
			
			byte[] b1 = new byte[indexlength*4];
			raf.seek(12);
			raf.read(b1);
			int[] unitIndexs = Tool.byteArrayToint(b1);
			
			long startPoint = unitIndexs[index];
			long length;
			if(index == unitIndexs.length-1)
			{
				length = bookFile.length() - startPoint;
			}
			else
			{
				length = unitIndexs[index+1] - startPoint;
			}
			raf.seek(startPoint);
			byte[] buffer = new byte[(int) length];
			raf.read(buffer);
			String s = new String(buffer,"UTF-8");
			String[] scentencestrings = s.split("\r\n");
			
			
			sentences_chinese.clear();
			sentences_english.clear();
			for(int i=0;i<scentencestrings.length;i+=2)
			{
				if(scentencestrings[i].length()>0)
				{
					sentences_english.add(scentencestrings[i]);
					sentences_chinese.add(scentencestrings[i+1]);
				}
			}
			
		
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
