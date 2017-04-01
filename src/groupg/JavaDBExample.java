package groupg;

import java.sql.*;

/**
 * @author Wilson Wong
 * @author Samanthe Comeau
 * @author Saul Woolf
 * @author Alazar Genene
 * @since 2017-03-19
 */
public class JavaDBExample
{
    void connectDB()
    {
        System.out.println("-------Embedded Java DB Connection Testing --------");
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

        System.out.println("Java DB driver registered!");
        System.out.println("\nJava DB connection established!");
    }

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
                System.out.println("Could not drop location.\n");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE PERSONELLE");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop personelle.\n");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE BUILDING");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop building.\n");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE FLOOR");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop floor.\n");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE CONNECTIONS");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop connection.\n");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE ADMIN");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop admin.\n");
                //e.printStackTrace();
            }

            try {
                stmt.execute("DROP TABLE CATEGORY");
            }
            catch (SQLException e)
            {
                System.out.println("Could not drop category.\n");
                //e.printStackTrace();
            }
            //END DROP TABLES

            //CREATE TABLES
            stmt.execute("CREATE TABLE LOCATION (LOCATION_ID char(20) NOT NULL Primary Key, LOCATION_NAME varchar(20), LOCATION_CATEGORY varchar(20), FLOOR_NUM int default 1, X_COORD int default 0, Y_COORD int default 0, BUILDING_ID int default 0)");
            stmt.execute("CREATE TABLE PERSONELLE (PERSONELLE_ID int NOT NULL Primary Key, PERSONELLE_NAME varchar(20) default NULL, OFFICE_NUMBER char(20))");
            stmt.execute("CREATE TABLE BUILDING (BUILDING_ID char(20) NOT NULL Primary Key, BUILDING_NAME varchar(20), FLOOR_COUNT int)");
            stmt.execute("CREATE TABLE FLOOR(FLOOR_ID char(20) NOT NULL Primary Key, FLOOR_NUMBER int, BUILDING_ID char(20), FILENAME varchar(20))");
            stmt.execute("CREATE TABLE CONNECTIONS(LOCATION_ONE char(20), LOCATION_TWO char(20))");
            stmt.execute("CREATE TABLE ADMIN(ADMIN_ID char(20), ADMIN_UN char(20), ADMIN_PW char(20))");
            stmt.execute("CREATE TABLE CATEGORY(CATEGORY_NAME varchar(20))");
            //END CREATE TABLES

            System.out.println("Tables created!");
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
    }

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

    void insertTables(){
        try {
            // substitute your database name for myDB
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDatabase;create=true");
            Statement stmt = connection.createStatement();
            //INSERT INTO TABLES
                /*stmt.execute("INSERT INTO ROOM VALUES " +
                        "('A11', 1, 2, 2, 'WAITING_ROOM', 0), " +
                        "('A21', 1, 2, 3, 'OFFICE', 0) ");
                stmt.execute("INSERT INTO PERSONELLE VALUES " +
                        "(0123, 'Dr. Hunter Peterson', 'A21'), " +
                        "(0124, 'Nurse Bella', 'A21') ");
                stmt.execute("INSERT INTO BUILDING VALUES " +
                        "('B0', 'Residential Services', 2), " +
                        "('B1', 'Morgan Hall', 4) ");
                stmt.execute("INSERT INTO FLOOR VALUES " +
                        "(1, 'Residential Services'), " +
                        "(1, 'Morgan Hall'), " +
                        "(2, 'Morgan Hall'), " +
                        "(3, 'Morgan Hall'), " +
                        "(4, 'Morgan Hall') ");*/
            //END INSERT TABLES
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
    }

}
