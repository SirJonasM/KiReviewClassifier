package Classification;

public class LoadAndTestModel {
    public static void main(String [] args) throws Exception {
        Classifier classifier = new Classifier("TestData","trees-Jonas");
        classifier.useStringToWordVector();
        classifier.evaluate();
    }
}
