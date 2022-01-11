package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import data.DiscussionThread;
import data.Group;
import data.Message;
import data.Status;
import data.User;
import model.Model;

public class Client {
	private BufferedReader in;
	private PrintWriter out;
	private Socket server;
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	private static Client client = null;
	private long temporaryId = 0;
	private final long MAX_TEMPORARY_ID = 50;

	private BlockingQueue<Map<String, String>> queue;

	private Client(Socket server, BufferedReader in, PrintWriter out, BlockingQueue<Map<String, String>> queue) {
		this.server = server;
		this.in = in;
		this.out = out;
		this.queue = queue;
	}

	public static Client getInstance() {
		if (client == null) {
			try {
				Socket server = new Socket("localhost", 3131);
				BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
				PrintWriter out = new PrintWriter(server.getOutputStream());
				BlockingQueue<Map<String, String>> queue = new LinkedBlockingQueue<>(1);

				new Thread(new ResponseHandler(in, out, queue)).start();
				client = new Client(server, in, out, queue);

			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
				System.exit(0);
			}
		}

		return client;
	}

	public void logout() {
		try {
			Map<String, String> request = new HashMap<>();

			request.put("DO", gson.toJson("LOGOUT"));
			send(gson.toJson(request));

			out.close();
			in.close();
			server.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean login(String username, String password) {
		boolean success = false;
		Map<String, String> request = new HashMap<>();

		request.put("DO", gson.toJson("LOGIN"));
		request.put("USERNAME", gson.toJson(username));
		request.put("PASSWORD", gson.toJson(password));

		send(gson.toJson(request));

		try {
			Map<String, String> result = queue.take();

			if (gson.fromJson(result.get("RESULT"), String.class).equals("SUCCESS")) {

				Model.getInstance().setUsername(username);
				Model.getInstance().setMe(gson.fromJson(result.get("USER"), GsonType.userType));
				Model.getInstance().setTreeThread(treeThreadBuilder(result));

				request.clear();
				request.put("DO", gson.toJson("RECEIVEALL"));
				send(gson.toJson(request));

				success = true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * 
	 * @param threadId
	 * @param text
	 * @return
	 */
	public boolean sendMessage(Group group, DiscussionThread thread, String text) {
		boolean success = false;
		long threadId = thread.getThreadId();
		Message message = new Message();
		Map<String, String> request = new HashMap<>();

		message.setSender(Model.getInstance().getMe());
		message.setStatus(Status.NOT_RECEIVED);
		message.setText(text);
		message.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
		Map<Status, List<User>> userStatus = new HashMap<>();
		userStatus.put(Status.NOT_RECEIVED, new ArrayList<>());
		userStatus.get(Status.NOT_RECEIVED).add(Model.getInstance().getMe());
		message.setUserSatus(userStatus);
		Model.getInstance().addMessage(group, thread, message);

		request.put("DO", gson.toJson("SEND"));
		request.put("TEXT", gson.toJson(text));
		request.put("THREADID", gson.toJson(threadId));
		request.put("TIMESTAMP", gson.toJson(message.getTimestamp()));

		send(gson.toJson(request));

		try {
			Map<String, String> result = queue.take();
			success = gson.fromJson(result.get("RESULT"), String.class).equals("SUCCESS");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return success;

	}

	/**
	 * 
	 * @param groupId
	 * @param title
	 * @param text
	 * @return
	 */
	public boolean createNewDiscussionThread(Group group, String title, String text) {
		boolean success = false;
		long groupId = group.getGroupId();
		Map<String, String> request = new HashMap<>();
		DiscussionThread thread = new DiscussionThread();
		Message message = new Message();

		message.setSender(Model.getInstance().getMe());
		message.setStatus(Status.NOT_RECEIVED);
		message.setText(text);
		message.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

		thread.setThreadId(temporaryId-- % MAX_TEMPORARY_ID);
		thread.setTitle(title);
		thread.setUnreadCount(0);
		thread.setTsLastMessage(message.getTimestamp());

		Model.getInstance().addThread(group, thread, message);

		request.put("DO", gson.toJson("NEW"));
		request.put("TEXT", gson.toJson(text));
		request.put("TIMESTAMP", gson.toJson(message.getTimestamp()));
		request.put("GROUPID", gson.toJson(groupId));
		request.put("TITLE", gson.toJson(title));
		request.put("TEMPORARYID", gson.toJson(thread.getThreadId()));

		send(gson.toJson(request));
		
		try {
			Map<String, String> result = queue.take();
			success = gson.fromJson(result.get("RESULT"), String.class).equals("SUCCESS");
			if (success) {
				long threadId = gson.fromJson(result.get("THREADID"), Long.class);
				Model.getInstance().setThreadId(thread.getThreadId(), threadId);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * 
	 * @return
	 */
	public List<Group> getGroups() {
		Map<String, String> request = new HashMap<>();
		List<Group> groups = new ArrayList<>();
		request.put("DO", gson.toJson("GET_GROUPS"));
		;

		send(gson.toJson(request));

		try {
			Map<String, String> result = queue.take();
			groups = gson.fromJson(result.get("LIST"), GsonType.groupListType);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return groups;

	}

	/**
	 * 
	 * @param threadId
	 * @return
	 */
	public List<Message> getMessages(long threadId) {
		Map<String, String> request = new HashMap<>();
		List<Message> messages = new ArrayList<>();
		request.put("DO", gson.toJson("GET_MESSAGES"));
		request.put("THREADID", gson.toJson(threadId));
		send(gson.toJson(request));

		try {
			Map<String, String> result = queue.take();
			messages = gson.fromJson(result.get("LIST"), GsonType.messageListType);
			request.clear();
			request.put("DO", gson.toJson("READ"));
			request.put("THREADID", gson.toJson(threadId));
			send(gson.toJson(request));
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return messages;
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	private Map<Group, List<DiscussionThread>> treeThreadBuilder(Map<String, String> result) {
		Map<Group, List<DiscussionThread>> tree = new TreeMap<>();
		int groupCount = gson.fromJson(result.get("GROUPCOUNT"), Integer.class);

		for (int i = 1; i <= groupCount; ++i) {
			Group group = gson.fromJson(result.get("GROUP" + i), GsonType.groupType);
			List<DiscussionThread> list = gson.fromJson(result.get("THREADS" + i), GsonType.threadListType);

			tree.put(group, list);
		}
		
		return tree;
	}

	/**
	 * 
	 * @param message
	 */
	private void send(String message) {
		synchronized (out) {
			out.println(message);
			out.flush();
		}
	}

}
