package org.linlinjava.litemall.db.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LitemallCapitalRecordExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    public LitemallCapitalRecordExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
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
     * This method corresponds to the database table litemall_capital _record
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
     * This method corresponds to the database table litemall_capital _record
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_capital _record
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
     * This class corresponds to the database table litemall_capital _record
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

        public Criteria andCapIdIsNull() {
            addCriterion("cap_id is null");
            return (Criteria) this;
        }

        public Criteria andCapIdIsNotNull() {
            addCriterion("cap_id is not null");
            return (Criteria) this;
        }

        public Criteria andCapIdEqualTo(Integer value) {
            addCriterion("cap_id =", value, "capId");
            return (Criteria) this;
        }

        public Criteria andCapIdNotEqualTo(Integer value) {
            addCriterion("cap_id <>", value, "capId");
            return (Criteria) this;
        }

        public Criteria andCapIdGreaterThan(Integer value) {
            addCriterion("cap_id >", value, "capId");
            return (Criteria) this;
        }

        public Criteria andCapIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("cap_id >=", value, "capId");
            return (Criteria) this;
        }

        public Criteria andCapIdLessThan(Integer value) {
            addCriterion("cap_id <", value, "capId");
            return (Criteria) this;
        }

        public Criteria andCapIdLessThanOrEqualTo(Integer value) {
            addCriterion("cap_id <=", value, "capId");
            return (Criteria) this;
        }

        public Criteria andCapIdIn(List<Integer> values) {
            addCriterion("cap_id in", values, "capId");
            return (Criteria) this;
        }

        public Criteria andCapIdNotIn(List<Integer> values) {
            addCriterion("cap_id not in", values, "capId");
            return (Criteria) this;
        }

        public Criteria andCapIdBetween(Integer value1, Integer value2) {
            addCriterion("cap_id between", value1, value2, "capId");
            return (Criteria) this;
        }

        public Criteria andCapIdNotBetween(Integer value1, Integer value2) {
            addCriterion("cap_id not between", value1, value2, "capId");
            return (Criteria) this;
        }

        public Criteria andCapUseridIsNull() {
            addCriterion("cap_userid is null");
            return (Criteria) this;
        }

        public Criteria andCapUseridIsNotNull() {
            addCriterion("cap_userid is not null");
            return (Criteria) this;
        }

        public Criteria andCapUseridEqualTo(Integer value) {
            addCriterion("cap_userid =", value, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapUseridNotEqualTo(Integer value) {
            addCriterion("cap_userid <>", value, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapUseridGreaterThan(Integer value) {
            addCriterion("cap_userid >", value, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapUseridGreaterThanOrEqualTo(Integer value) {
            addCriterion("cap_userid >=", value, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapUseridLessThan(Integer value) {
            addCriterion("cap_userid <", value, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapUseridLessThanOrEqualTo(Integer value) {
            addCriterion("cap_userid <=", value, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapUseridIn(List<Integer> values) {
            addCriterion("cap_userid in", values, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapUseridNotIn(List<Integer> values) {
            addCriterion("cap_userid not in", values, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapUseridBetween(Integer value1, Integer value2) {
            addCriterion("cap_userid between", value1, value2, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapUseridNotBetween(Integer value1, Integer value2) {
            addCriterion("cap_userid not between", value1, value2, "capUserid");
            return (Criteria) this;
        }

        public Criteria andCapMoneyIsNull() {
            addCriterion("cap_money is null");
            return (Criteria) this;
        }

        public Criteria andCapMoneyIsNotNull() {
            addCriterion("cap_money is not null");
            return (Criteria) this;
        }

        public Criteria andCapMoneyEqualTo(String value) {
            addCriterion("cap_money =", value, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyNotEqualTo(String value) {
            addCriterion("cap_money <>", value, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyGreaterThan(String value) {
            addCriterion("cap_money >", value, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyGreaterThanOrEqualTo(String value) {
            addCriterion("cap_money >=", value, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyLessThan(String value) {
            addCriterion("cap_money <", value, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyLessThanOrEqualTo(String value) {
            addCriterion("cap_money <=", value, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyLike(String value) {
            addCriterion("cap_money like", value, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyNotLike(String value) {
            addCriterion("cap_money not like", value, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyIn(List<String> values) {
            addCriterion("cap_money in", values, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyNotIn(List<String> values) {
            addCriterion("cap_money not in", values, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyBetween(String value1, String value2) {
            addCriterion("cap_money between", value1, value2, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapMoneyNotBetween(String value1, String value2) {
            addCriterion("cap_money not between", value1, value2, "capMoney");
            return (Criteria) this;
        }

        public Criteria andCapDealIsNull() {
            addCriterion("cap_deal is null");
            return (Criteria) this;
        }

        public Criteria andCapDealIsNotNull() {
            addCriterion("cap_deal is not null");
            return (Criteria) this;
        }

        public Criteria andCapDealEqualTo(String value) {
            addCriterion("cap_deal =", value, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealNotEqualTo(String value) {
            addCriterion("cap_deal <>", value, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealGreaterThan(String value) {
            addCriterion("cap_deal >", value, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealGreaterThanOrEqualTo(String value) {
            addCriterion("cap_deal >=", value, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealLessThan(String value) {
            addCriterion("cap_deal <", value, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealLessThanOrEqualTo(String value) {
            addCriterion("cap_deal <=", value, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealLike(String value) {
            addCriterion("cap_deal like", value, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealNotLike(String value) {
            addCriterion("cap_deal not like", value, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealIn(List<String> values) {
            addCriterion("cap_deal in", values, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealNotIn(List<String> values) {
            addCriterion("cap_deal not in", values, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealBetween(String value1, String value2) {
            addCriterion("cap_deal between", value1, value2, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealNotBetween(String value1, String value2) {
            addCriterion("cap_deal not between", value1, value2, "capDeal");
            return (Criteria) this;
        }

        public Criteria andCapDealidIsNull() {
            addCriterion("cap_dealid is null");
            return (Criteria) this;
        }

        public Criteria andCapDealidIsNotNull() {
            addCriterion("cap_dealid is not null");
            return (Criteria) this;
        }

        public Criteria andCapDealidEqualTo(Integer value) {
            addCriterion("cap_dealid =", value, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDealidNotEqualTo(Integer value) {
            addCriterion("cap_dealid <>", value, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDealidGreaterThan(Integer value) {
            addCriterion("cap_dealid >", value, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDealidGreaterThanOrEqualTo(Integer value) {
            addCriterion("cap_dealid >=", value, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDealidLessThan(Integer value) {
            addCriterion("cap_dealid <", value, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDealidLessThanOrEqualTo(Integer value) {
            addCriterion("cap_dealid <=", value, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDealidIn(List<Integer> values) {
            addCriterion("cap_dealid in", values, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDealidNotIn(List<Integer> values) {
            addCriterion("cap_dealid not in", values, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDealidBetween(Integer value1, Integer value2) {
            addCriterion("cap_dealid between", value1, value2, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDealidNotBetween(Integer value1, Integer value2) {
            addCriterion("cap_dealid not between", value1, value2, "capDealid");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeIsNull() {
            addCriterion("cap_datetime is null");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeIsNotNull() {
            addCriterion("cap_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeEqualTo(Date value) {
            addCriterion("cap_datetime =", value, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeNotEqualTo(Date value) {
            addCriterion("cap_datetime <>", value, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeGreaterThan(Date value) {
            addCriterion("cap_datetime >", value, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("cap_datetime >=", value, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeLessThan(Date value) {
            addCriterion("cap_datetime <", value, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeLessThanOrEqualTo(Date value) {
            addCriterion("cap_datetime <=", value, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeIn(List<Date> values) {
            addCriterion("cap_datetime in", values, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeNotIn(List<Date> values) {
            addCriterion("cap_datetime not in", values, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeBetween(Date value1, Date value2) {
            addCriterion("cap_datetime between", value1, value2, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapDatetimeNotBetween(Date value1, Date value2) {
            addCriterion("cap_datetime not between", value1, value2, "capDatetime");
            return (Criteria) this;
        }

        public Criteria andCapTitleIsNull() {
            addCriterion("cap_title is null");
            return (Criteria) this;
        }

        public Criteria andCapTitleIsNotNull() {
            addCriterion("cap_title is not null");
            return (Criteria) this;
        }

        public Criteria andCapTitleEqualTo(String value) {
            addCriterion("cap_title =", value, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleNotEqualTo(String value) {
            addCriterion("cap_title <>", value, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleGreaterThan(String value) {
            addCriterion("cap_title >", value, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleGreaterThanOrEqualTo(String value) {
            addCriterion("cap_title >=", value, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleLessThan(String value) {
            addCriterion("cap_title <", value, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleLessThanOrEqualTo(String value) {
            addCriterion("cap_title <=", value, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleLike(String value) {
            addCriterion("cap_title like", value, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleNotLike(String value) {
            addCriterion("cap_title not like", value, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleIn(List<String> values) {
            addCriterion("cap_title in", values, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleNotIn(List<String> values) {
            addCriterion("cap_title not in", values, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleBetween(String value1, String value2) {
            addCriterion("cap_title between", value1, value2, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapTitleNotBetween(String value1, String value2) {
            addCriterion("cap_title not between", value1, value2, "capTitle");
            return (Criteria) this;
        }

        public Criteria andCapStatusIsNull() {
            addCriterion("cap_status is null");
            return (Criteria) this;
        }

        public Criteria andCapStatusIsNotNull() {
            addCriterion("cap_status is not null");
            return (Criteria) this;
        }

        public Criteria andCapStatusEqualTo(Integer value) {
            addCriterion("cap_status =", value, "capStatus");
            return (Criteria) this;
        }

        public Criteria andCapStatusNotEqualTo(Integer value) {
            addCriterion("cap_status <>", value, "capStatus");
            return (Criteria) this;
        }

        public Criteria andCapStatusGreaterThan(Integer value) {
            addCriterion("cap_status >", value, "capStatus");
            return (Criteria) this;
        }

        public Criteria andCapStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("cap_status >=", value, "capStatus");
            return (Criteria) this;
        }

        public Criteria andCapStatusLessThan(Integer value) {
            addCriterion("cap_status <", value, "capStatus");
            return (Criteria) this;
        }

        public Criteria andCapStatusLessThanOrEqualTo(Integer value) {
            addCriterion("cap_status <=", value, "capStatus");
            return (Criteria) this;
        }

        public Criteria andCapStatusIn(List<Integer> values) {
            addCriterion("cap_status in", values, "capStatus");
            return (Criteria) this;
        }

        public Criteria andCapStatusNotIn(List<Integer> values) {
            addCriterion("cap_status not in", values, "capStatus");
            return (Criteria) this;
        }

        public Criteria andCapStatusBetween(Integer value1, Integer value2) {
            addCriterion("cap_status between", value1, value2, "capStatus");
            return (Criteria) this;
        }

        public Criteria andCapStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("cap_status not between", value1, value2, "capStatus");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table litemall_capital _record
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
     * This class corresponds to the database table litemall_capital _record
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