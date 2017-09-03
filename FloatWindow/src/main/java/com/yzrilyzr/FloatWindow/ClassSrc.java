package com.yzrilyzr.FloatWindow;

import java.lang.reflect.*;

import java.lang.annotation.Annotation;

public class ClassSrc
{
    private StringBuffer a;
    private Class<?> clazz;
    public ClassSrc(Class<?> clazz){
        this.clazz=clazz;
        a=new StringBuffer();
        getClassSrc(clazz,0);
    }
    public String get(){
        return a.toString();
    }
    public void getClassSrc(Class<?> c,int inter)
    {
        //head
        Package pkg=c.getPackage();
        if(pkg!=null&&inter==0)a.append("package "+c.getPackage().getName()+";");
        appendN(a,0);
        //annotation
        appendAnnotation(c.getDeclaredAnnotations(),a,inter+1);
        appendN(a,inter);
        //class head
        appendModifier(a,c);
        a.append(splitClass(c).replace(pkg==null?"null":pkg.getName()+".",""));
        //extends
        Type su=c.getGenericSuperclass();
        if(su!=null)a.append(" extends "+splitType(su));
        //implements
        appendClasses(c.getGenericInterfaces(),a,"implements");
        //start
        appendN(a,inter);
        a.append("{");
        //field
        Field[] fs=c.getDeclaredFields();
        for(Field m:fs)
        {
            appendAnnotation(m.getDeclaredAnnotations(),a,inter+1);
            appendN(a,inter+1);
            appendModifier(a,m);
            appendType(m.getGenericType(),a);
            a.append(m.getName());
            try
            {
                m.setAccessible(true);
                Object o=m.get(c);
                a.append(" = "+o);
            }
            catch(Throwable e)
            {}
            a.append(";");
            appendN(a,inter+1);
        }
        //method
        Method[] ms=c.getDeclaredMethods();
        for(Method m:ms)
        {
            appendAnnotation(m.getDeclaredAnnotations(),a,inter+1);
            appendN(a,inter+1);
            appendModifier(a,m);
            appendType(m.getGenericReturnType(),a);
            a.append(m.getName()+"(");
            appendParam(m.getGenericParameterTypes(),a);
            appendClasses(m.getGenericExceptionTypes(), a,"throws");
            a.append(" {}");
            appendN(a,inter+1);
        }
        //constructor
        Constructor<?>[] cs=c.getDeclaredConstructors();
        for(Constructor<?> m:cs)
        {
            appendAnnotation(m.getDeclaredAnnotations(),a,inter+1);
            appendN(a,inter+1);
            appendModifier(a,m);
            a.append(c.getSimpleName()+"(");
            appendParam(m.getGenericParameterTypes(),a);
            appendClasses(m.getGenericExceptionTypes(), a,"throws");
            a.append(" {}");
            appendN(a,inter+1);
        }
        //class
        Class<?>[] cls=c.getDeclaredClasses();
        for(Class<?> cc:cls)
        {
            if(splitType(cc.getGenericSuperclass()).indexOf(">")!=-1)
            {
                Object[] enu=cc.getEnumConstants();
                appendN(a,inter+1);
                appendModifier(a,cc);
                a.append("enum ");
                a.append(cc.getSimpleName());
                appendN(a,inter+1);
                a.append("{");
                for(Object o:enu)
                {
                    appendN(a,inter+2);
                    a.append(o+(o==enu[enu.length-1]?";":","));
                }
                appendN(a,inter+1);
                a.append("}");
            }
            else getClassSrc(cc,inter+1);
        }
        //end
        appendN(a,inter);
        a.append("}");
    }
    public void appendType(Type t,StringBuffer b)
    {
        b.append(splitType(t)+" ");
    }
    public void appendParam(Type[] ts, StringBuffer a)
    {
        int i=1;
        for(Type t:ts)
        {
            a.append(splitType(t)+(i==ts.length?" p"+i:" p"+i+","));
            i++;
        }
        a.append(")");
    }
    public void appendClasses(Type[] ts, StringBuffer a,String key)
    {
        if(ts.length!=0)a.append(" "+key+" ");
        int i=1;
        for(Type t:ts)
        {
            a.append(splitType(t)+(i==ts.length?"":" ,"));
            i++;
        }

    }
    public void appendAnnotation(Annotation[] as, StringBuffer a, int inter)
    {
        for(Annotation an:as)
        {
            appendN(a,inter);
            a.append(an);
        }
    }
    public String splitClass(Class<?> c)
    {
        if(c==null)return "";
        String[] s=c.toString().split(" ");
        String t=s[s.length-1];
        int size=0;
        char ch=0;
        while((ch=t.charAt(0))=='[')
        {
            t=t.substring(1);
            size++;
        }
        switch(ch)
        {
            case 'L':t=t.substring(1,t.length()-1);
                break;
            case 'B':t="byte";
                break;
            case 'C':t="char";
                break;
            case 'D':t="double";
                break;
            case 'F':t="float";
                break;
            case 'I':t="int";
                break;
            case 'S':t="short";
                break;
            case 'Z':t="boolean";
                break;
            case 'J':t="long";
                break;
        }
        for(int i=0;i<size;i++)t+="[]";
        int $=t.indexOf("$");
        while($!=-1)
        {
            t=t.substring($+1,t.length());
            $=t.indexOf("$");
        }
        return t;
    }
    public String splitType(Type c)
    {
        if(c==null)return "";
        String[] s=c.toString().split(" ");
        String t=s[s.length-1];
        int size=0;
        char ch=0;
        while((ch=t.charAt(0))=='[')
        {
            t=t.substring(1);
            size++;
        }
        switch(ch)
        {
            case 'L':t=t.substring(1,t.length()-1);
                break;
            case 'B':t="byte";
                break;
            case 'C':t="char";
                break;
            case 'D':t="double";
                break;
            case 'F':t="float";
                break;
            case 'I':t="int";
                break;
            case 'S':t="short";
                break;
            case 'Z':t="boolean";
                break;
            case 'J':t="long";
                break;
        }
        for(int i=0;i<size;i++)t+="[]";
        int $=t.indexOf("$");
        while($!=-1)
        {
            t=t.substring($+1,t.length());
            $=t.indexOf("$");
        }
        return t;
    }
    public void appendModifier(StringBuffer b,Member a)
    {
        String s=Modifier.toString(a.getModifiers());
        if(!s.equals(""))b.append(s+" ");
    }
    public void appendModifier(StringBuffer b,Class a)
    {
        String s=Modifier.toString(a.getModifiers());
        if(!s.equals(""))b.append(s+" ");
    }
    public void appendN(StringBuffer b,int r)
    {
        b.append("\n");
        for(int i=0;i<r;i++)b.append("    ");
    }

}
