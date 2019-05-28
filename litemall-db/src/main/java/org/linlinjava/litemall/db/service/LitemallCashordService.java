package org.linlinjava.litemall.db.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallCashordMapper;
import org.linlinjava.litemall.db.domain.LitemallCashord;
import org.linlinjava.litemall.db.domain.LitemallCashordUnion;
import org.springframework.stereotype.Service;

@Service
public class LitemallCashordService {

	@Resource
	private  LitemallCashordMapper cashordMapper;
	
	/**
	 * 添加提现记录
	 * @param cashord
	 * @return
	 */
	public int insert(LitemallCashord cashord){
		cashord.setSubTime(localDateTimeDate(LocalDateTime.now()));
		cashord.setUpTime(localDateTimeDate(LocalDateTime.now()));
		return cashordMapper.insert(cashord);
	}
	
	/**
	 * 查询提现记录
	 * @param username
	 * @param page
	 * @param size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LitemallCashordUnion> listcashord(String username,String status,Integer page,Integer size){
		@SuppressWarnings("rawtypes")
		Map map = new HashMap();
		map.put("status", status);
		map.put("username","%"+ username+"%");
		map.put("page", (page-1)*size);
        map.put("size", size);
        return cashordMapper.CashordList(map);
	}
	/**
	 * 查询提现记录总数
	 * @param username
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long listcashordCount(String username,String status){
		@SuppressWarnings("rawtypes")
		Map map = new HashMap();
		map.put("status", status);
		map.put("username","%"+ username+"%");
        return cashordMapper.CashordListCount(map);
	}
	
	/**
	 * 提现状态更新
	 * @param remarks 	备注
	 * @param id		记录ID
	 * @param status	体现状态
	 * @param adminId	操作人员
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int cashpostal(String remarks,Integer id,Integer status,Integer adminId){
		@SuppressWarnings("rawtypes")
		Map map = new HashMap();
		map.put("remarks",remarks);
		map.put("id",id);
		map.put("status", status);
		map.put("adminId", adminId);
		map.put("upTime", LocalDateTime.now());
		return cashordMapper.cashpostal(map);
	}
	/**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public Date localDateTimeDate( LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
       return date;
    }

}
