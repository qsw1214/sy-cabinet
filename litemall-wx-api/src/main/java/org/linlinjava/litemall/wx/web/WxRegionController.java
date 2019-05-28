package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallRegion;
import org.linlinjava.litemall.db.domain.LitemallRegionInfo;
import org.linlinjava.litemall.db.service.LitemallRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 区域服务
 */
@RestController
@RequestMapping("/wx/region")
@Validated
public class WxRegionController {
    private final Log logger = LogFactory.getLog(WxRegionController.class);

    @Autowired
    private LitemallRegionService regionService;

    /**
     * 区域数据
     * <p>
     * 根据父区域ID，返回子区域数据。
     * 如果父区域ID是0，则返回省级区域数据；
     *
     * @param pid 父区域ID
     * @return 区域数据
     */
    @GetMapping("list")
    public Object list(@NotNull Integer pid) {
        List<LitemallRegion> regionList = regionService.queryByPid(pid);
        return ResponseUtil.ok(regionList);
    }

    @GetMapping("get_info")
    public Object getInfoByCode(@NotNull Integer code){
        List<LitemallRegionInfo> regionList = regionService.selectInfoByCode(code);
        return ResponseUtil.ok(regionList);
    }

    @GetMapping("get_pid")
    public Object getPid(@NotNull Integer cid){
        return regionService.selectByCid(cid);
    }

    @GetMapping("get_id")
    public Object getIdByCode(@NotNull Integer code){
        return regionService.selectIdByCode(code);
    }

    @GetMapping("get_name")
    public Object getNameById(@NotNull Integer regionId){
        return regionService.selectNameById(regionId);
    }
}