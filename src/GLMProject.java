import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

//marker compared to marker = percentage of difference.

public class GLMProject {
    HashMap<String, String> markerValues;
    HashMap<String, HashMap<String, String>> compareValuesMap;
    Queue<String> keyQueue = new PriorityQueue<>();
    private void readFile(File inputFile) {
        try {
            markerValues = new HashMap<>();
            Scanner scanner = new Scanner(inputFile);

            String lastKey = "";
            StringBuilder tempBuilder = new StringBuilder();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains(";")) {
                    if (!lastKey.isEmpty()) {
                        markerValues.put(lastKey, tempBuilder.toString());
                        tempBuilder.setLength(0);
                    }
                    keyQueue.add(line);
                    lastKey = line;

                } else if (!line.contains("=")) {
                    tempBuilder.append(line);
                }
            }
            markerValues.put(lastKey, tempBuilder.toString());
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void compareMarkers() {
        compareValuesMap = new HashMap<>();
        DecimalFormat df = new DecimalFormat("0.00");
        HashMap<String, String> tempMap = new HashMap<>();

        while (!keyQueue.isEmpty()) {
            //Gets the keys in stack order.
            String baseKey = keyQueue.poll();

            //Gets the value belonging to the current baseKey.
            String baseValue = markerValues.get(baseKey);

            //Loop used to compare the base value with the other values
            //-belonging to the remaining keys in the stack.
            for (String compareKey : keyQueue) {
                //Value which gets compared with the baseValue.
                String compareValue = markerValues.get(compareKey);

                int differences = 0;
                for (int i = 0; i < compareValue.length(); i++) {
                    if (compareValue.charAt(i) != baseValue.charAt(i)) {
                        differences++;
                    }
                }
                float diffPercentage = (differences / 357f) * 100;
                tempMap.put(compareKey,  df.format(diffPercentage));
            }
            compareValuesMap.put(baseKey, tempMap);
            tempMap = new HashMap<>();
        }
    }
    private void displayComparison(File inputFile) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(inputFile, true));
            for (String key : compareValuesMap.keySet()) {
                bf.write(key);
                bf.newLine();
                for (String compareKey : compareValuesMap.get(key).keySet()) {
                    String value = compareValuesMap.get(key).get(compareKey);
                    value = value.replace(",", " = ");
                    value = "M" + value;

                    bf.write(value);
                    bf.newLine();
                }
                bf.newLine();
            }
            bf.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void execute(File inputFile) {
        readFile(inputFile);
        compareMarkers();

        String filePath = "GLM_output.txt";
        displayComparison(new File(filePath));
    }
}