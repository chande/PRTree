import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class PRTree{

    Stack<PrefixRegion> initStack = new Stack<PrefixRegion>();
    PrefixRegion root = new PrefixRegion();
    boolean rootStep = true;
    boolean rootMBR = true;
    Comparator<PrefixRegion> prComparator = new PrefixRegionComparator();
    Comparator<Record> recordComparator = new RecordComparator();
    PriorityQueue<PrefixRegion> prQ =
            new PriorityQueue<PrefixRegion>(10, prComparator);
    PriorityQueue<Record> recQ =
            new PriorityQueue<Record>(10, recordComparator);

    // Algorithm 1: ConstructTree(ObjectSet, Parampeter)
    public PRTree(ArrayList<Record> recordSet, int M){

        //lines 2 - 4
        PrefixRegion newNode = new PrefixRegion();
        root.objectList = recordSet;
        findMBRSet(root);
        initStack.push(root);

        //main loop
        while (!initStack.empty()){
            //line 6
            PrefixRegion currentPR = initStack.pop();

            //lines 7 - 8
            if (currentPR.objectList.size() <= M){
                //printPR(currentPR, false);

            }
            else{
                //line 10
                HashMap<Character, ArrayList<Record>> Ct = textPartition(currentPR);
                rootStep = false;

                //lines 11 - 14
                spatialPartition(Ct, currentPR);
                for (char c : currentPR.children.keySet()){
                    for (PrefixRegion pr : currentPR.children.get(c)){
                        if(!pr.objectList.isEmpty()){
                            pr.updateOffset();
                            findMBRSet(pr);
                            initStack.push(pr);
                        }
                    }
                }
                //printPR(currentPR, true);
            }
        }
        //printTree();
    }

    //Algorithm 2: kNN Query
    public void kNNQuery(String query, Coordinates point, int k, PrefixRegion node){
        //lines 1 - 2
        PriorityQueue<Record> resultList =
                new PriorityQueue<Record>(10, recordComparator);
        PrefixRegion currentNode = new PrefixRegion();
        prQ.add(node);

        while ((resultList.size() < k) && (!prQ.isEmpty())){
            //lines 5 - 6
            if (!recQ.isEmpty()){
                //System.out.println("TOP OF RECQ DISTANCE: " + recQ.peek().getDist() + "TOP OF PRQ DISTANCE: " + prQ.peek().distanceFromCurrentQuery);
                if ((recQ.peek().getDist() <= prQ.peek().getDist())){
                    boolean repeat = false;
                    for (Record r: resultList){
                        if (r.name.equals(recQ.peek().name)){
                            recQ.remove();
                            repeat = true;
                            break;
                            //System.out.println("LOL OK " + r.name + r.distanceFromCurrentQuery);
                        }

                    }
                    if (!repeat){
                        //System.out.println("****************ADDING " + recQ.peek().name + " TO RESULTS");
                        resultList.add(recQ.remove());
                        //continue;
                    }
                }
            }
            currentNode = prQ.remove();
            findMBRSet(currentNode);
            //System.out.println("JUST POPPED " + currentNode.stringPrefix + " AT DISTANCE" + currentNode.distanceFromCurrentQuery);
            //leaf node
            if (!currentNode.objectList.isEmpty()){
                //lines 8 - 9
                for (Record r : currentNode.objectList){
                    for (String recordName : r.name){
                        if ((recordName.startsWith(query) && !query.startsWith(recordName)) || r.name.contains(query)){
                            r.updateDistance(point);
                            //System.out.println("ADDING RECORD WITH NAME " + r.name + " distance " + r.getDist());
                            recQ.add(r);
                        }
                    }
                }
            }
            //interior node
            else{
                //System.out.println("STARTING LOOP***********************************");
                for (char c : currentNode.children.keySet()){
                    //lines 11 - 13
                    for (PrefixRegion pr : currentNode.children.get(c)){
                        //System.out.println("CHECKING PR WITH PREFIX: " + pr.stringPrefix);
                        if ((pr.stringPrefix.startsWith(query) || query.startsWith(pr.stringPrefix))){
                            pr.updateDistance(point);
                            //System.out.println("ALSO IT WAS ADDED AND HAS DISTANCE " + pr.getDist());
                            prQ.add(pr);
                        }
                    }
                }
                //System.out.println("LOOP ENDED************************************");
            }
        }
        for (Record r : recQ){
            resultList.add(r);
        }
        for (int i = 0; i < k; i++){
            if (!resultList.isEmpty()){
                Record r = resultList.remove();
                //System.out.println(r.name);
                //System.out.println(r.name + " at distance " + r.getDist());
            }
        }
        recQ.clear();
        resultList.clear();
        prQ.clear();
    }

    public void textOnlySearch(String query, PrefixRegion node){
        char c = query.charAt(0);
        //interior node
        if(node.children.containsKey(c)){
            for (PrefixRegion pr : node.children.get(c)){
                printNode(pr, query);
            }
        }
        //leaf node
        else{
            printNode(node, query);
        }
    }

    public void printNode(PrefixRegion pr, String query){
        if (!pr.objectList.isEmpty()){
            for (Record r : pr.objectList){
                for (String s : r.name){
                    if(s.contains(query)){
                        System.out.println(r.name + " " + r.point.getLatitude() + " "+ r.point.getLongitude() );
                        continue;
                    }
                }
            }
        }
        else{
            for (char c: pr.children.keySet()){
                for (PrefixRegion recPR : pr.children.get(c)){
                    printNode(recPR, query);
                }
            }
        }
    }

    // finds minimum/maximum longitude/latitude values that define MBR
    public void findMBRSet(PrefixRegion pr){
        double rootXMax = 0;
        double rootXMin = 0;
        double rootYMax = 0;
        double rootYMin = 0;
        boolean firstRound = true;
        boolean done = false;
        for (Record r : pr.objectList){
            if (pr.objectList.size() < 20){
                //System.out.println("FOR CURRENT RECORD WITH NAME " + r.name + " , POINTS ARE " + r.point.getLatitude() + " AND " + r.point.getLongitude());
            }
            if (firstRound){
                firstRound = false;
                rootXMax = r.point.getLatitude();
                rootXMin = r.point.getLatitude();
                rootYMax = r.point.getLongitude();
                rootYMin = r.point.getLongitude();
            }
            if(r.point.getLatitude() > rootXMax){
                rootXMax = r.point.getLatitude();
            }
            if(r.point.getLatitude() < rootXMin){
                rootXMin = r.point.getLatitude();
            }
            if (r.point.getLongitude() > rootYMax){
                rootYMax = r.point.getLongitude();
            }
            if(r.point.getLongitude() < rootYMin){
                rootYMin = r.point.getLongitude();
            }
        }
        pr.xMax = rootXMax;
        pr.xMin = rootXMin;
        pr.yMax = rootYMax;
        pr.yMin = rootYMin;
        if (pr.objectList.size() < 20){
            //System.out.println("OUT OF THOSE, MAX IS: X " + pr.xMax + " Y " + pr.yMax + " MIN IS: X" + pr.xMin + " Y " + pr.yMin);
        }
    }

    // check location of each record against centroid, assign to region, assign set to parent
    public void spatialPartition(HashMap<Character, ArrayList<Record>> Ct,
            PrefixRegion currentN){

        for (char c : Ct.keySet()){
            Coordinates centroid = getCentroid(Ct.get(c));
            PrefixRegion upperLeft = new PrefixRegion(c);
            PrefixRegion lowerLeft = new PrefixRegion(c);
            PrefixRegion lowerRight = new PrefixRegion(c);
            PrefixRegion upperRight = new PrefixRegion(c);

            //partition by centroid
            for(Record r : Ct.get(c)){
                if (r.point.getLatitude() > centroid.getLatitude() &&
                        r.point.getLongitude() < centroid.getLongitude()){
                    //System.out.println("ASSIGNING TO LOWER RIGHT");
                    lowerRight.objectList.add(r.clone());
                    lowerRight.setCharPrefix(c);
                    if (lowerRight.stringPrefix.isEmpty()){
                        lowerRight.setStringPrefix(r.currentPrefix);
                    }
                    lowerRight.offset = currentN.offset;
                }
                else if(r.point.getLatitude() < centroid.getLatitude() &&
                        r.point.getLongitude() < centroid.getLongitude()){
                    //System.out.println("ASSIGNING TO LOWER LEFT");
                    lowerLeft.objectList.add(r.clone());
                    lowerLeft.setCharPrefix(c);
                    if (lowerLeft.stringPrefix.isEmpty()){
                        lowerLeft.setStringPrefix(r.currentPrefix);
                    }
                    lowerLeft.offset = currentN.offset;
                }
                else if(r.point.getLatitude() < centroid.getLatitude() &&
                        r.point.getLongitude() > centroid.getLongitude()){
                    //System.out.println("ASSIGNING TO UPPER LEFT");
                    upperLeft.objectList.add(r.clone());
                    upperLeft.setCharPrefix(c);
                    if (upperLeft.stringPrefix.isEmpty()){
                        upperLeft.setStringPrefix(r.currentPrefix);
                    }
                    upperLeft.offset = currentN.offset;
                }
                else{
                    //System.out.println("ASSIGNING TO UPPER RIGHT");
                    upperRight.objectList.add(r.clone());
                    upperRight.setCharPrefix(c);
                    if (upperRight.stringPrefix.isEmpty()){
                        upperRight.setStringPrefix(r.currentPrefix);
                    }
                    upperRight.offset = currentN.offset;
                }
            }
            //assign children to current node
            currentN.children.put(c, new ArrayList<PrefixRegion>());
            if (!upperLeft.objectList.isEmpty()){
                findMBRSet(upperLeft);
                currentN.children.get(c).add(upperLeft);
            }
            if (!lowerLeft.objectList.isEmpty()){
                findMBRSet(lowerLeft);
                currentN.children.get(c).add(lowerLeft);
            }
            if (!lowerRight.objectList.isEmpty()){
                findMBRSet(lowerRight);
                currentN.children.get(c).add(lowerRight);
            }
            if (!upperRight.objectList.isEmpty()){
                findMBRSet(upperRight);
                currentN.children.get(c).add(upperRight);
            }
        }
        currentN.objectList.clear();
    }

    // calculate centroid
    public Coordinates getCentroid(ArrayList<Record> region){
        Coordinates centroid = new Coordinates(0, 0);
        double xSum = 0;
        double ySum = 0;
        for (Record r : region){
            xSum += r.point.getLatitude();
            ySum += r.point.getLongitude();
        }

        xSum = xSum / region.size();
        ySum = ySum / region.size();
        centroid.setLatitudeLongitude(xSum, ySum);
        return centroid;
    }

    // split prefix region according to current letter depth of tree
    public HashMap<Character, ArrayList<Record>> textPartition(PrefixRegion pr){
        HashMap<Character, ArrayList<Record>> partitioned =
                new HashMap<Character, ArrayList<Record>>();
        boolean repeatedPrefix = false;
        char at;

        ArrayList<Record> recordList = new ArrayList<Record>();
        for (Record record : pr.objectList){
            boolean camp = false;

            //System.out.println(record.name);
            //System.out.println("PR STRING: " + pr.stringPrefix);
            ArrayList<Character> arl = new ArrayList<Character>();
            if (record.name.contains("virginia") && record.name.contains("camp")){
                camp = true;
            }

            repeatedPrefix = false;
            for (String recordName : record.name){
                //If root, first character of first string is current prefix
                if (rootStep){
                    if (!arl.contains(recordName.charAt(0))){
                        arl.add(recordName.charAt(0));
                        if (recordName.length() > 1){
                            record.setString(recordName.substring(0, pr.offset+1));
                            pr.stringPrefix = recordName.substring(0, pr.offset+1);
                        }
                        else{
                            record.setString(recordName.substring(0, 0));
                            pr.stringPrefix = recordName.substring(0, 0);
                        }
                        if (camp){
                            //System.out.println("YEAH " + recordName.charAt(pr.offset));
                        }
                        if (partitioned.get(recordName.charAt(pr.offset)) == null){
                            recordList = new ArrayList<Record>();
                        }
                        else{
                            recordList = partitioned.get(recordName.charAt(pr.offset));
                        }
                        recordList.add(record.clone());
                        partitioned.put(recordName.charAt(pr.offset), recordList);
                    }
                }
                //otherwise, first character of word starting with prefix is current prefix
                else if(recordName.startsWith(pr.stringPrefix)){
                    char use;
                    //words in list start with the same letter
                    // if (!repeatedPrefix){
                    //repeatedPrefix = true;
                    if (pr.offset == recordName.length()){
                        use = recordName.charAt(pr.offset-1);

                        if (camp){
                            //System.out.println("OH RECORDNAME " + recordName + " STRING PREFIX: " + pr.stringPrefix);
                        }

                        //if(!repeatedPrefix){
                        record.setString(recordName);
                        //}
                        if (partitioned.get(use) == null){
                            recordList = new ArrayList<Record>();
                        }
                        else{
                            recordList = partitioned.get(use);
                        }
                        recordList.add(record.clone());
                        partitioned.put(use, recordList);
                    }
                    else if(pr.offset > recordName.length()){

                        //System.out.println("OFFSET IS GREATER FOR RECORD " + record.name + " " + pr.offset);
                        continue;
                    }
                    else{
                        if (camp){
                            //System.out.println("OK RECORDNAME " + recordName + " STRING PREFIX: " + pr.stringPrefix);
                        }
                        //if(!repeatedPrefix){
                        record.setString(recordName.substring(0, pr.offset+1));
                        //}
                        if (partitioned.get(recordName.charAt(pr.offset)) == null){
                            recordList = new ArrayList<Record>();
                        }
                        else{
                            recordList = partitioned.get(recordName.charAt(pr.offset));
                        }
                        recordList.add(record.clone());
                        partitioned.put(recordName.charAt(pr.offset), recordList);
                    }
                    // }
                }
            }
        }
        return partitioned;
    }

    public void printTree(){
        Stack<PrefixRegion> stack = new Stack();
        stack.push(root);

        while(!stack.empty()){
            PrefixRegion pr = stack.pop();

            for (char c : pr.children.keySet()){
                for (PrefixRegion node : pr.children.get(c)){
                    if (!node.objectList.isEmpty()){
                        printObjectList(node);
                    }
                    else{
                        stack.push(node);
                    }
                }
            }
        }
    }

    public void printObjectList(PrefixRegion pr){
        System.out.println("UNDER NODE WITH PREFIX " + pr.charPrefix);
        System.out.println("CONTENTS OF LEAF NODE:");
        for (Record r : pr.objectList){
            System.out.println(r.name);
        }
    }

    public void printPR(PrefixRegion pr, boolean interiorNode){
        if (interiorNode){
            System.out.println("CHILDREN OF NEW INTERIOR NODE WITH STRING PREFIX " +
                    pr.stringPrefix);
            for (char c : pr.children.keySet()){
                for (PrefixRegion ar : pr.children.get(c)){
                    for(Record r : ar.objectList){
                        System.out.println("PREFIX: '" + c + "'  - STRING PREFIX: '" +
                                pr.stringPrefix +
                                "' " + r.name);
                    }
                }
            }
        }
        else{
            System.out.println("OBJECTS IN LEAF:");
            for (Record r : pr.objectList){
                System.out.println("PREFIX: '" + pr.charPrefix + "'  - STRING PREFIX: '" +
                        pr.stringPrefix +
                        "' " + r.name);
            }
        }
    }
}