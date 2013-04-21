import java.util.Comparator;

public class RecordComparatorLAIS_TRIE implements Comparator<RecordLAIS_TRIE>
{
    @Override
    public int compare(RecordLAIS_TRIE x, RecordLAIS_TRIE y){

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