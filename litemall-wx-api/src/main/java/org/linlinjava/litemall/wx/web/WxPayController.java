package org.linlinjava.litemall.wx.web;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallUsageRecord;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.LitemallWxOrder;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallUsageRecordService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.service.LitemallWxOrderService;
import org.linlinjava.litemall.wx.service.WxRefundOrderService;
import org.linlinjava.litemall.wx.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wx/wxpay")
@Validated
public class WxPayController {
	@Autowired
	private WxPayService wxPayService;
	@Autowired
	private LitemallOrderService orderService;
	@Autowired
	private LitemallUserService userService;
	@Autowired
	private LitemallUsageRecordService usageRecordService;
	@Autowired
	private LitemallWxOrderService wxOrderService;
	@Autowired
	private WxRefundOrderService wxRefundOrderService;


	@GetMapping("pay")
	public Object list(Integer userId, Integer recordId,float wxpric, Integer isPay,HttpServletRequest request) {
		LitemallUser user = userService.findById(userId);
		LitemallUsageRecord record = usageRecordService.queryUsageRecord(recordId);
		String recordStr = String.format("%04d", recordId);
		String orderSn = orderService.generateOrderSn(userId);
		orderSn = orderSn + recordStr;
		String openid = user.getWeixinOpenid();
		WxPayMpOrderResult result = null;
		Integer orderId = null;
		if(wxpric == 0){
			return ResponseUtil.badArgument();
		}
		try {
			WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
			LitemallWxOrder wxOrder = new LitemallWxOrder();
			orderRequest.setOutTradeNo(orderSn);
			orderRequest.setOpenid(openid);
			if(isPay == 0) {
				orderRequest.setBody("储物柜押金:" + wxpric + "元");
				wxOrder.setOrderStyle(0);
			}else{
				orderRequest.setBody("柜子编号:"+record.getSubCabNum());
				wxOrder.setOrderStyle(1);
			}
			// 元转成分
			int fee = 0;
			BigDecimal actualPrice = new BigDecimal(wxpric);
			fee = actualPrice.multiply(new BigDecimal(100)).intValue();
			orderRequest.setTotalFee(fee);
			orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));
			result = wxPayService.createOrder(orderRequest);

			// 创建微信支付订单
			wxOrder.setOutTradeNo(orderSn);
			wxOrder.setTotalFee(fee);
			wxOrder.setMobile(user.getMobile());
			wxOrder.setOrderStatus(0);
			wxOrder.setUserId(user.getId());
			wxOrderService.create(wxOrder);
			orderId = wxOrder.getOrderId();
		} catch (WxPayException e) {
			System.out.println(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("result",result);
		map.put("orderId",orderId);
		return ResponseUtil.ok(map);
	}

	/**
	 * 微信退款
	 * @return
	 */
	@GetMapping("refund")
	public Object refund(Integer userId, Integer orderStatus,float refundFee,
						 @Sort @RequestParam(defaultValue = "add_time") String sort,
						 @Order @RequestParam(defaultValue = "desc") String order){
		return wxRefundOrderService.refund(userId,orderStatus,refundFee,sort,order);
	}

	/**
	 * 修改微信支付订单
	 * @param wxOrder
	 * @return
	 */
	@PostMapping("updateOrder")
	public Object updateOrder(@RequestBody LitemallWxOrder wxOrder){
		if(StringUtils.isEmpty(wxOrder.getOrderId())){
			return ResponseUtil.badArgument();
		}
		if(StringUtils.isEmpty(wxOrder.getOrderStatus())){
			return ResponseUtil.badArgument();
		}
		return wxOrderService.update(wxOrder);
	}

}
