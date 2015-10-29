package globalServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GlobalServer extends JFrame{
	
	private ServerSocket server;
	private ExecutorService executorService;
	
	private Lock gameLock;
	private final int SERVERLIMIT = 100;
	
	private JTextArea outputArea;
	
	//private Player[] players;
	private ArrayList<Player> player = new ArrayList<Player>();
	
	private ArrayBlockingQueue<String> messages = new ArrayBlockingQueue<String>(50);
	
	private boolean shutdown = false;
	
	private int currentPlayer;
	private Condition otherPlayerConnected;
	private Condition otherPlayerTurn;
	
	public GlobalServer() {
		
		super("GlobalServer");
		
		outputArea = new JTextArea();
		outputArea.setFont(new Font("Ariel", Font.PLAIN, 14));
		outputArea.setEditable(false);
		add(new JScrollPane(outputArea));
		
		// create ExecutorService with a thread for each player
		//executorService = Executors.newCachedThreadPool();
		
		/*
		gameLock = new ReentrantLock();
		
		
		// condition variable for both players being connected
		otherPlayerConnected = gameLock.newCondition();
		
		// condition variable for the other player's turn
		otherPlayerTurn = gameLock.newCondition();
		
		*/
		
		//players = new Player[SERVERLIMIT];
		
		try {
			server = new ServerSocket(12347); // Set up serverSocket
			executorService = Executors.newCachedThreadPool();
			
			startLoginMonitor();
			startMessageSender();
			startMessageListener();
			
			executorService.shutdown();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		
		//outputArea = new JTextArea();
		//add(outputArea, BorderLayout.CENTER);
		//outputArea.setText("Server awaiting connections\n");
		
		setSize(600, 400);
		setVisible(true);
		
	}

	private void startMessageListener() {
		executorService.execute(() -> {
			while (!shutdown) {
				try {
					synchronized(player) {
						Iterator<Player> i = player.iterator();
						while (i.hasNext()) {
							Player p = i.next();
							try {
								String msg = p.read();
								if (msg != null && !msg.equals(">>>LOGOUT<<<"))
									messages.put(p.returnName() + "> " + msg);
								else if (msg != null) {
									i.remove();
									messages.put("LOGOUT:" + p.returnName());
									messages.put(p.returnName() + " logged out");
								}
							} catch (IOException ioe) {
								i.remove();
								messages.put("LOGOUT:" + p.returnName());
								messages.put(p.returnName() + " got lost in hyperspacve");
							}
						}
					}
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		});
	}
	
	private void startMessageSender() {
		executorService.execute(() -> {
			while (!shutdown) {
				try {
					String message = messages.take();
					displayMessage("Sending '" + message + "' to " + player.size() + " players\n");
					synchronized (player) {
						Iterator<Player> i = player.iterator();
						while (i.hasNext()) {
							Player p = i.next();
							try {
								p.sendText(message);
							} catch (IOException ioe) {
								i.remove();
								messages.add("LOGOUT:" + p.returnName());
								messages.add(p.returnName() + " got lost in hyperspace");
							}
						}
					}
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		});
	}
	
	private void startLoginMonitor() {
		executorService.execute(() -> {
			while (!shutdown) {
				try {
					Socket s = server.accept();
					Player p = new Player(s);
					messages.add(p.returnName() + " joined the conversation");
					synchronized (player) {
						player.add(p);
						Iterator<Player> i = player.iterator();
						while (i.hasNext()) {
							Player p1 = i.next();
							if (p != p1)
								try  {
									p.sendText("LOGIN:" + p1.returnName());
								} catch (IOException ioe) {
									// Lost connection
								}
						}
					}
					displayMessage("PLAYER CONNECTED:" + p.returnName() + "\n");
					try {
						messages.put("LOGIN:" + p.returnName());
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				} catch (IOException ioe) {
					displayMessage("CONNECTION ERROR: " + ioe + "\n");
				}
			}
		});
	}
	
	private void displayMessage(String text) {
		SwingUtilities.invokeLater(() -> outputArea.append(text));
	}
	
	
	
	/*
	
	public void execute() {
		// wait for each client to connect
		for (int i=0; i<player.length; i++) {
			try { // wait for connection, create Player, start runnable
				//players[i] = new Player(server.accept(), i);
				Player p =  new Player(server.accept());
				player.add(p);
				displayMessage("Player " + i + " connected\n");
				executorService.execute(players[i]);
			} catch (IOException ioE) {
				ioE.printStackTrace();
			}
		}
		
		
		gameLock.lock();
		
		try {
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
	*/
	
	
}