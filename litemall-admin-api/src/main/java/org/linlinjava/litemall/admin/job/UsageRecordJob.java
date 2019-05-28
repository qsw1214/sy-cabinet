package org.linlinjava.litemall.admin.job;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.notify.NotifyService;
import org.linlinjava.litemall.core.notify.NotifyType;
import org.linlinjava.litemall.db.domain.LitemallUsageAndUser;
import org.linlinjava.litemall.db.service.LitemallUsageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UsageRecordJob {
	private final Log logger = LogFactory.getLog(UsageRecordJob.class);

	@Autowired
	private LitemallUsageRecordService usaService;
	@Autowired
	private NotifyService notifyService;

	/**
	 * 5分钟检查一次到期使用记录
	 * 
	 * @throws ParseException
	 */
	@SuppressWarnings("static-access")
	@Scheduled(fixedDelay = 5 * 60 * 1000)
	public void checkCouponExpired() throws ParseException {
		logger.info("5分钟检查一次到期使用记录");
		System.out.println("发送短信");
		// 查询已经到期的柜子进行状态更改，更改为超时。方便后台管理员查询已到期的柜子，进行清柜操作。（后台管理员手动刷新进行到期柜子查询）
		usaService.Expire();
		NotifyType notifyType = null;
		List<LitemallUsageAndUser> Soonlist = usaService.SoonExpire();
		// 查询还有30分钟到期的柜子，发送短信提示用户使用即将到期，请及时处理或续费。
		for (LitemallUsageAndUser useAnduaer : Soonlist) {
			
			usaService.ExpireNotice(useAnduaer.getUsa().getId());
			// 阿里云短信平台
			String Aliparams = "{number:\"" + useAnduaer.getCount() + "\"}";
			notifyService.AliSms(useAnduaer.getUser().getMobile(), notifyType.NOTICE, Aliparams);
			// 腾讯短信平台
			String[] Tenparams = new String[] { "柜子有" + useAnduaer.getCount() + "个" };
			notifyService.notifySmsTemplate(useAnduaer.getUser().getMobile(), notifyType.NOTICE, Tenparams);
		}
	}


}
