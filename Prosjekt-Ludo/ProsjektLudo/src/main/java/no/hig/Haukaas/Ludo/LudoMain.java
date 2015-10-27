package no.hig.Haukaas.Ludo;

import javax.swing.JFrame;

public class LudoMain {

	  public static void main( String[] args )
	    {
	        LoginClient client; 
	        
	        if (args.length == 0) {
	        	//client = new LoginClient("127.0.0.1");	//Localhost
	        	client = new LoginClient("128.39.168.159");	//Min IP
	        }
	        else
	        	client = new LoginClient(args[0]);	//use args
	        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
}
