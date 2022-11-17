package classes;

import java.io.*;

public class preDataProcessing {
    static BufferedReader reader;
    static BufferedWriter writer;


    public static void main(String[] args) throws IOException {
        reader = new BufferedReader(new FileReader("Data/TrainData.arff"));
        writer = new BufferedWriter(new FileWriter("Data/TrainDataProcessed.arff"));
        String line = reader.readLine();
        while(!line.equals("@data"))
            line = reader.readLine();
        line = reader.readLine();
        while(line != null) {
            Review review = getReview(line);
            processReview(review);
            writeReview(review);
            line = reader.readLine();
        }
        reader.close();
        writer.close();
    }

    private static Review getReview(String line) {
        String reviewText = line.split("'")[1];
        String evaluation = line.substring(line.length() - 3);
        return new Review(reviewText, evaluation);
    }
    private static void writeReview(Review review){

    }

    private static void processReview(Review review) {

    }
}
