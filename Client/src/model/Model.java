package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import client.Client;
import data.DiscussionThread;
import data.Group;
import data.Message;
import data.User;
import view.NotificationAction;

public class Model extends NotificationAction {
	private String username;
	private User me;
	private Map<Group, List<DiscussionThread>> treeThread = new ConcurrentSkipListMap<>();
	private Map<Long, List<Message>> messages = new HashMap<>();
	private Map<Long, DiscussionThread> threads = new HashMap<>();

	private static Model model = null;

	private Model() {
	}

	public static Model getInstance() {
		if (model == null)
			model = new Model();

		return model;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the me
	 */
	public User getMe() {
		return me;
	}

	/**
	 * @param me the me to set
	 */
	public void setMe(User me) {
		this.me = me;
	}

	/**
	 * @return the treeThread
	 */
	public Map<Group, List<DiscussionThread>> getTreeThread() {
		return treeThread;
	}

	/**
	 * ****************** LISTENER QUI VA FAIRE UN Model.getInstance().getMessage()
	 * *************** makes obsolete the threads whose status has been modified
	 * 
	 * @param listUpdate
	 */
	public void updateThread(List<Long> listUpdate) {
		for (long threadId : listUpdate) {
			this.threads.get(threadId).setObsolete(true);
		}
	}

	/**
	 * ****************** LISTENER QUI VA FAIRE UN Model.getInstance().getMessage()
	 * ********** ****************** ET QUI VA FAIRE UN
	 * Model.getInstance().getTreeThread *************** update the tree of threads
	 * 
	 * @param treeThread
	 */
	public void setTreeThread(Map<Group, List<DiscussionThread>> treeThread) {
		Set<Entry<Group, List<DiscussionThread>>> set = treeThread.entrySet();

		for (Entry<Group, List<DiscussionThread>> pair : set) {
			Group group = pair.getKey();
			List<DiscussionThread> threadList = pair.getValue();
			if (this.treeThread.containsKey(group)) {
				merge(this.treeThread.get(group), threadList);
			} else {
				this.treeThread.put(group, threadList);
				addThread(threadList);
			}
		}
	}

	/**
	 * @return the messages
	 */
	public List<Message> getMessages(DiscussionThread thread) {
		if (!messages.containsKey(thread.getThreadId()) || thread.isObsolete()) {
			messages.put(thread.getThreadId(), Client.getInstance().getMessages(thread.getThreadId()));
			thread.setObsolete(false);
		}
		if (thread.getUnreadCount() > 0)
			thread.setUnreadCount(0);
		return messages.get(thread.getThreadId());
	}

	/**
	 * ****************** LISTENER QUI VA FAIRE UN Model.getInstance().getMessage()
	 * *************** add a new message sent in the thread
	 * 
	 * @param group
	 * @param thread
	 * @param message
	 */
	public void addMessage(Group group, DiscussionThread thread, Message message) {
		this.messages.get(thread.getThreadId()).add(message);
		Collections.sort(this.treeThread.get(group));
	}

	/**
	 * ****************** LISTENER QUI VA FAIRE UN Model.getInstance().getMessage()
	 * ********** ****************** ET QUI VA FAIRE UN
	 * Model.getInstance().getTreeThread *************** add a new thread created
	 * 
	 * @param group
	 * @param thread
	 * @param message
	 */
	public void addThread(Group group, DiscussionThread thread, Message message) {
		List<Message> list = new ArrayList<>();
		list.add(message);

		this.messages.put(thread.getThreadId(), list);
		this.threads.put(thread.getThreadId(), thread);

		this.treeThread.computeIfAbsent(group, key -> new ArrayList<>());
		this.treeThread.get(group).add(0, thread);
	}

	public void setThreadId(long temporaryId, long threadId) {
		DiscussionThread thread = this.threads.remove(temporaryId);
		thread.setThreadId(threadId);
		this.threads.put(threadId, thread);
		this.messages.remove(temporaryId);
	}

	/**
	 * 
	 * @param treeThread
	 */
	public void refresh(Map<Group, List<DiscussionThread>> treeThread, User me) {
		this.treeThread.clear();
		this.messages.clear();
		this.threads.clear();
		this.me = me;
		setTreeThread(treeThread);
		this.updateNotifcation();
	}

	/**
	 * merges into listToMerge the list list
	 * 
	 * @param listToMerge
	 * @param listUpdate
	 */
	private void merge(List<DiscussionThread> listToMerge, List<DiscussionThread> listUpdate) {
		for (DiscussionThread threadUpdate : listUpdate) {
			if (threads.containsKey(threadUpdate.getThreadId())) {
				DiscussionThread thread = threads.get(threadUpdate.getThreadId());
				thread.setTsLastMessage(threadUpdate.getTsLastMessage());
				thread.setUnreadCount(threadUpdate.getUnreadCount());
				thread.setObsolete(true);
			} else {
				threads.put(threadUpdate.getThreadId(), threadUpdate);
				listToMerge.add(threadUpdate);
			}
		}
		Collections.sort(listToMerge);
	}

	/**
	 * adds threads to the threads map
	 * 
	 * @param list
	 */
	private void addThread(List<DiscussionThread> list) {
		for (DiscussionThread thread : list) {
			threads.put(thread.getThreadId(), thread);
		}
	}
}
