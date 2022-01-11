package beans;

public class UserRepository implements Comparable<UserRepository> {
    private long userId;
    private String username;
    private String password;
    private String name;
    private String firstname;
    private boolean campusUser;

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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the campus_user
	 */
	public boolean isCampusUser() {
		return campusUser;
	}

	/**
	 * @param campus_user the campus_user to set
	 */
	public void setCampusUser(boolean campus_user) {
		this.campusUser = campus_user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UserRepository))
			return false;
		UserRepository other = (UserRepository) obj;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.firstname + " " + this.name ;
	}

	@Override
    public int compareTo(UserRepository anotherUser) {
        int comparison = this.name.compareTo(anotherUser.name);
        if (comparison == 0) {
            comparison = this.firstname.compareTo(anotherUser.firstname);
            if (comparison == 0)
                comparison = (int) (this.userId - userId);
        }
        return comparison;
    }

}
