package globalServer;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Player implements Runnable {
	
	private Socket connection;
	private Scanner input;
	private Formatter output;
	private int playerNumber;
	private boolean suspended = true;

	public Player(Socket socket, int number) {
		playerNumber = number;
		connection = socket;
		
		try {
			input = new Scanner(connection.getInputStream());
			output = new Formatter(connection.getOutputStream());
		} catch (IOException ioE) {
			ioE.printStackTrace();
			
		}
	}
	
	public void setSuspended(boolean status) {
		suspended = status;
	}
	
	public boolean online() {
		return true; 
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(online()) {
			String user, pw, loginregister;
			loginregister = input.nextLine();
			if (loginregister.equals("LOGIN")) {
				int login = 0;	
				user = input.nextLine();
				pw = input.nextLine();
				login = DatabaseHandler.userLogin(user, pw);
				if(login > 0) {
					output.format("%d\n", login);
					output.flush();
				}
				else if (login == 0) {
					output.format("%d\n", login);
					output.flush();
					
				}
			} 
			else if (loginregister.equals("REGISTER")){
				boolean register = false;
				user = input.nextLine();
				pw = input.nextLine();
				register = DatabaseHandler.registerNewUser(user, pw);
				if (register) {
					output.format("%s\n", "ACCEPTED");
					output.flush();
				}
				else {
					output.format("%s\n", "DECLINED");
					output.flush();
				}
			}
			
			}
		} finally {
			try {
				connection.close();
			} catch (IOException ioE) {
				ioE.printStackTrace();
			}
		}
		
	}
	
}