package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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
    private JList<String> chatGroups;
    private DefaultListModel<String> chatGroupsModel;
    private BufferedWriter output;
    private BufferedReader input;
    private ExecutorService executorService;
    
    private ArrayList<Chat> chat = new ArrayList<Chat>();
    
    private JButton newChatGroup;
    private String groupChatName;
	
	public ChatClient(String host, Socket socket, BufferedWriter writer, BufferedReader reader) {
		
		this.ludoClientHost = host;
		this.connection = socket;
		this.output = writer;
		this.input = reader;
		
		Chat mainChat = new Chat(output, input, "GlobalChatRoom");
		chat.add(mainChat);
		addUser(mainChat.returnName());
		
		newChatGroup = new JButton("New chat group");
		newChatGroup.setPreferredSize(new Dimension(50, 50));
		add(newChatGroup, BorderLayout.NORTH);
		
		ActionListener chatButtonListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				groupChatName = JOptionPane.showInputDialog("Group chat name");
				if (groupChatName != null && !groupChatName.equals("")) {
					sendText("NEWGROUPCHAT:" + groupChatName.substring(0, 5));
					try {
						if(processChatRequest(input.readLine())) {
							Chat c = new Chat(output, input, groupChatName);
							chat.add(c);
							addUser(c.returnName());
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					
				}
			}		
		}; 
		newChatGroup.addActionListener(chatButtonListener);
		
        // Set up the list of participants
        chatGroups = new JList<String>(chatGroupsModel = new DefaultListModel<String>());
        chatGroups.setFixedCellWidth(160);
        chatGroups.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(chatGroups), BorderLayout.EAST);
        
		executorService = Executors.newCachedThreadPool();
		processConnection();
		executorService.shutdown();	
		
		setSize(600, 400);
		setVisible(true);
	}
	
	private boolean processChatRequest(String respons) {
		if (respons.equals("ERRORCHAT")) {
			JOptionPane.showMessageDialog(null, "Chat group already exits");
			return false;
		}
		return true;
	}
	
	private void processConnection() {
		executorService.execute(() -> {
			while (true) {
				try {
	                String tmp = input.readLine();
	                if (tmp.startsWith("NEWCHAT:")) { 
	                    addUser(tmp.substring(8));
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
        SwingUtilities.invokeLater(() -> chatGroupsModel
                .removeElement(username));
    }

    /**
     * Used to add a user to the user list in a thread safe manner
     * 
     * @param username
     *            the name of the user to add to the list
     */
    private void addUser(String chatName) {
        SwingUtilities
                .invokeLater(() -> chatGroupsModel.addElement(chatName));
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
