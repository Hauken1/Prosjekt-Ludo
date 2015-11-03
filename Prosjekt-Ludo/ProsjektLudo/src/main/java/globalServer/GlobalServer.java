package globalServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.List;
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
	
	private JTextArea outputArea;
	
	private ArrayList<Player> player = new ArrayList<Player>();
	
	private ArrayBlockingQueue<String> messages = new ArrayBlockingQueue<String>(50);
	
	private ArrayList<String> groupChatList = new ArrayList<String>();
	
	private boolean shutdown = false;
	
	public GlobalServer() {
		
		super("GlobalServer");
		
		groupChatList.add("Globa");
		
		outputArea = new JTextArea();
		outputArea.setFont(new Font("Ariel", Font.PLAIN, 14));
		outputArea.setEditable(false);
		add(new JScrollPane(outputArea), BorderLayout.CENTER);
		outputArea.setText("Server awaiting connections\n");
		
		try {
			server = new ServerSocket(12347); // Set up serverSocket
			executorService = Executors.newCachedThreadPool();
			
			startLoginMonitor();
			//groupChatMonitor();
			startMessageSender();
			startMessageListener();
			
			executorService.shutdown();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		
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
								if (msg != null && msg.startsWith("NEWGROUPCHAT:")) {
									groupChatList.add(msg.substring(13));
									displayMessage("New chat room: " + msg.substring(13) + " made by: " + p.returnName() + "\n");
								}
								else if (msg != null && groupChatList.contains(msg.substring(0, 5)))
									messages.put(msg.substring(0, 5) + p.returnName() + "> " + msg.substring(5));
								else if (msg != null) {
									i.remove();
									messages.put("LOGOUT:" + msg.substring(0, 5)+ p.returnName());
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
	
	/*
	private void groupChatMonitor() {
		executorService.execute(() -> {
			while (!shutdown) {
					synchronized(player) {
						Iterator<Player> i = player.iterator();
						while (i.hasNext()) {
							Player p = i.next();
							try {
								String msg = p.read();
								if (msg != null && msg.startsWith("NEWGROUPCHAT:") && !groupChatList.contains(msg.substring(13))) {
									groupChatList.add(msg.substring(13));
									displayMessage("New chat room: " + msg.substring(13) + " made by: " + p.returnName() + "\n");
								} else if (groupChatList.contains(msg.substring(13))) {
									p.sendText("ERRORCHAT");
								}
								
							} catch (IOException ioe) {
								ioe.printStackTrace();
							}
						}
					}
				
			}
		});
	}
	*/
	
	private void displayMessage(String text) {
		SwingUtilities.invokeLater(() -> outputArea.append(text));
	}
	
}