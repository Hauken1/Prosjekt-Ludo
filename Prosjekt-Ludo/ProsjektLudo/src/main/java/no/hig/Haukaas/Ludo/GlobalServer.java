package no.hig.Haukaas.Ludo;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GlobalServer {
	
	private ServerSocket server;
	private Player[] players;
	private int currentPlayer;
	private ExecutorService runGame;
	private Lock gameLock;
	private Condition otherPlayerConnected;
	private Condition otherPlayerTurn;
	private final static int PLAYER_X = 0;
	private final static int PLAYER_Y = 1;
	
	
	
	public GlobalServer() {
		
		runGame = Executors.newFixedThreadPool(2);
		gameLock = new ReentrantLock();
		
		otherPlayerConnected = gameLock.newCondition();
		
		otherPlayerTurn = gameLock.newCondition();
		
		players = new Player[2];
		
		currentPlayer = PLAYER_X;
		
		try {
			server = new ServerSocket(12345, 2);
		} catch (IOException ioE) {
			ioE.printStackTrace();
		}
		
	}

	public void execute() {
		for (int i=0; i<players.length; i++) {
			try {
				players[i] = new Player(server.accept(), i);
				runGame.execute(players[i]);
			} catch (IOException ioE) {
				ioE.printStackTrace();
			}
		}
		
		gameLock.lock();
		
		try {
			players[PLAYER_X].setSuspended(false);
			otherPlayerConnected.signal();
		} finally {
			gameLock.unlock();
		}	
	}
	
	
	
}