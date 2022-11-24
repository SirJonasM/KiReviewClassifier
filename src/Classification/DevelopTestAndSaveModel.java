package Classification;

import java.util.Scanner;

public class DevelopTestAndSaveModel {
    public static void main(String[] args) throws Exception {

        Classifier classifier = new Classifier(20, 100, "TrainDataProcessed", "DevData");
        classifier.doStringToWordVector();

        classifier.useStringToWordVector();

        classifier.doAttributeSelection();

        classifier.doFilteredClassifier();

        classifier.evaluate();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Unter welchem Ihr Baum abgespeichert werden?(wenn nicht speichern Enter drücken)");

        String name = scanner.nextLine().trim();
        if(!name.isBlank()) {
            name = name.replace(" ", "-");

            classifier.saveModel(name.replace(".", "-"));
        }
        System.out.println("----------- Programm beendet -------------");
    }
}
