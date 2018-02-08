package com.yzrilyzr.floatingwindow.apps;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.yzrilyzr.floatingwindow.PluginService;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.floatingwindow.Window;
import com.yzrilyzr.floatingwindow.view.StarterView;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myToast;
import com.yzrilyzr.floatingwindow.api.API;
import com.yzrilyzr.icondesigner.VecView;

public class StartButton implements StarterView.Listener,View.OnTouchListener,View.OnClickListener
{
	Window button,menu;
	Context ctx;
	int code=-1;
	StarterView cv;
	private static boolean once=false;
	public StartButton(Context ctx,Intent e)
	{
		if(once)return;
		once=true;
		this.ctx=ctx;
		//button
		int dd=util.px(30);
        button=new Window(ctx,dd,dd)
			.setPosition((util.getScreenWidth()-dd)/2,(util.getScreenHeight()-dd)/2)
			.show();
        LinearLayout a =(LinearLayout)button.getMainView();
        a.setBackground(null);
        a.removeAllViews();
        VecView i=new VecView(ctx);
        i.setImageVec("floatingwindow");
		i.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
		a.addView(i);
		//menu
		menu=new Window(ctx,-1,-1);
		cv=new StarterView(ctx);
        LinearLayout aa=(LinearLayout)menu.getMainView();
        aa.setOnTouchListener(null);
        aa.setBackground(null);
        aa.removeAllViews();
        aa.addView(cv);
        cv.setListener(this);
		i.setOnTouchListener(this);
        i.setOnClickListener(this);
	}
	@Override
	public void onClick(View p1)
	{
		menu.show();
		cv.toggle();
	}
	@Override
	public boolean onTouch(View p1, MotionEvent p2)
	{
		cv.setPosition(p2.getRawX(),p2.getRawY());
		button.moveableView(p1,p2);
		return false;
	}
	@Override
	public void onItemClick(int which)
	{
		code=which;
	}
	@Override
	public void onAnimEnd()
	{
		menu.dismiss();
		if(code==3)
		{
			API.startMainService(ctx,cls.MENU);
		}
		if(code==4)
		{
			PluginService.fstop(ctx);
		}
		if(code==5)
		{
			API.startMainService(ctx,cls.PLUGINPICKER);
		}
	}
	@Override
	public void onAnimStart()
	{
		code=-1;
	}
}
