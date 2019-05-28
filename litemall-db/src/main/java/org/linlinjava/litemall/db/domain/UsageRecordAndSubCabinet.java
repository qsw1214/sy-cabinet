package org.linlinjava.litemall.db.domain;

public class UsageRecordAndSubCabinet {
	private LitemallUsageRecord usa;
	private LitemallSubCabinet sub;
	private LitemallStandards sta;

	public LitemallUsageRecord getUsa() {
		return usa;
	}

	public void setUsa(LitemallUsageRecord usa) {
		this.usa = usa;
	}

	public LitemallSubCabinet getSub() {
		return sub;
	}

	public void setSub(LitemallSubCabinet sub) {
		this.sub = sub;
	}

	public LitemallStandards getSta() {
		return sta;
	}

	public void setSta(LitemallStandards sta) {
		this.sta = sta;
	}

}
