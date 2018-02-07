package com.yzrilyzr.floatingwindow;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.yzrilyzr.floatingwindow.api.API;
import com.yzrilyzr.myclass.myActivity;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myTextView;
import com.yzrilyzr.ui.myTextViewBack;
import com.yzrilyzr.ui.myTitleButton;
import com.yzrilyzr.ui.myToolBar;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PluginActivity extends myActivity
{
    private Context ctx=this;
    private Object obj;
    private LinearLayout ContentView;
    private myToolBar myToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContentView=(LinearLayout) findViewById(R.id.activitymainLinearLayout1);
        myToolBar=(myToolBar) findViewById(R.id.activitymainmyToolBar1);
        loadPlugin(ctx,getIntent());
        invokePlugin("onCreate",savedInstanceState);
    }
    public void menu(View v)
    {

    }
    public myTitleButton getTitleButton(int i)
    {
        return myToolBar.getButton(i);
    }
    public void removeToolBar(){
        ViewGroup v=(ViewGroup) myToolBar.getParent();
        v.removeView(myToolBar);
    }
    public void addToolBar(){
        ViewGroup v=(ViewGroup) myToolBar.getParent();
        v.addView(myToolBar);
    }
    public ViewGroup getMainView(){
        return (ViewGroup) myToolBar.getParent();
    }
    @Override
    public void setContentView(View view)
    {
        // TODO: Implement this method
        ContentView.removeAllViews();
        ContentView.addView(view);
        //super.setContentView(view);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params)
    {
        // TODO: Implement this method
        ContentView.addView(view,params);
        //super.addContentView(view, params);
    }

    @Override
    public void setTitle(CharSequence title)
    {
        // TODO: Implement this method
        myToolBar.setTitle(title.toString());
        //super.setTitle(title);
    }
    private void loadPlugin(final Context ctx,final Intent intent)
    {
        String pkg="";
        PackageManager PackageManager=ctx.getPackageManager();
        try
        {
            pkg=intent.getStringExtra("pkg");
            String clazz=intent.getStringExtra("class");
            String apiclz="com.yzrilyzr.floatingwindow.api.API";
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
            Constructor con=c.getDeclaredConstructor(Context.class,Intent.class);
            con.setAccessible(true);
            obj=con.newInstance(ctx,intent);
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
            ScrollView sv=new ScrollView(ctx);
            sv.addView(mtv);
            setContentView(sv);
        }
    }
    private Object invokePlugin(String method,Object... param)
    {
        try
        {
            Class a=obj.getClass();
            Method[] ms=a.getDeclaredMethods();
            for(Method m:ms)
            {
                if(m.getName().equals(method))
                {
                    m.setAccessible(true);
                    return m.invoke(obj,param);
                }
            }
        }
        catch(Throwable e)
        {
            System.out.println(e);
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
    protected void onDestroy()
    {
        // TODO: Implement this method
        super.onDestroy();
        invokePlugin("onDestroy");
    }

    @Override
    protected void onPause()
    {
        // TODO: Implement this method
        super.onPause();
        invokePlugin("onPause");
    }

    @Override
    protected void onRestart()
    {
        // TODO: Implement this method
        super.onRestart();
        invokePlugin("onRestart");
    }

    @Override
    protected void onResume()
    {
        // TODO: Implement this method
        super.onResume();
        invokePlugin("onResume");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO: Implement this method
        Object o=invokePlugin("onKeyDown",keyCode,event);
        return o==null?super.onKeyDown(keyCode, event):o;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event)
    {
        // TODO: Implement this method
        Object o=invokePlugin("onKeyLongPress",keyCode,event);
        return o==null?super.onKeyLongPress(keyCode, event):o;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        // TODO: Implement this method
        Object o=invokePlugin("onKeyUp",keyCode,event);
        return o==null?super.onKeyUp(keyCode, event):o;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO: Implement this method
        Object o=invokePlugin("onTouchEvent",event);
        return o==null?super.onTouchEvent(event):o;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO: Implement this method
        super.onActivityResult(requestCode, resultCode, data);
        invokePlugin("onActivityResult",requestCode,resultCode,data);
    }

    @Override
    public void onClick(View p1)
    {
        // TODO: Implement this method
        super.onClick(p1);
        invokePlugin("onClick",p1);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event)
    {
        // TODO: Implement this method
        Object o=invokePlugin("onTrackballEvent",event);
        return o==null?super.onTrackballEvent(event):o;
    }

    @Override
    protected void onStart()
    {
        // TODO: Implement this method
        super.onStart();
        invokePlugin("onStart");
    }

    @Override
    protected void onStop()
    {
        // TODO: Implement this method
        super.onStop();
        invokePlugin("onStop");
    }

    @Override
    public void onLowMemory()
    {
        // TODO: Implement this method
        super.onLowMemory();
        invokePlugin("onLowMemory");
    }

    @Override
    public void onTrimMemory(int level)
    {
        // TODO: Implement this method
        super.onTrimMemory(level);
        invokePlugin("onTrimMemory",level);
    }

}
