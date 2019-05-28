package org.linlinjava.litemall.db.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.linlinjava.litemall.db.dao.LitemallMainCabinetMapper;
import org.linlinjava.litemall.db.domain.LitemallMainCabinet;
import org.linlinjava.litemall.db.domain.LitemallMainCabinetExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
@Service
public class LitemallMainCabinetService {
	
	  @Resource
	  private  LitemallMainCabinetMapper maincabineMapper;
	/**
	 * 分页查询主柜
	 * @param mcnum
	 * @param page
	 * @param size
	 * @return
	 */
    public List<LitemallMainCabinet> querySelective(String mcnum,String region,Integer page, Integer size) {
    	LitemallMainCabinetExample example = new LitemallMainCabinetExample();
    	LitemallMainCabinetExample.Criteria criteria =  example.createCriteria();
    	if (!StringUtils.isEmpty(mcnum)) {
            criteria.andMainCabNumLike("%" + mcnum + "%");
        }
    	if(!StringUtils.isEmpty(region)){
    		criteria.andRegionLike("%" + region + "%");
    	}
    	criteria.andDeletedEqualTo(false);
        PageHelper.startPage(page, size);
        return maincabineMapper.selectByExample(example);
    }
    
    /**
     * 添加主柜
     * @param maincabinet
     * @return
     */
    public int add(LitemallMainCabinet maincabinet){
    	maincabinet.setAddTime(localDateTimeDate(LocalDateTime.now()));
    	maincabinet.setUpdateTime(localDateTimeDate(LocalDateTime.now()));
    	return maincabineMapper.insertSelective(maincabinet);
    }
    
    /**
     * 修改主柜信息
     * @param maincabinet
     * @return
     */
    public int update(LitemallMainCabinet maincabinet){
    	maincabinet.setUpdateTime(localDateTimeDate(LocalDateTime.now()));
        return maincabineMapper.updateByPrimaryKeySelective(maincabinet);
    }
    
    /**
     * 停用该主柜
     * @param id
     */
    public void deleteById(Integer id) {
    	maincabineMapper.logicalDeleteByPrimaryKey(id);
    }
    /**
     * 根据主柜ID查询主柜使用情况
     * @param id
     * @return
     */
    public LitemallMainCabinet selectByPrimaryKey(Integer id){
    	return maincabineMapper.selectByPrimaryKey(id);
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

    /**
     * 查询所有柜组编号
     * @return
     */
    public List<LitemallMainCabinet> listAllNum(LitemallMainCabinet mainCabinet){
        return maincabineMapper.listAllNum(mainCabinet);
    }

}
