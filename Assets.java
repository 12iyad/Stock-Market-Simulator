public class Assets {

    protected double buyPrice;
    protected double sellPrice;
    protected String assetName;
    private double originalPrice;
    private int quantity;
    private double priceChange;

    //Asset initialise constructor for each asset
    public Assets(String newAN, double newBP){
        buyPrice = newBP;
        assetName = newAN;
        originalPrice = newBP;
    }

    //Asset constructor when user buys
    public Assets(String newAN, double newBP, int newQuantity){
        buyPrice = newBP;
        assetName = newAN;
        originalPrice = newBP;
        quantity = newQuantity;
    }

    //Getter and setter methods
    public double getBuyPrice(){ return Math.round(buyPrice*100)/100.00; }

    public double getSellPrice(){ return Math.round((buyPrice*0.90)*100)/100.00; }
    
    public double getPriceChange(){return 1+(Math.random()*(0.10+0.10)-0.10);}

    public String getName(){ return assetName; }
    
    public void setBuyPrice(double newBP){ buyPrice = newBP; }
    
    public int getQuantity(){return quantity;}
    
    public void setQuantity(int newQuantity){quantity = newQuantity;}
    
    


    //Work out change for asset and display visual change using up and down arrow
    public String getChange(){
        String change="";
        double changeInt = Math.round((((buyPrice-originalPrice)/originalPrice)*100)*100)/100.00;
        if(changeInt<0){
            change = "("+changeInt+")" + "%" + "  (bearish)";
        }
        else if(changeInt>=0){
            change = "("+changeInt+")" + "%" + "  (bullish)";
        }
        return change;
    }


}