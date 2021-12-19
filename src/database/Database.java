package database;

import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import data.DiscussionThread;
import data.Group;
import data.Message;
import data.User;

public class Database {
    private Statement statement;

    public Database(String url, String login, String password) {
    }

    public User login(String username, String password) {
    }

    public User registerUser(String id, String name, String firstname, String password, List<String> groupsId) {
    }

    public Group registerGroup(String name, boolean service) {
    }

    public String deleteUser(String userId) {
    }

    public boolean deleteGroup(String groupName) {
    }

    public DiscussionThread newThread(int userId, String title, String groupName) {
    }

    public Message sendMessage(int threadId, String userId, String text, Timestamp timestamp) {
    }

    public void getAllMessages(int threadId) {
    }

    public List getGroupsNewThread(String userId, boolean service) {
    }

    public String getGroupOfThread(int threadId) {
    }

    public Set getThreads(String userId) {
    }

    public boolean messageReceivedByAll(int messageId) {
    }

    public boolean messageReadByAll(int messageId) {
    }

    public boolean messageReceivedAddToDB(String userId, int threadId) {
    }

    public boolean messageReadAddToDB(String userId, int threadId) {
    }

    private int unreadMessage(String userId, int threadId) {
    }

    public Map refresh(String userId) {
    }

}
