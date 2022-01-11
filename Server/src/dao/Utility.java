package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import beans.ThreadRepository;
import beans.MessageRepository;
import exception.DAOException;
import streamdata.Status;

public class Utility {
	private String url;
	private String user;
	private String password;

	/**
	 * 
	 * @param url
	 * @param user
	 * @param password
	 */
	public Utility(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * 
	 * @param userId
	 * @param threadId
	 * @return
	 * @throws DAOException
	 */
	public int unreadMessages(long userId, long threadId) throws DAOException {
		int count = 0;

		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT (SELECT COUNT(*) FROM message msg WHERE msg.threadId = ?) - (SELECT COUNT(*) FROM message_read rd WHERE EXISTS (SELECT * FROM message msg WHERE msg.threadId = ? AND rd.userId = ? AND msg.messageId = rd.messageId));");
			statement.setLong(1, threadId);
			statement.setLong(2, threadId);
			statement.setLong(3, userId);

			ResultSet result = statement.executeQuery();
			if (result.next())
				count = result.getInt(1);

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	/**
	 * 
	 * @param userId
	 * @param threadId
	 * @return
	 * @throws DAOException
	 */
	public int unreceivedMessages(long userId, long threadId) throws DAOException {
		int count = 0;

		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT (SELECT COUNT(*) FROM message msg WHERE msg.threadId = ?) - (SELECT COUNT(*) FROM message_received rcv WHERE EXISTS (SELECT * FROM message msg WHERE msg.threadId = ? AND rcv.userId = ? AND msg.messageId = rcv.messageId));");
			statement.setLong(1, threadId);
			statement.setLong(2, threadId);
			statement.setLong(3, userId);

			ResultSet result = statement.executeQuery();
			if (result.next())
				count = result.getInt(1);

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	/**
	 * 
	 * @param userId
	 * @return the list of discussion threads identifiers that have been updated
	 * @throws DAOException
	 */
	public List<Long> receivedMesageUpdate(long userId) throws DAOException {
		DiscussionThreadDAO threadDAO = new DiscussionThreadDAO(url, user, password);
		MessageDAO messageDAO = new MessageDAO(url, user, password);
		Association association = new Association(url, user, password);
		List<Long> updated = new ArrayList<>();

		List<ThreadRepository> threads = threadDAO.findByUser(userId);

		for (ThreadRepository thread : threads) {
			int unreceived = unreceivedMessages(userId, thread.getThreadId());
			if (unreceived > 0) {
				updated.add(thread.getThreadId());
				List<MessageRepository> messages = messageDAO.findByThreadRCV(thread.getThreadId(), userId);

				for (MessageRepository message : messages) {
					association.createReceive(userId, message.getMessageId());
				}
			}
		}

		return updated;
	}
	
	/**
	 * 
	 * @param userId
	 * @param threadId
	 * @return
	 * @throws DAOException
	 */
	public boolean receivedMesageUpdate(long userId, long threadId) throws DAOException {
		MessageDAO messageDAO = new MessageDAO(url, user, password);
		Association association = new Association(url, user, password);

		int unreceived = unreceivedMessages(userId, threadId);
		boolean updated = unreceived > 0;
		List<MessageRepository> messages = messageDAO.findByThreadRCV(threadId, userId);

		for (MessageRepository message : messages) {
			association.createReceive(userId, message.getMessageId());
		}

		return updated;
	}

	/**
	 * 
	 * @param userId
	 * @param threadId
	 * @return true if the thread has been updated
	 * @throws DAOException
	 */
	public boolean readMesageUpdate(long userId, long threadId) throws DAOException {
		MessageDAO messageDAO = new MessageDAO(url, user, password);
		Association association = new Association(url, user, password);

		int unread = unreadMessages(userId, threadId);
		boolean updated = unread > 0;
		List<MessageRepository> messages = messageDAO.findByThreadRD(threadId, userId);

		for (MessageRepository message : messages) {
			association.createRead(userId, message.getMessageId());
		}

		return updated;
	}

	/**
	 * 
	 * @param messageId
	 * @return status global of message
	 * @throws DAOException
	 */
	public Optional<Status> getStatusMessage(long messageId) throws DAOException {
		Status status = null;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"Select (SELECT COUNT(DISTINCT usr.userId) FROM user_account usr JOIN message msg ON msg.messageId = ? JOIN discussion_thread thrd ON thrd.threadId = msg.threadId JOIN belong blg ON blg.userId = usr.userId WHERE thrd.groupId = blg.groupId OR thrd.userId = usr.userId) AS total, (SELECT COUNT(rd.messageId) FROM message_read rd JOIN user_account usr ON rd.userId = usr.userId WHERE rd.messageId = ?) AS readCount, (SELECT COUNT(rcv.messageId) FROM message_received rcv JOIN user_account usr ON rcv.userId = usr.userId WHERE rcv.messageId = ?) AS receivedCount;");
			statement.setLong(1, messageId);
			statement.setLong(2, messageId);;
			statement.setLong(3, messageId);

			ResultSet result = statement.executeQuery();
			result.next();
			int total = result.getInt("total");
			int read = result.getInt("readCount");
			int receive = result.getInt("receivedCount");
			
			status = total - read == 0 ? Status.READ : total - receive == 0 ? Status.RECEIVED : Status.PENDING;

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Optional.of(status);
	}

	/**
	 * 
	 * @param userId
	 * @param messageId
	 * @return status of the user's message 
	 * @throws DAOException
	 */
	public Optional<Status> getStatusUser(long userId, long messageId) throws DAOException {
		Status status = null;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT (SELECT EXISTS(SELECT * FROM message_read rd WHERE rd.userId = ? AND rd.messageId = ?)) AS rd, (SELECT EXISTS(SELECT * FROM message_received rcv WHERE rcv.userId = ? AND rcv.messageId = ?)) AS rcv;");
			statement.setLong(1, userId);
			statement.setLong(2, messageId);
			statement.setLong(3, userId);
			statement.setLong(4, messageId);

			ResultSet result = statement.executeQuery();
			result.next();
			status = result.getBoolean("rd") ? Status.READ
					: result.getBoolean("rcv") ? Status.RECEIVED : Status.PENDING;

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Optional.of(status);
	}
	
	public Timestamp getTsLastMessage(long threadId) throws DAOException {
		Timestamp timestamp = null;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT message_timestamp FROM message WHERE threadId = ? ORDER BY message_timestamp DESC LIMIT 1;");
			statement.setLong(1, threadId);

			ResultSet result = statement.executeQuery();
			if(result.next())
				timestamp = result.getTimestamp("message_timestamp");

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return timestamp;
	}

}
