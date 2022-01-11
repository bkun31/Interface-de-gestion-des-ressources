package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import exception.DAOException;

public abstract class DAO<T> {
	protected String url;
	protected String user;
	protected String password;

	/**
	 * 
	 * @param url
	 * @param user
	 * @param password
	 */
	protected DAO(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

/**
 * 
 * @param object
 * @return
 * @throws DAOException
 */
	public abstract boolean create(T object) throws DAOException;

/**
 * 
 * @param object
 * @return
 * @throws DAOException
 */
	public abstract boolean delete(T object) throws DAOException;

/**
 * 
 * @param objecT
 * @return
 * @throws DAOException
 */
	public abstract boolean update(T objecT) throws DAOException;

/**
 * 
 * @param id
 * @return
 * @throws DAOException
 */
	public abstract Optional<T> findById(long id) throws DAOException;

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	public abstract List<T> findAll() throws DAOException;
	
	/**
	 * 
	 * @param result
	 * @return
	 * @throws DAOException
	 */
//	public abstract Optional<T> mapping(ResultSet result) throws DAOException;

	/**
	 * 
	 * @param resultSet
	 * @throws DAOException
	 */
	public static void close(ResultSet resultSet) throws DAOException {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new DAOException("Failed to close ResultSet : " + e.getMessage(), e);
			}
		}
	}

	/**
	 * 
	 * @param statement
	 * @throws DAOException
	 */
	public static void close(Statement statement) throws DAOException {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new DAOException("Failed to close ResultSet : " + e.getMessage(), e);
			}
		}
	}

	/**
	 * 
	 * @param connection
	 * @throws DAOException
	 */
	public static void close(Connection connection) throws DAOException {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new DAOException("Failed to close ResultSet : " + e.getMessage(), e);
			}
		}
	}

	/**
	 * 
	 * @param statement
	 * @param connection
	 * @throws DAOException
	 */
	public static void close(Statement statement, Connection connection) throws DAOException {
		close(statement);
		close(connection);
	}

	/**
	 * 
	 * @param resultSet
	 * @param statement
	 * @param connection
	 * @throws DAOException
	 */
	public static void close(ResultSet resultSet, Statement statement, Connection connection) throws DAOException {
		close(resultSet);
		close(statement);
		close(connection);
	}

}
