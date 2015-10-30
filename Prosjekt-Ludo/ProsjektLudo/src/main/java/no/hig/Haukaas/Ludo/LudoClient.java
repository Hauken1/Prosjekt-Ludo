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
		
		//spillBord = new JPanel();
		//kommunikasjon = new JPanel();
		
		GUI = new JPanel();
		
		displayArea = new JTextArea(4, 30);
		displayArea.setEditable(true);
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);
		
		spillEtSpillButton = new JButton("Spill");
		spillEtSpillButton.setPreferredSize(new Dimension(100,100));
		GUI.add(spillEtSpillButton);
		
		chatButton = new JButton("Chat");
		chatButton.setPreferredSize(new Dimension(100,100));
		GUI.add(chatButton);
		
		ActionListener spillButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
			}
		};
		spillEtSpillButton.addActionListener(spillButtonListener);
		
		ActionListener chatButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
			}
		};
		chatButton.addActionListener(chatButtonListener);
		
		add(GUI, BorderLayout.NORTH);
		
		try {	//Prøver å lage spill bordet
			LudoBoard board = new LudoBoard();
			add(board, BorderLayout.CENTER);
		} catch (Exception e) {
			System.out.println("Noe feil med brettet");
		}
		
		//boardPanel = new JPanel(); //Kan brukes for å vise spillet
		//boardPanel.setLayout(new GridLayout(3,3,0,0));	//Setter hvordan panelet skal se ut
			
		//idField = new JTextField(); //Set ut textfield
		//idField.setEditable(false);
		//add(idField, BorderLayout.NORTH);		
		//panel.add(boardPanel, BorderLayout.CENTER); 
		//add(kommunikasjon);
		
		setSize(1000, 1000);
		setVisible(true);		
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
