package com.yzrilyzr.floatingwindow;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import com.yzrilyzr.myclass.myActivity;
import com.yzrilyzr.ui.uidata;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import com.yzrilyzr.myclass.util;

public class MainActivity extends myActivity
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Display w=getWindowManager().getDefaultDisplay();
        uidata.SCREEN_WIDTH=w.getWidth();
        uidata.SCREEN_HEIGHT=w.getHeight();
        startService(new Intent(this,MainService.class).putExtra(IData.TAG,IData.START));
        //finish();
		uidata.readData(this);
		setContentView(R.layout.main);
    }
}
