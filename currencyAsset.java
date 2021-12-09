public class currencyAsset extends Assets{
    private int roundTO;

    public currencyAsset(String newAN, double newBP, int newRoundTO) {
        super(newAN, newBP);
        roundTO = newRoundTO;
    }

    @Override
    public double getBuyPrice(){ return Math.round(buyPrice*roundTO)/(roundTO+0.00); }

    @Override
    public double getSellPrice(){return Math.round((buyPrice*0.9)*roundTO)/(roundTO+0.00);}

}