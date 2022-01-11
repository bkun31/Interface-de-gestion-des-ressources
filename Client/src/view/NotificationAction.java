package view;

import java.util.ArrayList;
import java.util.List;

public class NotificationAction {
	public List<NotificationListener> messageListeners = new ArrayList<>();
	public List<NotificationListener> updateGroupListeners = new ArrayList<>();
	
	public void addNotificationListener(NotificationListener listener) {
		messageListeners.add(listener);
	}
	
	public void addUpdateListener(NotificationListener listener) {
		updateGroupListeners.add(listener);
	}
	
	public void messageNotification() {
		for(NotificationListener listener : messageListeners) {
			listener.newMessageArrived();
		}
	}
	
	public void messageSeenNotifcation() {
		for(NotificationListener listener : messageListeners) {
			listener.messageSeen();
		}
	}
	
	public void updateNotifcation() {
		for(NotificationListener listener : messageListeners) {
			listener.updateInfo();
		}
	}
}
