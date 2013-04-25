
public class RecordLAIS_TRIE{
    public String parseString = new String();
    public String recName = new String();
    public CoordinatesLAIS_TRIE point = new CoordinatesLAIS_TRIE(0, 0);
    public int offset = 0;
    public String currentPrefix = new String();
    public double distanceFromCurrentQuery = 0;

    public RecordLAIS_TRIE(){

    }

    public RecordLAIS_TRIE(String recName, double lat, double lon){
        parseString = recName;
        point.setLatitudeLongitude(lat, lon);
    }

    public void updateOffset(){
        offset +=1;
    }

    public void setString(String str){
        currentPrefix = str;
    }

    public void updateDistance(CoordinatesLAIS_TRIE queryCoords){
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
    public RecordLAIS_TRIE clone(){
        RecordLAIS_TRIE r = new RecordLAIS_TRIE();
        r.parseString = parseString;
        r.point.setLatitudeLongitude(Double.valueOf(point.latitude), Double.valueOf(point.longitude));
        r.offset = offset;
        r.currentPrefix = new String(currentPrefix);
        r.distanceFromCurrentQuery = distanceFromCurrentQuery;
        return r;
    }

}