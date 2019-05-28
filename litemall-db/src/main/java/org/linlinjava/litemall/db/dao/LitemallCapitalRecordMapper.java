package org.linlinjava.litemall.db.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallCapitalRecord;
import org.linlinjava.litemall.db.domain.LitemallCapitalRecordExample;

public interface LitemallCapitalRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    int countByExample(LitemallCapitalRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    int deleteByExample(LitemallCapitalRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer capId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    int insert(LitemallCapitalRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    int insertSelective(LitemallCapitalRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    List<LitemallCapitalRecord> selectByExample(LitemallCapitalRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    LitemallCapitalRecord selectByPrimaryKey(Integer capId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") LitemallCapitalRecord record, @Param("example") LitemallCapitalRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") LitemallCapitalRecord record, @Param("example") LitemallCapitalRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(LitemallCapitalRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(LitemallCapitalRecord record);
    
    /**
     * 根据月份查询交易记录
     */
    List<LitemallCapitalRecord> AccordingToTime(@SuppressWarnings("rawtypes") Map map);

    /**
     * 据月份查询交易记录数量
     */
    int queryAllTotal(@SuppressWarnings("rawtypes") Map map);
}