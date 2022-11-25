package Classification;

public class NaiveBayesLoadAndTest {

    public static void main(String[] args) throws Exception {
        Classifier classifier = new Classifier("DevData","test-test",true);
        classifier.evaluate();
    }
}
