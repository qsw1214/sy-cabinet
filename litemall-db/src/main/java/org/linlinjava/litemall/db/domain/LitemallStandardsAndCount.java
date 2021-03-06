package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class LitemallStandardsAndCount {
	
	private Integer count;
	
	private String stanDeposit;
	
	
	public String getStanDeposit() {
		return stanDeposit;
	}

	public void setStanDeposit(String stanDeposit) {
		this.stanDeposit = stanDeposit;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	private Integer subid;
	
	
	public Integer getSubid() {
		return subid;
	}

	public void setSubid(Integer subid) {
		this.subid = subid;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.stan_name
     *
     * @mbggenerated
     */
    private String stanName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.stan_details
     *
     * @mbggenerated
     */
    private String stanDetails;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.stan_money
     *
     * @mbggenerated
     */
    private String stanMoney;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.stan_capping
     *
     * @mbggenerated
     */
    private Integer stanCapping;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.stan_mian_id
     *
     * @mbggenerated
     */
    private Integer stanMianId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.stan_free
     *
     * @mbggenerated
     */
    private Integer stanFree;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.stan_beyond
     *
     * @mbggenerated
     */
    private Integer stanBeyond;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.add_time
     *
     * @mbggenerated
     */
    private Date addTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_standards.delect
     *
     * @mbggenerated
     */
    private Boolean delect;

    /**
     * 该规格下子柜数量(可寄存)
     */
    private Integer subSize;

    public Integer getSubSize() {
        return subSize;
    }

    public void setSubSize(Integer subSize) {
        this.subSize = subSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.id
     *
     * @return the value of litemall_standards.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.id
     *
     * @param id the value for litemall_standards.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.stan_name
     *
     * @return the value of litemall_standards.stan_name
     *
     * @mbggenerated
     */
    public String getStanName() {
        return stanName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.stan_name
     *
     * @param stanName the value for litemall_standards.stan_name
     *
     * @mbggenerated
     */
    public void setStanName(String stanName) {
        this.stanName = stanName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.stan_details
     *
     * @return the value of litemall_standards.stan_details
     *
     * @mbggenerated
     */
    public String getStanDetails() {
        return stanDetails;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.stan_details
     *
     * @param stanDetails the value for litemall_standards.stan_details
     *
     * @mbggenerated
     */
    public void setStanDetails(String stanDetails) {
        this.stanDetails = stanDetails;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.stan_money
     *
     * @return the value of litemall_standards.stan_money
     *
     * @mbggenerated
     */
    public String getStanMoney() {
        return stanMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.stan_money
     *
     * @param stanMoney the value for litemall_standards.stan_money
     *
     * @mbggenerated
     */
    public void setStanMoney(String stanMoney) {
        this.stanMoney = stanMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.stan_capping
     *
     * @return the value of litemall_standards.stan_capping
     *
     * @mbggenerated
     */
    public Integer getStanCapping() {
        return stanCapping;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.stan_capping
     *
     * @param stanCapping the value for litemall_standards.stan_capping
     *
     * @mbggenerated
     */
    public void setStanCapping(Integer stanCapping) {
        this.stanCapping = stanCapping;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.stan_mian_id
     *
     * @return the value of litemall_standards.stan_mian_id
     *
     * @mbggenerated
     */
    public Integer getStanMianId() {
        return stanMianId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.stan_mian_id
     *
     * @param stanMianId the value for litemall_standards.stan_mian_id
     *
     * @mbggenerated
     */
    public void setStanMianId(Integer stanMianId) {
        this.stanMianId = stanMianId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.stan_free
     *
     * @return the value of litemall_standards.stan_free
     *
     * @mbggenerated
     */
    public Integer getStanFree() {
        return stanFree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.stan_free
     *
     * @param stanFree the value for litemall_standards.stan_free
     *
     * @mbggenerated
     */
    public void setStanFree(Integer stanFree) {
        this.stanFree = stanFree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.stan_beyond
     *
     * @return the value of litemall_standards.stan_beyond
     *
     * @mbggenerated
     */
    public Integer getStanBeyond() {
        return stanBeyond;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.stan_beyond
     *
     * @param stanBeyond the value for litemall_standards.stan_beyond
     *
     * @mbggenerated
     */
    public void setStanBeyond(Integer stanBeyond) {
        this.stanBeyond = stanBeyond;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.add_time
     *
     * @return the value of litemall_standards.add_time
     *
     * @mbggenerated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.add_time
     *
     * @param addTime the value for litemall_standards.add_time
     *
     * @mbggenerated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.update_time
     *
     * @return the value of litemall_standards.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.update_time
     *
     * @param updateTime the value for litemall_standards.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_standards.delect
     *
     * @return the value of litemall_standards.delect
     *
     * @mbggenerated
     */
    public Boolean getDelect() {
        return delect;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_standards.delect
     *
     * @param delect the value for litemall_standards.delect
     *
     * @mbggenerated
     */
    public void setDelect(Boolean delect) {
        this.delect = delect;
    }
}