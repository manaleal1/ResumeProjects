import java.sql.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

/*************************************
 *	BEFORE RUNNING:
 *  o Read the README file in the current directory.
 *	o Change dataFileLocation to the correct location of the ml-1m directory.
 *	o Replace arguments with correct information in conn = DriverManager.getConnection()
 *
 *************************************/

public class hw6 {

	//This is the location to the ml-1m file directory. Since there are mulitple files to access, 
	//just store the file location in this variable. When you need to access a specific file, just do something like
	//String variableName = dataFileLocation + fileName.dat 
	static String dataFileLocation = "C://Replace//With//Correct//filepath//ml-1m//ml-1m//";
	
	public static void main(String[] args) {
		
	    Connection conn = null;
	    Statement stmt = null;
	    try {
	       conn = DriverManager.getConnection("Database_URL", "Username", "password");
	       stmt = conn.createStatement();
	    }
	    catch (SQLException se) {
	    	System.out.println(se);
	        System.exit(1);
	    }
	
	    //Drop all the tables
	    try {
	    	stmt.executeUpdate("DROP TABLE ratings");
	    	System.out.println("Table RATINGS dropped");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    try {
	    	stmt.executeUpdate("DROP TABLE movies");
	    	System.out.println("Table MOVIES dropped");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    try {
	    	stmt.executeUpdate("DROP TABLE users");
	    	System.out.println("Table USERS dropped");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    try {
	    	stmt.executeUpdate("DROP TABLE ageTable");
	    	System.out.println("Table AGETABLE dropped");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    try {
	    	stmt.executeUpdate("DROP TABLE occTable");
	    	System.out.println("Table OCCTABLE dropped");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    
	    System.out.println();
	    	
	    //create the tables
	    try {
	    	stmt.executeUpdate("CREATE TABLE users(UserID NUMBER(4), gender VARCHAR(1), ageCode NUMBER(3), occupationCode NUMBER(2), zipcode VARCHAR(10), CONSTRAINT UserID_PK PRIMARY KEY (UserID) )");
	    	System.out.println("Table USERS created");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    try {
	    	stmt.executeUpdate("CREATE TABLE ageTable(ageCode NUMBER(3), ageRange VARCHAR(8), CONSTRAINT ageCode_PK PRIMARY KEY (ageCode))");
	    	System.out.println("Table AGETABLE created");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    try {
	    	stmt.executeUpdate("CREATE TABLE occTable(occupationCode NUMBER(2), occupation VARCHAR(25), CONSTRAINT occupationCode_PK PRIMARY KEY (occupationCode))");
	    	System.out.println("Table OCCTABLE created");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    try {
	    	stmt.executeUpdate("CREATE TABLE movies(MovieID NUMBER(4), title VARCHAR(100), year VARCHAR(6), Category_1 VARCHAR(15),Category_2 VARCHAR(15),Category_3 VARCHAR(15),Category_4 VARCHAR(15),Category_5 VARCHAR(15),Category_6 VARCHAR(15), CONSTRAINT Movie_PK PRIMARY KEY (MovieID) )");
	    	//stmt.executeUpdate("CREATE TABLE movies(MovieID NUMBER(4), title VARCHAR(300), Category VARCHAR(400), CONSTRAINT Movie_PK PRIMARY KEY (MovieID) )");
	    	System.out.println("Table MOVIES created");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    try {
	    	stmt.executeUpdate("CREATE TABLE ratings(UserID NUMBER(4), MovieID NUMBER(4), Rating NUMBER(1), timestamp VARCHAR(10), CONSTRAINT UserID_FK FOREIGN KEY(UserID) REFERENCES users (UserID), CONSTRAINT MovieID_FK FOREIGN KEY(MovieID) REFERENCES movies (MovieID) )");
	    	System.out.println("Table RATINGS created");
	    }
	    catch(Exception e){
	    	System.out.println(e);
	    }
	    
	    System.out.println();
	    
	    //Insert data into ageTable
	    try {
	    	System.out.println("Inserting values into ageTable");
	    	stmt.executeUpdate("INSERT INTO ageTable VALUES(1, 'Under 18')");
	    	stmt.executeUpdate("INSERT INTO ageTable VALUES(18, '18-24')");
	    	stmt.executeUpdate("INSERT INTO ageTable VALUES(25, '25-34')");
	    	stmt.executeUpdate("INSERT INTO ageTable VALUES(35, '35-44')");
	    	stmt.executeUpdate("INSERT INTO ageTable VALUES(45, '45-49')");
	    	stmt.executeUpdate("INSERT INTO ageTable VALUES(50, '50-55')");
	    	stmt.executeUpdate("INSERT INTO ageTable VALUES(56, '56+')");
	    	System.out.println("Finished loading ageTable");
	    }
	    catch(Exception e) {
	    	System.out.println(e);
	    }
	    
	    System.out.println();
	    
	    //Insert data into occTable
	    try {
	    	System.out.println("Inserting values into occTable");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(0, 'other')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(1, 'academic/educator')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(2, 'artist')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(3, 'clerical/admin')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(4, 'college/grad student')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(5, 'customer service')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(6, 'doctor/health care')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(7, 'executive/managerial')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(8, 'farmer')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(9, 'homemaker')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(10, 'K-12 student')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(11, 'lawyer')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(12, 'programmer')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(13, 'retired')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(14, 'sales/marketing')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(15, 'scientist')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(16, 'self-employed')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(17, 'technician/engineer')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(18, 'tradesman/craftsman')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(19, 'unemployed')");
	    	stmt.executeUpdate("INSERT INTO occTable VALUES(20, 'writer')");
	    	System.out.println("Finished loading occTable");
	    }
	    catch(Exception e) {
	    	System.out.println(e);
	    }
	    
	    System.out.println();
	    
	    
	  
	  /*****************************
	   * TODO -- load users table   
	   ******************************/
	    System.out.println("Loading data into users Table from users.dat file");
	    ArrayList<String> usersList = new ArrayList<String>();
	    
	    try {
	    	//read from file
	    	Scanner usersFile = new Scanner(new FileReader(dataFileLocation + "users.dat"));  
			String test = "";
			
			try {
				while ((test = usersFile.nextLine()) != null) {
					usersList.add(test);
					//System.out.println(test);
				}
			}
			catch(NoSuchElementException s) {
				//System.out.println("Could not read the whole user.dat file.");
			}
			usersFile.close();
			
			String insertString = "INSERT INTO users VALUES(?, ?, ?, ?, ?)";
			PreparedStatement insertData = conn.prepareStatement(insertString);
			
			try {
				String line;
				StringTokenizer lineToken = null;
				Iterator itr = usersList.iterator(); 
				while(itr.hasNext()) {
					line = (String)itr.next();
					
					lineToken = new StringTokenizer(line, "::");
					String val;
					int i = 1;
					while(lineToken.hasMoreTokens()) {
						val = lineToken.nextToken();
						insertData.setString(i, val);
						i++;
					}
					insertData.addBatch();
				}
				insertData.executeBatch();
			}
			catch(Exception e){
				System.out.println(e);
			}
	    }
	    catch(Exception e) {
	    	System.out.println();
	    }
	    System.out.println("Finished Loading data into users Table.");
	    
	    System.out.println();
	    
	    /*******************************
	     * TODO -- load movies table
	     *******************************/
	    System.out.println("Loading data into movies Table from movies.dat file");
	    ArrayList<String> moviesList = new ArrayList<String>();
	    
	    try {
	    	//read from file
	    	Scanner moviesFile = new Scanner(new FileReader(dataFileLocation + "movies.dat"));  
			String test = "";
			
			try {
				while ((test = moviesFile.nextLine()) != null) {
					moviesList.add(test);
					//System.out.println(test);
				}
			}
			catch(NoSuchElementException s) {
				//System.out.println("Could not read the whole movies.dat file.");
			}
			moviesFile.close();
			
			String insertString = "INSERT INTO movies VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement insertData = conn.prepareStatement(insertString);
			
			try {
				String line;
				String[] lineToken = null;
				String[] categoryToken = null;
				String[] yearToken = null;
				Iterator itr = moviesList.iterator(); 
				while(itr.hasNext()) {
					line = (String)itr.next();
					lineToken = line.split(":{2}");
					//yearToken = lineToken[1].split("\\p{Punct}");
					yearToken = lineToken[1].split("[\\(]|[\\)]");
					categoryToken = lineToken[2].split("\\|");
					String val = null;
					int i = 1;
										
					insertData.setString(i, lineToken[0]); //MovieID
					i++;
					if(yearToken.length == 4) {
						insertData.setString(i, yearToken[0] + yearToken[1]); // Title
						i++;
					}
					else {	
						insertData.setString(i, yearToken[0]); 
						i++;
					}
					insertData.setString(i, yearToken[yearToken.length-1]); //yearToken[1] contains year
					i++;
					for (int count = 0; count < categoryToken.length; count++) { 
						val = categoryToken[count];				//category_1 - 6 
						insertData.setString(i, val);
						i++;
					}
					while(i <= 9) { 	//unused category columns get filled with null 
						insertData.setString(i, null);
						i++;
					}
					insertData.addBatch();
					
				}// end of outer while
				insertData.executeBatch();
			}
			catch(Exception e){
				System.out.println("Error while inserting data into movies table:");
				System.out.println(e);
			}
	    }
	    catch(Exception e) {
	    	System.out.println(e);
	    }
	    System.out.println("Finished Loading data into movies Table.");
	    
	    System.out.println();
	    
	    
	    /*****************************
	     * TODO -- load ratings table
	     *****************************/
	    
	    System.out.println("Loading data into ratings Table from ratings.dat file");
	    ArrayList<String> ratingsList = new ArrayList<String>();
	    
	    try {
	    	//read from file
	    	Scanner ratingsFile = new Scanner(new FileReader(dataFileLocation + "ratings.dat"));  
			String test = "";
			
			try {
				while ((test = ratingsFile.nextLine()) != null) {
					ratingsList.add(test);
					//System.out.println(test);
				}
			}
			catch(NoSuchElementException s) {
				//System.out.println("Could not read the whole ratings.dat file.");
			}
			ratingsFile.close();
			
			String insertString = "INSERT INTO ratings VALUES(?, ?, ?, ?)";
			PreparedStatement insertData = conn.prepareStatement(insertString);
			
			try {
				String line;
				StringTokenizer lineToken = null;
				Iterator itr = ratingsList.iterator(); 
				while(itr.hasNext()) {
					line = (String)itr.next();
					
					lineToken = new StringTokenizer(line, "::");
					String val;
					int i = 1;
					while(lineToken.hasMoreTokens()) {
						val = lineToken.nextToken();
						insertData.setString(i, val);
						i++;
					}
					insertData.addBatch();
				}
				insertData.executeBatch();
			}
			catch(Exception e){
				System.out.println(e);
			}
	    }
	    catch(Exception e) {
	    	System.out.println();
	    }
	    System.out.println("Finished Loading data into ratings Table.");
	    
	    System.out.println();
	    
	    
	    // Number of rows loaded in each table
	    try{
			ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM users");
			while(result.next())
				System.out.println("Users Rows loaded: " + result.getInt(1));
			result = stmt.executeQuery("SELECT COUNT(*) FROM ageTable");
			while(result.next())
				System.out.println("ageTable Rows loaded: " + result.getInt(1) );
			result = stmt.executeQuery("SELECT COUNT(*) FROM occTable"); 
			while(result.next())
				System.out.println("occTable Rows loaded: " + result.getInt(1));
			result = stmt.executeQuery("SELECT COUNT(*) FROM movies");
			while(result.next())
				System.out.println("movies Rows loaded: " + result.getInt(1));
			result = stmt.executeQuery("SELECT COUNT(*) FROM ratings"); 
			while(result.next())
				System.out.println("ratings Rows loaded: " + result.getInt(1));
		} catch(Exception e){
			System.out.println(e);
		}
	    
	    
	    System.out.println();
	    
	    
	    /**************************************
	     * TODO -- write an interesting query
	     **************************************/
	    try {
	    	System.out.println("The following is the total number of ratings for movies that were released in given year");
	    	
	    	ResultSet result = stmt.executeQuery("SELECT year, COUNT(RATING) " + 
	    											"FROM MOVIES m, RATINGS r " + 
	    											"WHERE m.movieid = r.movieid " + 
	    											"GROUP BY YEAR " + 
	    											"ORDER BY YEAR" 
	    											);
	    	System.out.println("Year TotalRatings");
	    	System.out.println("---- ------------");
	    	while(result.next()) {
	    		System.out.println(result.getString(1)+ " " + result.getString(2));
	    	}
	    	
	    }
	    catch(Exception e) {
	    	System.out.println(e);
	    }
	    
	    System.out.println();
	    
	    
	    try {
	    	stmt.close();
	    	conn.close();
	    }
	    catch(Exception e) {
	    	System.out.println(e);
	    }
	}//end of main
	
}// end of class
