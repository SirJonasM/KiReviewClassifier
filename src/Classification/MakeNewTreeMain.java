package Classification;

import java.util.Scanner;

public class MakeNewTreeMain {
    public static void main(String[] args) throws Exception {
        Classifier classifier = new Classifier(120, 500, "TrainDataProcessed", "DevData");
        classifier.doStringToWordVector();

        classifier.useStringToWordVector();

        classifier.doAttributeSelection();

        classifier.doFilteredClassifier();

        classifier.evaluate();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unter welchem Ihr Baum abgespeichert werden?(wenn nicht speichern enter drücken)");

        String name = scanner.nextLine().trim();
        classifier.saveModel(name.replace(" ", "-"));

    }
}
