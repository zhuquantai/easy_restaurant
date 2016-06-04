package com.dao.impl;

import java.util.List;

import com.dao.UserDAO;
import com.entity.User;

public class SuperUserDAOImpl extends BaseDAO<User> implements UserDAO {

	@Override
	public User getUser(String name) {
		String sql = "SELECT user_id id,name userName,password,balance,sex,telephone,address,description FROM superusers WHERE name=?";
		return query(sql, name);
	}

	@Override
	public String getPassword(String name) {
		String sql = "SELECT password FROM superusers WHERE name=?";
		return getSingleVal(sql, name);
	}

	@Override
	public long addUser(User user) {
		String sql = "INSERT INTO superusers(name,password,balance,sex,telephone,address,description) VALUES(?,?,?,?,?,?,?)";
		long id=insert(sql, user.getUserName(), user.getPassword(),user.getBalance(), user.getSex(), user.getTelephone(), user.getAddress(),
				user.getdescription());
		return id;
	}

	@Override
	public void delUser(int id) {
		String sql="DELETE FROM superusers WHERE user_id=?";
		update(sql, id);
	}

	@Override
	public void updateUser(String name, User user) {
		String sql="UPDATE superusers SET name=?,password=?,balance=?,sex=?,telephone=?,address=?,description=? WHERE name=?";
		update(sql, user.getUserName(),user.getPassword(),user.getBalance(),user.getSex(),user.getTelephone(),user.getAddress(),user.getAddress(),name);
	}

	@Override
	public void updateBalance(int id, double balance) {
		String sql="UPDATE superusers SET balance=? WHERE user_id=?";
		update(sql, id,balance);
	}

	@Override
	public List<User> getUsers() {
		return null;
	}
	

}
