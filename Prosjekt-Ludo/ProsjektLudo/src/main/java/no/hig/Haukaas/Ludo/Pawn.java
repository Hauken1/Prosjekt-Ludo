package no.hig.Haukaas.Ludo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JButton;
import no.hig.Haukaas.Ludo.LudoBoard;


public class Pawn {
	private JButton pawnImg;
	private int pawnnr;
	private int location; //Hvor er den? location 0-3 er hjemmeplassering.
	private int homelocation;
	private int color;	//Hvilken farge?
	private final static int POTITIONS = 62;
	private Vector<Point> coordinates= new Vector<>();
	private LudoBoard board;
	private Color paintColor[] = {Color.BLACK, Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE};
	private Graphics g;
	
	
	public Pawn(int loc, int col){
		homelocation = loc;
		pawnnr = loc; 
		color = col;
		
		System.out.println("1");
		
		if (color == 1) {
			System.out.println("2");
		//	for(int i = 0; i< 4; i++ ) {
				System.out.println("3");
				Point p;
			//	p = (Point) getGreenCoordinates(i);
				System.out.println("4");
			//	coordinates.add(new Point(p.x, p.y));
				
				System.out.println("hei");
				
		//	}
		} else if (color == 2) {
			for(int i = 0; i < POTITIONS; i++) {
				Point p;
				p = board.getYellowCoordinates(i);
				coordinates.add(p);
				
			}
		} else if (color == 3) {
			for(int i = 0; i < POTITIONS; i++) {
				Point p;
				p = board.getRedCoordinates(i);
				coordinates.add(p);
			}
			
		} else if (color == 4) {
			for(int i = 0; i < POTITIONS; i++) {
				Point p;
				p = board.getBlueCoordinates(i);
				coordinates.add(p);
			}
		}
		
		try {
		PaintComponent(g);
		} catch (Exception e) {
			System.out.println("Something went wrong when trying to paint pawns");
		}
		//addToBoard(n);
		
	}
		
	private void PaintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		float thickness = 4;
		g2.setStroke(new BasicStroke(thickness));
		
		g2.setColor(paintColor[0]);
		
		g2.drawOval(coordinates.elementAt(homelocation).x, coordinates.elementAt(homelocation).y, 40, 40);
		g2.setColor(paintColor[color]);
		g2.fillOval(coordinates.elementAt(homelocation).x, coordinates.elementAt(homelocation).y, 40, 40);
	}
	
	private void addToBoard(int n) {
		// TODO Auto-generated method stub
		
		
	}

	public int returnLocation() {
		return location;
	}
	
	public int pawnNr() {
		return pawnnr;
	}
	
}
