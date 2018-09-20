package com.yz.network.examination.format;

import java.text.DecimalFormat;
import java.util.Date; 

import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

/**
 * 
 * 
 * @author Administrator
 *
 */
public interface YzNetWorkFormater   {
	 

	/**
	 * @desc 格式化参数  将值转化为String
	 * @param src 原始值
	 * @param def 默认值
	 * @return
	 */
	public String formater(Object src, Object def); 
	
	public DecimalFormat num_format = new DecimalFormat("##,###.##");
	
    public static final YzNetWorkFormater def = (src,def)-> {if(StringUtil.isNotBlank(src)) {return String.valueOf(src);} return String.valueOf(def);}; // 默认格式化

    public static final YzNetWorkFormater num = (src,def)-> {if(StringUtil.isNotBlank(src)) {return num_format.format(src);} return String.valueOf(def);};  // 数字格式化
    
    public static final YzNetWorkFormater date = (src,def)-> {if(StringUtil.isNotBlank(src)) {return DateUtil.formatDate((Date)src, DateUtil.YYYYMMDDHHMMSS_SPLIT);} return String.valueOf(def);};  // 日期格式化
    
    public static final YzNetWorkFormater pwd = (src,def)-> {return StringUtil.toCRC32(src,6);}; // 传入身份证 
    
}
