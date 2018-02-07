package com.yzrilyzr.floatingwindow;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context p1, Intent p2)
    {
		p1.stopService(new Intent(p1,PluginService.class));
        System.exit(0);
    }
    
}
