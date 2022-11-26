package PreProcessing;

import java.io.*;

public class Main {
    static final String rawDataName = "TrainData";

    public static void main(String[] args) throws IOException {
       externelMain(rawDataName,rawDataName+"Processed");
    }

    public static void externelMain(String rawData, String newData) throws IOException {
        PreDataProcessing preDataProcessing = new PreDataProcessing();
        preDataProcessing.connectWords.add("dont");
        preDataProcessing.connectWords.add("didnt");
        preDataProcessing.connectWords.add("not");
        preDataProcessing.connectWords.add("wont");
        preDataProcessing.reader = new BufferedReader(new FileReader("Data/" + rawData +".arff"));
        preDataProcessing.writer = new BufferedWriter(new FileWriter("Data/" + newData + ".arff"));

        preDataProcessing.getWords();
        String line = preDataProcessing.reader.readLine();
        while(!line.equals("@data"))
            line = preDataProcessing.reader.readLine();
        line = preDataProcessing.reader.readLine();

        preDataProcessing.writer.write(preDataProcessing.header + "\n");
        for(String attribute : Review.attributes){
            preDataProcessing.writer.write(attribute + "\n");
        }
        preDataProcessing.writer.write("\n@data\n");

        while(line != null) {
            Review review = preDataProcessing.getReview(line);
            preDataProcessing.processReview(review);
            preDataProcessing.writeReview(review);
            line = preDataProcessing.reader.readLine();
        }
        preDataProcessing.writer.close();
    }
}
