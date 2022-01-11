package data;

import java.sql.Timestamp;

public class DiscussionThread implements Comparable<DiscussionThread>{
	private long threadId;
	private String title;
	private int unreadCount;
	private boolean obsolete;
	private Timestamp tsLastMessage;

	/**
	 * @return the threadId
	 */
	public long getThreadId() {
		return threadId;
	}

	/**
	 * @param threadId the threadId to set
	 */
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the unreadCount
	 */
	public int getUnreadCount() {
		return unreadCount;
	}

	/**
	 * @param unreadCount the unreadCount to set
	 */
	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	/**
	 * @return the obsolete
	 */
	public boolean isObsolete() {
		return obsolete;
	}

	/**
	 * @param obsolete the obsolete to set
	 */
	public void setObsolete(boolean obsolete) {
		this.obsolete = obsolete;
	}

	/**
	 * @return the lastMessage
	 */
	public Timestamp getTsLastMessage() {
		return tsLastMessage;
	}

	/**
	 * @param lastMessage the lastMessage to set
	 */
	public void setTsLastMessage(Timestamp lastMessage) {
		this.tsLastMessage = lastMessage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + threadId);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiscussionThread other = (DiscussionThread) obj;
		if (threadId != other.threadId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String format;
		String chaine =	title;
		if(unreadCount > 0) {
			chaine += "    (" +unreadCount+")";
			format = "<html><body style='width: %1spx'><b>%1s";
		}else {
			format = "<html><body style='width: %1spx'>%1s";
		}
		return String.format(format, 70,chaine);
	}

	@Override
	public int compareTo(DiscussionThread anotherThread) { 
		return - tsLastMessage.compareTo(anotherThread.tsLastMessage);
	}
	
}
