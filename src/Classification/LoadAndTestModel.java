package Classification;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LoadAndTestModel {
    static Classifier classifier;
    public static void main(String [] args) throws Exception {
        classifier = new Classifier("DevDataProcessed","moin",false);
        classifier.useStringToWordVector();
        long time1 = System.currentTimeMillis();
        String evaluation = classifier.evaluate();
        System.out.println("Time to Test Model: " + (System.currentTimeMillis()-time1)/1000/1000.);
        evaluation += "\nTime to Test Model: " + (System.currentTimeMillis()-time1)/1000/1000.;
        appendInfo(evaluation);

    }
    static void appendInfo(String info){
        try(FileWriter fileWriter = new FileWriter("Models/Info/"+classifier.nameID,true)){
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(info);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
