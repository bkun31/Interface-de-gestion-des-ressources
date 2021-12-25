package data;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private Timestamp timestamp;
    private String text;
    private User user;
    private Status status;

    public Message(String messageId, Timestamp timestamp, String text, DiscussionThread thread) {
    }

    public String getMessageId() {
    }

    public Timestamp getTimestamp() {
    }

    public String getText() {
    }

    public DiscussionThread getThread() {
    }

    public int compareTo(Message message) {
    }

    public boolean equals(Object object) {
    }

}
