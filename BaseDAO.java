package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.dao.DAO;
import com.db.ReflectionUtils;
import com.exception.DBException;
import com.web.ConnectionContext;

/**
 * 使用QueryRunner实现DAO
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class BaseDAO<T> implements DAO<T> {
	private QueryRunner queryRunner = null;
	private Class<T> type;

	@SuppressWarnings("unchecked")
	public BaseDAO() {
		queryRunner = new QueryRunner();
		type = ReflectionUtils.getSuperGenericType(getClass());
	}

	// 需要获取生成的id,不能使用QueryRunner
	@Override
	public long insert(String sql, Object... objects) {
		long id = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = ConnectionContext.getInstance().get();
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			if (objects != null) {
				for (int i = 0; i < objects.length; i++) {
					preparedStatement.setObject(i + 1, objects[i]);
				}
			}

			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();

			if (resultSet.next()) {
				id = resultSet.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new DBException("数据库连接异常");
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new DBException("数据库连接异常");
				}
			}
		}
		return id;
	}

	@Override
	public void update(String sql, Object... objects) {
		Connection connection = null;
		try {
			connection = ConnectionContext.getInstance().get();
			queryRunner.update(connection, sql, objects);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	@Override
	public T query(String sql, Object... objects) {
		Connection connection = null;
		try {
			connection = ConnectionContext.getInstance().get();
			return queryRunner.query(connection, sql, new BeanHandler<>(type), objects);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	@Override
	public List<T> queryForList(String sql, Object... objects) {
		Connection connection = null;
		try {
			connection = ConnectionContext.getInstance().get();
			return queryRunner.query(connection, sql, new BeanListHandler<>(type), objects);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getSingleVal(String sql, Object... objects) {
		Connection connection = null;
		try {
			connection = ConnectionContext.getInstance().get();
			return (V) queryRunner.query(connection, sql, new ScalarHandler(), objects);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	@Override
	public void batch(String sql, Object[]... objects) {
		Connection connection = null;
		try {
			connection = ConnectionContext.getInstance().get();
			queryRunner.batch(connection, sql, objects);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

}
