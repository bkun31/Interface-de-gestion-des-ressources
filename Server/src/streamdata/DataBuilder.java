package streamdata;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import beans.GroupRepository;
import beans.MessageRepository;
import beans.ThreadRepository;
import beans.UserRepository;

public class DataBuilder {

	/**
	 * 
	 * @param userDB
	 * @return
	 */
	public static User toUser(UserRepository userDB)
	{
		User user = new User();
		user.setUserId(userDB.getUserId());
		user.setName(userDB.getName());
		user.setFirstname(userDB.getFirstname());
		
		return user;
	}
	
	/**
	 * 
	 * @param groupDB
	 * @return
	 */
	public static Group toGroup(GroupRepository groupDB)
	{
		Group group = new Group();
		group.setGroupId(groupDB.getGroupId());
		group.setGroupName(groupDB.getGroupName());
		
		return group;
	}
	
	/**
	 * 
	 * @param threadDB
	 * @param unread
	 * @return
	 */
	public static DiscussionThread toDiscussionThread(ThreadRepository threadDB, int unread, Timestamp ts)
	{
		DiscussionThread thread = new DiscussionThread();
		thread.setThreadId(threadDB.getThreadId());
		thread.setTitle(threadDB.getTitle());
		thread.setUnreadCount(unread);
		thread.setTslastMessage(ts);
		
		return thread;
	}
	
	/**
	 * 
	 * @param messageDB
	 * @param user
	 * @param status
	 * @param userSatus
	 * @return
	 */
	public static Message toMessage(MessageRepository messageDB, User sender, Status status, Map<Status, List<User>> userStatus)
	{
		Message message = new Message();
		message.setMessageId(messageDB.getMessageId());
		message.setSender(sender);
		message.setText(messageDB.getText());
		message.setTimestamp(messageDB.getTimestamp());
		message.setStatus(status);
		message.setUserSatus(userStatus);
		
		return message;
		
	}
}
