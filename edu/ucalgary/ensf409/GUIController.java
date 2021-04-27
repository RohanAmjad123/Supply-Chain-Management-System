package edu.ucalgary.ensf409;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class GUIController: creates the Graphic User Interface
 * @since 1.0
 * @author Sajid Hafiz <a href="mailto:sajid.hafiz1@ucalgary.ca">sajid.hafiz1@ucalgary.ca</a>
 * @author Azlan Amjad <a href="mailto:azlan.amjad1@ucalgary.ca">azlan.amjad1@ucalgary.ca</a>
 * @author Rohan Amjad <a href="mailto:rohan.amjad@ucalgary.ca">rohan.amjad@ucalgary.ca</a>
 * @author Saud Agha <a href="mailto:saud.agha1@ucalgary.ca">saud.agha1@ucalgary.ca</a>
 * @version 1.2
 */
public class GUIController {
    private Inventory inv;
    private ReadFromDatabase furnitureTypeDB;
    private String category;
    private String type;
    private int quantity;
    private final JFrame FRM = new JFrame();

    //constructor
    /**
     * Class GUIController constructor, empty constructor
     */
    public GUIController(){
        this.furnitureTypeDB = new ReadFromDatabase();
    }

    /**
     * Creates main menu, composed of 3 buttons: Start, About, Exit
     */
    public void mainMenu(){
        JPanel mainMenu = new JPanel();

        JButton start = new JButton( new AbstractAction("Start") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                mainMenu.setVisible(false);
                menuForCategory();
            }
        });

        JButton about = new JButton( new AbstractAction("About") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                mainMenu.setVisible(false);
                about();
            }
        });

        JButton exit = new JButton( new AbstractAction("Exit") {
            @Override
            public void actionPerformed( ActionEvent e ){
                System.exit(0);
            }
        });

        mainMenu.add(start);
        mainMenu.add(about);
        mainMenu.add(exit);
        FRM.add(mainMenu);
        FRM.setTitle("Supply Chain Management");
        FRM.setVisible(true);
        FRM.setSize(500, 100);
        FRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /**
     * Creates menu for selecting category of furniture
     */
    public void menuForCategory(){
        JPanel menuCategory = new JPanel();
        FRM.add(menuCategory);

        JLabel label1 = new JLabel("Select Category of Furniture: ");
        menuCategory.add(label1);

        String [] choices = new String[furnitureTypeDB.getAllFurniture().size()+1];
        choices[0] = "Select Option";
        for(int i = 1; i < choices.length; i++){
            choices[i] = furnitureTypeDB.getAllFurniture().get(i-1);
        }
        final JComboBox<String> dropDown = new JComboBox<String>(choices);
        dropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> choice = (JComboBox<String>) e.getSource();
                category = (String) choice.getSelectedItem();

            }
        });

        JButton next = new JButton( new AbstractAction("Next") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                menuCategory.setVisible(false);
                menuForType();

            }
        });

        dropDown.setVisible(true);
        menuCategory.add(dropDown);
        menuCategory.add(next);

        FRM.setSize(500, 100);
        FRM.setResizable(false);
        FRM.setVisible(true);
        FRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /**
     * Creates menu for selecting type of furniture
     */
    public void menuForType() {
        FRM.setSize(500, 100);
        FRM.setResizable(false);
        FRM.setVisible(true);
        FRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menu = new JPanel();
        FRM.add(menu);
        JButton error_exit = new JButton( new AbstractAction("Quit") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                menu.setVisible(false);
                System.exit(1);

            }
        });

        if(category == null || category.equals("Select Option")){
            JLabel errorLabel = new JLabel("Invalid category selected, please quit and try again!");
            menu.add(errorLabel);
            menu.add(error_exit);
            throw new InvalidSelectionException("Invalid category selected!");
        }
        furnitureTypeDB.setAllType(category);
        JLabel label1 = new JLabel("Select Type of Furniture: ");

        menu.add(label1);
        String[] choices = new String[furnitureTypeDB.getAllType().size() + 1];
        choices[0] = "Select Option";
        for (int i = 1; i < choices.length; i++) {
            choices[i] = furnitureTypeDB.getAllType().get(i - 1);
        }

        final JComboBox<String> dropDown = new JComboBox<String>(choices);

        dropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> choice = (JComboBox<String>) e.getSource();
                type = (String) choice.getSelectedItem();

            }
        });

        JButton next = new JButton(new AbstractAction("Next") {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.setVisible(false);
                menuForQuantity();

            }
        });

        dropDown.setVisible(true);
        menu.add(dropDown);
        menu.add(next);
    }
    /**
     * Creates menu for quantity desired
     */
    public void menuForQuantity(){
        FRM.setSize(500, 100);
        FRM.setResizable(false);
        FRM.setVisible(true);
        FRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menu= new JPanel();

        FRM.add(menu);

        JButton error_exit = new JButton( new AbstractAction("Quit") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                menu.setVisible(false);
                System.exit(1);
            }
        });

        if(type == null || type.equals("Select Option")){
            JLabel errorLabel = new JLabel("Invalid type selected, please quit and try again!");
            menu.add(errorLabel);
            menu.add(error_exit);
            throw new InvalidSelectionException("Invalid type selected!");
        }

        JLabel label1 = new JLabel("Select Quantity of Furniture: ");
        menu.add(label1);

        SpinnerModel model = new SpinnerNumberModel(0, 0, 100, 1);
        JSpinner spinner = new JSpinner(model);

        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    spinner.commitEdit();
                } catch (java.text.ParseException exception) {
                    exception.printStackTrace();
                }
                quantity = (Integer) spinner.getValue();

            }
        });
        JButton next = new JButton( new AbstractAction("Next") {
            @Override
            public void actionPerformed( ActionEvent e ){
                menu.setVisible(false);
                printOutput();
            }
        });

        menu.add(spinner);
        menu.add(next);
    }

    /**
     * Prints the output based on the input(s) from previous menus
     */
    public void printOutput() {
        FRM.setSize(500, 300);
        FRM.setResizable(false);
        FRM.setVisible(true);
        FRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menu = new JPanel();
        FRM.add(menu);

        JButton error_exit = new JButton( new AbstractAction("Quit") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                menu.setVisible(false);
                System.exit(1);

            }
        });

        if(quantity == 0){
            JLabel errorLabel = new JLabel("Invalid quantity selected, please quit and try again!");
            menu.add(errorLabel);
            menu.add(error_exit);
            throw new InvalidSelectionException("Invalid quantity selected!");
        }

        furnitureTypeDB.initializeFurniture(category, type);
        inv = new Inventory(category.toLowerCase(), type.toLowerCase(), quantity, furnitureTypeDB);

        String input = type.toLowerCase() + " " + category.toLowerCase() + ", " + Integer.toString(quantity);

        String formattedOutput = inv.formattedOutput(input, inv.calculateCheapest());
        inv.writeFile(formattedOutput);

        JTextArea label = new JTextArea(formattedOutput);
        label.setSize(460, 300);
        label.setLineWrap(true);
        label.setOpaque(false);

        JButton exit = new JButton(new AbstractAction("Quit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menu.add(label);
        menu.add(exit);

        furnitureTypeDB.close();
        inv.getDb().close();
    }

    /**
     * About menu
     */
    public void about(){
        FRM.setSize(500, 125);
        FRM.setResizable(false);
        FRM.setVisible(true);
        FRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menu = new JPanel();

        JLabel label1 = new JLabel("Made by: Sajid Hafiz, Rohan Amjad, Azlan Amjad, Saud Agha");
        JLabel label2 = new JLabel("Instructions for use: Follow the menus and enter the data which is required");

        JButton goBack = new JButton( new AbstractAction("Go Back") {
            @Override
            public void actionPerformed( ActionEvent e ){
                menu.setVisible(false);
                mainMenu();
            }
        });

        JButton exit = new JButton( new AbstractAction("Quit") {
            @Override
            public void actionPerformed( ActionEvent e ){
                System.exit(0);
            }
        });

        menu.add(label1);
        menu.add(label2);
        menu.add(goBack);
        menu.add(exit);

        FRM.add(menu);
    }


    /**
     * Please refer to the README.txt for information on how to run the application.
     * @param args
     */
    public static void main (String [] args) throws InvalidSelectionException{
        GUIController gui = new GUIController();
        gui.mainMenu();
    }
}
