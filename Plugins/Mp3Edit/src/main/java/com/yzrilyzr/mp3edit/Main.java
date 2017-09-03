package com.yzrilyzr.mp3edit;
import android.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import android.app.Service;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.view.View.OnClickListener;
import com.yzrilyzr.FloatWindow.API;
import java.util.ArrayList;

public class Main extends Service implements OnClickListener
{

	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}

	Service ctx;
	Object win;
	public Main(Context ct,Intent intent)
	{
		ctx=(Service) ct;
		View v=API.parseXmlViewFromFile(ctx,"com.yzrilyzr.mp3edit","res/layout/main.xml");
		((ViewGroup)v).getChildAt(0).setOnClickListener(this);
		ListView lv=(ListView) ((ViewGroup)v).getChildAt(1);
		win=API.class2Object(API.WINDOW_CLASS,new Class[]{Context.class,int.class,int.class},new Object[]{ctx,-2,-2});
		API.invoke(win,"show");
		API.invoke(win,"addView",v);
		API.invoke(win,"setTitle","MP3 ID3编辑");
		try
		{
			final MusicId3 m=new MusicId3(intent.getStringExtra("file"));
			final ArrayList<ViewGroup> vgs=new ArrayList<ViewGroup>();
			for(final String s:m.id){
				ViewGroup vg=(ViewGroup) API.parseXmlViewFromFile(ctx,"com.yzrilyzr.mp3edit","res/layout/ent.xml");
					CheckBox c=(CheckBox) vg.findViewById(R.id.entCheckBox1);
					TextView t=(TextView) vg.findViewById(R.id.entTextView1);
					final EditText e=(EditText) vg.findViewById(R.id.entEditText1);
					ImageView iv=(ImageView) vg.findViewById(R.id.entImageView1);
					t.setText(s);
					if("APIC".equals(s))
					{
						iv.setVisibility(0);
						iv.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View p1)
								{
									// TODO: Implement this method
									
								}
							});
						e.setVisibility(8);
						byte[] b=(byte[]) m.map.get("APIC");
						if(b!=null){
							c.setChecked(true);
							iv.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));
						}
					}
					else
					{
						Object o=m.map.get(s);
						if(o!=null)
						{
							e.setEnabled(true);
							c.setChecked(true);
							e.setText(o.toString());
						}
					}
				e.addTextChangedListener(new TextWatcher(){

						@Override
						public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
						{
							// TODO: Implement this method
						}

						@Override
						public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
						{
							// TODO: Implement this method
							m.map.put(s,e.getText().toString());
						}

						@Override
						public void afterTextChanged(Editable p1)
						{
							// TODO: Implement this method
						}
					});
				c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
						@Override
						public void onCheckedChanged(CompoundButton p1, boolean p2)
						{
							// TODO: Implement this method
							e.setEnabled(p2);
							if(p2)m.map.put(s,e.getText().toString());
							else m.map.remove(s);
						}
					});
					vgs.add(vg);
			}
			lv.setAdapter(new BaseAdapter(){

					@Override
					public int getCount()
					{
						// TODO: Implement this method
						return vgs.size();
					}

					@Override
					public Object getItem(int p1)
					{
						// TODO: Implement this method
						return null;
					}

					@Override
					public long getItemId(int p1)
					{
						// TODO: Implement this method
						return 0;
					}

					@Override
					public View getView(int p1, View p2, ViewGroup p3)
					{
						// TODO: Implement this method
						return vgs.get(p1);
					}
				});
		}
		catch (Throwable e)
		{
			System.out.println(e);
		}
	}
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		int id=p1.getId();
		//if(id==R.id.mainmyFab1){}
	}


}
