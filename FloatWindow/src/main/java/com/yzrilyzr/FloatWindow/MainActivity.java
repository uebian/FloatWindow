package com.yzrilyzr.FloatWindow;
import com.yzrilyzr.myclass.myActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import com.yzrilyzr.ui.uiSettingsActivity;
import com.yzrilyzr.ui.uidata;
import android.view.Display;

public class MainActivity extends myActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        Display w=getWindowManager().getDefaultDisplay();
        uidata.SCREEN_WIDTH=w.getWidth();
        uidata.SCREEN_HEIGHT=w.getHeight();
        startService(new Intent(this,MainService.class).putExtra("IData","start"));
        finish();
    }

}
