package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;

import org.linlinjava.litemall.db.dao.LitemallMainCabinetMapper;
import org.linlinjava.litemall.db.dao.LitemallStandardsMapper;
import org.linlinjava.litemall.db.dao.LitemallSubCabinetMapper;
import org.linlinjava.litemall.db.domain.LitemallMainCabinet;
import org.linlinjava.litemall.db.domain.LitemallStandards;
import org.linlinjava.litemall.db.domain.LitemallSubCabinet;
import org.linlinjava.litemall.db.domain.LitemallSubCabinetExample;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallSubCabinetService {
    @Resource
    private LitemallSubCabinetMapper subCabinetMapper;
    @Resource
    private LitemallStandardsMapper standardsMapper;
    @Resource
    private LitemallMainCabinetMapper mainCabinetMapper;
    @Resource
    private LitemallMainCabinetService mainCabinetService;

    /**
     * 条件分页查询子柜信息
     *
     * @param subCabNum 子柜编号
     * @param status 状态
     * @param standardId 规格
     * @param page
     * @param size
     * @return
     */
    public List<LitemallSubCabinet> querySelective(String subCabNum, Integer status, Integer standardId, Integer page, Integer size){
        //实例化子柜例对象
        LitemallSubCabinetExample subCabExample = new LitemallSubCabinetExample();
        //声明"标准"
        LitemallSubCabinetExample.Criteria criteria = subCabExample.createCriteria();
        //子柜编码
        if(!StringUtils.isEmpty(subCabNum)){
            criteria.andSubCabNumLike("%" + subCabNum + "%");
        }
        //使用状态
        if(!StringUtils.isEmpty(status)){
            criteria.andStatusEqualTo(status);
        }
        //规格Id
        if(!StringUtils.isEmpty(standardId)){
            criteria.andStanIdEqualTo(standardId );
        }
        if(!StringUtils.isEmpty(page)
                || !StringUtils.isEmpty(size)) {
            //在分页插件添加页码
            PageHelper.startPage(page, size);
        }
        return subCabinetMapper.selectByExample(subCabExample);
    }
    /**
     * 修改子柜状态
     * @param subCabinet
     * @return
     */
    public int UseSubCab(LitemallSubCabinet subCabinet){
        return subCabinetMapper.updateByPrimaryKeySelective(subCabinet);
    }
    /**
     * 添加子柜信息
     *
     * @param
     * @return
     */
    public int addSubCabinet(LitemallSubCabinet subCabinet){
        //转换时间格式
        Date date = mainCabinetService.localDateTimeDate(LocalDateTime.now());
        //添加当前时间
        subCabinet.setAddTime(date);
        subCabinet.setUpdateTime(date);
        //调用方法
        return subCabinetMapper.insertSelective(subCabinet);
    }

    /**
     * 条件查询子柜信息
     *
     */
    public List<LitemallSubCabinet> listSubCabInfo(Integer mainId,String mainCabNum,String subCabNum, Integer status, Integer standardId, Integer page, Integer size){
        Integer currIndex = (page-1)* size;
        Map<String, Object> map = new HashMap<>();
        map.put("subCabNum",subCabNum);
        map.put("status",status);
        map.put("standardId",standardId);
        map.put("mainCabNum",mainCabNum);
        map.put("mainId",mainId);
        map.put("deleted",false);
        map.put("currIndex",currIndex);
        map.put("pageSize",size);
        return subCabinetMapper.listByExample(map);
    }
    /**
     * 删除子柜信息(逻辑删除)ById
     *
     * @param id
     */
    public void delectSubCabById(Integer id){
        //调用方法
        subCabinetMapper.logicalDeleteByPrimaryKey(id);
    }

    /**
     * 根据主柜ID查询子柜集合
     * @param mianid
     * @return
     */
    public  List<LitemallSubCabinet> selectListByMianid(Integer mianid){
        //实例化子柜例对象
        LitemallSubCabinetExample subCabExample = new LitemallSubCabinetExample();
        //声明"标准"
        LitemallSubCabinetExample.Criteria criteria = subCabExample.createCriteria();
        criteria.andDeletedEqualTo(false);
        criteria.andStatusEqualTo(0);
        return subCabinetMapper.selectByExample(subCabExample);
    }
    /**
     * 修改子柜信息
     *
     */
    public int updateSubCab(LitemallSubCabinet subCabinet){
        Date date = mainCabinetService.localDateTimeDate(LocalDateTime.now());
        subCabinet.setUpdateTime(date);
        return subCabinetMapper.updateByPrimaryKeySelective(subCabinet);
    }

    /**
     * 查询柜组编号和柜子信息
     * @param page
     * @param size
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LitemallSubCabinet> listMainSub(String mainCabNum,String subCabNum,Integer status, Integer standardId,Integer page,Integer size){
        @SuppressWarnings("rawtypes")
        Map map = new HashMap<>();
        map.put("mainCabNum",mainCabNum);
        map.put("subCabNum",subCabNum);
        map.put("status",status);
        map.put("standard",standardId);
        map.put("delete", false);
        map.put("currIndex", (page-1)*size);
        map.put("pageSize", size);
        return subCabinetMapper.listByExample(map);
    }

    /**
     * 根据ID查询出子柜信息
     * @param boxid
     * @return
     */
    public LitemallSubCabinet selectByPrimaryKey(Integer boxid){
        return subCabinetMapper.selectByPrimaryKey(boxid);
    }
    /**
     * 将一分钟前时间并状态为取包中的子柜修改成空闲状态
     * @param
     * @return
     */
    public int upStatusByUpdateTime(){
    	Date date = new Date();
    	Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR,-1);
    	return subCabinetMapper.upStatusByUpdateTime(c.getTime());
    }
    /**
     * 查询子柜、规格和柜组信息
     * @param id
     * @return
     * @author liuyu
     */
    public Map<String,Object> querySubStanMain(Integer id){
        //查询子柜信息
        LitemallSubCabinet subCabinet = subCabinetMapper.selectByPrimaryKey(id);
        if(StringUtils.isEmpty(subCabinet) && StringUtils.isEmpty(subCabinet.getStanId())){
            return null;
        }
        //查询规格信息
        Integer stanId = subCabinet.getStanId();
        LitemallStandards standards = standardsMapper.selectById(stanId);
        if(StringUtils.isEmpty(standards) && StringUtils.isEmpty(standards.getStanMianId())){
            return null;
        }
        Integer stanMianId = standards.getStanMianId();
        LitemallMainCabinet mainCabinet = mainCabinetMapper.selectByPrimaryKey(stanMianId);
        if(StringUtils.isEmpty(mainCabinet)){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("sub",subCabinet);
        map.put("stan",standards);
        map.put("main",mainCabinet);
        return map;
    }

    public LitemallSubCabinet query(Integer subId){
        return subCabinetMapper.selectByPrimaryKey(subId);
    }

}
