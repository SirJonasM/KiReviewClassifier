package Classification;

public class LoadAndTestModel {
    public static void main(String [] args) throws Exception {
        Classifier classifier = new Classifier("TestData","Jonas-M-Classifier");
        classifier.useStringToWordVector();
        classifier.evaluate();
    }
}
