package com.yzrilyzr.floatingwindow;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import com.yzrilyzr.floatingwindow.api.API;
import com.yzrilyzr.floatingwindow.apps.cls;
import com.yzrilyzr.myclass.myActivity;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.uidata;
import java.io.File;
import android.content.Intent;
import com.yzrilyzr.floatingwindow.apps.Message;
import java.io.IOException;

public class MainActivity extends myActivity
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        uidata.readData(this);
		API.startMainService(this,cls.LOAD);
        finish();
		try{
			File f=new File(util.mainDir+"/错误日志.txt");
			if(f.exists())
			{
				String ee=util.readwithN(f.getAbsolutePath());
				Intent in=new Intent();
				in.putExtra(Message.TITLE,"出错了");
				in.putExtra(Message.MSG,"悬浮窗在上次运行时出现了一个错误导致程序退出\n以下为错误日志:\n"+ee);
				API.startMainService(ctx,in,cls.MESSAGE);
				f.delete();
			}
		}
		catch (IOException e)
		{}
    }
}
