package org.linlinjava.litemall.db.service;

import net.sf.json.JSONObject;
import org.linlinjava.litemall.db.dao.LitemallDepositMapper;
import org.linlinjava.litemall.db.domain.LitemallDeposit;
import org.linlinjava.litemall.db.domain.LitemallDepositExample;
import org.linlinjava.litemall.db.domain.LitemallDepositExample.Criteria;
import org.linlinjava.litemall.db.util.timeUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author liuyu
 * @date
 */
@Service
public class LitemallDepositService {

    @Resource
    private LitemallDepositMapper depositMapper;

    /**
     * 添加押金信息
     * @param deposit
     * @return
     * @author liuyu
     */
    public int add(LitemallDeposit deposit){
        // 获取当前时间
        Date date = timeUtil.localDateTimeDate(LocalDateTime.now());
        deposit.setAddTime(date);
        deposit.setUpdateTime(date);
        deposit.setDepoDeleted(false);
        return depositMapper.insert(deposit);
    }

    /**
     * 修改押金信息
     * @param deposit
     * @return
     * @author liuyu
     */
    public int update(LitemallDeposit deposit){
        //获取当前时间
        Date date = timeUtil.localDateTimeDate(LocalDateTime.now());
        deposit.setReturnTime(date);
        return depositMapper.updateByPrimaryKeySelective(deposit);
    }
    /**
     * 根据订单ID查询
     * @param usaId
     * @return
     */
    public LitemallDeposit getDsposit(Integer usaId){
    	return depositMapper.getDspositByUsaId(usaId);
    }

    /**
     * 条件查询押金信息
     * @param deposit
     * @return
     * @author liuyu
     */
    public List<LitemallDeposit> list(LitemallDeposit deposit){
        if(StringUtils.isEmpty(deposit.getDepoUserId())){
           return null;
        }
        if(!StringUtils.isEmpty(deposit.getPage()) && !StringUtils.isEmpty(deposit.getSize())) {
            Integer page = deposit.getPage();
            Integer size = deposit.getSize();
            deposit.setPage((page - 1) * size);
        }
        return depositMapper.selectDeposit(deposit);
    }

    public int queryCount(Integer id){
        return depositMapper.queryCount(id);
    }

}
