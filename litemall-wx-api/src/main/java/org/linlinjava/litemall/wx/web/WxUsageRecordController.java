package org.linlinjava.litemall.wx.web;

import com.github.pagehelper.PageInfo;
//import jdk.internal.org.objectweb.asm.tree.InnerClassNode;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.wx.util.RecordThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.linlinjava.litemall.db.util.timeUtil.localDateTimeDate;

@RestController
@RequestMapping("/wx/usarec")
@Validated
public class WxUsageRecordController {
    @Autowired
    private LitemallUsageRecordService usageRecordService;
    @Autowired
    private LitemallSubCabinetService subCabinetService;
    private LitemallSubCabinet subCabinet = new LitemallSubCabinet();
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallMainCabinetService mainCabinetService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LitemallCapitalRecordService capitalRecordService;
    @Resource
    private LitemallCapitalRecordService caprecService;// 添加交易记录表
    @Resource
    private LitemallDepositService depositService; ///

    /**
     * 获取用户个人信息
     *
     * @param userId 用户ID
     * @param
     * @return 查询结果
     */
    @GetMapping("getuser")
    public Object getuser(Integer userId) {
        Map<Object, Object> result = new HashMap<Object, Object>();
        LitemallUser user = userService.getUser(userId);
        result.put("user", user);
        return ResponseUtil.ok(result);
    }

    /**
     * 用户确认下单
     *
     * @param record
     * @return
     */
    @PostMapping("add")
    public Object add(@RequestBody LitemallUsageRecord record) {
        Map<Object, Object> data = new HashMap<Object, Object>();
        subCabinet.setStatus(1);
        subCabinet.setId(record.getSubCabId());
        int usesub = subCabinetService.UseSubCab(subCabinet);// 修改储物状态
        if (usesub > 0) {
            int insert = usageRecordService.insert(record);// 新增用户使用记录
            if (insert > 0) {
                data.put("addcode", true);
                data.put("usaId", record.getId());
            } else {// 添加失败修改回状态
                subCabinet.setStatus(0);
                subCabinet.setId(record.getSubCabId());
                subCabinetService.UseSubCab(subCabinet);// 修改储物状态
                data.put("addcode", false);
            }
        } else {// 修改状态失败
            data.put("addcode", false);
        }
        return ResponseUtil.ok(data);
    }

    /**
     * 添加订单(未完成订单) 刘宇
     */
    @PostMapping("addRec")
    public Object addRec(@RequestBody LitemallUsageRecord record) throws InterruptedException {
        int insert = usageRecordService.insertRec(record);
        if (insert == 0) {
            return ResponseUtil.badArgumentValue();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("recordId", record.getId());
        Integer recordId = record.getId();
        System.out.println(recordId);
        // 存入缓存
        if (!StringUtils.isEmpty(record.getSubCabId()) && !StringUtils.isEmpty(record.getUserId())) {
            Integer subCabId = record.getSubCabId();
            Integer userId = record.getUserId();
            String boxid = subCabId.toString();
            String userIdStr = userId.toString();
            stringRedisTemplate.opsForValue().set(boxid, userIdStr, 60 * 5, TimeUnit.SECONDS);
        }
        // 调用线程
        RecordThread thread = new RecordThread(recordId, usageRecordService, depositService,
                capitalRecordService, userService, stringRedisTemplate);
        thread.start();
        return ResponseUtil.ok(map);
    }

    /**
     * 删除订单信息(逻辑删除)
     */
    @GetMapping("delete")
    public Object delete(Integer id) {
        if (StringUtils.isEmpty(id)) {
            return ResponseUtil.badArgumentValue();
        }
        LitemallUsageRecord record = new LitemallUsageRecord();
        record.setId(id);
        record.setDeleted(true);
        record.setUpdateTime(localDateTimeDate(LocalDateTime.now()));
        int i = usageRecordService.updateRecord(record);
        if (i == 0) {
            return ResponseUtil.badArgument();
        }
        return ResponseUtil.ok();
    }

    /**
     * 刘宇 修改订单信息
     *
     * @param record
     * @return
     */
    @PostMapping("updateRecord")
    public Object updateRecord(@RequestBody LitemallUsageRecord record) {
        Date date = localDateTimeDate(LocalDateTime.now());
        // 修改结束时间-- 开始时间+预存时间
        if (!StringUtils.isEmpty(record.getPreTime())) {
            Calendar c = Calendar.getInstance();
            c.setTime(record.getStartTime());
            c.add(Calendar.HOUR, Integer.parseInt(record.getPreTime()));
            record.setEndTime(c.getTime());
        }
        int i = usageRecordService.updateRecord(record);
        if (i == 0) {
            return ResponseUtil.badArgument();
        }
        return ResponseUtil.ok();
    }

    /**
     * 根据用户ID查询出最新的一条数据带子柜主柜数据
     *
     * @param id
     * @return
     */
    @GetMapping("getneworder")
    public Object getnewOrder(Integer id) {
        Map<Object, Object> data = new HashMap<Object, Object>();
        data.put("newOrder", usageRecordService.getnewOrder(id));
        return ResponseUtil.ok(data);
    }

    /**
     * 根据用户ID查询所有的正在使用的订单
     *
     * @param userId
     * @return
     */
    @GetMapping("listorder")
    public Object getListOrder(Integer userId) {
        Map<Object, Object> data = new HashMap<Object, Object>();
        List<RecordAndMianAndSub> ListOrder = usageRecordService.getListOrder(userId);
        // 获取当前时间
        LocalDateTime nowDateTime = LocalDateTime.now();
        Date nowDate = mainCabinetService.localDateTimeDate(nowDateTime);
        for (RecordAndMianAndSub rms : ListOrder) {
            Date startTime = rms.getStartTime();
            long actuM = (nowDate.getTime() - startTime.getTime()) / (1000 * 60);
            long actuH = actuM / 60;
            long minute = actuM % 60;

            String actualTime = actuH + "小时" + minute + "分钟";
            rms.setActualTime(actualTime);
        }
        if (ListOrder.size() > 0) {
            data.put("type", true);
            data.put("listorder", ListOrder);
        } else {
            data.put("type", false);
        }
        return ResponseUtil.ok(data);
    }

    /**
     * 条件查询用户信息
     *
     * @param userId 用户Id
     * @param status 订单状态
     * @return
     */
    @RequestMapping(value = "/usageList", method = RequestMethod.GET)
    public Object listUsage(Integer userId, Integer status, Integer page, Integer size) {
        // 判断userId和status不为空
        if ((StringUtils.isEmpty(userId) || userId.equals(0)) && StringUtils.isEmpty(status)) {
            return ResponseUtil.badArgument();
        }
        //查询总数量
        int allTotal = usageRecordService.queryAllTotal(userId, status);
        // 查询订单信息
        List<LitemallUsageRecord> listUsageRecord = usageRecordService.listUsageRecord(userId, status, page, size);
        long total = PageInfo.of(listUsageRecord).getTotal();
        // 查询信息存入集合
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", listUsageRecord);
        data.put("allTotal", allTotal);
        return ResponseUtil.ok(data);
    }

    /**
     * 条件查询订单信息
     *
     * @param userId
     * @param status
     * @return
     */
    @GetMapping("selectRecord")
    public Object selectRecord(Integer userId, Integer status, Integer page,Integer size){
        // 判断userId和status不为空
        if ((StringUtils.isEmpty(userId) || userId.equals(0)) && StringUtils.isEmpty(status)) {
            return ResponseUtil.badArgument();
        }
        List<LitemallUsageRecord> list = usageRecordService.selectRecord(userId, status,page,size);
        long total = PageInfo.of(list).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", list);
        return ResponseUtil.ok(data);
    }

    /**
     * 单个查询订单信息
     *
     * @param id
     * @return
     */
    @GetMapping("queryUsage")
    public Object queryUsage(Integer id) {
        // 判断订单id不为空
        if (StringUtils.isEmpty(id)) {
            return ResponseUtil.badArgument();
        }
        LitemallUsageRecord usageRecord = usageRecordService.queryUsageRecord(id);
        return ResponseUtil.ok(usageRecord);
    }

    /**
     * 查询实际花费金额
     *
     * @param recordId
     * @return
     */
    @GetMapping("cost")
    public Object takeBag(Integer recordId, Integer subCabId) {
        LitemallUsageRecord usageRecord = usageRecordService.calculateCost(recordId);
        if (StringUtils.isEmpty(usageRecord)) {
            return ResponseUtil.badArgument();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("items", usageRecord);
        return ResponseUtil.ok(map);
    }

    /**
     * 修改使用记录和子柜状态
     *
     * @param record
     * @return
     */
    @PostMapping("updateUsaRecord")
    public Object updateUsaSub(@RequestBody LitemallUsageRecord record) {
        Date date = localDateTimeDate(LocalDateTime.now());
        if (StringUtils.isEmpty(record.getId())) {
            return ResponseUtil.badArgument();
        }
        // 子柜入参
        LitemallSubCabinet subCabinet = new LitemallSubCabinet();
        if (!StringUtils.isEmpty(record.getSubCabId())) {
            subCabinet.setId(record.getSubCabId());
            subCabinet.setStatus(record.getSubStatus());
        }
        // 将未完成订单状态修改
        if (record.getStatus().equals(1)) {
            record.setStartTime(date);
        }
        record.setEndTime(date);
        record.setUpdateTime(date);
        // 续费修改结束时间
        if (!StringUtils.isEmpty(record.getPreTime())) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, Integer.parseInt(record.getPreTime()));
            record.setEndTime(c.getTime());
        }
        int i = usageRecordService.updateUsSub(record, subCabinet);
        if (i == 0) {
            return ResponseUtil.badArgument();
        }
        return ResponseUtil.ok();
    }

    /**
     * 查询用户单个柜组下子柜信息
     *
     * @param userId
     * @return
     */
    @GetMapping("queryRecordCount")
    public Object queryRecordCount(Integer userId) {
        if (StringUtils.isEmpty(userId)) {
            return ResponseUtil.badArgument();
        }
        Integer recordSize = usageRecordService.queryRecordCount(userId);
        Map<String, Integer> map = new HashMap<>();
        map.put("total", recordSize);
        return ResponseUtil.ok(map);
    }

    /**
     * 根据输入密码查询柜组下面所有正在使用的柜子
     *
     * @param mainId
     * @param phopas
     * @return
     */
    @GetMapping("recandsub")
    public Object RecordAndSubMap(Integer mainId, String phopas) {
        Map<Object, Object> data = new HashMap<Object, Object>();
        if (phopas.length() == 15) {
            String Mobile = phopas.substring(0, 11);
            String Password = phopas.substring(11);
            LitemallUser User = userService.queryByMobileAndPass(Mobile, Password);
            if (User != null) {
                List<UsageRecordAndSubCabinet> list = usageRecordService.RecordAndSubMap(mainId, User.getId());
                if (list.size() > 0) {
                    data.put("listcode", true);
                    data.put("items", list);
                } else {
                    data.put("listcode", false);
                }
            } else {
                data.put("listcode", false);
            }
        } else {
            data.put("listcode", false);
        }

        return ResponseUtil.ok(data);
    }

    /**
     * APP选择柜子进行打开
     *
     * @return
     */
    @GetMapping("bagtaking")
    public Object BagTaking(Integer userId, Float PayCost, Integer usaId, String actualTime, Float actualAmount,
                            Date endTime) {
        LitemallUsageRecord usa = usageRecordService.queryUsageRecord(usaId);
        Map<Object, Object> data = new HashMap<Object, Object>();
        Map<String, Object> map = new HashMap<>();
        LitemallCapitalRecord CapitalRecord = new LitemallCapitalRecord();
        map.put("balance", PayCost);
        map.put("id", userId);
        int upBalance = userService.upBalance(map);// 返还金额

        if (upBalance > 0) {
            LitemallUser user = userService.getUser(userId);
            if (user.getBalance() > 0) { // 如果用户余额大于0 就返还押金
                LitemallDeposit deposit = depositService.getDsposit(usaId);
                if (deposit != null) {
                    map.put("balance", deposit.getDepoMoney());
                    Integer balanceType = userService.upBalance(map);
                    if (balanceType > 0) {
                        deposit.setDepoStatus(2);
                        depositService.update(deposit);
                        CapitalRecord.setCapDealid(usaId);
                        CapitalRecord.setCapUserid(userId);
                        CapitalRecord.setCapTitle("押金返还");
                        CapitalRecord.setCapDatetime(endTime);
                        CapitalRecord.setCapMoney("+" + deposit.getDepoMoney());
                        CapitalRecord.setCapStatus(1);
                        CapitalRecord.setCapDeal("litemall_usage_record");
                        caprecService.add(CapitalRecord);
                    }
                }
            }
            int type = usageRecordService.BagTaking(usaId, actualTime, actualAmount, 1, endTime);
            if (type > 0) {
                subCabinet.setId(usa.getSubCabId());
                subCabinet.setStatus(5);
                subCabinetService.updateSubCab(subCabinet); // 修改柜子状态
                if (PayCost != 0) {
                    CapitalRecord.setCapDealid(usaId);
                    CapitalRecord.setCapUserid(userId);
                    CapitalRecord.setCapTitle("柜子使用返还金额");
                    CapitalRecord.setCapDatetime(endTime);
                    if (PayCost > 0) {
                        CapitalRecord.setCapMoney("+" + PayCost.toString());
                    } else {
                        CapitalRecord.setCapMoney(PayCost.toString());
                    }
                    CapitalRecord.setCapStatus(1);
                    CapitalRecord.setCapDeal("litemall_usage_record");
                    caprecService.add(CapitalRecord);
                }
                data.put("bagkcode", true);
            } else {
                data.put("bagkcode", false);
            }
        } else {
            data.put("bagkcode", false);
        }
        return ResponseUtil.ok(data);
    }

}
