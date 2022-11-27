package Classification;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetFalsePredictionOfModel {
    static Classifier classifier;
public static void main(String[] args) throws Exception {
    classifier = new Classifier("DevDataProcessed","Abgabe-J48",false);
    classifier.useStringToWordVector();
    String falsePredictions = classifier.getFalsePredictions();
    BufferedWriter writer = new BufferedWriter(new FileWriter("FalsePredictions/falsePredictions.txt"));
    writer.write(falsePredictions);
    }
}