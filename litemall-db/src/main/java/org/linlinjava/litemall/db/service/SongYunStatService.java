package org.linlinjava.litemall.db.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.SongYunStatMapper;

@Service

public class SongYunStatService {
	@Resource
	private SongYunStatMapper SystatMapper;
	/**
	 * 根据年月日统计柜子实际赚取金额 
	 * @param ymd
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> ProfitStati(Map map){
		return SystatMapper.ProfitStati(map);
	}
	
	/**
	 * 获取总利润
	 * @return
	 */
	public String totalProfit(){
		return SystatMapper.totalProfit();
	}
}
