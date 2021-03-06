package org.linlinjava.litemall.db.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LitemallStandardsExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public LitemallStandardsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andStanNameIsNull() {
            addCriterion("stan_name is null");
            return (Criteria) this;
        }

        public Criteria andStanNameIsNotNull() {
            addCriterion("stan_name is not null");
            return (Criteria) this;
        }

        public Criteria andStanNameEqualTo(String value) {
            addCriterion("stan_name =", value, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameNotEqualTo(String value) {
            addCriterion("stan_name <>", value, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameGreaterThan(String value) {
            addCriterion("stan_name >", value, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameGreaterThanOrEqualTo(String value) {
            addCriterion("stan_name >=", value, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameLessThan(String value) {
            addCriterion("stan_name <", value, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameLessThanOrEqualTo(String value) {
            addCriterion("stan_name <=", value, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameLike(String value) {
            addCriterion("stan_name like", value, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameNotLike(String value) {
            addCriterion("stan_name not like", value, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameIn(List<String> values) {
            addCriterion("stan_name in", values, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameNotIn(List<String> values) {
            addCriterion("stan_name not in", values, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameBetween(String value1, String value2) {
            addCriterion("stan_name between", value1, value2, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanNameNotBetween(String value1, String value2) {
            addCriterion("stan_name not between", value1, value2, "stanName");
            return (Criteria) this;
        }

        public Criteria andStanDetailsIsNull() {
            addCriterion("stan_details is null");
            return (Criteria) this;
        }

        public Criteria andStanDetailsIsNotNull() {
            addCriterion("stan_details is not null");
            return (Criteria) this;
        }

        public Criteria andStanDetailsEqualTo(String value) {
            addCriterion("stan_details =", value, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsNotEqualTo(String value) {
            addCriterion("stan_details <>", value, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsGreaterThan(String value) {
            addCriterion("stan_details >", value, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsGreaterThanOrEqualTo(String value) {
            addCriterion("stan_details >=", value, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsLessThan(String value) {
            addCriterion("stan_details <", value, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsLessThanOrEqualTo(String value) {
            addCriterion("stan_details <=", value, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsLike(String value) {
            addCriterion("stan_details like", value, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsNotLike(String value) {
            addCriterion("stan_details not like", value, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsIn(List<String> values) {
            addCriterion("stan_details in", values, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsNotIn(List<String> values) {
            addCriterion("stan_details not in", values, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsBetween(String value1, String value2) {
            addCriterion("stan_details between", value1, value2, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanDetailsNotBetween(String value1, String value2) {
            addCriterion("stan_details not between", value1, value2, "stanDetails");
            return (Criteria) this;
        }

        public Criteria andStanMoneyIsNull() {
            addCriterion("stan_money is null");
            return (Criteria) this;
        }

        public Criteria andStanMoneyIsNotNull() {
            addCriterion("stan_money is not null");
            return (Criteria) this;
        }

        public Criteria andStanMoneyEqualTo(String value) {
            addCriterion("stan_money =", value, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyNotEqualTo(String value) {
            addCriterion("stan_money <>", value, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyGreaterThan(String value) {
            addCriterion("stan_money >", value, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyGreaterThanOrEqualTo(String value) {
            addCriterion("stan_money >=", value, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyLessThan(String value) {
            addCriterion("stan_money <", value, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyLessThanOrEqualTo(String value) {
            addCriterion("stan_money <=", value, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyLike(String value) {
            addCriterion("stan_money like", value, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyNotLike(String value) {
            addCriterion("stan_money not like", value, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyIn(List<String> values) {
            addCriterion("stan_money in", values, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyNotIn(List<String> values) {
            addCriterion("stan_money not in", values, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyBetween(String value1, String value2) {
            addCriterion("stan_money between", value1, value2, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanMoneyNotBetween(String value1, String value2) {
            addCriterion("stan_money not between", value1, value2, "stanMoney");
            return (Criteria) this;
        }

        public Criteria andStanCappingIsNull() {
            addCriterion("stan_capping is null");
            return (Criteria) this;
        }

        public Criteria andStanCappingIsNotNull() {
            addCriterion("stan_capping is not null");
            return (Criteria) this;
        }

        public Criteria andStanCappingEqualTo(Integer value) {
            addCriterion("stan_capping =", value, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanCappingNotEqualTo(Integer value) {
            addCriterion("stan_capping <>", value, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanCappingGreaterThan(Integer value) {
            addCriterion("stan_capping >", value, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanCappingGreaterThanOrEqualTo(Integer value) {
            addCriterion("stan_capping >=", value, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanCappingLessThan(Integer value) {
            addCriterion("stan_capping <", value, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanCappingLessThanOrEqualTo(Integer value) {
            addCriterion("stan_capping <=", value, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanCappingIn(List<Integer> values) {
            addCriterion("stan_capping in", values, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanCappingNotIn(List<Integer> values) {
            addCriterion("stan_capping not in", values, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanCappingBetween(Integer value1, Integer value2) {
            addCriterion("stan_capping between", value1, value2, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanCappingNotBetween(Integer value1, Integer value2) {
            addCriterion("stan_capping not between", value1, value2, "stanCapping");
            return (Criteria) this;
        }

        public Criteria andStanMianIdIsNull() {
            addCriterion("stan_mian_id is null");
            return (Criteria) this;
        }

        public Criteria andStanMianIdIsNotNull() {
            addCriterion("stan_mian_id is not null");
            return (Criteria) this;
        }

        public Criteria andStanMianIdEqualTo(Integer value) {
            addCriterion("stan_mian_id =", value, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanMianIdNotEqualTo(Integer value) {
            addCriterion("stan_mian_id <>", value, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanMianIdGreaterThan(Integer value) {
            addCriterion("stan_mian_id >", value, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanMianIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("stan_mian_id >=", value, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanMianIdLessThan(Integer value) {
            addCriterion("stan_mian_id <", value, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanMianIdLessThanOrEqualTo(Integer value) {
            addCriterion("stan_mian_id <=", value, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanMianIdIn(List<Integer> values) {
            addCriterion("stan_mian_id in", values, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanMianIdNotIn(List<Integer> values) {
            addCriterion("stan_mian_id not in", values, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanMianIdBetween(Integer value1, Integer value2) {
            addCriterion("stan_mian_id between", value1, value2, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanMianIdNotBetween(Integer value1, Integer value2) {
            addCriterion("stan_mian_id not between", value1, value2, "stanMianId");
            return (Criteria) this;
        }

        public Criteria andStanFreeIsNull() {
            addCriterion("stan_free is null");
            return (Criteria) this;
        }

        public Criteria andStanFreeIsNotNull() {
            addCriterion("stan_free is not null");
            return (Criteria) this;
        }

        public Criteria andStanFreeEqualTo(Integer value) {
            addCriterion("stan_free =", value, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanFreeNotEqualTo(Integer value) {
            addCriterion("stan_free <>", value, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanFreeGreaterThan(Integer value) {
            addCriterion("stan_free >", value, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanFreeGreaterThanOrEqualTo(Integer value) {
            addCriterion("stan_free >=", value, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanFreeLessThan(Integer value) {
            addCriterion("stan_free <", value, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanFreeLessThanOrEqualTo(Integer value) {
            addCriterion("stan_free <=", value, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanFreeIn(List<Integer> values) {
            addCriterion("stan_free in", values, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanFreeNotIn(List<Integer> values) {
            addCriterion("stan_free not in", values, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanFreeBetween(Integer value1, Integer value2) {
            addCriterion("stan_free between", value1, value2, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanFreeNotBetween(Integer value1, Integer value2) {
            addCriterion("stan_free not between", value1, value2, "stanFree");
            return (Criteria) this;
        }

        public Criteria andStanBeyondIsNull() {
            addCriterion("stan_beyond is null");
            return (Criteria) this;
        }

        public Criteria andStanBeyondIsNotNull() {
            addCriterion("stan_beyond is not null");
            return (Criteria) this;
        }

        public Criteria andStanBeyondEqualTo(Integer value) {
            addCriterion("stan_beyond =", value, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andStanBeyondNotEqualTo(Integer value) {
            addCriterion("stan_beyond <>", value, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andStanBeyondGreaterThan(Integer value) {
            addCriterion("stan_beyond >", value, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andStanBeyondGreaterThanOrEqualTo(Integer value) {
            addCriterion("stan_beyond >=", value, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andStanBeyondLessThan(Integer value) {
            addCriterion("stan_beyond <", value, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andStanBeyondLessThanOrEqualTo(Integer value) {
            addCriterion("stan_beyond <=", value, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andStanBeyondIn(List<Integer> values) {
            addCriterion("stan_beyond in", values, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andStanBeyondNotIn(List<Integer> values) {
            addCriterion("stan_beyond not in", values, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andStanBeyondBetween(Integer value1, Integer value2) {
            addCriterion("stan_beyond between", value1, value2, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andStanBeyondNotBetween(Integer value1, Integer value2) {
            addCriterion("stan_beyond not between", value1, value2, "stanBeyond");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNull() {
            addCriterion("add_time is null");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNotNull() {
            addCriterion("add_time is not null");
            return (Criteria) this;
        }

        public Criteria andAddTimeEqualTo(Date value) {
            addCriterion("add_time =", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotEqualTo(Date value) {
            addCriterion("add_time <>", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThan(Date value) {
            addCriterion("add_time >", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("add_time >=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThan(Date value) {
            addCriterion("add_time <", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThanOrEqualTo(Date value) {
            addCriterion("add_time <=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeIn(List<Date> values) {
            addCriterion("add_time in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotIn(List<Date> values) {
            addCriterion("add_time not in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeBetween(Date value1, Date value2) {
            addCriterion("add_time between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotBetween(Date value1, Date value2) {
            addCriterion("add_time not between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andDelectIsNull() {
            addCriterion("delect is null");
            return (Criteria) this;
        }

        public Criteria andDelectIsNotNull() {
            addCriterion("delect is not null");
            return (Criteria) this;
        }

        public Criteria andDelectEqualTo(Boolean value) {
            addCriterion("delect =", value, "delect");
            return (Criteria) this;
        }

        public Criteria andDelectNotEqualTo(Boolean value) {
            addCriterion("delect <>", value, "delect");
            return (Criteria) this;
        }

        public Criteria andDelectGreaterThan(Boolean value) {
            addCriterion("delect >", value, "delect");
            return (Criteria) this;
        }

        public Criteria andDelectGreaterThanOrEqualTo(Boolean value) {
            addCriterion("delect >=", value, "delect");
            return (Criteria) this;
        }

        public Criteria andDelectLessThan(Boolean value) {
            addCriterion("delect <", value, "delect");
            return (Criteria) this;
        }

        public Criteria andDelectLessThanOrEqualTo(Boolean value) {
            addCriterion("delect <=", value, "delect");
            return (Criteria) this;
        }

        public Criteria andDelectIn(List<Boolean> values) {
            addCriterion("delect in", values, "delect");
            return (Criteria) this;
        }

        public Criteria andDelectNotIn(List<Boolean> values) {
            addCriterion("delect not in", values, "delect");
            return (Criteria) this;
        }

        public Criteria andDelectBetween(Boolean value1, Boolean value2) {
            addCriterion("delect between", value1, value2, "delect");
            return (Criteria) this;
        }

        public Criteria andDelectNotBetween(Boolean value1, Boolean value2) {
            addCriterion("delect not between", value1, value2, "delect");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table litemall_standards
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table litemall_standards
     *
     * @mbggenerated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}