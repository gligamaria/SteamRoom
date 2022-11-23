
public class Profile {

    // This class describes the profiles, having as variables
    // name and wanted temperature and humidity.

    private final String name;
    private final Integer wantedTemperature;
    private final Integer wantedHumidity;

    public Profile(Integer wantedTemperature, Integer wantedHumidity, String name){
        this.name = name;
        this.wantedHumidity = wantedHumidity;
        this.wantedTemperature = wantedTemperature;
    }

    public String getName() {
        return name;
    }

    public Integer getWantedTemperature() {
        return wantedTemperature;
    }

    public Integer getWantedHumidity() {
        return wantedHumidity;
    }

}
