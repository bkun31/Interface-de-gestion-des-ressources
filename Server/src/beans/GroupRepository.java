package beans;

public class GroupRepository implements Comparable<GroupRepository> {
	private long groupId;
	private String groupName;
	private boolean campus_group;

	/**
	 * @return the groupId
	 */
	public long getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the campus_group
	 */
	public boolean isCampus_group() {
		return campus_group;
	}

	/**
	 * @param campus_group the campus_group to set
	 */
	public void setCampus_group(boolean campus_group) {
		this.campus_group = campus_group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (groupId ^ (groupId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof GroupRepository))
			return false;
		GroupRepository other = (GroupRepository) obj;
		if (groupId != other.groupId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		if(this.isCampus_group())
			return this.groupName + "   Campus Group";
		return this.groupName + "   Service Group";
	}

	@Override
	public int compareTo(GroupRepository anotherGroup) {
		return groupName.compareTo(anotherGroup.groupName);
	}

}
