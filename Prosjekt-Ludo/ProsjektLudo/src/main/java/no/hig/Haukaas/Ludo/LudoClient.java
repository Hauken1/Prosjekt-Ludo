package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import globalServer.Player;

public class LudoClient extends JFrame {
	private String ludoClientHost; //host name for server
	private Socket connection;
	private int playerID;
	private JPanel kommunikasjon;
	private JPanel spillBord;
    private BufferedWriter output;
    private BufferedReader input;
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
	public LudoClient(String host, Socket socket, BufferedWriter writer, BufferedReader reader, int spillerID) {
		super("Ludo Klient");
		
		output = writer;
		input = reader;
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
		gameClient = new GameClient(ludoClientHost, connection, playerID, output, input);
		gameClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void doChatButtonListener() {
		setVisible(false);
		ChatClient chatClient = new ChatClient(ludoClientHost, connection, output, input);
		chatClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setUpGUILudoClient() {
		GUI = new JPanel();
		
		spillEtSpillButton = new JButton("Spill");
		spillEtSpillButton.setPreferredSize(new Dimension(300,300));
		ActionListener spillButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doSpillButtonListener();
			}
		};
		spillEtSpillButton.addActionListener(spillButtonListener);
		GUI.add(spillEtSpillButton);
		
		chatButton = new JButton("Chat");
		chatButton.setPreferredSize(new Dimension(300,300));	//Overdrevent, I know
		ActionListener chatButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doChatButtonListener();
			}
		};
		chatButton.addActionListener(chatButtonListener);
		GUI.add(chatButton);
		
		add(GUI, BorderLayout.NORTH);
		
	}
}
