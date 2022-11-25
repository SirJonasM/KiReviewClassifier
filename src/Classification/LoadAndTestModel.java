package Classification;

public class LoadAndTestModel {
    public static void main(String [] args) throws Exception {
        Classifier classifier = new Classifier("DevData","trees-Jonas");
        classifier.useStringToWordVector();
        classifier.evaluate();
    }
}
