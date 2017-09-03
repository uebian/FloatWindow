package com.yzrilyzr.mp3edit;

import android.app.*;
import android.os.*;
import com.yzrilyzr.FloatWindow.API;
import android.net.Uri;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		try{
			API.intent.putExtra("file",Uri.parse(getIntent().getDataString()).getPath());
			}catch(Throwable e){System.exit(0);}
      	API.startMainService(this,"com.yzrilyzr.mp3edit.Main");
		finish();
    }
}
