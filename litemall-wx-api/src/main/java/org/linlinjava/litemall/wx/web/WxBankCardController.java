package org.linlinjava.litemall.wx.web;

import com.github.pagehelper.PageInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallBankCard;
import org.linlinjava.litemall.db.service.LitemallBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/bankCard")
@Validated
public class WxBankCardController {

    private final Log logger = LogFactory.getLog(WxBrandController.class);

    @Autowired
    private LitemallBankCardService bankCardService;

    /**
     * 查询银行卡信息以及余额信息
     * @param userPhone
     * @return
     */
    @GetMapping("querybank")
    public Object queryBankCard(Integer userId){
        List<LitemallBankCard> bankCards = bankCardService.queryBank(userId);
        long total = PageInfo.of(bankCards).getTotal();
        Map<String, Object> bankMap = new HashMap<>();
        	if(bankCards.size()>0){
        		bankMap.put("banktype",true);
        		bankMap.put("bank",bankCards);
                bankMap.put("total", total);
        	}else{
        		bankMap.put("banktype",false);
        	}
        	
        return ResponseUtil.ok(bankMap);
    }
    
    @PostMapping("add")
    public Object Add(@RequestBody LitemallBankCard bankCards){
        Map<String, Object> bankMap = new HashMap<>();
        int add = bankCardService.add(bankCards);
        	if(add>0){
        		bankMap.put("addcode", true);
        	}else{
        		bankMap.put("addcode", false);
        	}
        return ResponseUtil.ok(bankMap);
    }
    
    @GetMapping("delete")
    public Object delete(Integer id){
        Map<String, Object> bankMap = new HashMap<>();
        int delete = bankCardService.delete(id);
        	if(delete > 0){
        		bankMap.put("delecode", true);
        	}else{
        		bankMap.put("delecode", false);
        	}
        return ResponseUtil.ok(bankMap);
    }
}
