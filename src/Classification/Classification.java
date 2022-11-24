package Classification;

import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.J48;
import weka.filters.supervised.attribute.AttributeSelection;
import  weka.attributeSelection.InfoGainAttributeEval;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class Classification {
    private static final int WORDS_TO_KEEP = 1000;
    private static final int NUM_TO_SELECT = 120;
    static Instances trainDataSet;
    static Instances testDataSet;
    static J48 j48;
    static AttributeSelection aSFilter;
    static StringToWordVector stringToWordVector;

    static FilteredClassifier fc;

    public static void main(String [] args){
        try{
            //Daten einlesen
            DataSource trainData = new DataSource("Data/TrainDataProcessed.arff");
            trainDataSet = trainData.getDataSet();
            trainDataSet.setClassIndex(1);


            DataSource testData = new DataSource("Data/DevData.arff");
            testDataSet = testData.getDataSet();
            testDataSet.setClassIndex(1);

            j48 = new J48();


            doStringToWordVector();

            useStringToWordVector();

            doAtrributeSelection();

            doFilteredClassifier();



            System.out.println("\n\n\n\n------------- Train Data Test ------------- \n");
            Evaluation evalTrain =  new Evaluation(trainDataSet);
            evalTrain.evaluateModel(fc,trainDataSet);

            printEvaluation(evalTrain);

            System.out.println("\n\n\n\n------------- Test Data Test ------------- \n");

            Evaluation evalTest = new Evaluation(testDataSet);
            evalTest.evaluateModel(fc,testDataSet);
            printEvaluation(evalTest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void useStringToWordVector() throws Exception {
        trainDataSet = StringToWordVector.useFilter(trainDataSet,stringToWordVector);
        testDataSet = StringToWordVector.useFilter(testDataSet,stringToWordVector);
    }

    private static void doFilteredClassifier() throws Exception {
        fc = new FilteredClassifier();
        fc.setFilter(aSFilter);
        fc.setClassifier(j48);
        fc.buildClassifier(trainDataSet);
        System.out.println(fc);

    }

    private static void doAtrributeSelection() {
        aSFilter =new AttributeSelection();
        aSFilter.setEvaluator(new InfoGainAttributeEval());
        Ranker ranker = new Ranker();
        ranker.setThreshold(0.0);
        ranker.setNumToSelect(NUM_TO_SELECT);
        aSFilter.setSearch(ranker);
    }

    private static void doStringToWordVector() throws Exception {
        stringToWordVector = new StringToWordVector();
        System.out.println(stringToWordVector.isOutputFormatDefined());
        stringToWordVector.setOutputWordCounts(true);
        stringToWordVector.setLowerCaseTokens(true);
        stringToWordVector.setInputFormat(testDataSet);
        stringToWordVector.setWordsToKeep(WORDS_TO_KEEP);
        //stringToWordVector.setTokenizer(new AlphabeticTokenizer());
    }

    private static void printEvaluation(Evaluation eval) throws Exception {
        System.out.println(eval.toMatrixString("Matrix:"));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toSummaryString());
    }
}
