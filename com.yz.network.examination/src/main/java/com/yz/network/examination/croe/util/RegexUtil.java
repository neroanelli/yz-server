package com.yz.network.examination.croe.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则
 * 
 * @author lx
 * @date 2018年8月31日 下午6:53:36
 */
public class RegexUtil {

	/**
	 * 正则匹配结果
	 *
	 * @param pattern
	 * @return
	 */
	public static String RegexMatch(String pattern, String input) {
		String result = "";
		try {
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(input);
			if (m.find()) {
				result = m.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 正则匹配结果
	 *
	 * @param pattern
	 * @return
	 */
	public static String regexMatch(String regPattern, String input) {
		String result = "";
		try {
			Pattern r = Pattern.compile(regPattern);
			Matcher m = r.matcher(input);
			if (m.find()) {
				result = m.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@FunctionalInterface
	public interface RegCallBack {
		public void execute();
	}
}
