package classes;

import java.io.*;
import java.util.ArrayList;

public class preDataProcessing {
    static BufferedReader reader;
    static BufferedReader positiveWordsReader;
    static BufferedReader negativeWordsReader;

    static BufferedWriter writer;
    static ArrayList<String> positiveWords = new ArrayList<>();
    static ArrayList<String> negativeWords = new ArrayList<>();


    static String header = """
            @relation imdb-sentiment-2011-weka.filters.unsupervised.instance.Resample-S1-Z50.0-weka.filters.unsupervised.instance.Resample-S1-Z10.0-no-replacement-V-weka.filters.unsupervised.instance.Resample-S1-Z10.0-no-replacement-V
                        
            @attribute Text string
            @attribute class-att {pos,neg}
                                          
            @data""";

    public static void main(String[] args) throws IOException {
        reader = new BufferedReader(new FileReader("Data/TrainData.arff"));
        positiveWordsReader = new BufferedReader(new FileReader("Data/positiveWords"));
        negativeWordsReader = new BufferedReader(new FileReader("Data/negativeWords"));
        getWords();
        positiveWordsReader.close();
        negativeWordsReader.close();
        System.out.println(positiveWords.size());
        System.out.println(negativeWords.size());

        writer = new BufferedWriter(new FileWriter("Data/TrainDataProcessed.arff"));
        writer.write(header);

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
        writer.close();
    }

    private static void getWords() throws IOException {
        String word = positiveWordsReader.readLine();
        while(word != null){
            positiveWords.add(word);
            word = positiveWordsReader.readLine();
        }
        word = negativeWordsReader.readLine();
        while(word != null){
            negativeWords.add(word);
            word = negativeWordsReader.readLine();
        }
    }

    private static Review getReview(String line) {
        String reviewText = line.split("'")[1];
        String evaluation = line.substring(line.length() - 3);
        return new Review(reviewText, evaluation);
    }
    private static void writeReview(Review review) throws IOException {
        writer.write(review.toString() + "\n");
    }

    private static void processReview(Review review) {

    }
}
