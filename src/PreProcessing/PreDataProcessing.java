package PreProcessing;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;

//https://gist.github.com/mkulakowski2/4289437
//https://gist.github.com/mkulakowski2/4289441
public class PreDataProcessing {
    BufferedReader reader;
    BufferedWriter writer;
     ArrayList<String> positiveWords = new ArrayList<>();
     ArrayList<String> negativeWords = new ArrayList<>();
    ArrayList<String> connectWords = new ArrayList<>();
    String header = "@relation imdb-sentiment-2011-weka.filters.unsupervised.instance.Resample-S1-Z50.0-weka.filters.unsupervised.instance.Resample-S1-Z10.0-no-replacement-V-weka.filters.unsupervised.instance.Resample-S1-Z10.0-no-replacement-V\n";


    public PreDataProcessing() {

    }

    public void getWords() throws IOException {
        BufferedReader positiveWordsReader = new BufferedReader(new FileReader("Data/Words/positiveWords"));
        BufferedReader negativeWordsReader = new BufferedReader(new FileReader("Data/Words/negativeWords"));
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
        positiveWordsReader.close();
        negativeWordsReader.close();
    }

    public Review getReview(String line) {
        String reviewText = line.split("'")[1];
        String evaluation = line.substring(line.length() - 3);
        return new Review(reviewText, evaluation);
    }
    public void writeReview(Review review) throws IOException {
        writer.write(review.toString() + "\n");
    }

    public void processReview(Review review) {
        review.setText(review.getText().toLowerCase());
        String[] text = review.getText().split(" ");
        for(int i = 0;i< text.length;i++){
            if(text[i].endsWith(")") ||text[i].endsWith("}")||text[i].endsWith("]")){
                text[i] = text[i].substring(0,text[i].length()-1);
            }
            if(text[i].endsWith("(") ||text[i].endsWith("[")||text[i].endsWith("{")){
                text[i] = text[i].substring(1);
            }
            if(connectWords.contains(text[i])){
                continue;
            }
            if(positiveWords.contains(text[i])){
                continue;
            }
            if(negativeWords.contains(text[i])) {
                continue;
            }
            text[i] = "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(text).forEach(a -> {
            if(a.isEmpty()) return;
            if(a.equals("not")) {
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
