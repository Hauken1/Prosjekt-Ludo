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
import java.util.concurrent.Semaphore;

import javax.security.auth.x500.X500Principal;
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
    private JButton dieRoller; 
    private ImageIcon die1;
    private ImageIcon die2;
    private ImageIcon die3;
    private ImageIcon die4;
    private ImageIcon die5;
    private ImageIcon die6;
    
    private JLabel greenPlayer;
    private JLabel yellowPlayer;
    private JLabel redPlayer;
    private JLabel bluePlayer;
    
    private JButton pawn1; 
    private JButton pawn2; 
    private JButton pawn3; 
    private JButton pawn4; 
    
    private final String throwDiceText;
    private final String receiveDiceText;
    private final String turnOwnerText;
    private final String makeMoveText;
    
	private int turnOwner = 2;
	private int spiller = 1;
	private int pawnToMove = 0;
	private boolean gameOver = false;
	private boolean pawnChoice = false;
	private Semaphore sema = new Semaphore(1);
	
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
		
		setUpGUI();	
		
		executorService = Executors.newCachedThreadPool();
		processConnection();
		executorService.shutdown();

		setSize(getPreferredSize());
		setVisible(true);
	}
	
	private void setUpGUI() {
		
		try {	//Prøver å lage spill brettet
			board = new LudoBoard();
			add(board, BorderLayout.CENTER);
			
		} catch (Exception e) {
			System.out.println("Noe feil med brettet");
		}
		
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
		players.setPreferredSize(new Dimension(300,300));
		players.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Players"));
		players.setBackground(color[5]);
		
		greenPlayer = new JLabel("Green player");
		greenPlayer.setBackground(Color.RED);
		greenPlayer.setFont(new Font("Serif", Font.BOLD, 20));
		greenPlayer.setForeground(Color.BLACK);
		greenPlayer.setBorder(BorderFactory.createLineBorder(color[0], 2, true));
		greenPlayer.setOpaque(true);
		greenPlayer.setBackground(color[1]);
		
		yellowPlayer = new JLabel("Yellow player");
		yellowPlayer.setFont(new Font("Serif", Font.BOLD, 20));
		yellowPlayer.setForeground(Color.BLACK);
		yellowPlayer.setBorder(BorderFactory.createLineBorder(color[0], 2, true));
		yellowPlayer.setOpaque(true);
		yellowPlayer.setBackground(color[2]);
		
		redPlayer = new JLabel("Red player: Your turn");
		redPlayer.setFont(new Font("Serif", Font.BOLD, 20));
		redPlayer.setForeground(Color.WHITE);
		redPlayer.setBorder(BorderFactory.createLineBorder(color[0], 2, true));
		redPlayer.setOpaque(true);
		redPlayer.setBackground(color[3]);
		
		bluePlayer = new JLabel("Blue player");
		bluePlayer.setFont(new Font("Serif", Font.BOLD, 20));
		bluePlayer.setForeground(Color.WHITE);
		bluePlayer.setBorder(BorderFactory.createLineBorder(color[0], 2, true));
		bluePlayer.setOpaque(true);
		bluePlayer.setBackground(color[4]);
		
		players.setLayout(new GridBagLayout());
		GridBagConstraints playerLayout = new GridBagConstraints();
		playerLayout.fill = GridBagConstraints.HORIZONTAL;
		playerLayout.weightx = 1;
		playerLayout.weighty = 1;
		playerLayout.gridy = 0;
		players.add(redPlayer, playerLayout);
		playerLayout.gridy = 1;
		players.add(yellowPlayer, playerLayout);
		playerLayout.gridy = 2;
		players.add(bluePlayer, playerLayout);
		playerLayout.gridy = 3;
		players.add(greenPlayer, playerLayout);
		
		JPanel diePanel = new JPanel();
		diePanel.setLayout(new GridBagLayout());
		GridBagConstraints diePanelConstraints = new GridBagConstraints();
		diePanel.setPreferredSize(new Dimension(200, 200));
		diePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(color[5]), "Dice value"));
		
		dieLabel = new JLabel();
		dieTextLabel = new JLabel();
		
		diePanelConstraints.anchor = GridBagConstraints.CENTER;
		diePanelConstraints.gridy = 0;
		diePanelConstraints.ipady = 20;
		diePanel.add(dieTextLabel, diePanelConstraints);
		diePanelConstraints.anchor = GridBagConstraints.CENTER;
		diePanelConstraints.gridy = 1;
		diePanel.add(dieLabel, diePanelConstraints);
		
		dieRoller = new JButton(); 
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
		
		JPanel pawnPanel = new JPanel();
		pawnPanel.setLayout(new GridBagLayout());
		GridBagConstraints pawnPanelGridBagConstraint = new GridBagConstraints();
		
		pawn1 = new JButton();
		pawn1.setText("Pawn 1");
		pawn1.setPreferredSize(new Dimension(100,100));
		ActionListener pawn1ActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pawnToMove = 0;
				processRoll(diceValue);
				pawnChoice = true;
				
			}
		};
		pawn1.addActionListener(pawn1ActionListener);
		
		pawn2 = new JButton();
		pawn2.setText("Pawn 2");
		pawn2.setPreferredSize(new Dimension(100,100));
		ActionListener pawn2ActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pawnToMove = 1;
				processRoll(diceValue);
				pawnChoice = true;
			}
		};
		pawn2.addActionListener(pawn2ActionListener);
		
		pawn3 = new JButton();
		pawn3.setText("Pawn 3");
		pawn3.setPreferredSize(new Dimension(100,100));
		ActionListener pawn3ActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pawnToMove = 2;
				processRoll(diceValue);
				pawnChoice = true;
				
			}
		};
		pawn3.addActionListener(pawn3ActionListener);
		
		pawn4 = new JButton();
		pawn4.setText("Pawn 4");
		pawn4.setPreferredSize(new Dimension(100,100));
		ActionListener pawn4ActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pawnToMove = 3;
				processRoll(diceValue);
				pawnChoice = true;
				
			}
		};
		pawn4.addActionListener(pawn4ActionListener);
		
		pawnPanelGridBagConstraint.gridy = 0;
		pawnPanelGridBagConstraint.gridx = 0;
		pawnPanel.add(pawn1,pawnPanelGridBagConstraint);
		pawnPanelGridBagConstraint.gridy = 0;
		pawnPanelGridBagConstraint.gridx = 1;
		pawnPanel.add(pawn2, pawnPanelGridBagConstraint);
		pawnPanelGridBagConstraint.gridy = 1;
		pawnPanelGridBagConstraint.gridx = 0;
		pawnPanel.add(pawn3, pawnPanelGridBagConstraint);
		pawnPanelGridBagConstraint.gridy = 1;
		pawnPanelGridBagConstraint.gridx = 1;
		pawnPanel.add(pawn4, pawnPanelGridBagConstraint);
		
		gameGUIComponents.setLayout(new BorderLayout());
		gameGUIComponents.add(players, BorderLayout.NORTH);
		gameGUIComponents.add(diePanel, BorderLayout.CENTER);
		gameGUIComponents.add(dieRoller, BorderLayout.SOUTH);
		gameGUIComponents.add(pawnPanel, BorderLayout.EAST);
		
		add(gameGUIComponents, BorderLayout.WEST);
		
	}
	
	private void rollDiceActionListener() {
		
		Random rng = new Random();
		diceValue = rng.nextInt(6) + 1;
		
		//diceValue = 6;	//6'er for testing
			
		if (turnOwner == spiller) {
			sendText(throwDiceText + spiller);
		} else {
		//	displayMessage("It's not your turn!\n");
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
		
		if(gameOver) {
			dieRoller.setEnabled(false);
			dieRoller.setText("GG");
		}
		
	}
	
	private void processRoll(int diceVal) {
		int inGoal;
		if ( turnOwner == 1) {	//Green player
			board.greenPawns.get(pawnToMove).changeLocation(diceValue);
			inGoal = board.greenPawnsInGoal.size();
			if (inGoal == 4) {
				displayMessage("Green Player won");
				System.out.println(("You won"));
				gameOver = true;
			}
			turnOwner ++;
			greenPlayer.setText("Green player:");
			redPlayer.setText("Red player: Your turn!");
			repaint();
			}
		else if(turnOwner == 2) { //Red player
			try {
				board.redPawns.get(pawnToMove).changeLocation(diceValue);
				inGoal = board.redPawnsInGoal.size();
				if (inGoal == 4) {
						System.out.println(("You won"));
						gameOver = true;
						displayMessage("Red Player won");
				}
				pawnToMove = 0;
				pawnChoice = false;
			} catch (Exception e ) {
				System.out.println("Goalerror");
			}
			turnOwner ++;
			redPlayer.setText("Red player:");
			yellowPlayer.setText("Yellow player: Your Turn!");
			repaint();
		}
		else if (turnOwner == 3) { //Yellow player
			board.yellowPawns.get(pawnToMove).changeLocation(diceValue);
			inGoal = board.yellowPawnsInGoal.size();
			if (inGoal == 4) {
				displayMessage("Yellow Player won");
				System.out.println(("You won"));
				gameOver = true;
			}
			turnOwner ++;
			yellowPlayer.setText("Yellow player:");
			bluePlayer.setText("Blue player: Your turn!");
			repaint();
		}
		else if (turnOwner == 4) { //Blue player
			board.bluePawns.get(pawnToMove).changeLocation(diceValue);
			inGoal = board.bluePawnsInGoal.size();
			if (inGoal == 4) {
				displayMessage("Blue Player won");
				System.out.println(("You won"));
				gameOver = true;
			}
			bluePlayer.setText("Blue player:");
			greenPlayer.setText("Green player: Your Turn!");
			turnOwner = 1;
			repaint();
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
