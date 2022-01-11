package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import event.Evenement;
import handler.ClientHandler;
import handler.NotificationHandler;
import view.AdminSide;

public class Server {
	public static Map<Long, PrintWriter> usersOnline = new ConcurrentHashMap<>();
	public static BlockingQueue<Evenement> queue = new LinkedBlockingQueue<>();
	public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	private static volatile boolean stop = false;
	

	public static void start() {
		try (ServerSocket server = new ServerSocket(3131)) {
			new Thread(new NotificationHandler()).start();
			new AdminSide();
			while (!stop) {
				Socket client = server.accept();
				new Thread(new ClientHandler(client)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
