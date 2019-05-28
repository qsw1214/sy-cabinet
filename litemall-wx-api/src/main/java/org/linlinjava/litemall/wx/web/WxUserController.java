package org.linlinjava.litemall.wx.web;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallSubCabinetService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务
 */
@RestController
@RequestMapping("/wx/user")
@Validated
public class WxUserController {
    private final Log logger = LogFactory.getLog(WxUserController.class);

    @Autowired
    private LitemallOrderService orderService;

    @Autowired
    private LitemallUserService userService;

    /**
     * 用户个人页面数据
     * <p>
     * 目前是用户订单统计信息
     *
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("index")
    public Object list(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        Map<Object, Object> data = new HashMap<Object, Object>();
        data.put("order", orderService.orderInfo(userId));
        return ResponseUtil.ok(data);
    }

    /**
     * 查询用户信息 By mobile(查询余额)
     * @param mobile
     * @return
     */
    @GetMapping("queryUser")
    public Object queryUserInfo(String nickname, String mobile,String sort, String order,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size){
        if(StringUtils.isEmpty(mobile)){
            return ResponseUtil.badArgument();
        }
        //查询用户信息
        List<LitemallUser> userList = userService.querySelective(nickname, mobile, page, size, sort, order);
        Map<Object, Object> data = new HashMap<Object, Object>();
        data.put("user", userList.get(0));
        return ResponseUtil.ok(data);
    }

    @PostMapping("updateUser")
    public Object updateUser(@RequestBody LitemallUser user){
        int i = userService.updateById(user);
        if(i !=1){
            return ResponseUtil.badArgument();
        }
        return ResponseUtil.ok();
    }

    @GetMapping("updateBalance")
    public Object updateBalance(Integer userId,Float balance,String bool){
        int update ;
        if(StringUtils.isEmpty(userId)){
            return ResponseUtil.badArgument();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id",userId);
        map.put("balance",balance);
        if(bool.equals("true")){
            // true 退款
            update = userService.upBalance(map);
        }else{
            // false 扣款
            update = userService.buckleBalance(map);
        }
        if(StringUtils.isEmpty(update) && update != 1){
            return ResponseUtil.badArgument();
        }
        logger.info("修改余额返回状态:"+ update);
        return ResponseUtil.ok();
    }

    /**
     * 余额提现
     * @return
     */
    @GetMapping("withdraw")
    public Object withdrawBalance(Float balance){




        return ResponseUtil.ok();
    }
}