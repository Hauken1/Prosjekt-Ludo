package globalServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Player implements Runnable {
	
	private Socket connection;
	private Scanner input;
	private Formatter output;
	
	private BufferedReader inpute;
	private BufferedWriter outpute;
	
	private String name;
	//private int playerNumber;
	private boolean suspended = true;

	public Player(Socket connection) throws IOException {
		this.connection = connection;
		
		//try {
			//input = new Scanner(connection.getInputStream());
			inpute = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			//output = new Formatter(connection.getOutputStream());
			outpute = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			
			name = inpute.readLine();
			if (!name.startsWith("LOGIN:"))
				throw new IOException("No login received from client");
			name = name.substring(6);
		//} catch (IOException ioE) {
			//ioE.printStackTrace();
			
		
	}
	
	public void close() throws IOException {
		inpute.close();
		outpute.close();
		connection.close();
	}
	
	public void sendText(String text) throws IOException {
		outpute.write(text);
		outpute.newLine();
		outpute.flush();
	}
	
	public String read() throws IOException {
		if (inpute.ready())
			return inpute.readLine();
		return null;
	}
	
	public void setSuspended(boolean status) {
		suspended = status;
	}
	
	public boolean online() {
		return true; 
	}
	
	public String returnName() {
		return name;
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
				System.out.println("Connection closed");
			} catch (IOException ioE) {
				ioE.printStackTrace();
			}
		}
		
	}
	
}