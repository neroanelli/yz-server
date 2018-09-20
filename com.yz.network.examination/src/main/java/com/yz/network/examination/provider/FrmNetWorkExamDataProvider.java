package com.yz.network.examination.provider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.yz.network.examination.constants.NetWorkExamConstants;
import com.yz.network.examination.form.BaseNetWorkExamForm;
import com.yz.redis.RedisService;
import com.yz.report.ReportJdbcDao;
import com.yz.util.ExceptionUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 网报参数提供
 * @author lingdian
 *
 */
@Component(value = "frmNetWorkExamDataProvider")
public class FrmNetWorkExamDataProvider implements NetWorkExamDataProvider<BaseNetWorkExamForm> {

	private static Logger logger = LoggerFactory.getLogger(FrmNetWorkMapper.class);

	@Autowired
	private ReportJdbcDao reportJdbcDao;

	@Override
	public BaseNetWorkExamForm provider(BaseNetWorkExamForm frm) {
		BaseNetWorkExamForm examForm = RedisService.getRedisService().hgetarr(frm.getId(), frm.getCacheKey(),
				frm.getClass());
		//if (true || examForm == null) {
			Map<String, Object> param = Maps.newHashMap();
			param.put("id", frm.getId());
			param.put("frmType", frm.getClass().getSimpleName());
			FrmNetWorkMapper mapper = new FrmNetWorkMapper(frm);
			return this.reportJdbcDao.queryObject(
					getNetWorkExamFrmCode(frm.getFrmCode(), NetWorkExamConstants.FRM_COMMON_REPCODE), param, mapper);
		//}
		//return examForm;
	}

	/**
	 * @desc 获取frm的RepCode
	 * @param frmCode
	 * @param def
	 * @return
	 */
	private String getNetWorkExamFrmCode(String frmCode, String def) {
		if (StringUtil.isBlank(frmCode)) {
			return def;
		}
		return frmCode;
	}

	/**
	 * 
	 * @desc
	 * @author lingdian
	 *
	 */
	private class FrmNetWorkMapper implements RowMapper<BaseNetWorkExamForm> {
		private BaseNetWorkExamForm frm;

		public FrmNetWorkMapper(BaseNetWorkExamForm frm) {
			try {
				this.frm = frm.getClass().newInstance();
				this.frm.setId(frm.getId());
			} catch (Exception e) {
				logger.error("FrmNetWorkMapper.error:{}", ExceptionUtil.getStackTrace(e));
			}
		}

		@Override
		public BaseNetWorkExamForm mapRow(ResultSet rs, int rowNum) throws SQLException {
			while (rs.next()) {
				frm.addParam(rs.getString("attrName"), rs.getString("attrVal"));
			}
			return frm;
		}
	}
}
