package controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import beans.GroupRepository;
import beans.ThreadRepository;
import beans.UserRepository;
import database.Database;
import event.Evenement;
import event.TYPE;
import exception.DAOException;
import server.Server;

public class Controller {
	private static final String ADMIN_PASSWORD = "admin";
	private static Controller controller = null;

	private static Database database = new Database();
	private static Map<Boolean, NavigableSet<UserRepository>> userMap = new HashMap<>();
	private static Map<Boolean, NavigableSet<GroupRepository>> groupMap = new HashMap<>();

	private Controller() {
	}

	/**
	 * 
	 * @return the controller instance
	 */
	public static Controller getInstance() {
		if (controller == null) {
			controller = new Controller();
			NavigableSet<UserRepository> userSet;
			NavigableSet<GroupRepository> groupSet;
			try {
				userSet = new TreeSet<UserRepository>(database.getUserDAO().findByService(false));
				userMap.put(false, userSet);
				userSet = new TreeSet<UserRepository>(database.getUserDAO().findByService(true));
				userMap.put(true, userSet);

				groupSet = new TreeSet<GroupRepository>(database.getGroupDAO().findByService(false));
				groupMap.put(false, groupSet);
				groupSet = new TreeSet<GroupRepository>(database.getGroupDAO().findByService(true));
				groupMap.put(true, groupSet);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}

		return controller;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param name
	 * @param firstname
	 * @param groups
	 * @param service
	 * @return true if the registration was successful
	 */
	public boolean registerUser(String username, String password, String name, String firstname, boolean service,
			List<GroupRepository> groups) {
		UserRepository userDB = new UserRepository();
		boolean success = false;

		userDB.setUsername(username);
		userDB.setPassword(password);
		userDB.setName(name);
		userDB.setFirstname(firstname);
		userDB.setCampusUser(service);

		success = database.registerUser(userDB, groups);

		if (success) {
			Evenement event = new Evenement();
			event.setType(TYPE.UPDATE);
			event.setUserIdSet(getConcernedUserId(userDB));
			Server.queue.add(event);
			userMap.get(service).add(userDB);
		}

		return success;
	}

	public boolean registerGroup(String groupname, boolean service, List<UserRepository> users, String adminpassword) {
        boolean success = adminpassword.equals(ADMIN_PASSWORD);
        if (success) {
            GroupRepository groupDB = new GroupRepository();

            groupDB.setGroupName(groupname);
            groupDB.setCampus_group(service);

            success = database.registerGroup(groupDB, users);

            if (success) {
                Evenement event = new Evenement();
                event.setType(TYPE.UPDATE);
                event.setUserIdSet(getConcernedUserId(groupDB));
                Server.queue.add(event);
                groupMap.get(service).add(groupDB);
            }
        }

        return success;
    }

	/**
	 * 
	 * @param user
	 * @param adminpassword
	 * @return true if the deletion was successful
	 */
	public boolean deleteUser(UserRepository user, String adminpassword) {
		boolean success = adminpassword.equals(ADMIN_PASSWORD);

		if (success) {
			Set<Long> userIdSet = getConcernedUserId(user);
			userIdSet.remove(user.getUserId());
			success = database.deleteUser(user);
			if (success) {
				Evenement event = new Evenement();
				event.setType(TYPE.DELETE_USER);
				event.setUserIdSet(userIdSet);
				event.setUserId(user.getUserId());
				Server.queue.add(event);

				userMap.get(user.isCampusUser()).remove(user);
			}
		}

		return success;
	}

	/**
	 * 
	 * @param group
	 * @param adminpassword
	 * @return true if the deletion was successful
	 */
	public boolean deleteGroup(GroupRepository group, String adminpassword) {
		boolean success = adminpassword.equals(ADMIN_PASSWORD);

		if (success) {
			Set<Long> userIdSet = getConcernedUserId(group);
			success = database.deleteGroup(group);

			if (success) {
				Evenement event = new Evenement();
				event.setType(TYPE.UPDATE);
				event.setUserIdSet(userIdSet);
				Server.queue.add(event);
				groupMap.get(group.isCampus_group()).remove(group);
			}
		}

		return success;
	}

	public boolean addUserToGroup(UserRepository user, GroupRepository group) {
		boolean success = false;
		try {
			success = database.getAssociation().createBelong(user.getUserId(), group.getGroupId());
			if (success) {
				Evenement event = new Evenement();
				event.setType(TYPE.UPDATE);
				event.setUserIdSet(getConcernedUserId(group));
				Server.queue.add(event);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return success;
	}

	public boolean deleteUserFromGroup(UserRepository user, GroupRepository group) {
		boolean success = false;
		try {
			Set<Long> userIdSet = getConcernedUserId(group);
			success = database.getAssociation().deleteBelong(user.getUserId(), group.getGroupId());
			if (success) {
				Evenement event = new Evenement();
				event.setType(TYPE.UPDATE);
				event.setUserIdSet(userIdSet);
				Server.queue.add(event);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return success;
	}

	public boolean updateUser(UserRepository user) {
		boolean success = false;
		try {
			success = database.getUserDAO().update(user);
			if (success) {
				Evenement event = new Evenement();
				event.setType(TYPE.UPDATE);
				event.setUserIdSet(getConcernedUserId(user));
				Server.queue.add(event);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return success;
	}

	public boolean updateGroup(GroupRepository group) {
		boolean success = false;
		try {
			success = database.getGroupDAO().update(group);
			if (success) {
				Evenement event = new Evenement();
				event.setType(TYPE.UPDATE);
				event.setUserIdSet(getConcernedUserId(group));
				;
				Server.queue.add(event);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * 
	 * @return list of all users
	 */
	public NavigableSet<UserRepository> getUsers() {
		NavigableSet<UserRepository> userSet = new TreeSet<>(userMap.get(false));
		userSet.addAll(userMap.get(true));

		return userSet;
	}

	/**
	 * 
	 * @param service
	 * @return list of service user
	 */
	public NavigableSet<UserRepository> getUsers(boolean service) {

		return userMap.get(service);
	}

	/**
	 * 
	 * @return list of all groups
	 */
	public NavigableSet<GroupRepository> getGroups() {
		NavigableSet<GroupRepository> groupSet = new TreeSet<>(groupMap.get(false));
		groupSet.addAll(groupMap.get(true));

		return groupSet;
	}

	/**
	 * 
	 * @param service
	 * @return list of service groups
	 */
	public NavigableSet<GroupRepository> getGroups(boolean service) {

		return groupMap.get(service);
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public NavigableSet<GroupRepository> getGroupsByUser(UserRepository user) {

		try {
			return new TreeSet<>(database.getGroupDAO().findByUser(user.getUserId()));
		} catch (DAOException e) {
			e.printStackTrace();
			return new TreeSet<>();
		}
	}

	/**
	 * 
	 * @param group
	 * @return
	 */
	public NavigableSet<UserRepository> getUsersByGroup(GroupRepository group) {

		try {
			return new TreeSet<>(database.getUserDAO().findByGroup(group.getGroupId()));
		} catch (DAOException e) {
			e.printStackTrace();
			return new TreeSet<>();
		}
	}

	/**
	 * 
	 * @param userChange
	 * @return the list of user ids affected by the userChange (include the
	 *         userChange id)
	 */
	private Set<Long> getConcernedUserId(UserRepository userChange) {
		Set<Long> userIdSet = new HashSet<>();
		try {
			List<ThreadRepository> threadList = database.getDiscussionTreadDAO().findByUser(userChange.getUserId());
			for (ThreadRepository thread : threadList) {
				List<UserRepository> users = database.getUserByThread(thread.getThreadId());
				for(UserRepository user : users)
					userIdSet.add(user.getUserId());
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return userIdSet;
	}

	/**
	 * 
	 * @param groupChange
	 * @return the list of user ids affected by the groupChange
	 */
	private Set<Long> getConcernedUserId(GroupRepository groupChange) {
        Set<Long> userIdSet = new HashSet<>();
        try {
            List<ThreadRepository> threadList = database.getDiscussionTreadDAO().findByGroup(groupChange.getGroupId());
            for (ThreadRepository thread : threadList) {
                List<UserRepository> users = database.getUserByThread(thread.getThreadId());
                for (UserRepository user : users)
                    userIdSet.add(user.getUserId());
            }
            List<UserRepository> users = database.getUserDAO().findByService(!groupChange.isCampus_group());
            for (UserRepository user : users) {
                userIdSet.add(user.getUserId());
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return userIdSet;
    }

}
