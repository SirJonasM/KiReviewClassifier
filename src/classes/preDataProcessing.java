package classes;

import java.io.*;
import java.util.ArrayList;

public class preDataProcessing {
    static BufferedReader reader;
    static ArrayList<Review> reviews = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        reader = new BufferedReader(new FileReader("Data/TrainData.arff"));
        String s = "abcneg";
        getReviews();
        reader.close();
    }

    private static void getReviews() throws IOException {
        String line = reader.readLine();
        while(!line.equals("@data"))
            line = reader.readLine();
        line = reader.readLine();
        StringBuilder reviewText = new StringBuilder();
        int countLine = 0;
        int n = 1;
        while(line != null) {
            reviewText.append(line.split("'")[1]);
            String evaluation = line.substring(line.length() - 3);
            Review review = new Review(reviewText.toString(), evaluation);
            line = reader.readLine();
            countLine++;
            if(countLine>=1_000*n){
                System.out.println(countLine);
                n++;
            }

        }
        System.out.println(reviews.size());
    }

    private static void getReview() {

    }
}
