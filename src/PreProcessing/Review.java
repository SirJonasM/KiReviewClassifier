package PreProcessing;

public class Review {
    static String[] attributes = new String[]{"@attribute Text String",
            "@attribute reviewLength NUMERIC",
            "@attribute satzZeichenAnzahl NUMERIC",
            "@attribute class-att {pos,neg}"};
    String text;
    String evaluation;
    int reviewLength;
    int satzZeichen;

    public Review(String text, String evaluation){
        this.text = text;
        this.evaluation = evaluation;
        this.reviewLength = text.length();
        this.satzZeichen = getSatzzeichenAnzahl(text);

    }

    private int getSatzzeichenAnzahl(String text) {
        int count = 0;
        for(char c : text.toCharArray()){
            if(c == '?' || c == '.' || c == '!' ) count++;
        }
        return count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    @Override
    public String toString() {
        return "'" + text + "'," +reviewLength + "," + satzZeichen +","  + evaluation;
    }
}
