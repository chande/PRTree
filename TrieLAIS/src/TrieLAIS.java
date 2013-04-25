import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author Sapan (modified by chande)
 * 
 */
public class TrieLAIS {

    private Map<Character, TrieLAIS> children;
    public RecordLAIS_TRIE word = new RecordLAIS_TRIE();
    private Set<TrieLAIS> descendantsWithWord;
    public ArrayList<RecordLAIS_TRIE> recList = new ArrayList<RecordLAIS_TRIE>();
    private boolean isBuilt = false;

    static Comparator<RecordLAIS_TRIE> recordComparator = new RecordComparatorLAIS_TRIE();
    static PriorityQueue<RecordLAIS_TRIE> recQ = new PriorityQueue<RecordLAIS_TRIE>(10, recordComparator);
    static ArrayList<RecordLAIS_TRIE> recordList = new ArrayList<RecordLAIS_TRIE>();

    //search variables
    static CoordinatesLAIS_TRIE point = new CoordinatesLAIS_TRIE(-74.378,40.799);
    static int k = 15;


    public TrieLAIS() {
        this.children = new HashMap<Character, TrieLAIS>();
        descendantsWithWord = new HashSet<TrieLAIS>();
    }

    public void add(RecordLAIS_TRIE word) throws IllegalAccessException {
        if (isBuilt) {
            throw new IllegalAccessException("Can not add words after calling the build method");
        }
        TrieLAIS t = this;
        for (Character c : word.parseString.toCharArray()) { //for every character in query do the following....
            //add character to the current instance and get the child instance right after adding the current char
            if (!t.children.containsKey(c)) {
                t.children.put(c, new TrieLAIS());
            }
            t = t.children.get(c);
        }
        //        try{
        if (t.word.parseString.equals(word.parseString)){
            t.recList.add(word);
            //System.out.println("T RECLIST SIZE FOR WORD: " + word.parseString + " " + t.recList.size() + "****************");
        }else{
            //            }
            //            else{
            //                t.word = word;
            //            }
            //        }
            //   catch (Exception e){
            t.word = word;
        }
        //     }
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
        if (this.word.parseString != null && this.word.parseString.length() > 0) {
            this.descendantsWithWord.add(this);
        }
        if (this.children.keySet().isEmpty()) {// we are at the leaf node
            this.descendantsWithWord.add(this);
        }
        return this.descendantsWithWord;
    }

    public static void printList(Set <TrieLAIS> l){
        for (TrieLAIS tr : l) {
            tr.word.updateDistance(point);
            recordList.add(tr.word);
            if (tr.recList.size() > 0){
                for (RecordLAIS_TRIE r : tr.recList){
                    r.updateDistance(point);
                    recordList.add(r);
                }
            }
        }
        for (RecordLAIS_TRIE r : recordList){
            recQ.add(r);
        }
        for(int i = 0; i < k; i++){
            if (recQ.peek() != null){
                RecordLAIS_TRIE output = recQ.remove();
                System.out.println(output.recName + " with distance " + output.getDist());
                //System.out.println(output.recName );
            }
        }
    }

    public static void main(String[] args) throws IOException, IllegalAccessException {
        TrieLAIS t = new TrieLAIS();

        // Open the file
        FileInputStream fstream = new FileInputStream("src/sanitized.txt");

        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;

        //Read File Line By Line
        while ((strLine = br.readLine()) != null){
            String line[] = strLine.split(",");
            String words[] = line[2].split(" ");
            for (String s : words){
                s = s.replace("\"","");
                RecordLAIS_TRIE r  = new RecordLAIS_TRIE();
                r.parseString = s.toLowerCase();
                r.recName = line[2].replace("\"","").toLowerCase();
                //System.out.println("PARSESTRING: " + r.parseString + " RECORD NAME: " + r.recName);
                r.point.setLatitudeLongitude(Double.parseDouble(line[0]), Double.parseDouble(line[1]));
                t.add(r);
            }
        }
        //Close the input stream
        in.close();
        t.build();


        HashMap<String, ArrayList<Double>> values = new HashMap<String, ArrayList<Double>>();
        for (int i = 0; i <= 4; i++){
            File[] files = new File("data/3/500").listFiles();
            for (File f : files){
                values.put(f.getName(), new ArrayList<Double>());

                FileInputStream fstream1 = new FileInputStream(f.getAbsolutePath());
                // Get the object of DataInputStream
                DataInputStream in1 = new DataInputStream(fstream1);
                BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
                String strLine1;
                //Read File Line By Line
                long startTime = System.nanoTime();
                while ((strLine1 = br1.readLine()) != null){
                    if (strLine1.length() > 0){
                        String[] line = strLine1.split(",");
                        String prfx = line[0];
                        Double lat = Double.parseDouble(line[1]);
                        Double lon = Double.parseDouble(line[2]);
                        point = new CoordinatesLAIS_TRIE(lat, lon);
                        k = Integer.parseInt(line[3]);
                        Set<TrieLAIS> l = t.search(prfx);
                    }
                }
                long endTime = System.nanoTime();
                values.get(f.getName()).add((endTime - startTime) / 1000000000.0);
            }

        }
        Double total = 0.0;
        for (String str : values.keySet()){
            total = 0.0;
            for (Double al : values.get(str)){
                total += al;
            }
            System.out.println(str + " took an average "+ total/5 + " s");
        }
        //        Set<TrieLAIS> l = t.search("par");
        //        printList(l);
        //        point = new CoordinatesLAIS_TRIE(-77.379,48.798);
        //        System.out.println("***********************");
        //        l = t.search("sch");
        //        printList(l);
    }
}



