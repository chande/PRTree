import java.util.Comparator;

public class RecordComparator implements Comparator<Record>
{
    @Override
    public int compare(Record x, Record y){

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