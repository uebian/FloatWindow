package com.yzrilyzr.floatingwindow.apps;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.floatingwindow.Window;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myButton;
import com.yzrilyzr.ui.myToast;
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
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.yzrilyzr.ui.myProgressBar;
import android.os.Handler;

public class Downloader implements OnClickListener
{
	Context ctx;
	Window w;
	EditText e1,e2;
	long progress=0,length;
	View cc,dd;
	boolean dow=false;
	myProgressBar pgb;
	public Downloader(Context c,Intent e)
	{
		ctx=c;
		String p1=e.getStringExtra("url");
		if(p1==null)p1="";
		length=e.getLongExtra("length",0);
		ViewGroup v=(ViewGroup) LayoutInflater.from(c).inflate(R.layout.window_webviewer_download,null);
		cc=v.getChildAt(0);
		dd=v.getChildAt(1);
		dd.setVisibility(8);
		w=new Window(c,util.px(300),-2)
			.setTitle("下载器")
			.addView(v)
			.setIcon("download")
			.show();
		e1=(EditText)v.findViewById(R.id.windowwebviewerdownloadmyEditText1);
		e2=(EditText) v.findViewById(R.id.windowwebviewerdownloadmyEditText2);
		((myButton)v.findViewById(R.id.windowwebviewerdownloadmyButton1)).setOnClickListener(this);
		((myButton)v.findViewById(R.id.windowwebviewerdownloadmyButton2)).setOnClickListener(this);
		((myButton)v.findViewById(R.id.windowwebviewerdownloadmyButton3)).setOnClickListener(this);
		pgb=(myProgressBar) v.findViewById(R.id.windowwebviewerdownloadmyProgressBar1);
		pgb.setMax((int)length);
		e1.setText(p1);
		e2.setText(util.mainDir+"/下载的文件/"+URLDecoder.decode(p1.substring(p1.lastIndexOf("/")+1)));
	}
	@Override
	public void onClick(View py1)
	{
		switch(py1.getId())
		{
			case R.id.windowwebviewerdownloadmyButton1:
				cc.setVisibility(8);
				dd.setVisibility(0);
				new Thread(new DownloaderTask(e1.getText()+"",e2.getText()+"")).start();
				break;
			case R.id.windowwebviewerdownloadmyButton2:
				dow=!dow;
				if(dow)new Thread(new DownloaderTask(e1.getText()+"",e2.getText()+"")).start();
				((myButton)py1).setText(dow?"暂停":"继续");
				break;
			case R.id.windowwebviewerdownloadmyButton3:
				dow=false;
				break;
		}
	}
	private class DownloaderTask implements Runnable
	{
		String url,to;
		public DownloaderTask(String url,String to)
		{ 
			this.url=url;
			this.to=to;
		}
		@Override
		public void run()
		{
			// TODO: Implement this method
			util.toast(ctx,"正在下载");
			dow=true;
			try
			{    
				File file=new File(to.substring(0,to.lastIndexOf("/")));   
				if(!file.exists())file.mkdirs();
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
				if(progress!=0)get.addHeader("Range", "bytes="+progress+"-");   
                HttpResponse response = client.execute(get);   
				Handler ha=new Handler(ctx.getMainLooper());
				Pro pe=new Pro();
				int code=response.getStatusLine().getStatusCode();
				if(HttpStatus.SC_OK==code||HttpStatus.SC_PARTIAL_CONTENT==code)
				{   
                    HttpEntity entity = response.getEntity();   
                    InputStream input = entity.getContent();
					byte[] bu=new byte[10240];
					int ii=0;
					BufferedOutputStream os=new BufferedOutputStream(new FileOutputStream(to));
					while(dow&&(ii=input.read(bu))!=-1)
					{
						os.write(bu,0,ii);
						os.flush();
						progress+=ii;
						ha.post(pe);
					}
					os.close();
                    input.close();
					if(dow)util.toast(ctx,"下载完成");
					else util.toast(ctx,"下载暂停");
                }
				else
				{   
                    util.toast(ctx,"无法连接服务器");
                }   
            }
			catch (Exception e)
			{
				util.toast(ctx,"下载失败");
            }   
		}
	}
	class Pro implements Runnable
	{
		@Override
		public void run()
		{
			pgb.setProgress((int)progress);
		}
	}
}
