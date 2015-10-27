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

/*Vet ikke helt om jeg skal gjøre det slik jeg har det nå. Dvs har en "client" for pålogging
*og en for selve spillet. Ville prøve å gjøre det på denne måten, men vet ikke helt hvordan det blir med connectionen  
*/

public class LudoClient extends JFrame implements Runnable {
	private String ludoClientHost; //host name for server
	private JTextArea displayArea; //Displays chat/messages from the server
	
	
	public LudoClient(String host, Socket socket) {
		super("Here we go!");
		ludoClientHost = host; 
		
		displayArea = new JTextArea(4, 30);
		displayArea.setEditable(false);
		add(new JScrollPane(displayArea));
		
		//boardPanel = new JPanel(); //Kan brukes for å vise spillet
		//boardPanel.setLayout(new GridLayout(3,3,0,0));	//Setter hvordan panelet skal se ut
			
		//idField = new JTextField(); //Set ut textfield
		//idField.setEditable(false);
		//add(idField, BorderLayout.NORTH);		
		//panel.add(boardPanel, BorderLayout.CENTER); 
	
		setSize(500, 300);
		setVisible(true);		
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
