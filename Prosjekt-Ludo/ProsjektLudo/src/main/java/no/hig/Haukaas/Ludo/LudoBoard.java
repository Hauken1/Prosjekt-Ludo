package no.hig.Haukaas.Ludo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * Class that makes the game board
 */
public class LudoBoard extends JPanel {
	private Color color[] = {Color.BLACK, Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE, Color.WHITE, Color.ORANGE, Color.GRAY};
	private final static int ROWS = 15;
	private final static int COLUMNS = 15;
	private JButton bonde;
	private Vector<Point> coordinatesGreen= new Vector<>();
	private Vector<Point> coordinatesRed = new Vector<>();
	private Vector<Point> coordinatesYellow= new Vector<>();
	private Vector<Point> coordinatesBlue = new Vector<>();
	private Image testImage;
	private JLayeredPane boardPane;
	private JLabel board;
	private JLabel die;
	private BufferedImage testDraw;
	Dimension boardSize;
	private int currentPlayer = 1;
	private int diceValue;
	private final static int PICTUREWIDTH = 41;	// 620/15 pixels per grid
	private final static int PICTUREHEIGHT = 41;	// 625/15 pixels per grid
	private final static int OFFSET = 20;
	private final static Point[][] GRID = new Point[ROWS][COLUMNS];
	
	final ArrayList<Pawned> greenPawns = new ArrayList<Pawned>();
	final ArrayList<Pawned> yellowPawns = new ArrayList<Pawned>();
	final ArrayList<Pawned> redPawns = new ArrayList<Pawned>();
	final ArrayList<Pawned> bluePawns = new ArrayList<Pawned>();
	
	
	
	/**
	 * Constructor for the ludo board
	 * Makes all the columns and rows and paints them.
	 */
	public LudoBoard() {
		
		setLayout(new GridBagLayout());
		setLayout(new GridLayout(ROWS, COLUMNS, 0, 0));
		/*
		RedLegitPotitions = new int[4][56];
		GreenLegitPotitions = new int[4][56];
		BlueLegitPotitions = new int[4][56];
		YellowLegitPotitions = new int[4][56];
		*/
			
		try {
			
			testImage = new ImageIcon(getClass().getResource("greenPawn1.png")).getImage();
			//testDraw = ImageIO.read(new File("bluePawn1.png"));
			//setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			ImageIcon boardBackground = new ImageIcon(getClass().getResource("ludo_Board.png"));
			ImageIcon gameBoard = resizeImageIcon(boardBackground, 1000, 900);
			//ImageIcon boardBackground = createImageIcon("ludoBoard.png");
			ImageIcon greenPawnIcon = new ImageIcon(getClass().getResource("greenPawn1.png"));
			ImageIcon yellowPawnIcon = new ImageIcon(getClass().getResource("yellowPawn1.png"));
			ImageIcon redPawnIcon = new ImageIcon(getClass().getResource("redPawn1.png"));
			ImageIcon bluePawnIcon = new ImageIcon(getClass().getResource("bluePawn1.png"));
			ImageIcon die = new ImageIcon(getClass().getResource("dice6.png"));
			
			boardPane = new JLayeredPane();
			boardPane.setPreferredSize(new Dimension(1100, 1150));
			boardPane.setOpaque(true);
			boardPane.setBackground(color[7]);
			board = new JLabel();	//Board Label
			board.setIcon(gameBoard);	//Sets the boardBackground
			boardPane.add(board, new Integer(0));
			boardSize = board.getPreferredSize();	
			board.setBounds(0, 0, boardSize.width, boardSize.height); //Gives the image some extra space
					
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
			
			JLabel redPlayer = new JLabel("Red player");
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
			diePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(color[5]), "Die"));
			
			JLabel dieLabel1 = new JLabel();
			
			dieLabel1.setIcon(die);
	
			diePanelConstraints.anchor = GridBagConstraints.CENTER;
			diePanelConstraints.gridy = 0;
			diePanel.add(dieLabel1, diePanelConstraints);
			
			JButton dieRoller = new JButton(); 
			dieRoller.setPreferredSize(new Dimension(200,200));
			dieRoller.setText("Red Players turn");
			dieRoller.setFont(new Font("Serif", Font.BOLD, 20));
			
			ActionListener rollDice = new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					//TODO MOUSELISTENER -- KLIKKE PÅ BONDE -> BEVEGE DEN
					//Dette er kun for testing purposes
					Random rng = new Random();
					diceValue = rng.nextInt(6) + 1;
					diceValue = 6;	//6'er for testing
					int player = rng.nextInt(4) +1 ;
					int bonde = rng.nextInt(4);
					if ( player == 1) {		
						greenPawns.get(bonde).changeLocation(diceValue);
						repaint();
						}
					else if(player == 2) {
						redPawns.get(bonde).changeLocation(diceValue);
						repaint();
					}
					else if (player == 3) {
						yellowPawns.get(bonde).changeLocation(diceValue);
						repaint();
					}
					else if (player == 4) {
						bluePawns.get(bonde).changeLocation(diceValue);
						repaint();
					
					}
				}
			};
			dieRoller.addActionListener(rollDice);
			
			gameGUIComponents.setLayout(new BorderLayout());
			gameGUIComponents.add(players, BorderLayout.NORTH);
			gameGUIComponents.add(diePanel, BorderLayout.CENTER);
			gameGUIComponents.add(dieRoller, BorderLayout.SOUTH);
			 
			makeGreenCoordinates();
			makeRedCoordinates();
			makeYellowCoordinates();
			makeBlueCoordinates();
			
			try {
				addPawns(greenPawns, 1);
				addPawns(yellowPawns, 2);
				addPawns(redPawns, 3);
				addPawns(bluePawns, 4);
			} catch (Exception e) {
				System.out.println("Something went wrong, when making the pawns");
			}

			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(boardPane);
			add(gameGUIComponents);					
			} catch (Exception e) {
				System.out.println("Error");
			}
		
	}//end LudoBoard constructor
	
	//Coordinates for all the pawns
	
	private void makeGreenCoordinates() {
		
		//Green home:
		coordinatesGreen.add(new Point(197,117));	//location 0
		coordinatesGreen.add(new Point(135, 175));
		coordinatesGreen.add(new Point(260, 175));
		coordinatesGreen.add(new Point(197, 230));
		
		//Green Track
		coordinatesGreen.add(new Point(102, 372));
		coordinatesGreen.add(new Point(167, 372));
		coordinatesGreen.add(new Point(227, 372));
		coordinatesGreen.add(new Point(292, 372));
		coordinatesGreen.add(new Point(355, 372));
		
		coordinatesGreen.add(new Point(415, 317));
		coordinatesGreen.add(new Point(415, 257));
		coordinatesGreen.add(new Point(415, 205));
		coordinatesGreen.add(new Point(415, 147));
		coordinatesGreen.add(new Point(415, 92));
		
		coordinatesGreen.add(new Point(415, 37));	
		coordinatesGreen.add(new Point(482, 37));
		coordinatesGreen.add(new Point(545, 37));
		
		coordinatesGreen.add(new Point(545, 92));
		coordinatesGreen.add(new Point(545, 147));
		coordinatesGreen.add(new Point(545, 205));
		coordinatesGreen.add(new Point(545, 257));
		coordinatesGreen.add(new Point(545, 317));
		
		coordinatesGreen.add(new Point(605, 372));
		coordinatesGreen.add(new Point(665, 372));
		coordinatesGreen.add(new Point(725, 372));
		coordinatesGreen.add(new Point(795, 372));
		coordinatesGreen.add(new Point(855, 372));
		
		coordinatesGreen.add(new Point(915, 372));
		coordinatesGreen.add(new Point(915, 432));
		coordinatesGreen.add(new Point(915, 487));
		
		coordinatesGreen.add(new Point(855, 487));
		coordinatesGreen.add(new Point(795, 487));
		coordinatesGreen.add(new Point(725, 487));
		coordinatesGreen.add(new Point(665, 487));
		coordinatesGreen.add(new Point(605, 487));
		
		coordinatesGreen.add(new Point(545, 542));
		coordinatesGreen.add(new Point(545, 602));
		coordinatesGreen.add(new Point(545, 657));
		coordinatesGreen.add(new Point(545, 712));
		coordinatesGreen.add(new Point(545, 767));
		
		coordinatesGreen.add(new Point(545, 825));
		coordinatesGreen.add(new Point(482, 825));
		coordinatesGreen.add(new Point(415, 825));
		
		coordinatesGreen.add(new Point(415, 767));
		coordinatesGreen.add(new Point(415, 712));
		coordinatesGreen.add(new Point(415, 657));
		coordinatesGreen.add(new Point(415, 602));
		coordinatesGreen.add(new Point(415, 542));
		
		coordinatesGreen.add(new Point(355, 487));
		coordinatesGreen.add(new Point(292, 487));
		coordinatesGreen.add(new Point(227, 487));
		coordinatesGreen.add(new Point(167, 487));
		coordinatesGreen.add(new Point(102, 487));
		
		coordinatesGreen.add(new Point(42, 487));
		coordinatesGreen.add(new Point(42, 432));
		coordinatesGreen.add(new Point(42, 372));
		
		//Green Goal highway
		coordinatesGreen.add(new Point(102, 372));
		coordinatesGreen.add(new Point(102, 432));
		coordinatesGreen.add(new Point(167,432));
		coordinatesGreen.add(new Point(227,432));
		coordinatesGreen.add(new Point(292,432));
		coordinatesGreen.add(new Point(355,432));
		
		//Green Goal
		coordinatesGreen.add(new Point(415,432));	//Location 62
		
	}
	
	private void makeRedCoordinates() {
		//Red home coordinates
		coordinatesRed.add(new Point(762, 117));
		coordinatesRed.add(new Point(702, 175));
		coordinatesRed.add(new Point(827, 175));
		coordinatesRed.add(new Point(762, 230));
		
		//Red Track
		coordinatesRed.add(new Point(545, 92));
		coordinatesRed.add(new Point(545, 147));
		coordinatesRed.add(new Point(545, 205));
		coordinatesRed.add(new Point(545, 257));
		coordinatesRed.add(new Point(545, 317));
		
		coordinatesRed.add(new Point(605, 372));
		coordinatesRed.add(new Point(665, 372));
		coordinatesRed.add(new Point(725, 372));
		coordinatesRed.add(new Point(795, 372));
		coordinatesRed.add(new Point(855, 372));
		
		coordinatesRed.add(new Point(915, 372));
		coordinatesRed.add(new Point(915, 432));
		coordinatesRed.add(new Point(915, 487));
		
		coordinatesRed.add(new Point(855, 487));
		coordinatesRed.add(new Point(795, 487));
		coordinatesRed.add(new Point(725, 487));
		coordinatesRed.add(new Point(665, 487));
		coordinatesRed.add(new Point(605, 487));
		
		coordinatesRed.add(new Point(545, 542));
		coordinatesRed.add(new Point(545, 602));
		coordinatesRed.add(new Point(545, 657));
		coordinatesRed.add(new Point(545, 712));
		coordinatesRed.add(new Point(545, 767));
		
		coordinatesRed.add(new Point(545, 825));
		coordinatesRed.add(new Point(482, 825));
		coordinatesRed.add(new Point(415, 825));
		
		coordinatesRed.add(new Point(415, 767));
		coordinatesRed.add(new Point(415, 712));
		coordinatesRed.add(new Point(415, 657));
		coordinatesRed.add(new Point(415, 602));
		coordinatesRed.add(new Point(415, 542));
		
		coordinatesRed.add(new Point(355, 487));
		coordinatesRed.add(new Point(292, 487));
		coordinatesRed.add(new Point(227, 487));
		coordinatesRed.add(new Point(167, 487));
		coordinatesRed.add(new Point(102, 487));
		
		coordinatesRed.add(new Point(42, 487));
		coordinatesRed.add(new Point(42, 432));
		coordinatesRed.add(new Point(42, 372));
		
		coordinatesRed.add(new Point(102, 372));
		coordinatesRed.add(new Point(167, 372));
		coordinatesRed.add(new Point(227, 372));
		coordinatesRed.add(new Point(292, 372));
		coordinatesRed.add(new Point(355, 372));
		
		coordinatesRed.add(new Point(415, 317));
		coordinatesRed.add(new Point(415, 257));
		coordinatesRed.add(new Point(415, 205));
		coordinatesRed.add(new Point(415, 147));
		coordinatesRed.add(new Point(415, 92));
		
		coordinatesRed.add(new Point(415, 37));	
		coordinatesRed.add(new Point(482, 37));
		coordinatesRed.add(new Point(545, 37));
		
		//Red Goalhighway coordinates
		coordinatesRed.add(new Point(545, 92));
		coordinatesRed.add(new Point(482, 92));
		coordinatesRed.add(new Point(482, 147));
		coordinatesRed.add(new Point(482, 205));
		coordinatesRed.add(new Point(482, 257));
		coordinatesRed.add(new Point(482, 317));
		
		//Red Goal coordinate
		coordinatesRed.add(new Point(482, 372));
		
	}
	private void makeYellowCoordinates() {
		//Yellow home coordinates
		coordinatesYellow.add(new Point(197, 627));
		coordinatesYellow.add(new Point(135, 685));
		coordinatesYellow.add(new Point(260, 685));
		coordinatesYellow.add(new Point(197, 740));
		
		//Yellow Track
		coordinatesYellow.add(new Point(415, 767));
		coordinatesYellow.add(new Point(415, 712));
		coordinatesYellow.add(new Point(415, 657));
		coordinatesYellow.add(new Point(415, 602));
		coordinatesYellow.add(new Point(415, 542));
		
		coordinatesYellow.add(new Point(355, 487));
		coordinatesYellow.add(new Point(292, 487));
		coordinatesYellow.add(new Point(227, 487));
		coordinatesYellow.add(new Point(167, 487));
		coordinatesYellow.add(new Point(102, 487));
		
		coordinatesYellow.add(new Point(42, 487));
		coordinatesYellow.add(new Point(42, 432));
		coordinatesYellow.add(new Point(42, 372));
		
		coordinatesYellow.add(new Point(102, 372));
		coordinatesYellow.add(new Point(167, 372));
		coordinatesYellow.add(new Point(227, 372));
		coordinatesYellow.add(new Point(292, 372));
		coordinatesYellow.add(new Point(355, 372));
		
		coordinatesYellow.add(new Point(415, 317));
		coordinatesYellow.add(new Point(415, 257));
		coordinatesYellow.add(new Point(415, 205));
		coordinatesYellow.add(new Point(415, 147));
		coordinatesYellow.add(new Point(415, 92));
		
		coordinatesYellow.add(new Point(415, 37));	
		coordinatesYellow.add(new Point(482, 37));
		coordinatesYellow.add(new Point(545, 37));
		
		coordinatesYellow.add(new Point(545, 92));
		coordinatesYellow.add(new Point(545, 147));
		coordinatesYellow.add(new Point(545, 205));
		coordinatesYellow.add(new Point(545, 257));
		coordinatesYellow.add(new Point(545, 317));
		
		coordinatesYellow.add(new Point(605, 372));
		coordinatesYellow.add(new Point(665, 372));
		coordinatesYellow.add(new Point(725, 372));
		coordinatesYellow.add(new Point(795, 372));
		coordinatesYellow.add(new Point(855, 372));
		
		coordinatesYellow.add(new Point(915, 372));
		coordinatesYellow.add(new Point(915, 432));
		coordinatesYellow.add(new Point(915, 487));
		
		coordinatesYellow.add(new Point(855, 487));
		coordinatesYellow.add(new Point(795, 487));
		coordinatesYellow.add(new Point(725, 487));
		coordinatesYellow.add(new Point(665, 487));
		coordinatesYellow.add(new Point(605, 487));
		
		coordinatesYellow.add(new Point(545, 542));
		coordinatesYellow.add(new Point(545, 602));
		coordinatesYellow.add(new Point(545, 657));
		coordinatesYellow.add(new Point(545, 712));
		coordinatesYellow.add(new Point(545, 767));
		
		coordinatesYellow.add(new Point(545, 825));
		coordinatesYellow.add(new Point(482, 825));
		coordinatesYellow.add(new Point(415, 825));
		
		//Yellow GoalHighway
		coordinatesYellow.add(new Point(415, 767));
		coordinatesYellow.add(new Point(482, 767));
		coordinatesYellow.add(new Point(482, 712));
		coordinatesYellow.add(new Point(482, 657));
		coordinatesYellow.add(new Point(482, 602));
		coordinatesYellow.add(new Point(482, 542));
		
		//Yellow Goal
		coordinatesYellow.add(new Point(482, 487));
	
	}
	private void makeBlueCoordinates() {
		//Blue Home
		coordinatesBlue.add(new Point(762, 627));
		coordinatesBlue.add(new Point(702, 685));
		coordinatesBlue.add(new Point(827, 685));
		coordinatesBlue.add(new Point(762, 740));
		
		//Blue Track
		coordinatesBlue.add(new Point(855, 487));
		coordinatesBlue.add(new Point(795, 487));
		coordinatesBlue.add(new Point(725, 487));
		coordinatesBlue.add(new Point(665, 487));
		coordinatesBlue.add(new Point(605, 487));
		
		coordinatesBlue.add(new Point(545, 542));
		coordinatesBlue.add(new Point(545, 602));
		coordinatesBlue.add(new Point(545, 657));
		coordinatesBlue.add(new Point(545, 712));
		coordinatesBlue.add(new Point(545, 767));
		
		coordinatesBlue.add(new Point(545, 825));
		coordinatesBlue.add(new Point(482, 825));
		coordinatesBlue.add(new Point(415, 825));
		
		coordinatesBlue.add(new Point(415, 767));
		coordinatesBlue.add(new Point(415, 712));
		coordinatesBlue.add(new Point(415, 657));
		coordinatesBlue.add(new Point(415, 602));
		coordinatesBlue.add(new Point(415, 542));
		
		coordinatesBlue.add(new Point(355, 487));
		coordinatesBlue.add(new Point(292, 487));
		coordinatesBlue.add(new Point(227, 487));
		coordinatesBlue.add(new Point(167, 487));
		coordinatesBlue.add(new Point(102, 487));
		
		coordinatesBlue.add(new Point(42, 487));
		coordinatesBlue.add(new Point(42, 432));
		coordinatesBlue.add(new Point(42, 372));
		
		coordinatesBlue.add(new Point(102, 372));
		coordinatesBlue.add(new Point(167, 372));
		coordinatesBlue.add(new Point(227, 372));
		coordinatesBlue.add(new Point(292, 372));
		coordinatesBlue.add(new Point(355, 372));
		
		coordinatesBlue.add(new Point(415, 317));
		coordinatesBlue.add(new Point(415, 257));
		coordinatesBlue.add(new Point(415, 205));
		coordinatesBlue.add(new Point(415, 147));
		coordinatesBlue.add(new Point(415, 92));
		
		coordinatesBlue.add(new Point(415, 37));	
		coordinatesBlue.add(new Point(482, 37));
		coordinatesBlue.add(new Point(545, 37));
		
		coordinatesBlue.add(new Point(545, 92));
		coordinatesBlue.add(new Point(545, 147));
		coordinatesBlue.add(new Point(545, 205));
		coordinatesBlue.add(new Point(545, 257));
		coordinatesBlue.add(new Point(545, 317));
		
		coordinatesBlue.add(new Point(605, 372));
		coordinatesBlue.add(new Point(665, 372));
		coordinatesBlue.add(new Point(725, 372));
		coordinatesBlue.add(new Point(795, 372));
		coordinatesBlue.add(new Point(855, 372));
		
		coordinatesBlue.add(new Point(915, 372));
		coordinatesBlue.add(new Point(915, 432));
		coordinatesBlue.add(new Point(915, 487));
		
		//Blue GoalHighway
		coordinatesBlue.add(new Point(855, 487));
		coordinatesBlue.add(new Point(855, 432));
		coordinatesBlue.add(new Point(795, 432));
		coordinatesBlue.add(new Point(725, 432));
		coordinatesBlue.add(new Point(665, 432));
		coordinatesBlue.add(new Point(605, 432));
		
		//Blue Goal
		coordinatesBlue.add(new Point(545, 432));
		
	}
	
	public void addPawns(final ArrayList<Pawned> pawns, int color) {

		for(int i = 0; i < 4; i++) {
				Pawned newPawn = new Pawned(i, color);
				pawns.add(newPawn);	
		}
	}
	//public Object getValueAt(int row, int col) {
	public Point getGreenCoordinates(int i) {
		return coordinatesGreen.elementAt(i);
	}
	
	public Point getYellowCoordinates(int i) {
		return coordinatesYellow.elementAt(i);
	}
	
	public Point getRedCoordinates(int i) {
		return coordinatesRed.elementAt(i);
	}
	
	public Point getBlueCoordinates(int i) {
		return coordinatesBlue.elementAt(i);
	}
	
	public void setUpGUI() {
		
	}
	
	public static ImageIcon resizeImageIcon(ImageIcon icon, Integer width, Integer height) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
		Graphics g2d = bufferedImage.createGraphics();
		g2d.drawImage(icon.getImage(), 0, 0, width, height, null);
		g2d.dispose();
		
		return new ImageIcon(bufferedImage, icon.getDescription());
	}
	
	public void paint(Graphics g){
		
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		paintPawns(g2d);
		
		/*
		 * TEST
		 * DRAWS A PAWN ON EVERY POSSIBLE LOCATION
		 * TODO REMOVE THIS
		Graphics2D g2 = (Graphics2D) g;
		
		for ( int i = 0; i < coordinatesBlue.size(); i++) {
		
		float thickness = 4;
		g2.setStroke(new BasicStroke(thickness));
		
		g2.setColor(color[5]);
		
		g2.drawOval(coordinatesGreen.elementAt(i).x, coordinatesGreen.elementAt(i).y, 40, 40);
		g2.setColor(color[2]);
		g2.fillOval(coordinatesGreen.elementAt(i).x, coordinatesGreen.elementAt(i).y, 40, 40);
		
		
		g2.setColor(color[5]);
		g2.drawOval(coordinatesRed.elementAt(i).x, coordinatesRed.elementAt(i).y, 40, 40);
		g2.setColor(color[1]);
		g2.fillOval(coordinatesRed.elementAt(i).x, coordinatesRed.elementAt(i).y, 40, 40);
		
		
		g2.setColor(color[5]);
		g2.drawOval(coordinatesYellow.elementAt(i).x, coordinatesYellow.elementAt(i).y, 40, 40);
		g2.setColor(color[4]);
		g2.fillOval(coordinatesYellow.elementAt(i).x, coordinatesYellow.elementAt(i).y, 40, 40);
		
		
		g2.setColor(color[5]);
		g2.drawOval(coordinatesBlue.elementAt(i).x, coordinatesBlue.elementAt(i).y, 40, 40);
		g2.setColor(color[3]);
		g2.fillOval(coordinatesBlue.elementAt(i).x, coordinatesBlue.elementAt(i).y, 40, 40);
		
		}
		*/
	}
	
	void paintPawns(Graphics2D g2d) {
		for (int i = 0; i< 4; i++) {
				int loc = greenPawns.get(i).returnLocation();
				float thickness = 4;
				g2d.setStroke(new BasicStroke(thickness));
				
				g2d.setColor(color[0]);
				g2d.drawOval(coordinatesGreen.elementAt(loc).x, coordinatesGreen.elementAt(loc).y, 40, 40);
				g2d.setColor(color[1]);
				g2d.fillOval(coordinatesGreen.elementAt(loc).x, coordinatesGreen.elementAt(loc).y, 40, 40);
				
				loc = yellowPawns.get(i).returnLocation();
				g2d.setColor(color[0]);
				g2d.drawOval(coordinatesYellow.elementAt(loc).x, coordinatesYellow.elementAt(loc).y, 40, 40);
				g2d.setColor(color[2]);
				g2d.fillOval(coordinatesYellow.elementAt(loc).x, coordinatesYellow.elementAt(loc).y, 40, 40);
				
				loc = redPawns.get(i).returnLocation();
				g2d.setColor(color[0]);
				g2d.drawOval(coordinatesRed.elementAt(loc).x, coordinatesRed.elementAt(loc).y, 40, 40);
				g2d.setColor(color[3]);
				g2d.fillOval(coordinatesRed.elementAt(loc).x, coordinatesRed.elementAt(loc).y, 40, 40);
				
				loc = bluePawns.get(i).returnLocation();
				g2d.setColor(color[0]);
				g2d.drawOval(coordinatesBlue.elementAt(loc).x, coordinatesBlue.elementAt(loc).y, 40, 40);
				g2d.setColor(color[4]);
				g2d.fillOval(coordinatesBlue.elementAt(loc).x, coordinatesBlue.elementAt(loc).y, 40, 40);
		}
		
	}
	/**
	 * 
	 * @author Hauken
	 *Class that holds the information about every pawn
	 *
	 */
	public class Pawned {
		private int pawnnr;
		private int location; //location 0-3 er hjemmeplassering.
		private int homelocation;
		private int color;	
		private final static int POTITIONS = 62;
		private Vector<Point> coordinates= new Vector<>();
		private Color paintColor[] = {Color.BLACK, Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE};
		
		/**
		 * Class constructor that makes every pawn and set their starting location
		 * @param loc holds the start location of the current pawn
		 * @param col holds the same nuber as location, but this is to verify the color
		 */
		public Pawned(int loc, int col){
			homelocation = loc;
			location = loc; 
			color = col;
						
			if (color == 1) {	
				Point p = getGreenCoordinates(loc);
				coordinates.add(new Point(p.x, p.y));	
			}
			 if (color == 2) {
					
				Point p = getYellowCoordinates(loc);
				coordinates.add(new Point(p.x, p.y));
			
			 }	
			if (color == 3) {
				Point p = getRedCoordinates(loc);
				coordinates.add(new Point(p.x, p.y));
			}	
			if (color == 4) {
				Point p = getBlueCoordinates(loc);
				coordinates.add(new Point(p.x, p.y));
			}
		}
		/**
		 * Method that returns a pawns location
		 * @return returns a pawns current location
		 */
		public int returnLocation() {
			return location;
		}
		/**
		 * Method that returns a pawns homelocation.
		 *  This is used when the pawn is knocked back to the starting location
		 * @return returns a pawns homelocation
		 */
		public int homeLocation() {
			return homelocation;
		}
		/**
		 * Method that changes a pawns location
		 * @param n holds the location that the pawns should change to
		 */
		public void changeLocation(int n) {
			location += n; 
		}
		
	} //END OF PAWN CLASS
}//END OF LUDOBOARD CLASS
	