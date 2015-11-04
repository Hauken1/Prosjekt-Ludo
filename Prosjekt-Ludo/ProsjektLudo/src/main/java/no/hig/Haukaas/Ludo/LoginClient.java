package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginClient extends JFrame {
	private String LudoClienthost; //host name for server
	private JTextArea displayArea; 
	private JPanel panel; 
	private JLabel userName;
	private JLabel passWord; 
	private JTextField userType;
	private JTextField passType;
	private JButton loginButton;
	private JButton registerButton;
	private Socket connection; //connection to server
	private BufferedWriter output; //output from server
	private BufferedReader input; //input to server
	
	/**
	 * Constructor for the login Client. Makes necessary objects/elements
	 * to be able to login and register.
	 * Starts the connection with the server.
	 * @param host contains the IP for the server
	 */
	public LoginClient(String host) {
		super("Ludo Login");
		LudoClienthost = host; 

		setUpGUILoginClient();
		
		setSize(300, 200);
		setVisible(true);
	}
	
	/**
     * Connects to the server, on port 12345 and localhost. This would be
     * changed for a production version. Once the socket connection is
     * established a bufferedReader and a bufferedWriter is created, this is our
     * input and output.
     */
    public void connect() {
        try {
            connection = new Socket(LudoClienthost, 12347);
            displayArea.append("Online");
            output = new BufferedWriter(new OutputStreamWriter(
                    connection.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            
        } catch (IOException ioe) { // If we are unable to connect, alert the
                                    // user and exit
            JOptionPane.showMessageDialog(this, "Error connecting to server: "
                    + ioe);
            displayArea.append("Offline");
        }
    }
    
    /**
     * Re connects to the server, on port 12345 and localhost. This would be
     * changed for a production version. Once the socket connection is
     * established a bufferedReader and a bufferedWriter is created, this is our
     * input and output.
     * 
     */
    public void reconnect() {
        try {
            connection = new Socket(LudoClienthost, 12347);
            output = new BufferedWriter(new OutputStreamWriter(
                    connection.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            
        } catch (IOException ioe) { // If we are unable to connect, alert the
                                    // user and exit
            JOptionPane.showMessageDialog(this, "Error connecting to server: "
                    + ioe);
            displayArea.append("Offline");
        }
    }
    
    /**
     * Sends two given String's to the server. So data the username and passord can
     * be checked up against the database
     * @param username to be sent
     * @param password to be sent
     */
    private void sendInfo(String username, String password) {
        try {
            output.write(username);
            output.newLine();
            output.flush();
            output.write(password);
            output.newLine();
            output.flush();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "Error sending message: " + ioe);
        }
    }
    	
	/**
	 * Method that contains most of the functionality of the ActionListener of LoginButton.
	 * Sends the text that is in the JTextField of the GUI to the server, and tries to
	 * log in. If accepted, the user will be brought to a new client
	 */
	public void doLoginButtonListener() {
		String textUsername = userType.getText();
		String textPassword = passType.getText();
		
			// Makes sure that something is writen in
		if (textUsername != null && !textUsername.equals("") &&
				textPassword != null && !textPassword.equals("")) {
		
			try {
				sendInfo("SENDLOGIN:" + textUsername, "SENDLOGIN:" + textPassword);	// send username and passord to server
				int n = input.read();	// reads the respons from the server
				if (n == 0) {	//if wrong
					reconnect();	//reconnets to the server
				}
				if (n > 0) {	//Logger inn, hvis true
					setVisible(false);
					LudoClient client = new LudoClient(LudoClienthost, connection, output, input, n);
					client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
					
				}
				else JOptionPane.showMessageDialog(null, "Wrong password or username. Please try again");
						
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Login went wrong. Please try again");
			}
		} else
			JOptionPane.showMessageDialog(null, "Username and password needed");
	}
  	
	/**
	 * Method that contains most of the functionality of the ActionListener of RegisterButton
	 * Makes a JOptionpane where the user can type in their username and password, and then
	 * sends it to the server and database.  
	 */
	public void doRegisterButtonListener() {
		int answer;
				
		JTextField user = new JTextField();
		JTextField pass = new JTextField();
				
		JPanel editorPanel = new JPanel();
		editorPanel.add(new JLabel("Username:"));
		editorPanel.add(user);
		editorPanel.add(new JLabel("Password"));
		editorPanel.add(pass);
		editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.Y_AXIS));
				
		answer = JOptionPane.showConfirmDialog(null, editorPanel, "Chose your Username and Password", JOptionPane.OK_CANCEL_OPTION);
					
		if (answer == JOptionPane.OK_OPTION) {
  			String textUsername = user.getText();
  			String textPassword = pass.getText();
  					
  				// Makes sure that something is writen in
  			if (textUsername != null && !textUsername.equals("") &&
  					textPassword != null && !textPassword.equals("")) {
  	  			try {
	  				sendInfo("SENDREGISTER:" + textUsername, "SENDREGISTER:" + textPassword); // sends username nad password to server
	  				if(processLogin(input.readLine())) // Checks the server respons
	  					JOptionPane.showMessageDialog(null, "Account created. Login to play Ludo");
	  				else
	  					reconnect();	// reconnect to the server if declined by the server
	  			} catch (Exception e1) {
	  				JOptionPane.showMessageDialog(null, "Register went wrong. Please try again");
	  			}
  			} else
  				JOptionPane.showMessageDialog(null, "Username and password needed");
		}
	}
	/**
	 * Process messages received by the client for registration
	 * @param login String containing message from the server
	 * @return return true if the registration process is successful, if not return false. 
	 */
	private boolean processLogin(String login) {
		if (login.equals("ACCEPTED")) return true;
		else {
			JOptionPane.showMessageDialog(null, "User already exists. Please try again");
			return false;
		}
	}
	
	void setUpGUILoginClient() {
		
		panel = new JPanel(); 
		panel.setLayout(null);
		userName = new JLabel("User name:");
		userName.setBounds(10,10, 80, 25);
		panel.add(userName);
		
		userType = new JTextField(20);
		userType.setBounds(100, 10, 160, 25);
		panel.add(userType);
		
		passWord = new JLabel("Password:");
		passWord.setBounds(10,40, 80, 25);
		panel.add(passWord);
		
		passType = new JTextField(20);
		passType.setBounds(100, 40, 160, 25);
		panel.add(passType);
		
		loginButton = new JButton("Login");
		loginButton.setBounds(10, 80, 100, 25);
		panel.add(loginButton);
		
		registerButton = new JButton("Register");
		registerButton.setBounds(160, 80, 100, 25);
		panel.add(registerButton);
		
		displayArea = new JTextArea();
		displayArea.setBounds(10, 120, 100, 15);
		displayArea.setEditable(false);
		panel.add(displayArea);
		
		ActionListener loginButtonListener = new ActionListener() {
		
			public void actionPerformed(ActionEvent event) {
				doLoginButtonListener();
			}		
		}; 
		loginButton.addActionListener(loginButtonListener);
		
		ActionListener registerButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doRegisterButtonListener();
			}
		};
		registerButton.addActionListener(registerButtonListener);
	
		add(panel, BorderLayout.CENTER);
	}
}
