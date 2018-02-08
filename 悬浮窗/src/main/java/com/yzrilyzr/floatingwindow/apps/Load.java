package com.yzrilyzr.floatingwindow.apps;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.floatingwindow.Window;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myLoadingView;
import com.yzrilyzr.ui.myRippleDrawable;
import com.yzrilyzr.ui.myRoundDrawable;
import com.yzrilyzr.ui.myTextViewTitleBack;
import com.yzrilyzr.ui.uidata;
import com.yzrilyzr.floatingwindow.api.API;
import com.yzrilyzr.icondesigner.VecView;

public class Load implements Runnable
{
	Window w;
	Context c;
	private static boolean once=false;
	public Load(Context c,Intent e){
		if(once)return;
		once=true;
		this.c=c;
		int wid,hei;
        w=new Window(c,wid=util.px(210),hei=util.px(260));
        w.setPosition((util.getScreenWidth()-wid)/2,(util.getScreenHeight()-hei)/2);
        w.show().setCanResize(false);
        LinearLayout v=(LinearLayout) w.getMainView();
        v.removeAllViews();
        v.setOnTouchListener(null);
        v.setOrientation(1);
        v.setGravity(Gravity.CENTER);
        v.setBackground(new myRippleDrawable(uidata.UI_COLOR_BACK,uidata.UI_RADIUS));
        VecView iv=new VecView(c);
        int ii=util.px(180);
        iv.setImageVec("floatingwindow");
        iv.setLayoutParams(new LinearLayout.LayoutParams(ii,ii));
        v.addView(iv);
        myTextViewTitleBack tv=new myTextViewTitleBack(c);
        tv.setText("悬浮窗");
        tv.setGravity(Gravity.CENTER);
        v.addView(tv);
        myLoadingView ml=new myLoadingView(c);
        ii=util.px(40);
        ml.setLayoutParams(new LinearLayout.LayoutParams(ii,ii));
        v.addView(ml);
        new Handler().postDelayed(this,1000);
    }
	@Override
	public void run()
	{
		// TODO: Implement this method
		w.dismiss();
		API.startMainService(c,cls.STARTBUTTON);
	}
}
