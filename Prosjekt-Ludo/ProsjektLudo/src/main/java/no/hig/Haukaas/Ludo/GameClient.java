package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GameClient extends JFrame {
	private JTextArea displayArea;
	
	public GameClient(String host, Socket socket, int spillerID){
		
		try {	//Prøver å lage spill bordet
			LudoBoard board = new LudoBoard();
			add(board, BorderLayout.CENTER);
		} catch (Exception e) {
			System.out.println("Noe feil med brettet");
		}
		
		displayArea = new JTextArea(4, 30);
		displayArea.setEditable(true);
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);
		
		setSize(1000,1000);
		setVisible(true);
	}
}
