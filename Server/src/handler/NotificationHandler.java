package handler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.Gson;

import beans.UserRepository;
import database.Database;
import event.Evenement;
import server.Server;
import streamdata.DiscussionThread;
import streamdata.Group;
import streamdata.User;

public class NotificationHandler implements Runnable {
	private Database database = new Database();
	private Gson gson = Server.gson;

	@Override
	public void run() {

		try {
			while (true) {
				Evenement event = Server.queue.take();
				switch (event.getType()) {
				case SEND:
					sendingService(event.getThreadId());
					break;
				case RECEIVE:
					updateReceive(event.getThreadIdList());
					break;
				case READ:
					updateRead(event.getThreadId());
					break;
				case UPDATE:
					update(event.getUserIdSet());
					break;
				case DELETE_USER:
					updateDeleteUser(event.getUserIdSet(), event.getUserId());
					break;
				default:
					break;
				}
			}
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}

	}

	private void sendingService(long threadId) {
		List<UserRepository> listDB = database.getUserByThread(threadId);

		for (UserRepository userDB : listDB) {
			if (Server.usersOnline.containsKey(userDB.getUserId())) {
				Map<String, String> result = new HashMap<>();
				Map<Group, List<DiscussionThread>> tree = buildTreeThreads(userDB.getUserId());

				result.put("DO", gson.toJson("NEW_MESSAGE"));

				Set<Map.Entry<Group, List<DiscussionThread>>> set = tree.entrySet();
				int i = 1;
				result.put("GROUPCOUNT", gson.toJson(tree.size()));

				for (Map.Entry<Group, List<DiscussionThread>> pair : set) {
					result.put("GROUP" + i, gson.toJson(pair.getKey()));
					result.put("THREADS" + i, gson.toJson(pair.getValue()));
					i++;
				}

				PrintWriter out = Server.usersOnline.get(userDB.getUserId());
				synchronized (out) {
					out.println(gson.toJson(result));
					out.flush();
				}
			}
		}
	}

	/**
	 * 
	 * @param threadIdList
	 */
	private void updateReceive(List<Long> threadIdList) {
		Map<UserRepository, List<Long>> map = new HashMap<>();
		for (long threadId : threadIdList) {
			List<UserRepository> listDB = database.getUserByThread(threadId);
			for (UserRepository userDB : listDB) {
				map.computeIfAbsent(userDB, key -> new ArrayList<>());
				map.get(userDB).add(threadId);
			}
		}

		Set<Entry<UserRepository, List<Long>>> entrySet = map.entrySet();
		for (Entry<UserRepository, List<Long>> pair : entrySet) {

			if (Server.usersOnline.containsKey(pair.getKey().getUserId())) {
				Map<String, String> result = new HashMap<>();
				PrintWriter out = Server.usersOnline.get(pair.getKey().getUserId());

				result.put("DO", gson.toJson("UPDATE_STATUS"));
				result.put("LIST", gson.toJson(pair.getValue()));

				synchronized (out) {
					out.println(gson.toJson(result));
					out.flush();
				}
			}
		}
	}

	/**
	 * 
	 * @param threadId
	 */
	private void updateRead(long threadId) {
		List<UserRepository> listDB = database.getUserByThread(threadId);
		List<Long> list = new ArrayList<>();
		list.add(threadId);
		for (UserRepository userDB : listDB) {
			if (Server.usersOnline.containsKey(userDB.getUserId())) {
				Map<String, String> result = new HashMap<>();

				result.put("DO", gson.toJson("UPDATE_STATUS"));
				result.put("LIST", gson.toJson(list));

				PrintWriter out = Server.usersOnline.get(userDB.getUserId());
				synchronized (out) {
					out.println(gson.toJson(result));
					out.flush();
				}
			}
		}
	}

	private void update(Set<Long> userIdSet) {
		for (long userId : userIdSet) {
			if (Server.usersOnline.containsKey(userId)) {
				Map<String, String> result = new HashMap<>();
				Optional<User> container = database.getUser(userId);

				if (container.isPresent()) {
					User user = container.get();
					result.put("DO", gson.toJson("UPDATE"));
					result.put("USER", Server.gson.toJson(user));

					Map<Group, List<DiscussionThread>> tree = buildTreeThreads(userId);
					Set<Map.Entry<Group, List<DiscussionThread>>> set = tree.entrySet();
					int i = 1;
					result.put("GROUPCOUNT", Server.gson.toJson(tree.size()));

					for (Map.Entry<Group, List<DiscussionThread>> pair : set) {
						result.put("GROUP" + i, Server.gson.toJson(pair.getKey()));
						result.put("THREADS" + i, Server.gson.toJson(pair.getValue()));
						i++;
					}

					PrintWriter out = Server.usersOnline.get(userId);
					synchronized (out) {
						out.println(gson.toJson(result));
						out.flush();
					}
				}
			}
		}
	}

	private void updateDeleteUser(Set<Long> userIdSet, long user) {

		if (Server.usersOnline.containsKey(user)) {
			Server.usersOnline.get(user).close();
			Server.usersOnline.remove(user);
		}

		for (long userId : userIdSet) {
			if (Server.usersOnline.containsKey(userId)) {
				Map<String, String> result = new HashMap<>();

				Optional<User> container = database.getUser(userId);

				if (container.isPresent()) {
					User user1 = container.get();
					result.put("DO", gson.toJson("UPDATE"));
					result.put("USER", Server.gson.toJson(user1));

					Map<Group, List<DiscussionThread>> tree = buildTreeThreads(userId);
					Set<Map.Entry<Group, List<DiscussionThread>>> set = tree.entrySet();
					int i = 1;
					result.put("GROUPCOUNT", Server.gson.toJson(tree.size()));

					for (Map.Entry<Group, List<DiscussionThread>> pair : set) {
						result.put("GROUP" + i, Server.gson.toJson(pair.getKey()));
						result.put("THREADS" + i, Server.gson.toJson(pair.getValue()));
						i++;
					}

					PrintWriter out = Server.usersOnline.get(userId);
					synchronized (out) {
						out.println(gson.toJson(result));
						out.flush();
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param userId
	 * @return the tree of threads
	 */
	private Map<Group, List<DiscussionThread>> buildTreeThreads(long userId) {
		Map<Group, List<DiscussionThread>> treeThreads = new TreeMap<>();
		List<DiscussionThread> list = database.getThreads(userId);

		for (DiscussionThread thread : list) {
			Group group = database.getGroupByThread(thread.getThreadId());

			treeThreads.computeIfAbsent(group, k -> new ArrayList<>());
			treeThreads.get(group).add(thread);
		}

		return treeThreads;
	}
}
