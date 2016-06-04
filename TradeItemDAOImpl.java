package com.dao.impl;

import java.util.Collection;
import java.util.List;
import com.dao.TradeItemDAO;
import com.entity.TradeItem;

public class TradeItemDAOImpl extends BaseDAO<TradeItem> implements TradeItemDAO {

	@Override
	public void batchSave(Collection<TradeItem> tradeItems) {
		String sql = "INSERT INTO trade_items(dish_id,quantity,trade_id) VALUES(?,?,?)";
		Object[][] objects = new Object[tradeItems.size()][3];
		int i = 0;
		for (TradeItem tradeItem : tradeItems) {
			objects[i][0] = tradeItem.getDishId();
			objects[i][1] = tradeItem.getQuantity();
			objects[i][2] = tradeItem.getTradeId();
			i++;
		}

		batch(sql, objects);
	}

	@Override
	public List<TradeItem> getTraedItemWithTradeId(Integer tradeId) {
		String sql = "SELECT item_id itemId,dish_id dishId,quantity,trade_id tradeId FROM trade_items WHERE trade_id=?";
		return queryForList(sql, tradeId);
	}

}
