package org.linlinjava.litemall.wx.web;

import java.util.HashMap;
import java.util.Map;

import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallCashord;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallCashordService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/wx/cashord")
@Validated
public class WxCashordController {
	@Autowired
	private LitemallCashordService cashordService;

	@Autowired
	private LitemallUserService userService;
	
	/**
	 * 添加提现记录并修改用户余额
	 * @param cashord
	 * @param balance
	 * @return
	 */
	@PostMapping("add")
	public Object insert(@RequestBody LitemallCashord cashord) {
		Map<Object, Object> data = new HashMap<Object, Object>();
		LitemallUser user = userService.getUser(cashord.getUserId());
		int insert = cashordService.insert(cashord);
		if (insert > 0) { // 添加提现记录成功
			user.setId(cashord.getUserId());
			user.setBalance(user.getBalance() - Float.parseFloat(cashord.getMoney()));
			int update = userService.updateById(user);
			if (update > 0) { // 修改用户余额成功
				data.put("insert", true);
			} else { // 修改用户余额失败
				data.put("insert", false);
			}
		} else { // 添加提现记录失败
			data.put("insert", false);
		}
		return ResponseUtil.ok(data);
	}
}
