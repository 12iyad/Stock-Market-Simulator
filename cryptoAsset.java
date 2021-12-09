
import java.util.Random;
public class cryptoAsset extends Assets{
    private final int transactionSize;
    private final double spread;

    public cryptoAsset(String newAN, double newBP, int tSize, double newSpread) {
        super(newAN, newBP);
        transactionSize = tSize;
        spread = newSpread;
    }

    @Override
    public double getSellPrice(){return buyPrice*spread; }

    public String getTransaction(){
        Random rand = new Random();
        String str = "";
        int num = rand.nextInt(2);
        if(num==0){
            return "USD " + rand.nextInt(transactionSize) + " Amount of " + this.getName() + " bought";
        }
        else if(num==1){
            return "USD " + rand.nextInt(transactionSize) + " Amount of " + this.getName() + " sold";
        }
        else{
            return "";
        }

    }

}