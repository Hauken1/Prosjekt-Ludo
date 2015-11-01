package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatClient extends JFrame{

	private String ludoClientHost; //host name for server
	private Socket connection;
	private JTextArea dialog;
    private JTextField textToSend;
    private JList<String> participants;
    private DefaultListModel<String> participantsModel;
    private BufferedWriter output;
    private BufferedReader input;
    private ExecutorService executorService;
    
    private JButton newChatGroup;
    private String groupChatName;
	
	public ChatClient(String host, Socket socket, BufferedWriter writer, BufferedReader reader) {
		
		this.ludoClientHost = host;
		this.connection = socket;
		this.output = writer;
		this.input = reader;
		
		newChatGroup = new JButton("newChatGroup");
		newChatGroup.setBounds(10, 80, 50, 25);
		add(newChatGroup, BorderLayout.NORTH);
		
		ActionListener chatButtonListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				groupChatName = JOptionPane.showInputDialog("Group chat name");
				if (groupChatName != null || !groupChatName.equals("")) {
					// do something with the name
				}
					
				
			}		
		}; 
		newChatGroup.addActionListener(chatButtonListener);
		
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
		
		setSize(600, 400);
		setVisible(true);
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
