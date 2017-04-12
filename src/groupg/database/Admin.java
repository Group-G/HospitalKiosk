package groupg.database;

import javafx.application.HostServices;

import java.math.BigInteger;

/**
 * Created by Sammy on 4/8/2017.
 */
public class Admin {
    private String username;
    private String password;
    private BigInteger hashed;


    public Admin(String username, String password){
        this.username = username;
        this.password = password;

        byte[] bytes = this.password.getBytes();
        BigInteger m = new BigInteger(bytes);
        this.hashed = m.modPow(HospitalData.key.publicKey, HospitalData.key.modulus);

        System.out.println(this.hashed);

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

    public Boolean login(BigInteger pw){
        if(this.hashed.equals(pw)){
            return true;
        }
        return false;
    }

    public String getSQL(){
        return "(\'" + this.username + "\', \'" + this.hashed + "\')";
    }



}


