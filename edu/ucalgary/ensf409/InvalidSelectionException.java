package edu.ucalgary.ensf409;

/**
 * InvalidSelectionException.java Custom Exception Class: throws the exception whenever the program encounters an invalid input from the client
 * @since 1.0
 * @author Sajid Hafiz <a href="mailto:sajid.hafiz1@ucalgary.ca">sajid.hafiz1@ucalgary.ca</a>
 * @author Azlan Amjad <a href="mailto:azlan.amjad1@ucalgary.ca">azlan.amjad1@ucalgary.ca</a>
 * @author Rohan Amjad <a href="mailto:rohan.amjad@ucalgary.ca">rohan.amjad@ucalgary.ca</a>
 * @author Saud Agha <a href="mailto:saud.agha1@ucalgary.ca">saud.agha1@ucalgary.ca</a>
 * @version 1.0
 */
public class InvalidSelectionException extends RuntimeException {
    public InvalidSelectionException(String message) {
        super(message);
    }
}
