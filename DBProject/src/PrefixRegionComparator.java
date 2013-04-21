import java.util.Comparator;

public class PrefixRegionComparator implements Comparator<PrefixRegion>
{
    @Override
    public int compare(PrefixRegion x, PrefixRegion y){

        // Assume neither string is null.
        if (x.getDist() < y.getDist())
        {
            return -1;
        }
        if (x.getDist() > y.getDist())
        {
            return 1;
        }
        return 0;
    }
}