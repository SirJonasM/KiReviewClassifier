package PreProcessing;

import java.io.*;

public class Main {
    static final String rawDataName = "DevData2";

    public static void main(String[] args) throws IOException {
        PreDataProcessing preDataProcessing = new PreDataProcessing();
        preDataProcessing.connectWords.add("dont");
        preDataProcessing.connectWords.add("didnt");
        preDataProcessing.connectWords.add("not");
        preDataProcessing.connectWords.add("wont");
        preDataProcessing.reader = new BufferedReader(new FileReader("Data/" + rawDataName +".arff"));
        preDataProcessing.writer = new BufferedWriter(new FileWriter("Data/" + rawDataName + "Processed.arff"));

        preDataProcessing.getWords();
        String line = preDataProcessing.reader.readLine();
        while(!line.equals("@data"))
            line = preDataProcessing.reader.readLine();
        line = preDataProcessing.reader.readLine();

        preDataProcessing.writer.write(preDataProcessing.header);

        while(line != null) {
            Review review = preDataProcessing.getReview(line);
            preDataProcessing.processReview(review);
            preDataProcessing.writeReview(review);
            line = preDataProcessing.reader.readLine();
        }
        preDataProcessing.writer.close();


    }
}
