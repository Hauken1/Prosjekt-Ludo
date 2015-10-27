package no.hig.Haukaas.Ludo;
import java.util.*;

/**
 * Class for finding the computer language and country so the correct
 * language will be displayed to the user.
 */
public class Internationalization {
	
	private ResourceBundle messages;
	
	/**
	 * Nothing
	 */
	public Internationalization() {
	}

    /**
     * Returns the choosen message
     * @param message tells what string to return
     * @return string with correct language or default set to Locale "us"
     */
    public String returnMessage(String message) {   	
    	Locale no = new Locale("no", "NO");
    	Locale us = new Locale("en", "US");
    	
    	if(no.equals(Locale.getDefault())) {
    		messages = ResourceBundle.getBundle("Resources/MessagesBundle", no);
    		return messages.getString(message);
    	}
    	if(us.equals(Locale.getDefault())) {
    		messages = ResourceBundle.getBundle("Resources/MessagesBundle", us);
    		return messages.getString(message);
    	}
    	
    	messages = ResourceBundle.getBundle("Resources/MessagesBundle", us);
		return messages.getString(message);
    }
}
