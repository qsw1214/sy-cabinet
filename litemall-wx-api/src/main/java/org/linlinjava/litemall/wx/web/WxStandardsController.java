package org.linlinjava.litemall.wx.web;

import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallMainCabinet;
import org.linlinjava.litemall.db.domain.LitemallStandards;
import org.linlinjava.litemall.db.domain.LitemallStandardsAndCount;
import org.linlinjava.litemall.db.domain.LitemallSubCabinet;
import org.linlinjava.litemall.db.service.LitemallMainCabinetService;
import org.linlinjava.litemall.db.service.LitemallStandardsService;
import org.linlinjava.litemall.db.service.LitemallSubCabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 查询规格信息
 *
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/wx/standards")
@Validated
public class WxStandardsController {
	@Autowired
	private LitemallStandardsService standardsService;
	@Autowired
	private LitemallSubCabinetService subCabinetService;
	@Autowired
	private LitemallMainCabinetService mainCabinetService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

    @GetMapping("listStan")
    public Object listStandard(String stanName,Integer stanMianId,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "20") Integer size){
		Map<String, Object> map = new HashMap<>();
		List<LitemallStandards> standards = standardsService.listStandard(stanName, stanMianId, page, size);
		List<LitemallStandardsAndCount> standardsList = standardsService.getStandardList(stanMianId);
        if(StringUtils.isEmpty(standardsList)){
            return ResponseUtil.badArgument();
        }
		Map<Integer, Integer> sizeMap = new HashMap<>();
		// 遍历子柜
		for(LitemallStandardsAndCount stanCount : standardsList){
			Integer subId = stanCount.getSubid();
			// 查询缓存是否占用
			String value = stringRedisTemplate.opsForValue().get(String.valueOf(subId));
			// 柜子未占用
			if(value == null ){
				// 每种规格柜子可使用数量
				Integer stanId = stanCount.getId();
				Integer stanSize = sizeMap.get(stanId);
				if(StringUtils.isEmpty(stanSize)){
					stanSize = 0;
				}
				sizeMap.put(stanId, stanSize + 1);
			}
		}
		//获取子柜可用数量
		for(LitemallStandards stan : standards){
			Integer id = stan.getId();
			Integer subSize = sizeMap.get(id);
			if(StringUtils.isEmpty(subSize)){
				subSize = 0;
			}
			stan.setSubSize(subSize);
		}
        long total = PageInfo.of(standards).getTotal();
		map.put("items",standards);
		map.put("total",total);
        return ResponseUtil.ok(map);
    }

	/**
	 * 根据柜组ID查询出所有规格及其下面可用子柜数量
	 * 
	 * @param mainId
	 * @return
	 */
	@GetMapping("standardList")
	public Object getStandardList(Integer mainId) {
		Map<String, Object> map = new HashMap<>();
		List<LitemallStandardsAndCount> standardsList = standardsService.getStandardList(mainId);
		List<LitemallStandardsAndCount> list = new ArrayList<>();
		int LastID = 0;
		int index = 0;
		int count = 1;
		for (int i = 0; i < standardsList.size(); i++) { //遍历
			if (stringRedisTemplate.opsForValue().get(standardsList.get(i).getSubid().toString())== null) { //减去占用柜子
				if (standardsList.get(i).getId() == LastID) {
					LitemallStandardsAndCount standardsAndCount = standardsList.get(i);
					count++;
					standardsAndCount.setCount(count);
					list.set(index - 1, standardsAndCount);
				} else {
					count = 1;
					LitemallStandardsAndCount standardsAndCount = standardsList.get(i);
					LastID = standardsAndCount.getId();
					standardsAndCount.setCount(count);
					list.add(standardsAndCount);
					index++;
				}

			} else {
				if (standardsList.get(i).getId() != LastID) {
					LitemallStandardsAndCount standardsAndCount = standardsList.get(i);
					LastID = standardsAndCount.getId();
					standardsAndCount.setCount(0);
					list.add(standardsAndCount);
					index++;
				}
			}
		}
		map.put("items", list);
		return ResponseUtil.ok(map);
	}

	/**
	 * 获取子柜信息
	 * @param mainId
	 * @param stanId
	 * @return
	 */
	@GetMapping("getSub")
	public Object getSubinet(Integer mainId, Integer stanId) {
//		List<LitemallSubCabinet> subCabList = standardsService.usableSubCab(stanId);
		Integer subCabId = null;
		String subCabNum = null;
		List<LitemallSubCabinet> subList = subCabinetService.querySelective(subCabNum, 0, stanId, null, null);
		System.out.println("查询返回子柜Size:" + subList.size());
		if (StringUtils.isEmpty(subList) && StringUtils.isEmpty(mainId)) {
			return ResponseUtil.badArgument();
		}
		for (LitemallSubCabinet sub : subList) {
			subCabId = sub.getId();
			//查询缓存是否占用
			String subIdStr = String.valueOf(subCabId);
			String value = stringRedisTemplate.opsForValue().get(subIdStr);
			if (value == null) {
				break;
			}
		}
		if (StringUtils.isEmpty(subCabId)) {
			return ResponseUtil.badArgument();
		}
		// 查询子柜、规格和柜组信息
		LitemallSubCabinet subCabinet = subCabinetService.selectByPrimaryKey(subCabId);
		LitemallStandards standard = standardsService.queryStandard(stanId);
		LitemallMainCabinet mainCabinet = mainCabinetService.selectByPrimaryKey(mainId);
		if (StringUtils.isEmpty(subCabinet) && StringUtils.isEmpty(standard) && StringUtils.isEmpty(mainCabinet)) {
			return ResponseUtil.badArgument();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("subCab", subCabinet);
		map.put("stan", standard);
		map.put("mainCab", mainCabinet);
		map.put("total", subList.size());
		return ResponseUtil.ok(map);
	}

}
