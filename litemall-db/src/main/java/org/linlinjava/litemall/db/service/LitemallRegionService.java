package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallRegionMapper;
import org.linlinjava.litemall.db.domain.LitemallRegion;
import org.linlinjava.litemall.db.domain.LitemallRegionExample;
import org.linlinjava.litemall.db.domain.LitemallRegionInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class LitemallRegionService {

    @Resource
    private LitemallRegionMapper regionMapper;

    public String selectNameById(Integer regionId){
        return regionMapper.selectNameById(regionId);
    }

    public Integer selectByCid(Integer cid){
        return regionMapper.selectByCid(cid);
    }

    public Integer selectIdByCode(Integer code){
        return regionMapper.selectIdByCode(code);
    }

    public List<LitemallRegionInfo> selectInfoByCode(Integer code){
        Integer areaId = regionMapper.selectIdByCode(code);
        List<LitemallRegionInfo> list = new ArrayList<>();
        LitemallRegionInfo areaInfo = regionMapper.selectInfoById(areaId);
        list.add(areaInfo);
        LitemallRegionInfo cityInfo = regionMapper.selectInfoById(areaInfo.getPid());
        list.add(cityInfo);
        LitemallRegionInfo provinceInfo = regionMapper.selectInfoById(cityInfo.getPid());
        list.add(provinceInfo);
        return list;
    }

    public List<LitemallRegion> getAll(){
        LitemallRegionExample example = new LitemallRegionExample();
        byte b = 4;
        example.or().andTypeNotEqualTo(b);
        return regionMapper.selectByExample(example);
    }

    public List<LitemallRegion> queryByPid(Integer parentId) {
        LitemallRegionExample example = new LitemallRegionExample();
        example.or().andPidEqualTo(parentId);
        return regionMapper.selectByExample(example);
    }

    public LitemallRegion findById(Integer id) {
        return regionMapper.selectByPrimaryKey(id);
    }

    public List<LitemallRegion> querySelective(String name, Integer code, Integer page, Integer size, String sort, String order) {
        LitemallRegionExample example = new LitemallRegionExample();
        LitemallRegionExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(code)) {
            criteria.andCodeEqualTo(code);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return regionMapper.selectByExample(example);
    }
}
