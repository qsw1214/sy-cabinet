package org.linlinjava.litemall.wx.web;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallCapitalRecord;
import org.linlinjava.litemall.db.domain.LitemallDeposit;
import org.linlinjava.litemall.db.service.LitemallCapitalRecordService;
import org.linlinjava.litemall.db.service.LitemallDepositService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.timeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/deposit")
@Validated
public class WxDepositController {

    @Autowired
    private LitemallDepositService depositService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallCapitalRecordService capitalRecordService;

    /**
     * 添加押金信息
     *
     * @param deposit
     * @return 查询结果
     * @author liuyu
     */
    @PostMapping("add")
    public Object addDeposit(@RequestBody LitemallDeposit deposit) {
        int addDepo = depositService.add(deposit);
        if(addDepo == 0){
            return ResponseUtil.badArgument();
        }
        // 添加交易明细
        LitemallCapitalRecord capitalRecord = new LitemallCapitalRecord();
        capitalRecord.setCapDealid(deposit.getDepoId());
        capitalRecord.setCapUserid(deposit.getDepoUserId());
        capitalRecord.setCapTitle("智能储物柜押金");
        capitalRecord.setCapDatetime(timeUtil.localDateTimeDate(LocalDateTime.now()));
        capitalRecord.setCapMoney("-" + deposit.getDepoMoney());
        capitalRecord.setCapStatus(0);
        capitalRecord.setCapDeal("litemall_usage_record");
        int addCap = capitalRecordService.add(capitalRecord);
        if(addCap == 0){
            return ResponseUtil.badArgument();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("depositId",deposit.getDepoId());
        return ResponseUtil.ok(map);
    }

    /**
     * 修改押金信息
     *  1、添加交易明细
     *  2、修改押金状态
     *  3、修改余额
     * @param deposit
     * @return 查询结果
     * @author liuyu
     */
    @PostMapping("update")
    public Object updateDeposit(@RequestBody LitemallDeposit deposit) {
        Date date = timeUtil.localDateTimeDate(LocalDateTime.now());
        //1、修改余额(退款)
        Map<String, Object> map = new HashMap<>();
        map.put("id", deposit.getDepoUserId());
        map.put("balance", deposit.getDepoMoney());
        Integer updateUser = userService.upBalance(map);
        if (updateUser == 0) {
            return ResponseUtil.returnFail();
        }
        //2、修改押金信息(状态1：使用中；2:已退还)
        deposit.setDepoStatus(2);
        deposit.setReturnTime(date);
        int updateDepo = depositService.update(deposit);
        if (updateDepo == 0) {
            return ResponseUtil.badArgument();
        }
        //3、添加交易明细
        LitemallCapitalRecord capRecord = new LitemallCapitalRecord();
        capRecord.setCapDealid(deposit.getDepoId());
        capRecord.setCapUserid(deposit.getDepoUserId());
        capRecord.setCapTitle("智能储物柜押金");
        capRecord.setCapDatetime(date);
        capRecord.setCapMoney("+" + deposit.getDepoMoney());
        capRecord.setCapStatus(1);
        capRecord.setCapDeal("litemall_usage_record");
        int addCap = capitalRecordService.add(capRecord);
        if (addCap == 0) {
            return ResponseUtil.badArgument();
        }
        return ResponseUtil.ok();
    }

    /**
     * 条件查询押金信息
     * @param deposit
     * @return
     * @author liuyu
     */
    @PostMapping("list")
    public Object listDeposit(@RequestBody LitemallDeposit deposit){
        List<LitemallDeposit> depositList = depositService.list(deposit);
        int allTotal = depositService.queryCount(deposit.getDepoUserId());
        long total = PageInfo.of(depositList).getTotal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("items",depositList);
        map.put("total",total);
        map.put("allTotal",allTotal);
        return ResponseUtil.ok(map);
    }

    /**
     * 通过订单Id
     * @param recordId
     * @return
     */
    @GetMapping("queryByUeaId")
    public Object queryDeposit(Integer recordId){
        if(StringUtils.isEmpty(recordId)){
            return ResponseUtil.badArgument();
        }
        LitemallDeposit deposit = depositService.getDsposit(recordId);
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isEmpty(deposit)){
            map.put("isNot",false);      //没有押金
        }else{
            map.put("isNot",true);     //存在押金
        }
        map.put("items",deposit);
        return ResponseUtil.ok(map);
    }

}


