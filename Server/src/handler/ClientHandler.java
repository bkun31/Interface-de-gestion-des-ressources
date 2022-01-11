package handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import beans.MessageRepository;
import beans.ThreadRepository;
import database.Database;
import event.Evenement;
import event.TYPE;
import server.GsonType;
import server.Server;
import streamdata.DiscussionThread;
import streamdata.Group;
import streamdata.Message;
import streamdata.User;

public class ClientHandler implements Runnable {
	private BufferedReader in;
	private PrintWriter out;
	private Socket client;
	private long userId;
	private boolean exit = false;

	private Database database = new Database();

	/**
	 * 
	 * @param usersOnline
	 * @param queue
	 * @param client
	 */
	public ClientHandler(Socket client) {
		this.userId = -1;
		this.client = client;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			String request;
			while (!exit && (request = in.readLine()) != null) {
				Thread.sleep(1000);
				manageRequest(request);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		if (userId != -1)
			Server.usersOnline.remove(userId, client);
	}

	/**
	 * manages the request according to the demand
	 * 
	 * @param request (JSON)
	 *
	 */
	private void manageRequest(String request) {
		HashMap<String, String> map = Server.gson.fromJson(request, GsonType.hashMapType);

		switch (Server.gson.fromJson(map.get("DO"), String.class)) {
		case "LOGIN":
			requestLogin(map);
			break;
		case "SEND":
			requestSendMessage(map);
			break;
		case "NEW":
			requestCreateNewThread(map);
			break;
		case "GET_GROUPS":
			requestGetGroups();
			break;
		case "GET_MESSAGES":
			requestGetMessages(map);
			break;
		case "RECEIVE":
			requestReceive(map);
			break;
		case "READ":
			requestRead(map);
			break;
		case "RECEIVEALL":
			requestReceiveAll();
			break;
		case "LOGOUT":
			requestLogout();
			break;
		default:
			unknowRequest();
			break;
		}
	}

	/**
	 * manage the login request
	 * 
	 * @param map request
	 *
	 */
	private void requestLogin(Map<String, String> map) {
		String username = Server.gson.fromJson(map.get("USERNAME"), String.class);
		String password = Server.gson.fromJson(map.get("PASSWORD"), String.class);
		Map<String, String> result = new HashMap<>();

		result.put("DO", Server.gson.toJson("LOGIN"));
		Optional<User> container = database.login(username, password);

		if (container.isPresent()) {
			User user = container.get();
			this.userId = user.getUserId();
			Server.usersOnline.put(userId, out);

			result.put("RESULT", Server.gson.toJson("SUCCESS"));
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

		} else
			result.put("RESULT", Server.gson.toJson("FAILURE"));

		synchronized (out) {
			out.println(Server.gson.toJson(result));
			out.flush();
		}
	}

	/**
	 * manage the send request
	 * 
	 * @param map
	 * 
	 */
	private void requestSendMessage(Map<String, String> map) {
		Evenement event = new Evenement();
		Map<String, String> result = new HashMap<>();

		MessageRepository messageDB = buildMessage(map);

		result.put("DO", Server.gson.toJson("SEND"));

		if (database.sendMessage(userId, messageDB)) {
			result.put("RESULT", "SUCCESS");
			event.setType(TYPE.SEND);
			event.setThreadId(messageDB.getThreadId());
			Server.queue.add(event);
		} else {
			result.put("RESULT", Server.gson.toJson("FAILURE"));
		}

		synchronized (out) {
			out.println(Server.gson.toJson(result));
			out.flush();
		}
	}

	/**
	 * manage the new thread request
	 * 
	 * @param map
	 * 
	 */
	private void requestCreateNewThread(Map<String, String> map) {
		Evenement event = new Evenement();
		Map<String, String> result = new HashMap<>();

		ThreadRepository threadDB = buildThread(map);
		MessageRepository messageDB = buildMessage(map);

		result.put("DO", Server.gson.toJson("NEW"));

		if (database.newThread(threadDB, messageDB)) {
			result.put("RESULT", "SUCCESS");
			result.put("THREADID", Server.gson.toJson(threadDB.getThreadId()));
			event.setType(TYPE.SEND);
			event.setThreadId(messageDB.getThreadId());
			Server.queue.add(event);

		} else {
			result.put("RESULT", Server.gson.toJson("FAILURE"));
		}

		synchronized (out) {
			out.println(Server.gson.toJson(result));
			out.flush();
		}
	}

	private void requestGetGroups() {
		Map<String, String> result = new HashMap<>();
		List<Group> groups = database.getGroupsNewThread(database.getUserDB(userId).isCampusUser());

		result.put("DO", Server.gson.toJson("GET_GROUP"));
		result.put("LIST", Server.gson.toJson(groups));

		synchronized (out) {
			out.println(Server.gson.toJson(result));
			out.flush();
		}
	}

	private void requestGetMessages(Map<String, String> map) {
		Map<String, String> result = new HashMap<>();
		long threadId = Server.gson.fromJson(map.get("THREADID"), Long.class);
		List<Message> messages = database.getMessages(threadId);

		result.put("DO", Server.gson.toJson("GET_MESSAGES"));
		result.put("LIST", Server.gson.toJson(messages));

		synchronized (out) {
			out.println(Server.gson.toJson(result));
			out.flush();
		}
	}

	private void requestReceive(Map<String, String> map) {
		List<Long> threadIdList = database.received(userId,
				Server.gson.fromJson(map.get("LIST"), GsonType.longListType));
		if (!threadIdList.isEmpty()) {
			Evenement event = new Evenement();
			event.setType(TYPE.RECEIVE);
			event.setThreadIdList(threadIdList);
			event.setUserId(userId);
			Server.queue.add(event);
		}
	}

	public void requestReceiveAll() {
		List<Long> threadIdList = database.receivedAll(userId);
		if (!threadIdList.isEmpty()) {
			Evenement event = new Evenement();
			event.setType(TYPE.RECEIVE);
			event.setUserId(userId);
			event.setThreadIdList(threadIdList);
			Server.queue.add(event);
		}
	}

	private void requestRead(Map<String, String> map) {
		Evenement event = new Evenement();
		long threadId = Server.gson.fromJson(map.get("THREADID"), Long.class);
		if (database.read(userId, threadId)) {
			event.setType(TYPE.READ);
			event.setThreadId(threadId);
			event.setUserId(userId);
			Server.queue.add(event);
		}
	}

	private void requestLogout() {
		Server.usersOnline.remove(userId);
		try {
			out.close();
			in.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			exit = true;
		}

	}

	/**
	 * 
	 */
	private void unknowRequest() {
		Map<String, String> defaultRequest = new HashMap<>();

		defaultRequest.put("DO", Server.gson.toJson("DEFAULT"));
		defaultRequest.put("DEFAULT", Server.gson.toJson("UNKNOW_REQUEST"));

		synchronized (out) {
			out.println(Server.gson.toJson(defaultRequest));
			out.flush();
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

	/**
	 * 
	 * @param map
	 * @return the model of message
	 */
	private MessageRepository buildMessage(Map<String, String> map) {
		MessageRepository message = new MessageRepository();
		message.setTimestamp(Server.gson.fromJson(map.get("TIMESTAMP"), Timestamp.class));
		message.setText(Server.gson.fromJson(map.get("TEXT"), String.class));
		if (map.containsKey("THREADID"))
			message.setThreadId(Server.gson.fromJson(map.get("THREADID"), Long.class));

		return message;
	}

	/**
	 * 
	 * @param map
	 * @return the model of thread
	 */
	private ThreadRepository buildThread(Map<String, String> map) {
		ThreadRepository thread = new ThreadRepository();
		thread.setTitle(Server.gson.fromJson(map.get("TITLE"), String.class));
		thread.setGroupId(Server.gson.fromJson(map.get("GROUPID"), Long.class));
		thread.setUserId(userId);
		return thread;
	}
}