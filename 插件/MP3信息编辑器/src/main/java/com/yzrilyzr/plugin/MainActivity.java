package com.yzrilyzr.plugin;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.yzrilyzr.FloatWindow.API;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            String file=getIntent().getDataString();
            file=Uri.parse(file).getPath();//.decode(file);
            API.intent.putExtra("path",file);
            API.startMainService(this,"com.yzrilyzr.plugin.Start");
            finish();
        }
        catch(Throwable e)
        {
            Toast.makeText(this,e+"",0).show();
        }
    }

}
