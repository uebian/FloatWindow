package com.yzrilyzr.FloatWindow;
import android.content.*;
import android.content.pm.*;
import com.yzrilyzr.ui.*;
import java.lang.reflect.*;

import android.content.res.Configuration;
import android.os.IBinder;
import android.widget.ScrollView;
import com.yzrilyzr.myclass.util;
import dalvik.system.PathClassLoader;
import java.util.ArrayList;

public class PluginService extends android.app.Service
{
    public static ArrayList<Object> pluginObject=new ArrayList<Object>();
    private Context ctx=this;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // TODO: Implement this method
        startForeground(0,null);
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
            String IDATA=intent.getStringExtra("IDATA");
            if(IDATA!=null){
                if(IDATA.equals("ACTIVITY")){
                    intent.setClass(ctx,PluginActivity.class);
                    intent.setAction(null);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }
            }
            pkg=intent.getStringExtra("pkg");
            String clazz=intent.getStringExtra("class");
            String apiclz="com.yzrilyzr.FloatWindow.API";
            String path=ctx.getPackageManager().getPackageInfo(pkg,PackageInfo.INSTALL_LOCATION_AUTO).applicationInfo.publicSourceDir;
            ClassLoader mainloader=ctx.getClassLoader();
            Class c=null,api=null;
            try
            {
                c=mainloader.loadClass(clazz);
                api=mainloader.loadClass(apiclz);
            }
            catch(Throwable e)
            {
                PathClassLoader pcl=new PathClassLoader(path,mainloader);
                c=pcl.loadClass(clazz);
                api=pcl.loadClass(apiclz);
            }
            int ver=api.getField("API_VERSION").getInt(api);
            if(ver<API_APP.API_VERSION)util.toast(ctx,"插件API版本太低，可能会影响使用(插件:"+ver+"，本程序:"+API_APP.API_VERSION+")\n请报告插件作者请求更新");
            Constructor con=c.getDeclaredConstructor(Context.class,Intent.class);
            con.setAccessible(true);
            Object o=con.newInstance(ctx,intent);
            pluginObject.add(o);
            //toast("插件:"+pkg+" 载入完毕");
        }
        catch(Throwable e)
        {
            System.out.println(e);
            String pkginfo="";
            try
            {
                PackageManager.getPackageInfo(pkg,PackageInfo.INSTALL_LOCATION_AUTO).applicationInfo.loadLabel(PackageManager);
            }
            catch (PackageManager.NameNotFoundException e2)
            {
                pkginfo="未知";
            }
            myTextView mtv=new myTextViewBack(ctx);
            mtv.setText("插件:"+pkginfo+"("+pkg+")\n\nstacktrace:\n"+handleException(e));
            mtv.setSelectAllOnFocus(true);
            ScrollView sv=new ScrollView(ctx);
            sv.addView(mtv);
            new Window(ctx,util.dip2px(300),util.dip2px(200))
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
        String msg = null;
        if (e instanceof InvocationTargetException)
        {
            Throwable targetEx = ((InvocationTargetException) e)
                .getTargetException();
            if (targetEx != null)
            {
                msg = util.getStackTrace(targetEx);
            }
        }
        else
        {
            msg = util.getStackTrace(e);
        }
        return msg;
    }

    @Override
    public void onCreate()
    {
        // TODO: Implement this method
        super.onCreate();
        uidata.isInit=true;
        uidata.readData(ctx);
        uidata.initIcon(ctx);
        Intent i=new Intent(this,MainService.class);
        i.putExtra("IData","pluginStart");
        startService(i);
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
