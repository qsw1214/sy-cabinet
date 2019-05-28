package org.linlinjava.litemall.db.domain;

public class LitemallUsageAndUser {

	private LitemallUser user;
	private LitemallUsageRecord usa;

	private Integer count;

	public LitemallUser getUser() {
		return user;
	}

	public void setUser(LitemallUser user) {
		this.user = user;
	}

	public LitemallUsageRecord getUsa() {
		return usa;
	}

	public void setUsa(LitemallUsageRecord usa) {
		this.usa = usa;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}