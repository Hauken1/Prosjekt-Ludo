package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GameClient extends JFrame {
	private JTextArea displayArea;
	private Socket connection;
	private String gameClientHost;
	private int spiller;
	private BufferedWriter output;
    private BufferedReader input;
    private ExecutorService executorService;
    private int lastDiceValue;
    
    private final String throwDiceText;
    private final String receiveDiceText;
    private final String turnOwnerText;
    private final String makeMoveText;
    
	private JButton throwDiceButton;
	private int turnOwner;
	
	public GameClient(String host, Socket socket, int spillerID, BufferedWriter writer, BufferedReader reader) {

		connection = socket;
		gameClientHost = host;
		spiller = spillerID;
		output = writer;
		input = reader;
		
		//The commands that will be sent to the server
		throwDiceText = "THROWDICE:";
		receiveDiceText = "RECEIVEDICE:";
		turnOwnerText = "TURNOWNER:";
		makeMoveText = "MOVE:";
		
		throwDiceButton = new JButton("Throw dice");
		throwDiceButton.setBounds(10, 80, 50, 25);
		add(throwDiceButton, BorderLayout.NORTH);
		
		try {	//Pr�ver � lage spill bordet
			LudoBoard board = new LudoBoard();
			add(board, BorderLayout.CENTER);
			
		} catch (Exception e) {
			System.out.println("Noe feil med brettet");
		}
		
		throwDiceButton.addActionListener(e -> {
			if (turnOwner == spiller) {
				sendText(throwDiceText + spiller);
			} else {
				displayMessage("It's not your turn!\n");
			}
		});
		
		displayArea = new JTextArea(4, 30);
		displayArea.setEditable(true);
		displayArea.setFont(new Font("Arial", Font.PLAIN, 30));
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);
		
		JTextArea dialog = new JTextArea(4, 30);	      
		dialog.setMaximumSize(new Dimension(100,100));
		dialog.setEditable(true);
		dialog.setFont(new Font("Arial", Font.PLAIN, 30));
		add(new JScrollPane(dialog), BorderLayout.EAST);
		
		
		executorService = Executors.newCachedThreadPool();
		processConnection();
		executorService.shutdown();

		setSize(getPreferredSize());
		setVisible(true);
	}
	
	private void processConnection() {
		executorService.execute(() -> {
			while (true) {
				try {
	                String tmp = input.readLine();
	                if (tmp.startsWith(receiveDiceText)) { // Receives the dice numbers
	                    receiveDice(tmp.substring(receiveDiceText.length()));
	                    
	                } else if (tmp.startsWith(turnOwnerText)) {
	                	turnOwner(tmp.substring(turnOwnerText.length()));
	                	
	                } else if (tmp.startsWith(makeMoveText)) {
	                	makeMove(tmp.substring(makeMoveText.length()));
	                	
	                } 
	                
	            } catch (IOException ioe) {
	                JOptionPane.showMessageDialog(this, "Error receiving data: " + ioe);
	            }
			}
		});
	}
	
	private void receiveDice(String value) {
		lastDiceValue = new Integer(value);
		
		//Shows the message to the player.
		if (turnOwner == spiller) {
			displayMessage("You got " + lastDiceValue + ".\n");
		} else {
			displayMessage("Player " + turnOwner + " got " + lastDiceValue + ".\n");
		}
		
		//TODO:Choose the piece you want to move.
		//TODO:If valid tell the server so the other players will know.
		//sendText(makeMoveText + theMovement);
	}
	
	private void makeMove(String piece) {
		//TODO:Move the piece that was broadcasted from the server
	}
	
	/**
	 * Controls who is the owner of this turn.
	 * @param owner The ID of the new owner.
	 */
	private void turnOwner(String owner) {
		Integer turnOwner = new Integer(owner);
		
		this.turnOwner = turnOwner;
		
		if (this.turnOwner == spiller) {
			displayMessage("It's your turn.\n");
		} else {
			displayMessage("The turn belongs to player " + this.turnOwner + ".\n");
		}
		
	}

    /**
     * Used to add messages to the message area in a thread safe manner
     * 
     * @param text
     *            the text to be added
     */
    private void displayMessage(String text) {
        SwingUtilities.invokeLater(() -> displayArea.append(text));
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
