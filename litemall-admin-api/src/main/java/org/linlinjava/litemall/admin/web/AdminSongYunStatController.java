package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.util.StatVo;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.service.SongYunStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/systat")
@Validated
public class AdminSongYunStatController {
	private final Log logger = LogFactory.getLog(AdminSongYunStatController.class);
	@Autowired
    private SongYunStatService SyStatService;
	
	@SuppressWarnings("rawtypes")
	@RequiresPermissions("admin:ad:list")
    @RequiresPermissionsDesc(menu={"柜子管理" , "资金流水统计"}, button="查询")
    @GetMapping("/profitstati")
    public Object profitstati(String ymd,String date,String year,String month,String day) {
		Map<String, Object> val = new HashMap<>();
		val.put("ymd", ymd);
		val.put("date", date);
		val.put("year", year);
		val.put("month", month);
		val.put("day", day);
		List<Map> rows  = SyStatService.ProfitStati(val);
        String[] columns = new String[]{"day", "amount"};
        StatVo statVo = new StatVo();
        statVo.setColumns(columns);
        statVo.setRows(rows);
        Map<String, Object> data = new HashMap<>();
        data.put("total", SyStatService.totalProfit());
        data.put("statVo", statVo);
        return ResponseUtil.ok(data);
    }
}
