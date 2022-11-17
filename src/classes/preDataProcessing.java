package classes;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern pattern = Pattern.compile("", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(review.text);
        boolean matchFound = matcher.find();
        if(matchFound) {
            //System.out.print("");
        }
    }
}

/*
Datensatz auf relevante Wörter reduzieren
Adjektive
Verben
toLowerCase
<Br>,... entfernen
couldnt
 */
