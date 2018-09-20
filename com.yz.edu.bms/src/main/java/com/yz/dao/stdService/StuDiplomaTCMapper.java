package com.yz.dao.stdService;

import com.yz.model.stdService.StuDiplomaConfig;
import com.yz.model.stdService.StuDiplomaConfigUnvis;
import com.yz.model.stdService.StuDiplomaTC;
import com.yz.model.stdService.StuDiplomaTCQuery;
import org.apache.ibatis.annotations.Param;

import javax.naming.Name;
import java.util.List;

public interface StuDiplomaTCMapper {
    /**
     * 根据查询条件获取毕业证发放配置列表
     * @param stuDiplomaTCQuery
     * @return
     */
    List<StuDiplomaTC> findDiplomaTCList(StuDiplomaTCQuery stuDiplomaTCQuery);

    /**
     * 批量修改转态
     * @param configIds
     * @param status
     */
    void updateStatus(@Param("configIds") String[] configIds,@Param("status") String status);

    /**
     * 根据配置ID删除毕业证发放任务配置信息
     * @param configId
     */
    void deleteByConfigId(String configId);

    /**
     * 根据配置ID删除毕业证发放任务配置关联信息
     * @param configId
     */
    void deleteUnvisByConfigId(String configId);

    /**
     * 根据配置ID获取配置信息
     * @param configId
     * @return
     */
    StuDiplomaTC getDiplomaTCByConfigId(String configId);

    /**
     * 插入配置信息
     * @param s
     * @return
     */
    Long insert(StuDiplomaConfig s);

    /**
     * 插入配置关联信息
     * @param stuDiplomaConfigUnvis
     * @param configId
     */
    void insertUnvis(@Param("stuDiplomaConfigUnvis") List<StuDiplomaConfigUnvis> stuDiplomaConfigUnvis,@Param("configId") String configId);


    /**
     * 修改配置
     * @param s
     */
    void update(StuDiplomaConfig s);

    /**
     * 修改容量和剩余
     * @param configId
     * @param number
     * @param alNumber
     */
    void updateNumber(@Param("configId") String configId, @Param("number") String number, @Param("alNumber") int alNumber);

    /**
     * 根据关联ID批量删除关联信息
     * @param delIds
     */
    void deleteUnvisByIds(@Param("delIds") String[] delIds);
}
