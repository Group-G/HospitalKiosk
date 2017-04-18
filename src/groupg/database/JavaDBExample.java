package groupg.database;

import java.sql.*;

/**
 * Created by  Alazar Genene, Saul Woolf, and Samantha Comeau on 4/1/17.
 */
public class JavaDBExample
{
    /**
     * Connects to the DB
     */
    public void connectDB() {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Java DB Driver not found. Add the classpath to your module.");
            System.out.println("For IntelliJ do the following:");
            System.out.println("File | Project Structure, Modules, Dependency tab");
            System.out.println("Add by clicking on the green plus icon on the right of the window");
            System.out.println("Select JARs or directories. Go to the folder where the Java JDK is installed");
            System.out.println("Select the folder java/jdk1.8.xxx/db/lib where xxx is the version.");
            System.out.println("Click OK, compile the code and run it.");
            e.printStackTrace();
            return;
        }

    }

    /**
     *  createTables
            @params none
            @return void
            @functionality attepts to connect to HospitalDatabase and drops/creates tables
                on fail it will return
     */
    public void createTables(){
        try
        {
            // substitute your database name for myDB
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDatabase;create=true");
            Statement stmt = connection.createStatement();

            //DROP TABLE

            try {
                stmt.execute("DROP TABLE LOCATION");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop location.");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE PERSONELLE");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop personelle.");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE BUILDING");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop building.");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE FLOOR");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop floor.");
                //e.printStackTrace();
            }



            try {
                stmt.execute("DROP TABLE ADMINS");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop admin.");
                //e.printStackTrace();
            }
            try {
                stmt.execute("DROP TABLE CONNECTIONS");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop people connections.");
                //e.printStackTrace();
            }
            try {
                stmt.execute("DROP TABLE PEOPLELOCATIONS");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop people locations.");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE CATEGORY");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop category.");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE TRACKID");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop track ids.");
                //e.printStackTrace();
            }
            //END DROP TABLES

            //CREATE TABLES

            stmt.execute("CREATE TABLE LOCATION (LOCATION_ID int NOT NULL Primary Key, LOCATION_NAME varchar(40), LOCATION_CATEGORY varchar(40), FLOOR_ID int, X_COORD int default 0, Y_COORD int default 0, BUILDING_ID int)");
            stmt.execute("CREATE TABLE PERSONELLE (PERSONELLE_ID int NOT NULL Primary Key, TITLE varchar(40) default NULL, PERSONELLE_NAME varchar(40) default NULL)");
            stmt.execute("CREATE TABLE BUILDING (BUILDING_ID int NOT NULL Primary Key, BUILDING_NAME varchar(40), FLOOR_COUNT int)");
            stmt.execute("CREATE TABLE FLOOR (FLOOR_ID int NOT NULL, FLOOR_NUMBER char(20), BUILDING_ID int, FILENAME varchar(40))");

            stmt.execute("CREATE TABLE CONNECTIONS(LOCATION_ONE int, LOCATION_TWO int)");
            stmt.execute("CREATE TABLE PEOPLELOCATIONS(PERSON_ID int, OFFICE_ID int)");
            stmt.execute("CREATE TABLE ADMINS(ADMIN_UN varchar(40) NOT NULL Primary Key, ADMIN_PW varchar(40))");
            stmt.execute("CREATE TABLE CATEGORY(CATEGORY_NAME varchar(40), PERMISSIONS INT, COLOR varchar(8))");

            stmt.execute("CREATE TABLE TRACKID(LOCATION_ID int, PERSONELLE_ID int, BUILDING_ID int, FLOOR_ID int)");

            //END CREATE TABLES

//            System.out.println("Tables created!");
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
    }

    public void restoreBackup(){
        backupStuff(true);
    }
    public void backupTables(){
        backupStuff(false);
    }

    public void backupStuff(boolean restore){
        String m1 = "", m2 = "";
        if(restore){
            m1 ="2";
        }
        else{
            m2 = "2";
        }
        try
        {
            // substitute your database name for myDB
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDatabase;create=true");
            Statement stmt = connection.createStatement();

            //DROP TABLE

            try {stmt.execute("DROP TABLE LOCATION" + m2);}
            catch (SQLException e){}

            try {stmt.execute("DROP TABLE PERSONELLE" + m2);}
            catch (SQLException e) {}

            try {stmt.execute("DROP TABLE BUILDING" + m2);}
            catch (SQLException e) {}

            try {stmt.execute("DROP TABLE FLOOR" + m2);}
            catch (SQLException e) {}

            try {stmt.execute("DROP TABLE ADMINS" + m2);}
            catch (SQLException e) {}

            try {stmt.execute("DROP TABLE CONNECTIONS" + m2);}
            catch (SQLException e) {}

            try {stmt.execute("DROP TABLE PEOPLELOCATIONS" + m2);}
            catch (SQLException e) {}

            try {stmt.execute("DROP TABLE CATEGORY" + m2);}
            catch (SQLException e) {}

            try {stmt.execute("DROP TABLE TRACKID" + m2);}
            catch (SQLException e) {}
            //END DROP TABLES

            //CREATE TABLES

            stmt.execute("CREATE TABLE LOCATION" + m2+ " (LOCATION_ID int NOT NULL Primary Key, LOCATION_NAME varchar(40), LOCATION_CATEGORY varchar(40), FLOOR_ID int, X_COORD int default 0, Y_COORD int default 0, BUILDING_ID int)");
            stmt.execute("INSERT INTO LOCATION" + m2+ "  SELECT * FROM LOCATION" + m1);

            stmt.execute("CREATE TABLE PERSONELLE" + m2+ "  (PERSONELLE_ID int NOT NULL Primary Key, TITLE varchar(40) default NULL, PERSONELLE_NAME varchar(40) default NULL)");
            stmt.execute("INSERT INTO PERSONELLE" + m2+ "  SELECT * FROM PERSONELLE" + m1);

            stmt.execute("CREATE TABLE BUILDING" + m2+ "  (BUILDING_ID int NOT NULL Primary Key, BUILDING_NAME varchar(40), FLOOR_COUNT int)");
            stmt.execute("INSERT INTO BUILDING" + m2+ "  SELECT * FROM BUILDING" + m1);

            stmt.execute("CREATE TABLE FLOOR" + m2+ "  (FLOOR_ID int NOT NULL, FLOOR_NUMBER char(20), BUILDING_ID int, FILENAME varchar(40))");
            stmt.execute("INSERT INTO FLOOR" + m2+ "  SELECT * FROM FLOOR" + m1);

            stmt.execute("CREATE TABLE CONNECTIONS" + m2+ " (LOCATION_ONE int, LOCATION_TWO int)");
            stmt.execute("INSERT INTO CONNECTIONS" + m2+ "  SELECT * FROM CONNECTIONS" + m1);

            stmt.execute("CREATE TABLE PEOPLELOCATIONS" + m2+ " (PERSON_ID int, OFFICE_ID int)");
            stmt.execute("INSERT INTO PEOPLELOCATIONS" + m2+ "  SELECT * FROM PEOPLELOCATIONS" + m1);

            stmt.execute("CREATE TABLE ADMINS" + m2+ " (ADMIN_UN varchar(40) NOT NULL Primary Key, ADMIN_PW varchar(40))");
            stmt.execute("INSERT INTO ADMINS" + m2+ "  SELECT * FROM ADMINS" + m1);

            stmt.execute("CREATE TABLE CATEGORY" + m2+ " (CATEGORY_NAME varchar(40), PERMISSIONS INT, COLOR varchar(8))");
            stmt.execute("INSERT INTO CATEGORY" + m2+ "  SELECT * FROM CATEGORY" + m1);

            stmt.execute("CREATE TABLE TRACKID" + m2+ " (LOCATION_ID int, PERSONELLE_ID int, BUILDING_ID int, FLOOR_ID int)");
            stmt.execute("INSERT INTO TRACKID" + m2+ "  SELECT * FROM TRACKID" + m1);

            //END CREATE TABLES

//            System.out.println("Tables created!");
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
    }

    /**
     *  insertTables
        @params none
        @return void
        @functionality inserts dummy values into HospitalDatabase
     */
    public void insertTables(){
        try {
            // substitute your database name for myDB
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDatabase;create=true");
            Statement stmt = connection.createStatement();

            //INSERT LOCATION
            //FORMAT

            //(LOCATION_ID int NOT NULL Primary Key, LOCATION_NAME varchar(20), LOCATION_CATEGORY varchar(20), FLOOR_ID int, X_COORD int default 0, Y_COORD int default 0, BUILDING_ID int)
//            stmt.execute("INSERT INTO LOCATION VALUES " +
//                    "(0, 'A111', 'Office', 4, 920, 127, 0), " +
//                    "(1, 'A112', 'Office', 4, 840, 124, 0), " +
//                    "(2, 'A113', 'Office', 4, 856, 396, 0) ");


            //INSERT PERSONELLE
            //FORMAT
            //(PERSONELLE_ID int NOT NULL Primary Key, TITLE varchar(20) default NULL, PERSONELLE_NAME varchar(20) default NULL)
//            stmt.execute("INSERT INTO PERSONELLE VALUES " +
//
//                    "(0, 'Dr.', 'Hunter Peterson'), " +
//                    "(1, 'Supreme Being', 'Jeff'), " +
//                    "(2, 'Nurse', 'Bella Bee') ");

//            //INSERT PEOPLELOCATIONS
//            //FORMAT
//            //(int PERSON_ID int OFFICE_ID)
//            stmt.execute("INSERT INTO PEOPLELOCATIONS VALUES " +
//
//                    "(0, 0), " +
//                    "(0, 1) ");

            //INSERT BUILDING
            //FORMAT
            //(BUILDING_ID int NOT NULL Primary Key, BUILDING_NAME varchar(20), FLOOR_COUNT int)
            stmt.execute("INSERT INTO BUILDING VALUES " +

                    "(0, 'Belkin House', 4), " +
                    "(1, 'Faulkner Hospital', 7) ");


            //INSERT FLOOR
            //FORMAT
            //(FLOOR_ID int NOT NULL Primary Key, FLOOR_NUMBER int, BUILDING_ID int, FILENAME varchar(20))
            stmt.execute("INSERT INTO FLOOR VALUES " +


                    "(0, 'Faulker Floor 1', 1, '/image/faulkner_1_cropped.png'), " +
                    "(1, 'Faulker Floor 2', 1, '/image/faulkner_2_cropped.png'), " +
                    "(2, 'Faulker Floor 3', 1, '/image/faulkner_3_cropped.png'), " +
                    "(3, 'Faulker Floor 4', 1, '/image/faulkner_4_cropped.png'), " +
                    "(4, 'Faulker Floor 5', 1, '/image/faulkner_5_cropped.png'), " +
                    "(5, 'Faulker Floor 6', 1, '/image/faulkner_6_cropped.png'), " +
                    "(6, 'Faulker Floor 7', 1, '/image/faulkner_7_cropped.png'), " +
                    "(7, 'Belkin Floor 1', 0, '/image/belkin_1_cropped.png'), " +
                    "(8, 'Belkin Floor 2', 0, '/image/belkin_2_cropped.png'), " +
                    "(9, 'Belkin Floor 3', 0, '/image/belkin_3_cropped.png'), " +
                    "(10, 'Belkin Floor 4', 0, '/image/belkin_4_cropped.png') " );



            //INSERT CONNECTIONS
            //FORMAT
            //(LOCATION_ONE int, LOCATION_TWO int)
//            stmt.execute("INSERT INTO CONNECTIONS VALUES " +
//                    "(2001, 2002)");


            //INSERT ADMIN
            //FORMAT
            //(ADMIN_ID int, ADMIN_UN char(20), ADMIN_PW char(20))
//            stmt.execute("INSERT INTO ADMINS VALUES " +
//                    "('admin', 'guest'), " +
//                    "('sjcomeau', 'sjc')");

            //INSERT CATEGORY
            //FORMAT
            //(CATEGORY_NAME varchar(20), PERMISSIONS INT)
            stmt.execute("INSERT INTO CATEGORY VALUES " +
                    "('Office', 0, '0xffffff'), " +
                    "('Bathroom', 0, '0xffdfff'), " +
                    "('Hall', 0, '0xfffaff'), " +
                    "('Elevator', 0, '0xf9ffff'), " +
                    "('Stairs', 0, '0xffff01'), " +
                    "('Waiting Area', 0, '0xf00fff'), " +
                    "('Break Room', 1, '0xfff00f'), " +
                    "('Kiosk', 0, '0xf99fff'), " +
                    "('Emergency Room', 0, '0x99ffff') ");

            //INSERT TRACKIDS
            //FORMAT
            //(LOCATION_ID int, PERSONELLE_ID int, BUILDING_ID int, FLOOR_ID int)
//            stmt.execute("INSERT INTO TRACKID VALUES " +
//                    "(3, 3, 2, 11) ");


            //Print
//            System.out.println("Tables inserted!");
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
    }


    /**
     * Fills database based on live data
     * @param location String contain SQL of location
     * @param personelle String contain SQL of personelle
     * @param offices String contain SQL of offices
     * @param floor String contain SQL of floor
     * @param building String contain SQL of building
     * @param connections String contain SQL of connections
     * @param admin String contain SQL of admin
     * @param category String contain SQL of category
     */
    public void fillTable(String location, String personelle, String offices, String floor, String building, String connections, String admin, String category, String trackIDS){
        backupTables();
        createTables();
        try {

            // substitute your database name for myDB
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDatabase;create=true");
            Statement stmt = connection.createStatement();

            //INSERT LOCATION
            //FORMAT

            //(LOCATION_ID int NOT NULL Primary Key, LOCATION_NAME varchar(20), LOCATION_CATEGORY varchar(20), FLOOR_ID int, X_COORD int default 0, Y_COORD int default 0, BUILDING_ID int)
            if(!location.equals("")) {
                stmt.execute("INSERT INTO LOCATION VALUES " + location);
            }


            //INSERT PERSONELLE
            //FORMAT
            //(PERSONELLE_ID int NOT NULL Primary Key, PERSONELLE_NAME varchar(20) default NULL, OFFICE_NUMBER int)
            if(!personelle.equals("")) {
                stmt.execute("INSERT INTO PERSONELLE VALUES " + personelle);
            }

            //INSERT PEOPLELOCATIONS
            //FORMAT
            //(int personId int officeID)
            if(!floor.equals("")) {
                stmt.execute("INSERT INTO FLOOR VALUES " + floor);
            }

            //INSERT PEOPLELOCATIONS
            //FORMAT
            //(int personId int officeID)
            if(!offices.equals("")) {
                stmt.execute("INSERT INTO PEOPLELOCATIONS VALUES " + offices);
            }

            //INSERT BUILDING
            //FORMAT
            //(BUILDING_ID int NOT NULL Primary Key, BUILDING_NAME varchar(20), FLOOR_COUNT int)
            if(!building.equals("")) {
                stmt.execute("INSERT INTO BUILDING VALUES " + building);
            }


            //INSERT FLOOR
            //FORMAT
            //(FLOOR_ID int NOT NULL Primary Key, FLOOR_NUMBER int, BUILDING_ID int, FILENAME varchar(20))
            if(!floor.equals("")) {
                stmt.execute("INSERT INTO FLOOR VALUES " + floor);
            }



            //INSERT CONNECTIONS
            //FORMAT
            //(LOCATION_ONE int, LOCATION_TWO int)
            if(!connections.equals("")) {
                stmt.execute("INSERT INTO CONNECTIONS VALUES " + connections);
            }


            //INSERT ADMIN
            //FORMAT
            //(ADMIN_ID int, ADMIN_UN char(20), ADMIN_PW char(20))
            if(!admin.equals("")) {
                stmt.execute("INSERT INTO ADMINS VALUES " + admin);
            }

            //INSERT CATEGORY
            //FORMAT
            //(CATEGORY_NAME varchar(20))
            if(!category.equals("")) {
                stmt.execute("INSERT INTO CATEGORY VALUES " + category);
            }

            //INSERT TRACKIDS
            //FORMAT
            //(LOCATION_ID int, PERSONELLE_ID int, BUILDING_ID int, FLOOR_ID int)
            if(!category.equals("")) {
                stmt.execute("INSERT INTO TRACKID VALUES " + trackIDS);
            }


            //Print
//            System.out.println("Tables inserted!");
        }
        catch (SQLException e)
        {
            System.out.println("WRITING TO DATABASE FAILED");
            e.printStackTrace();
            System.out.println("RESTORING FROM BACKUP");
            restoreBackup();
            System.out.println("BACKUP RESTORATION COMPLETE");
        }
    }



}
