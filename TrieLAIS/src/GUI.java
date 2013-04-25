import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class GUI extends JPanel
implements ActionListener {
    JTextField textField;
    JTextArea textArea;
    JTextArea displayArea;
    static TrieLAIS t = new TrieLAIS();

    static Comparator<RecordLAIS_TRIE> recordComparator = new RecordComparatorLAIS_TRIE();
    static PriorityQueue<RecordLAIS_TRIE> recQ = new PriorityQueue<RecordLAIS_TRIE>(10, recordComparator);
    static ArrayList<RecordLAIS_TRIE> recordList = new ArrayList<RecordLAIS_TRIE>();

    //search variables
    static CoordinatesLAIS_TRIE point = new CoordinatesLAIS_TRIE(-74.378,40.799);
    static int k = 4;

    public GUI() {
        super(new GridBagLayout());
        GridBagLayout gridbag = (GridBagLayout)getLayout();
        GridBagConstraints c = new GridBagConstraints();

        JButton button = new JButton("Clear");
        button.addActionListener(this);

        textField = new JTextField(20);
        textField.addActionListener(new MyTextActionListener());
        textField.getDocument().addDocumentListener(new MyDocumentListener());
        textField.getDocument().putProperty("name", "Text Field");

        textArea = new JTextArea();
        //        textArea.getDocument().addDocumentListener(new MyDocumentListener());
        //        textArea.getDocument().putProperty("name", "Text Area");

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(200, 75));

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane displayScrollPane = new JScrollPane(displayArea);
        displayScrollPane.setPreferredSize(new Dimension(200, 75));

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        gridbag.setConstraints(textField, c);
        add(textField);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        gridbag.setConstraints(scrollPane, c);
        add(scrollPane);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(displayScrollPane, c);
        add(displayScrollPane);

        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.0;
        c.gridheight = 1;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        gridbag.setConstraints(button, c);
        add(button);

        setPreferredSize(new Dimension(450, 250));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    class MyDocumentListener implements DocumentListener {
        final String newline = "\n";

        @Override
        public void insertUpdate(DocumentEvent e) {
            displayArea.setText("");
            //System.out.print(e.getDocument().getLength());
            try {
                updateLog(e, "inserted into");
            } catch (BadLocationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            displayArea.setText("");
            System.out.print(e.getDocument().getLength());
            try {
                updateLog(e, "removed from");
            } catch (BadLocationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events.
        }

        public void updateLog(DocumentEvent e, String action) throws BadLocationException {
            Document doc = e.getDocument();

            if (doc.getLength() > 0){
                Set<TrieLAIS> l = t.search(textField.getText().toLowerCase());
                displayArea.append("words starting from "+  textField.getText() + ":\n");

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
                    if (recQ.isEmpty()){
                        recQ.add(r);
                    }
                    else{
                        if (!r.recName.equals(recQ.peek().recName)){
                            recQ.add(r);
                        }
                    }
                }
                for(int i = 0; i < k; i++){
                    if (i < recQ.size()){
                        displayArea.append(recQ.remove().recName + '\n');
                    }
                }
            }
            recordList.clear();
            recQ.clear();
            displayArea.setCaretPosition(displayArea.getDocument().getLength());
        }
    }

    class MyTextActionListener implements ActionListener {
        /** Handle the text field Return. */
        @Override
        public void actionPerformed(ActionEvent e) {
            int selStart = textArea.getSelectionStart();
            int selEnd = textArea.getSelectionEnd();

            textArea.replaceRange(textField.getText(),
                    selStart, selEnd);
            textArea.append("\n");
            displayArea.setText("");
            textField.setText("");
        }
    }

    /** Handle button click. */
    @Override
    public void actionPerformed(ActionEvent e) {
        displayArea.setText("");
        textField.setText("");
        textField.requestFocus();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Trie Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new GUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IllegalAccessException, IOException {

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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}