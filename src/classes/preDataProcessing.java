package classes;

import java.io.*;
import java.util.ArrayList;

public class preDataProcessing {
    static BufferedReader reader;
    static BufferedWriter writer;
    static ArrayList<Review> reviews = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        reader = new BufferedReader(new FileReader("Data/TrainData.arff"));
        writer = new BufferedWriter(new FileWriter("Data/TrainDataProcessed.arff"));
        String s = "abcneg";
        getReviews();
        reader.close();
    }

    private static void getReviews() throws IOException {
        String line = reader.readLine();
        while(!line.equals("@data"))
            line = reader.readLine();
        line = reader.readLine();
        while(line != null) {
            Review review = getReview(line);
            processReview(review);
            line = reader.readLine();
        }
        System.out.println(reviews.size());
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
