package beans;

import java.sql.Timestamp;

public class MessageRepository implements Comparable<MessageRepository> {
	private long messageId;
	private Timestamp timestamp;
	private String text;
	private long threadId;

	/**
	 * @return the messageId
	 */
	public long getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (messageId ^ (messageId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof MessageRepository))
			return false;
		MessageRepository other = (MessageRepository) obj;
		if (messageId != other.messageId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", timestamp=" + timestamp + ", text=" + text + ", threadId="
				+ threadId + "]";
	}

	@Override
	public int compareTo(MessageRepository anotherMessage) {
		return this.timestamp.compareTo(anotherMessage.timestamp);
	}

}
