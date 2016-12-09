/*
+---------------------------------------------------------------+
| 名称: sqlitehelper/SysFuncManager.java                              
| 版权：烟台一天科技有限公司                                                             
| 日期：2014-10-11                                           
| 描述: 获取系统时间          
+---------------------------------------------------------------+
 */
package com.kfc.productcar.utils;

import java.util.Calendar;
import java.util.Random;

public class SysFuncManager {

	/**
	 *获取随机ID
	 **/
	public static String getRandomId()
	{
		String strId= String.valueOf(System.currentTimeMillis());
		Random ran =new Random(System.currentTimeMillis());
		strId = strId+ran.nextInt(10000)+1;  
		return strId;
	}
	/**
	 *获取系统时间
	 **/
	public static String SysTime()
	{
		String time="0000-00-00 00:00:00";
		Calendar c= Calendar.getInstance();
		  
		time=c.get(Calendar.YEAR)+"-"+//得到年
		formatTime(c.get(Calendar.MONTH)+1)+"-"+//月
		formatTime(c.get(Calendar.DAY_OF_MONTH))+" "+//日
		formatTime(c.get(Calendar.HOUR_OF_DAY))+":"+//时
		formatTime(c.get(Calendar.MINUTE))+":"+//分
		formatTime(c.get(Calendar.SECOND));//秒
		
		return time;
	}
	/**
	 *获取系统时间(yyyy-MM-dd：年月日|hh:mm:ss：时分秒|yyyy：年|MM：月|dd：日|hh：时|mm：分钟|ss：秒)
	 **/
	public static String SysTime(String timeType)
	{
		String time="0000-00-00 00:00:00";
		Calendar c= Calendar.getInstance();
		  
		if(timeType.equals("yyyy-MM-dd"))
		{
			time=c.get(Calendar.YEAR)+"-"+//年
			formatTime(c.get(Calendar.MONTH)+1)+"-"+//月
			formatTime(c.get(Calendar.DAY_OF_MONTH));//日
		}
		if(timeType.equals("hh:mm:ss"))
		{
			time=formatTime(c.get(Calendar.HOUR_OF_DAY))+":"+//时
			formatTime(c.get(Calendar.MINUTE))+":"+//分
			formatTime(c.get(Calendar.SECOND));//秒
		}
		if(timeType.equals("yyyy"))
		{
			time=c.get(Calendar.YEAR)+"";//年
		}
		if(timeType.equals("MM"))
		{
			time=formatTime(c.get(Calendar.MONTH)+1);//月
		}
		if(timeType.equals("dd"))
		{
			time=formatTime(c.get(Calendar.DAY_OF_MONTH));//日
		}
		if(timeType.equals("hh"))
		{
			time=formatTime(c.get(Calendar.HOUR_OF_DAY));//时
		}
		if(timeType.equals("mm"))
		{
			time=formatTime(c.get(Calendar.MINUTE));//分
		}
		if(timeType.equals("ss"))
		{
			time=formatTime(c.get(Calendar.SECOND));//秒
		}	
		return time;
	}
	private static String formatTime(int t)
	{
		return t>=10? ""+t:"0"+t;//三元运算符 t>10时取 ""+t
	}
}
