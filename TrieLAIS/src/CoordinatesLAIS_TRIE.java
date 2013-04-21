
public class CoordinatesLAIS_TRIE {

    public double latitude;
    public double longitude;

    public CoordinatesLAIS_TRIE(double lat, double lon){
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
