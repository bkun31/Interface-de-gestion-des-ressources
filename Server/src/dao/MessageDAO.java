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

import beans.MessageRepository;
import exception.DAOException;

public class MessageDAO extends DAO<MessageRepository> {

	public MessageDAO(String url, String user, String password) {
		super(url, user, password);
	}

	@Override
	public boolean create(MessageRepository message) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement
					("INSERT INTO message (message_timestamp, text, threadId) VALUES (?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			statement.setTimestamp(1, message.getTimestamp());
			statement.setString(2, message.getText());
			statement.setLong(3, message.getThreadId());
			success = statement.executeUpdate() > 0;

			ResultSet result = statement.getGeneratedKeys();
			if (result.next())
				message.setMessageId(result.getLong(1));
			close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to create entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public boolean delete(MessageRepository message) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("DELETE FROM message WHERE messageId = ?;");
			statement.setLong(1, message.getMessageId());

			success = statement.executeUpdate() > 0;
			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to delete entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public boolean update(MessageRepository message) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection
					.prepareStatement("UPDATE message SET messageId=?, message_timestamp=? , text=? , threadId=?;");
			statement.setLong(1, message.getMessageId());
			statement.setTimestamp(2, message.getTimestamp());
			statement.setString(3, message.getText());
			statement.setLong(4, message.getThreadId());

			success = statement.executeUpdate() > 0;
			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to update entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public Optional<MessageRepository> findById(long id) throws DAOException {
		Optional<MessageRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM message WHERE messageId = ?;");
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
	public List<MessageRepository> findAll() throws DAOException {
		List<MessageRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM message;");

			ResultSet result = statement.executeQuery();

			Optional<MessageRepository> container;
			while ((container = mapping(result)).isPresent())
				list.add(container.get());

			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return list;
	}

	public Optional<MessageRepository> findByUser(long userId) throws DAOException {
		Optional<MessageRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM message msg WHERE EXISTS ( SELECT * FROM send snd WHERE snd.userId = ? AND msg.messageId = snd.messageId);");
			statement.setLong(1, userId);

			ResultSet result = statement.executeQuery();
			container = MessageDAO.mapping(result);

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return container;
	}

	/**
	 * 
	 * @param threadId
	 * @return all messages in the thread
	 * @throws DAOException
	 */
	public List<MessageRepository> findByThread(long threadId) throws DAOException {
		List<MessageRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM message msg WHERE msg.threadId = ? ORDER BY msg.message_timestamp;");
			statement.setLong(1, threadId);

			ResultSet result = statement.executeQuery();
			Optional<MessageRepository> container;
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
	 * @param threadId
	 * @param n
	 * @return the last n messages of the thread
	 * @throws DAOException
	 */
	public List<MessageRepository> findByThread(long threadId, int n) throws DAOException {
		List<MessageRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM message msg WHERE msg.threadId = ? ORDER BY msg.message_timestamp DESC LIMIT ?;");
			statement.setLong(1, threadId);
			statement.setInt(2, n);

			ResultSet result = statement.executeQuery();
			Optional<MessageRepository> container;
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
	 * @param threadId
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	public List<MessageRepository> findByThreadRCV(long threadId, long userId) throws DAOException {
		List<MessageRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM message msg WHERE msg.threadId = ? AND NOT EXISTS (Select * FROM message_received rcv WHERE rcv.userId = ? AND rcv.messageId = msg.messageId AND msg.threadId = ?) ORDER BY msg.message_timestamp DESC;");
			statement.setLong(1, threadId);
			statement.setLong(2, userId);
			statement.setLong(3, threadId);

			ResultSet result = statement.executeQuery();
			Optional<MessageRepository> container;
			while ((container = mapping(result)).isPresent())
				list.add(container.get());

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return list;
	}
	
	public List<MessageRepository> findByThreadRD(long threadId, long userId) throws DAOException {
		List<MessageRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM message msg WHERE msg.threadId = ? AND NOT EXISTS (Select * FROM message_read rd WHERE rd.userId = ? AND rd.messageId = msg.messageId AND msg.threadId = ?) ORDER BY msg.message_timestamp DESC;");
			statement.setLong(1, threadId);
			statement.setLong(2, userId);
			statement.setLong(3, threadId);

			ResultSet result = statement.executeQuery();
			Optional<MessageRepository> container;
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
	 * @return the message bean model from the database
	 * @throws DAOException
	 */
	public static Optional<MessageRepository> mapping(ResultSet result) throws DAOException {
		MessageRepository message = null;
		try {
			if (result.next()) {
				message = new MessageRepository();
				message.setMessageId(result.getLong("messageId"));
				message.setTimestamp(result.getTimestamp("message_timestamp"));
				message.setText(result.getString("text"));
				message.setThreadId(result.getLong("threadId"));
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return Optional.ofNullable(message);
	}
}
