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
	private String password;

	public Player(Socket connection) throws IOException {
		this.connection = connection;
		
		input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));	
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
	
	public boolean loginChecker() {
		try {
			String tempName = input.readLine();
			String tempPass = input.readLine();
			
			if (!tempName.startsWith("SENDLOGIN:") && !tempName.startsWith("SENDREGISTER:"))
				return false;
			
			if (tempName.startsWith("SENDLOGIN:") && tempPass.startsWith("SENDLOGIN:")) {
				int login = 0;
				
				name = tempName.substring(10);
				password = tempPass.substring(10);
							
				login = DatabaseHandler.userLogin(name, password);
				if(login > 0) {
					output.write(login);
					output.newLine();
					output.flush();
					return true;
				}
				else if (login == 0) {
					output.write(login);
					output.newLine();
					output.flush();
					return false;
				}
			}
			else if (tempName.startsWith("SENDREGISTER:") && tempPass.startsWith("SENDREGISTER:")){
				boolean register = false; 
				
				name = tempName.substring(13);
				password = tempPass.substring(13);
				register = DatabaseHandler.registerNewUser(name, password);
			
				if (register) {
					output.write("ACCEPTED");
					output.newLine();
					output.flush();
					return true;
				}
				else {
					output.write("DECLINED");
					output.newLine();
					output.flush();
					return false;
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return false;
	}
}
