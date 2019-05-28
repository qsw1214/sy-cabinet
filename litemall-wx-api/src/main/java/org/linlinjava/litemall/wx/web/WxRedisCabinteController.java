package org.linlinjava.litemall.wx.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallSubCabinet;
import org.linlinjava.litemall.db.service.LitemallSubCabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 将子柜详情存放到redis中
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/wx/redisca")
@Validated
public class WxRedisCabinteController {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private LitemallSubCabinetService subCabinetService;

	/**
	 * 查询该箱子有没有被人占用
	 * 
	 * @param boxid
	 * @param userid
	 * @return
	 */
	@GetMapping("getredis")
	public Object getredis(String boxid, String userid) {
		String value = stringRedisTemplate.opsForValue().get(boxid);
		Map<Object, Object> data = new HashMap<Object, Object>();
		if (value == null) {
			LitemallSubCabinet subcabinet = subCabinetService.selectByPrimaryKey(Integer.parseInt(boxid));
			if (subcabinet.getStatus() == 0) {
				stringRedisTemplate.opsForValue().set(boxid, userid, 60 * 5, TimeUnit.SECONDS);
				data.put("recode", "1000");// 当前柜子未被占用
				return ResponseUtil.ok(data);
			} else {
				data.put("recode", "1002");// 当前柜子被其他用户占用
				return ResponseUtil.ok(data);
			}
		} else if (value.equals(userid)) {
			LitemallSubCabinet subcabinet = subCabinetService.selectByPrimaryKey(Integer.parseInt(boxid));
			if (subcabinet.getStatus() == 0) {
				data.put("recode", "1001");// 就是当前用户占用的柜子
			} else {
				data.put("recode", "1003");// 已经提交订到
			}
			return ResponseUtil.ok(data);
		} else {
			data.put("recode", "1002");// 当前柜子被其他用户占用
			return ResponseUtil.ok(data);
		}

	}

	/**
	 * 用户返回直接删除占用的箱子
	 * 
	 * @param boxid
	 * @param userid
	 * @return
	 */
	@GetMapping("delredis")
	public Object delredis(String boxid) {
		Map<Object, Object> data = new HashMap<Object, Object>();
		if (stringRedisTemplate.delete(boxid)) {
			data.put("recode", "1000");// 删除柜子占用成功
		} else {
			data.put("recode", "1001");// 删除柜子占用不成功
		}
		return ResponseUtil.ok(data);
	}
}
