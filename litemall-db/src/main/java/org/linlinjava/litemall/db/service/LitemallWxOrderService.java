package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallWxOrderMapper;
import org.linlinjava.litemall.db.domain.LitemallWxOrder;
import org.linlinjava.litemall.db.domain.LitemallWxOrderExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.linlinjava.litemall.db.util.timeUtil.localDateTimeDate;

@Service
public class LitemallWxOrderService {

    @Resource
    private LitemallWxOrderMapper wxOrderMapper;

    /**
     * 创建微信支付订单信息
     * @return
     */
    public int create(LitemallWxOrder wxOrder){
        Date date = localDateTimeDate(LocalDateTime.now());
        if(StringUtils.isEmpty(wxOrder.getOutTradeNo())){
            return 0;
        }
        if(StringUtils.isEmpty(wxOrder.getTotalFee())){
            return 0;
        }
        wxOrder.setAddTime(date);
        wxOrder.setUpdateTime(date);
        wxOrder.setDeleted(false);
        return wxOrderMapper.insert(wxOrder);
    }

    /**
     * 条件查询支付订单信息
     * @param UserId
     * @param sort
     * @param order
     * @return
     */
    public List<LitemallWxOrder> list(Integer UserId, Integer orderStatus, String sort, String order){
        LitemallWxOrderExample example = new LitemallWxOrderExample();
        LitemallWxOrderExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(UserId)){
            criteria.andUserIdEqualTo(UserId);
        }
        if(!StringUtils.isEmpty(orderStatus)){
            criteria.andOrderStatusEqualTo(orderStatus);
        }
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }
        return wxOrderMapper.selectByExample(example);
    }


    /**
     * 修改订单状态
     * @param wxOrder
     * @return
     */
    public int update(LitemallWxOrder wxOrder){
        Date date = localDateTimeDate(LocalDateTime.now());
        wxOrder.setUpdateTime(date);
        return wxOrderMapper.updateByPrimaryKeySelective(wxOrder);
    }

    /**
     * 单个查询微信支付订单
     * @param orderId
     * @return
     */
    public LitemallWxOrder query(Integer orderId){
        if(StringUtils.isEmpty(orderId)){
            return null;
        }
        return wxOrderMapper.selectByPrimaryKey(orderId);
    }

    /**
     * 批量修改
     * @param orderList
     * @return
     */
    public int updateBacth(List<Integer> orderList){
        if(StringUtils.isEmpty(orderList) && orderList.size() == 0){
            return 0;
        }
        return  wxOrderMapper.updateBatch(orderList);
    }

}
