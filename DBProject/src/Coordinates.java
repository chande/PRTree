
public class Coordinates {

    public double latitude;
    public double longitude;

    public Coordinates(double lat, double lon){
        latitude = lat;
        longitude = lon;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLatitudeLongitude(double lat, double lon){
        latitude = lat;
        longitude = lon;
    }

}
