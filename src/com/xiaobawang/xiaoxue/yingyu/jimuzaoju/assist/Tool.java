package com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist;


import java.io.File;
import java.util.Random;

public class Tool
{
	public static File findFile(File[] filelist, String name)
	{
		for (int i = 0; i < filelist.length; ++i)
		{
			if (filelist[i].getName().equals(name))
				return filelist[i];
		}
		assert false;
		return null;
	}

	public static int byteToint(byte[] b)
	{
		int a = 0;
		int t;
		for (int i = 0; i < 4; ++i)
		{
			t = (int) b[i];
			t = t & 0xff;
			a = (a << 8) + t;
		}
		return a;
	}

	public static int[] byteArrayToint(byte[] b)
	{
		int[] a = new int[b.length / 4];
		byte[] t = new byte[4];
		for (int i = 0; i < a.length; ++i)
		{
			for (int j = 0; j < 4; ++j)
			{
				t[j] = b[j + i * 4];
			}
			a[i] = byteToint(t);
		}
		return a;
	}
	public static String removeSuffix(String fileName)
	{
		String s = "";
		int pos = -1;
		for(int i=fileName.length()-1;i>=0;--i)
		{
			if(fileName.charAt(i) == '.') 
			{
				pos = i-1;
				break;
			}
		}
		for(int i=pos;i>=0;--i)
		{
			s = fileName.charAt(i) + s;
		}
		return s;
	}
	
	public static String removeBlankSpace(String word)
	{
		int s=0,e=word.length();
		for(int i=0;i<word.length();++i)
		{
			if(word.charAt(i)!=' ')
			{
				s = i;
				break;
			}
		}
		for(int i=word.length()-1;i>=0;--i)
		{
			if(word.charAt(i)!=' ')
			{
				e = i;
				break;
			}
		}
		if((s>=0&&s<word.length())&&((e+1)>s&&(e+1)<=word.length()))
		return	word.substring(s, e+1);
		return "";
	}
	
	public static int[] getRandomIndex(int maxValue)
	{
		int[] array = new int[maxValue];
		for(int i=0;i<array.length;++i)
		{
			array[i] = i;
		}
		if(maxValue<=1) return array;
		do
		{
		for(int i=0;i<array.length;++i)
		{
			int t1 = new Random().nextInt(array.length-i) + i;
			int v1 = array[i];
			array[i] = array[t1];
			array[t1] = v1;
		}
		}while(equalSequence(array));
		return array;
	}
	
	private static boolean equalSequence(int[] array)
	{
		for(int i=0;i<array.length;++i)
		{
			if(array[i]!=i)
				return false;
		}
		return true;
	}
	
}
