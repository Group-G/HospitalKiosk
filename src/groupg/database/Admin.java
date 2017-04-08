package groupg.database;

/**
 * Created by Sammy on 4/8/2017.
 */
public class Admin {
    private String username;
    private String password;

    public Admin(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        //System.out.println("USERNAME : " + this.username);
        //username and password are storing a ton of extra spaces
        //the database is storing them based on testing by Sam unsure why 4/8
        return this.username;
    }

    //FOR TESTING PURPOSES ONLY
    public String getPassword() {
        return this.password;
    }
}
