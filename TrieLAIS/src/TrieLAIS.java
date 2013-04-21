import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Sapan
 */
public class TrieLAIS {

    private Map<Character, TrieLAIS> children;
    private RecordLAIS_TRIE word = new RecordLAIS_TRIE();
    private Set<TrieLAIS> descendantsWithWord;
    private boolean isBuilt = false;


    public TrieLAIS() {
        this.children = new HashMap<Character, TrieLAIS>();
        descendantsWithWord = new HashSet<TrieLAIS>();
    }

    public void add(RecordLAIS_TRIE word) throws IllegalAccessException {
        if (isBuilt) {
            throw new IllegalAccessException("Can not add words after calling the build method");
        }
        TrieLAIS t = this;
        for (Character c : word.name.toCharArray()) { //for every character in query do the following....
            //add character to the current instance and get the child instance right after adding the current char
            if (!t.children.containsKey(c)) {
                t.children.put(c, new TrieLAIS());
            }
            t = t.children.get(c);
        }
        t.word = word.clone();
    }

    public Set<TrieLAIS> search(final String word) {
        Set s = new HashSet();
        TrieLAIS current = this;
        for (final Character c : word.toCharArray()) {
            current = current.children.get(c);
            if (current == null) {
                return Collections.emptySet();
            }
        }
        for (TrieLAIS t : current.descendantsWithWord) {
            s.add(t);
        }
        return s;
    }

    public Set<TrieLAIS> build() {
        isBuilt = true;
        for (final TrieLAIS childTrie : children.values()) {
            descendantsWithWord.addAll(childTrie.build());
        }
        if (this.word.name != null && this.word.name.length() > 0) {
            this.descendantsWithWord.add(this);
        }
        if (this.children.keySet().isEmpty()) {// we are at the leaf node
            this.descendantsWithWord.add(this);
        }
        return this.descendantsWithWord;
    }

    public static void main(String[] args) throws IOException, IllegalAccessException {
        TrieLAIS t = new TrieLAIS();

        // Open the file
        FileInputStream fstream = new FileInputStream("src/Data.txt");

        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;

        //Read File Line By Line
        while ((strLine = br.readLine()) != null){
            String line[] = strLine.split(",");
            RecordLAIS_TRIE r  = new RecordLAIS_TRIE();
            r.name = line[2];
            r.name = r.name.substring(1, r.name.length()-1);
            r.point.setLatitudeLongitude(Double.parseDouble(line[0]), Double.parseDouble(line[1]));
            t.add(r);
        }
        //Close the input stream
        in.close();

        t.build();
        Set<TrieLAIS> l = t.search("P");
        System.out.println("Results");
        for (TrieLAIS tr : l) {
            System.out.println(tr.word.name);
        }
    }
}