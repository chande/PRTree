import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static ArrayList<Record> records = new ArrayList<Record>();

    public static int getRecords(String[] args) throws NumberFormatException, IOException{
        String inputFile;
        int M;

        if (args.length != 2){
            inputFile = "data/sanitized.txt";
            M = 10;
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
        //        prt.textOnlySearch("virgin", prt.root);
        //        Coordinates point = new Coordinates(-74.378,40.799);
        //        //prt.singlePrefixQuery("par", point, 15, prt.root);
        //        System.out.println("***********************");
        //        point = new Coordinates(-77.379,48.798);
        //        prt.singlePrefixQuery("sch", point, 15, prt.root);

        HashMap<String, ArrayList<Double>> values = new HashMap<String, ArrayList<Double>>();
        for (int i = 0; i <= 4; i++){
            File[] files = new File("data/3/100").listFiles();
            for (File f : files){
                values.put(f.getName(), new ArrayList<Double>());

                FileInputStream fstream = new FileInputStream(f.getAbsolutePath());
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                //Read File Line By Line
                long startTime = System.nanoTime();
                while ((strLine = br.readLine()) != null){
                    if (strLine.length() > 0){
                        String[] line = strLine.split(",");
                        String prfx = line[0];
                        Double lat = Double.parseDouble(line[1]);
                        Double lon = Double.parseDouble(line[2]);
                        Coordinates qPoint = new Coordinates(lat, lon);
                        int results = Integer.parseInt(line[3]);
                        prt.kNNQuery(prfx, qPoint, results, prt.root);
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

    }
}
