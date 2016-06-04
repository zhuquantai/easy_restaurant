package com.dao.impl;

import java.util.List;

import com.dao.TradeDAO;
import com.entity.Trade;

public class TradeDAOImpl extends BaseDAO<Trade> implements TradeDAO {

	@Override
	public int addTrade(Trade trade) {
		String sql="INSERT INTO trades(user_id,trade_time) VALUES(?,?)";
		return (int)insert(sql,trade.getUserId(),trade.gettradeTime());
	}

	@Override
	public List<Trade> getTradeByUserId(Integer userId) {
		String sql="SELECT trade_id tradeId,user_id userId,trade_time tradeTime FROM trades WHERE user_id=?";
		return queryForList(sql, userId);
	}

}
