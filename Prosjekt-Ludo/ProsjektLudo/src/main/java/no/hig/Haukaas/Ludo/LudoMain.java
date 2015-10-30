package no.hig.Haukaas.Ludo;

import javax.swing.JFrame;

public class LudoMain {

	  public static void main( String[] args )
	    {
		  
		  LoginClient application = new LoginClient("127.0.0.1");
		  application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      application.connect(); // Connect to the server
		  
		  
		  //ChatClient applicatin = new ChatClient();
	      //applicatin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      //applicatin.connect(); // Connect to the server
	      //applicatin.processConnection(); // Start processing messages from the
	                                        // server
		  /*
		  	//new DatabaseTest(); 
	        LoginClient client; 
	        
	        if (args.length == 0) {
	        	client = new LoginClient("127.0.0.1");	//Localhost
	        	//client = new LoginClient("128.39.83.101");	//Min IP
	        }
	        else
	        	client = new LoginClient(args[0]);	//use args
	        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
	    */
	    }
}
