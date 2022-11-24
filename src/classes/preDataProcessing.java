package classes;

import java.io.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

//https://gist.github.com/mkulakowski2/4289437
//https://gist.github.com/mkulakowski2/4289441
public class preDataProcessing {
    static BufferedReader reader;
    static BufferedReader positiveWordsReader;
    static BufferedReader negativeWordsReader;

    static BufferedWriter writer;
    static ArrayList<String> positiveWords = new ArrayList<>();
    static ArrayList<String> negativeWords = new ArrayList<>();
    static ArrayList<String> connectWords = new ArrayList<>();
    static String header = "@relation imdb-sentiment-2011-weka.filters.unsupervised.instance.Resample-S1-Z50.0-weka.filters.unsupervised.instance.Resample-S1-Z10.0-no-replacement-V-weka.filters.unsupervised.instance.Resample-S1-Z10.0-no-replacement-V\n"
            +"\n@attribute Text string\n"
            +"@attribute class-att {pos,neg}\n"
            +"\n"
            +"@data\n";


    public static void main(String[] args) throws IOException {
        connectWords.add("dont");
        connectWords.add("didnt");
        connectWords.add("not");
        connectWords.add("wont");


        reader = new BufferedReader(new FileReader("Data/DevData.arff"));
        positiveWordsReader = new BufferedReader(new FileReader("Data/positiveWords"));
        negativeWordsReader = new BufferedReader(new FileReader("Data/negativeWords"));
        getWords();
        positiveWordsReader.close();
        negativeWordsReader.close();
        System.out.println(positiveWords.size());
        System.out.println(negativeWords.size());

        writer = new BufferedWriter(new FileWriter("Data/DevDataProcessed.arff"));
        writer.write(header);

        String line = reader.readLine();
        while(!line.equals("@data"))
            line = reader.readLine();
        line = reader.readLine();

        while(line != null) {
            Review review = getReview(line);
            processReview(review);
            writeReview(review);
            line = reader.readLine();
        }
        writer.close();
    }

    private static void getWords() throws IOException {
        String word = positiveWordsReader.readLine();
        while(word != null){
            positiveWords.add(word);
            word = positiveWordsReader.readLine();
        }
        word = negativeWordsReader.readLine();
        while(word != null){
            negativeWords.add(word);
            word = negativeWordsReader.readLine();
        }
    }

    private static Review getReview(String line) {
        String reviewText = line.split("'")[1];
        String evaluation = line.substring(line.length() - 3);
        return new Review(reviewText, evaluation);
    }
    private static void writeReview(Review review) throws IOException {
        writer.write(review.toString() + "\n");
    }

    private static void processReview(Review review) {
        review.setText(review.getText().toLowerCase());
        String[] text = review.getText().split(" ");
        for(int i = 0;i< text.length;i++){
            if(positiveWords.contains(text[i])){
                continue;
            }
            if(negativeWords.contains(text[i])){
                continue;
            }
            if(text[i].equals("not")){
                i++;
                continue;
            }
            text[i] = "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(text).forEach(a -> {
            if(a.isEmpty()) return;
            if(connectWords.contains(a)) {
                stringBuilder.append(a);
                return;
            }
            stringBuilder.append(a).append(" ");

        });
        review.setText(stringBuilder.toString());
        }
    }


/*
Datensatz auf relevante Wörter reduzieren
Adjektive
Verben
toLowerCase
not ... adjective -> notadjective
<Br>,... entfernen
couldnt
 */
