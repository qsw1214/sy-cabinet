package org.linlinjava.litemall.db.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallUsageAndUser;
import org.linlinjava.litemall.db.domain.LitemallUsageRecord;
import org.linlinjava.litemall.db.domain.LitemallUsageRecordExample;
import org.linlinjava.litemall.db.domain.RecordAndMianAndSub;
import org.linlinjava.litemall.db.domain.UsageRecordAndSubCabinet;

public interface LitemallUsageRecordMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	int countByExample(LitemallUsageRecordExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	int deleteByExample(LitemallUsageRecordExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	int insert(LitemallUsageRecord record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	int insertSelective(LitemallUsageRecord record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	List<LitemallUsageRecord> selectByExample(LitemallUsageRecordExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	LitemallUsageRecord selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	int updateByExampleSelective(@Param("record") LitemallUsageRecord record,
			@Param("example") LitemallUsageRecordExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	int updateByExample(@Param("record") LitemallUsageRecord record,
			@Param("example") LitemallUsageRecordExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	int updateByPrimaryKeySelective(LitemallUsageRecord record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table litemall_usage_record
	 *
	 * @mbggenerated
	 */
	int updateByPrimaryKey(LitemallUsageRecord record);

	/**
	 * 查询订单详细信息(包含柜组区域信息)
	 * 
	 * @param recordId
	 * @return
	 */
	LitemallUsageRecord queryUsage(Integer recordId);

	RecordAndMianAndSub getnewOrder(Integer id);

	List<RecordAndMianAndSub> getListOrder(Integer userId);

	int updateRecord(@SuppressWarnings("rawtypes") Map map);

	// 查询用户正在使用柜子信息 (当前\超时)
	int queryRecordCount(Integer userId);

	/**
	 * 根据输入密码查询柜组下面所有正在使用的柜子
	 * 
	 * @param map
	 * @return
	 */
	List<UsageRecordAndSubCabinet> RecordAndSub(@SuppressWarnings("rawtypes") Map map);

	/**
	 * APP开柜
	 * 
	 * @param map
	 * @return
	 */
	int BagTaking(@SuppressWarnings("rawtypes") Map map);

	/**
	 * 提前三十分钟查询即将到期的
	 * 
	 * @param current
	 * @return
	 */
	List<LitemallUsageAndUser> SoonExpire(@SuppressWarnings("rawtypes") Map map);

	/**
	 * 查询当前已到期的柜子
	 * 
	 * @param current
	 * @return
	 */
	int Expire(Date date);
	
	/**
	 * 修改当前订单已发送到期通知
	 * @param id
	 * @return
	 */
	int ExpireNotice(Integer id);
	
	/**
	 * 根据子柜ID查询已超时的使用记录
	 * @param subId
	 * @return
	 */
	LitemallUsageRecord getUsaBySubId(Integer subId);

	int queryAllTotal(Map map);

	/**
	 * 条件查询订单信息
	 * @param map
	 * @return
	 */
	List<LitemallUsageRecord> selectRecord(Map map);
}