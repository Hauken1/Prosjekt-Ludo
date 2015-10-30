package globalServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Player {
	
	private Socket connection;
	
	private BufferedReader input;
	private BufferedWriter output;
	
	private String name;

	public Player(Socket connection) throws IOException {
		this.connection = connection;
		
		input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			
		loginChecker(input.readLine(), input.readLine());		
	}
	
	public void close() throws IOException {
		input.close();
		output.close();
		connection.close();
	}
	
	public void sendText(String text) throws IOException {
		output.write(text);
		output.newLine();
		output.flush();
	}
	
	public String read() throws IOException {
		if (input.ready())
			return input.readLine();
		return null;
	}
	
	public String returnName() {
		return name;
	}
	
	private void loginChecker(String username, String pass) throws IOException {
		
		if (!username.startsWith("SENDLOGIN:") && !username.startsWith("SENDREGISTER:"))
			throw new IOException("No login/register received from client");
		
		if (username.startsWith("SENDLOGIN:") && pass.startsWith("SENDLOGIN:")) {
			boolean login = true;	//må skiftes til false når databasen har funksjoner for login
			
			name = username.substring(10);
			
		//	user = input.nextLine();
		//	pw = input.nextLine();
		// login = DatabaseTest.registerNewUser(user, pw);
			if(login) {
				output.write("CONNECTED");
				output.newLine();
				output.flush();
			}	
		} 
		else if (username.startsWith("SENDREGISTER:") && pass.startsWith("SENDREGISTER:")){
			boolean register = true; //må skiftes til false når databasen har funksjoner for registering.
			
			name = username.substring(13);
			
			//user = input.nextLine();
			//pw = input.nextLine();
			//register = DatabaseTest.registerNewUser(user, pw);
			if (register) {
				output.write("ACCEPTED");
				output.newLine();
				output.flush();
			}
		}
		
	}
	
	/*
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
				System.out.println("Connection closed");
			} catch (IOException ioE) {
				ioE.printStackTrace();
			}
		}
		
	}
	*/
}