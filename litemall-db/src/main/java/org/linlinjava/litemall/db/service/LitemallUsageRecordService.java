package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import net.sf.json.JSONObject;

import org.linlinjava.litemall.db.dao.LitemallSubCabinetMapper;
import org.linlinjava.litemall.db.dao.LitemallUsageRecordMapper;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * 使用记录
 * 
 * @author Administrator
 *
 */

@Service
public class LitemallUsageRecordService {
	@Resource
	private LitemallUsageRecordMapper usagerecordMapper;
	@Resource
	private LitemallSubCabinetMapper subCabinetMapper;
	@Resource
	private LitemallMainCabinetService mainCabinetService;
	@Resource
	private LitemallUserService userService;
	@Resource
	private LitemallStandardsService standardsService;
	@Resource
	private LitemallCapitalRecordService caprecService;// 添加交易记录表

	/**
	 * 添加用户使用记录
	 * 
	 * @param record
	 * @return
	 */
	public int insert(LitemallUsageRecord record) {
		Date date = localDateTimeDate(LocalDateTime.now());
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, Integer.parseInt(record.getPreTime()));
		record.setStatus(0);
		record.setActualAmount(Float.parseFloat("0.00"));
		record.setActualTime("0");
		record.setStartTime(date);
		record.setEndTime(c.getTime());// 预计结束时间
		record.setAddTime(date);
		record.setUpdateTime(date);
		record.setDeleted(false);
		int insnum = usagerecordMapper.insert(record);
		LitemallCapitalRecord CapitalRecord = new LitemallCapitalRecord();
		if (insnum > 0) {
			CapitalRecord.setCapDealid(record.getId());
			CapitalRecord.setCapUserid(record.getUserId());
			CapitalRecord.setCapTitle("智能储物柜使用");
			CapitalRecord.setCapDatetime(date);
			CapitalRecord.setCapMoney("-"+record.getPreAmount());
			CapitalRecord.setCapStatus(1);
			CapitalRecord.setCapDeal("litemall_usage_record");
			caprecService.add(CapitalRecord);
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 添加订单信息(状态:未完成)
	 * @return
	 */
	public int insertRec(LitemallUsageRecord record){
		Date date = localDateTimeDate(LocalDateTime.now());
		record.setAddTime(date);
		record.setUpdateTime(date);
		record.setActualAmount(Float.parseFloat("0.00"));
		record.setActualTime("0");
		record.setPreAmount(Float.parseFloat("0.00"));
		record.setPreTime("0");
		record.setStartTime(date);
		record.setEndTime(date);
		record.setDeleted(false);
		return usagerecordMapper.insert(record);
	}

	/**
	 * 根据用户ID查询出最新的一条数据带子柜主柜数据
	 *
	 * @param id
	 * @return
	 */
	public RecordAndMianAndSub getnewOrder(Integer id) {
		return usagerecordMapper.getnewOrder(id);
	}

	/**
	 * 根据用户ID查询所有的正在使用的订单
	 *
	 * @param userid
	 * @return
	 */
	public List<RecordAndMianAndSub> getListOrder(Integer userid) {
		return usagerecordMapper.getListOrder(userid);
	}

	/**
	 * LocalDateTime转换为Date
	 *
	 * @param localDateTime
	 */
	public Date localDateTimeDate(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		Date date = Date.from(zdt.toInstant());
		return date;
	}

	/**
	 * 分页条件查询使用记录*
	 * 
	 * @param record
	 * 
	 * @return
	 */
	public List<LitemallUsageRecord> querySelective(LitemallUsageRecord record, Integer page, Integer size) {
		LitemallUsageRecordExample example = new LitemallUsageRecordExample();
		LitemallUsageRecordExample.Criteria criteria = example.createCriteria();
		Integer status = record.getStatus();
		if (status != -1) {
			criteria.andStatusEqualTo(status);
		}
		if (!StringUtils.isEmpty(record.getUserId())) {
			criteria.andUserIdEqualTo(record.getUserId());
		}
		if (!StringUtils.isEmpty(record.getSubCabId())) {
			criteria.andSubCabIdEqualTo(record.getSubCabId());
		}
		criteria.andDeletedEqualTo(false);
		if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(size)) {
			PageHelper.startPage(page, size);
		}
		return usagerecordMapper.selectByExample(example);
	}

	/**
	 * 查询订单信息 (by userId,status)
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */

	public List<LitemallUsageRecord> listUsageRecord(Integer userId, Integer status,Integer page, Integer size) {
		LitemallUsageRecordExample example = new LitemallUsageRecordExample();
		// 声明 “条件约束”
		LitemallUsageRecordExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andStatusEqualTo(status);
		criteria.andDeletedEqualTo(false);
		if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(size)) {
			PageHelper.startPage(page, size);
		}
		return usagerecordMapper.selectByExample(example);
	}

	/**
	 * 分页条件查询
	 * @param userId
	 * @param status
	 * @return
	 */
	public List<LitemallUsageRecord> selectRecord(Integer userId, Integer status,Integer page,Integer size){
		if(StringUtils.isEmpty(userId)){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("userId",userId);
		map.put("status",status);
		if(!StringUtils.isEmpty(page) && !StringUtils.isEmpty(size)){
			page = (page - 1) * size;
			map.put("page",page);
			map.put("size",size);
		}
		return usagerecordMapper.selectRecord(map);
	}

	/**
	 * 查询订单详情
	 * 
	 * @param recordId
	 * @return
	 */
	public LitemallUsageRecord queryUsageRecord(Integer recordId) {
		if (StringUtils.isEmpty(recordId)) {
			return null;
		}
		return usagerecordMapper.queryUsage(recordId);
	}

	/**
	 * 取包走人-->计算费用
	 * 1、查询使用记录
	 * 2、查询子柜信息
	 * 3、计算使用时间
	 * 4、计算使用金额
	 */
	public LitemallUsageRecord calculateCost(Integer recordId){
		Float actualAmount = 0F;
		// 1.查询用户使用记录
		LitemallUsageRecord record = usagerecordMapper.selectByPrimaryKey(recordId);
		if (StringUtils.isEmpty(record)) {
			return null;
		}
		//2、获取规格信息
		Integer subCabId = record.getSubCabId();
		LitemallStandards standards = standardsService.selectStanBySub(subCabId);
		if(StringUtils.isEmpty(standards)){
			return null;
		}
		//3、计算使用时间
		//3.1、获取规则时间
		Integer stanBeyond = standards.getStanBeyond();	//超出时间
		Integer stanFree = standards.getStanFree();		//免费时间
		Integer stanCapp = standards.getStanCapping();	//封顶时间
		Integer stanMoney = Integer.valueOf(standards.getStanMoney());

		Date startTime = record.getStartTime();
		// 3.2、获取当前时间
		LocalDateTime nowDateTime = LocalDateTime.now();
		Date nowDate = mainCabinetService.localDateTimeDate(nowDateTime);
		// 3.3、计算使用时间(分)
		long preM = (nowDate.getTime() - startTime.getTime()) / (1000 * 60);
		Long preH = preM / 60;
		// 判断是否为空
		if (StringUtils.isEmpty(stanFree)) {
			stanFree = 0;
		}
		if (StringUtils.isEmpty(stanBeyond)) {
			stanBeyond = 0;
		}
		if (StringUtils.isEmpty(stanCapp)) {
			stanCapp = 0;
		}
		// 计算时间使用时间,超出时间,算一小时
		long residue = preM % 60;
		if (residue >= stanBeyond) {
			preH = preH + 1;
		}
		// 是否封顶()
		if (preH >= stanCapp) {
			actualAmount = Float.valueOf(stanCapp * stanMoney);
		} else {
			// 超过免费时间
			if (preM > stanFree) {
				actualAmount = Float.valueOf(preH.intValue() * stanMoney);
			}
		}
		record.setActualAmount(actualAmount);
		record.setEndTime(nowDate);
		record.setActualTime(String.valueOf(preH));
		return record;
	}


	 /**
	  * 修改
	  * 1、usageR--修改用户使用记录表(状态，实际金额和时间)
	  * 2、sub-----修改子柜状态
	  */
	 public int updateUsSub( LitemallUsageRecord record,LitemallSubCabinet subCabinet){
		int recResponse = usagerecordMapper.updateByPrimaryKeySelective(record);
		int subResponse = 2;
		if(!StringUtils.isEmpty(subCabinet.getId())) {
//			if(subCabinet.getStatus().equals(0)) {
//				Timer nTimer = new Timer();
//				nTimer.schedule(new TimerTask() {
//					public void run() {
//						 final int subResponse = subCabinetMapper.updateByPrimaryKeySelective(subCabinet);
//					}
//				},6000);
//
//			}else {
				subResponse = subCabinetMapper.updateByPrimaryKeySelective(subCabinet);
//			}
		}
		if (recResponse == 1 || subResponse !=0) {
			return 1;
		}
		return 0;
	}

	/**
	 * 修改订单状态
	 * 
	 * @param record
	 * @return
	 */
	public int updateRecord(LitemallUsageRecord record) {
		if (StringUtils.isEmpty(record.getId())) {
			return 0;
		}
		return usagerecordMapper.updateByPrimaryKeySelective(record);
	}


	/**
	 * 根据输入密码查询柜组下面所有正在使用的柜子
	 * 
	 * @param mainId
	 * @param userId
	 * @return
	 */
	public List<UsageRecordAndSubCabinet> RecordAndSubMap(Integer mainId, Integer userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("mainId", mainId);
		map.put("userId", userId);
		return usagerecordMapper.RecordAndSub(map);
	}

	/**
	 * 修改实际金额 时间 状态
	 * 
	 * @param usaId
	 * @param actualTime
	 * @param actualAmount
	 * @param status
	 * @param endTime
	 * @return
	 */
	public int BagTaking(Integer usaId, String actualTime, Float actualAmount, Integer status, Date endTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("usaId", usaId);
		map.put("actualTime", actualTime);
		map.put("actualAmount", actualAmount);
		map.put("status", status);
		map.put("endTime", endTime);
		return usagerecordMapper.BagTaking(map);
	}

	/**
	 * 柜子使用到期
	 * 
	 * @return
	 */
	public int Expire() {
		Date date = localDateTimeDate(LocalDateTime.now());
		return usagerecordMapper.Expire(date);
	}

	/**
	 * 柜子使用即将到期（这里为用户发送提示短信）
	 * 
	 * @return
	 */
	public List<LitemallUsageAndUser> SoonExpire() {
		Map<String, Object> map = new HashMap<>();
		Date date = localDateTimeDate(LocalDateTime.now());
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, 30); // 提前半小时
		map.put("date", c.getTime());
		return usagerecordMapper.SoonExpire(map);
	}
	/**
	 * 修改当前订单已发送到期通知
	 * @param id
	 * @return
	 */
	public int ExpireNotice(Integer id){
		return usagerecordMapper.ExpireNotice(id);
	}

	/**
	 * 查询用户使用记录
	 * 
	 * @param userId
	 * @return
	 */
	public Integer queryRecordCount(Integer userId) {
		return usagerecordMapper.queryRecordCount(userId);
	}
	
	/**
	 * 根据子柜ID查询已超时的使用记录
	 * @param subId
	 * @return
	 */
	public LitemallUsageRecord getUsaBySubId(Integer subId) {
		return usagerecordMapper.getUsaBySubId(subId);
	}

	public int queryAllTotal(Integer userId, Integer status){
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("status",status);
		return usagerecordMapper.queryAllTotal(map);
	}

}
