package Classification;

import java.util.Scanner;

public class DevelopTestAndSaveModel {
    public static void main(String[] args) throws Exception {

        Classifier classifier = new Classifier(120, 500, "TrainDataProcessed", "DevData");
        classifier.doStringToWordVector();

        classifier.useStringToWordVector();

        classifier.doAttributeSelection();

        classifier.doFilteredClassifier();

        evaluateAndSave(classifier);
    }
    static void evaluateAndSave(Classifier classifier) throws Exception {
        classifier.evaluate();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Unter welchem Namen soll Ihr Classifier abgespeichert werden?(nicht speichern -> Enter)");

        String name = scanner.nextLine().trim();
        if(!name.isBlank()) {
            name = name.replace(" ", "-");

            classifier.saveModel(name.replace(".", "-"));
        }
        System.out.println("----------- Programm beendet -------------");
    }
}
