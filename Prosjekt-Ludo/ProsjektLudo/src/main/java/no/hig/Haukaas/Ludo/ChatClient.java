package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.Container;
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

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatClient extends JFrame{

	private String ludoClientHost; //host name for server
	private Socket connection;
	
    private JList<String> chatGroups;
    private DefaultListModel<String> chatGroupsModel;
    private BufferedWriter output;
    private BufferedReader input;
    private ExecutorService executorService;
    
    private ArrayList<Chat> chatList = new ArrayList<Chat>();
    
    private JButton newChatGroup;
    private String groupChatName;
    
    private String clientUserName;
    
    private JTextArea dialog;
    private JTextField textToSend;
    private JList<String> participantsChat;
    private DefaultListModel<String> participantsModelChat;
	
    private JTabbedPane tabs;
    
	public ChatClient(String host, Socket socket, BufferedWriter output, BufferedReader input, String userName) {
		
		this.ludoClientHost = host;
		this.connection = socket;
		this.output = output;
		this.input = input;
		this.clientUserName = userName;
		
		tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT); //Lagger en ny tabbPane
		
		addChat(null); // Legger bare til noe i group chat listen for å få å kunne kjøre for loop
		
		newChatGroup = new JButton("New chat group");	//JButton knapp for ny chat
		newChatGroup.setPreferredSize(new Dimension(200, 100));
		add(newChatGroup, BorderLayout.EAST);
		
			// Legg til listener for om vinduet
		addWindowListener(new WindowAdapter() {
	    	@Override
	    	public void windowClosing(WindowEvent e) {
	    		sendText(">>>LOGOUT<<<");	//Sender melding til serveren om logout
	    	}
		});
		
			// action listener for newChatGroup 
		ActionListener chatButtonListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				groupChatName = JOptionPane.showInputDialog("Group chat name");
				if (groupChatName != null && !groupChatName.equals("")) { //Passer på at noe blir skrevet in
					sendText("NEWGROUPCHAT:" + groupChatName);	//Sender melding om ny group chat skal lages med navn
				}
			}		
		}; 
		newChatGroup.addActionListener(chatButtonListener);
		
        // List for håndtering av chate grupper navn
        chatGroups = new JList<String>(chatGroupsModel = new DefaultListModel<String>());
        chatGroups.setFixedCellWidth(160);
        chatGroups.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(chatGroups), BorderLayout.EAST);
   
		executorService = Executors.newCachedThreadPool(); // Lager et pool av threads for bruk
		processConnection(); // Starter en ny evighets tråd som tar seg av meldinger fra server
		executorService.shutdown();	// Dreper tråden når klassen dør
		
		add(newChatGroup, BorderLayout.NORTH);
		add(tabs, BorderLayout.CENTER);
		
		setSize(1200, 800);
		setVisible(true);
	}
	
	/**
	 * Lager et nytt box object med elementer for håndtering av
	 * vising av chat og skrive in chat melding som skal sendes
	 * og legge til ny brukere i en oversikt liste
	 * @param chatName Tar navnet på chaten
	 * @return Box tab
	 */
	private Box addBoxToLayout(String chatName) {
		
		
		Box tmp = Box.createVerticalBox();
		
		// Set up the textarea used to display all messages
	    dialog = new JTextArea();
	    dialog.setEditable(false);
	    dialog.setPreferredSize(new Dimension(1, 360));
	    dialog.setFont(new Font("Arial", Font.PLAIN, 26));
	    tmp.add(new JScrollPane(dialog));

	    // Set up the list of participants
	    participantsChat = new JList<String>(participantsModelChat = new DefaultListModel<String>());
	    participantsChat.setFixedCellWidth(80);
	    participantsChat.setFont(new Font("Arial", Font.PLAIN, 26));
	    tmp.add(new JScrollPane(participantsChat));

	    // Set up the textfield used to enter text to send
	    textToSend = new JTextField();
	    textToSend.setFont(new Font("Arial", Font.PLAIN, 26));
	    tmp.add(textToSend);
	    // Add an actionlistener to the textfield
	    textToSend.addActionListener(e -> {
	        sendText(groupChatName + ":" + e.getActionCommand()); //Sender chat melding til alle brukere
	        textToSend.setText(""); // Setter feltet til ingenting etter noe sendt.
	    });
	    textToSend.requestFocus();
	    
	    tmp.add(Box.createVerticalStrut(25));
	    
		return tmp;
	}
	
	/**
	 * Denne tar seg av håndtering av meldinger som blir sendt fra server.
	 * Looper hele tiden og lager seg en ny tråd når en ny oppgave skal gjøres.
	 */
	private void processConnection() {
		executorService.execute(() -> {
			while (true) {
				try {
	                String tmp = input.readLine();
	                
	                if (tmp.startsWith("NEWGROUPCHAT:")) { //Legger til ny chatTab
	                	
	                	tabs.addTab(tmp.substring(13), addBoxToLayout(tmp.substring(13)));
	                	addChat(tmp.substring(13));
	                	           	
	                	sendText(tmp.substring(13) + "JOIN:" + clientUserName); // Sender ut at brukern også vil joine chaten.
	                }
	                else if (tmp.equals("ERRORCHAT")) {	// Forteller at chaten finnes allerede
	                	JOptionPane.showMessageDialog(this, "Chat group already exits");
	                }
	                	                
	                for (int i=1; i<chatGroupsModel.size(); i++) { // Looper igjen alle groupChatene som finnes i lsiten
	                	
		                if (tmp.startsWith(chatGroupsModel.get(i) + "JOIN:")){	// Sjekker om noen har lyst å joine
		                	if (participantsModelChat.contains(clientUserName)) {	//Vist personen finnes allerede
		                		addUser(tmp.substring(chatGroupsModel.get(i).length() + 5));	//Legger til brukern som kom in
		                		removeUser(clientUserName);	//Fjerner klient brukern fra listen
		                		sendText(chatGroupsModel.get(i) + "JOIN:" + clientUserName); // Sender klient som lyst å joine til chaten
		                	}
		                	else
		                		addUser(tmp.substring(chatGroupsModel.get(i).length() + 5));	//Legger til bruker vist ikke finnes allerede	                	
		                }
		                else if (tmp.startsWith(chatGroupsModel.get(i) + "OUT:")) { // Mottar melding om at noen har logget ut
		                    removeUser(tmp.substring(chatGroupsModel.get(i).length() + 4)); // Fjerner bruker fra listen
		                }
		                else if (tmp.startsWith(chatGroupsModel.get(i) + ":")) { // Tar alle andre meldinger
		                    displayMessage(tmp.substring(chatGroupsModel.get(i).length() + 1) + "\n"); //Vist det i JTextArea for chat
		                }
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
     * Used to add a chat to the chat list in a thread safe manner
     * @param chatName legger til ny chat i listen
     */
    private void addChat(String chatName) {
    	
        SwingUtilities
                .invokeLater(() -> chatGroupsModel.addElement(chatName));
    }
    
    /**
     * Used to remove a user from the user list in a thread safe manner
     * 
     * @param username
     *            the name of the user to remove from the list
     */
    private void removeUser(String username) {
        SwingUtilities.invokeLater(() -> participantsModelChat
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
	            .invokeLater(() -> participantsModelChat.addElement(username));
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
