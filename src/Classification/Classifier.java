package Classification;

import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.J48;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;


public class Classifier {
    String nameID;
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
private DataSource testSrc;
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

            testSrc = new DataSource("Data/" + testSet + ".arff");
            testDataSet = testSrc.getDataSet();
            testDataSet.setClassIndex(1);
            j48 = new J48();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Classifier(String testSet, String model,boolean isNaiveBayes) throws Exception {
        testSrc = new DataSource("Data/"+testSet +".arff");
        testMode = true;
        nameID = model;
        if(isNaiveBayes)nameID = "NaiveBayes/"+model;
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
        nameID = name;
        if(isNaiveBayes) nameID = "NaiveBayes/"+name;
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
        //System.out.println(testDataSet);
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
        configureStringToWordVector();
    }
    void configureStringToWordVector() throws Exception {
        stringToWordVector.setOutputWordCounts(true);
        stringToWordVector.setLowerCaseTokens(true);
        stringToWordVector.setTFTransform(true);
        stringToWordVector.setIDFTransform(true);
        stringToWordVector.setInputFormat(trainDataSet);
        stringToWordVector.setWordsToKeep(wordsToKeep);
        stringToWordVector.setTokenizer(new AlphabeticTokenizer());
    }

    String evaluate() throws Exception {
        String evaluation = "";
        if(!testMode) {
            System.out.println("\n\n\n\n------------- Test on Train Dataset ------------- \n");
            Evaluation evalTrain = new Evaluation(trainDataSet);
            if(isNaiveBayes) evalTrain.evaluateModel(naiveBayes,trainDataSet);
            else evalTrain.evaluateModel(fc, trainDataSet);
            evaluation = "------------- Test on Train Dataset ------------- \n" + printEvaluation(evalTrain);
        }
        System.out.println("\n\n\n\n------------- Test on Test Dataset ------------- \n");

        Evaluation evalTest = new Evaluation(testDataSet);
        if(isNaiveBayes) evalTest.evaluateModel(naiveBayes,testDataSet);
        else evalTest.evaluateModel(fc, testDataSet);
        return evaluation + "\n\n\n\n------------- Test on Test Dataset ------------- \n"+printEvaluation(evalTest);
    }

    private String printEvaluation(Evaluation eval) throws Exception {
        String confusionMatrix = eval.toMatrixString("Matrix:");
        String classDetails = eval.toClassDetailsString();
        String summary = eval.toSummaryString();
        System.out.println(confusionMatrix);
        System.out.println(classDetails);
        System.out.println(summary);
        return confusionMatrix + "\n\n"+classDetails+"\n\n" + summary;
    }

    public String getFalsePredictions() throws Exception {
        Evaluation eval = new Evaluation(testDataSet);
        Instances instances = testSrc.getDataSet();
        StringBuilder sB = new StringBuilder();
        for(int i = 0;i<testDataSet.size();i++){

            boolean reviewAndPredictionNeg = (eval.evaluateModelOnce(fc, testDataSet.instance(i))==1.0) && (testDataSet.instance(i).toString().contains("neg"));
            boolean reviewAndPredictionPos = (eval.evaluateModelOnce(fc, testDataSet.instance(i))==0.0) && (!testDataSet.instance(i).toString().contains("neg"));
            if (reviewAndPredictionNeg || reviewAndPredictionPos){
                    continue;
            }
            sB.append(instances.instance(i)).append("\n\n");
        }
        return sB.toString();
    }
}
