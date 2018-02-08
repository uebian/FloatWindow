package com.yzrilyzr.floatingwindow.apps;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.floatingwindow.Window;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yzrilyzr.floatingwindow.PluginService;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.view.Gravity;

public class PluginPicker implements AdapterView.OnItemClickListener
{
	Context ctx;
	List<Map<String,Object>> l=new ArrayList<Map<String,Object>>();
	Window w;
	public PluginPicker(final Context c,Intent e) throws PackageManager.NameNotFoundException
	{
		ctx=c;
		w=new Window(c,util.px(300),util.px(400))
            .setTitle("启动程序")
            .setIcon("class")
            .setBar(0,0,0,0);
        PackageManager pm=c.getPackageManager();
		List<ApplicationInfo> installedAppList =pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo appInfo : installedAppList)
        {
            boolean flag = false;
            if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
            {
                flag = true;
            }
            else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                flag = true;
            }
            if (flag)
            {
				try{
                ApplicationInfo ai=pm.getApplicationInfo(appInfo.packageName,PackageManager.GET_META_DATA);
				if(ai==null||ai.metaData==null)continue;
				boolean is=ai.metaData.getBoolean("fwplugin",false);
				if(is)
				{
					String cls=ai.metaData.getString("fwpluginclasses",null);
					if(cls!=null)
					{
						String[] cs=cls.split(";");
						for(String s:cs)
						{
							String[] k=s.split(":");
							Map<String,Object> m=new HashMap<String,Object>();
							m.put("icon",appInfo.loadIcon(pm));
							m.put("pkg",appInfo.packageName);
							m.put("name",k[0]);
							m.put("class",k[1]);
							m.put("info",appInfo.loadLabel(pm));
							l.add(m);
						}
					}
				}
				}catch(Throwable ep){
					
				}
            }
        }
        SimpleAdapter ad=new SimpleAdapter(c,l,R.layout.window_applist_entry,new String[]{"icon","name","info"},new int[]{R.id.windowapplistentryImageView1,R.id.windowapplistentryTextView1,R.id.windowapplistentryTextView2}){ 
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                ViewHolder holder;
                if(convertView==null)
                {
                    convertView=LayoutInflater.from(c).inflate(R.layout.window_applist_entry,parent,false);
                    holder=new ViewHolder();
                    holder.text1 = (TextView) convertView.findViewById(R.id.windowapplistentryTextView1);
                    holder.text2 = (TextView) convertView.findViewById(R.id.windowapplistentryTextView2);
                    holder.icon = (ImageView) convertView.findViewById(R.id.windowapplistentryImageView1);
                    convertView.setTag(holder);
                }
                else holder=(ViewHolder) convertView.getTag();
                Map m=l.get(position);
                holder.text1.setText(""+m.get("name"));
                holder.text2.setText(""+m.get("info"));
                Object o=m.get("icon");
                if(o!=null)holder.icon.setImageDrawable((Drawable)o);
                return convertView;
			}
        };
        GridView mlv=new GridView(c);
        mlv.setAdapter(ad);
		mlv.setNumColumns(4);
		mlv.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
        w.addView(mlv);
        w.show();
        mlv.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
	{
		String p=(String)l.get(p3).get("pkg");
		String c=(String)l.get(p3).get("class");
		if(c.startsWith("."))c=p+c;
		PluginService.loadPlugin(ctx,new Intent()
		.putExtra("pkg",p)
		.putExtra("class",c));
	}
	static class ViewHolder
	{
		TextView text1,text2;
		ImageView icon;
	}
}
