package com.xiaobawang.xiaoxue.yingyu.jimuzaoju;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xiaobawang.colordict.api.TranslationUtil;
import com.xiaobawang.util.api.DownloadMoreUtil;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist.GlobalValue;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist.Operate;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.assist.Tool;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.control.FileOperate;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.control.Init;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.settings.ConstValues;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.widget.CloudDialog;
import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.widget.MyTextView;

public class MainActivity extends Activity
{
	
	private List<String> mTextCorrect = new LinkedList<String>();
	private List<String> mTextDestination = new LinkedList<String>();
	private List<String> mTextSource = new LinkedList<String>();
	//UI
	private Spinner mGradeSpinner ;
	private Spinner mBookSpinner ;
	private MyTextView mTextViewSource = null;
	private MyTextView mTextViewDestination = null;
	private ListView mListView = null;
	private Button mDownloadButton = null;
	private TextView mTextView_problem = null,
			mTextView_score = null;
	
	//Button
	private Button mSubmit = null;
	private Button mTranslate = null;
	private Button mAssist = null;
	private Button mLeft_arrow = null;
	private Button mRight_arrow = null;
	private Button mhelpButton = null;
	
	private List<String> mGradeNames = new ArrayList<String>();
	
	private ArrayAdapter<String> mListViewAdapter = null;
	ArrayAdapter<String> mBookAdapter = null;
	ArrayAdapter<String> mGradeAdapter = null;
	
	private int mCurrentProblem = -1;
	private int mCurrentUnit = -1;
	private int mScore = 0;
	private boolean[] mHaveScored;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		setListener();
		init();
		setUiSettings();
		Init.getInstance().init();
		startApp();
	}
	
	private void init()
	{
		registerBoradcastReceiver();
		initGradeName();
		Operate.getInstance().setGrade(MainActivity.this);
	}
	
	private void startApp()
	{
		mGradeSpinner.setSelection(GlobalValue.currentGrade);
		upDateBookName(GlobalValue.currentGrade);
		if(!GlobalValue.BookNames.isEmpty())
		{
			GlobalValue.currentBook = GlobalValue.BookNames.get(0);
			setUnitAndSentence(0, 0);
		}
	}
	
	private void findView()
	{
		mTextViewSource = (MyTextView)findViewById(R.id.activity_main_MyTextView_source);
		mTextViewDestination = (MyTextView)findViewById(R.id.activity_main_MyTextView_destination);
		mGradeSpinner = (Spinner)findViewById(R.id.activity_main_spinner);
		mBookSpinner = (Spinner)findViewById(R.id.activity_main_spinner_select_sentence_library);
		mListView = (ListView)findViewById(R.id.activity_main_listview);
		mDownloadButton = (Button)findViewById(R.id.acitivity_main_button_download);
		mSubmit = (Button)findViewById(R.id.activity_main_button_submit);
		mAssist = (Button)findViewById(R.id.activity_main_assist);
		mTranslate = (Button)findViewById(R.id.activity_main_translate);
		mLeft_arrow = (Button)findViewById(R.id.activity_main_left_arrow);
		mRight_arrow = (Button)findViewById(R.id.activity_main_right_arrow);
		mTextView_score=(TextView)findViewById(R.id.activity_main_textview_score);
		mTextView_problem = (TextView)findViewById(R.id.activity_main_textview_problem_number);
		mhelpButton = (Button)findViewById(R.id.activity_main_button_help);
	}
	
	private void setUiSettings()
	{
		mGradeAdapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,mGradeNames);
		mGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mGradeSpinner.setAdapter(mGradeAdapter);
		mGradeSpinner.setSelection(GlobalValue.currentGrade, true);
		
		mBookAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,GlobalValue.BookNames);
		mBookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		mBookSpinner.setAdapter(mBookAdapter);
		
		mListViewAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.marquee_textview_listview,GlobalValue.unitNames);
		mListView.setAdapter(mListViewAdapter);
		
		mTextViewSource.setBoradcastActionName(ConstValues.MyTextViewBoradcast_Action_source);
		mTextViewDestination.setBoradcastActionName(ConstValues.MyTextViewBoradcast_Action_destination);
		setClickable(mCurrentProblem);
	}
	
	private void setListener()
	{
		mGradeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{				
				if(arg2!=GlobalValue.currentGrade)
				{
					GlobalValue.currentGrade = arg2;
					upDateBookName(arg2);
					setScore(0);
					if(!GlobalValue.BookNames.isEmpty())
						setUnitAndSentence(0, 0);
					else
					{
						GlobalValue.unitNames.clear();
						upDateUnitName(GlobalValue.currentGrade, null);
						mTextCorrect.clear();
						mTextDestination.clear();
						mTextSource.clear();
						updateMyTextView();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
			
		});
		
		mDownloadButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				DownloadMoreUtil.open(MainActivity.this, GlobalValue.currentGrade+1);
			}
		});
		
		mBookSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{		
				
					System.out.println(GlobalValue.BookNames.get(arg2));
					
					if(!GlobalValue.BookNames.get(arg2).equals(GlobalValue.currentBook))
					{
						setScore(0);
					}
					GlobalValue.currentBook = GlobalValue.BookNames.get(arg2);
					upDateUnitName(GlobalValue.currentGrade, GlobalValue.currentBook);
					setUnitAndSentence(0,0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
			
		});
		
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				readSentence(arg2);
				setUnitAndSentence(arg2, 0);
			}
			
		});
		
		mSubmit.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				checkCorrect();
			}
		});
		
		mTranslate.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(mTextCorrect.isEmpty())
				{
					Operate.getInstance().showToast("没有句子", MainActivity.this);
				}
				else
				{
					System.out.println("currentproble"+mCurrentProblem);
					showDialog("翻译", "翻译:"+GlobalValue.sentences_chinese.get(mCurrentProblem));
				}
			}
		});
		
		mAssist.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				assist();
			}
		});
		
		mLeft_arrow.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				int p = mCurrentProblem- 1;
				if(p>=0&&p<GlobalValue.sentences_english.size())
				{
				setUnitAndSentence(mCurrentUnit, p);
				}
			}
		});
		mRight_arrow.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				int p = mCurrentProblem + 1;
				if(p>=0&&p<GlobalValue.sentences_english.size())
				setUnitAndSentence(mCurrentUnit, p);
			}
		});
		
//		View.OnLongClickListener longclickListener =  new View.OnLongClickListener()
//		{
//			
//			@Override
//			public boolean onLongClick(View v)
//			{
//				if(myText.hasSelection())
//				{
//					String s = myText.getText().toString();
//					s = s.substring(myText.getSelectionStart(), myText.getSelectionEnd());
//					Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
//					return false;
//				}
//				return false;
//			}
//			
//		}	
		mTextViewSource.setOnLongClickListener(new View.OnLongClickListener()
		{
			
			@Override
			public boolean onLongClick(View v)
			{
				if(mTextViewSource.hasSelection())
				{
					String s = mTextViewSource.getText().toString();
					s = s.substring(mTextViewSource.getSelectionStart(), mTextViewSource.getSelectionEnd());
					// TranslationUtil是封装了ColorDict的API的类，可以对任何文本进行跳查，width/height为-1时表示填充屏幕
					TranslationUtil.translate(MainActivity.this, s, false, -1, 400, Gravity.BOTTOM);					
				}
				return false;
			}
		});
		
		mTextViewDestination.setOnLongClickListener(new View.OnLongClickListener()
		{
			
			@Override
			public boolean onLongClick(View v)
			{
				if(mTextViewDestination.hasSelection())
				{
					String s = mTextViewDestination.getText().toString();
					s = s.substring(mTextViewDestination.getSelectionStart(),mTextViewDestination.getSelectionEnd());
					// TranslationUtil是封装了ColorDict的API的类，可以对任何文本进行跳查，width/height为-1时表示填充屏幕
					TranslationUtil.translate(MainActivity.this, s, false, -1, 400, Gravity.BOTTOM);					
				}
				return false;
			}
		});		
		
		mhelpButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, BriefIntroductionActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if(action.equals(ConstValues.MyTextViewBoradcast_Action_destination))
			{
				Bundle bundle = intent.getExtras();
				String value = bundle.getString("word", null);
				if(value!=null)
				{
				String word = Tool.removeBlankSpace(value);
				System.out.println(value + "BroadCast");
				moveWord(word, mTextDestination, mTextSource);
				}
			}
			else if(action.equals(ConstValues.MyTextViewBoradcast_Action_source))
			{
				Bundle bundle = intent.getExtras();
				String value = bundle.getString("word", null);
				if(value!=null)
				{
				String word = Tool.removeBlankSpace(value);
				System.out.println(value + "BroadCast");
				moveWord(word, mTextSource, mTextDestination);
				}
			}
		}
		
	};
	
	private	void moveWord(String word,List<String> from,List<String> to)
	{
		synchronized (MainActivity.this)
		{
			delete(word, from);
			add(word,to);
			updateMyTextView();			
		}
	}
	
	private void updateMyTextView()
	{
		mTextViewDestination.setText(mTextDestination);
		mTextViewSource.setText(mTextSource);
	}
	
	private void add(String word,List<String> list)
	{
		System.out.println("123456add" + "#" + word + "#");
		if(word.length()>0)
		{
		list.add(word);
		}
	}
	
	private void delete(String word,List<String> list)
	{
		System.out.println("123456delete" + "#" + word + "#");
		for(int i=0;i<list.size();++i)
		{
			if(list.get(i).equals(word))
			{
				list.remove(i);
				return ;
			}
		}		
	}
	
	private void splitSentence(String sentence,List<String> list)
	{
		if(!list.isEmpty())
			list.clear();
		String[] words = sentence.split(" ");
		for(int i=0;i<words.length;++i)
		{
			if(words[i].length()>0)
			{
				list.add(words[i]);
			}
		}
	}
	
	private void checkCorrect()
	{
		if(mTextCorrect.isEmpty())
		{
			Operate.getInstance().showToast("没有单词", MainActivity.this);
			return;
		}
		if(mTextCorrect.equals(mTextDestination))
		{
			
			addScore(true);
			System.out.println("答题正确!"+mScore);
			showDialog("提示", "答题正确!");
		}
		else
		{
			System.out.println("答题错误!"+mScore);
			addScore(false);
			showDialog("提示", "答题错误!");
		}
	}
	
	
	
	private void setUnitAndSentence(int unitIndex,int problemIndex)
	{
		
		if(unitIndex!=mCurrentUnit)
		{
			mCurrentUnit = unitIndex;
			readSentence(unitIndex);
			setScore(0);
				mHaveScored = new boolean[GlobalValue.sentences_english.size()];
				for(int i=0;i<mHaveScored.length;++i)
				{
					mHaveScored[i] = false;
				}
		}
		if(mCurrentProblem!=problemIndex)
		{
		mCurrentProblem = problemIndex;
		}
		setSentence(problemIndex);		
		setClickable(problemIndex);
		mTextView_problem.setText("第" + (problemIndex+1) + "题");
	}
	
	private void setScore(int score)
	{
		mScore = score;
		mTextView_score.setText(String.valueOf(mScore));
	}
	
	private void setClickable(int problemIndex)
	{
		if(problemIndex==0||problemIndex==-1)
		{
			mLeft_arrow.setClickable(false);
			mLeft_arrow.setBackgroundResource(R.drawable.activity_main_left_arrow_pressed);
		}
		if(problemIndex==GlobalValue.sentences_english.size()-1||problemIndex == -1)
		{
			mRight_arrow.setClickable(false);
			mRight_arrow.setBackgroundResource(R.drawable.activity_main_right_arrow_pressed);
		}
		if(problemIndex >0&&problemIndex<GlobalValue.sentences_english.size()) 
		{
			mLeft_arrow.setClickable(true);
			mLeft_arrow.setBackgroundResource(R.drawable.activity_main_left_arrow);
		}
		if(problemIndex<GlobalValue.sentences_english.size()-1&&problemIndex>=0)
		{
			mRight_arrow.setClickable(true);
			mRight_arrow.setBackgroundResource(R.drawable.activity_main_right_arrow);
		}
	}
	
	public void addScore(boolean b)
	{
		if(b&&!mHaveScored[mCurrentProblem])
		{
			mScore += 100/GlobalValue.sentences_english.size();
			if((100-mScore)<(100/GlobalValue.sentences_english.size()))
			{
				mScore = 100;
			}
			setScore(mScore);
		}
		mHaveScored[mCurrentProblem] = true;
	}
	
	@Override
	public void onDestroy()
	{
	    super.onDestroy();
	    this.unregisterReceiver(this.mBroadcastReceiver);
	}
	
	private void registerBoradcastReceiver()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConstValues.MyTextViewBoradcast_Action_destination);
		intentFilter.addAction(ConstValues.MyTextViewBoradcast_Action_source);
		registerReceiver(mBroadcastReceiver, intentFilter);
	}
	
	private void initGradeName()
	{
			mGradeNames.add("小学一年级");
			mGradeNames.add("小学二年级");
			mGradeNames.add("小学三年级");
			mGradeNames.add("小学四年级");
			mGradeNames.add("小学五年级");
			mGradeNames.add("小学六年级");
	}
	
	private void upDateUnitName(int grade,String bookName)
	{
		try
		{
			if(bookName==null)
			{
				GlobalValue.unitNames.clear();
			}
			else
			{
				FileOperate.getInstance().getUnitName(GlobalValue.unitNames, grade, bookName);
			}
			mListViewAdapter.notifyDataSetChanged();
		} catch (Exception e)
		{
			catchFileError(e);
		}
	}

	private void upDateBookName(int grade)
	{
		try
		{
			FileOperate.getInstance().getBookName(GlobalValue.BookNames, grade);
			if(GlobalValue.BookNames.isEmpty())
				Operate.getInstance().showToast("没有句库,请先下载",MainActivity.this);
			if(GlobalValue.BookNames.size()>0)
			{
				GlobalValue.currentBook = GlobalValue.BookNames.get(0);
			}
			else GlobalValue.currentBook = null;
			mBookAdapter.notifyDataSetChanged();
		} catch (Exception e)
		{
			catchFileError(e);
		}
	}
	 private Handler MyHandler = new Handler()
	    {
			public void handleMessage(Message msg)
			{
				String s = null;
				switch (msg.what)
				{
				case 0:
					s =	msg.getData().getString(ConstValues.Msg_key,null);
					if(s!=null)
					Operate.getInstance().showToast(s, MainActivity.this);
					MainActivity.this.finish();
				case 1:
					s =	msg.getData().getString(ConstValues.Msg_key,null);
					if(s!=null)
					Operate.getInstance().showToast(s, MainActivity.this);
				}
	    	}
	    };
	private void catchFileError(Exception e)
	{
		if(e.getMessage().equals("内存卡异常"))
		{
			Bundle bundle = new Bundle();
			bundle.putString(ConstValues.Msg_key, e.getMessage());
			Message msg  = new Message();
			msg.what = 0;
			msg.setData(bundle);
			MyHandler.sendMessage(msg);
		}
		else 
		{
			Bundle bundle = new Bundle();
			bundle.putString(ConstValues.Msg_key, e.getMessage());
			Message msg  = new Message();
			msg.what = 1;
			msg.setData(bundle);
			MyHandler.sendMessage(msg);				
		}		
	}
	private void readSentence(int index)
	{
		try
		{
			FileOperate.getInstance().getSentence(GlobalValue.sentences_english,GlobalValue.sentences_chinese, GlobalValue.currentBook,
					GlobalValue.currentGrade, index);	
			
		} catch (Exception e)
		{
			catchFileError(e);
		}
		for(int i=0;i<GlobalValue.sentences_english.size();++i)
		{
			System.out.println(GlobalValue.sentences_english.get(i));
		}
		for(int i=0;i<GlobalValue.sentences_chinese.size();++i)
		{
			System.out.println(GlobalValue.sentences_chinese.get(i));
		}
		
	}
	
	private void setSentence(int index)
	{
		mTextDestination.clear();
		splitSentence(GlobalValue.sentences_english.get(index), mTextCorrect);
		int[] indexs= Tool.getRandomIndex(mTextCorrect.size());
		mTextSource.clear();
		for(int i=0;i<indexs.length;++i)
		{
			mTextSource.add(mTextCorrect.get(indexs[i]));
		}
		updateMyTextView();
	}
	
	private void showDialog(String title,String text)
	{
//		AlertDialog.Builder builder = new Builder(MainActivity.this);
//		builder.setIcon(R.drawable.ic_launcher);
//		builder.setTitle(title);
//		builder.setMessage(text);
//		builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
//		{
//
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				dialog.dismiss();
//			}
//		});
//		
//		builder.create().show();
		CloudDialog cloudDialog = new CloudDialog(MainActivity.this, R.style.dialog_cloud, text);
		cloudDialog.setCanceledOnTouchOutside(true);
		cloudDialog.show();
	}
	
	private void assist()
	{		
		if(isRightNow())
		{
			if(mTextDestination.size()==mTextCorrect.size())
				return;
		}
		else
		{
			setUnitAndSentence(mCurrentUnit, mCurrentProblem);
		}
		int index = mTextDestination.size();
		moveWord(mTextCorrect.get(index), mTextSource, mTextDestination);
		mHaveScored[mCurrentProblem] = true;
	}

		
	private boolean isRightNow()
	{
		for(int i=0;i<mTextDestination.size();++i)
		{
			if(!(mTextDestination.get(i)
					.equals(mTextCorrect.get(i))))
				return false;
		}
		return true;
	}
}

