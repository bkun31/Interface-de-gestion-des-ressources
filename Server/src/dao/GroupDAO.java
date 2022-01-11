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

import beans.GroupRepository;
import exception.DAOException;

public class GroupDAO extends DAO<GroupRepository> {
	
	public GroupDAO(String url, String username, String password) {
		super(url, username, password);
	}
	
	@Override
	public boolean create(GroupRepository group) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement
					("INSERT INTO user_group (groupname, campus_group) VALUES (?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, group.getGroupName());
			statement.setBoolean(2, group.isCampus_group());
			success = statement.executeUpdate() > 0;

			ResultSet result = statement.getGeneratedKeys();
			if (result.next())
				group.setGroupId(result.getLong(1));
			close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to create entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public boolean delete(GroupRepository group) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("DELETE FROM user_group WHERE groupId = ?;");
			statement.setLong(1, group.getGroupId());

			success = statement.executeUpdate() > 0;
			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to delete entry : " + e.getMessage(), e);
		}

		return success;
	}

	@Override
	public boolean update(GroupRepository group) throws DAOException {
		boolean success = false;
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("UPDATE user_group SET groupName=?, campus_group=? WHERE groupId=?;");
			statement.setString(1, group.getGroupName());
			statement.setBoolean(2, group.isCampus_group());
			statement.setLong(3, group.getGroupId());

			success = statement.executeUpdate() > 0;
			close(statement, connection);
		} catch (SQLException e) {
			throw new DAOException("Failed to update entry : " + e.getMessage(), e);
		}

		return success;
	}
	
	@Override
	public Optional<GroupRepository> findById(long id) throws DAOException {
		Optional<GroupRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_group WHERE groupId = ?;");
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
	public List<GroupRepository> findAll() throws DAOException {
		List<GroupRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_group grp ORDER BY grp.groupname;");

			ResultSet result = statement.executeQuery();

			Optional<GroupRepository> container;
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
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	public List<GroupRepository> findByUser(long userId) throws DAOException {
		List<GroupRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM user_group grp WHERE EXISTS ( SELECT * FROM belong blg WHERE blg.userid = ? AND grp.groupid = blg.groupid) ORDER BY grp.groupname;");
			statement.setLong(1, userId);

			ResultSet result = statement.executeQuery();
			Optional<GroupRepository> container;
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
	 * @param service
	 * @return the list of groups that are not in service
	 * @throws DAOException
	 */
	public List<GroupRepository> findByService(boolean service) throws DAOException {
		List<GroupRepository> list = new ArrayList<>();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM user_group grp WHERE grp.campus_group = ? ORDER BY grp.groupname;");
			statement.setBoolean(1, service);

			ResultSet result = statement.executeQuery();
			Optional<GroupRepository> container;
			while ((container = mapping(result)).isPresent()) {
				list.add(container.get());
			}
			DAO.close(result, statement, connection);
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param threadId
	 * @return
	 * @throws DAOException
	 */
	public Optional<GroupRepository> findByThread(long threadId) throws DAOException {
		Optional<GroupRepository> container = Optional.empty();
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = connection.prepareStatement
					("SELECT grp.* FROM user_group grp JOIN discussion_thread thrd ON grp.groupid = thrd.groupid WHERE thrd.threadid = ?; ");
			statement.setLong(1, threadId);

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
	 * @param result
	 * @return the group bean model from the database
	 * @throws DAOException
	 */
	public static Optional<GroupRepository> mapping(ResultSet result) throws DAOException {
		GroupRepository group = null;
		try {
			if (result.next()) {
				group = new GroupRepository();
				group.setGroupId(result.getLong("groupId"));
				group.setGroupName(result.getString("groupName"));
				group.setCampus_group(result.getBoolean("campus_group"));
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage(), e);
		}

		return Optional.ofNullable(group);
	}

}
