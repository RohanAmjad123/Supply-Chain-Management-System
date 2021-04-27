package edu.ucalgary.ensf409;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import jdk.jfr.Timestamp;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Unit Testing File. These unit tests only work with the provided inventory.sql file and may not work with others.
 * Make sure to reset the state of the database by running inventory.sql before you run the unit tests. 
 * @since 1.0
 * @author Azlan Amjad <a href="mailto:azlan.amjad1@ucalgary.ca">azlan.amjad1@ucalgary.ca</a>
 * @author Sajid Hafiz <a href="mailto:sajid.hafiz1@ucalgary.ca">sajid.hafiz1@ucalgary.ca</a>
 * @author Saud Agha <a href="mailto:saud.agha1@ucalgary.ca">saud.agha1@ucalgary.ca</a>
 * @author Rohan Amjad <a href="mailto:rohan.amjad@ucalgary.ca">rohan.amjad@ucalgary.ca</a>
 * @version 1.5
 */
 public class InventoryTest {
	 public final static String fileName = "orderform.txt";
	 public final static String fileName1 = "badorder.txt";

	 @Test
	 // Check if the actual output of a successful order of a single requested furniture item  matches the expected output
	 public void testWriteFileSuccessfulOrder1() {
		 String category = "chair";
		 String type = "mesh";
		 int quantity = 1;
		 String input = type + " " + category + ", " + quantity;
		 // Initialize the connection 
		 var rfd = new ReadFromDatabase();
		 rfd.initializeFurniture(category, type);
		 // Fill in the furniture List data in the db object in myInv
		 Inventory myInv = new Inventory(category, type, quantity, rfd);
		 // Create the file
		 ArrayList<Furniture> validSolutions = myInv.calculateCheapest();
		 myInv.writeFile(myInv.formattedOutput(input, validSolutions));

		 // Need to insert back into the DB to keep DB consistent for future tests.
		 for (Furniture validSolution : validSolutions) {
			 TestMethods.insertIntoChair(validSolution, rfd);
		 }
		 // Close the connection
		 rfd.close();
		 // Read from test file and orderform.txt
		 String actualOutput = TestMethods.readFile(TestMethods.getRelativePath(fileName, "dat"));
		 String expectedOutput = TestMethods.readFile(TestMethods.getRelativePath("test.txt", "test-files"));
		 assertEquals("The expected output does not match the actual file", expectedOutput, actualOutput);
	 }

	 @Test
	 // Check if the furniture was removed from the database after a successful order
	public void testFurnitureIsRemoved() {
		 String category = "chair";
		 String type = "mesh";
		 int quantity = 1;
		 // Initialize the connection
		 var rfd = new ReadFromDatabase();
		 rfd.initializeFurniture(category, type);
		 // Fill in the furniture List data in the db object in myInv
		 Inventory myInv = new Inventory(category, type, quantity, rfd);
		 // calculateCheapest will call removeFurniture on furniture with ID: C6748, C8138, C9890
		 ArrayList<Furniture> validSolutions = myInv.calculateCheapest();
		 int rowsAffected = 0;
		 // rowsAffected should be 3 after inserting three new furniture (since they should be removed).
		 for (Furniture validSolution : validSolutions) {
			rowsAffected += TestMethods.insertIntoChair(validSolution, rfd);
		 }
		 // Close the connection
		 rfd.close();
		 assertEquals("Furniture was not correctly removed from the db", 3, rowsAffected);
	 }
	 @Test
	// Check the output of an unsuccessful order when ordering a chair
	public void testUnsuccessfulOrderChair() {
		 String category = "chair";
		 String type = "mesh";
		 int quantity = 4;
		 String input = type + " " + category + ", " + quantity;
		 // Initialize the connection
		 var rfd = new ReadFromDatabase();
		 rfd.initializeFurniture(category, type);
		 // Fill in the furniture List data in the db object in myInv
		 Inventory myInv = new Inventory(category, type, quantity, rfd);

		 // Create the file
		 ArrayList<Furniture> validSolutions = myInv.calculateCheapest();
		 myInv.writeFile(myInv.formattedOutput(input, validSolutions));

		 rfd.close();

		 // Read from test file and orderform.txt
		 String actualOutput = TestMethods.readFile(TestMethods.getRelativePath(fileName1, "dat"));
		 String expectedOutput = TestMethods.readFile(TestMethods.getRelativePath("test1.txt", "test-files"));
		 assertEquals("The expected output does not match the actual file",  expectedOutput, actualOutput);
	 }

	 @Test
	// Check the output of an unsuccessful order when ordering a desk
	public void testUnsuccessfulOrderDesk() {
		 String category = "desk";
		 String type = "adjustable";
		 int quantity = 4;
		 String input = type + " " + category + ", " + quantity;
		 // Initialize the connection
		 var rfd = new ReadFromDatabase();
		 rfd.initializeFurniture(category, type);
		 // Fill in the furniture List data in the db object in myInv
		 Inventory myInv = new Inventory(category, type, quantity, rfd);

		 // Create the file
		 ArrayList<Furniture> validSolutions = myInv.calculateCheapest();
		 myInv.writeFile(myInv.formattedOutput(input, validSolutions));
		// Close the connection
		 rfd.close();
		 // Read from test file and orderform.txt
		 String actualOutput = TestMethods.readFile(TestMethods.getRelativePath(fileName1, "dat"));
		 String expectedOutput = TestMethods.readFile(TestMethods.getRelativePath("test2.txt", "test-files"));
		 assertEquals("The expected output does not match the actual file", expectedOutput, actualOutput);
	 }
	 @Test
	// Check the output of an unsuccessful order when ordering a lamp
	public void testUnsucessfulOrderLamp() {
		 String category = "lamp";
		 String type = "desk";
		 int quantity = 4;
		 String input = type + " " + category + ", " + quantity;
		 // Initialize the connection
		 var rfd = new ReadFromDatabase();
		 rfd.initializeFurniture(category, type);
		 // Fill in the furniture List data in the db object in myInv
		 Inventory myInv = new Inventory(category, type, quantity, rfd);

		 // Create the file
		 ArrayList<Furniture> validSolutions = myInv.calculateCheapest();
		 myInv.writeFile(myInv.formattedOutput(input, validSolutions));

		 // Close the connection
		 rfd.close();

		 // Read from test file and orderform.txt
		 String actualOutput = TestMethods.readFile(TestMethods.getRelativePath(fileName1, "dat"));
		 String expectedOutput = TestMethods.readFile(TestMethods.getRelativePath("test3.txt", "test-files"));
		 assertEquals("The expected output does not match the actual file", expectedOutput, actualOutput);
	 }

	 @Test
	// Check the output of an unsuccessful order when ordering a filing
	public void testUnsuccessfulOrderFiling() {
		 String category = "filing";
		 String type = "large";
		 int quantity = 4;
		 String input = type + " " + category + ", " + quantity;
		 // Initialize the connection
		 var rfd = new ReadFromDatabase();
		 rfd.initializeFurniture(category, type);
		 // Fill in the furniture List data in the db object in myInv
		 Inventory myInv = new Inventory(category, type, quantity, rfd);

		 // Create the file
		 ArrayList<Furniture> validSolutions = myInv.calculateCheapest();
		 myInv.writeFile(myInv.formattedOutput(input, validSolutions));

		 //Close the connection
		 rfd.close();
		 // Read from test file and orderform.txt
		 String actualOutput = TestMethods.readFile(TestMethods.getRelativePath(fileName1, "dat"));
		 String expectedOutput = TestMethods.readFile(TestMethods.getRelativePath("test4.txt", "test-files"));
		 assertEquals("The expected output does not match the actual file", expectedOutput, actualOutput);
	 }

	@Test
	// Check if the actual output of a successful order of multiple requested furniture items matches the expected output
	public void testWriteFileSuccessfulOrder2() {
		String category = "filing";
		String type = "small";
		int quantity = 2;
		String input = type + " " + category + ", " + quantity;
		// Initialize the connection
		var rfd = new ReadFromDatabase();
		rfd.initializeFurniture(category, type);
		// Fill in the furniture List data in the db object in myInv
		Inventory myInv = new Inventory(category, type, quantity, rfd);
		// Create the file
		ArrayList<Furniture> validSolutions = myInv.calculateCheapest();
		myInv.writeFile(myInv.formattedOutput(input, validSolutions));

		// Need to insert back into the DB to keep DB consistent for future tests.
		for (Furniture validSolution : validSolutions) {
			TestMethods.insertIntoFiling(validSolution, rfd);
		}
		// Close the connection
		rfd.close();
		// Read from test file and orderform.txt
		String actualOutput = TestMethods.readFile(TestMethods.getRelativePath(fileName, "dat"));
		String expectedOutput = TestMethods.readFile(TestMethods.getRelativePath("test5.txt", "test-files"));
		assertEquals("The expected output does not match the actual file", expectedOutput, actualOutput);
	}
	@Test
	// Check that having an empty furnitureList will correctly throw a "NoFurnituresFoundException"
	public void testNoFurnituresFoundException() {
		String category = "chair";
		String type = "kneeling";
		int quantity = 2;
		String input = type + " " + category + ", " + quantity;
		// Initialize the connection
		var rfd = new ReadFromDatabase();
		var rfdTemp = new ReadFromDatabase();
		rfdTemp.initializeFurniture(category, type);

		// Temp now holds the two kneeling chair furnitures
		ArrayList<Furniture> temp = rfdTemp.getFurnitureList();
		rfdTemp.close();

		// Remove the two kneeling chairs from the database
		for (Furniture furniture : temp) {
			rfd.removeFurniture(furniture, category);
		}
		// Initialize the furnitureList inside rfd. The size should be zero.
		rfd.initializeFurniture(category, type);
		Inventory myInv = new Inventory(category, type, quantity, rfd);
		// calculate cheapest should throw NoFurnituresFoundException
		try {
			myInv.calculateCheapest();
			fail("The custom exception: NoFurnituresFoundException was not thrown...");

		} catch (NoFurnituresFoundException e) {
			assertEquals("No furniture was found in the array list of Furniture!", e.getMessage());
		}
		// Insert furniture items back into the database and then close the connection.
		finally {
			for (Furniture furniture : temp) {
				TestMethods.insertIntoChair(furniture, rfd);
			}
			rfd.close();
		}
	}

	@Test
	// Testing getters in Inventory/ReadFromDatabase class
	public void testGettersInventory(){
	 	ReadFromDatabase tempDB = new ReadFromDatabase();
	 	Inventory tempInv = new Inventory("chair", "mesh",2,tempDB);
	 	String category = tempInv.getCategory();
	 	String type = tempInv.getType();
	 	int quantity = tempInv.getQuantity();
	 	ReadFromDatabase bruh = tempInv.getDb();
	 	String expected = "chair" + "mesh" + "2" + bruh.getAllFurniture().toString();
	 	String actual = category + "" + type + "" + Integer.toString(quantity) + "" + tempDB.getAllFurniture().toString();
	 	assertEquals("The expected output does not match the actual output!", expected,actual);
	}

	@Test
	// Testing getters in Furniture class
	public void testGettersFurniture(){
	 	ArrayList<Part> tempParts = new ArrayList<>();
	 	for(int i = 0; i < 6; i++){
			tempParts.add(new Part("part" + Integer.toString(i), "Y"));
		}
	 	Furniture tempFurniture = new Furniture("12345", "type", tempParts,100,"54321");
	 	String id = tempFurniture.getID();
	 	String type = tempFurniture.getType();
	 	int price = tempFurniture.getPrice();
	 	String manuID = tempFurniture.getManuID();
	 	String expected = "12345" + "" + "type" + tempParts.toString() + "" + Integer.toString(100) + "" + "54321";
		String actual = tempFurniture.getID() + "" + tempFurniture.getType() + "" + tempFurniture.getParts().toString() + ""
				+ Integer.toString(tempFurniture.getPrice()) + "" + tempFurniture.getManuID();
		assertEquals("The expected output does not match the actual output!", expected,actual);
	}

	@Test
	// Testing getters in Parts class
	public void testGettersPart(){
	 	Part tempPart = new Part("part", "Y");
	 	String expected = "part" + "" + "Y";
	 	String actual = tempPart.getPartName() + "" + tempPart.getUsable();
		assertEquals("The expected output does not match the actual output!", expected,actual);
	}

	@Test
	// Testing getters for ReadFromDatabase class
	public void testGettersReadFromDatabase(){
	 	ReadFromDatabase testDatabase = new ReadFromDatabase();
	 	// tests getter for FurnitureList
	 	testDatabase.initializeFurniture("chair", "Kneeling");
	 	String expected = "C1320" + "C3819";
	 	String actual = testDatabase.getFurnitureList().get(0).getID() + "" + testDatabase.getFurnitureList().get(1).getID();
	 	// tests getter for AllType
	 	testDatabase.setAllType("CHAIR");
	 	String expected1 = "Task";
	 	String actual1 = testDatabase.getAllType().get(0);
	 	// tests getter for AllFurniture
		testDatabase.setAllFurniture();
		// Note depending on OS the table name for 'chair' in your database software may be all caps or all lower-case. This is
		// for a Mac configuration where the table name is all CAPS.
		String expected2 = "CHAIR";
		String actual2 = testDatabase.getAllFurniture().get(0);
		// final string
		String finalExpected = expected + expected1 + expected2;
		String finalActual = actual + actual1 + actual2;
		assertEquals("the expected output does not match the actual output", finalActual.toLowerCase(), finalExpected.toLowerCase());
	}


	/*
TestMethods class. A collection of helper methods to be used for Unit Testing.
 */
	static class TestMethods {

		/**
		 * Inserts a particular furniture object into the database in the filing table and returns how many rows were affected
		 * @param myFurniture Furniture ID
		 * @param rfd Furniture type
		 * @return an integer corresponding to how many rows affected
		 */
		public static int insertIntoFiling(Furniture myFurniture, ReadFromDatabase rfd) {
			String query = "INSERT INTO FILING (ID, Type, Rails, Drawers, Cabinet, Price, ManuID) VALUES (?, ?, ?, ?, ?, ?, ?)";
			int rowsAffected = 0;
			try {
				Connection myConnection = rfd.getDbConnect();
				PreparedStatement myStmt = myConnection.prepareStatement(query);
				myStmt.setString(1, myFurniture.getID());
				myStmt.setString(2, myFurniture.getType());
				myStmt.setString(3, myFurniture.getParts().get(2).getUsable());
				myStmt.setString(4, myFurniture.getParts().get(1).getUsable());
				myStmt.setString(5, myFurniture.getParts().get(0).getUsable());
				myStmt.setString(6, String.valueOf(myFurniture.getPrice()));
				myStmt.setString(7, myFurniture.getManuID());
				rowsAffected = myStmt.executeUpdate();
				myStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return rowsAffected;
		}

		public void insertIntoLamp(ArrayList<Furniture> myFurniture) {

		}

		/**
		 * Inserts a particular furniture object into the database in the chair table and returns how many rows were affected
		 * @param myFurniture Furniture ID
		 * @param rfd Furniture type
		 * @return an integer corresponding to how many rows affected
		 */
		public static int insertIntoChair(Furniture myFurniture, ReadFromDatabase rfd) {
			String query = "INSERT INTO CHAIR (ID, Type, Legs, Arms, Seat, Cushion, Price, ManuID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			int rowsAffected = 0;
			try {
				Connection myConnection = rfd.getDbConnect();
				PreparedStatement myStmt = myConnection.prepareStatement(query);
				myStmt.setString(1, myFurniture.getID());
				myStmt.setString(2, myFurniture.getType());
				myStmt.setString(3, myFurniture.getParts().get(3).getUsable());
				myStmt.setString(4, myFurniture.getParts().get(2).getUsable());
				myStmt.setString(5, myFurniture.getParts().get(1).getUsable());
				myStmt.setString(6, myFurniture.getParts().get(0).getUsable());
				myStmt.setString(7, String.valueOf(myFurniture.getPrice()));
				myStmt.setString(8, myFurniture.getManuID());
				rowsAffected = myStmt.executeUpdate();
				myStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return rowsAffected;
		}

		/**
		 * Reads and returns the contents of a file that is read line by line.
		 * @param testFileName the name of the file
		 * @return the contents of the file
		 */
		public static String readFile(String testFileName) {
			StringBuilder formattedString = new StringBuilder();
			try {
				Scanner scanner = new Scanner(new File(testFileName));
				while (scanner.hasNextLine()) {
					formattedString.append(scanner.nextLine());
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return formattedString.toString();
		}

		/**
		 *  Give us the full relative path to the filename, OS independently
		 * @param fileName is the name of the file ex: "orderform.txt"
		 * @param DIR is the name of the folder in which the filename will be located i.e. the directory
		 */
		public static String getRelativePath(String fileName, String DIR) {
			File path = new File(DIR);
			File full = new File(path, fileName);
			return full.getPath();
		}
	}
 }
