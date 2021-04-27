package edu.ucalgary.ensf409;

/**
 * Class Part: definition for a Part object
 * @since 1.0
 * @author Azlan Amjad <a href="mailto:azlan.amjad1@ucalgary.ca">azlan.amjad1@ucalgary.ca</a>
 * @author Sajid Hafiz <a href="mailto:sajid.hafiz1@ucalgary.ca">sajid.hafiz1@ucalgary.ca</a>
 * @author Saud Agha <a href="mailto:saud.agha1@ucalgary.ca">saud.agha1@ucalgary.ca</a>
 * @author Rohan Amjad <a href="mailto:rohan.amjad@ucalgary.ca">rohan.amjad@ucalgary.ca</a>
 * @version 1.5
 */
public class Part {
    private String partName;
    private String usable;

    /**
     * Class Part constructor, used by ReadFromDatabase to instantiate Part objects
     * in order to fill an ArrayList of parts, which is later passed to the constructor of Furniture
     * @param partName Part name
     * @param usable Part usability status (Y/N)
     */
    public Part(String partName, String usable) {
        // initialize data members
        this.partName = partName;
        this.usable = usable;
    }

    // getter methods
    /**
     * partName getter method
     * @return partName
     */
    public String getPartName() {
        return this.partName;
    }
    /**
     * usable getter method
     * @return usable
     */
    public String getUsable() {
        return this.usable;
    }
}

