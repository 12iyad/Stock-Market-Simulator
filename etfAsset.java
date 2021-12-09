public class etfAsset extends Assets{
    private final String countryName;

    public etfAsset( String newAN, double newBP, String newCountryName) {
        super(newAN, newBP);
        countryName = newCountryName;
    }

    @Override
    public String getName(){return assetName + "  ("+countryName+")";}

}