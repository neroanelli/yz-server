package com.yz.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.model.BdStudentBaseInfo;
import com.yz.model.StudentHistory;
import com.yz.model.student.BdLearnInfo;
import com.yz.util.MethodUtil;
import com.yz.util.StringUtil;

public class StudentDataCheckUtil {

	private static final Map<Class<?>, String[]> CHECK_MAP = new HashMap<Class<?>, String[]>();

	static {
		CHECK_MAP.put(StudentHistory.class,
				new String[] {"edcsType","unvsName","graduateTime","profession","diploma","isOpenUnvs","studyType","materialType","materialCode"});
		CHECK_MAP.put(BdStudentBaseInfo.class,
				new String[] {"stdName","idType","idCard","sex","birthday","mobile","nation","politicalStatus","rprType","rprProvinceCode","rprCityCode","rprDistrictCode",
						"nowProvinceCode","nowCityCode","nowDistrictCode","address","jobType","jobStatus","maritalStatus","rprAddressCode"});
		CHECK_MAP.put(BdLearnInfo.class,
				new String[] {"stdType","unvsId","pfsnId","taId","scholarship","feeId","offerId","grade","stdStage" });
	}
	private static final Logger log = LoggerFactory.getLogger(StudentDataCheckUtil.class);

	/**
	 * 检测学员资料是否完善
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isComplete(Object o, String recruitType) {
		Class<?> clz = o.getClass();
		String[] fieldArray = CHECK_MAP.get(clz);
		try {
			for (String field : fieldArray) {
				Method getter = MethodUtil.getterMethod(clz, field);
				Object result = getter.invoke(o, null);
				String resultStr = result==null ? "": result.toString();
				if("BdStudentBaseInfo".equals(o.getClass().getSimpleName())){
					if("1".equals(recruitType)){
						if ("".equals(resultStr) && "jobStatus,maritalStatus,rprProvinceCode,rprCityCode,rprDistrictCode".indexOf(field)<0) return false;
					}else{
						if (("".equals(resultStr) || "00".equals(resultStr)) && "jobType,rprAddressCode".indexOf(field)<0) return false;
					}
				}else if("StudentHistory".equals(o.getClass().getSimpleName())){
					if("1".equals(recruitType)){
						if ("".equals(resultStr) && "isOpenUnvs,studyType,materialType,materialCode".indexOf(field)<0) return false;
					}else{
						Object edcsTypeObj = MethodUtil.getterMethod(clz, "edcsType").invoke(o,null);
						String edcsType = edcsTypeObj==null ? "": edcsTypeObj.toString();
						if(!"".equals(edcsType) && "679".indexOf(edcsType)>=0){
							if ("".equals(resultStr)) return false;
						}else{
							if ("".equals(resultStr) && "profession,diploma,studyType,materialType,materialCode".indexOf(field)==-1) return false;
						}
					}
				}else{
					if ("".equals(resultStr)) return false;
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			log.error("---------------------- 资料完善检测失败", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 检测考试资料是否已完善
	 * @param history
	 * @return
	 */
	public static boolean isTestCompleted(StudentHistory history) {
		String diploma = history.getDiploma();
		String edcsType = history.getEdcsType();
		String graduateTime = history.getGraduateTime();
		String profession = history.getProfession();
		String unvsName = history.getUnvsName();
		if (StringUtil.hasValue(diploma) && StringUtil.hasValue(edcsType) && StringUtil.hasValue(graduateTime)
				&& StringUtil.hasValue(profession) && StringUtil.hasValue(unvsName)) {
			return true;
		}

		return false;
	}
}
