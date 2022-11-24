package Classification;

import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.J48;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;


public class Classifier {
    int wordsToKeep;
    int numToSelect;
    Instances trainDataSet;
    Instances testDataSet;
    J48 j48;
    AttributeSelection aSFilter;
    StringToWordVector stringToWordVector;

    FilteredClassifier fc;
    private final boolean  testMode;

    public Classifier(int numToSelect, int wordsToKeep, String trainingsSet, String testSet) {
        testMode = false;
        this.numToSelect = numToSelect;
        this.wordsToKeep = wordsToKeep;
        try {
            //Daten einlesen
            DataSource trainData = new DataSource("Data/" + trainingsSet + ".arff");
            trainDataSet = trainData.getDataSet();
            trainDataSet.setClassIndex(1);

            DataSource testData = new DataSource("Data/" + testSet + ".arff");
            testDataSet = testData.getDataSet();
            testDataSet.setClassIndex(1);
            j48 = new J48();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Classifier(String testSet, String model) {
        testMode = true;
        try {
            DataSource testData = new DataSource("Data/" + testSet + ".arff");
            testDataSet = testData.getDataSet();
            testDataSet.setClassIndex(1);
            loadModel(model);
            j48 = new J48();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void loadModel(String model) {
        try{
            ObjectInputStream inClassifier = new ObjectInputStream(new FileInputStream("Models/Classifier/" + model));
            fc = (FilteredClassifier) inClassifier.readObject();
            inClassifier.close();
            ObjectInputStream inStringToWordVector = new ObjectInputStream(new FileInputStream("Models/StringToWordVector/" + model));
            stringToWordVector = (StringToWordVector) inStringToWordVector.readObject();
            inStringToWordVector.close();
        }catch (IOException e) {
            System.out.println("Wahrscheinlich war der Name falsch.");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    void saveModel(String name) throws IOException {
        ObjectOutputStream outModel = new ObjectOutputStream(new FileOutputStream("Models/Classifier/" + name));
        outModel.writeObject(fc);
        ObjectOutputStream outStringToWordVector = new ObjectOutputStream(new FileOutputStream("Models/StringToWordVector/" + name)) ;
        outStringToWordVector.writeObject(stringToWordVector);

        outModel.close();
        System.out.println("\n\n------------- Saved Model: " + name + " -------------\n\n");
    }

    void useStringToWordVector() throws Exception {
        if(!testMode)  trainDataSet = StringToWordVector.useFilter(trainDataSet, stringToWordVector);
        testDataSet = StringToWordVector.useFilter(testDataSet, stringToWordVector);
    }

    void doFilteredClassifier() throws Exception {
        fc = new FilteredClassifier();
        fc.setFilter(aSFilter);
        fc.setClassifier(j48);
        fc.buildClassifier(trainDataSet);
        System.out.println(fc);
    }

    void doAttributeSelection() {
        aSFilter = new AttributeSelection();
        aSFilter.setEvaluator(new InfoGainAttributeEval());
        Ranker ranker = new Ranker();
        ranker.setThreshold(0.0);
        ranker.setNumToSelect(numToSelect);
        aSFilter.setSearch(ranker);
    }

    void doStringToWordVector() throws Exception {
        stringToWordVector = new StringToWordVector();
        stringToWordVector.setOutputWordCounts(true);
        stringToWordVector.setLowerCaseTokens(true);
        stringToWordVector.setInputFormat(testDataSet);
        stringToWordVector.setWordsToKeep(wordsToKeep);
        //stringToWordVector.setTokenizer(new AlphabeticTokenizer());
    }

    void evaluate() throws Exception {
        if(!testMode) {
            System.out.println("\n\n\n\n------------- Train Data Test ------------- \n");
            Evaluation evalTrain = new Evaluation(trainDataSet);
            evalTrain.evaluateModel(fc, trainDataSet);
            printEvaluation(evalTrain);
        }
        System.out.println("\n\n\n\n------------- Test Data Test ------------- \n");

        Evaluation evalTest = new Evaluation(testDataSet);
        evalTest.evaluateModel(fc, testDataSet);
        printEvaluation(evalTest);
    }

    private void printEvaluation(Evaluation eval) throws Exception {
        System.out.println(eval.toMatrixString("Matrix:"));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toSummaryString());
    }
}
