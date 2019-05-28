package org.linlinjava.litemall.db.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallCashord;
import org.linlinjava.litemall.db.domain.LitemallCashordExample;
import org.linlinjava.litemall.db.domain.LitemallCashordUnion;

public interface LitemallCashordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_cashord
     *
     * @mbggenerated
     */
    int countByExample(LitemallCashordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_cashord
     *
     * @mbggenerated
     */
    int deleteByExample(LitemallCashordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_cashord
     *
     * @mbggenerated
     */
    int insert(LitemallCashord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_cashord
     *
     * @mbggenerated
     */
    int insertSelective(LitemallCashord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_cashord
     *
     * @mbggenerated
     */
    List<LitemallCashord> selectByExample(LitemallCashordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_cashord
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") LitemallCashord record, @Param("example") LitemallCashordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_cashord
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") LitemallCashord record, @Param("example") LitemallCashordExample example);

    /**
     * 查询提现记录
     * @param map
     * @return
     */
    List<LitemallCashordUnion> CashordList(@SuppressWarnings("rawtypes") Map map);
    

    /**
     * 查询体现记录总数 
     * @param map
     * @return
     */
    long CashordListCount(@SuppressWarnings("rawtypes") Map map);
    
    /**
     * 更新提现状态
     * @param map
     * @return
     */
    int cashpostal(@SuppressWarnings("rawtypes") Map map);
    
}