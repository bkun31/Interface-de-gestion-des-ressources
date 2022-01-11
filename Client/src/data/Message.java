package data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class Message implements Comparable<Message>{
    private long messageId;
    private Timestamp timestamp;
    private String text;
    private User sender;
    private Status status;
    private Map<Status, List<User>> userSatus;

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
     * @return the user
     */
    public User getSender() {
        return sender;
    }

    /**
     * @param user the user to set
     */
    public void setSender(User user) {
        this.sender = user;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    /**
	 * @return the userSatus
	 */
	public Map<Status, List<User>> getUserSatus() {
		return userSatus;
	}

	/**
	 * @param userSatus the userSatus to set
	 */
	public void setUserSatus(Map<Status, List<User>> userSatus) {
		this.userSatus = userSatus; 
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (int) (prime * result + messageId);
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
        Message other = (Message) obj;
        if (messageId != other.messageId)
            return false;
        return true;
    }

	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", timestamp=" + timestamp + ", text=" + text + ", sender=" + sender
				+ ", status=" + status + ", userSatus=" + userSatus + "]";
	}

	@Override
	public int compareTo(Message anotherMessage) {
		return timestamp.compareTo(anotherMessage.timestamp);
	}

    
}
