package Classification;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

public class DevelopTestAndSaveModel {
    static String name = "";
    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();
        Classifier classifier = new Classifier(10, 11, "TrainDataProcessed", "DevData");
        long setUp = System.nanoTime();
        classifier.doStringToWordVector();

        classifier.useStringToWordVector();
        long stringToWordVector = System.nanoTime();

        classifier.doAttributeSelection();
        long attributeSelection = System.nanoTime();

        classifier.doFilteredClassifier();
        long buildModel = System.nanoTime();
        long evaluate = evaluateAndSave(classifier);

        if(!name.isBlank()){
            try (FileWriter fileWriter = new FileWriter("Models/Info/" + name,true)) {
                fileWriter.write("\n\nTime to SetUp Classifier: " + (setUp-start)/1_000_000/1000.0);
                fileWriter.write("\nTime to filter with String to Word Vector Filter: " + (stringToWordVector-setUp)/1_000_000/1000.0);
                fileWriter.write("\nTime to rank Attributes with AttributeSelection: " + (attributeSelection-stringToWordVector)/1_000_000/1000.0);
                fileWriter.write("\nTime to Build the Model: " + (buildModel-attributeSelection)/1_000_000/1000.0);
                fileWriter.write("\nTime to Test Model: " + (evaluate-buildModel)/1_000_000/1000.);
                fileWriter.write("\nTime to Build and Test Model: "+ (evaluate-start)/1_000_000/1000.);
            }
        }
        System.out.println("Time to SetUp Classifier: " + (setUp-start)/1_000_000/1000.0);
        System.out.println("Time to filter with String to Word Vector Filter: " + (stringToWordVector-setUp)/1_000_000/1000.0);
        System.out.println("Time to rank Attributes with AttributeSelection: " + (attributeSelection-stringToWordVector)/1_000_000/1000.0);
        System.out.println("Time to Build the Model: " + (buildModel-attributeSelection)/1_000_000/1000.0);
        System.out.println("Time to Test Model: " + (evaluate-buildModel)/1_000_000/1000.);
        System.out.println("Time to Build and Test Model: "+ (evaluate-start)/1_000_000/1000.);
    }
    static long evaluateAndSave(Classifier classifier) throws Exception {
        String evaluation = classifier.evaluate();
        long time2 = System.nanoTime();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unter welchem Namen soll Ihr Classifier abgespeichert werden?(nicht speichern -> Enter)");

        name = scanner.nextLine().trim();
        if(!name.isBlank()) {
            name = name.replace(" ", "-");
            name = name.replace(".", "-");
            classifier.saveModel(name);
            try (FileWriter fileWriter = new FileWriter("Models/Info/" + classifier.nameID)) {
                BufferedWriter writer = new BufferedWriter(fileWriter);
                writer.write(evaluation);
                writer.close();
            }

        }
        System.out.println("----------- Programm beendet -------------");
        return time2;
    }
}
