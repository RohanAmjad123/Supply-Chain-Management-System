ENSF 409 W21 Project: Supply Chain Management


What is this project about?
This is a class project for ENSF 409 (Principles of Software Engineering 409) at the University of Calgary. The goal of this project is to design an application to calculate the
cheapest combination of available inventory items that can be used to fill a specific order from an existing inventory database. If the order can be fulfilled, the application will
write to a file specifying what furnitures's you should order from the database and the total cost. It will also remove the furniture used to fill the order from the database.
In the event that the order cannot be fulfilled, the application will write to a file stating that the order cannot be fulfilled.


How do I run the application?
First create a MySQL user with Username: scm, and Password: ensf409. Grant all privileges to this user.
Then create an inventory schema in your MySQL database using the provided inventory.sql in source-files\inventory.sql.

On Windows from Command Line:
javac -cp .;source-files\mysql-connector-java-8.0.23.jar;. edu\ucalgary\ensf409\GUIController.java
java -cp .;source-files\mysql-connector-java-8.0.23.jar;. edu.ucalgary.ensf409.GUIController

On Mac from Command Line:
javac -cp .:source-files/mysql-connector-java-8.0.23.jar:. edu/ucalgary/ensf409/GUIController.java
java -cp .:source-files/mysql-connector-java-8.0.23.jar:. edu.ucalgary.ensf409.GUIController


How do I run the unit tests?

On Windows from Command Line:
javac -cp .;source-files\junit-4.13.2.jar;source-files\hamcrest-core-1.3.jar;source-files\mysql-connector-java-8.0.23 edu\ucalgary\ensf409\InventoryTest.java
java -cp .;source-files\junit-4.13.2.jar;source-files\hamcrest-core-1.3.jar;source-files\mysql-connector-java-8.0.23 org.junit.runner.JUnitCore edu.ucalgary.ensf409.InventoryTest

On Mac from Command Line:
javac -cp .:source-files/junit-4.13.2.jar:source-files/hamcrest-core-1.3.jar;sourcefiles\mysql-connector-java-8.0.23 edu/ucalgary/ensf409/InventoryTest.java
java -cp .:source-files/junit-4.13.2.jar:source-files/hamcrest-core-1.3.jar;sourcefiles\mysql-connector-java-8.0.23 org.junit.runner.JUnitCore edu.ucalgary.ensf409.InventoryTest


Requirements:
OpenJDK 11
MySQL (Hardcoded Username: scm, Password: ensf409)
MySQL Connector (JDBC) (Included)
JUnit 4.13.2 Framework (Included)


Team members:
Sajid Hafiz
Saud Agha
Rohan Amjad
Azlan Amjad







