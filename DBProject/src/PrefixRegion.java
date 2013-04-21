import java.util.ArrayList;
import java.util.HashMap;

public class PrefixRegion{
    public char charPrefix;
    public String stringPrefix = new String();
    public int offset = 0;

    public double xMin = 0;
    public double xMax = 0;
    public double yMin = 0;
    public double yMax = 0;
    public double distanceFromCurrentQuery = 0;

    public HashMap<Character, ArrayList<PrefixRegion>> children =
            new HashMap<Character, ArrayList<PrefixRegion>>();
    //upperLeft = <prefix, children[0]>
    //lowerLeft = <prefix, children[1]>
    //lowerRight = <prefix, children[2]>
    //upperRight = <prefix, children[3]>
    public ArrayList<Record> objectList = new ArrayList<Record>();

    public PrefixRegion(){
        //ok
    }

    public PrefixRegion(char c){
        charPrefix = c;
        children.put(c, new ArrayList<PrefixRegion>(4));
    }

    public PrefixRegion(char pfx, int p1Lat, int p1Lon, int p2Lat, int p2Lon){
        children = null;
        charPrefix = pfx;
    }

    public void setCharPrefix(char c){
        charPrefix = c;
    }

    public void setStringPrefix(String c){
        stringPrefix = c;
    }

    public void updateOffset(){
        offset +=1;
    }

    //minD calculation
    public void updateDistance(Coordinates queryCoords){
        double queryX = queryCoords.getLatitude();
        double queryY = queryCoords.getLongitude();

        distanceFromCurrentQuery = 0;
        if (xMin > queryX){
            distanceFromCurrentQuery += Math.pow((xMin - queryX), 2);
        }
        else if (xMax < queryX){
            distanceFromCurrentQuery += Math.pow((queryX - xMax), 2 );
        }
        if(yMin > queryY){
            distanceFromCurrentQuery += Math.pow((yMin - queryY), 2);
        }
        else if(yMax < queryY){
            distanceFromCurrentQuery += Math.pow((queryY-yMax), 2);
        }
    }

    public double getDist(){
        return distanceFromCurrentQuery;
    }
}