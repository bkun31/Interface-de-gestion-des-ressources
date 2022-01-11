package client;

import view.LoginGui;

public class MainClient {
	public static void main(String[] args) {
		
		new LoginGui(Client.getInstance());
	}

}
