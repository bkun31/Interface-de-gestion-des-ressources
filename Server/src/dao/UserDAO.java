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

import beans.UserRepository;
import exception.DAOException;

public class UserDAO extends DAO<UserRepository> {

	public UserDAO(String url, String user, String password) {
		super(url, user, password);
	}

	@Override
	public boolean create(UserRepository usr) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO user_account (username, password, name, firstname, campus_user) VALUES (?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, usr.getUsername());
			statement.setString(2, usr.getPassword());
			statement.setString(3, usr.getName());
			statement.setString(4, usr.getFirstname());
			statement.setBoolean(5, usr.isCampusUser());
			success = statement.executeUpdate() > 0;

			ResultSet result = statement.getGeneratedKeys();
			if (result.next())
				usr.setUserId(result.getLong(1));
			close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to create entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public boolean delete(UserRepository usr) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("DELETE FROM user_account WHERE userId = ?;");
			statement.setLong(1, usr.getUserId());

			success = statement.executeUpdate() > 0;
			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to delete entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public boolean update(UserRepository usr) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"UPDATE user_account SET username=?, password=? ,name=? , firstname=?, campus_user=? WHERE userId = ?");
			statement.setString(1, usr.getUsername());
			statement.setString(2, usr.getPassword());
			statement.setString(3, usr.getName());
			statement.setString(4, usr.getFirstname());
			statement.setBoolean(5, usr.isCampusUser());
			statement.setLong(6, usr.getUserId());

			success = statement.executeUpdate() > 0;
			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to update entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public Optional<UserRepository> findById(long id) throws DAOException {
		Optional<UserRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_account WHERE userId = ?;");
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
	public List<UserRepository> findAll() throws DAOException {
		List<UserRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_account;");

			ResultSet result = statement.executeQuery();

			Optional<UserRepository> container;
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
	 * @param username
	 * @return
	 * @throws DAOException
	 */
	public Optional<UserRepository> findByUsername(String username) throws DAOException {
		Optional<UserRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_account WHERE username = ?;");
			statement.setString(1, username);

			ResultSet result = statement.executeQuery();

			container = mapping(result);

			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return container;
	}

	public Optional<UserRepository> findByUsernamePassword(String username, String passwd) throws DAOException {
		Optional<UserRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM user_account WHERE username = ? AND password = ?;");
			statement.setString(1, username);
			statement.setString(2, passwd);

			ResultSet result = statement.executeQuery();

			container = mapping(result);

			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return container;
	}

	/**
	 * 
	 * @param messageId
	 * @return
	 * @throws DAOException
	 */
	public Optional<UserRepository> findByMessage(long messageId) throws DAOException {
		Optional<UserRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT DISTINCT usr.* FROM user_account usr JOIN send snd ON snd.userId = usr.userId WHERE snd.messageId = ?; ");
			statement.setLong(1, messageId);

			ResultSet result = statement.executeQuery();
			container = UserDAO.mapping(result);

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return container;
	}

	/**
	 * 
	 * @param threadId
	 * @return the list of participants
	 * @throws DAOException
	 */
	public List<UserRepository> findByThread(long threadId) throws DAOException {
		List<UserRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement
					("SELECT DISTINCT usr.* FROM user_account usr JOIN "
							+ "discussion_thread thrd ON thrd.threadId = ? "
							+ "JOIN belong blg ON blg.userId = usr.userId WHERE "
							+ "thrd.userId = usr.userId OR blg.groupId = thrd.groupId;");
			statement.setLong(1, threadId);

			ResultSet result = statement.executeQuery();

			Optional<UserRepository> container;
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
	 * @param groupId
	 * @return the list of members
	 * @throws DAOException
	 */
	public List<UserRepository> findByGroup(long groupId) throws DAOException {
		List<UserRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM user_account usr WHERE EXISTS ( SELECT * FROM belong blg WHERE blg.groupId = ? AND usr.userId = blg.userId);");
			statement.setLong(1, groupId);

			ResultSet result = statement.executeQuery();
			Optional<UserRepository> container;
			while ((container = mapping(result)).isPresent())
				list.add(container.get());

			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return list;
	}
	
	public List<UserRepository> findByService(boolean service) throws DAOException {
		List<UserRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM user_account WHERE campus_user = ?;");
			statement.setBoolean(1, service);

			ResultSet result = statement.executeQuery();
			Optional<UserRepository> container;
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
	 * @return the user bean model from the database
	 * @throws DAOException
	 */
	public static Optional<UserRepository> mapping(ResultSet result) throws DAOException {
		UserRepository user = null;
		try {
			if (result.next()) {
				user = new UserRepository();
				user.setUserId(result.getLong("userId"));
				user.setUsername(result.getString("username"));
				user.setPassword(result.getString("password"));
				user.setName(result.getString("name"));
				user.setFirstname(result.getString("firstname"));
				user.setCampusUser(result.getBoolean("campus_user"));
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return Optional.ofNullable(user);
	}
}
