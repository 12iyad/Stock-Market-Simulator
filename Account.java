
import java.util.*;

public class Account {
    private double balance;
    private String name;
    private int tick;
    private boolean isNotification = false;
    private String notificationMessage;
    private ArrayList<Assets> ownedAssets;
    private String newNotification = "No dividends received";

    public Account(String newUsername, int newBalance) {
        name = newUsername;
        balance = newBalance;
        tick = 0;
        ownedAssets = new ArrayList<Assets>();
    }
    public double getBalance(){
        return Math.round(balance*100)/100.00;
    }
    
    public String getName(){
        return name;
    }
    
    public int getTick(){return tick;}
    
    public String getNotificationAlert(){
    if(isNotification){
        return "(New!) ";
    }
    else{
        return "";
    }
    
    }
    
    public void setNotification(String notif){newNotification = notif;}
    
    public String getNotificationMessage(){return newNotification;}
    
    public ArrayList<Assets> getOwnedAssets(){return ownedAssets;}
    
    public void addAsset(Assets asset){ownedAssets.add(asset);}
    
    public void removeAsset(Assets asset){ownedAssets.remove(asset);}
    
    public void setNotification(boolean notif){isNotification = notif;}
    
    public void setTick(int newTick){tick = newTick;}
    
    public void setBalance(double newBal){
        balance = newBal;
    }
    

    
    

    
}
