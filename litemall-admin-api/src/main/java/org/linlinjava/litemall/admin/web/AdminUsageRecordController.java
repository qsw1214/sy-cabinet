package org.linlinjava.litemall.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallUsageRecord;
import org.linlinjava.litemall.db.service.LitemallUsageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/admin/usagerecord")
@Validated
public class AdminUsageRecordController {
	private final Log logger = LogFactory.getLog(AdminMainCabinetController.class);
	
	@Autowired
  	private LitemallUsageRecordService usagerecordService;
	
	/**
  	 * 分页条件查询
  	 * @param page
  	 * @param limit
  	 * @return
  	 */
    @RequiresPermissions("admin:usagerecord:list")
    @RequiresPermissionsDesc(menu={"柜子管理" , "使用记录"}, button="查询")
    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "-1") Integer status,
					   @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
		LitemallUsageRecord record = new LitemallUsageRecord();
		record.setStatus(status);
		List<LitemallUsageRecord> UsageRecord = usagerecordService.querySelective(record,page,limit);
		long total = PageInfo.of(UsageRecord).getTotal();
		Map<String, Object> data = new HashMap<>();
		data.put("total", total);
		data.put("items", UsageRecord);
		return ResponseUtil.ok(data);
    }

	/**
	 * 添加使用记录
	 */
	@PostMapping("/addUsa")
	public Object add(@RequestBody LitemallUsageRecord usageRecord){
		int insert = usagerecordService.insert(usageRecord);
		if(insert != 1){
			return ResponseUtil.badArgument();
		}
		return ResponseUtil.ok();
	}

}
