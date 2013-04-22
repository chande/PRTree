import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static ArrayList<Record> records = new ArrayList<Record>();

    public static int getRecords(String[] args) throws NumberFormatException, IOException{
        String inputFile;
        int M;

        if (args.length != 2){
            inputFile = "src/sanitized.txt";
            M = 3;
        }
        else{
            inputFile = args[0];
            M = Integer.parseInt(args[1]);
        }

        FileInputStream fstream = new FileInputStream(inputFile);
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null){
            String[] line = strLine.split(",");
            Record currentRec = new Record(line[2].replaceAll("\"", "").toLowerCase(), Double.parseDouble(line[0]),
                    Double.parseDouble(line[1]));
            records.add(currentRec);
        }
        return M;
    }

    public static void main(String[] args) throws IOException{
        int M = getRecords(args);
        PRTree prt = new PRTree(records, M);
        //prt.textOnlySearch("hurt", prt.root);
        Coordinates point = new Coordinates(-74.378,40.799);
        prt.singlePrefixQuery("hurt", point, 4, prt.root);
    }
}
