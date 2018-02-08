package com.yzrilyzr.floatingwindow;
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.KeyEvent;
import com.yzrilyzr.ui.myToast;
import android.content.Intent;

public class AccessibilityService extends AccessibilityService
{
	int k=0;
	@Override
	public void onAccessibilityEvent(AccessibilityEvent p1)
	{
		// TODO: Implement this method
	}
	@Override
	public void onInterrupt()
	{
		// TODO: Implement this method
	}
	@Override
	protected boolean onKeyEvent(KeyEvent event)
	{
		int c=event.getKeyCode(),e=event.getAction();
		int d=KeyEvent.ACTION_DOWN;
		if(e==d)
		if(c==KeyEvent.KEYCODE_MENU)
			if(k<3)k++;
			else k=0;
		else if(c==KeyEvent.KEYCODE_BACK)
			if(k>=3)k++;
			else k=0;
		if(k==6){
			PluginService.fstop(this);
		}
		return super.onKeyEvent(event);
	}
}
