package com.yzrilyzr.uidemo;

import android.view.*;
import android.widget.*;
import com.yzrilyzr.myclass.*;
import com.yzrilyzr.ui.*;

import android.content.Intent;
import android.os.Bundle;
import com.yzrilyzr.FloatWindow.R;

public class MainActivity extends myActivity
	{/*
		myCanvas mc;
		mySlidingMenu msm;
		myViewPager mvp;
		//myMusicPlayer mmp;
		public void toggle(View v)
			{msm.toggle();}
		public void setting(View v)
			{startActivity(new Intent(MainActivity.this, uiSettingsActivity.class));}
		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				try
					{
						setContentView(R.layout.main);
						((ImageView)findViewById(R.id.mainImageView1)).setImageDrawable(new myRoundDrawable(this, R.drawable.launch));
						mc = (myCanvas)findViewById(R.id.mainmyCanvas1);
						msm = (mySlidingMenu)findViewById(R.id.mainmySlidingMenu1);
						mvp = (myViewPager)findViewById(R.id.mainmyViewPager1);
						mvp.setPages(new myButton(this), new myButton(this), new myButton(this));	
						ListAdapter adapter = new ArrayAdapter<String>(this,R.layout.layout_alertdialog_items,R.id.layoutalertdialogitemsmyTextView1,
						new String[]{"项目1","项目2","项目3"});
						myListView mlv;
						(mlv=(myListView)findViewById(R.id.mainmyListView1)).setAdapter(adapter);
						mlv.setScrollView(true);
						//((myColorView)findViewById(R.id.mainmyColorView1)).setColorView(uidata.UI_COLOR_MAIN);
						((myCheckBox)findViewById(R.id.mainmyCheckBox1)).setListener(new myCheckBox.OnCheckedChangeListener(){
									@Override
									public void onCheckedChange(myCheckBox mcb, boolean b)
										{
											if (b)mc.startRDraw();
											else mc.stopRDraw();
										}
								});
						final mySeekBar msb=(mySeekBar)findViewById(R.id.mainmySeekBar1);
						msb.setOnChangeListener(new mySeekBar.OnChange(){
									@Override public void onChange(mySeekBar msb, int p)
										{
											//mc.setColor(Color.rgb(p%65536,p%256,p));
											if (p == msb.getMax())
												{mc.clear();}
											if (p == 0)mc.saveCanvas();
										}
									@Override public void onDown(mySeekBar msb)
										{}
									@Override public void onUp(mySeekBar msb)
										{}
								});

					}
				catch (Exception e)
					{util.toast(this, e.toString());}
			}
		public void showDialog(View v)
			{
				try
					{
						new myAlertDialog(this)
							.setMessage("信息\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n下面还有\n\n\n\n\n")
							.setPositiveButton("确定", new myDialogInterface(){@Override public void click(View p1, int p3)
										{
											music(null);
										}})
							.setNegativeButton("取消", null)
							.setNeutralButton("其他", null)
							.setTitle("标题")
							.show();
					}
				catch (Exception e)
					{util.toast(this, e.toString());}
			}
		public void music(View v)
			{
				startActivity(new Intent(this, musicplayer.class));
			}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event)
			{
				// TODO: Implement this method
				if (keyCode == KeyEvent.KEYCODE_BACK && msm.getIsOpen())
					{
						msm.closeMenu();
						return true;
					}
				if (keyCode == KeyEvent.KEYCODE_MENU)
					{
						msm.toggle();
					}
				return super.onKeyDown(keyCode, event);
			}
		public void icon(View v)
			{
				startActivity(new Intent(this,iconView.class));
			}*/
	}
