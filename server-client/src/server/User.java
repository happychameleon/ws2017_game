package server;

/**
 * Created by m on 3/20/17.
 */
public class User {
    public String name;
    public String ipAddress;

    public User(String name, String ipAddress){
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public void changeName(String name){
        this.name = name;
    }

    public String returnName(){
        return name;
    }

    public String returnAddress(){
        return ipAddress;
    }
}
