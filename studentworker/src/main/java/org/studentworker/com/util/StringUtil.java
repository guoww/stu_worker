package org.studentworker.com.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import org.apache.commons.codec.binary.Base64;

public class StringUtil extends org.apache.commons.lang.StringUtils {
	public static char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static SecureRandom sr = new SecureRandom();
	private static String RANDOM_STRING = "abcdefjhijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	public static final Charset UTF8 = Charset.forName("UTF-8");

	public static String newUUID() {
		return UUID.randomUUID().toString();
	}

	public static String urlEncode(String value) {
		return urlEncode(value, "UTF-8");
	}

	public static String urlEncode(String value, String enc) {
		try {
			return URLEncoder.encode(value, enc);
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	public static boolean isNotEmptyORNull(String str) {
		if (null != str && !"".equals(str) && !"null".equals(str)) {
			return true;
		}
		return false;
	}

	public static String urlDecode(String value) {
		return urlDecode(value, "UTF-8");
	}

	public static String urlDecode(String value, String enc) {
		try {
			return URLDecoder.decode(value, enc);
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	public static int rndInt(int min, int max) {
		return (int) (sr.nextDouble() * (max + 1 - min) + min);
	}
	public static boolean isSameDate(Date date1, Date date2) {
	       Calendar cal1 = Calendar.getInstance();
	       cal1.setTime(date1);

	       Calendar cal2 = Calendar.getInstance();
	       cal2.setTime(date2);

	       boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
	               .get(Calendar.YEAR);
	       boolean isSameMonth = isSameYear
	               && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
	       boolean isSameDate = isSameMonth
	               && cal1.get(Calendar.DAY_OF_MONTH) == cal2
	                       .get(Calendar.DAY_OF_MONTH);

	       return isSameDate;
	   }

	/**
	 * 产生随机字符串
	 * 
	 * @param len
	 *            长度
	 * @param digit
	 *            是否包含数字
	 * @param lower
	 *            是否包含小写字母
	 * @param upper
	 *            是否包含大写字母
	 * @return
	 */
	public static String randomString(int len, boolean digit, boolean lower, boolean upper) {
		StringBuilder source = new StringBuilder();
		if (digit) {
			source.append(Arrays.copyOfRange(HEX_DIGITS, 0, 10));
		}
		if (lower) {
			source.append(RANDOM_STRING.substring(0, 26));
		}
		if (upper) {
			source.append(RANDOM_STRING.substring(26));
		}
		return randomString(source.toString(), len);
	}

	public static String randomString(String source, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int r = rndInt(0, source.length() - 1);
			sb.append(source.substring(r, r + 1));
		}
		return sb.toString();
	}

	/**
	 * 产生一个包含大小写字母的随机字符串
	 * 
	 * @param len
	 *            长度
	 * @return
	 */
	public static String randomString(int len) {
		return randomString(RANDOM_STRING, len);
	}

	public static String clearHTML(String text) {
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(text);
		text = m_html.replaceAll(""); // 过滤html标签

		return text.trim(); // 返回文本字符串
	}

	public static String toHexString(byte[] bytes) {
		int j = bytes.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = bytes[i];
			str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
			str[k++] = HEX_DIGITS[byte0 & 0xf];
		}
		return new String(str);
	}

	/**
	 * 首字母变小写
	 */
	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}

	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}

	public static String base64EncodeString(String str, String charsetName) {
		Charset charset = Charset.forName(charsetName);
		return new String(Base64.encodeBase64(str.getBytes(charset)), charset);
	}

	public static String base64EncodeString(String str) {
		return base64EncodeString(str, "utf-8");
	}

	public static String base64DecodeString(String str) {
		return base64DecodeString(str, "utf-8");
	}

	public static String base64DecodeString(String str, String charsetName) {
		Charset charset = Charset.forName(charsetName);
		return new String(Base64.decodeBase64(str.getBytes(charset)), charset);
	}

	public static byte[] base64Encode(String str, String charsetName) {
		Charset charset = Charset.forName(charsetName);
		return Base64.encodeBase64(str.getBytes(charset));
	}

	public static byte[] base64Encode(String str) {
		return base64Encode(str, "utf-8");
	}

	public static byte[] base64Decode(String str) {
		return base64Decode(str, "utf-8");
	}

	public static byte[] base64Decode(String str, String charsetName) {
		Charset charset = Charset.forName(charsetName);
		return Base64.decodeBase64(str.getBytes(charset));
	}

	/**
	 * @Title: ToSBC
	 * @Description: (半角转全角)
	 * @param input
	 * @return
	 * @throws
	 * @author xiaojiang0229 2015年6月4日 下午5:54:05
	 * @version V1.0
	 */
	public static String toSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * @Title: ToDBC
	 * @Description: (全角转半角)
	 * @param input
	 * @return
	 * @throws
	 * @author xiaojiang0229 2015年6月4日 下午5:54:14
	 * @version V1.0
	 */
	public static String toDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}
	
	/**
	 * 
	 * 
	 * (base64解码）
	 *
	 * @param encdata
	 * @return
	 * @author 钟臣云 新增日期：2014-8-13下午08:58:11
	 * @version 1.0
	 */
	public static String decompressData(String encdata) {  
        try {  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            InflaterOutputStream zos = new InflaterOutputStream(bos);  
            zos.write(getdeBASE64inCodec(encdata));   
            zos.close();  
            String nickName = new String(bos.toByteArray());
            if (isBlank(nickName)) {
				return encdata;
			}
            return nickName;
        } catch (Exception ex) {  
            return encdata;  
        }
    }  
	
	public static byte[] getdeBASE64inCodec(String s) {  
        if (s == null)  
            return null;  
        return new Base64().decode(s.getBytes());  
    } 
	
	public static String compressData(String data) { 
		if (Base64.isBase64(data)) {
			return data;
		}
        try {  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            DeflaterOutputStream zos = new DeflaterOutputStream(bos);  
            zos.write(data.getBytes());  
            zos.close();  
            return new String(getenBASE64inCodec(bos.toByteArray()));  
        } catch (Exception ex) {  
            return data;  
        }  
    }  
	
	 public static String getenBASE64inCodec(byte [] b) {  
	        if (b == null)  
	            return null;  
	        return new String((new Base64()).encode(b));  
	    }  
}
