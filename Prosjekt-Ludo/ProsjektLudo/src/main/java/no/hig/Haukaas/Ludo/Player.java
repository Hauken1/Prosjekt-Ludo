package no.hig.Haukaas.Ludo;

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
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}