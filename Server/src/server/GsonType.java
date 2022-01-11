package server;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import streamdata.DiscussionThread;
import streamdata.Group;
import streamdata.Message;
import streamdata.User;

public class GsonType {

	public static Type longListType = new TypeToken<List<Long>>() {
	}.getType();
	public static Type messageType = new TypeToken<Message>() {
	}.getType();
	public static Type disscusionThreadType = new TypeToken<DiscussionThread>() {
	}.getType();
	public static Type hashMapType = new TypeToken<HashMap<String, String>>() {
	}.getType();
	public static Type groupType = new TypeToken<Group>() {
	}.getType();
	public static Type userType = new TypeToken<User>() {
	}.getType();
	public static Type threadListType = new TypeToken<List<DiscussionThread>>() {
	}.getType();
	public static Type groupListType = new TypeToken<ArrayList<Group>>() {
	}.getType();
	public static Type messageListType = new TypeToken<ArrayList<Message>>() {
	}.getType();
}
