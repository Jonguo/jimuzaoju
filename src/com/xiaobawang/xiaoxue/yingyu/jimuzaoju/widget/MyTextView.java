package com.xiaobawang.xiaoxue.yingyu.jimuzaoju.widget;



import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;



public class MyTextView extends TextView
{

	private static final int INTERVAL = 4;
    private long lastDownTime = 0;
    private long lastUpTime = 0;
    private MotionEvent lastEvent = null;
    private static String INTERVAL_STRING = null;
    private Context context;
    private boolean setSelect = false;
    
    private String mBroadcastAction = null;
	public MyTextView(Context context)
	{
		super(context);
		init();
		this.context = context;
	}
	
	public MyTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
		this.context = context;
	}
	
	public MyTextView(Context context, AttributeSet attrs, int i)
	{
		super(context, attrs, i);
		init();
		this.context = context;
	}

	private void init()
	{
		if(INTERVAL_STRING!=null) return;
		synchronized (MyTextView.class)
		{
			if (INTERVAL_STRING == null)
			{
				INTERVAL_STRING="";
				for (int i = 0; i < INTERVAL; ++i)
				{
					INTERVAL_STRING += ' ';
				}
			}		
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		System.out.println("MyTextView onTouchEvent");
		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP:
			System.out.println("Action_up"+event.getX()+" "+ event.getY());
			 lastUpTime = event.getEventTime();
			 if(event.getEventTime() - lastDownTime < 500)
			 dealClick(event);
			 return super.onTouchEvent(event);
		case MotionEvent.ACTION_DOWN:
			System.out.println("Action_down"+event.getX()+" "+ event.getY());
			lastDownTime = event.getEventTime();
			 if(event.getEventTime() - lastUpTime > 400)
				return super.onTouchEvent(event);
			 return true;	
		default:
			return super.onTouchEvent(event);
		}
	}
	

	
	private void dealClick(MotionEvent event)
	{
		
		System.out.println("dealClick");
		int line = getLayout().getLineForVertical(getScrollY()+(int)event.getY());
		 int clickPoint = getLayout().getOffsetForHorizontal(line, event.getX());
		 String s = getSelect(clickPoint);
		 System.out.println(s);
		 sendBroadcastToMain(s);
	}
	
	private String splitText(String word)
	{
		String[] words = word.split(" ");
		for(int i=0;i<words.length;++i)
		{
			if(words[i]!=null&&words[i].length()>0)
				return words[i];
		}
		return "";
	}
	
	public String getSelect(int clickPoint)
	{
		int s = findStartPoint(clickPoint),
			e = findEndPoint(clickPoint);
		
		if(!outOfIndex(s, getText().toString().length()) && !outOfIndex(e, getText().toString().length()))	
		{
		String s1 =	this.getText().toString().substring(s,e+1);
		s1 = splitText(s1);
		return s1;
		}
		return "";
	}
	
	private int findStartPoint(int clickPoint)
	{
		System.out.println(clickPoint);
		String s = this.getText().toString();
		if(outOfIndex(clickPoint, s.length()))
			return -1;
		for(int i=0;;i++)
		{
			if(clickPoint-i<=0)
			{ 
				return clickPoint - i;
			}
			else if(!outOfIndex(clickPoint-i,s.length())&&s.charAt(clickPoint - i)!=' ')
			{
				if(i>=INTERVAL/2+1)
				return clickPoint - i + INTERVAL;
				else 
				{
					while(!outOfIndex(clickPoint-i,s.length())&&s.charAt(clickPoint - i)!=' ')
					{
						i++;
					}
					return clickPoint - i + 1;
				}
			}
		}
	}
	
	private int findEndPoint(int clickPoint)
	{
		String s = this.getText().toString();
		if(outOfIndex(clickPoint, s.length()))
			return -1;
		for(int i=0;;++i)
		{
			if(clickPoint+i>=s.length())
			{
				return clickPoint+i-1;
			}
			else if(!outOfIndex(clickPoint+i,s.length())&&s.charAt(clickPoint + i)!=' ')
			{
				if(i>=INTERVAL)
				{
					return clickPoint + i -INTERVAL;
				}
				else 
				{
					while(!outOfIndex(clickPoint+i,s.length())&&s.charAt(clickPoint + i)!=' ')
					{
						i++;
					}
					return clickPoint + i - 1;					
				}
			}
		}
	}
	
	public void setText(List<String> texts)
	{
		String text = "";
		if(texts.size()<=0) 
		{
			setText("");
			return;
		}
		for(int i=0;i<texts.size()-1;++i)
		{
			text = text + texts.get(i) + INTERVAL_STRING;
		}
		text=text + texts.get(texts.size()-1);
		this.setText(text);
	}
	
	private void sendBroadcastToMain(String string)
	{
		Intent intent = new Intent(mBroadcastAction);
		Bundle bundle = new Bundle();
		bundle.putString("word", string);
		intent.putExtras(bundle);
		context.sendBroadcast(intent);
	}
	
	public void setBoradcastActionName(String string)
	{
		mBroadcastAction = string;
	}
	
	private boolean outOfIndex(int index,int length)
	{
		if(index>=0&&index<length)
			return false;
		return true;
	}
	
}
