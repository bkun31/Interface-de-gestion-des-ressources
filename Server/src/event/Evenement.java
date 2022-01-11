package event;

import java.util.List;
import java.util.Set;

public class Evenement {
    private TYPE type;
    private List<Long> threadIdList;
    private long threadId;
    private long userId;
    private Set<Long> userIdSet;
   
    
    /**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

    /**
     * @return the type
     */
    public TYPE getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TYPE type) {
        this.type = type;
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

	/**
	 * @return the threadIdList
	 */
	public List<Long> getThreadIdList() {
		return threadIdList;
	}

	/**
	 * @param threadIdList the threadIdList to set
	 */
	public void setThreadIdList(List<Long> threadIdList) {
		this.threadIdList = threadIdList;
	}

	public Set<Long> getUserIdSet() {
		return userIdSet;
	}

	public void setUserIdSet(Set<Long> userIdSet) {
		this.userIdSet = userIdSet;
	}

}
