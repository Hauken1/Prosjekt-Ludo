package no.hig.Haukaas.Ludo;

import javax.swing.JFrame;

import globalServer.DatabaseHandler;

public class LudoMain {

	  public static void main( String[] args )
	    {
		 	//new DatabaseHandler(); 
		  
		  LoginClient application = new LoginClient("127.0.0.1");
		  application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      application.connect(); // Connect to the server
		  
		  

	       /* LoginClient client; 
	        
	        if (args.length == 0) {
	        	client = new LoginClient("127.0.0.1");	//Localhost
	        	//client = new LoginClient("128.39.83.101");	//Min IP
	        }
	        else
	        	client = new LoginClient(args[0]);	//use args
	        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }*/
	    
	    }
}
