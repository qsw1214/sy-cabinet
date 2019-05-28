package org.linlinjava.litemall.wx.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallMainCabinet;
import org.linlinjava.litemall.db.service.LitemallMainCabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询主柜
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/wx/maincab")
@Validated
public class WxMainCabinteController {

    @Autowired
    private LitemallMainCabinetService mainCabinetService;


    /**
     * 扫码获取根据主柜ID查询主柜信息再返回子柜集合
     *
     * @param
     * @return retype 1001：柜组已锁定 1002：柜组故障 1003：柜组暂停使用 1004：柜组正常 1000：未找到该柜组
     */
    @GetMapping("subcablist")
    public Object list(String mainCabNum) {
        Map<Object, Object> data = new HashMap<Object, Object>();
        LitemallMainCabinet cabinet = new LitemallMainCabinet();
        cabinet.setMainCabNum(mainCabNum);
        List<LitemallMainCabinet> mainCabinets = mainCabinetService.listAllNum(cabinet);
        if (!StringUtils.isEmpty(mainCabinets)
                && mainCabinets.size() != 0) {
            LitemallMainCabinet mainCabinet = mainCabinets.get(0);
            if (mainCabinet.getDeleted()) {
                data.put("retype", "1001");
            }
            if (mainCabinet.getStatus() == 1) {
                data.put("retype", "1002");
            }
            if (mainCabinet.getStatus() == 2) {
                data.put("retype", "1003");
            }
            if (mainCabinet.getStatus() == 0) {
                data.put("retype", "1004");
                data.put("items", mainCabinet);
            }
        } else {
            data.put("retype", "1000");
        }
        return ResponseUtil.ok(data);
    }

    // 判断是不是数字
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
