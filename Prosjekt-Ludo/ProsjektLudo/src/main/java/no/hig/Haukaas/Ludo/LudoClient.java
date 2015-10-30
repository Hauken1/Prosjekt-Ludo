package no.hig.Haukaas.Ludo;


import java.awt.BorderLayout;
import java.awt.Font;
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
import java.net.Socket;
import java.util.Formatter;
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

public class LudoClient extends JFrame {
	private String ludoClientHost; //host name for server
	private JTextArea displayArea; //Displays chat/messages from the server
	private JPanel kommunikasjon;
	private JPanel spillBord;
	
	private JTextArea dialog;
    private JTextField textToSend;
    private JList<String> participants;
    private DefaultListModel<String> participantsModel;
    
    private BufferedWriter output;
    private BufferedReader input;
    private Socket connection;
	
	public LudoClient(String host, Socket connection, BufferedWriter output, BufferedReader input) {
		super("Ludo Klient");
		ludoClientHost = host;
		this.connection = connection;
		this.output = output;
		this.input = input;
		
		//spillBord = new JPanel();
		//kommunikasjon = new JPanel();
		
		//boardPanel = new JPanel(); //Kan brukes for å vise spillet
		//boardPanel.setLayout(new GridLayout(3,3,0,0));	//Setter hvordan panelet skal se ut
			
		//idField = new JTextField(); //Set ut textfield
		//idField.setEditable(false);
		//add(idField, BorderLayout.NORTH);		
		//panel.add(boardPanel, BorderLayout.CENTER); 
		//add(kommunikasjon);
		
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
        setSize(600, 400);
        setVisible(true);
        
	}
	
	/**
     * This method handles the communication from the server. Note that this
     * method never returns, messages from the server is read in a loop that
     * never ends. All other user interaction is handled in the GUI thread.
     * 
     * Login and logout messages is used to add/remove users to/from the list of
     * participants while all other messages are displayed.
     */
    public void processConnection() {
        while (true) {
        	System.out.println("Hello!");
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
                JOptionPane.showMessageDialog(this, "Error receiving data: "
                        + ioe);
            }
        }
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
