package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import beans.GroupRepository;
import beans.MessageRepository;
import beans.ThreadRepository;
import beans.UserRepository;
import dao.Association;
import dao.DiscussionThreadDAO;
import dao.GroupDAO;
import dao.MessageDAO;
import dao.UserDAO;
import dao.Utility;
import exception.DAOException;
import streamdata.DataBuilder;
import streamdata.DiscussionThread;
import streamdata.Group;
import streamdata.Message;
import streamdata.Status;
import streamdata.User;

public class Database {
	private static String URL;
	private static String USER;
	private static String PASSWORD;

	private UserDAO userDAO;
	private GroupDAO groupDAO;
	private DiscussionThreadDAO threadDAO;
	private MessageDAO messageDAO;
	private Association association;
	private Utility utility;

	public Database() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		this.userDAO = new UserDAO(URL, USER, PASSWORD);
		this.groupDAO = new GroupDAO(URL, USER, PASSWORD);
		this.threadDAO = new DiscussionThreadDAO(URL, USER, PASSWORD);
		this.messageDAO = new MessageDAO(URL, USER, PASSWORD);
		this.association = new Association(URL, USER, PASSWORD);
		this.utility = new Utility(URL, USER, PASSWORD);
	}

	/**
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @throws DAOException
	 */
	public static void loginDataBase(String url, String user, String password) throws DAOException {
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			URL = url;
			USER = user;
			PASSWORD = password;
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}
	}

	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @return the groupDAO
	 */
	public GroupDAO getGroupDAO() {
		return groupDAO;
	}

	/**
	 * @return the discussionTreadDAO
	 */
	public DiscussionThreadDAO getDiscussionTreadDAO() {
		return threadDAO;
	}

	/**
	 * @return the messageDAO
	 */
	public MessageDAO getMessageDAO() {
		return messageDAO;
	}

	/**
	 * @return the association
	 */
	public Association getAssociation() {
		return association;
	}

	/**
	 * @return the utility
	 */
	public Utility getUtility() {
		return utility;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return the user who logged in
	 */
	public Optional<User> login(String username, String password) {
		Optional<User> container = Optional.empty();
		try {
			Optional<UserRepository> containerDB = userDAO.findByUsernamePassword(username, password);
			if (containerDB.isPresent())
				container = Optional.of(DataBuilder.toUser(containerDB.get()));
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return container;
	}

	/**
	 * 
	 * @param user
	 * @param groups
	 * @return true if the registration was successful
	 */
	public boolean registerUser(UserRepository user, List<GroupRepository> groups) {

		boolean success = false;
		try {
			success = userDAO.create(user);
			if (success) {
				for (GroupRepository group : groups) {
					success &= association.createBelong(user.getUserId(), group.getGroupId());
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * 
	 * @param user
	 * @param groups
	 * @return true if the registration was successful
	 */
	public boolean registerGroup(GroupRepository group, List<UserRepository> users) {

		boolean success = false;
		try {
			success = groupDAO.create(group);
			if (success) {
				for (UserRepository user : users) {
					success &= association.createBelong(user.getUserId(), group.getGroupId());
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * 
	 * @param user
	 * @return true if the deletion was successful
	 */
	public boolean deleteUser(UserRepository user) {
		try {
			return userDAO.delete(user);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param group
	 * @return true if the deletion was successful
	 */
	public boolean deleteGroup(GroupRepository group) {
		try {
			return groupDAO.delete(group);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param newDiscussionThread
	 * @param message
	 * @return if the creation of the thread was successful
	 */
	public boolean newThread(ThreadRepository newDiscussionThread, MessageRepository message) {
		boolean success = false;
		try {
			success = threadDAO.create(newDiscussionThread);
			if (success) {
				message.setThreadId(newDiscussionThread.getThreadId());
				success = messageDAO.create(message)
						&& association.createSend(newDiscussionThread.getUserId(), newDiscussionThread.getThreadId(),
								message.getMessageId())
						&& association.createReceive(newDiscussionThread.getUserId(), message.getMessageId())
						&& association.createRead(newDiscussionThread.getUserId(), message.getMessageId());
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return success;

	}

	/**
	 * 
	 * @param userId
	 * @param message
	 * @return
	 */
	public boolean sendMessage(long userId, MessageRepository message) {
		try {
			return messageDAO.create(message)
					&& association.createSend(userId, message.getThreadId(), message.getMessageId())
					&& association.createReceive(userId, message.getMessageId())
					&& association.createRead(userId, message.getMessageId());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param threadId
	 * @return
	 */
	public List<Message> getMessages(long threadId) {
		List<Message> list = new ArrayList<>();
		try {
			List<MessageRepository> listDB = messageDAO.findByThread(threadId);

			for (MessageRepository messageDB : listDB) {
				Optional<UserRepository> container = userDAO.findByMessage(messageDB.getMessageId());
				User sender;

				if (container.isPresent())
					sender = DataBuilder.toUser(container.get());
				else {
					sender = new User();
					sender.setUserId(0);
					sender.setName("old");
					sender.setFirstname("user");
				}

				Map<Status, List<User>> userStatusMap = new TreeMap<>();
				List<UserRepository> partcipants = userDAO.findByThread(threadId);

				for (UserRepository userDB : partcipants) {
					User user = DataBuilder.toUser(userDB);
					Status userStatus = utility.getStatusUser(userDB.getUserId(), messageDB.getMessageId()).get();
					userStatusMap.computeIfAbsent(userStatus, key -> new ArrayList<User>());
					userStatusMap.get(userStatus).add(user);
				}

				Status status = userStatusMap.containsKey(Status.PENDING) ? Status.PENDING
						: userStatusMap.containsKey(Status.RECEIVED) ? Status.RECEIVED : Status.READ;

				list.add(DataBuilder.toMessage(messageDB, sender, status, userStatusMap));
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @param service
	 * @return
	 */
	public List<Group> getGroupsNewThread(boolean service) {
		List<Group> list = new ArrayList<>();
		try {

			List<GroupRepository> listDB = groupDAO.findByService(!service);

			for (GroupRepository groupDB : listDB) {
				list.add(DataBuilder.toGroup(groupDB));
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<DiscussionThread> getThreads(long userId) {
		List<DiscussionThread> list = new ArrayList<>();
		try {
			List<ThreadRepository> listDB = threadDAO.findByUser(userId);
			for (ThreadRepository threadDB : listDB) {
				int unreadMessages = utility.unreadMessages(userId, threadDB.getThreadId());
				Timestamp timestamp = utility.getTsLastMessage(threadDB.getThreadId());

				list.add(DataBuilder.toDiscussionThread(threadDB, unreadMessages, timestamp));
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @param threadId
	 * @return
	 */
	public DiscussionThread getThread(long userId, long threadId) {
		DiscussionThread thread = null;
		try {
			ThreadRepository threadDB = threadDAO.findById(threadId).get();

			int unreadMessages = utility.unreadMessages(userId, threadDB.getThreadId());
			Timestamp timestamp = utility.getTsLastMessage(threadDB.getThreadId());

			thread = DataBuilder.toDiscussionThread(threadDB, unreadMessages, timestamp);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return thread;
	}

	/**
	 * 
	 * @param threadId
	 * @return
	 */
	public Group getGroupByThread(long threadId) {
		try {
			return DataBuilder.toGroup(groupDAO.findByThread(threadId).get());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param userId
	 * @param threadId
	 * @return
	 */
	public int unreadMessages(long userId, long threadId) {
		try {
			return utility.unreadMessages(userId, threadId);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Optional<User> getUser(long userId) {
		Optional<User> container = Optional.empty();
		try {
			Optional<UserRepository> containerDB = userDAO.findById(userId);
			if (containerDB.isPresent())
				container = Optional.of(DataBuilder.toUser(containerDB.get()));
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return container;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public UserRepository getUserDB(long userId) {
		try {
			return userDAO.findById(userId).get();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param threadId
	 * @return
	 */
	public ThreadRepository getThread(long threadId) {
		try {
			return threadDAO.findById(threadId).get();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param groupId
	 * @return
	 */
	public GroupRepository getGroup(long groupId) {
		try {
			return groupDAO.findById(groupId).get();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<UserRepository> getUserByThread(long threadId) {
		try {
			return userDAO.findByThread(threadId);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * 
	 * @param userId
	 * @return list id thread update
	 */
	public List<Long> receivedAll(long userId) {
		try {
			return utility.receivedMesageUpdate(userId);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	public List<Long> received(long userId, List<Long> threadIdList) {
		List<Long> threadUpdated = new ArrayList<>();
		try {
			for (long threadId : threadIdList) {
				if (utility.receivedMesageUpdate(userId, threadId))
					threadUpdated.add(threadId);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return threadUpdated;
	}

	/**
	 * 
	 * @param userId
	 * @param threadId
	 * @return
	 */
	public boolean read(long userId, long threadId) {
		try {
			return utility.readMesageUpdate(userId, threadId);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
