package org.linlinjava.litemall.wx.service;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.notify.NotifyService;
import org.linlinjava.litemall.core.notify.NotifyType;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallWxOrder;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.service.LitemallWxOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static org.linlinjava.litemall.wx.util.AdminResponseCode.ORDER_REFUND_FAILED;

@Service
public class WxRefundOrderService {

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private LitemallWxOrderService wxOrderService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private LitemallUserService userService;

    private final Log logger = LogFactory.getLog(WxRefundOrderService.class);

    /**
     * 微信退款
     * @param userId
     * @return
     */
    @Transactional
    public Object refund(Integer userId, Integer orderStatus,float refundFeeFlo,
                         String sort, String order){
        // 查询订单信息
        List<LitemallWxOrder> list = wxOrderService.list(userId, orderStatus, sort,order);
        if(StringUtils.isEmpty(list) || list.size() ==0){
            return ResponseUtil.badArgument();
        }
        if(StringUtils.isEmpty(refundFeeFlo)){
            return ResponseUtil.badArgument();
        }
        // 元转分
        BigDecimal refundDec = new BigDecimal(refundFeeFlo);
        int refundInt = refundDec.multiply(new BigDecimal(1)).intValue();
        // 订单集合
        HashMap<String, Object> balanceMap = new HashMap<>();
        LitemallWxOrder order1 = new LitemallWxOrder();
        int refundFee = 0;
        for(LitemallWxOrder wxOrder : list) {
            String outTradeNo = wxOrder.getOutTradeNo();
            Integer totalFee = wxOrder.getTotalFee();
            // 校验订单号和订单金额
            if (StringUtils.isEmpty(outTradeNo)) {
                return ResponseUtil.badArgument();
            }
            if (StringUtils.isEmpty(totalFee) && totalFee == 0) {
                return ResponseUtil.badArgument();
            }

            // 计算退款
            if(refundInt >= totalFee){
                refundFee = totalFee;
                refundInt = refundInt - totalFee;
            }else{
                refundFee = refundInt;
                refundInt = 0;
            }
            // 微信退款
            WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
            wxPayRefundRequest.setOutTradeNo(outTradeNo);
            wxPayRefundRequest.setOutRefundNo("refund_" + outTradeNo);
            wxPayRefundRequest.setTotalFee(totalFee);
            wxPayRefundRequest.setRefundFee(refundFee);

            WxPayRefundResult wxPayRefundResult = null;
            try {
                wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
            } catch (WxPayException e) {
                e.printStackTrace();
                return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
            }
            if (!wxPayRefundResult.getReturnCode().equals("SUCCESS")) {
                logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
                return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
            }
            // TODO 发送邮件和短信通知，这里采用异步发送
            // 退款成功通知用户, 例如“您申请的订单退款 [ 单号:{1} ] 已成功，请耐心等待到账。”
            // 注意订单号只发后6位
            notifyService.notifySmsTemplate(wxOrder.getMobile(), NotifyType.REFUND,
                    new String[]{outTradeNo.substring(8, 14)});

            //修改微信订单状态
            order1.setOrderId(wxOrder.getOrderId());
            order1.setOrderStatus(4);
            order1.setOutRefundNo("refund_" + outTradeNo);
            order1.setRefundFee(refundFee);
            int update = wxOrderService.update(order1);
            //修改余额
            Float balance =(float) refundFee/100;
            balanceMap.put("balance",balance);
            balanceMap.put("id",wxOrder.getUserId());
            Integer integer = userService.buckleBalance(balanceMap);
            if(refundInt == 0){
                break;
            }
        }
        return ResponseUtil.ok();
    }
}
