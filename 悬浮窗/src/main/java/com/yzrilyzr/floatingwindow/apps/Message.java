package com.yzrilyzr.floatingwindow.apps;
import android.content.Context;
import android.content.Intent;
import android.widget.ScrollView;
import com.yzrilyzr.floatingwindow.Window;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myTextView;
import com.yzrilyzr.ui.myTextViewBack;

public class Message
{
	public static final String MSG="MSG",TITLE="TITLE";
	public Message(Context c,Intent e){
		myTextView mtv=new myTextViewBack(c);
		mtv.setText(e.getStringExtra(MSG));
		mtv.setSelectAllOnFocus(true);
		mtv.setTextSize(util.px(3));
		ScrollView sv=new ScrollView(c);
		sv.addView(mtv);
		new Window(c,util.px(300),util.px(200))
			.addView(sv)
			.setTitle(e.getStringExtra(TITLE))
			.show();
	}
}
