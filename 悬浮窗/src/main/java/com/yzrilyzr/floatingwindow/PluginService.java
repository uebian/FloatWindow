package com.yzrilyzr.floatingwindow;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.IBinder;
import android.widget.ScrollView;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myTextView;
import com.yzrilyzr.ui.myTextViewBack;
import com.yzrilyzr.ui.uidata;
import dalvik.system.PathClassLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import com.yzrilyzr.ui.myToast;

public class PluginService extends android.app.Service
{
    public static ArrayList<Object> pluginObject=new ArrayList<Object>();
    private Context ctx=this;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
		Intent inte =new Intent(this,BroadcastReceiver.class);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(this,0,inte,0);
        Notification.Builder builder1 = new Notification.Builder(this)
            .setSmallIcon(R.drawable.icon) //设置图标
            .setTicker("悬浮窗主服务已开始运行")
            .setContentTitle("悬浮窗主服务正在运行")//设置标题
            .setContentText("点击这里停止运行")//消息内容
            .setSound(null)
            .setVibrate(null)
            .setAutoCancel(false)//打开程序后图标消失
            .setContentIntent(pendingIntent)
            .setLights(uidata.UI_COLOR_MAIN,500,2000);
        Notification notification1 = builder1.build();
        startForeground(1,notification1);
        if(intent!=null)
            loadPlugin(ctx,intent);
        invokePlugin("onStartCommand",intent,flags,startId);
        return START_STICKY;
    }
    private void loadPlugin(final Context ctx,final Intent intent)
    {
        String pkg="";
        PackageManager PackageManager=ctx.getPackageManager();
        try
        {
            switch(intent.getIntExtra(IData.TAG,0))
            {
				case IData.ACTIVITY:
                    intent.setClass(ctx,PluginActivity.class);
                    intent.setAction(null);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
            }
            pkg=intent.getStringExtra("pkg");
            String clazz=intent.getStringExtra("class");
            String path=PackageManager.getPackageInfo(pkg,PackageInfo.INSTALL_LOCATION_AUTO).applicationInfo.publicSourceDir;
            ClassLoader mainloader=ctx.getClassLoader();
            Class c=null;
            try
            {
                c=mainloader.loadClass(clazz);
            }
            catch(Throwable e)
            {
                PathClassLoader pcl=new PathClassLoader(path,mainloader);
                c=pcl.loadClass(clazz);
            }
            Object o=c.getConstructor(Context.class,Intent.class).newInstance(ctx,intent);
            pluginObject.add(o);
			myToast.s(ctx,"插件已载入");
        }
        catch(Throwable e)
        {
            String pkginfo="";
            try
            {
                pkginfo=PackageManager.getPackageInfo(pkg,PackageInfo.INSTALL_LOCATION_AUTO).applicationInfo.loadLabel(PackageManager)+"";
            }
            catch (PackageManager.NameNotFoundException e2)
            {
                pkginfo="未知";
            }
            myTextView mtv=new myTextViewBack(ctx);
            mtv.setText("插件:"+pkginfo+"("+pkg+")\n\nstacktrace:\n"+handleException(e));
            mtv.setSelectAllOnFocus(true);
			mtv.setTextSize(util.px(3));
            ScrollView sv=new ScrollView(ctx);
            sv.addView(mtv);
            new Window(ctx,util.px(300),util.px(200))
                .addView(sv)
                .setTitle("出错了")
                .show();
        }
    }

    private Object invokePlugin(String method,Object... param)
    {
        if(pluginObject!=null)
            for(Object o:pluginObject)
            {
                try
                {
                    Class a=o.getClass();
                    Method[] ms=a.getDeclaredMethods();
                    for(Method m:ms)
                    {
                        if(m.getName().equals(method))
                        {
                            m.setAccessible(true);
                            return m.invoke(o,param);
                        }
                    }
                }
                catch(Throwable e)
                {
                    System.out.println(e);
                }
            }
		return null;
    }
    private String handleException(Throwable e)
    {
        String msg=null;
        if(e instanceof InvocationTargetException)
			while(e instanceof InvocationTargetException)
			{
				e=((InvocationTargetException)e).getTargetException();
				if(e!=null)msg=util.getStackTrace(e);
			}
        else msg=util.getStackTrace(e);
        return msg;
    }
    @Override
    public void onCreate()
    {
        // TODO: Implement this method
        super.onCreate();
        uidata.isInit=true;
        uidata.readData(ctx);
        /*Intent i=new Intent(this,MainService.class);
        i.putExtra("IData","pluginStart");
        startService(i);*/
        invokePlugin("onCreate");
    }
    @Override
    public void onDestroy()
    {
        // TODO: Implement this method
        super.onDestroy();
        invokePlugin("onDestory");
        stopForeground(false);
    }

    @Override
    public void onLowMemory()
    {
        // TODO: Implement this method
        super.onLowMemory();
        invokePlugin("onLowMemory");
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        // TODO: Implement this method
        super.onStart(intent, startId);
        invokePlugin("onStart",intent,startId);
    }

    @Override
    public IBinder onBind(Intent p1)
    {
        // TODO: Implement this method
        Object o=invokePlugin("onBind",p1);
        return o==null?null:(IBinder)o;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        // TODO: Implement this method
        super.onTaskRemoved(rootIntent);
        invokePlugin("onTaskRemoved",rootIntent);
    }

    @Override
    public void onTrimMemory(int level)
    {
        // TODO: Implement this method
        super.onTrimMemory(level);
        invokePlugin("onTrimMemory",level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        // TODO: Implement this method
        super.onConfigurationChanged(newConfig);
        invokePlugin("onConfigurationChanged",newConfig);
    }
}
