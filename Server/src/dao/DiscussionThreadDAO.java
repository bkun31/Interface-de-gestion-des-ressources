package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import beans.ThreadRepository;
import exception.DAOException;

public class DiscussionThreadDAO extends DAO<ThreadRepository> {

	public DiscussionThreadDAO(String url, String user, String password) {
		super(url, user, password);
	}

	@Override
	public boolean create(ThreadRepository discussionThread) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement
					("INSERT INTO discussion_thread (title, groupId, userId) VALUES (?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, discussionThread.getTitle());
			statement.setLong(2, discussionThread.getGroupId());
			statement.setLong(3, discussionThread.getUserId());
			success = statement.executeUpdate() > 0;

			ResultSet result = statement.getGeneratedKeys();
			if (result.next())
				discussionThread.setThreadId(result.getLong(1));
			close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to create entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public boolean delete(ThreadRepository discussionThread) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection
					.prepareStatement("DELETE FROM discussion_thread WHERE threadId = ?;");
			statement.setLong(1, discussionThread.getThreadId());

			success = statement.executeUpdate() > 0;
			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to delete entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public boolean update(ThreadRepository discussionThread) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection
					.prepareStatement("UPDATE discussion_thread SET threadId=?, title=?, groupId=?, userId=?;");
			statement.setLong(1, discussionThread.getThreadId());
			statement.setString(2, discussionThread.getTitle());
			statement.setLong(3, discussionThread.getGroupId());
			statement.setLong(4, discussionThread.getUserId());

			success = statement.executeUpdate() > 0;
			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to update entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public Optional<ThreadRepository> findById(long id) throws DAOException {
		Optional<ThreadRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM discussion_thread WHERE threadId = ?;");
			statement.setLong(1, id);

			ResultSet result = statement.executeQuery();

			container = mapping(result);

			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return container;
	}

	@Override
	public List<ThreadRepository> findAll() throws DAOException {
		List<ThreadRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM discussion_thread;");

			ResultSet result = statement.executeQuery();

			Optional<ThreadRepository> container;
			while ((container = mapping(result)).isPresent())
				list.add(container.get());

			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return list;
	}

	/**
	 * 
	 * @param messageId
	 * @return the thread of the message
	 * @throws DAOException
	 */
	public Optional<ThreadRepository> findByMessage(long messageId) throws DAOException {
		Optional<ThreadRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM discussion_thread thrd WHERE EXISTS ( SELECT * FROM message msg WHERE msg.messageId = ? AND msg.threadId "
					+ "= thrd.threadId);");
			statement.setLong(1, messageId);

			ResultSet result = statement.executeQuery();
			container = mapping(result);

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return container;
	}

	/**
	 * 
	 * @param userId
	 * @return the list of threads of the user
	 * @throws DAOException
	 */
	public List<ThreadRepository> findByUser(long userId) throws DAOException {
		List<ThreadRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT thrd.*, MAX(msg.message_timestamp) FROM discussion_thread "
					+ "thrd JOIN message msg ON msg.threadId = thrd.threadId WHERE EXISTS (SELECT * FROM belong blg "
					+ "WHERE thrd.userId = ? OR (blg.userId = ? AND thrd.groupId = blg.groupId)) GROUP BY thrd.threadId ORDER BY MAX(msg.message_timestamp) DESC");
			statement.setLong(1, userId);
			statement.setLong(2, userId);

			ResultSet result = statement.executeQuery();
			Optional<ThreadRepository> container;
			while ((container = mapping(result)).isPresent())
				list.add(container.get());

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return list;
	}

	/**
	 * 
	 * @param userId
	 * @return the list of threads created by the user
	 * @throws DAOException
	 */
	public List<ThreadRepository> findByCreator(long userId) throws DAOException {
		List<ThreadRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT  thrd.*, MAX(msg.message_timestamp) FROM discussion_thread thrd JOIN message msg ON msg.threadId ="
					+ " thrd.threadId WHERE thrd.userId = ? GROUP BY thrd.threadId ORDER BY MAX(msg.message_timestamp) DESC;");
			statement.setLong(1, userId);

			ResultSet result = statement.executeQuery();
			Optional<ThreadRepository> container;
			while ((container = mapping(result)).isPresent())
				list.add(container.get());

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return list;
	}

	/**
	 * 
	 * @param groupId
	 * @return the list of threads of the group
	 * @throws DAOException
	 */
	public List<ThreadRepository> findByGroup(long groupId) throws DAOException {
		List<ThreadRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT DISTINCT thrd.*,MAX(msg.message_timestamp) FROM discussion_thread thrd JOIN message msg ON msg.threadId "
					+ "= thrd.threadId WHERE thrd.groupId = ? GROUP BY thrd.threadId ORDER BY MAX(msg.message_timestamp) DESC;");
			statement.setLong(1, groupId);

			ResultSet result = statement.executeQuery();
			Optional<ThreadRepository> container;
			while ((container = mapping(result)).isPresent())
				list.add(container.get());

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return list;
	}

	/**
	 * 
	 * @param result
	 * @return the thread bean model from the database
	 * @throws DAOException
	 */
	public static Optional<ThreadRepository> mapping(ResultSet result) throws DAOException {
		ThreadRepository discussionThread = null;
		try {
			if (result.next()) {
				discussionThread = new ThreadRepository();
				discussionThread.setThreadId(result.getLong("threadId"));
				discussionThread.setTitle(result.getString("title"));
				discussionThread.setGroupId(result.getLong("groupId"));
				discussionThread.setUserId(result.getLong("userId"));
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return Optional.ofNullable(discussionThread);
	}

}
