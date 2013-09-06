package com.xiaobawang.xiaoxue.yingyu.jimuzaoju.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.xiaobawang.xiaoxue.yingyu.jimuzaoju.R;

public class CloudDialog extends AlertDialog{

	private String mText = null;

public CloudDialog(Context context, int theme) {
    super(context, theme);
}

public CloudDialog(Context context) {
    super(context);
}
public CloudDialog(Context context, int theme,String text) {
    super(context, theme);
    mText = text;
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(com.xiaobawang.xiaoxue.yingyu.jimuzaoju.R.layout.dialog_cloud);
//    Button button = (Button)findViewById(R.id.cloud_dialog_button);
    TextView textView = (TextView)findViewById(R.id.cloud_dialog_textview);
    
//    button.setOnClickListener(new View.OnClickListener()
//	{
//		
//		@Override
//		public void onClick(View v)
//		{
//			CloudDialog.this.dismiss();
//		}
//	});
    textView.setText(mText);
}
}