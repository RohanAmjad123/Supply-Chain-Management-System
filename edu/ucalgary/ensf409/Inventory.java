package edu.ucalgary.ensf409;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Invontory class: includes methods for executing the application
 * @since 1.0
 * @author Sajid Hafiz <a href="mailto:sajid.hafiz1@ucalgary.ca">sajid.hafiz1@ucalgary.ca</a>
 * @author Azlan Amjad <a href="mailto:azlan.amjad1@ucalgary.ca">azlan.amjad1@ucalgary.ca</a>
 * @author Rohan Amjad <a href="mailto:rohan.amjad@ucalgary.ca">rohan.amjad@ucalgary.ca</a>
 * @author Saud Agha <a href="mailto:saud.agha1@ucalgary.ca">saud.agha1@ucalgary.ca</a>
 * @version 1.9
 */
public class Inventory {
    private ReadFromDatabase db;
    private final String CATEGORY;
    private final String TYPE;
    private final int QUANTITY;

    //constructor
    /**
     * Inventory constructor, will be used to instantiate Inventory object by the GUIController
     * @param category Furniture category
     * @param type Furniture type
     * @param quantity Requested Furniture quantity
     * @param database Database object, includes data members and methods for data manipulation
     */
    public Inventory(String category, String type, int quantity, ReadFromDatabase database){
        this.CATEGORY = category;
        this.TYPE = type;
        this.QUANTITY = quantity;
        this.db = database;
    }

    //getters
    /**
     * db getter method
     * @return ReadFromDatabase db
     */
    public ReadFromDatabase getDb() {
        return db;
    }
    /**
     * CATEGORY getter method
     * @return String CATEGORY
     */
    public String getCategory() {
        return CATEGORY;
    }
    /**
     * TYPE getter method
     * @return String TYPE
     */
    public String getType() {
        return TYPE;
    }
    /**
     * QUANTITY getter method
     * @return int QUANTITY
     */
    public int getQuantity() {
        return QUANTITY;
    }

    //publicly used methods
    /**
     * Creates a formatted string with all needed info like cost and ID.
     * @param input Is the input that the user entered
     * @param furnitures is the cheapest configuration of furnitures
     * @return Returns a formatted string.
     */
    public String formattedOutput(String input, ArrayList<Furniture> furnitures) {
        /*
        If furnitures is not null then output a string reflecting the cost, ID's and input of the paramater furnitures.
         */
        if (furnitures != null) {
            int totalCost = 0;
            totalCost += calcSum(furnitures);
            StringBuilder sb = new StringBuilder();
            sb.append("Furniture Order Form");
            sb.append("\n");
            sb.append("Faculty Name: ");
            sb.append("\n");
            sb.append("Contact: ");
            sb.append("\n");
            sb.append("\n");
            sb.append("Date: ");
            sb.append("\n");
            sb.append("Original Request:" + input);
            sb.append("\n");
            sb.append("Items Ordered");
            sb.append("\n");
            for (int i = 0; i < furnitures.size(); i++){
                sb.append("ID: " + furnitures.get(i).getID());
                sb.append("\n");
            }
            sb.append("Total Price: " + "$" + totalCost);

            return sb.toString();
        }
        /*
        If there is no valid solution i.e. furnitures is null, then the formatted string will reflect that.
         */
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("User request: " + input);
            sb.append("\n");
            sb.append("Output: Order cannot be fulfilled based on current inventory. Suggested manufacturers are: ");
            sb.append("\n");
            sb.append(manufacturerOfCategory());
            return sb.toString();
        }
    }
    /**
     * Calculate the cheapest configuration of furnitures that will allow you to build the needed furniture
     * @return the cheapest combination of furnitures needed which are stored in an ArrayList<Furniture>
     */
    public ArrayList<Furniture> calculateCheapest() {
        ArrayList<Furniture> furnitureList = this.db.getFurnitureList();
        // Throw the custome exception NoFurnituresFound if furnitureList size < 1
        if (furnitureList.size() < 1) {
            throw new NoFurnituresFoundException("No furniture was found in the array list of Furniture!");
        }
        ArrayList<ArrayList<Furniture>> solutions = new ArrayList<>();
        ArrayList<Furniture> cheapestSolutions;
        ArrayList<ArrayList<Furniture>> validSolutions = new ArrayList<>();
        long pow_set_size = (long)Math.pow(2, furnitureList.size());
        int counter, j;
        /*
        This will calculate the power set of furnitureList and add each combination to solutions.
         */
        for (counter = 0; counter<pow_set_size; counter++) {
            ArrayList<Furniture> temp = new ArrayList<>();
            for (j = 0; j< furnitureList.size(); j++) {
                if ((counter & (1 << j)) > 0) {
                    temp.add(furnitureList.get(j));
                }
            }
            solutions.add(temp);
        }
        /*
        Verify that each configuration in solution is a valid solution. If it is a valid configuration i.e. it has all parts,
        add the configuration to validSolutions.
         */
        for (int i = 1; i<solutions.size(); i++) {
            if (validateSolution(solutions.get(i))) {
                validSolutions.add(solutions.get(i));
            }
        }
        /*
        If there are no valid solutions, return null.
         */
        if (validSolutions.size()  == 0)  {
            return null;
        }

        /*
        Finds the cheapest configuration in the valid solutions linked list and sets the configuration
         to cheapestSolutions.
         */
        ArrayList<Furniture> tempSolution;
        tempSolution = validSolutions.get(0);
        int cost = calcSum(tempSolution);
        for (int r = 1; r<validSolutions.size(); r++) {
            if (calcSum(validSolutions.get(r)) < cost) {
                cost = calcSum(validSolutions.get(r));
                tempSolution = validSolutions.get(r);
            }
        }
        cheapestSolutions = tempSolution;

        /*
        Remove the furniture in cheapestSolutions from the database.
         */
        for (int i = 0; i<cheapestSolutions.size(); i++) {
            this.db.removeFurniture(cheapestSolutions.get(i), getCategory());
        }
        return cheapestSolutions;
    }
    /**
     * It writes into a file and then closes the stream.
     * @param formattedString is the formatted string to be written into the file.
     */
    public void writeFile(String formattedString) {
        String fileName = getRelativePath("orderform.txt");
        if (formattedString.charAt(0) == 'U') {
            fileName = getRelativePath("badorder.txt");
        }
     
        // If the directory does not exist, create it. If it does exist, make sure
        // it is a directory.
        File directory = new File("dat");
        try {
            if (! directory.exists()) {
                directory.mkdir();
            } else {
                if (! directory.isDirectory()) {
                    System.err.println("File " + "dat" + " exists but is not a directory.");
                    System.exit(1);
                }
            }
        }
        catch (Exception e) {
            System.err.println("Unable to create directory " + "dat" + ".");
            System.err.println(e.toString());
            System.exit(1);
        }

        // Try to write out to orderform.txt
        BufferedWriter file = null;
        try {
            file = new BufferedWriter(new FileWriter((fileName)));
            file.write(formattedString);
        }
        catch (Exception e) {
            System.err.println("ERROR" + "I/O error opening/writing file.");
            System.err.println(e.getMessage());
            closeWriter(file);
            System.exit(1);
        }
        closeWriter(file);
    }

    //privately used methods
    /**
     * Finds a list of manufacturers where they manufacture a particular category
     * @return Returns a string of manufacturer names
     */
    public String manufacturerOfCategory() {
        StringBuilder sb = new StringBuilder();
        switch(getCategory()) {
            case "chair":
                sb.append("Office Furnishings");
                sb.append("\n");
                sb.append("Chairs R Us");
                sb.append("\n");
                sb.append("Furniture Goods");
                sb.append("\n");
                sb.append("Fine Office Supplies");
                return sb.toString();

            case "desk":
                sb.append("Office Furnishings");
                sb.append("\n");
                sb.append("Academic Desks");
                sb.append("\n");
                sb.append("Furniture Goods");
                sb.append("\n");
                sb.append("Fine Office Supplies");
                return sb.toString();

            case "filing":

            case "lamp":
                sb.append("Office Furnishings");
                sb.append("\n");
                sb.append("Furniture Goods");
                sb.append("\n");
                sb.append("Fine Office Supplies");
                return sb.toString();
        }
        return "";
    }
    /**
     * Calculates the summed value of each furniture item in the linked list
     * @param furnitures Calculate the cost of every single item in the furnitures Linked List
     * @return an integer value corresponding to the total amount of the furnitures in the linked list
     */
    public int calcSum(ArrayList<Furniture> furnitures) { //helper method
        int totalSum = 0;
        for (int i = 0; i< furnitures.size(); i++) {
            totalSum += furnitures.get(i).getPrice();
        }
        return totalSum;
    }
    /**
     * Give us the full relative path to the filename, OS independently
     * @param fileName is the name of the file ex: "orderform.txt"
     */
    public String getRelativePath(String fileName) {
        File path = new File("dat");
        File full = new File(path, fileName);
        return full.getPath();
    }
    /**
     * Validates the configuration of furnitures.
     * @param solution is an array list of furniture to be tested for it's validity.
     * @return a boolean value. Return true if it is a valid solution i.e. this configuration has all the needed parts
     * otherwise, return false.
     */
    public boolean validateSolution(ArrayList<Furniture> solution) {
        /*
        Create a hashmap with <PartName, Quantity> key value pairs.
         */
        HashMap<String, Integer> partsFound = new HashMap<String, Integer>();
        int numParts = solution.get(0).getParts().size();
        for (int j = 0; j<numParts; j++) { //Initialize my hashmap
            String partName = solution.get(0).getParts().get(j).getPartName();
            partsFound.put(partName, 0);
        }
        /*
        If the iterated furniture part is usable, put in the hashmap the updated found parts and the partname.
         */
        for (int i = 0; i<solution.size(); i++) {
            for (int n = 0; n<numParts; n++) {
                if (solution.get(i).getParts().get(n).getUsable().equals("Y")) {
                    int val = partsFound.get(solution.get(i).getParts().get(n).getPartName());
                    partsFound.put(solution.get(i).getParts().get(n).getPartName(), val+1);
                }
            }
        }
        /*
        If the hashmap has any <K,V> pairs where the value is less than the quantity then return false as it is not
        a valid solution. Otherwise, return true.
         */
        for (int j = getQuantity()-1; j>=0; j--) {
            if (partsFound.containsValue(j)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Close the stream
     * @param file is a BuffedWriter object. It will attempt to close the file.
     */
    private void closeWriter(BufferedWriter file) {
        try {
            if (file != null) {
                file.close();
            }
        }
        catch (Exception e) {
            System.err.println("ERROR: " + "I/O error closing file.");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}