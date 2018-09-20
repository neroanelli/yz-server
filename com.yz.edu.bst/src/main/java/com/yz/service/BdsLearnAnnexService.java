package com.yz.service;

import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileUploadUtil;
import com.yz.core.util.SessionUtil;
import com.yz.dao.BdsLearnAnnexMapper;
import com.yz.exception.BusinessException;
import com.yz.model.annex.BdsLearnAnnexInfo;
import com.yz.model.annex.BdsLearnAnnexTypeInfo;
import com.yz.vo.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class BdsLearnAnnexService {

    private static final Logger log = LoggerFactory.getLogger(BdsLearnAnnexService.class);

    @Autowired
    private BdsLearnAnnexMapper bdsLearnAnnexMapper;

    public Object findAnnexByLearnId(String learnId) {
        List<BdsLearnAnnexTypeInfo> list = bdsLearnAnnexMapper.findAnnexByLearnId(learnId);
        return list;
    }

    public void insert(BdsLearnAnnexInfo bdsLearnAnnexInfo) {

        //补充插入信息
        LoginUser user = SessionUtil.getUser();
        if (null != user){
            bdsLearnAnnexInfo.setUploadUser(user.getUsername());
            bdsLearnAnnexInfo.setUploadUserId(user.getUserId());
        }else {
            throw new BusinessException("E000034");// 登录超时或尚未登录
        }
        //判断是否必传
        if ("1".equals(bdsLearnAnnexInfo.getIsRequire())) {
            bdsLearnAnnexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_UNCHECK);
        }else {
            bdsLearnAnnexInfo.setAnnexStatus(StudentConstants.ANNEX_STATUS_ALLOW);
        }
        bdsLearnAnnexMapper.insert(bdsLearnAnnexInfo);
    }

    public void deleteAnnexByLearnIdAndAnnexType(String learnId, String annexType) {
        bdsLearnAnnexMapper.deleteAnnexByLearnIdAndAnnexType(learnId,annexType);
    }
}
