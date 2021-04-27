package edu.ucalgary.ensf409;

import java.util.ArrayList;

/**
 * Class Furniture: defintion for a Furniture object
 * @since 1.0
 * @author Azlan Amjad <a href="mailto:azlan.amjad1@ucalgary.ca">azlan.amjad1@ucalgary.ca</a>
 * @author Sajid Hafiz <a href="mailto:sajid.hafiz1@ucalgary.ca">sajid.hafiz1@ucalgary.ca</a>
 * @author Saud Agha <a href="mailto:saud.agha1@ucalgary.ca">saud.agha1@ucalgary.ca</a>
 * @author Rohan Amjad <a href="mailto:rohan.amjad@ucalgary.ca">rohan.amjad@ucalgary.ca</a>
 * @version 2.1
 */
public class Furniture {
    private String ID;
    private String type;
    private ArrayList<Part> parts = new ArrayList<Part>();
    private int price;
    private String manuID;

    /**
     * Class Furniture constructor, used by ReadFromDatabase to instantiate Furniture objects
     * in order to fill in ArrayList of Furniture of the same category and type
     * @param ID Furniture ID
     * @param type Furniture type
     * @param parts ArrayList of Furniture parts
     * @param price Furniture price
     * @param manuID Furniture manufacturer ID
     */
    public Furniture(String ID, String type, ArrayList<Part> parts, int price, String manuID) {
        // initialize data members
        this.ID = ID;
        this.type = type;
        this.parts = parts;
        this.price = price;
        this.manuID = manuID;
    }

    // getter methods
    /**
     * ID getter method
     * @return ID
     */
    public String getID(){
        return this.ID;
    }
    /**
     * type getter method
     * @return type
     */
    public String getType() {
        return type;
    }
    /**
     * parts getter method
     * @return ArrayList of parts
     */
    public ArrayList<Part> getParts() {
        return parts;
    }
    /**
     * price getter method
     * @return price
     */
    public int getPrice() {
        return this.price;
    }
    /**
     * manuID getter method
     * @return manuID
     */
    public String getManuID() {
        return manuID;
    }
}