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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * Class that makes the game board
 */
public class LudoBoard extends JPanel {
	private Color color[] = {Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK, Color.ORANGE, Color.GRAY};
	private final static int ROWS = 15;
	private final static int COLUMNS = 15;
	private Felter[][] felt;
	private JButton bonde;
	private Vector<Point> coordinatesGreen= new Vector<>();
	private Vector<Point> coordinatesRed = new Vector<>();
	private Vector<Point> coordinatesYellow= new Vector<>();
	private Vector<Point> coordinatesBlue = new Vector<>();
	private int RedLegitPotitions[][];
	private int GreenLegitPotitions[][];
	private int BlueLegitPotitions[][];
	private int YellowLegitPotitions[][];
	private Image testImage;
	private JLayeredPane boardPane;
	private JLabel board;
	private JLabel die;
	private BufferedImage testDraw;
	Dimension boardSize;
	private final static int PICTUREWIDTH = 41;	// 620/15 pixels per grid
	private final static int PICTUREHEIGHT = 41;	// 625/15 pixels per grid
	private final static int OFFSET = 20;
	private final static Point[][] GRID = new Point[ROWS][COLUMNS];
	
	
	
	/**
	 * Constructor for the ludo board
	 * Makes all the columns and rows and paints them.
	 */
	public LudoBoard() {
		
		setLayout(new GridBagLayout());
		setLayout(new GridLayout(ROWS, COLUMNS, 0, 0));
		//setBackground(color[0]);
		
		RedLegitPotitions = new int[4][56];
		GreenLegitPotitions = new int[4][56];
		BlueLegitPotitions = new int[4][56];
		YellowLegitPotitions = new int[4][56];
		
		final ArrayList<Pawn> greenPawns = new ArrayList<Pawn>();
		final ArrayList<Pawn> yellowPawns = new ArrayList<Pawn>();
		final ArrayList<Pawn> redPawns = new ArrayList<Pawn>();
		final ArrayList<Pawn> bluePawn = new ArrayList<Pawn>();
		
		
		felt = new Felter[15][15];
		
		
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
			greenPlayer.setBorder(BorderFactory.createLineBorder(color[5], 2, true));
			greenPlayer.setOpaque(true);
			greenPlayer.setBackground(color[2]);
			
			JLabel yellowPlayer = new JLabel("Yellow player");
			yellowPlayer.setFont(new Font("Serif", Font.BOLD, 20));
			yellowPlayer.setBorder(BorderFactory.createLineBorder(color[5], 2, true));
			yellowPlayer.setOpaque(true);
			yellowPlayer.setBackground(color[4]);
			
			JLabel redPlayer = new JLabel("Red player");
			redPlayer.setFont(new Font("Serif", Font.BOLD, 20));
			redPlayer.setBorder(BorderFactory.createLineBorder(color[5], 2, true));
			redPlayer.setOpaque(true);
			redPlayer.setBackground(color[1]);
			
			JLabel bluePlayer = new JLabel("Blue player");
			bluePlayer.setFont(new Font("Serif", Font.BOLD, 20));
			bluePlayer.setBorder(BorderFactory.createLineBorder(color[5], 2, true));
			bluePlayer.setOpaque(true);
			bluePlayer.setBackground(color[3]);
			
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
			dieRoller.setText("Not your turn");
			dieRoller.setFont(new Font("Serif", Font.BOLD, 20));
			
			gameGUIComponents.setLayout(new BorderLayout());
			gameGUIComponents.add(players, BorderLayout.NORTH);
			gameGUIComponents.add(diePanel, BorderLayout.CENTER);
			gameGUIComponents.add(dieRoller, BorderLayout.SOUTH);
			 
			addPawns(greenPawnIcon, greenPawns);
			addPawns(yellowPawnIcon, yellowPawns);
			addPawns(redPawnIcon, redPawns);
			addPawns(bluePawnIcon, bluePawn);
			
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(boardPane);
			add(gameGUIComponents);
		
			makeGreenCoordinates();
			makeRedCoordinates();
			makeYellowCoordinates();
			makeBlueCoordinates();
			
//			add(label);
			//JLabel board1 = new JLabel(boardGame);
			//boardPane.add(boardGame, new Integer(0));
			//add(board1);
			//Dimension boardSize = board1.getPreferredSize();
			//board1.setBounds(15, 10, boardSize.width, boardSize.height);
			//add(boardPane);
			/*
			for (int row = 0; row < felt.length; row++) {
				for(int col = 0; col < felt[row].length; col++) {
					felt[row][col] = new Felter(row, col);
					add(felt[row][col]);
					
				}
			}
			*/
			
			} catch (Exception e) {
				System.out.println("Error");
			}
		/*
		for(int i = 1; i<=4; i++) {
			int row = 0;
			int col = 0;
			//newRedPawn(i, i, row +1, col + i);
		}
		*/
		
	}//end LudoBoard constructor
	
	//TODO Legg til koordinater her
	
	private void makeGreenCoordinates() {
	
		coordinatesGreen.add(new Point(215,145));
		coordinatesGreen.add(new Point(145, 215));
		coordinatesGreen.add(new Point(285, 215));
		coordinatesGreen.add(new Point(215, 285));
	}
	
	private void makeRedCoordinates() {
		coordinatesRed.add(new Point(835, 145));
		coordinatesRed.add(new Point(765, 215));
		coordinatesRed.add(new Point(905, 215));
		coordinatesRed.add(new Point(835, 285));
	}
	private void makeYellowCoordinates() {
		coordinatesYellow.add(new Point(215, 765));
		coordinatesYellow.add(new Point(145, 835));
		coordinatesYellow.add(new Point(215, 905));
		coordinatesYellow.add(new Point(285, 835));
	}
	private void makeBlueCoordinates() {
		coordinatesBlue.add(new Point(835, 765));
		coordinatesBlue.add(new Point(765, 835));
		coordinatesBlue.add(new Point(835, 905));
		coordinatesBlue.add(new Point(905, 835));
	}
	
	public void addPawns(ImageIcon pawnImg, final ArrayList<Pawn> pawns) {
		for(int i = 0; i <2; i++) {
			for (int j = 0; j< 2; j++) {
				JButton pawn = new JButton(pawnImg);
				pawn.setBorderPainted(false);
				pawn.setContentAreaFilled(false);
				//boardPane.add(pawn, new Integer(1));
				Dimension size = new Dimension(pawn.getIcon().getIconWidth(), pawn.getIcon().getIconHeight());
				pawn.setBounds(0, 0, size.width, size.height);
				Pawn newPawn = new Pawn(pawn, i);
				pawns.add(newPawn);
			}
		}
	}
	
	public void setUpGUI() {
		
	}
	
	//public void addPawn(int row, int col, String color) {
	//	felt[row][col].makePawn(color);
//	}
	
	public void makePotitions() {
		
	}
	
	/*
	public ImageIcon createImageIcon(String path) {
		try {
			BufferedImage temp = new B;
			ImageIcon icon = new ImageIcon(temp);
			return icon;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	*/
	
	public static ImageIcon resizeImageIcon(ImageIcon icon, Integer width, Integer height) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
		Graphics g2d = bufferedImage.createGraphics();
		g2d.drawImage(icon.getImage(), 0, 0, width, height, null);
		g2d.dispose();
		
		return new ImageIcon(bufferedImage, icon.getDescription());
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		for ( int i = 0; i< 4; i++) {
		
		float thickness = 4;
		
		g2.setColor(color[5]);
		g2.setStroke(new BasicStroke(thickness));
		g2.drawOval(coordinatesGreen.elementAt(i).x, coordinatesGreen.elementAt(i).y, 52, 52);
		g2.setColor(color[2]);
		g2.fillOval(coordinatesGreen.elementAt(i).x, coordinatesGreen.elementAt(i).y, 52, 52);
		
		g2.setColor(color[5]);
		g2.drawOval(coordinatesRed.elementAt(i).x, coordinatesRed.elementAt(i).y, 52, 52);
		g2.setColor(color[1]);
		g2.fillOval(coordinatesRed.elementAt(i).x, coordinatesRed.elementAt(i).y, 52, 52);
		
		g2.setColor(color[5]);
		g2.drawOval(coordinatesYellow.elementAt(i).x, coordinatesYellow.elementAt(i).y, 52, 52);
		g2.setColor(color[4]);
		g2.fillOval(coordinatesYellow.elementAt(i).x, coordinatesYellow.elementAt(i).y, 52, 52);
		
		g2.setColor(color[5]);
		g2.drawOval(coordinatesBlue.elementAt(i).x, coordinatesBlue.elementAt(i).y, 52, 52);
		g2.setColor(color[3]);
		g2.fillOval(coordinatesBlue.elementAt(i).x, coordinatesBlue.elementAt(i).y, 52, 52);
		}
		/*
		g.drawImage(testImage, p.x, p.y, null);
		g.drawImage(testImage, p1.x, p1.y, null);
		g.drawImage(testImage, p2.x, p2.y, null);
		g.drawImage(testImage, p3.x, p3.y, null);
			
		/*
	
		*/
			
		//g.setColor(color[1]);
		//g.fillRect(0, 0, 145, 80);
	}

	/*
	/*
	void Draw(Graphics2D g2d) {
		for(int color = 1; color <=4; color++) {
			
		}
	}
	
	
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(boardGame, 0, 0, null);
	}
	
	
	/*
	 * 
	 * Ikke noe fornuftig lenger ned! Kun testing.
	 * 
	 */
	
	
	/**
	 * 
	 * Class for a single field on the game board
	 *
	 */
	private class Felter extends JButton {
		
		private int row;
		private int column;
		private JButton pawn;
		private boolean goal = false; 
		private boolean home = false;
		
		private Vector<Pawn> RedPawn = new Vector<Pawn>();   
		private Vector<Pawn> GreenPawn = new Vector<Pawn>();
		private Vector<Pawn> BluePawn = new Vector<Pawn>();
		private Vector<Pawn> YellowPawn = new Vector<Pawn>();
		
		ImageIcon bluePawnIcon = new ImageIcon(getClass().getResource("bluePawn1.png"));
		
		public Felter(int r, int k) {
			row = r;
			column = k;
			setOpaque(true);
			setBorderPainted(false);
			setContentAreaFilled(false);
			setIcon(bluePawnIcon);
			setBorder(BorderFactory.createLineBorder(color[5]));
		}
		
		public int getFeltrow() {
			return row;
		}
		
		public int getFeltcolumn() {
			return column;
		}
		/**
		 * Paints the field in the correct color
		 */
		/*
		public void paintComponent(Graphics g) {
			
			if ( row < 6 && column >= 0 && column < 6) {
			super.paintComponent(g);
			g.drawRect(0, 0, 145, 80);
			g.setColor(color[1]);
			g.fillRect(0, 0, 145, 80);
			}
			else if (row >= 9 && column < 6) {
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[2]);
				g.fillRect(0, 0, 145, 80);
			}
			else if (row < 6 && column >= 9){
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[3]);
				g.fillRect(0, 0, 145, 80);
			}
			else if (row >= 9 && column >= 9){
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[4]);
				g.fillRect(0, 0, 145, 80);
			}
			else if(row >= 6 && row <=7  && column == 1 ) {
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[1]);
				g.fillRect(0, 0, 145, 80);
				
			}
			else if(row == 1 && column  >= 7 && column <=8) {
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[3]);
				g.fillRect(0, 0, 145, 80);
			}
			else if(row >= 7 && row <= 8 && column == 13) {
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[4]);
				g.fillRect(0, 0, 145, 80);
			}
			else if(row == 13 && column >= 6 && column <= 7) {
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[2]);
				g.fillRect(0, 0, 145, 80);
			}
			else if (column == 7 && row >= 9 && row <=13) {
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[2]);
				g.fillRect(0, 0, 145, 80);
			}
			else if (row == 7 && column >= 2 && column <= 5) {
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[1]);
				g.fillRect(0, 0, 145, 80);
			}
			else if (column == 7 && row >= 2 && row <=5) {
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[3]);
				g.fillRect(0, 0, 145, 80);
			}
			else if( row == 7 && column >= 9 && column <= 12) {
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[4]);
				g.fillRect(0, 0, 145, 80);
			}
			else if (row >= 6 && row <= 8 && column >= 6 && column <= 8){
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[6]);
				g.fillRect(0, 0, 145, 80);
			}
		}
		*/
		public void makePawn(String color) {
			
			pawn = new JButton(color); 
			add(pawn);
			
		}
		
		public void newRedPawn(int pawnloc, int nr, int row, int col) {
			RedPawn.add(new Pawn(pawnloc, nr));
			//add(RedPawn);
		}
	}
	
}
