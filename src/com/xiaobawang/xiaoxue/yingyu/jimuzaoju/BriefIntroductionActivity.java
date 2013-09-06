package com.xiaobawang.xiaoxue.yingyu.jimuzaoju;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

public class BriefIntroductionActivity extends Activity
{
	WebView mWebView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brief_introduction);
		Button button = (Button)findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				BriefIntroductionActivity.this.finish();
			}
		});
	}
	
}
