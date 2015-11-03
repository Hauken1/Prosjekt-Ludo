package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GameClient extends JFrame {
	private JTextArea displayArea;
	
	public GameClient(String host, Socket socket, int spillerID){
		super("Ludo");
		
		try {	//Prøver å lage spill bordet
			LudoBoard board = new LudoBoard();
			add(board, BorderLayout.CENTER);
			
		} catch (Exception e) {
			System.out.println("Noe feil med brettet");
		}
		
		displayArea = new JTextArea(4, 30);
		displayArea.setEditable(true);
		displayArea.setFont(new Font("Arial", Font.PLAIN, 30));
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);
		
		JTextArea dialog = new JTextArea(4, 30);	      
		dialog.setMaximumSize(new Dimension(100,100));
		dialog.setEditable(true);
		dialog.setFont(new Font("Arial", Font.PLAIN, 30));
		add(new JScrollPane(dialog), BorderLayout.EAST);
		
		setSize(getPreferredSize());
		setVisible(true);
	}
}
