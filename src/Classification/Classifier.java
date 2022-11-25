package Classification;

import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
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
    NaiveBayesMultinomialText naiveBayes;
    AttributeSelection aSFilter;
    StringToWordVector stringToWordVector;

    FilteredClassifier fc;
    private final boolean  testMode;
    private final boolean  isNaiveBayes;

    public Classifier(int numToSelect, int wordsToKeep, String trainingsSet, String testSet) {
        testMode = false;
        isNaiveBayes = false;
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

    public Classifier(String testSet, String model,boolean isNaiveBayes) {
        testMode = true;
        this.isNaiveBayes = isNaiveBayes;
        try {
            DataSource testData = new DataSource("Data/" + testSet + ".arff");
            testDataSet = testData.getDataSet();
            testDataSet.setClassIndex(1);
            loadModel(model);
            if(!isNaiveBayes) j48 = new J48();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    public Classifier(String trainingSet, String testSet){
        testMode = false;
        isNaiveBayes = true;
        try {
            //Daten einlesen
            DataSource trainData = new DataSource("Data/" + trainingSet + ".arff");
            trainDataSet = trainData.getDataSet();
            trainDataSet.setClassIndex(1);

            DataSource testData = new DataSource("Data/" + testSet + ".arff");
            testDataSet = testData.getDataSet();
            testDataSet.setClassIndex(1);
            this.naiveBayes = new NaiveBayesMultinomialText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadModel(String model) {
        try{
            if(isNaiveBayes) loadNaiveBayes(model);
            else loadFilteredClassifier(model);
        }catch (IOException e) {
            System.out.println("Wahrscheinlich war der Name falsch.");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFilteredClassifier(String model) throws IOException, ClassNotFoundException {
        ObjectInputStream inClassifier = new ObjectInputStream(new FileInputStream("Models/Classifier/"+model));
        fc = (FilteredClassifier) inClassifier.readObject();
        inClassifier.close();

        ObjectInputStream inStringToWordVector = new ObjectInputStream(new FileInputStream("Models/StringToWordVector/" + model));
        stringToWordVector = (StringToWordVector) inStringToWordVector.readObject();
        inStringToWordVector.close();
    }

    private void loadNaiveBayes(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream inClassifier = new ObjectInputStream(new FileInputStream("Models/Classifier/NaiveBayes/" + name));
        naiveBayes = (NaiveBayesMultinomialText) inClassifier.readObject();
    }


    void saveModel(String name) throws IOException {
        if(isNaiveBayes) saveNaiveBayes(name);
        else saveFilteredClassifier(name);

        System.out.println("\n\n------------- Saved Model: " + name + " -------------\n\n");
    }

    private void saveFilteredClassifier(String name) throws IOException {
        ObjectOutputStream outModel = new ObjectOutputStream(new FileOutputStream("Models/Classifier/"+name));
        outModel.writeObject(fc);
        outModel.close();

        ObjectOutputStream outStringToWordVector = new ObjectOutputStream(new FileOutputStream("Models/StringToWordVector/" + name));
        outStringToWordVector.writeObject(stringToWordVector);
        outStringToWordVector.close();
    }

    private void saveNaiveBayes(String name) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Models/Classifier/NaiveBayes/" + name));
        out.writeObject(naiveBayes);
        out.close();
    }

    void useStringToWordVector() throws Exception {
        if(!testMode)  trainDataSet = StringToWordVector.useFilter(trainDataSet, stringToWordVector);
        testDataSet = StringToWordVector.useFilter(testDataSet, stringToWordVector);
        System.out.println(testDataSet);
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
    void doNaiveBayes() throws Exception {
        configureNaiveBayes();
        naiveBayes.buildClassifier(trainDataSet);
        System.out.println(naiveBayes);
    }
    //Methode um den Naive Bayes Classifier zu konfigurieren;
    private void configureNaiveBayes() {
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
            if(isNaiveBayes) evalTrain.evaluateModel(naiveBayes,trainDataSet);
            else evalTrain.evaluateModel(fc, trainDataSet);
            printEvaluation(evalTrain);
        }
        System.out.println("\n\n\n\n------------- Test Data Test ------------- \n");

        Evaluation evalTest = new Evaluation(testDataSet);
        if(isNaiveBayes) evalTest.evaluateModel(naiveBayes,testDataSet);
        else evalTest.evaluateModel(fc, testDataSet);
        printEvaluation(evalTest);
    }

    private void printEvaluation(Evaluation eval) throws Exception {
        System.out.println(eval.toMatrixString("Matrix:"));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toSummaryString());
    }
}
