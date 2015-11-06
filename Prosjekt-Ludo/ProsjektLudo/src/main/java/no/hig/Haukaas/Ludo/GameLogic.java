package no.hig.Haukaas.Ludo;

import java.util.Vector;

public class GameLogic {

	private int[][] places = new int[4][63];
	private int piecePosition[][] = new int[4][4];
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int YELLOW = 4;
	private Vector<Pawn> RedPawn = new Vector<Pawn>();   
	private Vector<Pawn> GreenPawn = new Vector<Pawn>();
	private Vector<Pawn> BluePawn = new Vector<Pawn>();
	private Vector<Pawn> YellowPawn = new Vector<Pawn>();
	
	
	public GameLogic() {
		
		for(int i = 1; i<=4; i++) {	
			piecePosition[RED][i] = 0;
			piecePosition[GREEN][i] = 0;
			piecePosition[BLUE][i] = 0;
			piecePosition[YELLOW][i] = 0;
			}
		
		for(int i = 1; i<=4; i++) {
		//	newRedPawn(i, i);
		}
		
	}
		// TODO Auto-generated constructor stub
	
	public void startPawn(){
		
	}
	/*
	public void newRedPawn(int pawnloc, int nr) {
		RedPawn.add(new Pawn(pawnloc, nr));
		
	}
	*/
}
