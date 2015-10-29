package no.hig.Haukaas.Ludo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*Ser for meg at brettet skal være 15x15 (kan gjøres mindre da "hjemm-plassene", til de 
*ulike fargene kan gjøres mindre. 
*/
/**
 * 
 * Class that makes the game board
 */
public class LudoBoard extends JPanel {
	private Color color[] = {Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK};
	private final static int ROWS = 15;
	private final static int COLUMNS = 15;
	private Felter[][] felt;
	
	/**
	 * Constructor for the ludo board
	 * Makes all the columns and rows and paints them.
	 */
	public LudoBoard() {
		
		setLayout(new GridLayout(ROWS, COLUMNS, 0, 0));
		setBackground(color[0]);
		
		felt = new Felter[15][15];
		
		try {	
			for (int row = 0; row < felt.length; row++) {
				for(int col = 0; col < felt[row].length; col++) {
					felt[row][col] = new Felter(row, col);
					add(felt[row][col]);
				}
			}
			} catch (Exception e) {
				System.out.println("Error");
			}
	}//end LudoBoard constructor
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
	}
	
	/**
	 * 
	 * Class for a single field on the game board
	 *
	 */
	private class Felter extends JLabel {
		
		private int row;
		private int column;
		
		public Felter(int r, int k) {
			row = r;
			column = k;
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
		public void paintComponent(Graphics g) {
			
			if ( row < 6 & column >= 0 && column < 6) {
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
			else if (row < 6 & column >= 9){
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[3]);
				g.fillRect(0, 0, 145, 80);
			}
			else if (row >= 9 && column >= 9){
				g.drawRect(0, 0, 145, 80);
				g.setColor(color[3]);
				g.fillRect(0, 0, 145, 80);
			}
			
		}
		
		
	}
	
}
