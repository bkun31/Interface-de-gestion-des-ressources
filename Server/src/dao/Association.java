package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import exception.DAOException;

public class Association {
	protected String url;
	protected String user;
	protected String password;

	/**
	 * @param url
	 * @param username
	 * @param password
	 */
	public Association(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * 
	 * @param userId
	 * @param groupId
	 * @return true if the association has been created
	 * @throws DAOException
	 */
	public boolean createBelong(long userId, long groupId) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("INSERT INTO belong VALUES (?,?);");
			statement.setLong(1, userId);
			statement.setLong(2, groupId);
			success = statement.executeUpdate() == 1;

			DAO.close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to create entry : " + e.getMessage(), e);
		}

		return success;
	}

	/**
	 * 
	 * @param userId
	 * @param threadId
	 * @param messageId
	 * @return true if the association has been created
	 * @throws DAOException
	 */
	public boolean createSend(long userId, long threadId, long messageId) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("INSERT INTO send VALUES (?,?,?);");
			statement.setLong(1, userId);
			statement.setLong(2, threadId);
			statement.setLong(3, messageId);
			success = statement.executeUpdate() == 1;

			DAO.close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to create entry : " + e.getMessage(), e);
		}

		return success;
	}

	/**
	 * 
	 * @param userId
	 * @param messageId
	 * @return true if the association has been created
	 * @throws DAOException
	 */
	public boolean createReceive(long userId, long messageId) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("INSERT INTO message_received VALUES (?,?);");
			statement.setLong(1, userId);
			statement.setLong(2, messageId);
			success = statement.executeUpdate() == 1;

			DAO.close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to create entry : " + e.getMessage(), e);
		}

		return success;
	}

	/**
	 * 
	 * @param userId
	 * @param messageId
	 * @return true if the association has been created
	 * @throws DAOException
	 */
	public boolean createRead(long userId, long messageId) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("INSERT INTO message_read VALUES (?,?);");
			statement.setLong(1, userId);
			statement.setLong(2, messageId);
			success = statement.executeUpdate() == 1;

			DAO.close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to create entry : " + e.getMessage(), e);
		}

		return success;
	}

	/**
	 * 
	 * @param userId
	 * @param groupId
	 * @return true if the association has been deleted
	 * @throws DAOException
	 */
	public boolean deleteBelong(long userId, long groupId) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("DELETE FROM belong WHERE userId = ? AND groupId = ?;");
			statement.setLong(1, userId);
			statement.setLong(2, groupId);

			success = statement.executeUpdate() > 0;
			DAO.close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to delete entry : " + e.getMessage(), e);
		}

		return success;
	}

	/**
	 * 
	 * @param userId
	 * @param threadId
	 * @param messageId
	 * @return true if the association has been deleted
	 * @throws DAOException
	 */
	public boolean deleteSend(long userId, long threadId, long messageId) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("DELETE FROM send WHERE userId = ? AND threadId = ? AND messageId = ?;");
			statement.setLong(1, userId);
			statement.setLong(2, threadId);
			statement.setLong(3, messageId);

			success = statement.executeUpdate() > 0;
			DAO.close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to delete entry : " + e.getMessage(), e);
		}

		return success;
	}

	/**
	 * 
	 * @param userId
	 * @param messageId
	 * @return true if the association has been deleted
	 * @throws DAOException
	 */
	public boolean deleteReceive(long userId, long messageId) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("DELETE FROM message_received WHERE userId = ? AND messageId = ?;");
			statement.setLong(1, userId);
			statement.setLong(2, messageId);

			success = statement.executeUpdate() > 0;
			DAO.close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to delete entry : " + e.getMessage(), e);
		}

		return success;
	}

	/**
	 * 
	 * @param userId
	 * @param messageId
	 * @return true if the association has been deleted
	 * @throws DAOException
	 */
	public boolean deleteRead(long userId, long messageId) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("DELETE FROM message_read WHERE userId = ? AND messageId = ?;");
			statement.setLong(1, userId);
			statement.setLong(2, messageId);

			success = statement.executeUpdate() > 0;
			DAO.close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to delete entry : " + e.getMessage(), e);
		}

		return success;
	}
}
