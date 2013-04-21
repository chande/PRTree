import java.util.ArrayList;

public class Record{
    public ArrayList<String> name = new ArrayList<String>();
    public Coordinates point = new Coordinates(0, 0);
    public int offset = 0;
    public String currentPrefix = new String();
    public double distanceFromCurrentQuery = 0;

    public Record(){

    }

    public Record(String recName, double lat, double lon){
        for (String item : recName.split(" ")){
            name.add(item);
        }
        point.setLatitudeLongitude(lat, lon);
    }

    public void updateOffset(){
        offset +=1;
    }

    public void setString(String str){
        currentPrefix = str;
    }

    public void updateDistance(Coordinates queryCoords){
        distanceFromCurrentQuery = 0;
        double queryX = queryCoords.getLatitude();
        double queryY = queryCoords.getLongitude();
        double term1 = Math.pow(point.getLatitude() - queryX, 2);
        double term2 = Math.pow(point.getLongitude() - queryY, 2);
        distanceFromCurrentQuery = Math.sqrt(term1 + term2);
    }

    public double getDist(){
        return distanceFromCurrentQuery;
    }

    @Override
    public Record clone(){
        Record r = new Record();
        r.name = new ArrayList<String>();
        for (String s : name){
            r.name.add(new String(s));
        }
        r.point.setLatitudeLongitude(Double.valueOf(point.latitude), Double.valueOf(point.longitude));
        r.offset = offset;
        r.currentPrefix = new String(currentPrefix);
        r.distanceFromCurrentQuery = distanceFromCurrentQuery;
        return r;
    }

}