package Classification;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.J48;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;


public class Classification {
    static Instances trainDataSet;
    static Instances testDataSet;
    static J48 j48;

    static FilteredClassifier fc;

    public static void main(String [] args){
        try{
            DataSource trainData = new DataSource("Data/TrainDataProcessed.arff");
            trainDataSet = trainData.getDataSet();
            trainDataSet.setClassIndex(1);


            DataSource testData = new DataSource("Data/DevData.arff");
            testDataSet = testData.getDataSet();
            testDataSet.setClassIndex(1);

            trainDataSet.setClassIndex(trainDataSet.numAttributes()-1);
            j48 = new J48();

            StringToWordVector stringToWordVector = new StringToWordVector();
            System.out.println(stringToWordVector.isOutputFormatDefined());
            stringToWordVector.setInputFormat(trainDataSet);
            stringToWordVector.setLowerCaseTokens(true);
            stringToWordVector.setWordsToKeep(100);


            fc = new FilteredClassifier();
            fc.setFilter(stringToWordVector);
            fc.setClassifier(j48);
            fc.buildClassifier(trainDataSet);
            System.out.println(fc);


            System.out.println("------------- Train Data Test ------------- \n");
            Evaluation evalTrain =  new Evaluation(trainDataSet);
            evalTrain.evaluateModel(fc,trainDataSet);

            System.out.println(evalTrain.toSummaryString());
            System.out.println("------------- Test Data Test ------------- \n");

            Evaluation evalTest = new Evaluation(testDataSet);
            evalTest.evaluateModel(fc,testDataSet);

            System.out.println(evalTest.toSummaryString());




        }catch (Exception e){
            e.printStackTrace();;
        }
    }
}
