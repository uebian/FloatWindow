package com.yzrilyzr.AudioEdit;
import android.app.Activity;
import android.os.Bundle;
import com.yzrilyzr.FloatWindow.API;
import android.net.Uri;

public class Start extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		try{
		String f=Uri.parse(getIntent().getDataString()).getPath();
		API.intent.putExtra("file",f);
		}catch(Throwable e){}
		API.startMainService(this,"com.yzrilyzr.AudioEdit.Main");
		finish();
	} 
}
