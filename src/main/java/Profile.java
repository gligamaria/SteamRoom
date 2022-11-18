public class Profile {
    private String name;
    private Integer wantedTemperature;
    private Integer wantedHumidity;

    public Profile(Integer wantedTemperature, Integer wantedHumidity, String name){
        this.name = name;
        this.wantedHumidity = wantedHumidity;
        this.wantedTemperature = wantedTemperature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWantedTemperature() {
        return wantedTemperature;
    }

    public void setWantedTemperature(Integer wantedTemperature) {
        this.wantedTemperature = wantedTemperature;
    }

    public Integer getWantedHumidity() {
        return wantedHumidity;
    }

    public void setWantedHumidity(Integer wantedHumidity) {
        this.wantedHumidity = wantedHumidity;
    }
}
