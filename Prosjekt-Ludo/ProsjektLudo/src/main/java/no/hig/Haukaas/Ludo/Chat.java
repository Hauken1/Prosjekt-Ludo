package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Chat extends JFrame {

	private JTextArea dialog;
    private JTextField textToSend;
    private JList<String> participants;
    private DefaultListModel<String> participantsModel;
    private BufferedWriter output;
    private BufferedReader input;
    private ExecutorService executorService;
    
    private String chatName;
    private String clientUserName;
    
	
	public Chat(BufferedWriter output, BufferedReader input, String name, String userName) {
		super(name);
		this.output = output;
		this.input = input;
		this.chatName = name;
		this.clientUserName = userName;
		
		//sendText(chatName + "JOIN:" + clientUserName);
		
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
	    	System.out.println("Chat: " + chatName + ":" + e.getActionCommand() +"\n");
	        sendText(chatName + ":" + e.getActionCommand());
	        textToSend.setText("");
	    });
	    textToSend.requestFocus();
	    
	    addWindowListener(new WindowAdapter() {
	    	@Override
	    	public void windowClosing(WindowEvent e) {
	    		sendText(chatName + "OUT:" + clientUserName);
	    		//executorService.shutdown();
	    	}
		});
	    
		//executorService = Executors.newCachedThreadPool();
		//processConnection();
		//executorService.shutdown();
		
		setSize(600, 400);
		setVisible(true);
		
	}
	

	private void processConnection() {
		executorService.execute(() -> {
			while (true) {
				try {
					System.out.println("Chat: Hei er jeg her noen gang?");
	                String tmp = input.readLine();
	                if (tmp.startsWith(chatName + "JOIN:")){
	                	System.out.println("Chat: Inne i if'en");
	                	if (participantsModel.contains(clientUserName)) {
	                		System.out.println("Chat: Inne i neste if'en");
	                		addUser(tmp.substring(chatName.length() + 5));
	                		removeUser(clientUserName);
	                		sendText(chatName + "JOIN:" + clientUserName);
	                	}
	                	else
	                		addUser(tmp.substring(chatName.length() + 5));	                	
	                }
	                else if (tmp.startsWith(chatName + "OUT:")) { // User is logging out {
	                	System.out.println("Chat: inne i første else if");
	                    removeUser(tmp.substring(chatName.length() + 4));
	                }
	                else if (tmp.startsWith(chatName + ":")) { // All other messages
	                	System.out.println("Chat: inne i andre else if");
	                    displayMessage(tmp.substring(chatName.length() + 1) + "\n");
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
	
	public String returnName() {
		return chatName;
	}
}
