package Classification;

public class NaiveBayesLoadAndTest {

    public static void main(String[] args) throws Exception {
        Classifier classifier = new Classifier("DevData","best-sofar",true);
        String evaluation = classifier.evaluate();
        LoadAndTestModel.appendInfo(evaluation,classifier.nameID);
    }
}
