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
				boolean login = true;	//må skiftes til false når databasen har funksjoner for login
			//	user = input.nextLine();
			//	pw = input.nextLine();
			// login = DatabaseTest.registerNewUser(user, pw);
				if(login) {
					output.format("%s\n", "CONNECTED");
					output.flush();
				}	
			} 
			else if (loginregister.equals("REGISTER")){
				boolean register = true; //må skiftes til false når databasen har funksjoner for registering.
				//user = input.nextLine();
				//pw = input.nextLine();
				//register = DatabaseTest.registerNewUser(user, pw);
				if (register) {
					output.format("%s\n", "ACCEPTED");
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