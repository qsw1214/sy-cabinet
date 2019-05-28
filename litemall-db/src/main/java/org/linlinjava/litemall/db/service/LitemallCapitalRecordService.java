package org.linlinjava.litemall.db.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallCapitalRecordMapper;
import org.linlinjava.litemall.db.domain.LitemallCapitalRecord;
import org.springframework.stereotype.Service;

@Service
public class LitemallCapitalRecordService {

	@Resource
	private LitemallCapitalRecordMapper caprecMapper;

	/**
	 * 谭糠 新增交易记录
	 * 
	 * @return
	 */
	public int add(LitemallCapitalRecord record) {
		return caprecMapper.insert(record);
	}

	/**
	 * 谭糠 根据时间查询交易记录
	 * 
	 * @return
	 */
	public List<LitemallCapitalRecord> AccordingToTime(@SuppressWarnings("rawtypes") Map map) {
		return caprecMapper.AccordingToTime(map);
	}

	/**
	 * 查询 交易记录数量
	 * @param map
	 * @return
	 */
	public int queryAllTotal(@SuppressWarnings("rawtypes") Map map){
		return caprecMapper.queryAllTotal(map);
	}
}
