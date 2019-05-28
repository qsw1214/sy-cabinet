package org.linlinjava.litemall.admin.job;

import java.text.ParseException;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.service.LitemallSubCabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class BagTakJob {
	private final Log logger = LogFactory.getLog(BagTakJob.class);
	@Autowired
	private LitemallSubCabinetService SubService;
	@Scheduled(fixedDelay = 60 * 1000)
	public void checkCouponExpired() throws ParseException {
		int update= SubService.upStatusByUpdateTime();
		logger.info("1分钟检查一次柜子状态,本次修改"+update+"条数据");
		
	}
}
