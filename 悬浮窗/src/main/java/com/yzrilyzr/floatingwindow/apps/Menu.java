package com.yzrilyzr.floatingwindow.apps;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.yzrilyzr.floatingwindow.PluginService;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.floatingwindow.Window;

public class Menu implements OnClickListener
{
	Context c;
	public Menu(Context ctx,Intent e){
		c=ctx;
		final Window w=new Window(ctx,-2,-2);
       	w.setBar(8,8,8,0).setIcon("");
        View v=LayoutInflater.from(ctx).inflate(R.layout.window_menu,null);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound1).setOnClickListener(this);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound2).setOnClickListener(this);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound3).setOnClickListener(this);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound4).setOnClickListener(this);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound5).setOnClickListener(this);
        v.findViewById(R.id.windowcontrolpanelmyLinearLayoutRound6).setOnClickListener(this);
        w.addView(v)
            .setTitle("菜单")
            .setCanResize(false)
            .show();
	}
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId())
		{
			case R.id.windowcontrolpanelmyLinearLayoutRound1:
				//showSystemStatus();
				break;
			case R.id.windowcontrolpanelmyLinearLayoutRound2:
				//showAPIsearch();
				break;
			case R.id.windowcontrolpanelmyLinearLayoutRound3:
				//showSystemStatus();
				break;
			case R.id.windowcontrolpanelmyLinearLayoutRound4:
				//showHelp();
				break;
			case R.id.windowcontrolpanelmyLinearLayoutRound5:
				//showAbout();
				break;
			case R.id.windowcontrolpanelmyLinearLayoutRound6:
				PluginService.fstop(c);
				break;
		}
	}
}
