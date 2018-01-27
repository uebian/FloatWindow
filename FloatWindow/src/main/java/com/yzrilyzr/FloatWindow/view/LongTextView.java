package com.yzrilyzr.FloatWindow.view;

import android.view.inputmethod.*;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.yzrilyzr.icondesigner.VECfile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

public class LongTextView extends View implements InputConnection
{
	String CRLF="\n";
	ArrayList<String> menuKey=new ArrayList<String>();
	HashMap<String,VECfile> menuIcon=new HashMap<String,VECfile>();
	HashMap<String,Syntax> syntax=new HashMap<String,Syntax>();
	Syntax curSyntax=null;
	float menuOff=0,menudyOff,menuYvel,menuLastY;
	boolean menuTouch=false;
	RectF menu;
	int suggestionStart=0,suggestionEnd=0;
	float lineNumWidth;
	mPaint pa;
	float xOff=0,yOff=0,dxOff=0,dyOff=0,xVel=0,yVel=0,lastX,lastY;
	float th;
	int cursorC=0;
	ArrayList<String> stringLines;
	boolean isEdit=false,isTouch=false;
	public boolean ViewMode=false;
	boolean longClick=false,isSelection=false;
	int showHan=0,touchHan=-1;
	long LongClickMillis,doubleClickMillis;
	Path han=new Path(),hanl=new Path(),hanr=new Path();
	Cursor[] cursors=new Cursor[]{new Cursor(),new Cursor()};
	ConcurrentHashMap<String,CopyOnWriteArrayList<Span>> span=new ConcurrentHashMap<String,CopyOnWriteArrayList<Span>>();
	CopyOnWriteArrayList<Span> nowspan=new CopyOnWriteArrayList<Span>();
	static int textColor=0xffffffff,
	selectionColor=0xff60c0ff,
	backColor=0xff000000,
	lineNumColor=0xffaaaaaa,
	lineNumBackColor=0xff202020,
	lineNumLineColor=0xffffffff,
	curLineColor=0xff404040,
	cursorColor=0xff5050ff,
	cursorLeftColor=0xffff5050,
	cursorRightColor=0xff50ff50,
	cursorLineColor=0xffffffff,
	enterColor=0xffc0c0c0,
	suggBackColor=0xff101010,
	suggTextColor=0xffffffff,
	suggLineColor=0xffffffff,
	textSize=14,
	textTypeface;
	public void findUp(String to)
	{
		int li=cursors[0].line,in=cursors[0].index;
		String[] ks=split(to);
		boolean b=true;
		int i2=0,l2=0,i3=0,l3=0;
		while(li>=0&&b)
		{
			StringBuilder sb=new StringBuilder();
			for(int i=li-ks.length+1;i<=li;i++)
				if(i==li&&in!=-1)sb.append(getLine(i).substring(0,in));
				else sb.append(getLine(i)).append(CRLF);
			int k=sb.lastIndexOf(to);
			if(k!=-1)
			{
				l3=li;
				i2=k;
				l2=li-ks.length+1;
				b=false;
				if(ks.length==1)i3=k+to.length();
				else i3=ks[ks.length-1].length();
			}
			li--;
			in=-1;
		}
		if(b)
		{
			Toast.makeText(getContext(),"在此之前找不到:"+to,0).show();
			return;
		}
		isSelection=true;
		cursors[0].line=l2;
		cursors[0].index=i2;
		cursors[1].line=l3;
		cursors[1].index=i3;
		while(cursors[0].x+xOff<th)xOff++;
		while(cursors[0].x+xOff>getWidth()-th)xOff--;
		while(cursors[0].line*th+yOff<0)yOff+=th/2;
		while(cursors[0].line*th+yOff+th>getHeight()-3*th)yOff-=th/2;
	}
	public void findDown(String to)
	{
		int li=cursors[0].line,in=cursors[0].index;
		if(isSelection)
		{
			li=cursors[1].line;in=cursors[1].index;
		}
		String[] ks=split(to);
		boolean b=true;
		int i2=0,l2=0,i3=0,l3=0;
		while(li<stringLines.size()&&b)
		{
			StringBuilder sb=new StringBuilder();
			for(int i=li;i<li+ks.length;i++)
				sb.append(getLine(i)).append(CRLF);
			int k=sb.indexOf(to,in);
			if(k!=-1)
			{
				l2=li;
				i2=k;
				l3=li+ks.length-1;
				b=false;
				if(ks.length==1)i3=k+to.length();
				else i3=ks[ks.length-1].length();
			}
			li++;
			in=0;
		}
		if(b)
		{
			Toast.makeText(getContext(),"在此之后找不到:"+to,0).show();
			return;
		}
		isSelection=true;
		cursors[0].line=l2;
		cursors[0].index=i2;
		cursors[1].line=l3;
		cursors[1].index=i3;
		while(cursors[0].x+xOff<th)xOff++;
		while(cursors[0].x+xOff>getWidth()-th)xOff--;
		while(cursors[0].line*th+yOff<0)yOff+=th/2;
		while(cursors[0].line*th+yOff+th>getHeight()-3*th)yOff-=th/2;
	}
	public void clear()
	{
		stringLines.clear();
	}
	class Cursor
	{
		public int index;
		public float x;
		public int line;
		public void computeLineIndex(float x,float y)
		{
			line=(int)((-yOff+y)/th);
			computeX2Index(x);
		}
		public void computeX2Index(float xx)
		{
			x=xx-xOff-lineNumWidth;
			String curLineStr=getLine(line);
			float w=0;
			int max=curLineStr.length(),min=0,mid=(max+min)/2;
			while(w!=x&&max>=min)
			{
				w=pa.measureText(curLineStr,0,mid);
				if(w>x)max=mid-1;
				else if(w<x)min=mid+1;
				mid=(min+max)/2;
			}
			index=mid;
		}
		public void computeX2Index()
		{
			computeX2Index(x+xOff);
		}
		public String computeIndex()
		{
			String curlineStr=getLine(line);
			if(index<0&&line!=0)
			{
				line--;
				curlineStr=getLine(line);
				index=curlineStr.length();
			}
			if(index>curlineStr.length()&&line!=stringLines.size()-1)
			{
				index=0;
				line++;
				curlineStr=getLine(line);
			}
			if(index<0)index=0;
			if(index>curlineStr.length())index=curlineStr.length();
			return curlineStr;
		}
		public void computeLine()
		{
			if(line<0)line=0;
			if(line>=stringLines.size())line=stringLines.size()-1;
		}
		public void computeCursorX()
		{
			x=pa.measureText(getLine(line).substring(0,index))+lineNumWidth;
		}
	}
	@Override
	public CharSequence getTextBeforeCursor(int p1, int p2)
	{
		// TODO: Implement this method
		return null;
	}
	@Override
	public CharSequence getTextAfterCursor(int p1, int p2)
	{
		// TODO: Implement this method
		return null;
	}
	@Override
	public CharSequence getSelectedText(int p1)
	{
		// TODO: Implement this method
		return null;
	}
	@Override
	public int getCursorCapsMode(int p1)
	{
		// TODO: Implement this method
		return 0;
	}
	@Override
	public ExtractedText getExtractedText(ExtractedTextRequest p1, int p2)
	{
		// TODO: Implement this method
		return null;
	}
	@Override
	public boolean deleteSurroundingText(int p1, int p2)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean setComposingText(CharSequence p1, int p2)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean setComposingRegion(int p1, int p2)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean finishComposingText()
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean commitText(CharSequence p1, int p2)
	{
		computeSelection(true);
		menuKey.clear();
		{
			String[] lll=split(p1.toString());
			String c=getCurrentLine();
			String a=c.substring(0,cursors[0].index),b=c.substring(cursors[0].index);
			if(lll.length==1)
			{
				setCurrentLine(a+lll[0]+b);
				cursors[0].index+=lll[0].length();
			}
			else
				for(int i=0;i<lll.length;i++)
				{
					if(i==0)
					{
						setLine(cursors[0].line,a+lll[i]);
					}
					else if(i==lll.length-1)
					{
						stringLines.add(cursors[0].line+i,lll[i]+b);
						cursors[0].line+=i;
						cursors[0].index=lll[i].length();
					}
					else
					{
						stringLines.add(cursors[0].line+i,lll[i]);
					}
				}
			suggestion();
		}
		isEdit=true;
		return false;
	}
	void delete()
	{
		if(!isSelection)
		{
			StringBuffer sb=new StringBuffer()
				.append(getCurrentLine());
			if(cursors[0].index==0)
			{
				if(cursors[0].line!=0)
				{
					stringLines.remove(cursors[0].line);
					cursors[0].line--;
					String ll=getCurrentLine();
					cursors[0].index=ll.length();
					setCurrentLine(ll+sb.toString());
				}
			}
			else
			{
				sb.delete(cursors[0].index-1,cursors[0].index);
				cursors[0].index--;
				setCurrentLine(sb.toString());
			}
		}
		else computeSelection(true);
		suggestion();
	}
	void suggestion()
	{
		if(curSyntax==null)return;
		Matcher m=Pattern.compile("\\b\\w+\\b").matcher(getCurrentLine());
		while(m.find())
			if(m.start()<=cursors[0].index&&m.end()>=cursors[0].index)
			{
				if(m.start()==suggestionStart&&m.end()>=suggestionEnd&&menuKey.size()!=0)
				{
					String u=m.group().toLowerCase();
					for(String k:menuKey)
						if(!k.substring(1).toLowerCase().startsWith(u))menuKey.remove(k);
				}
				else
				{
					String u=m.group().toLowerCase();
					for(String k:curSyntax.keys)
						if(k.substring(1).toLowerCase().startsWith(u))menuKey.add(k);
				}
				suggestionStart=m.start();
				suggestionEnd=m.end();
				break;
			}
	}
	String computeSelection(boolean delete)
	{
		StringBuffer selected=new StringBuffer();
		if(!isSelection)return "";
		if(cursors[0].line==cursors[1].line)
		{
			StringBuffer s=new StringBuffer()
				.append(getCurrentLine());
			selected.append(s.substring(cursors[0].index,cursors[1].index));
			if(delete)s.delete(cursors[0].index,cursors[1].index);
			setCurrentLine(s.toString());
		}
		else
		{
			int l1=cursors[0].line,l2=cursors[1].line,i1=cursors[0].index,i2=cursors[1].index;
			for(int i=l1;i<=l2;i++)
			{
				if(i==l1)
				{
					String b=getLine(i);
					selected.append(b.substring(i1)).append(CRLF);
					if(delete)setLine(i,b.substring(0,i1));
				}
				else if(i==l2)
				{
					String b=getLine(i),c=getLine(l1);
					selected.append(b.substring(0,i2));
					if(delete)
					{
						setLine(i-1,c+b.substring(i2));
						stringLines.remove(i);
					}
				}
				else
				{ 
					selected.append(getLine(i)).append(CRLF);
					if(delete)
					{
						stringLines.remove(i);
						cursors[1].line--;
						l2--;
						i--;
					}
				}
			}
		}
		if(delete)isSelection=false;
		return selected.toString();
	}
	@Override
	public boolean commitCompletion(CompletionInfo p1)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean commitCorrection(CorrectionInfo p1)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean setSelection(int p1, int p2)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean performEditorAction(int p1)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean performContextMenuAction(int p1)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean beginBatchEdit()
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean endBatchEdit()
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean sendKeyEvent(KeyEvent p1)
	{
		// TODO: Implement this method
		if(p1.getAction()==KeyEvent.ACTION_DOWN)
		{
			isEdit=true;
			menuKey.clear();
			menuOff=0;
			int act=p1.getKeyCode();
			if(act==KeyEvent.KEYCODE_DPAD_RIGHT)cursors[0].index++;
			else if(act==KeyEvent.KEYCODE_DPAD_LEFT)cursors[0].index--;
			else if(act==KeyEvent.KEYCODE_DPAD_UP)
			{cursors[0].line--;cursors[0].computeX2Index();}
			else if(act==KeyEvent.KEYCODE_DPAD_DOWN)
			{cursors[0].line++;cursors[0].computeX2Index();}
			else if(act==KeyEvent.KEYCODE_DPAD_CENTER);
			else if(act==KeyEvent.KEYCODE_DEL)delete();
			else if(act==KeyEvent.KEYCODE_ENTER)commitText(CRLF,0);
		}
		return true;
	}
	@Override
	public boolean clearMetaKeyStates(int p1)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean reportFullscreenMode(boolean p1)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean performPrivateCommand(String p1, Bundle p2)
	{
		// TODO: Implement this method
		return false;
	}
	@Override
	public boolean requestCursorUpdates(int p1)
	{
		// TODO: Implement this method
		return false;
	}
	public LongTextView(Context ctx,AttributeSet a)
	{
		super(ctx,a);
		stringLines=new ArrayList<String>();
		pa=new mPaint(Paint.ANTI_ALIAS_FLAG);
		pa.setTextSize(dip2px(textSize));
		th=pa.getTextSize()*1.2f;
		pa.setStyle(Paint.Style.FILL);
		pa.setTypeface(Typeface.MONOSPACE);
		readSyntax();
	}
	void readSyntax()
	{
		try
		{
			menuIcon.put("C",VECfile.readFileFromIs(getContext().getAssets().open("class.vec")));
			menuIcon.put("F",VECfile.readFileFromIs(getContext().getAssets().open("field.vec")));
			menuIcon.put("M",VECfile.readFileFromIs(getContext().getAssets().open("method.vec")));
			menuIcon.put("P",VECfile.readFileFromIs(getContext().getAssets().open("package.vec")));
			BufferedReader is=new BufferedReader(new InputStreamReader(getContext().getAssets().open("syntax.java")));
			String b=null;
			Syntax syn=null;
			int mode=0;
			ArrayList<String> group=null;
			while((b=is.readLine())!=null)
			{
				if(b.startsWith("#"))continue;
				else if(b.startsWith("Syntax:"))
				{
					mode=1;
					syntax.put(b.substring(7),syn=new Syntax());
				}
				else if(b.startsWith("KeyGroup:"))
				{
					mode=2;
					String n=b.substring(9);
					syn.group.put(n,group=new ArrayList<String>());
					syn.order.add(n);
				}
				else if(mode==1)
				{
					syn.colors.add((int)Long.parseLong(b.substring(0,8),16));
					syn.regexp.add(b.substring(9));
				}
				else if(mode==2)group.add(b);
			}
			Pattern p=Pattern.compile("@\\[.*?,.*?\\]#");
			for(Syntax sy:syntax.values())
			{
				StringBuilder patt=new StringBuilder();
				for(int i=0;i<sy.regexp.size();i++)
				{
					String f=sy.regexp.get(i);
					Matcher m=p.matcher(f);
					while(m.find())
					{
						String[] st=m.group().split(",");
						String k=st[0].substring(2),ch=st[1].substring(0,st[1].length()-2);
						StringBuilder b2=new StringBuilder();
						ArrayList<String> ks=sy.group.get(k);
						for(String h:ks)b2.append(h).append(ch);
						f=f.replace(m.group(),b2.substring(0,b2.length()-1));
					}
					sy.regexp.set(i,f);
					patt.append(f);
					if(i!=sy.regexp.size()-1)patt.append("|");
					sy.patterns.add(Pattern.compile(f));
				}
				sy.pattern=Pattern.compile(patt.toString());
				for(String s:sy.order)
				{
					ArrayList<String> ar=sy.group.get(s);
					Collections.sort(ar);
					String t=s.substring(0,1);
					for(String a:ar)sy.keys.add(t+a);
				}
				sy.order.clear();
				sy.group.clear();
				sy.regexp.clear();
			}
			is.close();
		}
		catch(Exception e)
		{
			setText("语法文件解析失败:"+e);
		}
		curSyntax=syntax.get("Java");
	}
	public LongTextView(Context ctx)
	{
		this(ctx,null);
	}
	public static int dip2px(float dipValue)
	{
		final float scale = Resources.getSystem().getDisplayMetrics().density;
		return	(int)(dipValue * scale + 0.5f);
	}
	String getCurrentLine()
	{
		cursors[0].computeLine();
		return stringLines.get(cursors[0].line);
	}
	String getLine(int i)
	{
		if(i<0)i=0;
		if(i>=stringLines.size())i=stringLines.size() -1;
		return stringLines.get(i);
	}
	void setCurrentLine(String text)
	{
		cursors[0].computeLine();
		stringLines.set(cursors[0].line,text);
	}
	void setLine(int i,String text)
	{
		if(i<0)i=0;
		if(i>=stringLines.size())i=stringLines.size() -1;
		stringLines.set(i,text);
	}
	public String getText()
	{
		StringBuffer s=new StringBuffer();
		for(String a:stringLines)
			s.append(a).append(CRLF);
		return s.toString();
	}
	public void setText(String text)
	{
		try
		{
			stringLines.clear();
			String[] sd=split(text);
			for(String s:sd)stringLines.add(s);
		}
		catch(OutOfMemoryError e)
		{
			Toast.makeText(getContext(),"内存不足",0).show();
		}
	}
	public void addText(String text)
	{
		try
		{
			stringLines.add(text);
		}
		catch(OutOfMemoryError e)
		{
			Toast.makeText(getContext(),"内存不足",0).show();
		}
	}
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs)
	{
		outAttrs.imeOptions = EditorInfo.TYPE_CLASS_TEXT;
		outAttrs.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
		return this;
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		int w=getWidth(),h=getHeight();
		canvas.drawColor(backColor);
		//限定坐标
		if(xOff>0)xOff=0;
		if(yOff>0)yOff=0;
		if(yOff<-stringLines.size()*th)yOff=-stringLines.size()*th;
		try
		{
			lineNumWidth=pa.measureText(Integer.toString(stringLines.size()));
			float yA=yOff%th;
			cursors[0].computeLine();
			cursors[0].computeIndex();
			cursors[1].computeLine();
			cursors[1].computeIndex();
			if(!isSelection)
			{
				pa.setColor(curLineColor);
				canvas.drawRect(0,cursors[0].line*th+yOff,w,cursors[0].line*th+th+yOff,pa);
				pa.setColor(cursorC<20||isEdit?cursorLineColor:0x00000000);
				cursors[0].computeCursorX();
				canvas.drawRect(cursors[0].x+xOff,cursors[0].line*th+yOff,cursors[0].x+xOff+3,cursors[0].line*th+th+yOff,pa);
				float hx=cursors[0].x+xOff,hy=cursors[0].line*th+th+yOff;
				float s=dip2px(10);
				Path p=han;
				p.reset();
				p.moveTo(hx,hy);
				p.lineTo(hx+s,hy+s);
				p.lineTo(hx+s,hy+4*s);
				p.lineTo(hx-s,hy+4*s);
				p.lineTo(hx-s,hy+s);
				p.close();
			}
			else
			{
				cursors[0].computeCursorX();
				cursors[1].computeCursorX();
				int l1=cursors[0].line,l2=cursors[1].line,i1=cursors[0].index,i2=cursors[1].index;
				if(l2<l1||(l2==l1&&i2<i1))
				{
					Cursor c=cursors[0];
					cursors[0]=cursors[1];
					cursors[1]=c;
					l1=cursors[0].line;
					l2=cursors[1].line;
					i1=cursors[0].index;
					i2=cursors[1].index;
				}
				pa.setColor(selectionColor);
				if(l1==l2)canvas.drawRect(pa.measureText(getLine(l1),0,Math.min(i1,i2))+xOff+lineNumWidth,l1*th+yOff,pa.measureText(getLine(l1),0,Math.max(i1,i2))+xOff+lineNumWidth,l2*th+th+yOff,pa);
				else
					for(int i=(int)(-yOff/th);i<(-yOff+h)/th;i++)
					{
						float yyy=i*th+yOff;
						if(yyy<-th||i<l1)continue;
						if(yyy>h||i>l2)break;
						String li=getLine(i);
						if(i==l1)canvas.drawRect(pa.measureText(li,0,i1)+xOff+lineNumWidth,yyy,pa.measureText(li)+xOff+lineNumWidth,yyy+th,pa);
						else if(i==l2)canvas.drawRect(lineNumWidth+xOff,yyy,pa.measureText(li,0,i2)+xOff+lineNumWidth,yyy+th,pa);
						else canvas.drawRect(xOff+lineNumWidth,yyy,pa.measureText(li)+xOff+lineNumWidth,yyy+th,pa);
					}
				float hx=cursors[0].x+xOff,hy=cursors[0].line*th+th+yOff;
				float hx1=cursors[1].x+xOff,hy1=cursors[1].line*th+th+yOff;
				float s=dip2px(10);
				Path p=hanl;
				p.reset();
				p.moveTo(hx,hy);
				p.lineTo(hx,hy+4*s);
				p.lineTo(hx-s*2,hy+4*s);
				p.lineTo(hx-s*2,hy+s);
				p.close();
				p=hanr;
				p.reset();
				p.moveTo(hx1,hy1);
				p.lineTo(hx1,hy1+4*s);
				p.lineTo(hx1+s*2,hy1+4*s);
				p.lineTo(hx1+s*2,hy1+s);
				p.close();
			}
			//跟随光标
			if(isEdit)
			{
				int t=touchHan==2?1:0;
				while(cursors[t].x+xOff<th)xOff++;
				while(cursors[t].x+xOff>w-th)xOff--;
				while(cursors[t].line*th+yOff<0)yOff+=th/2;
				while(cursors[t].line*th+yOff+th>h-3*th)yOff-=th/2;
				showHan=300;
			}
			//text
			if(xOff>0)xOff=0;
			if(yOff>0)yOff=0;
			if(yOff<-stringLines.size()*th)yOff=-stringLines.size()*th;
			String s=null;
			for(int i=(int)(-yOff/th),l=1;i<(-yOff+h)/th&&i>=0&&i<stringLines.size();i++,l++)
			{
				s=stringLines.get(i);
				if(s.length()<1000)
				{
					pa.setColor(textColor);
					canvas.drawText(s.replace("	","    "),lineNumWidth+xOff,l*th+yA,pa);
					if(curSyntax!=null)
					{
						CopyOnWriteArrayList<Span> gg=span.get(Integer.toString(i));
						if(cursors[0].line==i)gg=nowspan;
						if(gg!=null)
							for(Span spp:gg)
							{
								float xx=spp.x+lineNumWidth+xOff;
								if(xx>w)break;
								pa.setColor(spp.color);
								canvas.drawText(spp.ch.replace("	","    "),xx,l*th+yA,pa);
							}
					}
					pa.setColor(enterColor);
					canvas.drawText("↓",lineNumWidth+xOff+pa.measureText(s),l*th+yA,pa);
				}
				else
				{
					pa.setColor(enterColor);
					canvas.drawText("<无法显示:单行太长>",lineNumWidth+xOff,l*th+yA,pa);
				}
			}
			pa.setColor(lineNumBackColor);
			canvas.drawRect(0,0,lineNumWidth,h,pa);
			pa.setColor(lineNumLineColor);
			canvas.drawLine(lineNumWidth,0,lineNumWidth,h,pa);
			pa.setColor(lineNumColor);
			for(int i=(int)(-yOff/th),l=1;i<(-yOff+h)/th&&i>=0&&i<stringLines.size();i++,l++)
				canvas.drawText(Integer.toString(i+1),0,l*th+yA,pa);
			if(isSelection)
			{
				pa.setColor(cursorLeftColor);
				canvas.drawPath(hanl,pa);
				pa.setColor(cursorRightColor);
				canvas.drawPath(hanr,pa);
			}
			else if(showHan>0)
			{
				pa.setColor(cursorColor);
				canvas.drawPath(han,pa);
			}
			if(menuKey.size()!=0)
			{
				canvas.save();
				float hA=th*2f;
				if(menuOff<-hA*menuKey.size()+4f*hA)menuOff=-hA*menuKey.size()+4f*hA;
				if(menuOff>0)menuOff=0;
				float top=cursors[0].line*th+th*2+yOff;
				menu=new RectF(0,top,w,top+4f*hA);
				canvas.clipRect(menu);
				pa.setColor(suggBackColor);
				canvas.drawRoundRect(menu,dip2px(2),dip2px(2),pa);
				float starty=0;
				for(int o=0;o<menuKey.size();o++)
				{
					starty=top+hA*(o+1)+menuOff;
					if(starty<menu.top)continue;
					if(starty-hA>menu.bottom)break;
					pa.setColor(suggTextColor);
					s=menuKey.get(o);
					VECfile vec=menuIcon.get(s.substring(0,1));
					if(vec!=null)
						for(com.yzrilyzr.icondesigner.Shape sh:vec.shapes)
							sh.onDraw(canvas,vec.antialias,vec.dither,0,(starty-hA)/hA*(float)vec.width,hA/(float)vec.width,vec.dp,vec.sp);
					canvas.drawText(s.substring(1),vec==null?0:hA,starty-hA/2f,pa);
					pa.setColor(suggLineColor);
					canvas.drawLine(0,starty,w,starty,pa);
				}
				canvas.restore();
			}
			//vel
			if(!isTouch)
			{
				xOff+=(xVel*=0.95);
				yOff+=(yVel*=0.95);
				menuOff+=(menuYvel*=0.95);
			}
		}
		catch(Throwable e)
		{
		}
		//cursor闪动
		if(cursorC++>=40)
		{
			cursorC=0;
			isEdit=false;
		}
		//拖动指针
		if(showHan>0)showHan--;
		invalidate();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		getParent().requestDisallowInterceptTouchEvent(true);
		float x=event.getX(),y=event.getY();
		if(event.getAction()==MotionEvent.ACTION_DOWN)
			if(menuKey.size()!=0&&menu!=null&&menu.contains(x,y))menuTouch=true;
			else menuTouch=false;
		if(!menuTouch)
		{
			menuKey.clear();
			menuOff=0;
			switch(event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					dxOff=x;
					dyOff=y;
					isTouch=true;
					//长按
					longClick=false;
					LongClickMillis=System.currentTimeMillis();
					touchHan=-1;
					//点击拖动
					RectF bounds = new RectF();
					han.computeBounds(bounds, true);
					RectF bounds2 = new RectF();
					hanl.computeBounds(bounds2, true);
					RectF bounds3 = new RectF();
					hanr.computeBounds(bounds3, true);
					if(bounds.contains((int)x,(int)y))touchHan=1;
					else if(bounds2.contains((int)x,(int)y))touchHan=0;
					else if(bounds3.contains((int)x,(int)y))touchHan=2;
					lastX=x;
					lastY=y;

					break;
				case MotionEvent.ACTION_UP:
					isTouch=false;
					if(Math.abs(x-lastX)<10&&Math.abs(y-lastY)<10&&!ViewMode&&!longClick)
					{
						if(System.currentTimeMillis()-doubleClickMillis<200)
						{
							isSelection=!isSelection;
							if(isSelection)
							{
								cursors[1].line=cursors[0].line;
								cursors[1].x=cursors[0].x;
								cursors[1].index=cursors[0].index;
								Matcher m=Pattern.compile("\\b\\w+\\b").matcher(getCurrentLine());
								while(m.find())
									if(m.start()<cursors[0].index&&m.end()>cursors[0].index)
									{
										cursors[0].index=m.start();
										cursors[1].index=m.end();
										break;
									}
							}
						}
						doubleClickMillis=System.currentTimeMillis();
						openIME();
						//touchP2Cursor(x,y);
						if(!isSelection&&touchHan==-1)
						{
							cursors[0].computeLineIndex(x,y);
							showHan=300;
						}
						//isSelection=false;
					}
					else
					{
						isEdit=false;
					}
					touchHan=-1;
					break;
				case MotionEvent.ACTION_MOVE:
					if(touchHan==-1)
					{
						xVel=(x-dxOff)*2;
						xOff+=x-dxOff;
						dxOff=x;
						yVel=(y-dyOff)*2;
						yOff+=y-dyOff;
						dyOff=y;
						isEdit=false;
					}
					//选择拖动条
					if(touchHan==0)
					{
						cursors[0].computeLineIndex(x,y-120);
						showHan=300;
						isEdit=true;
					}
					if(touchHan==2)
					{
						cursors[1].computeLineIndex(x,y-120);
						showHan=300;
						isEdit=true;
					}
					if(touchHan==1)
					{
						cursors[0].computeLineIndex(x,y-120);
						showHan=300;
						isEdit=true;
					}
					break;
			}
			if(System.currentTimeMillis()-LongClickMillis>500&&!longClick&&
			   Math.abs(x-lastX)<10&&Math.abs(y-lastY)<10)
			{
				longClick=true;
				new AlertDialog.Builder(getContext())
					.setItems(!isSelection?
					("全选,粘贴,跳转到开头,跳转到结尾,跳转至…,插入时间,字数统计").split(","):
					("全选,剪切,复制,粘贴,重复选中的文本,转为大写,转为小写,首字母大写,跳转到开头,跳转到结尾,跳转至…,插入时间,字数统计").split(","),
					new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							// TODO: Implement this method
							clicki(p2);
						}
					})
					.show();
			}
		}
		else
			switch(event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					isTouch=true;
					menudyOff=y;
					menuLastY=y;
					break;
				case MotionEvent.ACTION_MOVE:
					menuYvel=(y-menudyOff)*2;
					menuOff+=y-menudyOff;
					menudyOff=y;
					break;
				case MotionEvent.ACTION_UP:
					isTouch=false;
					if(Math.abs(y-menuLastY)<10)
					{
						int w=(int)Math.floor((y-menuOff-menu.top)/th/2f);
						if(w>=0&&w<menuKey.size())
						{
							StringBuilder cl=new StringBuilder().append(getCurrentLine());
							String k=menuKey.get(w).substring(1);
							StringBuilder cl2=new StringBuilder()
								.append(cl.substring(0,suggestionStart))
								.append(k)
								.append(cl.substring(suggestionEnd));
							setCurrentLine(cl2.toString());
							cursors[0].index=suggestionStart+k.length();
						}
						menuKey.clear();
					}
					break;
			}
		return true;
	}
	public void hideIME()
	{
		((InputMethodManager)getContext(). getSystemService(Context.INPUT_METHOD_SERVICE)). 
			hideSoftInputFromWindow(getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
	}
	public void openIME()
	{
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		InputMethodManager ime=((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
		ime.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
	}
	public void clicki(int i)
	{
		try
		{
			if(i==0)
			{
				isSelection=true;
				cursors[1].line=stringLines.size()-1;
				cursors[0].line=0;
				cursors[1].index=getLine(stringLines.size()-1).length();
				cursors[0].index=0;
			}
			else if((i==1&&!isSelection)||(i==3&&isSelection))commitText(paste(),0);
			else if((i==2&&!isSelection)||(i==8&&isSelection))
			{
				isEdit=true;
				isSelection=false;
				cursors[0].line=0;
				cursors[0].index=0;
			}
			else if((i==3&&!isSelection)||(i==9&&isSelection))
			{
				isEdit=true;
				isSelection=false;
				cursors[0].line=stringLines.size()-1;
				cursors[0].index=getLine(stringLines.size()-1).length();
			}
			else if((i==4&&!isSelection)||(i==10&&isSelection))
			{
				final EditText e=new EditText(getContext());
				e.setInputType(InputType.TYPE_CLASS_NUMBER);
				new AlertDialog.Builder(getContext())
					.setView(e)
					.setTitle("跳转至…(共"+stringLines.size()+"行)")
					.setPositiveButton("跳转",new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							// TODO: Implement this method
							try
							{
								cursors[0].line=Integer.parseInt(e.getText().toString())-1;
								cursors[0].index=0;
								isEdit=true;
							}
							catch(Throwable ee)
							{}
						}
					})
					.setNegativeButton("取消",null)
					.show();
			}
			else if((i==5&&!isSelection)||(i==11&&isSelection))commitText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())),0);
			else if((i==6&&!isSelection)||(i==12&&isSelection))
			{
				String a=getText();
				int[] l={0,0,0,0,0,0};
				for(int is=0;is<a.length();is++)
				{
					char ch=a.charAt(is);
					if(ch=='(')l[0]++;
					else if(ch==')')l[1]++;
					else if(ch=='{')l[2]++;
					else if(ch=='}')l[3]++;
					else if(ch=='[')l[4]++;
					else if(ch==']')l[5]++;
				}
				new AlertDialog.Builder(getContext())
					.setMessage(
					"字符数:"+a.length()+CRLF+
					"单词数:"+(a.split("\\w+").length-1)+CRLF+
					"行数:"+stringLines.size()+CRLF+
					"()数:"+l[0]+"|"+l[1]+CRLF+
					"{}数:"+l[2]+"|"+l[3]+CRLF+
					"[]数:"+l[4]+"|"+l[5]+CRLF
				).show();
			}
			else if(isSelection)
			{
				if(i==1)copy(computeSelection(true));
				if(i==2)copy(computeSelection(false));
				if(i==4)
				{
					int l1=cursors[1].line;
					int l0=cursors[0].line;
					int i0=cursors[0].index;
					String s=computeSelection(true);
					commitText(s,0);
					commitText(s,0);
					isSelection=true;
					String[] a=split(s);
					cursors[1].line=l1+a.length-1;
					cursors[0].line=l0;
					if(a.length==1)cursors[1].index+=s.length();
					else cursors[1].index=a[a.length-1].length();
					cursors[0].index=i0;
				}
				if(i==5)
				{
					int l1=cursors[1].line;
					int l0=cursors[0].line;
					int i1=cursors[1].index;
					int i0=cursors[0].index;
					commitText(computeSelection(true).toUpperCase(),0);
					isSelection=true;
					cursors[1].line=l1;
					cursors[0].line=l0;
					cursors[1].index=i1;
					cursors[0].index=i0;
				}
				if(i==6)
				{
					int l1=cursors[1].line;
					int l0=cursors[0].line;
					int i1=cursors[1].index;
					int i0=cursors[0].index;
					commitText(computeSelection(true).toLowerCase(),0);
					isSelection=true;
					cursors[1].line=l1;
					cursors[0].line=l0;
					cursors[1].index=i1;
					cursors[0].index=i0;
				}
				if(i==7)
				{
					int l1=cursors[1].line;
					int l0=cursors[0].line;
					int i1=cursors[1].index;
					int i0=cursors[0].index;
					StringBuilder s=new StringBuilder();
					s.append(computeSelection(true));
					s.setCharAt(0,new String(new char[]{s.charAt(0)}).toUpperCase().charAt(0));
					commitText(s,0);
					isSelection=true;
					cursors[1].line=l1;
					cursors[0].line=l0;
					cursors[1].index=i1;
					cursors[0].index=i0;
				}
			}
		}
		catch(Exception e)
		{}
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO: Implement this method
		setMeasuredDimension(measure(widthMeasureSpec,getWidth()),measure(heightMeasureSpec,dip2px(600)));
	}
	public static int measure(int measureSpec, int def)
	{
		int result = 0;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		if (mode == MeasureSpec.EXACTLY)result = size;
		else
		{
			result = def;
			if(mode == MeasureSpec.AT_MOST)result=Math.min(result, size);
		}
		return result;
	}
	void highLight()
	{
		for(int i=(int)(-yOff/th);i<(-yOff+getHeight())/th&&i>=0&&i<stringLines.size()&&curSyntax!=null;i++)
		{
			CopyOnWriteArrayList<Span> s=new CopyOnWriteArrayList<Span>();
			String l=getLine(i);
			Matcher m=curSyntax.pattern.matcher(l);
			while(m.find())
			{
				String f=m.group();
				for(int d=0;d<curSyntax.patterns.size();d++)
					if(curSyntax.patterns.get(d).matcher(f).matches())
						s.add(new Span(curSyntax.colors.get(d),pa.measureText(l,0,m.start()),f));
			}
			span.put(Integer.toString(i),s);
		}
	}
	void highLightNow()
	{
		if(curSyntax!=null)
		{
			CopyOnWriteArrayList<Span> s=new CopyOnWriteArrayList<Span>();
			String l=getCurrentLine();
			Matcher m=curSyntax.pattern.matcher(l);
			while(m.find())
			{
				String f=m.group();
				for(int d=0;d<curSyntax.patterns.size();d++)
					if(curSyntax.patterns.get(d).matcher(f).matches())
						s.add(new Span(curSyntax.colors.get(d),pa.measureText(l,0,m.start()),f));
			}
			nowspan=s;
		}
	}
	String[] split(String s)
	{
		int len=1,ind=0;
		while((ind=s.indexOf(CRLF,ind))!=-1)
		{
			len++;
			ind++;
		}
		String[] r=new String[len];
		for(int i=0,l=0,g=0;i<len;i++)
		{
			l=s.indexOf(CRLF,l);
			if(l!=-1)r[i]=s.substring(g,l);
			else r[i]=s.substring(g);
			g=l+++1;
		}
		return r;
	}
	String paste()
	{
		return ((ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE)).getText().toString();
	}
	void copy(String s)
	{
		((ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE)).setText(s);
	}
	static class Span
	{
		int color;
		float x;
		String ch;

		public Span(int color, float x, String ch)
		{
			this.color = color;
			this.x = x;
			this.ch = ch;
		}
	}
	static class mPaint extends Paint
	{
		public mPaint(int f)
		{
			super(f);
		}
		@Override
		public float measureText(String text)
		{
			int c=0,i=0;
			while((i=text.indexOf("	",i))!=-1)
			{c++;i++;}
			return super.measureText(text.replace("	",""))+super.measureText("    ")*c;
		}
		@Override
		public float measureText(String text, int start, int end)
		{
			int c=0,i=start;
			while((i=text.indexOf("	",i))!=-1&&i<end)
			{c++;i++;}
			return super.measureText(text.replace("	",""), start, end-c)+super.measureText("    ")*c;
		}
	}
	static class Syntax
	{
		ArrayList<Integer> colors=new ArrayList<Integer>();
		ArrayList<Pattern> patterns=new ArrayList<Pattern>();
		Pattern pattern;
		ArrayList<String> regexp=new ArrayList<String>();
		ArrayList<String> keys=new ArrayList<String>();
		ArrayList<String> order=new ArrayList<String>();
		HashMap<String,ArrayList<String>> group=new HashMap<String,ArrayList<String>>();
	}
}

