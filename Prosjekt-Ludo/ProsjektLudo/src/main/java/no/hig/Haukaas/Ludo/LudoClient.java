package no.hig.Haukaas.Ludo;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LudoClient extends JFrame implements Runnable {
	private String ludoClientHost; //host name for server
	private Socket connection;
	private int playerID;
	private JTextArea displayArea; //Displays chat/messages from the server
	private JPanel kommunikasjon;
	private JPanel GUI;
	private JButton spillEtSpillButton;
	private JButton chatButton;
	
	/**
	 * Constructor for the Ludo client. 
	 * Makes a JFrame window where players can interact with each other and join games.
	 * 
	 * @param host	IP address of the server
	 * @param socket	Connection to the server
	 * @param spillerID	PlayerID retrived from the Database.
	 */
	public LudoClient(String host, Socket socket, int spillerID) {
		super("Ludo Klient");
		
		ludoClientHost = host;
		connection = socket;
		playerID = spillerID;
		
		setUpGUILudoClient();
					
		setSize(1000, 1000);
		setVisible(true);		
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void doSpillButtonListener() {
		setVisible(false);
		GameClient gameClient;
		gameClient = new GameClient(ludoClientHost, connection, playerID);
		gameClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void setUpGUILudoClient() {
		GUI = new JPanel();
		
		displayArea = new JTextArea(4, 30);
		displayArea.setEditable(true);
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);
		
		spillEtSpillButton = new JButton("Spill");
		spillEtSpillButton.setPreferredSize(new Dimension(300,300));
		GUI.add(spillEtSpillButton);
		
		chatButton = new JButton("Chat");
		chatButton.setPreferredSize(new Dimension(300,300));	//Overdrevent, I know
		GUI.add(chatButton);
		
		ActionListener spillButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doSpillButtonListener();
			}
		};
		spillEtSpillButton.addActionListener(spillButtonListener);
		
		ActionListener chatButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//TODO Petter
			}
		};
		chatButton.addActionListener(chatButtonListener);
		
		add(GUI, BorderLayout.NORTH);
	}
}
