package globalServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GlobalServer extends JFrame{
	
	private ServerSocket server;
	private ExecutorService runGame;
	private Lock gameLock;
	
	private JTextArea outputArea;
	
	private Player[] players;
	private int currentPlayer;
	private Condition otherPlayerConnected;
	private Condition otherPlayerTurn;
	private final static int PLAYER_X = 0;
	private final static int PLAYER_Y = 1;
	
	public GlobalServer() {
		
		super("GlobalServer");
		
		// create ExecutorService with a thread for each player
		runGame = Executors.newFixedThreadPool(2);
		gameLock = new ReentrantLock();
		
		// condition variable for both players being connected
		otherPlayerConnected = gameLock.newCondition();
		
		// condition variable for the other player's turn
		otherPlayerTurn = gameLock.newCondition();
		
		players = new Player[2];
		
		currentPlayer = PLAYER_X;
		
		try {
			server = new ServerSocket(12347, 3); // Set up serverSocket
		} catch (IOException ioE) {
			ioE.printStackTrace();
		}
		
		outputArea = new JTextArea();
		add(outputArea, BorderLayout.CENTER);
		outputArea.setText("Server awaiting connections\n");
		
		setSize(300, 300);
		setVisible(true);
		
	}

	public void execute() {
		// wait for each client to connect
		for (int i=0; i<players.length; i++) {
			try { // wait for connection, create Player, start runnable
				players[i] = new Player(server.accept(), i);
				displayMessage("Player " + i + " connected");
				runGame.execute(players[i]);
			} catch (IOException ioE) {
				ioE.printStackTrace();
			}
		}
		
		gameLock.lock();
		
		try {
			players[PLAYER_X].setSuspended(false);
			otherPlayerConnected.signal();
		} finally {
			gameLock.unlock(); 
		}	
	}
	
	
	public void displayMessage(final String messageToDisplay) {
		// display message from event dispatch thread of execution
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() { // updates outputArea
						outputArea.append(messageToDisplay);
					}
				}
		);		
					
	}
	
	
	
}