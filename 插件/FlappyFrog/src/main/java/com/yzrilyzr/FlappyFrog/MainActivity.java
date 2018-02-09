package com.yzrilyzr.FlappyFrog;

import android.app.Activity;
import android.os.Bundle;
import com.yzrilyzr.FloatWindow.API;
import android.widget.Toast;
import java.io.StringWriter;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import android.content.Intent;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        try
        {
            API.startMainService(this,new Intent(),"com.yzrilyzr.FlappyFrog.Main");
            //API.startMainActivity(this,"com.yzrilyzr.FlappyFrog.ToActivity");
        }
        catch(Throwable e)
        {
            ByteArrayOutputStream sw=new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(sw));
            Toast.makeText(this,sw.toString(),0).show();
        }
        finish();
    }
}
