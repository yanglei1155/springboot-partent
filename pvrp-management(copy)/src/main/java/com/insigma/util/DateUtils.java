package com.insigma.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class DateUtils {
	/**yyyy-MM-dd*/
	public static String date = "yyyy-MM-dd";
	/**yyyy/MM/dd*/
	public static String date2 = "yyyy/MM/dd";
	
	/**yyyyMMdd**/
	public static String date3 = "yyyyMMdd";
	
	/**yyyy**/
	public static String date4 = "yyyy";
	
	/**yyyy年MM月dd日**/
	public static String date5 = "yyyy年M月d日";

	/**yyyy,MM,dd**/
	public static String date6 = "yyyy,MM,dd";
	/**HH:mm:ss*/
	public static String time = "HH:mm:ss";
	/**yyyy-MM-dd HH:mm:ss*/
	public static String dateTime = "yyyy-MM-dd HH:mm:ss";

	/**
	 * date转string
	 * @param d
	 * @param formatStr
	 * @return
	 */
	public static String parseDate(Date d,String formatStr){
		return new SimpleDateFormat(formatStr).format(d);
	}

	/**
	 * string转date
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static Date paserStringToDate(String date,String formatStr) throws Exception{
		return new SimpleDateFormat(formatStr).parse(date);
	}
	/**
	 * long转换date
	 * @param d
	 * @param formatStr
	 * @return
	 */
	public static String parseLongtoDate(long d,String formatStr){
		return new SimpleDateFormat(formatStr).format(d);
	}

	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date getCurrentDateDetail(){
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 获取当前时间 yyyy-MM-dd
	 * @return
	 */
	public static Date getCurrentDate() throws Exception{
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(date);
		String dataFormat = sdf.format(currentTime);
		return  sdf.parse(dataFormat);
	}
	
	/**
	 * 获取当前时间 yyyyMMdd
	 * @return
	 */
	public static String getStringCurrentDate(){
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(date3);
		String dataFormat = sdf.format(currentTime);
		return  dataFormat;
	}

	/**
	 * 根据时间格式获取当前时间字符串
	 * @param format
	 * @return
	 */
	public static String getStringCurrentDate(String format){
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(date3);
		String dataFormat = sdf.format(currentTime);
		return  dataFormat;
	}

	/**
	 * 获取当前时间 yyyy年MM月dd
	 * @return
	 */
	public static String getCnCurrentDate(){
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(date5);
		String dataFormat = sdf.format(currentTime);
		return  dataFormat;
	}
	
	/**
	 * 日期加减
	 * @param dt
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date calculateDate(Date dt,int year,int month,int day){
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.YEAR,year);//日期减1年
		rightNow.add(Calendar.MONTH,month);//日期加3个月
		rightNow.add(Calendar.DAY_OF_YEAR,day);//日期加10天
		return rightNow.getTime();
	}

	/**
	 * string转换为时间戳
	 * @param date
	 * @return
	 */
	public static Long strToLong(String date) throws Exception{
		return paserStringToDate(date,dateTime).getTime();
	}
	
	
	/**
	 * 获取当前年 yyyy
	 * @return
	 */
	public static String getStringYearDate(){
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(date4);
		String dataFormat = sdf.format(currentTime);
		return  dataFormat;
	}
	
	/**
	* 获取两个日期相差的月数
	* @param d1  较大的日期
	* @param d2  较小的日期
	* @return 如果d1>d2返回 月数差 否则返回0
	 * @throws ParseException 
	*/
	public static int getMonthDiff(String d1, String d2){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(sdf.parse(d1));
			c2.setTime(sdf.parse(d2));
			if(c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;
			int year1 = c1.get(Calendar.YEAR);
			int year2 = c2.get(Calendar.YEAR);
			int month1 = c1.get(Calendar.MONTH);
			int month2 = c2.get(Calendar.MONTH);
			int day1 = c1.get(Calendar.DAY_OF_MONTH);
			int day2 = c2.get(Calendar.DAY_OF_MONTH);
			// 获取年的差值 假设 d1 = 2015-8-16 d2 = 2011-9-30
			int yearInterval = year1 - year2;
			// 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
			if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;
			// 获取月数差值
			int monthInterval = (month1 + 12) - month2 ;
			if(day1 < day2) monthInterval --;
			monthInterval %= 12;
			return yearInterval * 12 + monthInterval;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	 /**
     * 计算时间
     * @param startTime ： 开始时间
     * @param endTime  ： 结束时间
     * @return   
     */
    public static int caculateTotalTime(String startTime,String endTime) {
        SimpleDateFormat formatter =   new SimpleDateFormat( "yyyy-MM-dd");
        Date date1=null;
        Date date = null;
        Long l = 0L;
        try {
            date = formatter.parse(startTime);
            long ts = date.getTime();
            date1 =  formatter.parse(endTime);
            long ts1 = date1.getTime();

            l = (ts - ts1) / (1000 * 60 * 60 * 24);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l.intValue();
    }

	/**
	 * 获取当前年龄生日
	 * @param  age  ： 年龄
	 */
	public static String getBirthday(int age){
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy.MM.dd");
		Date date=new Date();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR,-age);
		Date time = calendar.getTime();
		String birthday = simpleDateFormat.format(time);
		return  birthday;

	}

	/**
	 * 根据字符串获取年月日
	 * @param date
	 * @return
	 */
	public static Map<String,String>getTime(String date,String format){
		Map<String,String>map=new HashMap<>();
		try {
			Calendar calendar=Calendar.getInstance();
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
			Date dateTime = simpleDateFormat.parse(date);
			calendar.setTime(dateTime);
			map.put("year",String.valueOf(calendar.get(Calendar.YEAR)));
			map.put("month",String.valueOf(calendar.get(Calendar.MONTH)+1));
			map.put("day",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
		}catch (Exception e){
			e.printStackTrace();
		}
		return  map;
	}

	public static void main(String[] args) {

	}
}
