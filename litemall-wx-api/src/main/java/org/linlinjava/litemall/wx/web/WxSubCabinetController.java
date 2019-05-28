package org.linlinjava.litemall.wx.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.imageio.plugins.common.I18N;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallMainCabinet;
import org.linlinjava.litemall.db.domain.LitemallStandards;
import org.linlinjava.litemall.db.domain.LitemallSubCabinet;
import org.linlinjava.litemall.db.domain.LitemallUsageRecord;
import org.linlinjava.litemall.db.service.LitemallMainCabinetService;
import org.linlinjava.litemall.db.service.LitemallStandardsService;
import org.linlinjava.litemall.db.service.LitemallSubCabinetService;
import org.linlinjava.litemall.db.service.LitemallUsageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

/**
 * 查询子柜
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/wx/subcabinet")
@Validated
public class WxSubCabinetController {

	@Autowired
	private LitemallSubCabinetService subCabinetService;
	@Autowired
	private LitemallStandardsService standardsService;
	@Autowired
	private LitemallMainCabinetService mainCabinetService;
	@Autowired
	private LitemallUsageRecordService usageRecordService;

	/**
	 * 查询子柜信息 ,以及相关联的订单、规格信息
	 * @param recordId	订单Id
	 * @return
	 */
	@GetMapping("queryInfo")
	public Object querySubStanMain(Integer recordId){
		//1、订单信息
		LitemallUsageRecord usageRecord = usageRecordService.calculateCost(recordId);
		System.out.println(usageRecord);
		if (StringUtils.isEmpty(usageRecord)) {
			return ResponseUtil.badArgument();
		}
		Integer subCabId = usageRecord.getSubCabId();
        //2、子柜信息
		LitemallSubCabinet subCabinet = subCabinetService.selectByPrimaryKey(subCabId);
		System.out.println(subCabinet);
		if(StringUtils.isEmpty(subCabinet)){
			return ResponseUtil.badArgument();
		}
		//3、规格信息
		LitemallStandards standards = standardsService.selectStanBySub(subCabId);
		System.out.println(standards);
		if(StringUtils.isEmpty(standards)){
			return ResponseUtil.badArgument();
		}
		Integer stanMianId = standards.getStanMianId();
		if(StringUtils.isEmpty(stanMianId)){
			return ResponseUtil.badArgument();
		}
		//4、柜组信息
		LitemallMainCabinet mainCabinet = mainCabinetService.selectByPrimaryKey(stanMianId);
		System.out.println(mainCabinet);
		if(StringUtils.isEmpty(mainCabinet)){
			return ResponseUtil.badArgument();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("sub",subCabinet);
		map.put("stan",standards);
		map.put("main",mainCabinet);
		map.put("usa",usageRecord);
		return ResponseUtil.ok(map);
	}

	/**
	 * 修改子柜信息(状态)
	 * @param id
	 * @param status
	 * @return
	 */
	@GetMapping("updateSub")
	public Object updateSub(Integer id , Integer status){
		LitemallSubCabinet subCabinet = new LitemallSubCabinet();
		subCabinet.setId(id);
		subCabinet.setStatus(status);
		int i = subCabinetService.updateSubCab(subCabinet);
		if(StringUtils.isEmpty(i)){
			return ResponseUtil.badArgument();
		}
		return ResponseUtil.ok();
	}

	/**
	 * 查询单个子柜信息
	 * @param subId
	 * @return
	 */
	@GetMapping("query")
	public Object query(Integer subId){
		if(StringUtils.isEmpty(subId)){
			return ResponseUtil.badArgument();
		}
		LitemallSubCabinet query = subCabinetService.query(subId);
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isEmpty(query)){
			map.put("total", 0);
		}else{
			map.put("total", 1);
		}
		map.put("items",query);
		return ResponseUtil.ok(map);
	}



}
