package com.yzrilyzr.floatingwindow.apps;
import android.webkit.*;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.floatingwindow.Window;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myEditText;
import com.yzrilyzr.ui.myProgressBar;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.yzrilyzr.ui.myButton;
import android.widget.EditText;
import com.yzrilyzr.ui.myToast;
import com.yzrilyzr.floatingwindow.api.API;

public class WebViewer implements DownloadListener,OnClickListener,Window.WindowInterface
{
	WebView web;
	myProgressBar prog;
	Context ctx;
	Window w,cv;
	myEditText edit;
	public WebViewer(Context c,Intent e)
	{
		ctx=c;
		ViewGroup v=(ViewGroup) LayoutInflater.from(c).inflate(R.layout.window_webviewer,null);
		w=new Window(c,util.px(300),util.px(360))
			.setTitle("WebViewer")
			.addView(v)
			.setIcon("internet")
			.show();
		web=(WebView) v.getChildAt(2);
		prog=(myProgressBar) v.getChildAt(1);
		ViewGroup f=(ViewGroup)v.getChildAt(0);
		edit=(myEditText) f.getChildAt(0);
		f.getChildAt(1).setOnClickListener(this);
		WebSettings s=web.getSettings();
		s.setJavaScriptEnabled(true);
		s.setPluginState(WebSettings.PluginState.ON);
		s.setUseWideViewPort(true);
		s.setSavePassword(true);
        s.setSaveFormData(true);
		s.setLoadWithOverviewMode(true);
		s.setCacheMode(WebSettings.LOAD_NO_CACHE);
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
		{
            s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
		web.setWebViewClient(new WebViewClient(){
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url)
				{
					web.loadUrl(url);
					return true;
				}
			});
		web.setWebChromeClient(new WebChromeClient(){
				public void onProgressChanged(WebView view, int progress)   
				{
					prog.setVisibility(View.VISIBLE); 
					prog.setProgress(progress);     
					if(progress == 100)
					{     
						prog.setVisibility(View.GONE); 
						w.setTitle(view.getTitle());
					}
				}
				public boolean onJsAlert(WebView view,String url,String message,final JsResult result)
				{
					/*if(sho)new myAlertDialog(ctx)
					 .setTitle("来自网页的提示")
					 .setMessage(message)
					 .setPositiveButton("确定",new myDialogInterface(){
					 public void click(View v,int i)
					 {
					 result.confirm();
					 }
					 })
					 .setNeutralButton("不要再显示",new myDialogInterface(){
					 public void click(View v,int i)
					 {
					 sho=false;
					 result.cancel();
					 }
					 })
					 .setNegativeButton("取消",new myDialogInterface(){
					 public void click(View v,int i)
					 {
					 result.cancel();
					 }
					 })
					 .show();
					 else result.cancel();*/
					return true;
				}
				@Override
				public View getVideoLoadingProgressView()
				{
					FrameLayout frameLayout = new FrameLayout(ctx);
					frameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					return frameLayout;
				}
				@Override
				public void onShowCustomView(View view, CustomViewCallback callback)
				{
					cv=new Window(ctx,-2,-2)
						.addView(view)
						.show();
				}
				@Override
				public void onHideCustomView()
				{
					cv.dismiss();
				}
			});
		web.setDownloadListener(this);
		String ur=e.getStringExtra("url");
		if(ur!=null)web.loadUrl(ur);
	}
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		// TODO: Implement this method
	}
	@Override
	public void onPositionChanged(int x, int y)
	{
		// TODO: Implement this method
	}
	@Override
	public void onButtonDown(int code)
	{
		if(code==Window.ButtonCode.MIN)
			if(!w.getMin())
				web.onPause();
			else web.onResume();
		else if(code==Window.ButtonCode.CLOSE)
			web.destroy();
	}
	@Override
	public void onClick(View p1)
	{
		String s=edit.getText().toString();
		if(s.indexOf("://")==-1)s="http://"+s;
		edit.setText(s);
		web.loadUrl(s);
	}
	@Override
	public void onDownloadStart(String p1, String p2, String p3, String p4, long p5)
	{
		API.startMainService(ctx,new Intent().putExtra("url",p1),cls.DOWNLOADER);
	}
}
