package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import javax.swing.SwingUtilities;

public class LoginClient extends JFrame implements Runnable {
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
	private Scanner input; //input from server
	private Formatter output; //output to server
	
	/**
	 * Constructor for the login Client. Makes necessary objects/elements
	 * to be able to login and register.
	 * Starts the connection with the server.
	 * @param host contains the IP for the server
	 */
	public LoginClient(String host) {
		super("Ludo Login");
		LudoClienthost = host; 
		
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
		
		setSize(300, 200);
		setVisible(true);
		startClient();
	}
	/**
	 * Starts the connection with the server with the given IP and Port.
	 */
	public void startClient() {
		try {	//connect to server and get streams
			//Make a connection to server
			connection = new Socket(InetAddress.getByName(LudoClienthost),12347);
			displayArea.append("Online");
			//get streams for input and output
			input = new Scanner(connection.getInputStream());
			output = new Formatter(connection.getOutputStream());	
		}
		catch (IOException ioE) {
			System.out.println("Cant connect to server");
			displayArea.append("Offline");
		}
		//create and start worker thread for this client
		ExecutorService worker = Executors.newFixedThreadPool(1);
		worker.execute(this); //execute client
	}
	
	//control thread that allows continuous update of displayArea
	public void run() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Method that contains most of the functionality of the ActionListener of LoginButton.
	 * Sends the text that is in the JTextField of the GUI to the server, and tries to
	 * log in. If accepted, the user will be brought to a new client
	 */
	public void doLoginButtonListener() {
		String textUsername = userType.getText();
		String textPassword = passType.getText();
		try {
			/*Sender beskjed til serveren at det er en login, deretter 
			 * sendes brukernavn og passord. 
			 */
			sendUserPassLogin(textUsername, textPassword);
			//F�r svar fra server
			if(input.hasNextLine()){
				int n = input.nextInt();
				if (n > 0) {	//Logger inn, dvs lager spill klient
					setVisible(false);
					LudoClient client;
					client = new LudoClient(LudoClienthost, connection, n);
					client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} 
				else JOptionPane.showMessageDialog(null, "Wrong username or password. Please try again");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Check connection to server.");
			System.out.println( "Could not get acces to server" );
		}
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
			try {
				sendUserPassRegister(textUsername, textPassword);
				if(processLogin(input.nextLine()))
					JOptionPane.showMessageDialog(null, "Account created. Login to play Ludo");
				else System.out.println("Error");		
			} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Something went wrong. Check connection to server and try again");
			}
		}
	}
	
	/**
	 * Sends a message to let the server know that it is a login.
	 * Also sends the username and password that the user typed in.
	 * @param username the users login name used for login
	 * @param password the users password used for login
	 */
	public void sendUserPassLogin (String username, String password) {	
		output.format("%s\n", "LOGIN");
		output.format("%s\n%s\n", username, password);
		output.flush();
	}
	/**
	 * Sends a message to let the server know that it is a registration,
	 * also sends the username and password for the new user.
	 * @param username the users login name for registration
	 * @param password the users password for registration
	 */
	public void sendUserPassRegister (String username, String password) {	
		output.format("%s\n", "REGISTER");
		output.format("%s\n%s\n", username, password);
		output.flush();
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
}
