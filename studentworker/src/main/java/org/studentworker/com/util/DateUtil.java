package org.studentworker.com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Long getCurrentTimeStamp(){
		Date d = new Date();
		return d.getTime();
	}
	
	public static Long dateFormat(String dateStr,String parttner){
		SimpleDateFormat sdf = new SimpleDateFormat(parttner);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}

	public static String format(long regist_time, String parttner) {
		Date date = new Date(regist_time);
		SimpleDateFormat sdf = new SimpleDateFormat(parttner);
		String str = sdf.format(date);
		return str;
	}
}
