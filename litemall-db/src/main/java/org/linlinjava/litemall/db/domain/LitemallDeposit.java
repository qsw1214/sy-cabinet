package org.linlinjava.litemall.db.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class LitemallDeposit {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.depo_id
     *
     * @mbggenerated
     */
    private Integer depoId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.depo_user_id
     *
     * @mbggenerated
     */
    private Integer depoUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.depo_sub_id
     *
     * @mbggenerated
     */
    private Integer depoSubId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.depo_money
     *
     * @mbggenerated
     */
    private Integer depoMoney;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.depo_record_id
     *
     * @mbggenerated
     */
    private Integer depoRecordId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.depo_status
     *
     * @mbggenerated
     */
    private Integer depoStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.return_time
     *
     * @mbggenerated
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date returnTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.add_time
     *
     * @mbggenerated
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.update_time
     *
     * @mbggenerated
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_deposit.depo_deleted
     *
     * @mbggenerated
     */
    private Boolean depoDeleted;

    /**
     * 规格名称
     */
    private String stanName;
    /**
     * 子柜编号
     */
    private String subCabNum;
    /**
     * 柜组区域
     */
    private String mainRegion;

    private Integer page;
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getStanName() {
        return stanName;
    }

    public void setStanName(String stanName) {
        this.stanName = stanName;
    }

    public String getSubCabNum() {
        return subCabNum;
    }

    public void setSubCabNum(String subCabNum) {
        this.subCabNum = subCabNum;
    }

    public String getMainRegion() {
        return mainRegion;
    }

    public void setMainRegion(String mainRegion) {
        this.mainRegion = mainRegion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.depo_id
     *
     * @return the value of litemall_deposit.depo_id
     *
     * @mbggenerated
     */
    public Integer getDepoId() {
        return depoId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.depo_id
     *
     * @param depoId the value for litemall_deposit.depo_id
     *
     * @mbggenerated
     */
    public void setDepoId(Integer depoId) {
        this.depoId = depoId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.depo_user_id
     *
     * @return the value of litemall_deposit.depo_user_id
     *
     * @mbggenerated
     */
    public Integer getDepoUserId() {
        return depoUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.depo_user_id
     *
     * @param depoUserId the value for litemall_deposit.depo_user_id
     *
     * @mbggenerated
     */
    public void setDepoUserId(Integer depoUserId) {
        this.depoUserId = depoUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.depo_sub_id
     *
     * @return the value of litemall_deposit.depo_sub_id
     *
     * @mbggenerated
     */
    public Integer getDepoSubId() {
        return depoSubId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.depo_sub_id
     *
     * @param depoSubId the value for litemall_deposit.depo_sub_id
     *
     * @mbggenerated
     */
    public void setDepoSubId(Integer depoSubId) {
        this.depoSubId = depoSubId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.depo_money
     *
     * @return the value of litemall_deposit.depo_money
     *
     * @mbggenerated
     */
    public Integer getDepoMoney() {
        return depoMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.depo_money
     *
     * @param depoMoney the value for litemall_deposit.depo_money
     *
     * @mbggenerated
     */
    public void setDepoMoney(Integer depoMoney) {
        this.depoMoney = depoMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.depo_record_id
     *
     * @return the value of litemall_deposit.depo_record_id
     *
     * @mbggenerated
     */
    public Integer getDepoRecordId() {
        return depoRecordId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.depo_record_id
     *
     * @param depoRecordId the value for litemall_deposit.depo_record_id
     *
     * @mbggenerated
     */
    public void setDepoRecordId(Integer depoRecordId) {
        this.depoRecordId = depoRecordId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.depo_status
     *
     * @return the value of litemall_deposit.depo_status
     *
     * @mbggenerated
     */
    public Integer getDepoStatus() {
        return depoStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.depo_status
     *
     * @param depoStatus the value for litemall_deposit.depo_status
     *
     * @mbggenerated
     */
    public void setDepoStatus(Integer depoStatus) {
        this.depoStatus = depoStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.return_time
     *
     * @return the value of litemall_deposit.return_time
     *
     * @mbggenerated
     */
    public Date getReturnTime() {
        return returnTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.return_time
     *
     * @param returnTime the value for litemall_deposit.return_time
     *
     * @mbggenerated
     */
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.add_time
     *
     * @return the value of litemall_deposit.add_time
     *
     * @mbggenerated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.add_time
     *
     * @param addTime the value for litemall_deposit.add_time
     *
     * @mbggenerated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.update_time
     *
     * @return the value of litemall_deposit.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.update_time
     *
     * @param updateTime the value for litemall_deposit.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_deposit.depo_deleted
     *
     * @return the value of litemall_deposit.depo_deleted
     *
     * @mbggenerated
     */
    public Boolean getDepoDeleted() {
        return depoDeleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_deposit.depo_deleted
     *
     * @param depoDeleted the value for litemall_deposit.depo_deleted
     *
     * @mbggenerated
     */
    public void setDepoDeleted(Boolean depoDeleted) {
        this.depoDeleted = depoDeleted;
    }
}