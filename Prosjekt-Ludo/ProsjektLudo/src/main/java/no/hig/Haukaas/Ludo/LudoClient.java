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
	private JTextArea displayArea; //Displays chat/messages from the server
	private JPanel kommunikasjon;
	private JPanel spillBord;
	private JTextArea dialog;
    private JTextField textToSend;
    private JList<String> participants;
    private DefaultListModel<String> participantsModel;
    private BufferedWriter output;
    private BufferedReader input;
    private ExecutorService executorService;
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
				//TODO Petter
			}
		};
		chatButton.addActionListener(chatButtonListener);
		GUI.add(chatButton);
		
		add(GUI, BorderLayout.NORTH);
		
		// Set up the textarea used to display all messages
        dialog = new JTextArea();
        dialog.setEditable(false);
        dialog.setFont(new Font("Arial", Font.PLAIN, 26));
        add(new JScrollPane(dialog), BorderLayout.CENTER);

        // Set up the list of participants
        participants = new JList<String>(
                participantsModel = new DefaultListModel<String>());
        participants.setFixedCellWidth(160);
        participants.setFont(new Font("Arial", Font.PLAIN, 26));
        add(new JScrollPane(participants), BorderLayout.EAST);

        // Set up the textfield used to enter text to send
        textToSend = new JTextField();
        textToSend.setFont(new Font("Arial", Font.PLAIN, 26));
        add(textToSend, BorderLayout.SOUTH);
        // Add an actionlistener to the textfield
        textToSend.addActionListener(e -> {
            sendText(e.getActionCommand());
            textToSend.setText("");
        });
        textToSend.requestFocus();

        // Add a window listener, this sends a message indicating
        // to the server that the user is leaving (logging out)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sendText(">>>LOGOUT<<<");
            }
        });
        
		executorService = Executors.newCachedThreadPool();
		processConnection();
		executorService.shutdown();	
	}
	
	private void processConnection() {
		executorService.execute(() -> {
			while (true) {
				try {
	                String tmp = input.readLine();
	                if (tmp.startsWith("LOGIN:")) { // User is logging in
	                    addUser(tmp.substring(6));
	                } else if (tmp.startsWith("LOGOUT:")) { // User is logging out
	                    removeUser(tmp.substring(7));
	                } else { // All other messages
	                    displayMessage(tmp + "\n");
	                }
	            } catch (IOException ioe) {
	                JOptionPane.showMessageDialog(this, "Error receiving data: " + ioe);
	            }
			}
		});
	}

    /**
     * Used to add messages to the message area in a thread safe manner
     * 
     * @param text
     *            the text to be added
     */
    private void displayMessage(String text) {
        SwingUtilities.invokeLater(() -> dialog.append(text));
    }

    /**
     * Used to remove a user from the user list in a thread safe manner
     * 
     * @param username
     *            the name of the user to remove from the list
     */
    private void removeUser(String username) {
        SwingUtilities.invokeLater(() -> participantsModel
                .removeElement(username));
    }

    /**
     * Used to add a user to the user list in a thread safe manner
     * 
     * @param username
     *            the name of the user to add to the list
     */
    private void addUser(String username) {
        SwingUtilities
                .invokeLater(() -> participantsModel.addElement(username));
    }

    /**
     * Method used to send a message to the server. Handled in a separate method
     * to ensure that all messages are ended with a newline character and are
     * flushed (ensure they are sent.)
     * 
     * @param textToSend
     *            the message to send to the server
     */
    private void sendText(String textToSend) {
        try {
            output.write(textToSend);
            output.newLine();
            output.flush();
        } catch (IOException ioe) {
            JOptionPane
                    .showMessageDialog(this, "Error sending message: " + ioe);
        }
    }
}
