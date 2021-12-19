package server;

import java.util.Map;
import database.Database;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import com.google.gson.Gson;

public class Server {
    private Map usersOnline;
    private Database database;

    public void start() {
    }

    private class HandleInput {
        private BufferedReader in;
        private PrintWriter out;
        private Socket client;
        private Database database;
        private Queue eventQueue;

        public HandleInput(Socket socket, Database database, Queue eventQueue) {
        }

        public void run() {
        }

        private String manageRequest(String request) {
        }

        private String requestLogin(Gson gson) {
        }

        private String requestSendMessage(Gson gson) {
        }

        private String requestCreateNewThread(Gson gson) {
        }

        private String requestGetThread(Gson gson) {
        }

        private String requestGetGroups(Gson gson) {
        }

        private String requestGetMessages(Gson gson) {
        }

        private String requestRefresh(Gson gson) {
        }
    }

    private class HandleOutput {
        private Socket usersOnline;
        private Database database;
        private Queue eventQueue;

        public HandleOutput(Map usersOnline, Database database, Queue eventQueue) {
        }

        public void run() {
        }

        private String notifyNewMessages() {
        }

        private String notifyUpdateStatusMessages() {
        }
    }

}
