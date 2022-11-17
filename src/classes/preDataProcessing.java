package classes;

import java.io.*;

public class preDataProcessing {
    static BufferedReader reader;


    public static void main(String[] args) throws IOException {
        reader = new BufferedReader(new FileReader("Data/TrainData.arff"));
        getReview();
        reader.close();
    }

    private static void getReview() {

    }
}
