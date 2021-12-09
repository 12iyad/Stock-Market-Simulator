
import java.util.*;
import java.io.IOException;

public class StockMarketSimulator
{
    public static final int delay = 1500;

    public static void main(String[] args){
        cls();
        assetsData(createAccount());
    }
    
   public static void cls(){
        try{
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        catch (Exception e){
            System.out.println(e);
        }
   }

    public static void stockMarketSimulator(Account acc, Assets[][] polyAssets, dividendCompanyStock[] dividendCompanyStocks, cryptoAsset[] cryptoAssets) {
        System.out.println(displayDetails(acc));
        System.out.println(" - Portfolio (1)\n" + 
                           " - Assets (2)\n" + 
                           " - Notifications " + acc.getNotificationAlert() + "(3)\n");
    }
    
    
    public static void mainTimer(Account acc, Assets[][] polyAssets, dividendCompanyStock[] dividendCompanyStocks, cryptoAsset[] cryptoAssets){
        Random random = new Random();
        acc.setTick(acc.getTick()-1);
        TimerTask timer = new TimerTask(){
        @Override
        public void run(){
            if(isDividendDate(acc, dividendCompanyStocks)){
                acc.setNotification(true);
                acc.setNotification(acc.getNotificationMessage()+"\n"+dividendMessage(acc, dividendCompanyStocks));
                acc.setBalance(acc.getBalance()+dividendPayout(acc, dividendCompanyStocks));
            }
            int assetIndex = random.nextInt(6);
            if(assetIndex==0||assetIndex==1||assetIndex==2||assetIndex==3){int index = random.nextInt(polyAssets[assetIndex].length); polyAssets[assetIndex][index] = priceRecalculatePoly(polyAssets[assetIndex][index]);}
            else if(assetIndex==4){int index = random.nextInt(dividendCompanyStocks.length); dividendCompanyStocks[index] = priceRecalculateDividend(dividendCompanyStocks[index]);}
            else if(assetIndex==5){int index = random.nextInt(cryptoAssets.length); cryptoAssets[index] = priceRecalculateCrypto(cryptoAssets[index]);}
            cls();
            acc.setTick(acc.getTick()+1);
            stockMarketSimulator(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
        }
                
    };
        Timer update = new Timer();
        update.schedule(timer, 0, delay);
        
        int choice = inputInt("");
        
        if(choice==1){
            timer.cancel();
            portfolioTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);}
            
        else if(choice==2){
            timer.cancel();
            assetsAvailableTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);}  
            
        else if(choice==3){
            acc.setNotification(false);
            timer.cancel();
            notificationTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);}   
        else{
            timer.cancel();
            cls();
            System.out.println("Please choose from the correct options");
            mainTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
        }
        
    }
   
    public static void  assetsData(Account acc){
        String[] currencyPairs = {"USD:EUR", "EUR:GBP", "GBP:NZD", "GBP:CAD"};
        double[] currencyPrice = {0.8344,0.8575,1.9401,1.7547};

        String[] cryptoName = {"BTC", "ETH", "LTC", "XRP"};
        double[] cryptoPrice = {42981.32,1248.18,161.61,0.33};
        int[] transactionSize = {100000000, 1000000, 10000, 1000};

        String[][] companyName = {{"Microsoft (MSFT)", "Apple (AAPL)", "Tesla (TSLA)", "GameStop (GME)"}, /*Dividend paying stocks*/{"BP PLC (BP)", "AT&T (T)", "IBM (IBM)", "Exxon Mobil (XOM)"}};
        double[][] companyPrice = {{235.75,121.03,693.73,5645.33},{3.23, 25.82, 123.60, 58.94}};
        double[][] dividendYieldANDDate = {{0.0566, 0.0698, 0.051, 0.0562}, {10, 16, 9, 14}};

        String[] etfName = {"FTSE 100", "S&P 500", "Global Clean Energy", "FTSE Japan"};
        String[] etfCountry = {"UK", "USA", "Global", "Japan"};
        double[] etfPrice = {30.06, 53.68, 51.66, 22.18};

        String[] commodityName = {"Physical Gold", "Crude Oil", "Physical Silver", "Physical Platinum"};
        double [] commodityPrice = {24.06, 5.13, 17.27, 93.62};

        Assets[][] polyAssets =
                {initCompanyStock(companyName[0], companyPrice[0]),
                initCommodityAssets(commodityName, commodityPrice),
                initCurrencyAssets(currencyPairs, currencyPrice),
                initETFAssets(etfName, etfPrice, etfCountry)};

        dividendCompanyStock[] dividendCompanyStocks = initDividendCompanyAssets(companyName[1], companyPrice[1], dividendYieldANDDate[0], dividendYieldANDDate[1]);
        cryptoAsset[] cryptoAssets = initCryptoAssets(cryptoName, cryptoPrice, transactionSize);
        etfAsset[] etfAssets = initETFAssets(etfName, etfPrice, etfCountry);
        
        mainTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets); 
    }

    public static currencyAsset[] initCurrencyAssets(String[] currencyName, double[] currencyPrice){
        currencyAsset[] assets = new currencyAsset[currencyName.length];
        for(int i = 0; i < assets.length; i++){
            assets[i] = new currencyAsset(currencyName[i], currencyPrice[i], 10000);
        }
        return assets; }


    public static cryptoAsset[] initCryptoAssets(String[] cryptoName, double[] cryptoPrice, int[] transactionSize){
        cryptoAsset[] assets = new cryptoAsset[cryptoName.length];
        for(int i = 0; i < assets.length; i++){
            assets[i] = new cryptoAsset(cryptoName[i], cryptoPrice[i], transactionSize[i], 0.80);
        }
        return assets; }


    public static companyStock[] initCompanyStock(String[] companyName, double[] companyPrice){
        companyStock[] assets = new companyStock[companyName.length];
        for(int i = 0; i < assets.length; i++){
            assets[i] = new companyStock(companyName[i], companyPrice[i]);
        }
        return assets; }


    public static dividendCompanyStock[] initDividendCompanyAssets(String[] companyName, double[] companyPrice, double[] dividendYield, double[] dividendDate){
        dividendCompanyStock[] assets = new dividendCompanyStock[companyName.length];
        for(int i = 0; i < assets.length; i++){
            assets[i] = new dividendCompanyStock(companyName[i], companyPrice[i], dividendDate[i], dividendYield[i]);
        }

        return assets; }


    public static etfAsset[] initETFAssets(String[] etfName, double[] etfPrice, String[] etfCountry){
        etfAsset[] assets = new etfAsset[etfName.length];
        for(int i = 0; i < assets.length; i++){
            assets[i] = new etfAsset(etfName[i], etfPrice[i], etfCountry[i]);
        }
        return assets; }


    public static commodityAsset[] initCommodityAssets(String[] commodityName, double[] commodityPrice){
        commodityAsset[] assets = new commodityAsset[commodityName.length];
        for(int i = 0; i < assets.length; i++){
            assets[i] = new commodityAsset(commodityName[i], commodityPrice[i]);
        }
        return assets; }

    public static void assetsAvailable(Account acc, Assets[][] polyAssets, dividendCompanyStock[] dividendCompanyStocks, cryptoAsset[] cryptoAssets){
        System.out.println(displayDetails(acc));
        System.out.println("What type of asset do you want to look at?\n" +
                "- Company stocks                 (0)\n" +
                "- Commodities                    (1)\n" +
                "- Currency Exchange              (2)\n" +
                "- ETF Index Funds                (3)\n" +
                "- Dividend paying company stocks (4)\n" +
                "- Cryptocurrencies               (5)\n\n" +
                "- Back                           (6)");
    }
    
    public static void assetsAvailableTimer(Account acc, Assets[][] polyAssets, dividendCompanyStock[] dividendCompanyStocks, cryptoAsset[] cryptoAssets){
        Random random = new Random();
        acc.setTick(acc.getTick()-1);
        TimerTask timer = new TimerTask(){
        @Override
        public void run(){
            if(isDividendDate(acc, dividendCompanyStocks)){
                acc.setNotification(true);
                acc.setNotification(acc.getNotificationMessage()+"\n"+dividendMessage(acc, dividendCompanyStocks));
                acc.setBalance(acc.getBalance()+dividendPayout(acc, dividendCompanyStocks));
            }
            int assetIndex = random.nextInt(6);
            if(assetIndex==0||assetIndex==1||assetIndex==2||assetIndex==3){int index = random.nextInt(polyAssets[assetIndex].length); polyAssets[assetIndex][index] = priceRecalculatePoly(polyAssets[assetIndex][index]);}
            else if(assetIndex==4){int index = random.nextInt(dividendCompanyStocks.length); dividendCompanyStocks[index] = priceRecalculateDividend(dividendCompanyStocks[index]);}
            else if(assetIndex==5){int index = random.nextInt(cryptoAssets.length); cryptoAssets[index] = priceRecalculateCrypto(cryptoAssets[index]);}
            cls();
            acc.setTick(acc.getTick()+1);
            assetsAvailable(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
        }
                
    };
    
        Timer update = new Timer();
        update.schedule(timer, 0, delay);
        
        boolean run = true;
        while(run){
           int index = inputInt("");
           if(index==0||index==1||index==2||index==3||index==4||index==5){
               run=false;
               timer.cancel();
               listAssetTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets, index);
           }
           else if(index==6){
               run=false;
               timer.cancel();
               mainTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
           }
        }
    }

    public static void listAssetTimer(Account acc, Assets[][] polyAssets, dividendCompanyStock[] dividendCompanyStocks, cryptoAsset[] cryptoAssets, int index){
        Random random = new Random();
        acc.setTick(acc.getTick()-1);
        TimerTask timer = new TimerTask(){
        @Override
        public void run(){
            if(isDividendDate(acc, dividendCompanyStocks)){
                acc.setNotification(true);
                acc.setNotification(acc.getNotificationMessage()+"\n"+dividendMessage(acc, dividendCompanyStocks));
                acc.setBalance(acc.getBalance()+dividendPayout(acc, dividendCompanyStocks));
            }
            int assetIndex = random.nextInt(6);
            if(assetIndex==0||assetIndex==1||assetIndex==2||assetIndex==3){int index = random.nextInt(polyAssets[assetIndex].length); polyAssets[assetIndex][index] = priceRecalculatePoly(polyAssets[assetIndex][index]);}
            else if(assetIndex==4){int index = random.nextInt(dividendCompanyStocks.length); dividendCompanyStocks[index] = priceRecalculateDividend(dividendCompanyStocks[index]);}
            else if(assetIndex==5){int index = random.nextInt(cryptoAssets.length); cryptoAssets[index] = priceRecalculateCrypto(cryptoAssets[index]);}
            cls();
            acc.setTick(acc.getTick()+1);
            listAsset(acc, polyAssets, dividendCompanyStocks, cryptoAssets, index);
        }
                
        };
        Timer update = new Timer();
        update.schedule(timer, 0, delay);

        boolean run = true;
        while(run){
            int choice = inputInt("");
            if(index==0||index==1||index==2||index==3){
                run=false;
                timer.cancel();
                buyAssetTimer(acc, polyAssets[index], polyAssets, dividendCompanyStocks, cryptoAssets, choice);
            }
            else if(index==4){
                run=false;
                timer.cancel();
                buyAssetTimer(acc, dividendCompanyStocks, polyAssets, dividendCompanyStocks, cryptoAssets, choice);
            }
            else if(index==5){
                run=false;
                timer.cancel();
                buyAssetTimer(acc, cryptoAssets, polyAssets, dividendCompanyStocks, cryptoAssets, choice);
            }
            else if(choice==6){
                run=false;
                timer.cancel();
                assetsAvailableTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
            }
        }
    }

    public static void listAsset(Account acc, Assets[][] polyAssets, dividendCompanyStock[] dividendCompanyStocks, cryptoAsset[] cryptoAssets, int index){
        cls();
        System.out.println(displayDetails(acc));
        
        if(index==0||index==1||index==2||index==3){
            for(int i = 0; i < polyAssets[index].length;i++){
            System.out.println("Name: " + polyAssets[index][i].getName() + "     Buy "+ "("+i+")" + "\nPrice: " + polyAssets[index][i].getBuyPrice()+" "+polyAssets[index][i].getChange()+"\n");}
        }
        
        
        else if(index==4){
            for(int i = 0; i < dividendCompanyStocks.length; i++){
                System.out.println(dividendCompanyStockList(dividendCompanyStocks, i));
            }
        }
        
        else if(index==5){
            for(int i = 0; i < cryptoAssets.length; i++){
                System.out.println(cryptoAssetList(cryptoAssets, i));
            }
            Random rand = new Random();
            System.out.println("Public Blockchain Transactions: "+cryptoAssets[rand.nextInt(cryptoAssets.length)].getTransaction());
            
        }
        
        System.out.println("-  Back (6)");

    }
    
    public static String dividendCompanyStockList(dividendCompanyStock[] assets, int index){
        return "Name: " + assets[index].getName() + "     Buy: "+ "("+index+")" +
        "\nPrice: " + assets[index].getBuyPrice()+" "+assets[index].getChange() +
        "\nYield amount per share: " + assets[index].getDividendYield() + 
        " Per " + assets[index].getDividendDate() + " seconds\n";
    }
    
    public static String cryptoAssetList(cryptoAsset[] assets, int index){
        return "Name: " + assets[index].getName() + "     Buy: "+ "("+index+")" + "\nPrice: " + 
        assets[index].getBuyPrice()+" "+assets[index].getChange()+"\n\n";
        
    }
    
    
    public static void portfolioTimer(Account acc, Assets[][] polyAssets, dividendCompanyStock[] dividendCompanyStocks, cryptoAsset[] cryptoAssets){
        Random random = new Random();
        acc.setTick(acc.getTick()-1);
        TimerTask timer = new TimerTask(){
        @Override
        public void run(){
            cls();
            if(isDividendDate(acc, dividendCompanyStocks)){
                acc.setNotification(true);
                acc.setNotification(acc.getNotificationMessage()+"\n"+dividendMessage(acc, dividendCompanyStocks));
                acc.setBalance(acc.getBalance()+dividendPayout(acc, dividendCompanyStocks));
            }
            int assetIndex = random.nextInt(6);
            if(assetIndex==0||assetIndex==1||assetIndex==2||assetIndex==3){int index = random.nextInt(polyAssets[assetIndex].length); polyAssets[assetIndex][index] = priceRecalculatePoly(polyAssets[assetIndex][index]);}
            else if(assetIndex==4){int index = random.nextInt(dividendCompanyStocks.length); dividendCompanyStocks[index] = priceRecalculateDividend(dividendCompanyStocks[index]);}
            else if(assetIndex==5){int index = random.nextInt(cryptoAssets.length); cryptoAssets[index] = priceRecalculateCrypto(cryptoAssets[index]);}
            acc.setTick(acc.getTick()+1);
            portfolio(acc);
        }
                
        };
        Timer update = new Timer();
        update.schedule(timer, 0, delay);

        boolean run = true;
        while(run){
            int choice = inputInt("");
            if(choice==6){
                run = false;
                timer.cancel();
                mainTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
            }
            else if(choice<(acc.getOwnedAssets().size())){
                for(int i = 0; i < acc.getOwnedAssets().size();i++){
                    if(acc.getOwnedAssets().get(choice).getName().equals(polyAssets[0][i].getName())){acc.setBalance(acc.getBalance()+polyAssets[0][i].getBuyPrice()*acc.getOwnedAssets().get(choice).getQuantity());}
                    else if(acc.getOwnedAssets().get(choice).getName().equals(polyAssets[1][i].getName())){acc.setBalance(acc.getBalance()+polyAssets[1][i].getBuyPrice()*acc.getOwnedAssets().get(choice).getQuantity());}
                    else if(acc.getOwnedAssets().get(choice).getName().equals(polyAssets[2][i].getName())){acc.setBalance(acc.getBalance()+polyAssets[2][i].getBuyPrice()*acc.getOwnedAssets().get(choice).getQuantity());}
                    else if(acc.getOwnedAssets().get(choice).getName().equals(polyAssets[3][i].getName())){acc.setBalance(acc.getBalance()+polyAssets[3][i].getBuyPrice()*acc.getOwnedAssets().get(choice).getQuantity());}
                    else if(acc.getOwnedAssets().get(choice).getName().equals(dividendCompanyStocks[i].getName())){acc.setBalance(acc.getBalance()+dividendCompanyStocks[i].getBuyPrice()*acc.getOwnedAssets().get(choice).getQuantity());}
                    else if(acc.getOwnedAssets().get(choice).getName().equals(cryptoAssets[i].getName())){acc.setBalance(acc.getBalance()+cryptoAssets[i].getBuyPrice()*acc.getOwnedAssets().get(choice).getQuantity());}
            }
            
            run = false;
            timer.cancel();
            acc.removeAsset(acc.getOwnedAssets().get(choice));
            portfolioTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
                

            }
            
        }
    }   
    
    
    public static void portfolio(Account acc){
        System.out.println(displayDetails(acc));
        if(acc.getOwnedAssets().size()==0){
            System.out.println("You do not own any stocks");
        }
        
        else{ 
            for(int i = 0; i < acc.getOwnedAssets().size(); i++){
                System.out.println(acc.getOwnedAssets().get(i).getName() +
                "\nQuantity: " + acc.getOwnedAssets().get(i).getQuantity() +
                "\nBought at: " + acc.getOwnedAssets().get(i).getBuyPrice() + "  Sell" + " ("+i+")\n");
            }
        }
        System.out.println("- Back (6)");
        
    }
    
    public static void notificationTimer(Account acc, Assets[][] polyAssets, dividendCompanyStock[] dividendCompanyStocks, cryptoAsset[] cryptoAssets){
        Random random = new Random();
        acc.setTick(acc.getTick()-1);
        TimerTask timer = new TimerTask(){
        @Override
        public void run(){
            if(isDividendDate(acc, dividendCompanyStocks)){
                acc.setNotification(true);
                acc.setNotification(acc.getNotificationMessage()+"\n"+dividendMessage(acc, dividendCompanyStocks));
                acc.setBalance(acc.getBalance()+dividendPayout(acc, dividendCompanyStocks));
            }
            int assetIndex = random.nextInt(6);
            if(assetIndex==0||assetIndex==1||assetIndex==2||assetIndex==3){int index = random.nextInt(polyAssets[assetIndex].length); polyAssets[assetIndex][index] = priceRecalculatePoly(polyAssets[assetIndex][index]);}
            else if(assetIndex==4){int index = random.nextInt(dividendCompanyStocks.length); dividendCompanyStocks[index] = priceRecalculateDividend(dividendCompanyStocks[index]);}
            else if(assetIndex==5){int index = random.nextInt(cryptoAssets.length); cryptoAssets[index] = priceRecalculateCrypto(cryptoAssets[index]);}
            cls();
            acc.setTick(acc.getTick()+1);
            notifications(acc);
        }
                
        };
        Timer update = new Timer();
        update.schedule(timer, 0, delay);

        boolean run = true;
        while(run){
            int choice = inputInt("");
            if(choice==6){
                run=false;
                timer.cancel();
                mainTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
            }
        }
    }
    
    public static Assets priceRecalculatePoly(Assets asset){
        asset.setBuyPrice(asset.getBuyPrice()*asset.getPriceChange());
        return asset;
    }
    
    public static dividendCompanyStock priceRecalculateDividend(dividendCompanyStock asset){
        asset.setBuyPrice(asset.getBuyPrice()*asset.getPriceChange());
        return asset;
    }
    
    public static cryptoAsset priceRecalculateCrypto(cryptoAsset asset){
        asset.setBuyPrice(asset.getBuyPrice()*asset.getPriceChange());
        return asset;
    }
    
    public static void notifications(Account acc){
        System.out.println(displayDetails(acc));
        System.out.println(acc.getNotificationMessage());
        System.out.println("\n - Back (6)");
    }
    
    public static void buyAssetTimer(Account acc, Assets[] assetArray, Assets[][] polyAssets, dividendCompanyStock[] dividendCompanyStocks, cryptoAsset[] cryptoAssets, int index){
        Random random = new Random();
        if(index==6){assetsAvailableTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);}
        
        Assets asset = assetArray[index];
        acc.setTick(acc.getTick()-1);
        TimerTask timer = new TimerTask(){
        @Override
        public void run(){
            if(isDividendDate(acc, dividendCompanyStocks)){
                acc.setNotification(true);
                acc.setNotification(acc.getNotificationMessage()+"\n"+dividendMessage(acc, dividendCompanyStocks));
                acc.setBalance(acc.getBalance()+dividendPayout(acc, dividendCompanyStocks));
            }
            int assetIndex = random.nextInt(6);
            if(assetIndex==0||assetIndex==1||assetIndex==2||assetIndex==3){int index = random.nextInt(polyAssets[assetIndex].length); polyAssets[assetIndex][index] = priceRecalculatePoly(polyAssets[assetIndex][index]);}
            else if(assetIndex==4){int index = random.nextInt(dividendCompanyStocks.length); dividendCompanyStocks[index] = priceRecalculateDividend(dividendCompanyStocks[index]);}
            else if(assetIndex==5){int index = random.nextInt(cryptoAssets.length); cryptoAssets[index] = priceRecalculateCrypto(cryptoAssets[index]);}
            cls();
            acc.setTick(acc.getTick()+1);
            buyAsset(acc, asset);
        }
                
        };
        Timer update = new Timer();
        update.schedule(timer, 0, delay);

        boolean run = true;
        while(run){
            
            int quantity = inputInt("");
            boolean alreadyOwned = false;
            int whichAsset = 0;
            
            for(int i = 0; i < acc.getOwnedAssets().size();i++){
                if(acc.getOwnedAssets().get(i).getName().equals(asset.getName())){
                    alreadyOwned = true;
                    whichAsset = i;
                }
            }
            
            if(asset.getBuyPrice()*quantity>acc.getBalance()){
                System.out.println("Insufficient funds!");
                timer.cancel();
                mainTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
            }
            
            else if(alreadyOwned){
                run=false;
                timer.cancel();
                acc.setBalance(acc.getBalance()-(asset.getBuyPrice()*quantity));
                acc.getOwnedAssets().get(whichAsset).setBuyPrice((acc.getOwnedAssets().get(whichAsset).getBuyPrice()*acc.getOwnedAssets().get(whichAsset).getQuantity()+asset.getBuyPrice()*quantity)/(acc.getOwnedAssets().get(whichAsset).getQuantity()+quantity));
                acc.getOwnedAssets().get(whichAsset).setQuantity(acc.getOwnedAssets().get(whichAsset).getQuantity()+quantity);
                mainTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
            }

            else{
                run=false;
                timer.cancel();
                System.out.println("Stocks bought! ");
                acc.setBalance(acc.getBalance()-(asset.getBuyPrice()*quantity));
                acc.addAsset(new Assets(asset.getName(), asset.getBuyPrice(), quantity));
                mainTimer(acc, polyAssets, dividendCompanyStocks, cryptoAssets);
            }
        }
            
        
    }
    
    public static boolean isDividendDate(Account acc, dividendCompanyStock[] dividendCompanyStock){
        boolean isDividendAssetOwned = false;
        boolean isDividendDate = false;
        int dividendAssetIndex = 0;
        
        for(int j = 0; j < acc.getOwnedAssets().size();j++){
            for(int i = 0; i < dividendCompanyStock.length;i++){
                if(acc.getOwnedAssets().get(j).getName().equals(dividendCompanyStock[i].getName())){ 
                   isDividendAssetOwned = true;
                   dividendAssetIndex = i;
                   
                }
            }
        }
        if(isDividendAssetOwned){
            if(dividendCompanyStock[dividendAssetIndex].isDividendDate(acc.getTick())){
                isDividendDate = true;
            }
        }
        return isDividendDate;
    }
    
   public static double dividendPayout(Account acc, dividendCompanyStock[] dividendCompanyStock){
       double payoutAmount = 0;
       for(int j = 0; j < acc.getOwnedAssets().size();j++){
           for(int i = 0; i < dividendCompanyStock.length;i++){
               if(acc.getOwnedAssets().get(j).getName().equals(dividendCompanyStock[i].getName())){ 
                   payoutAmount = dividendCompanyStock[i].getDividendYield()*acc.getOwnedAssets().get(j).getQuantity();
               }
           }
       }

       return payoutAmount;
    }
    
    public static String dividendMessage(Account acc, dividendCompanyStock[] dividendCompanyStock){
       String message = "";
       for(int j = 0; j < acc.getOwnedAssets().size();j++){
           for(int i = 0; i < dividendCompanyStock.length;i++){
               if(acc.getOwnedAssets().get(j).getName().equals(dividendCompanyStock[i].getName())){ 
                   message = "You have been paid: " + dividendCompanyStock[i].getDividendYield()*dividendCompanyStock[i].getBuyPrice() + " by " + dividendCompanyStock[i].getName();
               }
           }
       }
       return message;
    }
    
    public static void buyAsset(Account acc, Assets asset){
        System.out.println(displayDetails(acc));
        System.out.println("Choose quantity " + asset.getName() + "\nCurrent price: " + asset.getBuyPrice());
 
    }
     
    public static String inputString(String s){
        Scanner scanner = new Scanner(System.in);
        System.out.println(s);
        return scanner.nextLine();
    }

    public static int inputInt(String s){
        Scanner scanner = new Scanner(System.in);
        System.out.println(s);
        return scanner.nextInt();
    }
    
    public static String displayDetails(Account acc){
        return "\u000C" + acc.getName() + 
        "    Balance: " + acc.getBalance() + 
        "      Time: " + acc.getTick() + "\n";
    }

    public static Account createAccount(){
        Account acc = new Account(inputString("What is your username"), inputInt("What is your balance"));
        return acc;
    }

}
