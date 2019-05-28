package org.linlinjava.litemall.wx.util;

import net.sf.json.JSONObject;
import org.apache.catalina.core.ApplicationContext;
import org.linlinjava.litemall.db.domain.LitemallCapitalRecord;
import org.linlinjava.litemall.db.domain.LitemallDeposit;
import org.linlinjava.litemall.db.domain.LitemallUsageRecord;
import org.linlinjava.litemall.db.service.LitemallCapitalRecordService;
import org.linlinjava.litemall.db.service.LitemallDepositService;
import org.linlinjava.litemall.db.service.LitemallUsageRecordService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.timeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordThread extends Thread {

    private volatile boolean isStop = false;

    private LitemallUsageRecordService usageRecordService;

    private LitemallDepositService depositService;

    private LitemallCapitalRecordService capitalRecordService;

    private LitemallUserService userService;

    private StringRedisTemplate stringRedisTemplate;

    private Integer recordId;

    public RecordThread(Integer recordId,
                        LitemallUsageRecordService usageRecordService,
                        LitemallDepositService depositService,
                        LitemallCapitalRecordService capitalRecordService,
                        LitemallUserService userService,
                        StringRedisTemplate stringRedisTemplate) {
        this.recordId = recordId;
        this.usageRecordService = usageRecordService;
        this.depositService = depositService;
        this.capitalRecordService = capitalRecordService;
        this.userService = userService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void run() {
        while(!isStop) {
            try {
                Thread.sleep(5 * 60 * 1000);
                System.out.println("执行线程");
                // 查询订单信息
                LitemallUsageRecord usageRecord = usageRecordService.queryUsageRecord(recordId);
                System.out.println(JSONObject.fromObject(usageRecord));
                if (!usageRecord.getStatus().equals(3)) {
                    return;
                }
                LitemallUsageRecord record = new LitemallUsageRecord();
                record.setId(recordId);
                record.setStatus(4);
                // 修改订单状态
                int updateRecord = usageRecordService.updateRecord(record);
                if (usageRecord.getStanDeposit() != 0) {
                    LitemallDeposit dsposit = depositService.getDsposit(recordId);
                    // 退还押金
                    if (!StringUtils.isEmpty(dsposit)) {
                        int i = updateDeposit(dsposit);
                        System.out.println("退还押金" + i);
                    }
                }
                setStop(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止线程
     * @param stop
     */
    public void setStop(boolean stop){
        this.isStop = stop;
    }

    /**
     * 修改押金
     * @param deposit
     * @return
     */
    public int updateDeposit(LitemallDeposit deposit) {
        Date date = timeUtil.localDateTimeDate(LocalDateTime.now());
        //1、修改余额(退款)
        Map<String, Object> map = new HashMap<>();
        map.put("id", deposit.getDepoUserId());
        map.put("balance", deposit.getDepoMoney());
        Integer updateUser = userService.upBalance(map);
        if (updateUser == 0) {
            return 0;
        }
        //2、修改押金信息(状态1：使用中；2:已退还)
        deposit.setDepoStatus(2);
        deposit.setReturnTime(date);
        int updateDepo = depositService.update(deposit);
        if (updateDepo == 0) {
            return 0;
        }
        //3、添加交易明细
        LitemallCapitalRecord cap = new LitemallCapitalRecord();
        cap.setCapDealid(deposit.getDepoId());
        cap.setCapUserid(deposit.getDepoUserId());
        cap.setCapTitle("智能储物柜押金");
        cap.setCapDatetime(date);
        cap.setCapMoney("+" + deposit.getDepoMoney());
        cap.setCapStatus(1);
        cap.setCapDeal("litemall_usage_record");
        int addCap = capitalRecordService.add(cap);
        if (addCap == 0) {
            return 0;
        }
        return 1;
    }
}
