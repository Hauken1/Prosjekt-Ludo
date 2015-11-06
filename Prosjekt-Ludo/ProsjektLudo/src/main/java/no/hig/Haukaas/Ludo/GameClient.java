package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GameClient extends JFrame {
	private Color color[] = {Color.BLACK, Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE, Color.WHITE, Color.ORANGE, Color.GRAY};
	private JTextArea typeArea;
	private JTextArea dialog;
	private Socket connection;
	private String gameClientHost;

	private BufferedWriter output;
    private BufferedReader input;
    private ExecutorService executorService;
    private int lastDiceValue;
    private LudoBoard board;
    private int diceValue;
    private JLabel dieLabel;
    private JLabel dieTextLabel;
    private ImageIcon die1;
    private ImageIcon die2;
    private ImageIcon die3;
    private ImageIcon die4;
    private ImageIcon die5;
    private ImageIcon die6;
    
    private final String throwDiceText;
    private final String receiveDiceText;
    private final String turnOwnerText;
    private final String makeMoveText;
    
	private JButton throwDiceButton;
	private int turnOwner = 2;
	private int spiller = 1;
	
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
		
		try {	//Prøver å lage spill brettet
			board = new LudoBoard();
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
		
		typeArea = new JTextArea();
		typeArea.setEditable(true);
		typeArea.setFont(new Font("Arial", Font.PLAIN, 30));
		add(new JScrollPane(typeArea), BorderLayout.SOUTH);
		
		dialog = new JTextArea(1, 10);	      
		dialog.setMaximumSize(new Dimension(100,100));
		dialog.setEditable(false);
		dialog.setFont(new Font("Arial", Font.PLAIN, 30));
		add(new JScrollPane(dialog), BorderLayout.EAST);
		
		//SpillGUI
		die1 = new ImageIcon(getClass().getResource("dice1.png"));
		die2 = new ImageIcon(getClass().getResource("dice2.png"));
		die3 = new ImageIcon(getClass().getResource("dice3.png"));
		die4 = new ImageIcon(getClass().getResource("dice4.png"));
		die5 = new ImageIcon(getClass().getResource("dice5.png"));
		die6 = new ImageIcon(getClass().getResource("dice6.png"));
		
		JPanel gameGUIComponents = new JPanel();
		JPanel players = new JPanel();
		players.setPreferredSize(new Dimension(200,200));
		players.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Players"));
		players.setBackground(color[6]);
		
		JLabel greenPlayer = new JLabel("Green player");
		greenPlayer.setBackground(Color.RED);
		greenPlayer.setFont(new Font("Serif", Font.BOLD, 20));
		greenPlayer.setBorder(BorderFactory.createLineBorder(color[0], 2, true));
		greenPlayer.setOpaque(true);
		greenPlayer.setBackground(color[1]);
		
		JLabel yellowPlayer = new JLabel("Yellow player");
		yellowPlayer.setFont(new Font("Serif", Font.BOLD, 20));
		yellowPlayer.setBorder(BorderFactory.createLineBorder(color[0], 2, true));
		yellowPlayer.setOpaque(true);
		yellowPlayer.setBackground(color[2]);
		
		JLabel redPlayer = new JLabel("Red player: Your turn");
		redPlayer.setFont(new Font("Serif", Font.BOLD, 20));
		redPlayer.setBorder(BorderFactory.createLineBorder(color[0], 2, true));
		redPlayer.setOpaque(true);
		redPlayer.setBackground(color[3]);
		
		JLabel bluePlayer = new JLabel("Blue player");
		bluePlayer.setFont(new Font("Serif", Font.BOLD, 20));
		bluePlayer.setBorder(BorderFactory.createLineBorder(color[0], 2, true));
		bluePlayer.setOpaque(true);
		bluePlayer.setBackground(color[4]);
		
		players.setLayout(new GridBagLayout());
		GridBagConstraints playerLayout = new GridBagConstraints();
		playerLayout.fill = GridBagConstraints.HORIZONTAL;
		playerLayout.weightx = 1;
		playerLayout.weighty = 1;
		playerLayout.gridy = 0;
		players.add(greenPlayer, playerLayout);
		playerLayout.gridy = 1;
		players.add(yellowPlayer, playerLayout);
		playerLayout.gridy = 2;
		players.add(redPlayer, playerLayout);
		playerLayout.gridy = 3;
		players.add(bluePlayer, playerLayout);
		
		JPanel diePanel = new JPanel();
		diePanel.setLayout(new GridBagLayout());
		GridBagConstraints diePanelConstraints = new GridBagConstraints();
		diePanel.setPreferredSize(new Dimension(200, 200));
		diePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(color[5]), "Dice value"));
		
		dieLabel = new JLabel();
		dieTextLabel = new JLabel();
		
		//dieLabel1.setIcon(die);

		diePanelConstraints.anchor = GridBagConstraints.CENTER;
		diePanelConstraints.gridy = 0;
		diePanelConstraints.ipady = 20;
		diePanel.add(dieTextLabel, diePanelConstraints);
		diePanelConstraints.anchor = GridBagConstraints.CENTER;
		diePanelConstraints.gridy = 1;
		diePanel.add(dieLabel, diePanelConstraints);
		
		JButton dieRoller = new JButton(); 
		dieRoller.setPreferredSize(new Dimension(200,200));
		dieRoller.setText("Throw Dice");
		dieRoller.setFont(new Font("Serif", Font.BOLD, 20));
		
		ActionListener rollDice = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//TODO MOUSELISTENER -- KLIKKE PÅ BONDE -> BEVEGE DEN
				//Dette er kun for testing purposes
				rollDiceActionListener();
			}
		};
		dieRoller.addActionListener(rollDice);
		
		gameGUIComponents.setLayout(new BorderLayout());
		gameGUIComponents.add(players, BorderLayout.NORTH);
		gameGUIComponents.add(diePanel, BorderLayout.CENTER);
		gameGUIComponents.add(dieRoller, BorderLayout.SOUTH);
		
		add(gameGUIComponents, BorderLayout.WEST);	
		
		executorService = Executors.newCachedThreadPool();
		processConnection();
		executorService.shutdown();

		setSize(getPreferredSize());
		setVisible(true);
	}
	
	private void setUpGUI() {
		
	}
	
	private void rollDiceActionListener() {
		Random rng = new Random();
		diceValue = rng.nextInt(6) + 1;
		
		//diceValue = 6;	//6'er for testing
		
		int bonde = rng.nextInt(4);
		
		if (turnOwner == spiller) {
			sendText(throwDiceText + spiller);
		} else {
			displayMessage("It's not your turn!\n");
		}
		
		if ( turnOwner == 1) {		
			board.greenPawns.get(bonde).changeLocation(diceValue);
			
			turnOwner ++;
			repaint();
			}
		else if(turnOwner == 2) {
			board.redPawns.get(bonde).changeLocation(diceValue);
			turnOwner ++;
			repaint();
		}
		else if (turnOwner == 3) {
			board.yellowPawns.get(bonde).changeLocation(diceValue);
			turnOwner ++;
			repaint();
		}
		else if (turnOwner == 4) {
			board.bluePawns.get(bonde).changeLocation(diceValue);
			turnOwner = 1;
			repaint();
		}
		
		switch (diceValue) {
		case 1:
			dieTextLabel.setText("You got a: ");
			dieLabel.setIcon(die1);
			break;
		case 2:
			dieTextLabel.setText("You got a: ");
			dieLabel.setIcon(die2);
			break;
		case 3:
			dieTextLabel.setText("You got a: ");
			dieLabel.setIcon(die3);
			break;
		case 4:
			dieTextLabel.setText("You got a: ");
			dieLabel.setIcon(die4);
			break;
		case 5:
			dieTextLabel.setText("You got a: ");
			dieLabel.setIcon(die5);
			break;
		case 6:
			dieTextLabel.setText("You got a: ");
			dieLabel.setIcon(die6);
			break;
		}
		
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
        SwingUtilities.invokeLater(() -> dialog.append(text));
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
