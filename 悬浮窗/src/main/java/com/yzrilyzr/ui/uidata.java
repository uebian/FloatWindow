package com.yzrilyzr.ui;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.yzrilyzr.myclass.util;
import com.yzrilyzr.ui.myIconDrawer.DrawType;
public class uidata
{
	public static float UI_TEXTSIZE_DEFAULT=5f;
	public static float UI_TEXTSIZE_TITLE=7f;
	public static int UI_PADDING_DEFAULT=5;
	public static boolean UI_USETYPEFACE=true;
	public static boolean UI_USESHADOW=true;
	public static Typeface UI_TYPEFACE=null;
	public static int UI_TEXTCOLOR_MAIN=0xffffffff;
	public static int UI_TEXTCOLOR_BACK=0xff000000;
	public static int UI_TEXTCOLOR_HL=0xffaaaaaa;
	public static int UI_COLOR_MAIN=0xff03a9f4;
	public static int UI_COLOR_BACK=0xffb3e5fc;
	public static int UI_COLOR_MAINHL=0xa0e1f5fe;
	public static float UI_DENSITY=1.0F;
	public static float UI_RADIUS=3.0f;
	public static boolean isInit=false;
	public static boolean mod=false;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
	public static final void saveData(Context ctx)
	{
		UI_DENSITY=ctx.getResources().getDisplayMetrics().density;
		util.getSPWrite(ctx)
			.putBoolean("typeface",UI_USETYPEFACE)
			.putBoolean("mod",mod)
			.putBoolean("shadow",UI_USESHADOW)
			.putInt("maincolor",UI_COLOR_MAIN)
			.putInt("backcolor",UI_COLOR_BACK)
			.putInt("highcolor",UI_COLOR_MAINHL)
			.putInt("maintext",UI_TEXTCOLOR_MAIN)
			.putInt("backtext",UI_TEXTCOLOR_BACK)
			.putInt("hightext",UI_TEXTCOLOR_HL)
			.putInt("padding",(int)(UI_PADDING_DEFAULT/UI_DENSITY))
			.putFloat("sizetext",UI_TEXTSIZE_DEFAULT/UI_DENSITY)
			.putFloat("titletext",UI_TEXTSIZE_TITLE/UI_DENSITY)
			.putFloat("radius",UI_RADIUS/UI_DENSITY)
			.putBoolean("init",true)
			.commit();
	}

	public static final void readData(Context ctx)
	{
		SharedPreferences sp=util.getSPRead(ctx);
		isInit=sp.getBoolean("init",false);
		UI_TYPEFACE=Typeface.MONOSPACE;//.createFromAsset(ctx.getAssets(),"font.ttf");
		UI_DENSITY=ctx.getResources().getDisplayMetrics().density;
		UI_USETYPEFACE=sp.getBoolean("typeface",UI_USETYPEFACE);
		mod=sp.getBoolean("mod",mod);
		UI_USESHADOW=sp.getBoolean("typeface",UI_USESHADOW);
		UI_COLOR_MAIN=sp.getInt("maincolor",UI_COLOR_MAIN);
		UI_COLOR_BACK=sp.getInt("backcolor",UI_COLOR_BACK);
		UI_COLOR_MAINHL=sp.getInt("highcolor",UI_COLOR_MAINHL);
		UI_TEXTCOLOR_MAIN=sp.getInt("maintext",UI_TEXTCOLOR_MAIN);
		UI_TEXTCOLOR_BACK=sp.getInt("backtext",UI_TEXTCOLOR_BACK);
		UI_TEXTCOLOR_HL=sp.getInt("hightext",UI_TEXTCOLOR_HL);
		UI_PADDING_DEFAULT=(int)(sp.getInt("padding",UI_PADDING_DEFAULT)*UI_DENSITY);
		UI_TEXTSIZE_DEFAULT=sp.getFloat("sizetext",UI_TEXTSIZE_DEFAULT)*UI_DENSITY;
		UI_TEXTSIZE_TITLE=sp.getFloat("titletext",UI_TEXTSIZE_TITLE)*UI_DENSITY;
		UI_RADIUS=sp.getFloat("radius",UI_RADIUS)*UI_DENSITY;
		if(!isInit)saveData(ctx);
	}
}
