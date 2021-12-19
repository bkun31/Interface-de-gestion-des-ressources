package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.TreeSet;
import data.DiscussionThread;
import data.Group;
import data.User;
import com.google.gson.Gson;

public class Client {
    private BufferedReader in;

    private PrintWriter out;

    private Socket server;

    private User me;

    private Group groupsNewDiscussionThread;

    private DiscussionThread discussionThread;

    public void start() {
    }

    public void stop() {
    }

    public void login(String username, String password) {
    }

    public void sendMessage(int threadId, String text) {
    }

    public void createNewDiscussionThread(String groupId, String title, String text) {
    }

    public TreeSet getThreads() {
    }

    public TreeSet getGroups() {
    }

    public TreeSet getMessages(int threadId) {
    }

    public void refresh() {
    }

    private class HandleInput {
        private BufferedReader in;
        private PrintWriter out;
        private Socket server;
        private User me;
        private TreeSet groupsNewDiscussionThread;
        private TreeSet discussionThreads;
    
        public HandleInput(BufferedReader in, PrintWriter out, Socket server, User me, TreeSet groupsNewDiscussionThread, TreeSet discussionThreads) {
        }
    
        public void run() {
        }
    
        private String manageServerRequest(String request) {
        }
    
        private String updateThreads(Gson gson) {
        }
    }

}
