package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import data.DiscussionThread;
import data.Group;
import model.Model;

public class ResponseHandler implements Runnable {
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	private BufferedReader in;
	private PrintWriter out;

	private BlockingQueue<Map<String, String>> queue;

	public ResponseHandler(BufferedReader in, PrintWriter out, BlockingQueue<Map<String, String>> queue) {
		this.in = in;
		this.out = out;
		this.queue = queue;
	}

	public void run() {
		String input;
		while (true) {
			try {
				input = in.readLine();
				if (input != null)
					manageRequest(gson.fromJson(input, GsonType.hashMapType));
				else {
					System.exit(0);
				}
			} catch (IOException e) {
				System.exit(0);
				// e.printStackTrace();
			}
		}
	}

	/**
	 * manages the different responses received from the server
	 * 
	 * @param request
	 */
	private void manageRequest(Map<String, String> request) {
		switch (gson.fromJson(request.get("DO"), String.class)) {
		case "NEW_MESSAGE":
			newMessage(request);
			break;
		case "UPDATE_STATUS":
			updateStatus(request);
			break;
		case "UPDATE":
			updateTree(request);
			break;
		default:
			delegateClient(request);
			break;
		}
	}

	/**
	 * updates the thread tree
	 * 
	 * @param request
	 */
	private void newMessage(Map<String, String> request) {
		Map<String, String> response = new HashMap<>();
		Map<Group, List<DiscussionThread>> tree = new TreeMap<Group, List<DiscussionThread>>();
		List<Long> threadIdList = new ArrayList<>();

		int groupCount = gson.fromJson(request.get("GROUPCOUNT"), Integer.class);

		for (int i = 1; i <= groupCount; ++i) {
			Group group = gson.fromJson(request.get("GROUP" + i), GsonType.groupType);
			List<DiscussionThread> list = gson.fromJson(request.get("THREADS" + i), GsonType.threadListType);
			for (DiscussionThread thread : list) {
				threadIdList.add(thread.getThreadId());
			}
			tree.put(group, list);
		}

		response.put("DO", gson.toJson("RECEIVE"));
		response.put("LIST", gson.toJson(threadIdList));
		send(gson.toJson(response));
		Model.getInstance().setTreeThread(tree);
		Model.getInstance().messageNotification();
	}

	/**
	 * indicates status update
	 * 
	 * @param request
	 */
	private void updateStatus(Map<String, String> request) {
		List<Long> listUpdate = gson.fromJson(request.get("LIST"), GsonType.threadIdList);
		Model.getInstance().updateThread(listUpdate);
		Model.getInstance().messageSeenNotifcation();
	}

	private void updateTree(Map<String, String> request) {
		Model.getInstance().refresh(treeThreadBuilder(request), gson.fromJson(request.get("USER"), GsonType.userType));
	}

	/**
	 * delegates the request to the main thread for processing
	 * 
	 * @param request
	 */
	private void delegateClient(Map<String, String> request) {
		queue.add(request);
	}

	/**
	 * sends the message to the socket
	 * 
	 * @param message
	 */
	private void send(String message) {
		synchronized (out) {
			out.println(message);
			out.flush();
		}
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
}
