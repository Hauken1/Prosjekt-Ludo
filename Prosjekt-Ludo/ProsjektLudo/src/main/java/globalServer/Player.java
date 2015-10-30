package globalServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Player {
	
	private Socket connection;
	//private Scanner input;
	//private Formatter output;
	
	private BufferedReader input;
	private BufferedWriter output;
	
	private String name;
	//private int playerNumber;
	//private boolean suspended = true;

	public Player(Socket connection) throws IOException {
		this.connection = connection;
		
		//try {
			//input = new Scanner(connection.getInputStream());
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			//output = new Formatter(connection.getOutputStream());
			output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			
			//loginChecker(input.readLine(), input.readLine());
			
			name = input.readLine();
			if (!name.startsWith("LOGIN:"))
				throw new IOException("No login received from client");
			name = name.substring(6);
		//} catch (IOException ioE) {
			//ioE.printStackTrace();
			
		
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
				System.out.println("Connection closed");
			} catch (IOException ioE) {
				ioE.printStackTrace();
			}
		}
		
	}
	*/
}