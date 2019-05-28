package org.linlinjava.litemall.wx.web;

import net.sf.json.JSONObject;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallCapitalRecord;
import org.linlinjava.litemall.db.domain.LitemallDeposit;
import org.linlinjava.litemall.db.service.LitemallCapitalRecordService;
import org.linlinjava.litemall.db.service.LitemallUsageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/capital")
@Validated
public class WxCapitalRecordController {
	@Autowired
	private LitemallCapitalRecordService CapitalService;

	@Autowired
	private LitemallUsageRecordService usageRecordService;

	@Autowired
	private WxDepositController depositController;

	/**
	 * 获取用户个人信息
	 *
	 * @param userId 用户ID
	 * @param
	 * @return 查询结果
	 */
	@GetMapping("getcap")
	public Object getCap(String month, String year, Integer userId,Integer page,Integer size) {
		int currIndex = (page - 1) * size;
		Map<Object, Object> result = new HashMap<Object, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("month", month);
		map.put("year", year);
		map.put("userId", userId);
		map.put("currIndex",currIndex);
		map.put("pageSize",size);
		List<LitemallCapitalRecord> Cap = CapitalService.AccordingToTime(map);
		int allTotal = CapitalService.queryAllTotal(map);
		result.put("items", Cap);
		result.put("allTotal",allTotal);
		return ResponseUtil.ok(result);
	}

	/**
	 * 添加用户交易明细
	 *
	 * @param capRecord
	 * @return
	 * @author 刘宇
	 */
	@PostMapping("add")
	public Object add(@RequestBody LitemallCapitalRecord capRecord) {
		Map<Integer, String> capitalMap = new HashMap<>();
		capitalMap.put(1, "智能储物柜押金");
		capitalMap.put(2, "智能储物柜退款");
		capitalMap.put(3, "智能储物柜支付");
		// 判断交易类型
		if(capRecord.getCapType().equals(2)){
			capRecord.setCapMoney("+"+ capRecord.getCapMoney());
		}else{
			capRecord.setCapMoney("-"+ capRecord.getCapMoney());
		}
		String capTitle = capitalMap.get(capRecord.getCapType());
		Date date = usageRecordService.localDateTimeDate(LocalDateTime.now());
		capRecord.setCapDatetime(date);
		capRecord.setCapTitle(capTitle);
		capRecord.setCapStatus(1);
		capRecord.setCapDeal("litemall_usage_record");
		int add = CapitalService.add(capRecord);
		if (add == 0) {
			return ResponseUtil.badArgument();
		}
		return ResponseUtil.ok();
	}

	/**
	 * 用户取包---添加交易明细
	 * @param capRecord
	 * @return
	 */
	@PostMapping("addCapRec")
	public Object addCapRec(@RequestBody LitemallCapitalRecord capRecord){
		/**
		 * 1、添加交易明细
		 * 2、退还押金
		 * 3、添加押金明细
		 * 4、修改订单状态、子柜状态
		 */

		//1、添加交易明细(退款至余额)
		Map<Integer, String> capitalMap = new HashMap<>();
		capitalMap.put(1, "智能储物柜押金");
		capitalMap.put(2, "智能储物柜退款");
		capitalMap.put(3, "智能储物柜支付");
		// 判断交易类型
		if(capRecord.getCapType().equals(2)){
			capRecord.setCapMoney("+"+ capRecord.getCapMoney());
		}else{
			capRecord.setCapMoney("-"+ capRecord.getCapMoney());
		}
		String capTitle = capitalMap.get(capRecord.getCapType());
		Date date = usageRecordService.localDateTimeDate(LocalDateTime.now());
		capRecord.setCapDatetime(date);
		capRecord.setCapTitle(capTitle);
		capRecord.setCapStatus(1);
		capRecord.setCapDeal("litemall_usage_record");
		int add = CapitalService.add(capRecord);
		if (add == 0) {
			return ResponseUtil.badArgument();
		}

		//2、退还押金
		LitemallDeposit deposit = new LitemallDeposit();
		deposit.setDepoUserId(capRecord.getCapUserid());
		deposit.setDepoRecordId(capRecord.getCapDealid());
		return depositController.updateDeposit(deposit);
	}
}
