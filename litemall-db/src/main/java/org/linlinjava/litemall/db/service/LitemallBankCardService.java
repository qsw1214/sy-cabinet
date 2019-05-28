package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallBankCardMapper;
import org.linlinjava.litemall.db.domain.LitemallBankCard;
import org.linlinjava.litemall.db.domain.LitemallBankCardExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class LitemallBankCardService {

    @Resource
    private LitemallBankCardMapper bankCardMapper;

    /**
     * 查询银行卡信息以及用户余额
     */
    public List<LitemallBankCard> queryBank(String userPhone){
        LitemallBankCardExample example = new LitemallBankCardExample();
        LitemallBankCardExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(userPhone)){
            criteria.andUserPhoneEqualTo(userPhone);
        }
        List<LitemallBankCard> bankCards = bankCardMapper.selectByExample(example);
        return bankCards;
    }
    public List<LitemallBankCard> queryBank(Integer userId){
    	 LitemallBankCardExample example = new LitemallBankCardExample();
         LitemallBankCardExample.Criteria criteria = example.createCriteria();
         criteria.andUserIdEqualTo(userId);
         criteria.andDeletedEqualTo(false);
         List<LitemallBankCard> bankCards = bankCardMapper.selectByExample(example);
         return bankCards;
    }
    
    /**
     * 新增银行卡
     * @param bankCards
     * @return
     */
    public int add(LitemallBankCard bankCards){
    	bankCards.setAddTime(localDateTimeDate(LocalDateTime.now()));
    	bankCards.setUpdateTime(localDateTimeDate(LocalDateTime.now()));
    	bankCards.setDeleted(false);
    	return bankCardMapper.insert(bankCards);
    }
    public int delete(Integer id){
    	LitemallBankCard bankCard = new LitemallBankCard();
    	bankCard.setId(id);
    	bankCard.setDeleted(true);
    	return bankCardMapper.updateByPrimaryKeySelective(bankCard);
    }
    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public Date localDateTimeDate( LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
       return date;
    }

}
