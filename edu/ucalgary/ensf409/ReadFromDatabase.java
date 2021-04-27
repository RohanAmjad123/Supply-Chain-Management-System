package edu.ucalgary.ensf409;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class ReadFromDatabase: reads required data from the database, contains methods and data members for data manipulation
 * @since 1.0
 * @author Sajid Hafiz <a href="mailto:sajid.hafiz1@ucalgary.ca">sajid.hafiz1@ucalgary.ca</a>
 * @author Azlan Amjad <a href="mailto:azlan.amjad1@ucalgary.ca">azlan.amjad1@ucalgary.ca</a>
 * @author Rohan Amjad <a href="mailto:rohan.amjad@ucalgary.ca">rohan.amjad@ucalgary.ca</a>
 * @author Saud Agha <a href="mailto:saud.agha1@ucalgary.ca">saud.agha1@ucalgary.ca</a>
 * @version 1.6
 */
public class ReadFromDatabase {
    private final String DBURL = "jdbc:mysql://localhost/INVENTORY";
    private final String USERNAME = "scm";
    private final String PASSWORD = "ensf409";
    private Connection dbConnect;
    private ArrayList<Furniture> furnitureList = new ArrayList<Furniture>();
    private ArrayList<String> allFurniture = new ArrayList<>();
    private ArrayList<String> allType = new ArrayList<>();

    //contructor
    /**
     * Class ReadFromDatabase constructor, constructor made for GUIController
     */
    public ReadFromDatabase() {
        initializeConnection();
        setAllFurniture();
    }

    //getters
    /**
     * dbConnect getter method
     * @return a Connection object. Used for Unit Testing
     */
    public Connection getDbConnect() {
        return dbConnect;
    }
    /**
     * furnitureList getter method
     * @return an ArrayList of furniture
     */
    public ArrayList<Furniture> getFurnitureList() {
        return this.furnitureList;
    }
    /**
     * allFurnitures getter methdo
     * @return an ArrayList<String> of all furniture in database
     */
    public ArrayList<String> getAllFurniture(){
        return this.allFurniture;
    }
    /**
     * allType getter method
     * @return an ArrayList<String> of all types in category
     */
    public ArrayList<String> getAllType(){
        return this.allType;
    }

    //setters
    /**
     * Sets an array list of all the Furniture in the databsae (Inventory.sql)
     */
    public void setAllFurniture(){
        try {
            ArrayList<String> furnitureList = new ArrayList<String>();
            DatabaseMetaData d = dbConnect.getMetaData();
            ResultSet r = d.getTables("INVENTORY",null,"%",null);
            while(r.next()){
                if(r.getString(4).equalsIgnoreCase("TABLE")){
                    furnitureList.add(r.getString(3));
                }
            }

            for(int i = 0; i < furnitureList.size(); i++){
                if(furnitureList.get(i).equalsIgnoreCase("MANUFACTURER")){
                    furnitureList.remove(i);
                }
            }

            this.allFurniture = furnitureList;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * Sets an array list of all the types in selected category
     * @param category Furniture category
     */
    public void setAllType(String category){
        try {
            ArrayList<String> allResults = new ArrayList<>();
            String query = "SELECT TYPE FROM " + category;
            Statement stmt = dbConnect.createStatement();
            ResultSet result = stmt.executeQuery(query);
            while(result.next()){
                allResults.add(result.getString("Type"));
            }
            for(int i = 0; i < allResults.size(); i++){
                for(int j = i+1; j < allResults.size(); j++){
                    if(allResults.get(i).equals(allResults.get(j))){
                        allResults.remove(j);
                    }
                }
            }
            Set<String> set = new HashSet<>(allResults);
            allType.addAll(set);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    //publicly used methods
    /**
     * Fills in the furnitureList given the category and type of furniture
     * @param category Furniture category
     * @param type Furniture type
     */
    public void initializeFurniture(String category, String type) {
        Statement stmt;
        ResultSet set;
        String query = "SELECT * FROM " + category + " WHERE Type = " + "'" + type + "'";
        try {
            stmt = dbConnect.createStatement();
            set = stmt.executeQuery(query);
            ResultSetMetaData setMetaData = set.getMetaData();

            int numberOfPartColumns = setMetaData.getColumnCount();

            while(set.next()) {
                ArrayList<Part> parts = new ArrayList<Part>();

                for (int i = numberOfPartColumns - 2; i > 2; i--) {
                    parts.add(new Part(setMetaData.getColumnLabel(i), set.getString(i)));
                }

                String id = set.getString("ID");
                String fType = set.getString("Type");
                String manuID = set.getString("ManuID");
                int price = set.getInt("Price");

                furnitureList.add(new Furniture(id, fType, parts, price, manuID));
            }

            stmt.close();
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes furniture from the category table using the primary key ID
     * @param furniture a furniture object to be deleted from the database
     */
    public void removeFurniture(Furniture furniture, String category)  {
        try {
            String query = "DELETE FROM " + category +" WHERE ID = ?";
            PreparedStatement stmt = dbConnect.prepareStatement(query);
            stmt.setString(1, furniture.getID());
            int col = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Closes connection to database
     */
    public void close() {
        try {
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempts to establish a connection to the database using the DBURL, USERNAME, PASSWORD
     */
    public void initializeConnection() {
        try {
            this.dbConnect = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

