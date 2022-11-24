package Classification;

public class LoadAndTestModel {
    public static void main(String [] args) throws Exception {
        Classifier classifier = new Classifier("DevData","JonasMoewes-Tree");
        classifier.useStringToWordVector();
        classifier.evaluate();
    }
}
