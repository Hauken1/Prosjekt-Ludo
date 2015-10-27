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

public class LudoClient extends JFrame implements Runnable {
	private String ludoClientHost; //host name for server
	private JTextArea displayArea; //Displays chat/messages from the server
	private JPanel kommunikasjon;
	private JPanel spillBord;
	
	public LudoClient(String host, Socket socket) {
		super("Ludo Klient");
		ludoClientHost = host;
		
		//spillBord = new JPanel();
		//kommunikasjon = new JPanel();
		
		displayArea = new JTextArea(4, 30);
		displayArea.setEditable(true);
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);
		
		//boardPanel = new JPanel(); //Kan brukes for å vise spillet
		//boardPanel.setLayout(new GridLayout(3,3,0,0));	//Setter hvordan panelet skal se ut
			
		//idField = new JTextField(); //Set ut textfield
		//idField.setEditable(false);
		//add(idField, BorderLayout.NORTH);		
		//panel.add(boardPanel, BorderLayout.CENTER); 
		//add(kommunikasjon);
		
		setSize(500, 300);
		setVisible(true);		
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
