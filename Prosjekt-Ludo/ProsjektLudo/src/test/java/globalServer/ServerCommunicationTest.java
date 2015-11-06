package globalServer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerCommunicationTest {
	private final String throwDiceText = "THROWDICE:";
    private final String receiveDiceText = "RECEIVEDICE:";
    
    private Socket socket;
    private BufferedWriter output;
	private BufferedReader input;
	private ExecutorService executorService;
	private String tmp;
	private GlobalServer server;
    
	@Before
	public void startServer() throws UnknownHostException, IOException {
		server = new GlobalServer();
		socket = new Socket("127.0.0.1",12347);
		
		output = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream()));
        input = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        
        executorService = Executors.newCachedThreadPool();
	}
	
	public void sendText(String textToSend) throws IOException {
            output.write(textToSend);
            output.newLine();
            output.flush();
	}
	
	@Test
	public void diceTest() throws IOException {
		
		sendText("SENDLOGIN:user");
		sendText("SENDLOGIN:pass");
		sendText(throwDiceText);
		
		executorService.execute(() ->{
			while (true) {
				try {
					tmp = input.readLine();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		assert(tmp.startsWith(receiveDiceText));
	}
}
