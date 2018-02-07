package com.yzrilyzr.floatingwindow;
import android.view.*;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import com.yzrilyzr.icondesigner.VecView;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myTextView;
import com.yzrilyzr.ui.myTextViewBack;
import com.yzrilyzr.ui.uidata;
public class Window implements View.OnClickListener,View.OnTouchListener,View.OnLongClickListener
{
    private Context ctx;
    private WindowManager window;
    private WindowManager.LayoutParams windowParam,minParam;
    private ViewGroup winView,contentView,titleBar;
    private VecView icon;
    private myTextView title;
    private touchPoint touchPoint=touchPoint.NULL;
    private boolean focusable=false,maxwin=false,minwin=false;
    private VecView buttonFocusWin,buttonMinWin,buttonMaxWin,buttonCloseWin,minButton;
    private float OutLineWidth;
    private int width=-2,height=-2;
    private WindowInterface inter;
    private Window scaleWindow;
    private boolean resize=true;

    public Window(Context ctx,int widt,int heigh)
    {
        this.ctx=ctx;
        width=widt;
        height=heigh;
        window=(WindowManager)ctx.getSystemService(ctx.WINDOW_SERVICE);
        windowParam=new WindowManager.LayoutParams(
            width,height,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.RGBA_8888
        );
        windowParam.gravity=Gravity.LEFT|Gravity.TOP;
        winView=(ViewGroup) LayoutInflater.from(ctx).inflate(R.layout.window_main,null);
        icon=(VecView) winView.findViewById(R.id.mainwindowImageView1);
        title=(myTextView) winView.findViewById(R.id.mainwindowTextView1);
        buttonFocusWin=(VecView) winView.findViewById(R.id.mainwindowButton1);
        buttonMinWin=(VecView) winView.findViewById(R.id.mainwindowButton2);
        buttonMaxWin=(VecView) winView.findViewById(R.id.mainwindowButton3);
        buttonCloseWin=(VecView) winView.findViewById(R.id.mainwindowButton4);
        buttonFocusWin.setOnClickListener(this);
        buttonFocusWin.setOnLongClickListener(this);
        buttonMinWin.setOnClickListener(this);
        buttonMinWin.setOnLongClickListener(this);
        buttonMaxWin.setOnClickListener(this);
        buttonMaxWin.setOnLongClickListener(this);
        buttonCloseWin.setOnClickListener(this);
        buttonCloseWin.setOnLongClickListener(this);
        buttonFocusWin.setOnTouchListener(null);
        buttonMinWin.setOnTouchListener(null);
        buttonMaxWin.setOnTouchListener(null);
        buttonCloseWin.setOnTouchListener(null);
        minButton=(VecView) winView.getChildAt(0);
        minButton.setOnClickListener(this);
        minButton.setOnTouchListener(this);
        titleBar=(ViewGroup) winView.getChildAt(1);
        titleBar.setOnTouchListener(this);
        contentView=(ViewGroup) winView.getChildAt(2);
        winView.setOnTouchListener(this);
        OutLineWidth=((LinearLayout.LayoutParams)contentView.getLayoutParams()).getMarginStart();
        minButton.setImageResource(R.drawable.icon);
        //winView.setBackground(new myRippleDrawable());
    }
    public Window setBar(int focus,int min,int max,int close)
    {
        buttonFocusWin.setVisibility(focus);
        buttonMinWin.setVisibility(min);
        buttonMaxWin.setVisibility(max);
        buttonCloseWin.setVisibility(close);
        return this;
    }
    public Window setWindowInterface(WindowInterface i)
    {
        inter=i;
        return this;
    }
    @Override
    public boolean onLongClick(View p1)
    {
        // TODO: Implement this method
        switch(p1.getId())
        {
            case R.id.mainwindowButton1:
                if(inter!=null)inter.onButtonDown(ButtonCode.FOCUS_LONG);
                break;
            case R.id.mainwindowButton2:
                if(inter!=null)inter.onButtonDown(ButtonCode.MIN_LONG);
                break;
            case R.id.mainwindowButton3:
                if(inter!=null)inter.onButtonDown(ButtonCode.MAX_LONG);
                break;
            case R.id.mainwindowButton4:
                if(inter!=null)inter.onButtonDown(ButtonCode.CLOSE_LONG);
                break;
            case R.id.mainwindowButton5:
                if(inter!=null)inter.onButtonDown(ButtonCode.MIN_LONG);
                break;
        }
        return true;
    }
    public Window setMessage(String msg)
	{
        myTextViewBack mtv=new myTextViewBack(ctx);
        mtv.setText(msg);
        addView(mtv);
        //setSize(util.dip2px(240),util.dip2px(360));
        return this;
    }
    public Window show()
    {
        if(windowParam.type!=WindowManager.LayoutParams.TYPE_PHONE)throw new SecurityException("不能更改悬浮窗种类");
        window.addView(winView,windowParam);
        return this;
    }
    public Window setIcon(Drawable b)
    {
        icon.setImageVec(null);
        icon.setImageDrawable(b);
		minButton.setImageVec(null);
		minButton.setImageDrawable(b);
        return this;
    }
    public Window setIcon(int b)
    {
        icon.setImageVec(null);
        icon.setImageResource(b);
		minButton.setImageVec(null);
        minButton.setImageResource(b);
        return this;
    }
	public Window setIcon(String vecAsset)
	{
		icon.setImageVec(vecAsset);
		minButton.setImageVec(vecAsset);
		return this;
	}
    public Window setFocusable(boolean f)
    {
        windowParam.flags=f?windowParam.FLAG_NOT_TOUCH_MODAL:windowParam.FLAG_NOT_FOCUSABLE;
        buttonFocusWin.setImageVec(f?"focusedwin":"unfocusedwin");
        window.updateViewLayout(winView,windowParam);
        return this;
    }
    public Window toggleFocusable()
    {
        focusable=!focusable;
        setFocusable(focusable);
        return this;
    }
    public Window setPosition(float x,float y)
    {
        windowParam.x=(int) x;
        windowParam.y=(int) y;
        return this;
    }
    public boolean getMin()
    {
        return minwin;
    }
    public boolean getMax()
    {
        return maxwin;
    }
    public boolean getFocus()
    {
        return focusable;
    }
    public WindowManager.LayoutParams getLayoutParams()
    {
        return windowParam;
    }
    public Window setLayoutParams(WindowManager.LayoutParams l)
    {
        window.updateViewLayout(winView,l);
        windowParam=l;
        return this;
    }
    public Window setTitle(String t)
    {
        title.setText(t);
        return this;
    }
    public Window setCanResize(boolean resiz)
    {
        resize=resiz;
        return this;
    }
    public Window setMaxWin(boolean b)
    {
        windowParam.width=b?-1:width;
        windowParam.height=b?-1:height;
        buttonMaxWin.setImageVec(b?"restorewin":"maxwin");
        window.updateViewLayout(winView,windowParam);
        return this;
    }
    public Window toggleMaxWin()
    {
        maxwin=!maxwin;
        setMaxWin(maxwin);
        return this;
    }
    public ViewGroup getContentView()
    {
        return contentView;
    }
    public ViewGroup getMainView()
    {
        return winView;
    }
    public Window setMinWin(boolean b)
    {
        if(b)
        {
            winView.removeView(contentView);
            titleBar.setVisibility(8);
            minButton.setVisibility(0);
            winView.setBackground(null);
            windowParam.width=-2;
            windowParam.height=-2;
            setFocusable(false);
            winView.setBackground(null);
        }
        else
        {
            winView.addView(contentView);
            titleBar.setVisibility(0);
            minButton.setVisibility(8);
            setMaxWin(maxwin);
            setFocusable(focusable);
            winView.setBackgroundColor(uidata.UI_COLOR_MAIN);
        }
        update();
        return this;
    }
    public Window addView(View view)
    {
        contentView.addView(view);
        return this;
    }
    public Window update()
    {
        window.updateViewLayout(winView,windowParam);
        return this;
    }
    public Window toggleMinWin()
    {
        minwin=!minwin;
        setMinWin(minwin);
        return this;
    }
    public Window dismiss()
    {
        window.removeView(winView);
        return this;
    }
    public Window setSize(int w,int h)
    {
        if(inter!=null)inter.onSizeChanged(w,h,width,height);
        width=w;
        height=h;
        windowParam.width=w;
        windowParam.height=h;
        update();
        return this;
    }
    @Override
    public void onClick(View p1)
    {
        // TODO: Implement this method
        switch(p1.getId())
        {
            case R.id.mainwindowButton1:
                if(inter!=null)inter.onButtonDown(ButtonCode.FOCUS);
                toggleFocusable();
                break;
            case R.id.mainwindowButton2:
                if(inter!=null)inter.onButtonDown(ButtonCode.MIN);
                toggleMinWin();
                break;
            case R.id.mainwindowButton3:
                if(inter!=null)inter.onButtonDown(ButtonCode.MAX);
                toggleMaxWin();
                break;
            case R.id.mainwindowButton4:
                if(inter!=null)inter.onButtonDown(ButtonCode.CLOSE);
                dismiss();
                break;
            case R.id.mainwindowButton5:
                if(inter!=null)inter.onButtonDown(ButtonCode.MIN);
                toggleMinWin();
                break;
        }
    }
    @Override
    public boolean onTouch(View p1, MotionEvent p2)
    {
        // TODO: Implement this method
        //update();/*
        if(maxwin&&!minwin)return true;
        if(p1==minButton||p1==titleBar)return moveableView(p1,p2);
        int x=(int) p2.getX(),y=(int) p2.getY(),rx=(int) p2.getRawX(),ry=(int) p2.getRawY(),w=p1.getWidth(),h=p1.getHeight();
        if(resize)
            switch (p2.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    if(x<OutLineWidth*5)
                    {
                        //左边
                        touchPoint=touchPoint.LEFT;
                        util.toast(ctx,"拖动以改变窗口大小");
                        if(y>h-OutLineWidth*5)
                        {
                            //左下角
                            touchPoint=touchPoint.LEFT_BOTTOM;
                        }
                    }
                    else if(x>w-OutLineWidth*5)
                    {
                        //右边
                        touchPoint=touchPoint.RIGHT;
                        util.toast(ctx,"拖动以改变窗口大小");
                        if(y>h-OutLineWidth*5)
                        {
                            //右下角
                            touchPoint=touchPoint.RIGHT_BOTTOM;
                        }
                    }
                    else if(y>h-OutLineWidth*5)
                    {
                        //下边
                        touchPoint=touchPoint.BOTTOM;
                        util.toast(ctx,"拖动以改变窗口大小");
                    }
                    else touchPoint=touchPoint.NULL;
                    break;
                case MotionEvent.ACTION_MOVE:
                    switch(touchPoint)
                    {
                        case NULL:

                            break;
                        case LEFT:

                            break;
                        case LEFT_BOTTOM:

                            break;
                        case BOTTOM:

                            break;
                        case RIGHT_BOTTOM:

                            break;                       
                        case RIGHT:

                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    switch(touchPoint)
                    {
                        case NULL:

                            break;
                        case LEFT:
                            width=windowParam.x+width-rx;
                            windowParam.x=rx;
                            break;
                        case LEFT_BOTTOM:
                            height=ry-windowParam.y;
                            width=windowParam.x+width-rx;
                            windowParam.x=rx;
                            break;
                        case BOTTOM:
                            height=ry-windowParam.y;
                            break;
                        case RIGHT_BOTTOM:
                            width=rx-windowParam.x;
                            height=ry-windowParam.y;
                            break;
                        case RIGHT:
                            width=rx-windowParam.x;
                            break;
                    }
                    update();
                    setSize(width,height);
                    break;
            }
        return true;
    }
    private int lastX, lastY;
    private int paramX, paramY;
    private int lastX2,lastY2;
    private enum touchPoint
    {
        NULL,LEFT,LEFT_BOTTOM,BOTTOM,RIGHT_BOTTOM,RIGHT;
    };
    public boolean moveableView(View p1,MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
				toNormal();
                paramX = windowParam.x;
                paramY = windowParam.y;
                lastX2=paramX;
                lastY2=paramY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                windowParam.x = paramX + dx;
                windowParam.y = paramY + dy;
				toNormal();
                window.updateViewLayout(winView,windowParam);
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(windowParam.x-lastX2)<10*OutLineWidth&&
                   Math.abs(windowParam.y-lastY2)<10*OutLineWidth)
                {
                    if(p1.getId()==R.id.mainwindowButton5)
                    {
                        if(inter!=null)inter.onButtonDown(ButtonCode.MIN);
                        toggleMinWin();
                    }
                    return false;
                }
                break;
        }
        return true;
    }

	private void toNormal()
	{
		if(windowParam.x<0)windowParam.x=0;
		if(windowParam.y<0)windowParam.y=0;
		if(windowParam.x+winView.getWidth()>util.getScreenWidth())windowParam.x=util.getScreenWidth()-winView.getWidth();
		if(windowParam.y+winView.getHeight()>util.getScreenHeight())windowParam.y=util.getScreenHeight()-winView.getHeight();
		if(inter!=null)inter.onPositionChanged(windowParam.x,windowParam.y);
	}
	public static final class ButtonCode
    {
        public static final int MIN=0,MAX=1,FOCUS=2,CLOSE=3,FOCUS_LONG=4,MIN_LONG=5,MAX_LONG=6,CLOSE_LONG=7;
    }
    public interface WindowInterface
    {
        public abstract void onButtonDown(int code);
        public abstract void onSizeChanged(int w,int h,int oldw,int oldh);
        public abstract void onPositionChanged(int x,int y);
    }
}
