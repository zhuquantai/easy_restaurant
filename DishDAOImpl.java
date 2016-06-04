package com.dao.impl;

import java.util.Collection;
import java.util.List;

import com.dao.DishDAO;
import com.entity.CriteriaDish;
import com.entity.Dish;
import com.entity.ShoppingCartItem;
import com.web.Page;

public class DishDAOImpl extends BaseDAO<Dish> implements DishDAO {

	@Override
	public List<Dish> getAllDishes() {
		String sql = "SELECT dish_id id,dish_name name,price,description,image,storeNumber,salesAmount "
				+ "FROM dishes";
		return queryForList(sql);
	}

	@Override
	public List<Dish> getDishes(CriteriaDish cd) {
		String sql = "SELECT dish_id id,dish_name name,price,description,image,storeNumber,salesAmount "
				+ "FROM dishes " + "WHERE price BETWEEN ? AND ?";
		return queryForList(sql, cd.getMinPrice(), cd.getMaxPrice());
	}

	@Override
	public void addDish(Dish dish) {
		String sql = "INSERT INTO dishes(dish_name,price,description,image,storeNumber,salesAmount) "
				+ "VALUES(?,?,?,?,?,?)";
		update(sql, dish.getName(), dish.getPrice(), dish.getDescription(), dish.getImage(), dish.getStoreNumber(),
				dish.getSalesAmount());
	}

	@Override
	public void editDish(String name, Dish dish) {
		String sql = "UPDATE dishes SET dish_name=?,price=?,description=?,image=?,storeNumber=?,salesAmount=? "
				+ "WHERE dish_name=?";
		update(sql, dish.getName(), dish.getPrice(), dish.getDescription(), dish.getImage(), dish.getStoreNumber(),
				dish.getSalesAmount(), name);
	}

	@Override
	public void delDish(int id) {
		String sql = "DELETE FROM dishes " + "WHERE dish_id=?";
		update(sql, id);
	}

	@Override
	public List<Dish> getDishsOrderBy(String orderName, String orderType) {
		String sql = "SELECT dish_id id,dish_name name,price,description,image,storeNumber,salesAmount "
				+ "FROM dishes " + "ORDER BY ? ?";
		return queryForList(sql, orderName, orderType);
	}

	@Override
	public long getTotalDishNumber(CriteriaDish cd) {
		if (cd.getName() != null) {
			String sql = "SELECT count(dish_id) FROM dishes WHERE dish_name LIKE ? OR name_index LIKE ? ";
			String name = "%" + cd.getName() + "%";
			return getSingleVal(sql, name, name);
		} else {
			String sql = "SELECT count(dish_id) " + "FROM dishes " + "WHERE price BETWEEN ? AND ?";
			return getSingleVal(sql, cd.getMinPrice(), cd.getMaxPrice());
		}
	}

	@Override
	public Page<Dish> getPage(CriteriaDish cd) {
		Page<Dish> page = new Page<>(cd.getPageNum());
		page.setTotalItemNum((int) getTotalDishNumber(cd));
		page.setList(getPageList(cd, 5));
		return page;
	}

	/**
	 * MYSQL使用LIMIT进行分页，其fromIndex从0开始
	 * 
	 * @param pageSize:每页显示数量
	 */
	@Override
	public List<Dish> getPageList(CriteriaDish cd, int pageSize) {
		// 如果是按名称查询
		if (cd.getName() != null) {
			String sql = "SELECT dish_id id,dish_name name,price,description,image,storeNumber,salesAmount "
					+ "FROM dishes " + "WHERE dish_name LIKE ? OR name_index LIKE ? LIMIT ?,?";
			String name = "%" + cd.getName() + "%";
			return queryForList(sql, name, name, (cd.getPageNum() - 1) * pageSize, pageSize);
		} else {
			String sql = "SELECT dish_id id,dish_name name,price,description,image,storeNumber,salesAmount "
					+ "FROM dishes " + "WHERE price BETWEEN ? AND ? LIMIT ?,?";
			return queryForList(sql, cd.getMinPrice(), cd.getMaxPrice(), (cd.getPageNum() - 1) * pageSize, pageSize);
			// 如果按价格查询
		}
	}

	@Override
	public int getStoreNumber(Integer id) {
		String sql = "SELECT storenumber " + "FROM dishes " + "WHERE dish_id=? ";
		return getSingleVal(sql, id);
	}

	@Override
	public Dish getDish(int id) {
		String sql = "SELECT dish_id id,dish_name name,price,description,image,storeNumber,salesAmount "
				+ "FROM dishes " + "WHERE dish_id=? ";
		return query(sql, id);
	}

	@Override
	public void batchUpdateSalesAmountAndStoreNumber(Collection<ShoppingCartItem> items) {
		String sql = "UPDATE dishes SET storeNumber=?,salesAmount=? " + "WHERE dish_id=?";

		// 获取批处理的参数,是一个二维数组
		Object[][] objects = new Object[items.size()][3];
		int i = 0;
		for (ShoppingCartItem item : items) {
			int amount = item.getDish().getSalesAmount();
			objects[i][0] = amount - item.getQuantity();
			objects[i][1] = amount + item.getQuantity();
			objects[i][2] = item.getDish().getId();
			i++;
		}
		batch(sql, objects);
	}

	@Override
	public Dish getDish(String name) {
		String sql = "SELECT dish_id id,dish_name name,price,description,image,storeNumber,salesAmount "
				+ "FROM dishes " + "WHERE dish_name=? ";
		return query(sql, name);
	}

}
