package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
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

public class LoginClient extends JFrame implements Runnable {
	private String LudoClienthost; //host name for server
	private JTextArea displayArea; 
	private JTextField idField;
	private JPanel boardPanel;
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
	private boolean test;
	
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
		
		displayArea = new JTextArea();	//Hvis om clienten er online
		displayArea.setBounds(10, 120, 100, 15);
		displayArea.setEditable(false);
		panel.add(displayArea);
		
		
		
		//TODO funksjon for å sjekk om brukeren er registert allerede
		
		ActionListener loginButtonListener = new ActionListener() {
		
			public void actionPerformed(ActionEvent event) {
				String textUsername = userType.getText();
				String textPassword = passType.getText();
		
				//TODO Funksjon som sender med brukernavn og passord og comparer med det som ligger i databasen. 
				//IF true så logger man inn. 
				
				try {
					sendUserPass(textUsername, textPassword);	//Når en connection er established ser server etter to strenger. Passord og brukernavn
					if(input.hasNextLine()){
						boolean test1;
						test1 = processLogin(input.nextLine());	//Sjekker om bruker er autentisert
						if (test1 == true ) {	//Logger inn, dvs lager spill klient
					
							setVisible(false);
							LudoClient client;
							client = new LudoClient(LudoClienthost, connection);
							client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						}
					}
				} catch (Exception e) {
					 System.out.println( "Could not get acces to server" );
				}
			}
		}; 
		loginButton.addActionListener(loginButtonListener);
		
		ActionListener registerButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
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
  							//TODO Funksjon for å sende strengene til server og deretter databasen. 
  							//Trenger server og database
  							
  							//System.out.println( "Hello World!" );
  							sendUserPass(textUsername, textPassword);
  							if(input.nextLine() == "Accepted")
  								JOptionPane.showMessageDialog(null, "Account created. Login to play Ludo");
  							
  						} catch (Exception e1) {
  							JOptionPane.showMessageDialog(null, "Something went wrong. Please try again");
  						}
					}
			}
		};
		registerButton.addActionListener(registerButtonListener);
	
		add(panel, BorderLayout.CENTER);
		
		setSize(300, 200);
		setVisible(true);
		startClient();
	}
	
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
	public void sendUserPass (String username, String password) {	//Sender dette til server. server sjekker med database og hvis det ikke fines lages en ny bruker
		output.format("%s\n%s\n", username, password);
		output.flush();
	}
	
	//Process login messages received by client
	private boolean processLogin(String login) {
		if (login.equals("Connected")) return true;
		else {
			JOptionPane.showMessageDialog(null, "Wrong Password or Username. Please try again");
			return false;
		}
		
	}
}
