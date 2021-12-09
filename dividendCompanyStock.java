public class dividendCompanyStock extends companyStock{
    private double dividendDate;
    private double dividendYield;

    public dividendCompanyStock(String newAN, double newBP, double newDividendDate, double newDividendYield) {
        super(newAN, newBP);
        dividendDate = newDividendDate;
        dividendYield = newDividendYield;
    }

    public boolean isDividendDate(int date){
        if((date%dividendDate)==0){return true;}
        else{return false;}
    }

    public double getDividendYield(){ return dividendYield; }

    public double getDividendDate() { return dividendDate; }
}