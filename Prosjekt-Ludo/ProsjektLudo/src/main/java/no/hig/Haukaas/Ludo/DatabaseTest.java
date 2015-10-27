package no.hig.Haukaas.Ludo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {
	
	private static final String URL = "jdbc:mysql://mysql.stud.hig.no/s130443";
	private static final String USERNAME = "s130443";
	private static final String PASSWORD = "IR5ouFpy";
	
	private static final String CONNECTION_FAILED = "Failed to connect to database";

	
	Connection connection = null;
	
	public DatabaseTest() {
		readAllUsers();
		
		if (registerNewUser("usernagfghfme", "password")) {
			System.out.println("Created new user");
		} else System.out.println("User already exists");
		readAllUsers();
	}
	/**
	 * Method for connecting to an external database.
	 */
	private void connectToDatabase() throws SQLException{
		connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
	
	private void readAllUsers() {
		String testquery = "SELECT * FROM users";
		Statement statement;
		
		try {
			connectToDatabase();
			statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(testquery);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int noColumns = metaData.getColumnCount();
			
			for (int i = 1; i <= noColumns; i++) {
				System.out.printf("%-8s\t", metaData.getColumnName(i));
			} System.out.println();
			
			while (resultSet.next()) {
				for (int i = 1; i <= noColumns; i++) {
					System.out.printf("%-8s\t", resultSet.getObject(i));
				} System.out.println();
			}
			
			connection.close();
		} catch (SQLException e) {
			System.out.println(CONNECTION_FAILED);
			e.printStackTrace();
		}
	}
	
	/**
	 * Method for checking if the user already exists based on it's username.
	 * @param username The username to check.
	 * @return Returns true if the user exists, else false.
	 */
	private boolean checkIfUserAlreadyExists(String username) {
		String query = "SELECT * FROM users WHERE username=\'" + username +"\'";
		Statement stmt;
		
		try {
			stmt = connection.createStatement();
			
			ResultSet resultSet = stmt.executeQuery(query);
			if (resultSet.next()) 		
				return true;
			
		} catch (SQLException e) {
			System.out.println(CONNECTION_FAILED);
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Method for registering a new user with a unique username and a password.
	 * In practice, this is just adding the user to the database.
	 * @param username The username for the user. Must be unique
	 * @param password The password for the user.
	 * @return Returns true if the user was registered, else false if already exists.
	 */
	private boolean registerNewUser(String username, String password) {
		String query = " insert into users (username, password) values (?, ?)";
		
		try {	
			connectToDatabase();
			PreparedStatement stmt = connection.prepareStatement(query);
			if (!checkIfUserAlreadyExists(username)) {
				stmt.setString(1, username);
				stmt.setString(2, password);
				
				stmt.execute();
				connection.close();
				return true;
			}
			else {
				connection.close();
				return false;
			}
			
		} catch (SQLException e) {
			System.out.println(CONNECTION_FAILED);
			e.printStackTrace();
		}
		return false;
	}
	

}
