package org.linlinjava.litemall.db.domain;

import java.util.Date;

/**
 * 子柜关联规格表
 * @author Administrator
 *
 */
public class SubCabinetAndStandards {

    private Integer id;		//子柜ID

    private Integer mainCabId; //主柜ID

    private String subCabNum;	//

    private Integer status;

    private Date addTime;
    
    private Integer stanId;
    
    private Date updateTime;
    
    private String stanName;

    private String stanDetails;

    private String stanMoney;
    private Integer stanCapping;

    private Integer stanFree;
    private Integer stanBeyond;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMainCabId() {
		return mainCabId;
	}
	public void setMainCabId(Integer mainCabId) {
		this.mainCabId = mainCabId;
	}
	public String getSubCabNum() {
		return subCabNum;
	}
	public void setSubCabNum(String subCabNum) {
		this.subCabNum = subCabNum;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Integer getStanId() {
		return stanId;
	}
	public void setStanId(Integer stanId) {
		this.stanId = stanId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getStanName() {
		return stanName;
	}
	public void setStanName(String stanName) {
		this.stanName = stanName;
	}
	public String getStanDetails() {
		return stanDetails;
	}
	public void setStanDetails(String stanDetails) {
		this.stanDetails = stanDetails;
	}
	public String getStanMoney() {
		return stanMoney;
	}
	public void setStanMoney(String stanMoney) {
		this.stanMoney = stanMoney;
	}
	public Integer getStanCapping() {
		return stanCapping;
	}
	public void setStanCapping(Integer stanCapping) {
		this.stanCapping = stanCapping;
	}
	public Integer getStanFree() {
		return stanFree;
	}
	public void setStanFree(Integer stanFree) {
		this.stanFree = stanFree;
	}
	public Integer getStanBeyond() {
		return stanBeyond;
	}
	public void setStanBeyond(Integer stanBeyond) {
		this.stanBeyond = stanBeyond;
	}
    
    


}
