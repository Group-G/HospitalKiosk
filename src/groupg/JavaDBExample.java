package groupg;

import java.sql.*;

/**
 * @author Wilson Wong
 * @author Samanthe Comeau
 * @author Saul Woolf
 * @author Alazar Genene
 * @since 2017-04-01
 */
public class JavaDBExample
{
    /* connectDB
        @params none
        @return void
        @functionality attempts to connect to HospitalDatabase, on fail it will print how to
            connect and return
     */
    void connectDB()
    {
//        System.out.println("-------Embedded Java DB Connection Testing --------");
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

//        System.out.println("Java DB driver registered!");
//        System.out.println("\nJava DB connection established!");
    }

    /* createTables
            @params none
            @return void
            @functionality attepts to connect to HospitalDatabase and drops/creates tables
                on fail it will return
         */
    void createTables(){
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
                stmt.execute("DROP TABLE ADMIN");
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
            //END DROP TABLES

            //CREATE TABLES

            stmt.execute("CREATE TABLE LOCATION (LOCATION_ID int NOT NULL Primary Key, LOCATION_NAME varchar(20), LOCATION_CATEGORY varchar(20), FLOOR_ID int, X_COORD int default 0, Y_COORD int default 0, BUILDING_ID int)");
            stmt.execute("CREATE TABLE PERSONELLE (PERSONELLE_ID int NOT NULL Primary Key, TITLE varchar(20) default NULL, PERSONELLE_NAME varchar(20) default NULL)");
            stmt.execute("CREATE TABLE BUILDING (BUILDING_ID int NOT NULL Primary Key, BUILDING_NAME varchar(20), FLOOR_COUNT int)");
            stmt.execute("CREATE TABLE FLOOR(FLOOR_ID int NOT NULL, FLOOR_NUMBER char(20), BUILDING_ID int, FILENAME varchar(20))");

            stmt.execute("CREATE TABLE CONNECTIONS(LOCATION_ONE int, LOCATION_TWO int)");
            stmt.execute("CREATE TABLE PEOPLELOCATIONS(PERSON_ID int, OFFICE_ID int)");
            stmt.execute("CREATE TABLE ADMIN(ADMIN_UN char(20) NOT NULL Primary Key, ADMIN_PW char(20))");
            stmt.execute("CREATE TABLE CATEGORY(CATEGORY_NAME varchar(20))");
            //END CREATE TABLES

//            System.out.println("Tables created!");
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
    }

    /* insertTables
        @params none
        @return void
        @functionality inserts dummy values into HospitalDatabase
     */
    void insertTables(){
        try {
            // substitute your database name for myDB
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDatabase;create=true");
            Statement stmt = connection.createStatement();

            //INSERT LOCATION
            //FORMAT

            //(LOCATION_ID int NOT NULL Primary Key, LOCATION_NAME varchar(20), LOCATION_CATEGORY varchar(20), FLOOR_ID int, X_COORD int default 0, Y_COORD int default 0, BUILDING_ID int)
            stmt.execute("INSERT INTO LOCATION VALUES " +
                    "(2001, 'A111', 'WAITING_ROOM', 1001, 1, 1, 101), " +
                    "(2002, 'A112', 'OFFICE', 1002, 1, 3, 102), " +
                    "(2003, 'A113', 'OFFICE', 1002, 5, 4, 102) ");


            //INSERT PERSONELLE
            //FORMAT
            //(PERSONELLE_ID int NOT NULL Primary Key, PERSONELLE_NAME varchar(20) default NULL, OFFICE_NUMBER int)
            stmt.execute("INSERT INTO PERSONELLE VALUES " +

                    "(3001, 'Dr.', 'Hunter Peterson'), " +
                    "(3002, 'Supreme Being', 'Jeff'), " +
                    "(3003, 'Nurse', 'Bella Bee') ");

            //INSERT PEOPLELOCATIONS
            //FORMAT
            //(int personId int officeID)
            stmt.execute("INSERT INTO PEOPLELOCATIONS VALUES " +

                    "(3001, 2002), " +
                    "(3002, 2003) ");

            //INSERT BUILDING
            //FORMAT
            //(BUILDING_ID int NOT NULL Primary Key, BUILDING_NAME varchar(20), FLOOR_COUNT int)
            stmt.execute("INSERT INTO BUILDING VALUES " +

                    "(101, 'Residential Services', 1), " +
                    "(102, 'Morgan Hall', 4) ");


            //INSERT FLOOR
            //FORMAT
            //(FLOOR_ID int NOT NULL Primary Key, FLOOR_NUMBER int, BUILDING_ID int, FILENAME varchar(20))
            stmt.execute("INSERT INTO FLOOR VALUES " +

                    "(1001, '1', 101, 'B0_F1_img'), " +
                    "(1002, '1', 102, 'B1_F1_img'), " +
                    "(1003, '2', 102, 'B1_F2_img'), " +
                    "(1004, '3', 102, 'B1_F3_img'), " +
                    "(1005, '4', 102, 'B1_F4_img') ");



            //INSERT CONNECTIONS
            //FORMAT
            //(LOCATION_ONE int, LOCATION_TWO int)
            stmt.execute("INSERT INTO CONNECTIONS VALUES " +
                    "(2001, 2002)");


            //INSERT ADMIN
            //FORMAT
            //(ADMIN_ID int, ADMIN_UN char(20), ADMIN_PW char(20))
            stmt.execute("INSERT INTO ADMIN VALUES " +
                    "('admin', 'admin'), " +
                    "('sjcomeau', 'sjc') ");

            //INSERT CATEGORY
            //FORMAT
            //(CATEGORY_NAME varchar(20))
            stmt.execute("INSERT INTO CATEGORY VALUES " +
                    "('Office'), " +
                    "('Bathroom'), " +
                    "('Hall'), " +
                    "('Waiting Area'), " +
                    "('Emergency Room') ");

            //Print
//            System.out.println("Tables inserted!");
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
    }



    /* selectTables
        @params none
        @return void
        @functionality attempts to connect to HospitalDatabase and select values (unimportant rn)
     */
    void selectTables(){
        try{
            // substitute your database name for myDB
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDatabase;create=true");
            Statement stmt = connection.createStatement();

            /*
            //New Line
            System.out.println("\n");

            //Description
            System.out.println("Selecting everything from personelle.\n");

            //Select everything from PERSONELLE
            ResultSet personelle = stmt.executeQuery("SELECT * FROM PERSONELLE");
            ResultSetMetaData personelleDataset = personelle.getMetaData();
            int personelleColumns = personelleDataset.getColumnCount();
            for (int i = 1; i <= personelleColumns; i++){
                System.out.format("%20s", personelleDataset.getColumnName(i) +'|');
            }
            System.out.println(personelle);
            while (personelle.next()) {
                System.out.println(" ");
                for (int j = 1; j <= personelleColumns; j++) {
                    System.out.format("%20s", personelle.getString(j) + '|');
                }
            }

            //New Line
            System.out.println("\n");

            //Description
            System.out.println("Selecting the room ID's that are on floor 1.\n");

            //Select room ID's that are on floor 1 from ROOM
            ResultSet room = stmt.executeQuery("SELECT ROOM_ID FROM ROOM WHERE FLOOR_NUM=1");
            ResultSetMetaData roomDataset = room.getMetaData();
            int roomColumns = roomDataset.getColumnCount();
            for (int i = 1; i <= roomColumns; i++){
                System.out.format("%20s", roomDataset.getColumnName(i) +'|');
            }
            System.out.println(room);
            while (room.next()) {
                System.out.println(" ");
                for (int j = 1; j <= roomColumns; j++) {
                    System.out.format("%20s", room.getString(j) + '|');
                }
            }
            */
        }
            catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
    }



    void fillTable(String location, String personelle, String offices, String floor, String building, String connections, String admin,  String category){
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
                stmt.execute("INSERT INTO ADMIN VALUES " + admin);
            }

            //INSERT CATEGORY
            //FORMAT
            //(CATEGORY_NAME varchar(20))
            if(!category.equals("")) {
                stmt.execute("INSERT INTO CATEGORY VALUES " + category);
            }

            //Print
//            System.out.println("Tables inserted!");
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
    }



}
