package com.yzrilyzr.myclass;

import android.view.*;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import com.yzrilyzr.FloatWindow.R;
import android.graphics.drawable.ColorDrawable;
import com.yzrilyzr.ui.uidata;

public class myActivity extends Activity implements View.OnClickListener
	{
public myActivity ctx=this;
		@Override
		public void onClick(View p1)
			{
				// TODO: Implement this method
			}


		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				// TODO: Implement this method
				super.onCreate(savedInstanceState);
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				getWindow().setBackgroundDrawable(new ColorDrawable(uidata.UI_COLOR_BACK));
				overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
			}

		@Override
		public void onConfigurationChanged(Configuration newConfig)
			{
				super.onConfigurationChanged(newConfig);
				if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO)
					{} 
				else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES)
					{}
			}

		@Override
		public void finish()
			{
				// TODO: Implement this method
				super.finish();
				overridePendingTransition(R.anim.activity_in2, R.anim.activity_out2);
			}

		public void exit(View v)
			{this.finish();}

		

	}

