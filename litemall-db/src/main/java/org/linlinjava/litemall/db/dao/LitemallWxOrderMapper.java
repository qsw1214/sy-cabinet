package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallWxOrder;
import org.linlinjava.litemall.db.domain.LitemallWxOrderExample;

import java.util.List;

public interface LitemallWxOrderMapper {
    long countByExample(LitemallWxOrderExample example);

    int deleteByExample(LitemallWxOrderExample example);

    int deleteByPrimaryKey(Integer orderId);

    int insert(LitemallWxOrder record);

    int insertSelective(LitemallWxOrder record);

    List<LitemallWxOrder> selectByExample(LitemallWxOrderExample example);

    LitemallWxOrder selectByPrimaryKey(Integer orderId);

    int updateByExampleSelective(@Param("record") LitemallWxOrder record, @Param("example") LitemallWxOrderExample example);

    int updateByExample(@Param("record") LitemallWxOrder record, @Param("example") LitemallWxOrderExample example);

    int updateByPrimaryKeySelective(LitemallWxOrder record);

    int updateByPrimaryKey(LitemallWxOrder record);

    // 批量修改订单状态
    int updateBatch(List<Integer> orderList);
}