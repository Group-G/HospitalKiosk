package groupg.database;

import groupg.Main;
import javafx.application.HostServices;

import java.math.BigInteger;
import static groupg.Main.h;

/**
 * Created by Sammy on 4/8/2017.
 */
public class Admin {
    private String type;
    private int typeNum;
    private String username;
    private String password;
    private BigInteger hashed;


    public Admin(String username, String password, int type){
        this.username = username;
        this.password = password;
        this.typeNum = type;
        if(type == 1){
            this.type = "Admin";
        }
        else{
            this.type = "User";
        }


        byte[] bytes = this.password.getBytes();
        BigInteger m = new BigInteger(bytes);
//        System.out.println(Main.h.key);
//        System.out.println(Main.h.key.publicKey + ", " +  Main.h.key.modulus);
        this.hashed = m.modPow(Main.h.key.publicKey, Main.h.key.modulus);

//        System.out.println(this.hashed);

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
        return "(\'" + this.username + "\', \'" + this.hashed + "\', " + this.typeNum + ")";
    }


    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.username;
    }
}


