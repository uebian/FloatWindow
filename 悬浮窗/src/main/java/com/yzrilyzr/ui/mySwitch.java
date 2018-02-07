package com.yzrilyzr.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;
import com.yzrilyzr.floatingwindow.R;
import com.yzrilyzr.myclass.util;

public class mySwitch extends Switch
{
    /*private Context ctx;
    private float drawx=0;
    private String method;
    private boolean isChecked=false,isOn=false,last=false;
    private OnCheckedChangeListener occl=null;
*/
    private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    public mySwitch(Context c,AttributeSet a)
    {
        super(c,a);
        //if(a!=null)method=a.getAttributeValue(null,"onClick");
       // ctx=c;
    }
    public mySwitch(Context c)
    {
		this(c,null);
	}
  /*  public void setChecked(boolean c)
    {
        isChecked=c;invalidate();
    }
    public boolean getChecked()
    {
        return isChecked;
    }
    public void setListener(OnCheckedChangeListener o)
    {
        occl=o;
    }
    public interface OnCheckedChangeListener
    {
        public abstract void onCheckedChange(mySwitch mcb,boolean state);
    }*/
    @Override
    protected void onDraw(Canvas canvas)
    {
      	float hei=getHeight(),wid=getWidth();
		float r=hei/2f,r2=hei/6f,r3=hei/3f;
		float drawx=r+(wid-2f*r)*(isChecked()?1:0);
		paint.setColor(0xffeeeeee);
		canvas.drawRoundRect(new RectF(r,r3,wid-r,hei-r3),r2,r2,paint);
		paint.setColor(uidata.UI_COLOR_MAIN);
		canvas.drawRoundRect(new RectF(r,r3,drawx,hei-r3),r2,r2,paint);
		paint.setColor(0xffffffff);
		//if(ondown)canvas.drawCircle(drawx,hei/2,r,paint);
		canvas.drawCircle(drawx,hei/2,r3,paint);
//        float radius=h/3;
//        if(drawx<radius)drawx=radius;
//        if(drawx>w-radius)drawx=w-radius;
//        paint.setColor(0xffeeeeee);
//        paint.setShader(new LinearGradient(0,radius,0,h-radius,new int[]{0xffffffff,uidata.UI_COLOR_MAINHL/*,uidata.UI_COLOR_MAIN*/},null,Shader.TileMode.CLAMP));
//        canvas.drawRoundRect(new RectF(radius/2,radius,w-radius/2,radius*2),radius/2,radius/2,paint);
//        paint.setColor(uidata.UI_COLOR_MAIN);
//        paint.setShader(new LinearGradient(0,radius,0,h-radius,new int[]{uidata.UI_COLOR_MAIN,uidata.UI_COLOR_BACK,uidata.UI_COLOR_MAIN},null,Shader.TileMode.CLAMP));
//        canvas.drawRoundRect(new RectF(radius/2,radius,drawx,radius*2),radius/2,radius/2,paint);
//        paint.setColor(0xffffffff);
//        paint.setShader(new RadialGradient(drawx,h/2,radius,uidata.UI_COLOR_MAINHL,uidata.UI_COLOR_MAIN,Shader.TileMode.CLAMP));
//        canvas.drawCircle(drawx,h/2,radius,paint);
//        if(!isOn&&!isChecked&&drawx>radius)
//        {
//            drawx-=w/20;
//            invalidate();
//        }
//        if(!isOn&&isChecked&&drawx<w-radius)
//        {
//            drawx+=w/20;
//            invalidate();
//        }
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        // TODO: Implement this method
//        getParent().requestDisallowInterceptTouchEvent(true);
//        switch(event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                isOn=true;last=false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                isOn=true;
//                drawx=event.getX();
//                boolean l=drawx>getWidth()/2;
//                last=l==isChecked;
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                isChecked=drawx>getWidth()/2;
//                if(last)isChecked=!isChecked;
//                isOn=false;
//                invalidate();
//                if(occl!=null)occl.onCheckedChange(this,isChecked);
//                if(method!=null)util.call(getContext().getClass(),method,new Class[]{View.class,boolean.class},getContext(),new Object[]{this,isChecked});
//                break;
//        }
//        return true;
//    }
//	@Override
 	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(WidgetUtils.measure(widthMeasureSpec,util.px(50)),WidgetUtils.measure(heightMeasureSpec,util.px(30)));
    }

}
