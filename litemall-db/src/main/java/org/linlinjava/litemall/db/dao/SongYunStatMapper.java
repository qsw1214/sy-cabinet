package org.linlinjava.litemall.db.dao;

import java.util.List;
import java.util.Map;


public interface SongYunStatMapper {
	
	/**
	 * 根据年月日统计柜子实际赚取金额 
	 * @param ymd
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> ProfitStati(Map map);
	
	/**
	 * 获取总利润
	 * @return
	 */
	String totalProfit();

}
