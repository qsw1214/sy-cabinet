package org.linlinjava.litemall.admin.web;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.service.AdminOrderService;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallAd;
import org.linlinjava.litemall.db.domain.LitemallMainCabinet;
import org.linlinjava.litemall.db.service.LitemallMainCabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/maincabine")
@Validated
public class AdminMainCabinetController {
	 private final Log logger = LogFactory.getLog(AdminMainCabinetController.class);
	 
	  	@Autowired
	    private AdminOrderService adminOrderService;
	  	@Autowired
	  	private LitemallMainCabinetService mainCabinetService;
	  	
	  	/**
	  	 * 分页条件查询
	  	 * @param mcnum
	  	 * @param region
	  	 * @param page
	  	 * @param limit
	  	 * @return
	  	 */
	    @RequiresPermissions("admin:maincabine:list")
	    @RequiresPermissionsDesc(menu={"柜子管理" , "主柜管理"}, button="查询")
	    @GetMapping("/list")
	    public Object list(String mcnum,String region,
	                       @RequestParam(defaultValue = "1") Integer page,
	                       @RequestParam(defaultValue = "10") Integer limit) {
	        return  adminOrderService.list(mcnum,region, page, limit);
	    }
	    
	    /**
	     * 添加
	     * @param maincabinet
	     * @return
	     */
	    @RequiresPermissions("admin:maincabine:create")
	    @RequiresPermissionsDesc(menu={"柜子管理" , "主柜管理"}, button="添加")
	    @PostMapping("/create")
	    public Object create(@RequestBody LitemallMainCabinet maincabinet) {
	        Object error = validate(maincabinet);
	        if (error != null) {
	            return error;
	        }
	        mainCabinetService.add(maincabinet);
	        return ResponseUtil.ok(maincabinet);
	    }
	    /**
	     * 修改柜子
	     * @param maincabinet
	     * @return
	     */
	    @RequiresPermissions("admin:maincabine:update")
	    @RequiresPermissionsDesc(menu={"柜子管理" , "主柜管理"}, button="编辑")
	    @PostMapping("/update")
	    public Object update(@RequestBody LitemallMainCabinet maincabinet) {
	        Object error = validate(maincabinet);
	        if (error != null) {
	            return error;
	        }
	        if (mainCabinetService.update(maincabinet) == 0) {
	            return ResponseUtil.updatedDataFailed();
	        }

	        return ResponseUtil.ok(maincabinet);
	    }
	    
	    /**
	     * 停用柜子
	     * @param maincabinet
	     * @return
	     */
	    @RequiresPermissions("admin:maincabine:delete")
	    @RequiresPermissionsDesc(menu={"柜子管理" , "主柜管理"}, button="删除")
	    @PostMapping("/delete")
	    public Object delete(@RequestBody LitemallMainCabinet maincabinet) {
	        Integer id = maincabinet.getId();
	        if (id == null) {
	            return ResponseUtil.badArgument();
	        }
	        mainCabinetService.deleteById(id);
	        return ResponseUtil.ok();
	    }
	    
	    /**
	     * 字段检查
	     * @param maincabinet
	     * @return
	     */
	    private Object validate(LitemallMainCabinet maincabinet) {
	        String mainCabNum = maincabinet.getMainCabNum();
	        if (StringUtils.isEmpty(mainCabNum)) {
	            return ResponseUtil.badArgument();
	        }
	        String region = maincabinet.getRegion();
	        if (StringUtils.isEmpty(region)) {
	            return ResponseUtil.badArgument();
	        }
	        String standard = maincabinet.getStandard();
	        if (StringUtils.isEmpty(standard)) {
	            return ResponseUtil.badArgument();
	        }
	        return null;
	    }
}
