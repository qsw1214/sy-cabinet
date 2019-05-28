package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallStandardsMapper;
import org.linlinjava.litemall.db.domain.LitemallStandards;
import org.linlinjava.litemall.db.domain.LitemallStandardsAndCount;
import org.linlinjava.litemall.db.domain.LitemallStandardsExample;
import org.linlinjava.litemall.db.domain.LitemallSubCabinet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class LitemallStandardsService {
    @Resource
    private LitemallStandardsMapper standardsMapper;
    @Autowired
    private LitemallSubCabinetService subCabinetService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询规格信息
     * @param
     * @param page
     * @param size
     * @return
     */
    public List<LitemallStandards> querySelective(String stanName, Integer stanMainId,
                                                  Integer page,
                                                  Integer size) {
        LitemallStandardsExample example = new LitemallStandardsExample();
        LitemallStandardsExample.Criteria criteria =  example.createCriteria();
        if (!StringUtils.isEmpty(stanName)) {
            criteria.andStanNameLike("%" + stanName + "%");
        }
        if(!StringUtils.isEmpty(stanMainId)){
            criteria.andStanMianIdEqualTo(stanMainId);
        }
        criteria.andDelectEqualTo(false);
        PageHelper.startPage(page, size);
        return standardsMapper.selectByExample(example);
    }

    /**
     * 查询规格信息
     * @param stanId
     * @return
     */
    public LitemallStandards queryStandard(Integer stanId){
        return standardsMapper.queryStandards(stanId);
    }

    public LitemallStandards selectStanBySub(Integer subCabId){
        return standardsMapper.selectStanBySub(subCabId);
    }


    /**
     * 单个查询规格信息
     * @param id
     * @return
     * @author liuyu
     */
    public LitemallStandards selectById(Integer id){
        return standardsMapper.selectById(id);
    }
    
    /**
     * 根据柜组ID查询出所有规格及其下面可用子柜数量
     * @param mainid
     * @return
     */
    public List<LitemallStandardsAndCount> getStandardList(	Integer mainid){
    	return standardsMapper.getStandardList(mainid);
    }
    
    /**
     * 添加规格
     * @param standards
     * @return
     */
    public int add(LitemallStandards standards){
        standards.setAddTime(localDateTimeDate(LocalDateTime.now()));
        standards.setUpdateTime(localDateTimeDate(LocalDateTime.now()));
        return standardsMapper.insertSelective(standards);
    }

    /**
     * 修改规格信息
     * @param record
     * @return
     */
    public int update(LitemallStandards record){
        record.setUpdateTime(localDateTimeDate(LocalDateTime.now()));
        return standardsMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 删除规格
     * @param id
     */
    public void deleteById(Integer id) {
        standardsMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 获取可使用(可存储)子柜集合
     * @param stanId
     * @return
     */
    public List<LitemallSubCabinet> usableSubCab(Integer stanId){
        String subCabNum = null;
        Map<Integer, Object> subMap = new HashMap<>();
        List<LitemallSubCabinet> subList = subCabinetService.querySelective(subCabNum,0,stanId,null, null);
        if(StringUtils.isEmpty(subList)){
            return null;
        }
        for(LitemallSubCabinet sub: subList){
            Integer subId = sub.getId();
            String subIdStr=String.valueOf(subId);
            // 查询子柜是否被占用
            String value = stringRedisTemplate.opsForValue().get(subIdStr);
            if (value == null){
                subMap.put(subId,sub);
            }
        }
        // 获取空闲未被占用的子柜集合
        List<LitemallSubCabinet> subCabList =new ArrayList(subMap.values());
        return subCabList;
    }

    /**
     * 查询规格信息 + 柜组编码
     */
    public List<LitemallStandards> listStandard(String stanName, Integer stanMianId, Integer page, Integer size){
        Integer currIndex = (page-1)* size;
        Map<String, Object> map = new HashMap<>();
        map.put("stanName", stanName);
        map.put("stanMianId", stanMianId);
        map.put("currIndex", currIndex);
        map.put("size", size);
        return standardsMapper.queryStand(map);
    }
    
    /**
     * 谭糠
     * 根据柜组ID查询下面正常规则
     * @param mainId
     * @return
     */
    public List<LitemallStandards> getSatnByMain(Integer mainId){
    	return standardsMapper.getSatnByMain(mainId);
    }

    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public Date localDateTimeDate(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }
}
