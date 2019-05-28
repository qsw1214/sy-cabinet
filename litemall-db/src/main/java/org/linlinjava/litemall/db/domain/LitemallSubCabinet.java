package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class LitemallSubCabinet {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_sub_cabinet.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_sub_cabinet.sub_cab_num
     *
     * @mbggenerated
     */
    private String subCabNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_sub_cabinet.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_sub_cabinet.add_time
     *
     * @mbggenerated
     */
    private Date addTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_sub_cabinet.stan_id
     *
     * @mbggenerated
     */
    private Integer stanId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_sub_cabinet.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column litemall_sub_cabinet.deleted
     *
     * @mbggenerated
     */
    private Boolean deleted;

    // 柜组编码
    private String mainCabNum;

    // 规格名称
    private String stanName;

    //柜组Id
    private Integer mainId;

    public Integer getMainId() {
        return mainId;
    }

    public void setMainId(Integer mainId) {
        this.mainId = mainId;
    }

    public String getMainCabNum() {
        return mainCabNum;
    }

    public void setMainCabNum(String mainCabNum) {
        this.mainCabNum = mainCabNum;
    }

    public String getStanName() {
        return stanName;
    }

    public void setStanName(String stanName) {
        this.stanName = stanName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_sub_cabinet.id
     *
     * @return the value of litemall_sub_cabinet.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_sub_cabinet.id
     *
     * @param id the value for litemall_sub_cabinet.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_sub_cabinet.sub_cab_num
     *
     * @return the value of litemall_sub_cabinet.sub_cab_num
     *
     * @mbggenerated
     */
    public String getSubCabNum() {
        return subCabNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_sub_cabinet.sub_cab_num
     *
     * @param subCabNum the value for litemall_sub_cabinet.sub_cab_num
     *
     * @mbggenerated
     */
    public void setSubCabNum(String subCabNum) {
        this.subCabNum = subCabNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_sub_cabinet.status
     *
     * @return the value of litemall_sub_cabinet.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_sub_cabinet.status
     *
     * @param status the value for litemall_sub_cabinet.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_sub_cabinet.add_time
     *
     * @return the value of litemall_sub_cabinet.add_time
     *
     * @mbggenerated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_sub_cabinet.add_time
     *
     * @param addTime the value for litemall_sub_cabinet.add_time
     *
     * @mbggenerated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_sub_cabinet.stan_id
     *
     * @return the value of litemall_sub_cabinet.stan_id
     *
     * @mbggenerated
     */
    public Integer getStanId() {
        return stanId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_sub_cabinet.stan_id
     *
     * @param stanId the value for litemall_sub_cabinet.stan_id
     *
     * @mbggenerated
     */
    public void setStanId(Integer stanId) {
        this.stanId = stanId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_sub_cabinet.update_time
     *
     * @return the value of litemall_sub_cabinet.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_sub_cabinet.update_time
     *
     * @param updateTime the value for litemall_sub_cabinet.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column litemall_sub_cabinet.deleted
     *
     * @return the value of litemall_sub_cabinet.deleted
     *
     * @mbggenerated
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column litemall_sub_cabinet.deleted
     *
     * @param deleted the value for litemall_sub_cabinet.deleted
     *
     * @mbggenerated
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}