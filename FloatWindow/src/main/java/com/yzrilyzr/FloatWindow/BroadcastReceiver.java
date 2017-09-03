package com.yzrilyzr.FloatWindow;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context p1, Intent p2)
    {
        // TODO: Implement this method
        Intent in=new Intent(p1,MainService.class);
        in.putExtra("IData","settings");
        p1.startService(in);
    }
    
}
