package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.domain.LitemallCashordUnion;
import org.linlinjava.litemall.db.service.LitemallCashordService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/cashord")
@Validated
public class AdminCashordController {
	private final Log logger = LogFactory.getLog(AdminCashordController.class);

	@Autowired
	private LitemallCashordService cashordService;
	@Autowired
	private LitemallUserService userService;

	@RequiresPermissions("admin:cashord:list")
	@RequiresPermissionsDesc(menu = { "柜子管理", "提现管理" }, button = "查询")
	@GetMapping("/list")
	public Object list(String username, String status, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer limit) {
		List<LitemallCashordUnion> casList = cashordService.listcashord(username, status, page, limit);
		long total = cashordService.listcashordCount(username, status);
		Map<String, Object> data = new HashMap<>();
		data.put("total", total);
		data.put("items", casList);
		return ResponseUtil.ok(data);
	}

	@RequiresPermissions("admin:cashord:cashpostal")
	@RequiresPermissionsDesc(menu = { "柜子管理", "提现管理" }, button = "编辑")
	@GetMapping("/cashpostal")
	public Object cashpostal(String remarks, Integer id, Integer userId, Integer status, Double money) {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("balance", money);
		map.put("id", userId);
		if (status == 2) { // 提现失败返还扣除费用
			if (userService.upBalance(map) > 0) {
				Subject currentUser = SecurityUtils.getSubject();
				LitemallAdmin LitemallAdmin = (org.linlinjava.litemall.db.domain.LitemallAdmin) currentUser
						.getPrincipal();
				data.put("items", cashordService.cashpostal(remarks, id, status, LitemallAdmin.getId()));
			} else {
				data.put("items", 0);
			}
		} else {
			Subject currentUser = SecurityUtils.getSubject();
			LitemallAdmin LitemallAdmin = (org.linlinjava.litemall.db.domain.LitemallAdmin) currentUser.getPrincipal();
			data.put("items", cashordService.cashpostal(remarks, id, status, LitemallAdmin.getId()));
		}
		return ResponseUtil.ok(data);
	}

}
