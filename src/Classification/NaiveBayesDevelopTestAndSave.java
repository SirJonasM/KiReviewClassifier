package Classification;


public class NaiveBayesDevelopTestAndSave {
    public static void main(String[] args) throws Exception {
        Classifier classifier = new Classifier("TrainDataProcessed","DevData");
        classifier.doNaiveBayes();
        DevelopTestAndSaveModel.evaluateAndSave(classifier);
    }


}
