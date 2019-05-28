package org.linlinjava.litemall.admin.web;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.service.AdminOrderService;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallStandards;
import org.linlinjava.litemall.db.service.LitemallStandardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/standard")
@Validated
public class AdminStandardController {
    @Autowired
    private AdminOrderService adminOrderService;
    @Autowired
    private LitemallStandardsService standardsService;

    @RequiresPermissions("admin:standard:list")
    @RequiresPermissionsDesc(menu={"柜子管理", "规格管理"}, button="查找")
    @GetMapping("/list")
    public Object listStan(String stanName,Integer stanMianId,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit){
        return adminOrderService.listStan(stanName,stanMianId, page, limit);
    }

    @RequiresPermissions("admin:standard:create")
    @RequiresPermissionsDesc(menu={"柜子管理", "规格管理"}, button="添加")
    @PostMapping("/create")
    public Object create(@RequestBody LitemallStandards standards){
        //校验格式
        Object error = validate(standards);
        if(error != null){
            return error;
        }
        //添加信息
        standardsService.add(standards);
        return ResponseUtil.ok(standards);
    }

    /**
     * 修改柜子信息
     *
     */
    @RequiresPermissions("admin:standard:update")
    @RequiresPermissionsDesc(menu={"柜子管理", "规格管理"}, button="编辑")
    @PostMapping("/update")
    public Object update(@RequestBody LitemallStandards standards){
        //校验格式
        Object error = validate(standards);
        if(error != null){
            return error;
        }
        //判断返回状态
        if(standardsService.update(standards) == 0){
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(standards);
    }

    /**
     * 删除规格（逻辑删除）
     *
     */
    @RequiresPermissions("admin:standard:delete")
    @RequiresPermissionsDesc(menu={"柜子管理", "规格管理"}, button="删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody LitemallStandards standards){
        Integer id = standards.getId();
        if(id == null){
            return ResponseUtil.badArgument();
        }
        standardsService.deleteById(id);
        return ResponseUtil.ok();
    }

    /**
     * 检查字段格式
     *
     */
    public Object validate(LitemallStandards standards){
        //子柜编号
        if(StringUtils.isEmpty(standards.getStanName())){
            return ResponseUtil.badArgument();
        }
        //使用状态
        if(StringUtils.isEmpty(standards.getStanMoney())){
            return ResponseUtil.badArgument();
        }
        //规格
        if(StringUtils.isEmpty(standards.getStanMianId())){
            return ResponseUtil.badArgument();
        }
        return null;
    }
    
    
    /**
     * 谭糠
     * 根据柜组ID查询下面正常规则
     * @param mainId
     * @return
     */
    @RequiresPermissions("admin:standard:bymain")
    @RequiresPermissionsDesc(menu={"柜子管理", "子柜管理"}, button="查询规格")
    @GetMapping("/bymain")
    public Object getSatnByMain(Integer mainId){
        if(mainId == null){
            return ResponseUtil.badArgument();
        }
        //存入集合
        Map<String, Object> data = new HashMap<>();
        data.put("items", standardsService.getSatnByMain(mainId));
        return ResponseUtil.ok(data);
    }
}
