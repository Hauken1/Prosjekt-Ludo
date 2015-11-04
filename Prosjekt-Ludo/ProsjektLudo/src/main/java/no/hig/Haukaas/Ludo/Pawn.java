package no.hig.Haukaas.Ludo;

import javax.swing.JButton;


public class Pawn {
	private JButton pawnImg;
	private int pawnnr;
	private int location; 
	
	public Pawn(int pawnloc, int n){
		location = pawnloc;
		pawnnr = n;
		
		addToBoard(n);
		
	}
	
	public Pawn(JButton button, int loc) {
		pawnImg = button;
		location = loc;
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
