package com.fid.domain.vo;

public class StockVo {
	
	private String stockCode;
	
	private String stockWindCode;

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockWindCode() {
		return stockWindCode;
	}

	public void setStockWindCode(String stockWindCode) {
		this.stockWindCode = stockWindCode;
	}

	@Override
	public String toString() {
		return "StockVo [stockCode=" + stockCode + ", stockWindCode=" + stockWindCode + "]";
	}
	
}
