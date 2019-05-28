package org.linlinjava.litemall.admin.web;

import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.service.AdminOrderService;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallCapitalRecord;
import org.linlinjava.litemall.db.domain.LitemallDeposit;
import org.linlinjava.litemall.db.domain.LitemallMainCabinet;
import org.linlinjava.litemall.db.domain.LitemallSubCabinet;
import org.linlinjava.litemall.db.domain.LitemallUsageRecord;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallCapitalRecordService;
import org.linlinjava.litemall.db.service.LitemallDepositService;
import org.linlinjava.litemall.db.service.LitemallMainCabinetService;
import org.linlinjava.litemall.db.service.LitemallSubCabinetService;
import org.linlinjava.litemall.db.service.LitemallUsageRecordService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/subcabinet")
@Validated
public class AdminSubCabinetController {
	// 日志
	private final Log logger = LogFactory.getLog(AdminSubCabinetController.class);

	@Autowired
	private LitemallSubCabinetService subCabinetService;
	@Autowired
	private AdminOrderService adminOrderService;
	@Autowired
	private LitemallMainCabinetService mainCabinetService;
	@Autowired
	private LitemallUsageRecordService recordService;
	@Autowired
	private LitemallUserService userService;
	@Resource
	private LitemallDepositService depositService;
	@Resource
	private LitemallCapitalRecordService caprecService;// 添加交易记录表

	/**
	 * 分页条件查询(子柜信息)
	 *
	 */
	@RequiresPermissions("admin:subcabinet:list")
	@RequiresPermissionsDesc(menu = { "柜子管理", "子柜管理" }, button = "查询")
	@GetMapping("/list")
	public Object listSubCab(Integer mainId, String mainCabNum, String subCabNum, Integer status, Integer standardId,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
		return adminOrderService.listSub(mainId, mainCabNum, subCabNum, status, standardId, page, limit);
	}

	/**
	 * 添加子柜信息
	 *
	 */
	@RequiresPermissions("admin:subcabinet:create")
	@RequiresPermissionsDesc(menu = { "柜子管理", "子柜管理" }, button = "添加")
	@PostMapping("/create")
	public Object createSubCabinet(@RequestBody LitemallSubCabinet subCabinet) {
		// 校验格式
		Object error = validate(subCabinet);
		if (error != null) {
			return error;
		}
		// 添加信息
		subCabinetService.addSubCabinet(subCabinet);
		return ResponseUtil.ok(subCabinet);
	}

	/**
	 * 清柜（首先查询出需要结束的订单，算出需要缴纳的金额，扣除用户金额，用户余额充足就退回押金，并将订单修改成结束状态，反之则只是修改订单状态）
	 * 
	 * @param subId
	 * @return
	 */
	@RequiresPermissions("admin:subcabinet:clearsark")
	@RequiresPermissionsDesc(menu = { "柜子管理", "子柜管理" }, button = "清柜")
	@GetMapping("/clearsark")
	public Object clearsark(Integer subId) {
		Map<Object, Object> data = new HashMap<Object, Object>();
		LitemallSubCabinet subCabinet = subCabinetService.selectByPrimaryKey(subId);
		Integer totalMoney; // 与缴纳金额
		LitemallUsageRecord usa = recordService.getUsaBySubId(subCabinet.getId()); // 查询用户使用记录
		Date endTime = new Date();
		Integer Minute = getGapCount(usa.getStartTime(), endTime); // 得到分钟数
		Integer Hour = (int) Math.floor(Minute / 60); // 得到小时
		if (Hour > 24) { // 如果大于24小时就去算出天数
			Integer day = (int) Math.floor(Hour / 24); // 算出天数
			Integer SurplusHour = Math.floorMod(Hour, 24); // 取模算出剩余小时数
			if (SurplusHour > usa.getUsaCapp()) { // 如果剩下小时数大于封顶时间就直接加一天
				day += 1;
				// 单价*封顶时间* 天数=天数总额
				totalMoney = day * (usa.getUsaCapp() * Integer.parseInt(usa.getUsaMoney()));
				usa.setActualTime(day + "天");
			} else {
				Integer SurplusMinute = Math.floorMod(Minute, 60); // 取模算出剩余分钟数
				if (SurplusMinute > usa.getUsaBeyond()) { // 如果剩余分钟数大于超出分钟数就直接加一小时
					SurplusHour += 1;
				}
				// 单价 * 封顶时间 *天数= 天数总额
				totalMoney = day * (usa.getUsaCapp() * Integer.parseInt(usa.getUsaMoney()));
				totalMoney += SurplusHour * Integer.parseInt(usa.getUsaMoney());
				usa.setActualTime(day + "天" + SurplusHour + "小时");
			}
		} else { // 如果天数小于24小时就根据具体小时数算出总额
			if (Hour > usa.getUsaCapp()) {// 如果小时数大于每天封顶时间 就直接算做一天
				totalMoney = usa.getUsaCapp() * Integer.parseInt(usa.getUsaMoney());
				usa.setActualTime("1天");
			} else { // 小于封顶数就算具体小时数
				Integer SurplusMinute = Math.floorMod(Minute, 60); // 取模算出剩余分钟数
				if (SurplusMinute > usa.getUsaBeyond()) { // 如果剩余分钟数大于超出分钟数就直接加一小时
					Hour += 1;
				}
				totalMoney = Hour * Integer.parseInt(usa.getUsaMoney());
				usa.setActualTime(Hour + "小时");
			}
		}
		usa.setActualAmount((float) totalMoney); // 实际金额
		float PayCost = usa.getActualAmount() - usa.getPreAmount(); // 算出需要在用户账户扣除的金额
		Map<String, Object> map = new HashMap<>();
		map.put("balance", PayCost);
		map.put("id", usa.getUserId());
		int upBalance = userService.upBalance(map);// 扣除金额
		LitemallCapitalRecord CapitalRecord = new LitemallCapitalRecord();
		if (upBalance > 0) {
			LitemallUser user = userService.getUser(usa.getUserId());
			if (user.getBalance() > 0) { // 如果用户余额大于0 就返还押金
				LitemallDeposit deposit = depositService.getDsposit(usa.getId());
				if (deposit != null) {
					map.put("balance", deposit.getDepoMoney());
					Integer balanceType = userService.upBalance(map);
					if (balanceType > 0) {
						deposit.setDepoStatus(2);
						depositService.update(deposit);
						CapitalRecord.setCapDealid(usa.getId());
						CapitalRecord.setCapUserid(usa.getUserId());
						CapitalRecord.setCapTitle("押金返还");
						CapitalRecord.setCapDatetime(endTime);
						CapitalRecord.setCapMoney("+" + deposit.getDepoMoney());
						CapitalRecord.setCapStatus(1);
						CapitalRecord.setCapDeal("litemall_usage_record");
						caprecService.add(CapitalRecord);
					}
				}
			}
			int type = recordService.BagTaking(usa.getId(), usa.getActualTime(), usa.getActualAmount(), 1, endTime);
			if (type > 0) {
				subCabinet.setStatus(0);
				subCabinetService.updateSubCab(subCabinet); // 修改柜子状态
				if (PayCost != 0) {
					CapitalRecord.setCapDealid(usa.getId());
					CapitalRecord.setCapUserid(usa.getUserId());
					CapitalRecord.setCapTitle("柜子使用返还金额");
					CapitalRecord.setCapDatetime(endTime);
					if (PayCost > 0) {
						CapitalRecord.setCapMoney("+" + PayCost);
					} else {
						CapitalRecord.setCapMoney(PayCost + "");
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

	/**
	 * 获取两个日期之间的间隔分钟数
	 */
	public static Integer getGapCount(Date startDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);
		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);
		return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60));
	}

	/**
	 * 修改柜子信息
	 *
	 */
	@RequiresPermissions("admin:subcabinet:update")
	@RequiresPermissionsDesc(menu = { "柜子管理", "子柜管理" }, button = "编辑")
	@PostMapping("/update")
	public Object update(@RequestBody LitemallSubCabinet subCabinet) {
		// 校验格式
		Object error = validate(subCabinet);
		if (error != null) {
			return error;
		}
		// 判断返回状态
		if (subCabinetService.updateSubCab(subCabinet) == 0) {
			return ResponseUtil.updatedDataFailed();
		}
		return ResponseUtil.ok(subCabinet);
	}

	/**
	 * 禁用子柜
	 *
	 */
	@RequiresPermissions("admin:subcabinet:delete")
	@RequiresPermissionsDesc(menu = { "柜子管理", "子柜管理" }, button = "删除")
	@PostMapping("/delete")
	public Object delete(@RequestBody LitemallSubCabinet subCabinet) {
		Integer id = subCabinet.getId();
		if (id == null) {
			return ResponseUtil.badArgument();
		}
		subCabinetService.delectSubCabById(id);
		return ResponseUtil.ok();
	}

	/**
	 * 查询所有子柜规格
	 * 
	 * @return
	 */
	// @RequiresPermissions("admin:subcabinet:listStandard")
	// @RequiresPermissionsDesc(menu={"柜子管理", "子柜管理"}, button="查询规格")
	// @GetMapping("/listStandard")
	// public Object listStandard(LitemallSubCabinet subCabinet){
	// //查询子柜规格
	// List<LitemallSubCabinet> listStandard =
	// subCabinetService.getAllStandard(subCabinet);
	// long total = PageInfo.of(listStandard).getTotal();
	// //存入集合
	// Map<String, Object> data = new HashMap<>();
	// data.put("total", total);
	// data.put("items", listStandard);
	// return ResponseUtil.ok(data);
	// }

	/**
	 * 查询所有柜组编码
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:subcabinet:listAllNum")
	@RequiresPermissionsDesc(menu = { "柜子管理", "子柜管理" }, button = "查询柜组编码")
	@GetMapping("/listAllNum")
	public Object listAllNum(LitemallMainCabinet mainCabinet) {
		// 查询柜组编号
		List<LitemallMainCabinet> listAllNum = mainCabinetService.listAllNum(mainCabinet);
		long total = PageInfo.of(listAllNum).getTotal();
		// 存入集合
		Map<String, Object> data = new HashMap<>();
		data.put("total", total);
		data.put("items", listAllNum);
		return ResponseUtil.ok(data);
	}

	/**
	 * 检查字段格式
	 *
	 */
	public Object validate(LitemallSubCabinet subCabinet) {
		// 子柜编号
		if (StringUtils.isEmpty(subCabinet.getSubCabNum())) {
			return ResponseUtil.badArgument();
		}
		// 使用状态
		if (StringUtils.isEmpty(subCabinet.getStatus())) {
			return ResponseUtil.badArgument();
		}
		// 规格
		if (StringUtils.isEmpty(subCabinet.getStanId())) {
			return ResponseUtil.badArgument();
		}
		return null;
	}
}
